/**
 * Created by Michael DESIGAUD on 10/09/2015.
 */
angular.module('chuvApp.login').controller('LoginController', ['$scope', '$translatePartialLoader', '$translate', '$rootScope', '$state', 'User', 'backendUrl',
    function ($scope, $translatePartialLoader, $translate, $rootScope, $state, User, backendUrl) {
      $translatePartialLoader.addPart('login');
      $translate.refresh();

      $scope.username = null;
      $scope.password = null;
      $scope.error = false;

      $scope.currentDescription = {title: "Select a description", content: ""};
      $scope.stackSrc = "stack.png";

      var descriptions = new Array;
      descriptions['Body'] = "Medical research has identified over five hundred brain diseases, ranging from migraine and addiction to depression and Alzheimer’s. These diseases are usually diagnosed in terms of symptoms and syndromes, i.e. their subjective and objective signs on the body.<br /><br />Such approach, however, makes it very difficult to produce correct diagnoses, or even to select patients for clinical trials. To prevent and cure brain disease, researchers need to understand their underlying causes. This means we need to move beyond isolated studies of individual disorders and investigate brain diseases systematically, identifying key similarities and distinctions, creating new biologically grounded classifications, and understanding the complex disease processes leading from changes at lower levels of brain organisation to clinical symptoms.";
      descriptions['Whole Brain'] = "Multi-modal sensory physiology and anatomy map how different brain regions come together to shape perception-action and our sense of body ownership, awareness and consciousness, and can help validate human brain models. Whole brain synchrotron scans reveal the vasculature supporting cognition, and provide constraints for models of blood flow and brain metabolism.<br /><br />New techniques of ultra-microscopy can create detailed 3D pictures of whole animal brains, tracing the circuits involved in specific functions. Rapid improvements in imaging technology have made it possible to image the structure of the brain at ever higher resolution, to map the fibres linking different areas, and to elucidate the neuronal circuitry responsible for specific functions. ";
      descriptions['Brain Regions'] = "Structural and functional MRI yield dimensions of brain regions which can be used to build models. Region-specific cellular architecture and densities constrain the cellular composition of model regions. Receptor, ion channel, signalling and other protein distributions further constrain neurochemical organisation within and across brain regions. Correlations between protein distributions, cognitive circuits and genomic variability point to neural mechanisms of cognition, provide global constraints for detailed brain models and generate data for model validation.";
      descriptions['Microcircuits'] = "Microcircuits, functional modules that act as elementary processing units bridging single cells to systems and behavior, could provide the link between neurons and global brain function. Microcircuits are designed to serve particular functions. The cellular and molecular composition of microcircuits supports their role in cognition. Single-cell gene expression yields sets of genes that form different types of neurons and glia and determine their morphological and electrical properties. Global brain maps of gene and protein distributions constrain the cellular composition of microcircuit models. Cell geometry and synaptic selection rules constrain local synaptic connectivity in microcircuit models. Electrophysiology, multi-electrode recordings, voltage sensitive dye mapping and optogenetic studies provide data to validate microcircuit models.\n\nReference for the first two sentences: https://mitpress.mit.edu/books/microcircuits";
      descriptions['Cells'] = "The brain and spinal cord are made up of many cells, including neurons and glial cells. 3D reconstruction of the anatomy of single cells yields the structural geometry needed to establish the morphological properties of different cell types. Correlations between gene expression and the geometric properties of cells constrain the artificial synthesis of cellular morphologies from gene expression patterns, as well as models of morphological plasticity. Single-cell gene expression, combined with general rules for the production and distribution of proteins and for protein interactions, constrain molecularly detailed models of neurons and glia.";
      descriptions['Synapses'] = "Synapse activity is central to understanding the mechanisms of learning and memory. Physiological, biophysical and imaging studies of synaptic transmission, plasticity and neuromodulation constrain synaptic models. Pair-wise single cell gene expression constrains the repertoire of synaptic models at the synapses between pairs of neurons of known type, making it possible to model synapse activity. The dynamics of single cell gene expression constrain long-term molecular changes in synapses in response to environmental stimuli.";
      descriptions['Chromosomes'] = "Recognising how a gene defect works through the different levels to produce major disease will be a crucial step in understanding the “bridging laws” linking the physiological properties of neuronal circuitry to specific cognitive and behavioural competencies. Combined with data on the genes expressed in single cells, sequencing technology has made it feasible to sequence whole genomes and to generate 3D maps of gene expression. The state of the chromosome reflects when genes are active or inactive and constrains gene network models. It is likely that the genome constrains the number of genetic cell types in the brain, the size of brain regions, connectivity between brain regions, and total brain size. It may also predict cognitive functions, behavioural traits, epigenetic vulnerability, and brain disorders.";
      descriptions['Proteins'] = "The number and different types of proteins cells produce, the different parts of the cell where they are located, and their respective life cycles all constrain how many and which proteins can come together in a single cell. The set of proteins, other biochemicals, and ions that protein binds to and reacts with forms the protein’s interactome. Results on protein-protein interactions from biochemical studies, molecular dynamic simulations, and predictive informatics constrain reaction-diffusion models of cells.";

      var stackImages = new Array;
      stackImages['Body'] = 'stackBody.png';
      stackImages['Whole Brain'] = 'stackWhole.png';
      stackImages['Brain Regions'] = 'stackRegions.png';
      stackImages['Microcircuits'] = 'stackMicrocircuits.png';
      stackImages['Cells'] = 'stackCells.png';
      stackImages['Synapses'] = 'stackSynapses.png';
      stackImages['Chromosomes'] = 'stackChromosomes.png';
      stackImages['Proteins'] = 'stackProteins.png';

      $scope.explore = function () {
        $state.go('explore');
      };

      $scope.showStackInfo = function(s) {
        $scope.currentDescription.title = s;
        $scope.currentDescription.content = descriptions[s];
        $scope.stackSrc = stackImages[s];
      };

      $scope.login = function () {
        User.authenticate($scope.username, $scope.password)
          .then(function(user) {
              if (user !== null) {
                  $state.go('home');
              } else {
                  $scope.error = true;
                  alert("bad credentials");
              }
        });
      };

      $scope.go = function () {
        window.location.href = backendUrl + '/login/hbp';
      };
    }]
);
