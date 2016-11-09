/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;

import com.github.slugify.Slugify;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.Model;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.*;
import eu.hbp.mip.utils.DataUtil;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/models", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/models", description = "the models API")
public class ModelsApi {

    private static final Logger LOGGER = Logger.getLogger(ModelsApi.class);

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private VariableRepository variableRepository;

    @Autowired
    @Qualifier("dataUtil")
    private DataUtil dataUtil;


    @ApiOperation(value = "Get models", response = Model.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List> getModels(
            @ApiParam(value = "Only ask own models") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask published models") @RequestParam(value = "valid", required = false) Boolean valid
    )  {
        LOGGER.info("Get models");

        User user = securityConfiguration.getUser();

        Iterable<Model> models = null;
        if(own != null && own)
        {
            models = modelRepository.findByCreatedByOrderByCreatedAt(user);
        }
        else
        {
            models = modelRepository.findByValidOrCreatedByOrderByCreatedAt(true, user);
        }

        if(valid != null && models != null)
        {
            for (Iterator<Model> i = models.iterator(); i.hasNext(); )
            {
                Model m = i.next();
                if(valid != m.getValid())
                {
                    i.remove();
                }
            }
        }

        List<Object> modelsList = new LinkedList<>();
        for (Iterator<Model> i = models.iterator(); i.hasNext(); )
        {
            Model m = i.next();
            m.setDataset(datasetRepository.findOne(m.getDataset().getCode()));
            modelsList.add(getModelWithDataset(m));
        }

        return new ResponseEntity<List<Model>>(HttpStatus.OK).ok(modelsList);

    }


    @ApiOperation(value = "Create a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Model created") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Model> addAModel(
            @RequestBody @ApiParam(value = "Model to create", required = true) Model model
    )  {

        LOGGER.info("Create a model");

        User user = securityConfiguration.getUser();

        model.setTitle(model.getConfig().getTitle().get("text"));
        model.setCreatedBy(user);
        model.setCreatedAt(new Date());
        if(model.getValid() == null)
        {
            model.setValid(false);
        }

        // Ensure the title is unique
        boolean titleExists = true;
        for(int i = 1; titleExists; i++)
        {
            String title = model.getTitle();
            titleExists = modelRepository.countByTitle(title) > 0;
            if(titleExists)
            {
                if(i > 1)
                {
                    title = title.substring(0, title.length()-4);
                }
                model.setTitle(title + " (" + i + ")");
            }
        }

        // Create slug from title
        String slug = null;
        try {
            slug = new Slugify().slugify(model.getTitle());
        } catch (IOException e) {
            slug = "";  // Should never happen
            LOGGER.trace(e);
        }

        // Ensure slug is unique
        boolean slugExists = true;
        for(int i = 1; slugExists; i++)
        {
            slugExists = modelRepository.exists(slug);
            if(slugExists)
            {
                if(i > 1)
                {
                    slug = slug.substring(0, slug.length()-2);
                }
                slug += "-"+i;
            }
            model.setSlug(slug);
        }

        Map<String, String> map = new HashMap<>(model.getConfig().getTitle());
        map.put("text", model.getTitle());
        model.getConfig().setTitle(map);

        for (Variable var : model.getQuery().getVariables())
        {
            variableRepository.save(var);
        }
        for (Variable var : model.getQuery().getCovariables())
        {
            variableRepository.save(var);
        }
        for (Variable var : model.getQuery().getGrouping())
        {
            variableRepository.save(var);
        }

        configRepository.save(model.getConfig());
        queryRepository.save(model.getQuery());
        datasetRepository.save(model.getDataset());
        modelRepository.save(model);

        LOGGER.info("Model saved (also saved model.config and model.query)");

        return new ResponseEntity<Model>(HttpStatus.CREATED).ok(model);
    }

    @ApiOperation(value = "Get a model", response = Model.class)
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Model> getAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    )  {
        LOGGER.info("Get a model");

        User user = securityConfiguration.getUser();

        Model model = modelRepository.findOne(slug);

        if(model == null)
        {
            LOGGER.warn("Cannot find model : " + slug);
            return ResponseEntity.badRequest().body(null);
        }

        if (!model.getValid() && !model.getCreatedBy().getUsername().equals(user.getUsername()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<String> yAxisVars = configRepository.findOne(model.getConfig().getId()).getyAxisVariables();
        Collection<String> yAxisVarsColl = new LinkedHashSet<>(yAxisVars);
        model.getConfig().setyAxisVariables(new LinkedList<>(yAxisVarsColl));

        return new ResponseEntity<>(HttpStatus.OK).ok(getModelWithDataset(model));
    }


    @ApiOperation(value = "Update a model", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Model updated") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Model to update", required = true) Model model
    )  {
        LOGGER.info("Update a model");

        User user = securityConfiguration.getUser();
        Model oldModel = modelRepository.findOne(slug);

        if(!user.getUsername().equals(oldModel.getCreatedBy().getUsername()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        model.setTitle(model.getConfig().getTitle().get("text"));

        String oldTitle = oldModel.getTitle();
        String newTitle = model.getTitle();

        // If title has been updated, ensure it is unique
        if(!newTitle.equals(oldTitle)) {
            boolean newTitleExists = true;
            for(int i = 1; newTitleExists && !newTitle.equals(oldTitle); i++)
            {
                newTitle = model.getTitle();
                newTitleExists = modelRepository.countByTitle(newTitle) > 0;
                if (newTitleExists && !newTitle.equals(oldTitle)) {
                    if (i > 1) {
                        newTitle = newTitle.substring(0, newTitle.length() - 4);
                    }
                    model.setTitle(newTitle + " (" + i + ")");
                }
            }
        }

        Map<String, String> map = new HashMap<>(model.getConfig().getTitle());
        map.put("text", model.getTitle());
        model.getConfig().setTitle(map);

        configRepository.save(model.getConfig());
        queryRepository.save(model.getQuery());
        datasetRepository.save(model.getDataset());
        modelRepository.save(model);

        LOGGER.info("Model updated (also saved/updated model.config and model.query)");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Model getModelWithDataset(Model model)
    {
        List<String> allVars = new LinkedList<>();
        allVars.addAll(model.getDataset().getVariable());
        allVars.addAll(model.getDataset().getHeader());
        allVars.addAll(model.getDataset().getGrouping());

        Gson gson = new Gson();
        JsonObject jsonModel = gson.fromJson(gson.toJson(model, Model.class), JsonObject.class);
        jsonModel.get("dataset").getAsJsonObject()
                .add("data", dataUtil.getDataFromVariables(allVars));

        return gson.fromJson(jsonModel, Model.class);
    }

}
