/**
 * Created by Michael DESIGAUD on 12/08/2015.
 */

'use strict';
angular.module('chuvApp.models')

  .directive("circlePacking", [function () {
    return {
      templateUrl: "scripts/app/models/variable_selection/circle_packing.html",
      replace: true,
      link: function ($scope, element) {

        $scope.search_history = [];
        $scope.search = {};

        var groups,
          disableLastWatch = function () {},
          group_dict,
          color = d3.scale.linear()
            .domain([-1, 5])
            .range(["hsl(152,80%,80%)", "hsl(228,30%,40%)"])
            .interpolate(d3.interpolateHcl),
          svg;

        function createCirclePackingDataStructure () {

          // all groups by code
          group_dict = {};

          function map_groups(group) {
            return group_dict[group.code] = {
              label: group.label,
              code: group.code,
              is_group: true,
              original: group,
              children: group.groups.map(map_groups)
            };
          }

          // put groups in datastructure
          groups = map_groups($scope);

          // and then all the variables in all the right groups
          $scope.allVariables.forEach(function (variable) {
            var group = group_dict[variable.group.code];
            if (!group) return;
            group.children.push(group_dict[variable.code] = {
              code: variable.code,
              label: variable.label,
              is_group: false,
              original: variable,
              children: []
            })
          });
        }

        // clears the current circle packing and recreates it from crash.
        // quite compute intensive, do not overuse.
        // used when resizing
        // inspired from: bl.ocks.org/mbostock/7607535
        function updateCirclePacking () {
          element.find(".panel-body").empty();
          disableLastWatch();

          var margin = 20,
            diameter = element.width(),
            root = groups,
            pack = d3.layout.pack()
              .padding(2)
              // prevent sorting, otherwise packing will look way too regular.
              .sort(null)
              .size([diameter - margin, diameter - margin])
              // circle weight is based on the length of text. It's not
              // strictly necessary but makes things nicer looking.
              .value(function(d) { return 2 + d.label.length; });
          svg = d3.select(element.find(".panel-body")[0]).append("svg")
            .on("click", function() { zoom(root); })
            .attr("width", diameter)
            .attr("height", diameter)
            .append("g")
            .attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");
          var focus = groups,
            nodes = pack.nodes(groups),
            view,
            circle = svg.selectAll("circle")
              .data(nodes)
              .enter().append("circle")
              .attr("class", function(d) { return d.parent ? d.children ? "node" : "node node--leaf" : "node node--root"; })
              .style("fill", color_for_node)
              .on("click", function(d) {
                if (focus !== d) {
                  zoom(d);
                }
                d3.event.stopPropagation();
              }),
            text = svg.selectAll("text")
              .data(nodes)
              .enter().append("text")
              .attr("class", function(d) { return d.children ? "circle-label group" : "circle-label"; })
              .style("display", function(d) {
                return d.parent === root ? "inline" : "none";
              })
              .text(function(d) {
                if(!d.parent)
                  return d.label;

                // magic function to cut off text that's too long.
                // I came up with this after a little trial and error
                var max_length = 5 + d.r * 100 / d.parent.r;

                if (d.label.length > max_length)
                  return d.label.substr(0, max_length - 3) + "...";
                return d.label;
              }),
            node = svg.selectAll("circle,text");

          zoomTo([root.x, root.y, root.r * 2 + margin]);

          function zoom(d) {
            focus = d;

            var transition = d3.transition()
              .duration(750)
              .tween("zoom", function(d) {
                return function(t) {
                  zoomTo(d3.interpolateZoom(view, [focus.x, focus.y, focus.r * 2 + margin])(t));
                };
              });

            var condition = function (d) { return d && (d.parent === focus || !d.children && d === focus); };
            transition.selectAll("text")
              .filter(function(d) {
                return this.style.display === "inline" || condition(d);
              })
              .style("fill-opacity", function(d) { return condition(d) ? 1 : 0; })
              .each("start", function(d) { if (condition(d)) this.style.display = "inline"; })
              .each("end", function(d) { if (!condition(d)) this.style.display = "none"; });

            // this happens when a circle is clicked: bind the variable
            // and notify angular, since the event doesn't happen within angular.
            if ($scope.focused_variable !== d.original) {
              $scope.set_focused_variable(d !== groups && d.original);
              $scope.search.value = null;
              $scope.$apply();
            }
          }

          function zoomTo(v) {
            var k = diameter / v[2]; view = v;
            node.attr("transform", function(d) { return "translate(" + (d.x - v[0]) * k + "," + (d.y - v[1]) * k + ")"; });
            circle.attr("r", function(d) { return d.r * k; });
          }

          d3.select(self.frameElement).style("height", diameter + "px");

          disableLastWatch = $scope.$watch(
            "focused_variable",
            function (variable) {
              if (variable && variable.code && group_dict[variable.code])
                zoom(group_dict[variable.code]);
            }
          );
        }

        function color_for_node (node) {
            var category = _.find(Object.keys($scope.configuration), function (category) {
              return node.code && $scope.variable_is_used_as(category, node.code)
            });
            if (category) {
              return $scope.colors[category];
            }
            return node.children ? color(node.depth) : null;
          }

        $scope.$on(
          "configurationChanged",
          function () {
            svg.selectAll("circle").style("fill", color_for_node);
          }
        );

        // update circle packing whenever groups change
        // (won't happen often)
        $scope.$watch("groups", function (groups) {
          if (groups != null) {
            createCirclePackingDataStructure();
            updateCirclePacking();
          }
        });

        //redraw the whole stuff when resizing.
        //every circle changes size and position, so I might as well...
        var prev_dimension = element.width();
        function resize_handler() {
          if ($scope.groups != null && element.width !== prev_dimension) {
            prev_dimension = element.width();
            updateCirclePacking();
          }
        }
        angular.element(window).bind('resize', resize_handler);
        $scope.$on("$destroy", function () {
          angular.element(window).unbind('resize', resize_handler)
        });


        $scope.$watch(
          "search.value",
          function (variable) {
            if (variable && variable.code && group_dict[variable.code]) {
              $scope.set_focused_variable(variable);

              if ($scope.search_history.indexOf(variable) < 0) {
                $scope.search_history.unshift(variable);
                if ($scope.search_history.length > 5) {
                  $scope.search_history.pop();
                }
              }
            }
          }
        );

      }
    };
  }])


  .directive("variableStatistics", ["$timeout", "Variable", '$stateParams', function ($timeout, Variable, $stateParams) {
    return {
      templateUrl: "scripts/app/models/variable_selection/variable_statistics.html",
      link: function ($scope, element) {

        // so that two simultaneous requests don't clash.
        var request_id = 0;

        // maps the input data into an array containing:
        // - the list of x coordinates to display
        // - the data series
        // - whether or not there are multiple series.
        function map_data_to_hc_series (data, fallback_category_name) {
          var has_multiple_series = data.hasOwnProperty("categories"),
            value_matrix = {},
            ordered_categories = [],
            known_x_values = {},
            ordered_x_values = [];

          // first pass: reference and map all values
          var idx, category, x_value;
          for (idx = 0; idx < data.value.length; idx++) {
            category = has_multiple_series ? data.categories[idx] : fallback_category_name;
            x_value = data.header[idx];

            if (!value_matrix.hasOwnProperty(category)) {
              value_matrix[category] = {};
              ordered_categories.push(category);
            }
            if (!known_x_values.hasOwnProperty(x_value)) {
              known_x_values[x_value] = null;
              ordered_x_values.push(x_value);
            }
            value_matrix[category][x_value] = data.value[idx];
          }

          // second pass: create data structure
          var series = ordered_categories.map(function (category) {
            return {
              name: category,
              data: ordered_x_values.map(function (x_value) {
                return value_matrix[category][x_value] || 0;
              })
            };
          });

          return [ordered_x_values, series, has_multiple_series];
        }

        /**
         * sets up the highcharts configuration for a given statistics dataset.
         * @param statistic
         */
        function setup_stats (statistic) {

          var categories_and_data = map_data_to_hc_series(statistic.dataset.data, statistic.dataset.name);

          statistic.hc_config = {

            options: {
              chart: {
                type: 'column'
              },
              tooltip: {
                headerFormat: '<span style="font-weight:bold">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
              }
            },
            xAxis: { categories: categories_and_data[0] },

            series: categories_and_data[1],

            title: {
              text: categories_and_data[2] ? statistic.dataset.name : null
            },

            size: {
              width: null,
              height: 350
            }
          };
        }

        $scope.$watch(
          "focused_variable",
          function (focused_variable) {
            if (!focused_variable || !focused_variable.code) return;

            $scope.focused_variable_loaded = false;
            $scope.has_error = false;

            request_id++;
            var current_request_id = request_id;

            Variable
              .get_stats(focused_variable.code)
              .then(function (response) {
                if (current_request_id != request_id) return;
                $scope.focused_variable_loaded = true;
                $scope.stats = response.data;

                if (!angular.isArray($scope.stats)) {
                  $scope.stats = [$scope.stats];
                }

                $scope.stats
                  .filter(function (statistic) { return statistic.dataType === 'DatasetStatistics' })
                  .forEach(setup_stats);

                $scope.measurement_count = $scope.stats[0].count || "??";

                $scope.variable_description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the " +
                  "industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to " +
                  "make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, " +
                  "remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem " +
                  "Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                  "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. " +
                  "The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content " +
                  "here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use " +
                  "Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. \n" +
                  "Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)." +
                  "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10." +
                  "33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English " +
                  "versions from the 1914 translation by H. Rackham.";
              }, function () {
                if (current_request_id != request_id) return;
                $scope.has_error = true;
              })
          }
        );

        // this is to overcome a ng-highcharts sizing bug.
        $scope.show_stats_after_timeout = function () {
          $scope.show = false;
          $timeout(
            function () {
              $scope.show = true;
            },
            0
          );
        };
      }
    };
  }])


  .directive("variableDescription", [function () {
    return {
      scope: {
        text: "="
      },
      template: "<p ng-repeat='paragraph in real_text'>{{paragraph}} <a ng-if='is_shortened && $index==real_text.length-1' ng-click='show_full()'>More</a></p>",
      controller: ["$scope", function ($scope) {

        $scope.$watch("text", function (val) {
          if (!angular.isString(val)) {
            $scope.real_text = "";
            return;
          }

          if (val.length < 800) {
            $scope.real_text = val;
            $scope.is_shortened = false;
          } else {
            $scope.real_text = val.substr(0, 700) + "...";
            $scope.is_shortened = true;
          }
          $scope.real_text = $scope.real_text.split("\n");
        });

        $scope.show_full = function () {
          $scope.real_text = $scope.text.split("\n");
          $scope.is_shortened = false;
        }

      }]
    };
  }])


  .directive("variableConfiguration", function () {
    return {
      templateUrl: "scripts/app/models/variable_selection/variable_configuration.html",
      scope: {
        setFocusedVariable: "=",
        setVariableSelectedAs: "=",
        configuration: "="
      },
      controller: ["$scope", function ($scope) {

        $scope.has_configuration = function () {
          return _.any(
            Object.keys($scope.configuration),
            function (sub_arr) { return Object.keys($scope.configuration[sub_arr]).length; }
          );
        };

      }]
    };
  });
