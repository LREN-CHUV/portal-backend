package eu.hbp.mip;

import eu.hbp.mip.controllers.DatasetsApi;
import eu.hbp.mip.model.Variable;
import eu.hbp.mip.repositories.VariableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartupTasks implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupTasks.class);

    @Autowired
    private VariableRepository variableRepository;

    @Autowired
    private DatasetsApi datasetsApi;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Pre-fill the local variable repository with the list of datasets, interpreted here as variables
        // (a bit like a categorical variable can be split into a set of variables (a.k.a one hot encoding in Data science) )
        try {
            for (ch.chuv.lren.woken.messages.datasets.Dataset dataset: datasetsApi.fetchDatasets()) {
                final String code = dataset.dataset().code();
                Variable v = variableRepository.findOne(code);
                if (v == null) {
                    v = new Variable(code);
                    variableRepository.save(v);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Cannot initialise the variable repository. Is the connection to Woken working?", e);
        }

        LOGGER.info("MIP Portal backend is ready!");
    }
}
