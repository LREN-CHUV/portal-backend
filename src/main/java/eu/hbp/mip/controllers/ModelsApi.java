/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;

import ch.chuv.lren.mip.portal.WokenConversions;
import ch.chuv.lren.woken.messages.query.filters.FilterRule;
import com.github.slugify.Slugify;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import eu.hbp.mip.model.Model;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.UserInfo;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.*;
import eu.hbp.mip.utils.DataUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.Option;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/models", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/models", description = "the models API")
public class ModelsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsApi.class);

    @Autowired
    private UserInfo userInfo;

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

        User user = userInfo.getUser();

        Iterable<Model> models;
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
        models = models != null ? models : new LinkedList<>();
        for (Model m : models) {
            m.setDataset(datasetRepository.findOne(m.getDataset().getCode()));
            modelsList.add(getModelWithDataset(m));
        }

        return ResponseEntity.ok(modelsList);
    }


    @ApiOperation(value = "Create a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Model created") })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Model> addAModel(
            @RequestBody @ApiParam(value = "Model to create", required = true) Model model
    )  {

        LOGGER.info("Create a model");

        User user = userInfo.getUser();

        model.setTitle(model.getConfig().getTitle().get("text"));
        model.setCreatedBy(user);
        model.setCreatedAt(new Date());
        if(model.getValid() == null)
        {
            model.setValid(false);
        }

        ensureTitleUniqueness(model);
        ensureSlugUniqueness(model);

        Map<String, String> map = new HashMap<>(model.getConfig().getTitle());
        map.put("text", model.getTitle());
        model.getConfig().setTitle(map);

        saveVariables(model.getQuery().getVariables());
        saveVariables(model.getQuery().getCovariables());
        saveVariables(model.getQuery().getGrouping());

        configRepository.save(model.getConfig());
        queryRepository.save(model.getQuery());
        if (model.getDataset() != null) {
            datasetRepository.save(model.getDataset());
        }
        modelRepository.save(model);

        LOGGER.info("Model saved (also saved model.config and model.query)");

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    private void saveVariables(@RequestBody @ApiParam(value = "Model to create", required = true) List<Variable> variables) {
        for (Variable var : variables)
        {
            variableRepository.save(var);
        }
    }

    private void ensureSlugUniqueness(@RequestBody @ApiParam(value = "Model to create", required = true) Model model) {
        String slug = createSlug(model.getTitle());
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
    }

    private String createSlug(@RequestBody @ApiParam(value = "Model to create", required = true) String title) {
        String slug;
        try {
            slug = new Slugify().slugify(title);
        } catch (IOException e) {
            slug = "";  // Should never happen
            LOGGER.trace("Cannot slugify title", e);
        }
        return slug;
    }

    private void ensureTitleUniqueness(@RequestBody @ApiParam(value = "Model to create", required = true) Model model) {
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
    }

    @ApiOperation(value = "Get a model", response = Model.class)
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Model> getAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    )  {
        LOGGER.info("Get a model");

        User user = userInfo.getUser();

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

        return ResponseEntity.ok(getModelWithDataset(model));
    }


    @ApiOperation(value = "Update a model", response = Void.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Model updated") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug,
            @RequestBody @ApiParam(value = "Model to update", required = true) Model model
    )  {
        LOGGER.info("Update a model");

        User user = userInfo.getUser();
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

        WokenConversions conv = new WokenConversions();
        String filtersJson = model.getQuery().getFilters();
        Option<FilterRule> filters = conv.toFilterRule(filtersJson);
        String filtersSQL = conv.toSqlWhere(filters);

        Gson gson = new Gson();
        JsonObject jsonModel = gson.fromJson(gson.toJson(model, Model.class), JsonObject.class);
        jsonModel.get("dataset").getAsJsonObject()
                .add("data", dataUtil.getDataFromVariables(allVars, filtersSQL));

        return gson.fromJson(jsonModel, Model.class);
    }

}
