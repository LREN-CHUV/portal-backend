/**
 * Created by Michael DESIGAUD on 12/08/2015.
 */
angular.module('chuvApp.util').factory('ChartUtil', ['$timeout',function ($timeout) {

        /**
         *  build box plot chart
         * @param chartConfig
         * @param dataset
         */
        var buildBoxplotChart= function (chartConfig,dataset) {
            var xCode = dataset.header[0];
            var yCode = dataset.header[1];
            var y2Code = dataset.header[2];
            chartConfig.xAxis.code = xCode;
            chartConfig.xAxis.title = {text: xCode};
            chartConfig.xAxis.categories = dataset.data[chartConfig.xAxis.code];

            chartConfig.options.yAxis = [];
            chartConfig.options.yAxis.push({title: {text: yCode}});

            chartConfig.series = [];

            chartConfig.series.push({name:yCode,data: dataset.data[yCode],code:yCode});
            if(y2Code){
                chartConfig.series.push({name:y2Code,data: dataset.data[y2Code],type: 'scatter',code:y2Code});
            }
        };

        /**
         * Sort Axis value
         * @param chartConfig Chart config
         * @param dataset Dataset
         * @returns Dataset sorted
         */
        var sortXAxis = function(chartConfig, dataset) {

            var tmpDataArray = [];
            var axisX = chartConfig.xAxis;
            var nbValue = dataset.data[axisX.code].length;

            console.log("Sort chart by X axis '" + axisX.code + "'");

            // Loop for each value of X Axis
            for(var vIdx = 0; vIdx < nbValue; vIdx++) {
                var row = {};
                console.log("Create new row value for temporally array");
                for(var idxHeader in dataset.header) {
                    if (dataset.header.hasOwnProperty(idxHeader)) {
                        var header = dataset.header[idxHeader];
                        console.log("Add value no:" + vIdx + " for serie " + header + " in row value for temporally array");
                        row[header] = dataset.data[header][vIdx];
                    }
                }
                tmpDataArray.push(row);
            }

            console.log("Sort temporally array by '" + axisX.code + "'");

            tmpDataArray.sort(function(a, b) {
                var aValue = a[axisX.code];
                var bValue = b[axisX.code];

                // If numeric, sort
                if(isFinite(aValue) && isFinite(bValue)) {
                    if (parseFloat(aValue) > parseFloat(bValue)) {
                        return 1;
                    } else if (parseFloat(aValue) < parseFloat(bValue)) {
                        return -1;
                    }
                    return 0;
                }

                var aDate;
                var bDate;

                // If date ISO, sort
                var aDateIso = aValue.match(/^(\d{4})\-(\d{2})\-(\d{2})/);
                var bDateIso = bValue.match(/^(\d{4})\-(\d{2})\-(\d{2})/);
                if (aDateIso != null && bDateIso != null && aDateIso.length == 4 && bDateIso.length == 4 ) {

                    aDate = new Date(aDateIso[1], aDateIso[2]-1, aDateIso[3]).getTime();
                    bDate = new Date(bDateIso[1], bDateIso[2]-1, bDateIso[3]).getTime();
                    if (aDate > bDate) {
                        return 1;
                    } else if (aDate < bDate) {
                        return -1;
                    }
                    return 0;

                } else {

                    // If date NON ISO (patch for stub), sort
                    var aDateNonIso = aValue.match(/^(\d{2})\/(\d{2})\/(\d{4})/);
                    var bDateNonIso = bValue.match(/^(\d{2})\/(\d{2})\/(\d{4})/);
                    if (aDateNonIso != null && bDateNonIso != null && aDateNonIso.length == 4 && bDateNonIso.length == 4) {
                        aDate = new Date(aDateNonIso[3], aDateNonIso[2] - 1, aDateNonIso[1]).getTime();
                        bDate = new Date(bDateNonIso[3], bDateNonIso[2] - 1, bDateNonIso[1]).getTime();
                        if (aDate > bDate) {
                            return 1;
                        } else if (aDate < bDate) {
                            return -1;
                        }
                        return 0;
                    }

                }

                if (aValue > bValue) {
                    return 1;
                } else if (aValue < bValue) {
                    return -1;
                }

                return 0;
            });

            console.log("Normalize temporally array in dataset");

            for(idxHeader in dataset.header) {
                if (dataset.header.hasOwnProperty(idxHeader)) {
                    header = dataset.header[idxHeader];
                    var sortedValues = [];
                    for(var vSort = 0; vSort < tmpDataArray.length; vSort++) {
                        sortedValues.push(tmpDataArray[vSort][header]);
                    }
                    dataset.data[header] = sortedValues;
                    // Update X Axis values
                    if (header === axisX.code) {
                        chartConfig.xAxis.categories = sortedValues;
                    }
                }
            }

            return dataset;
        };

        var buildStandardChart= function (chartConfig,dataset,isProportionalXMode) {
            var dataX = dataset.data[chartConfig.xAxis.code];
            chartConfig.xAxis.title = {text: chartConfig.xAxis.code};
            var data = [];
            if(!isProportionalXMode) {
                chartConfig.xAxis.categories = dataset.data[chartConfig.xAxis.code];
            }
            console.log(chartConfig.xAxis.categories);
            dataset = sortXAxis(chartConfig, dataset);
            chartConfig.options.yAxis = [];
            angular.forEach(chartConfig.series, function (configSet) {
                var dataY = dataset.data[configSet.code];
                configSet.data = [];
                for (var j = 0; j < dataY.length; j++) {
                    var unit = {y:dataY[j]};
                    if(isProportionalXMode){
                        unit.x = dataX[j];
                    }
                    configSet.data.push(unit);
                }
                if(isProportionalXMode) {
                    configSet.data = _.sortBy(configSet.data, 'x');
                }
                chartConfig.options.yAxis.push({title: {text: configSet.name}});
            });

            // options
            chartConfig.options.legend= {
                align: 'center',
                verticalAlign: 'bottom',
                layout: 'horizontal'
            };
        };

        /**
         * build pieChart with x like label and y like percent value
         * @param chartConfig
         * @param dataset
         */
        var buildPieChart= function (chartConfig,dataset) {
            var dataX = dataset.data[chartConfig.xAxis.code];
            chartConfig.xAxis.title = {text: chartConfig.xAxis.code};
            var series = [_.last(chartConfig.series)];
            series[0].data = [];
            var dataY = dataset.data[series[0].code];
            for (var i = 0; i < dataX.length; i++) {
                var name = dataX[i];
                var y = dataY[i];
                series[0].data.push({name: name, y: y});
            }
            chartConfig.series = series;

            chartConfig.options.tooltip = {pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b> ({point.y})'};
            chartConfig.options.plotOptions = {
                pie: {
                    allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '{point.percentage:.1f} %',
                            distance: -30
                        },
                    showInLegend: true
                }
            };
            chartConfig.options.legend= {
                align: 'right',
                verticalAlign: 'bottom',
                layout: 'vertical'
            };
        };


        return{
            toChartData: function (chartConfig, dataset) {
                var response = {};
                response.dataset = dataset;

                // resize chart
                chartConfig.func = function(chart) {
                    $timeout(function() {
                        if (chart) {
                            chart.reflow();
                        }
                    }, 0);
                };

                // add xAxis data
                if (chartConfig.xAxis !== undefined && dataset.header.indexOf(chartConfig.xAxis.code) > -1) {
                    // existing chart
                    //chartConfig.xAxis.categories = dataset.data[chartConfig.xAxis.code];
                } else {
                    // new chart
                    chartConfig.xAxis = {code: dataset.header[0]};
                    //chartConfig.xAxis.categories = dataset.data[chartConfig.xAxis.code];
                }
                chartConfig.xAxis.title = {text: chartConfig.xAxis.code};

                // check if data exists for series, if not series is clean
                if (chartConfig.series !== undefined){
                    var isKO = false;
                    for (var i = 0; i < chartConfig.series.length; i++) {
                        if(dataset.header.indexOf(chartConfig.series[i].code) == -1){
                            isKO = true;
                        }
                    }
                    if (isKO) {
                        chartConfig.series = [];
                    }
                }

                if (chartConfig.series === undefined || chartConfig.series.length == 0){
                    var y = 0;
                    if (dataset.header.length >1){
                        y = 1;
                    }
                    var code= dataset.header[y];
                    chartConfig.series = [{name:code,code:code}];
                }

                if (chartConfig.options.chart.type === "pie") {
                    buildPieChart(chartConfig,dataset)
                } else if (chartConfig.options.chart.type === "boxplot") {
                    buildBoxplotChart(chartConfig,dataset);
                } else {
                    buildStandardChart(chartConfig,dataset);
                }
                console.log(chartConfig);
                return response;
            },
            getChartColor: function () {
                return {
                    blue: ['#7fdde9', '#4693b1', '#eb5333'],
                    green: ['#7ecfa6', '#c3e87f', '#f3a238']
                };
            },
            /**
             * build boxplot
             * @param chartConfig
             * @param dataset
             */
            buildBoxplotChart: function (chartConfig,dataset) {
                buildBoxplotChart(chartConfig,dataset);
            },
            /**
             * build pieChart with x like label and y like percent value
             * @param chartConfig
             * @param dataset
             */
            buildPieChart: function (chartConfig,dataset) {
                buildPieChart(chartConfig,dataset);
            },
            /**
             * build standard chart
             * @param chartConfig
             * @param dataset
             */
            buildStandardChart: function (chartConfig,dataset) {
                buildStandardChart(chartConfig,dataset);
            }
        }
    }]
);
