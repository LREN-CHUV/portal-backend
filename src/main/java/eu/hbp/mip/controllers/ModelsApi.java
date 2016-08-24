/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip.controllers;

import com.github.slugify.Slugify;
import eu.hbp.mip.configuration.SecurityConfiguration;
import eu.hbp.mip.model.Filter;
import eu.hbp.mip.model.Model;
import eu.hbp.mip.model.User;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.ConfigRepository;
import eu.hbp.mip.repositories.ModelRepository;
import eu.hbp.mip.utils.CSVUtil;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import eu.hbp.mip.repositories.DatasetRepository;
import eu.hbp.mip.repositories.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/models", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/models", description = "the models API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class ModelsApi {

    private static final Logger LOGGER = Logger.getLogger(ModelsApi.class);

    @Autowired
    SecurityConfiguration securityConfiguration;

    @Autowired
    CSVUtil csvUtil;

    @Autowired
    DatasetRepository datasetRepository;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    QueryRepository queryRepository;

    @Autowired
    ConfigRepository configRepository;

    @ApiOperation(value = "Get models", response = Model.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable> getModels(
            @ApiParam(value = "Max number of results") @RequestParam(value = "limit", required = false) Integer limit,
            @ApiParam(value = "Only ask own models") @RequestParam(value = "own", required = false) Boolean own,
            @ApiParam(value = "Only ask models from own team") @RequestParam(value = "team", required = false) Boolean team,
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
                m.setDataset(datasetRepository.findOne(m.getDataset().getCode()));
                if(valid != m.getValid())
                {
                    i.remove();
                }
            }
        }

        return new ResponseEntity<List<Model>>(HttpStatus.OK).ok(models);

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

        long count = 1;
        for(int i = 1; count > 0; i++)
        {
            count = modelRepository.countByTitle(model.getTitle());

            if(count > 0)
            {
                String title = model.getTitle();
                if(i > 1)
                {
                    title = title.substring(0, title.length()-4);
                }
                model.setTitle(title + " (" + i + ")");
            }
        }

        String slug = null;
        try {
            slug = new Slugify().slugify(model.getTitle());
        } catch (IOException e) {
            slug = "";
            LOGGER.trace(e);
        }

        boolean alreadyExists = true;
        for(int i = 1; alreadyExists; i++)
        {
            alreadyExists = modelRepository.exists(slug);
            if(alreadyExists)
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

        configRepository.save(model.getConfig());
        queryRepository.save(model.getQuery());
        datasetRepository.save(model.getDataset());
        modelRepository.save(model);

        LOGGER.info("Model saved (also saved model.config, model.query and model.dataset)");

        return new ResponseEntity<Model>(HttpStatus.CREATED).ok(model);
    }

    @ApiOperation(value = "Get a model", response = Model.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found"), @ApiResponse(code = 404, message = "Not found") })
    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public ResponseEntity<Model> getAModel(
            @ApiParam(value = "slug", required = true) @PathVariable("slug") String slug
    )  {
        LOGGER.info("Get a model");

        User user = securityConfiguration.getUser();

        Model model = null;

        model = modelRepository.findOne(slug);
        if (!model.getValid() && !model.getCreatedBy().getUsername().equals(user.getUsername()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<String> yAxisVars = configRepository.findOne(model.getConfig().getId()).getyAxisVariables();
        Collection<String> yAxisVarsColl = new LinkedHashSet<>(yAxisVars);
        model.getConfig().setyAxisVariables(new LinkedList<>(yAxisVarsColl));

        List<Variable> varsQuery = queryRepository.findOne(model.getQuery().getId()).getVariables();
        Collection<Variable> varsQueryColl = new LinkedHashSet<>(varsQuery);
        model.getQuery().setVariables(new LinkedList<>(varsQueryColl));

        List<Variable> grpgsQuery = queryRepository.findOne(model.getQuery().getId()).getGrouping();
        Collection<Variable> grpgsQueryColl = new LinkedHashSet<>(grpgsQuery);
        model.getQuery().setGrouping(new LinkedList<>(grpgsQueryColl));

        List<Variable> covarsQuery = queryRepository.findOne(model.getQuery().getId()).getCovariables();
        Collection<Variable> covarsQueryColl = new LinkedHashSet<>(covarsQuery);
        model.getQuery().setCovariables(new LinkedList<>(covarsQueryColl));

        List<Filter> fltrs = queryRepository.findOne(model.getQuery().getId()).getFilters();
        Collection<Filter> fltrsColl = new LinkedHashSet<>(fltrs);
        model.getQuery().setFilters(new LinkedList<>(fltrsColl));

        List<String> varsDS = datasetRepository.findOne(model.getDataset().getCode()).getVariable();
        Collection<String> varsDSColl = new LinkedHashSet<>(varsDS);
        model.getDataset().setVariable(new LinkedList<>(varsDSColl));

        List<String> grpgsDS = datasetRepository.findOne(model.getDataset().getCode()).getGrouping();
        Collection<String> grpgsDSColl = new LinkedHashSet<>(grpgsDS);
        model.getDataset().setGrouping(new LinkedList<>(grpgsDSColl));

        List<String> headersDS = datasetRepository.findOne(model.getDataset().getCode()).getHeader();
        Collection<String> headersDSColl = new LinkedHashSet<>(headersDS);
        model.getDataset().setHeader(new LinkedList<>(headersDSColl));

        return new ResponseEntity<>(HttpStatus.OK).ok(model);
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

        model.setTitle(model.getConfig().getTitle().get("text"));

        Model oldModel = modelRepository.findOne(slug);

        String author = oldModel.getCreatedBy().getUsername();

        if(!user.getUsername().equals(author))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String oldTitle = oldModel.getTitle();
        String newTitle = model.getTitle();

        if(!newTitle.equals(oldTitle)) {
            long count = 1;
            for(int i = 1; count > 0 && !newTitle.equals(oldTitle); i++)
            {
                newTitle = model.getTitle();
                count = modelRepository.countByTitle(newTitle);
                if (count > 0 && !newTitle.equals(oldTitle)) {
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

        LOGGER.info("Model updated (also saved/updated model.config, model.query and model.dataset)");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static String randomStr(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }


}
