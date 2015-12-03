/**
 * Created by David JAY on 07/09/2015.
 */
/**
 * Created by David JAY on 06/09/2015.
 */
angular.module('chuvApp.components.criteria')
    .directive('criteriaChainedSelect', function () {
        return {
            restrict: 'AE', //attribute or element
            scope: {
                groups: '=',
                variables: '=',
                criteria: '=',
                error: '=',
                useGroupCriteria: '@',
                removeEvent: '@',
                focusEvent: '@'
            },
            templateUrl: 'scripts/components/criteria/chained-select.html',

            controller: ['$scope', 'filterFilter','$log','$rootScope', function ($scope, filterFilter,$log,$rootScope) {
                $scope.selectedList = [];
                $scope.isInit = false;
                $scope.useGroupCriteria = false;
                var defaultMsgError = "error";


                /**
                 * init directive with hierachy groups
                 * @param variable
                 */
                $scope.init = function (criteria) {
                    var variable = criteria;

                    if (variable === undefined || variable === null || (!_.isArray(variable) && variable.code === undefined)) {
                        return;
                    }
                    if (_.isArray(variable)) {
                        variable = variable[0];
                    }

                    var variable = $scope.getDataByCode($scope.variables, variable.code);
                    if (variable === undefined) {
                        return;
                    }

                    var variableGroup = variable.group;
                    if (variableGroup === undefined) {
                        return;
                    }
                    var group = $scope.getDataByCode($scope.groups, variable.group.code);

                    while (variableGroup !== undefined && variableGroup != null) {
                        $scope.selectedList.push(group);
                        $scope.initSelectedVariablesByGroup(group);
                        variableGroup = variableGroup.groups[0];
                        if (variableGroup !== undefined) {
                            group = $scope.getDataByCode(group.groups, variableGroup.code);
                        }
                    }
                    $scope.selectedList.push(variable);
                    if (_.isArray(criteria) && criteria.length>1){
                        $scope.selectedList = _.initial($scope.selectedList);
                    }

                };

                /**
                 * get object in list by code
                 * @param list
                 * @param code
                 * @returns {*}
                 */
                $scope.getDataByCode = function (list, code) {
                    return _.find(list, function (variable) {
                        return variable.code === code;
                    });
                };

                /**
                 * remove selected item and it's children
                 * @param index
                 */
                $scope.removeSelected = function (index) {
                    if ($scope.selectedList.length <= index) {
                        return;
                    }
                    $scope.selectedList = _.first($scope.selectedList, index);

                    if ($scope.useGroupCriteria) {
                        $scope.criteria = []
                    } else {
                        $scope.criteria.code = undefined;
                    }
                };


                /**
                 * init list variables of group
                 * @param group
                 */
                $scope.getNbVariablesOfGroup = function (group) {
                    if (group.groups.length == 0) {
                        var nb = 0;
                        angular.forEach($scope.variables, function (variable, index) {
                            if (variable.group) {
                                var code = variable.group.code;
                                var currentGroup = variable.group.groups;
                                while (currentGroup !== undefined && currentGroup.length > 0) {
                                    code = currentGroup[0].code;
                                    currentGroup = currentGroup.groups;
                                }
                                if (group.code === code) {
                                    nb++;
                                }
                            }
                        });
                        return nb;
                    } else {
                        return "...";
                    }
                };

                /**
                 * init list variables of group
                 * @param group
                 */
                $scope.initSelectedVariablesByGroup = function (group) {
                    if (group.groups.length == 0) {
                        $scope.selectedVariablesByGroup = $scope.getVariablesOfGroup($scope.selectedList, $scope.variables);
                    }
                };


                /**
                 * set list variables of selected groups
                 * @param groups
                 * @param variables
                 * @returns {*}
                 */
                $scope.getVariablesOfGroupSubGroup = function (group) {
                    var treeGrp = _.pick(_.first($scope.selectedList), "code", "label");
                    var currentGrp = _.first($scope.selectedList);
                    var parentGroup = currentGrp;
                    var targetGrp = _.pick(currentGrp, "code", "label");

                    while (parentGroup) {
                        targetGrp.groups = [];
                        var currentGrp = filterFilter(parentGroup.groups, {code: group.code});
                        if (currentGrp) {
                            targetGrp.groups.push(_.pick(currentGrp, "code", "label"));
                            parentGroup = currentGrp;
                        }
                    }


                    angular.forEach(groups, function (currentGroup, index) {
                        if (currentGroup.isVariable === undefined) {
                            if (selectedGroup === undefined) {
                                selectedGroup = _.pick(currentGroup, "code", "label");
                                selectedGroup.groups = [];
                                tmpGroup = selectedGroup.groups;
                                filterArray = filterFilter(filterArray, {group: {code: currentGroup.code}});
                            } else {
                                tmpGroup.push(_.pick(currentGroup, "code", "label"));
                                tmpGroup[0].groups = [];
                                tmpGroup = tmpGroup[0].groups;
                            }
                        }
                    });
                    var filterArray = _.filter(filterArray, function (variable) {
                        return _.isEqual(variable.group, selectedGroup);
                    });
                    return   filterArray;
                };


                /**
                 * set list variables of selected groups
                 * @param groups
                 * @param variables
                 * @returns {*}
                 */
                $scope.getVariablesOfGroup = function (groups, variables) {
                    var selectedGroup;
                    var tmpGroup;
                    var filterArray = variables;
                    angular.forEach(groups, function (currentGroup, index) {
                        if (currentGroup.isVariable === undefined) {
                            if (selectedGroup === undefined) {
                                selectedGroup = _.pick(currentGroup, "code", "label");
                                selectedGroup.groups = [];
                                tmpGroup = selectedGroup.groups;
                                filterArray = filterFilter(filterArray, {group: {code: currentGroup.code}});
                            } else {
                                tmpGroup.push(_.pick(currentGroup, "code", "label"));
                                tmpGroup[0].groups = [];
                                tmpGroup = tmpGroup[0].groups;
                            }
                        }
                    });
                    var filterArray = _.filter(filterArray, function (variable) {
                        return _.isEqual(variable.group, selectedGroup);
                    });
                    return   filterArray;
                };

                /**
                 * Bind criteria of selected nested list
                 * @param v
                 */
                $scope.setCriteria = function (v) {
                    // get last selected
                    var last = _.last(v);
                    if (!$scope.useGroupCriteria && last.isVariable === undefined){
                        //not used criteria if is not a variable
                        return;
                    }

                    if ($scope.useGroupCriteria && last.isVariable === undefined) {
                        $scope.criteria = _.map($scope.selectedVariablesByGroup,
                            function (variable) {
                                return _.pick(variable, "code");
                            }
                        );
                    } else {
                        var criteria = _.pick(last, "code");
                        // set last with only code property
                        $scope.criteria = $scope.useGroupCriteria?[criteria]:criteria;
                    }
                };

                //initialize directive
                $scope.$watch('criteria', function (newValue, oldValue) {
                    if (!$scope.isInit && newValue !== undefined && (newValue.length === undefined || newValue.length>0)  && $scope.selectedList.length == 0) {
                        $log.debug("init chained-select");
                        $scope.init(newValue);
                        $scope.isInit = true;
                    }
                });

                /**
                 * Remove event calling
                 */
                $rootScope.$on($scope.removeEvent, function (evt) {
                    $scope.removeSelected(0);
                });
            }]
        };
    });
