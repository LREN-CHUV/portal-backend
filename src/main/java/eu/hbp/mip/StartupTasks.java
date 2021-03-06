package eu.hbp.mip;

import ch.chuv.lren.woken.messages.datasets.Dataset;
import com.google.gson.Gson;
import eu.hbp.mip.controllers.DatasetsApi;
import eu.hbp.mip.controllers.MiningApi;
import eu.hbp.mip.controllers.VariablesApi;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.VariableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


@Component
@DependsOn("wokenCluster")
public class StartupTasks implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupTasks.class);
    private static final Gson gson = new Gson();

    @Autowired
    private VariableRepository variableRepository;

    @Autowired
    private DatasetsApi datasetsApi;

    @Autowired
    private VariablesApi variablesApi;

    @Autowired
    private MiningApi miningApi;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        boolean variablesRepositoryOk = false;

        // Pre-fill the local variable repository with the list of datasets, interpreted here as variables
        // (a bit like a categorical variable can be split into a set of variables (a.k.a one hot encoding in Data science) )
        // Try 5 times, to be more robust in the face of cluster failures / slow startup
        LOGGER.info("Prefill variable repository with datasets...");
        for (int i = 0; i < 5; i++) {
            try {
                StringBuilder fetchedDatasets = new StringBuilder();
                for (Dataset dataset : datasetsApi.fetchDatasets()) {
                    final String code = dataset.id().code();
                    fetchedDatasets.append(code).append(' ');
                    Variable v = variableRepository.findOne(code);
                    if (v == null) {
                        LOGGER.info("Store additional variable {}", code);
                        v = new Variable(code);
                        variableRepository.save(v);
                    }
                }
                LOGGER.info("Datasets fetched from Woken: " + fetchedDatasets.toString());
                variablesRepositoryOk = true;
                break;
            } catch (Exception e) {
                variablesRepositoryOk = false;
                LOGGER.error("Cannot initialise the variable repository. Is the connection to Woken working?", e);
            }
        }

        if (!variablesRepositoryOk) {
            System.exit(1);
        }

        /*
        for (String variableJson: variablesApi.loadVariables()) {
            String code = gson.fromJson(variableJson, Variable.class).getCode();
            MiningQuery histogram = new MiningQuery();
            histogram.setAlgorithm(new Algorithm("histogram", "histogram", false));
            histogram.setVariables(Collections.singletonList(new Variable(code)));
            histogram.setCovariables(Collections.emptyList());
            histogram.setGrouping(Collections.emptyList());
            // TODO: need to get groupings from Woken
        }
        */

        LOGGER.info("[OK] MIP Portal backend is ready!");
    }

}
