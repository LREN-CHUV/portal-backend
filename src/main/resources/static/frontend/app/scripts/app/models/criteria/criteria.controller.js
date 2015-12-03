'use strict'

/**
 * Created by Michael DESIGAUD on 14/08/2015.
 */
angular.module('chuvApp.models').controller('CriteriaController', ['$scope', '$stateParams', 'Model', 'ChartUtil', 'filterFilter', 'Variable', 'Group', '$rootScope','$log','$timeout',
    function ($scope, $stateParams, Model, ChartUtil, filterFilter, Variable, Group, $rootScope, $log,$timeout) {

        $scope.query.variables = [];
        $scope.query.groupings = [];
        $scope.query.coVariables = [];
        $scope.query.filters = [];

        /**
         * load jquery decorator
         */
        $scope.initDesign = function () {
            if ($('.custom-scrollbar').size() > 0) {
                $(".custom-scrollbar").mCustomScrollbar();
            }
        };


        /**
         *
         * @param model
         */
        $scope.loadResources = function (model) {
            if ($stateParams.slug !== undefined) {
                $scope.initDesign();
            }

            Variable.query()
                .$promise.then(function (allVariables) {
                    $scope.allVariables = _.sortBy(allVariables,"label");
                    $scope.variables = filterFilter($scope.allVariables, {isVariable: true});
                    $scope.groupingVariables = filterFilter($scope.allVariables, {isGrouping: true});
                    $scope.coVariables = filterFilter($scope.allVariables, {isCovariable: true});
                    $scope.filterVariables = filterFilter($scope.allVariables, {isFilter: true});
                    return Group.get().$promise;
                })
                .then(function (group) {
                    $scope.groups = group.groups;
                    _.extend($scope.query, model.query);
                    if ($stateParams.slug === undefined) {
                        $scope.initDesign();
                    }
                });
        };

        /**
         * add item in list
         */
        $scope.addItem = function (list,object,classList) {
            if (!$scope.contains(list, object)) {
                list.push(object);
                $("."+classList).removeClass("active");
            }
        };

        /**
         * remove an item in list
         */
        $scope.removeItem = function (list, index) {
            list.splice(index, 1);
        };

        /**
         * get object in list by code
         * @param list
         * @param code
         * @returns {*}
         */
        $scope.getDataByCode = function (list, code) {
            var finded = _.find(list, function (variable) {
                return variable.code === code;
            });
            return finded === undefined ? {label: "select a new entry"} : finded;
        };

        /**
         * return coVariable by code
         * @param code
         */
        $scope.getCoVariableByCode = function (code) {
            var finded = _.find($scope.coVariables, function (coVariable) {
                return coVariable.code === code;
            });
            return finded === undefined ? {label: "select a new entry"} : finded;
        };

        /**
         * get input type of variable
         */
        $scope.getInputType = function (variable, list) {
            var type = 'text';
            if (variable === undefined) {
                return type;
            }
            var variableTmp = $scope.getDataByCode(list, variable.code);

            switch (variableTmp.type) {
                case "T":
                    type = 'text';
                    break;
                case "D":
                    type = 'date';
                    break;
                case "I":
                case "N":
                    type = 'number';
                    break;
                default:
                    break;
            }
            return type;
        };

        /**
         * delete value of filter if empty
         * @param filter
         * @param index
         */
        $scope.setFilterValue = function (filter, index) {
            if (filter.values === undefined || filter.values.length == 0) {
                return;
            }

            if (filter.values[index] == "" || filter.values[index] == null) {
                filter.values.splice(index, 2);
            }
        };

        /**
         * set Operator of filter
         * @param filter
         */
        $scope.setOperator = function (filter) {
            if (filter.values === undefined || filter.values.length == 0) {
                return;
            }
            if (filter.values.length == 1) {
                filter.operator = "eq";
                return;
            }
            var variableTmp = $scope.getDataByCode($scope.filterVariables, filter.variable.code);
            switch (variableTmp.type) {
                case "T":
                    filter.operator = "in";
                    break;
                case "D":
                case "I":
                case "N":
                    filter.operator = "between";
                    break;
                default:
                    filter.operator = "????";
                    break;
            }
        };

        /**
         * check if list contains value
         * @param list
         * @param value
         * @returns {boolean}
         */
        $scope.contains = function (list, value) {
            var findFunction = function (item) {
                return angular.equals(item, value)
            };

            return _.find(list, findFunction) !== undefined;
        };

        /**
         * active class of group
         * @param color new color theme
         * @param event current click event
         */
        $scope.activateGroup = function (event) {
            var parent = $(event.currentTarget).parent();
            $scope.activateItem(parent);
        };

        /**
         * active class of item
         * @param item jquery object
         */
        $scope.activateItem = function (item) {
            if (item.hasClass("active")) {
                item.removeClass("active");
            } else {
                item.addClass("active");
            }
        };

        // load criteria's resources after model
        $rootScope.$on('event:loadModel', function (evt, model) {
            $scope.loadResources(model);
        });

        // loading criteria's resources on new model
        if ($stateParams.slug === undefined) {
            $scope.loadResources({});
        }

        /**
         * focus on query's variable
         * @param evtName
         * @param item
         */
        $scope.emitEventFocus = function(evtName,item){
            if(item){
                $scope.activateItem($('#'+item));
            }
            $rootScope.$broadcast(evtName);
        };

        /**
         * init scolling on searching criteria
         */
        $scope.initSearchFor = function(){
            // ---------------------------------- //
            // --------- SEARCH REUME CONFIG
            // ---------------------------------- //
            var $searchResume = $('.container-ee-search-resume');
            // Init the search resume
            var $listSearch = $searchResume.find('.search-list'),
                $wrapperListSearch = $listSearch.find(".wrapper-search-list"),
                $arrows = $searchResume.find('.search-list-arrows'),
                leftWrapper = 0;
                $arrows.find('.prev').addClass('active');
                $arrows.find('.next').addClass('active');

            $arrows
                .find('.next')
                .on('click', function(e) {
                    e.preventDefault();
                    goToList(-1);
                })
                .end()
                .find('.prev')
                .on('click', function(e) {
                    e.preventDefault();
                    goToList(1);
                });

            // Update list width
            var updateListWidth =function () {
                var wList = 0;
                $wrapperListSearch.find('a').each(function() {
                    wList += $(this).outerWidth(true);
                });
                $wrapperListSearch.css({
                    width : wList
                });
            };

            // Show or not the arrows
            var updateSearchArrows = function(leftWrapper) {
                if( $wrapperListSearch.width() > $listSearch.width() ) {

                    // Prev
                    if( leftWrapper < 0 ) {
                        $arrows.find('.prev').addClass('active');
                    } else {
                        $arrows.find('.prev').removeClass('active');
                    }

                    // Next
                    if( leftWrapper <= 0 && -leftWrapper + $listSearch.width() < $wrapperListSearch.width() ) {
                        $arrows.find('.next').addClass('active');
                    } else {
                        $arrows.find('.next').removeClass('active');
                    }
                }
                else {
                    $arrows.find('a').removeClass('active');
                }
            };


            var  goToList = function(direction) {
                leftWrapper = 0;
                updateListWidth();
                var offset = 100,
                    left = 0;

                leftWrapper = parseFloat($wrapperListSearch.css('left'));

                left = leftWrapper + offset * direction;

                if( direction > 0 && left >= 0) {
                    left = 0;
                } else if( direction <= 0 && -left + $listSearch.width() >= $wrapperListSearch.width() ) {
                    left = $listSearch.width() - $wrapperListSearch.width();
                }

                //updateSearchArrows(left);

                $wrapperListSearch.stop().animate({ left : left });
            };
        };

        $timeout(function() {
            $scope.initSearchFor();
        }, 500);

    }]);
