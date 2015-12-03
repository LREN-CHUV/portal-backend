/**
 * Created by Florent PERINEL on 14/08/2015.
 */
'use strict';

angular.module('chuvApp.requests')
  .factory('Request', ['$rootScope', function () {

    var datas = {
      facets: [
        {
          name: "type",
          values: {
            "EE": 77,
            "IA": 13,
            "BSD": 5
          }
        }, {
          name: "author",
          values: {
            "Henry Markram": 80,
            "Anastasia Ailamaki": 15
          }
        }
      ],
      filters: {},
      results: [
        {
          id: 1,
          label: "Distribution of Alzheimer cases across Europe, between 2000 and 2014",
          date: "2013-10-31T09:03:00-02:00",
          dataset: "123456798",
          request: {
            filters: [
              {variable: "period", operator: "range", values: [2000, 2014]},
              {
                variable: "location",
                operator: "in",
                values: ["UK", "Switzerland", "Poland", "Greece", "France", "Germany", "Romania"]
              },
              {variable: "pathology", operator: "equal", values: ["G30"]},
            ],
            clusters: ["location"],
            columns: ["location"]
          },
          data: {
            "Country": ["UK", "Switzerland", "Poland", "Greece", "France", "Germany", "Romania"],
            "Number of cases": [15.2, 9.3, 15.6, 10, 2.5, 5.6, 8.6]
          },
          type: "EE",
          chart: {
            type: "pie",
            title: "Distribution of Alzheimer cases across Europe, between 2000 and 2014",
            size: {height: 100, width: 300}
          },
          thumbnail: "images/charts/charts_003.png",
          author: "Henry Markram",
          author_obj: {id: 1, fullname: "Henry Markram", picture: "images/authors/Henry-Markram_001.png"}
        },
        {
          id: 2,
          label: "Another request about Alzheimer cases",
          date: "2013-10-31T09:03:00-02:00",
          dataset: "123456799",
          request: { },
          data: { },
          type: "EE",
          chart: {
            type: "line",
            title: "Another request about Alzheimer cases",
            size: {height: 100, width: 300}
          },
          thumbnail: "images/charts/charts_001.png",
          author: "Anastasia Ailamaki",
          author_obj: {id: 3, fullname: "Anastasia Ailamaki", picture: "images/authors/75530b64.jpg"}
        },
        {
          id: 3,
          label: "Parkinson cases in the world between 2000 and 2010",
          date: "2013-10-31T09:03:00-02:00",
          dataset: "123456700",
          request: { },
          data: { },
          type: "IA",
          chart: {
            type: "3dpoint",
            title: "Parkinson cases in the world between 2000 and 2010",
            size: {height: 100, width: 300}
          },
          thumbnail: "images/charts/charts_002.png",
          author: "Anastasia Ailamaki",
          author_obj: {id: 3, fullname: "Anastasia Ailamaki", picture: "images/authors/75530b64.jpg"}
        },
        {
          id: 4,
          label: "Parkinson and Alzheimer cases in the world",
          date: "2013-10-31T09:03:00-02:00",
          dataset: "123456711",
          request: { },
          data: { },
          type: "BSD",
          chart: { },
          thumbnail: "images/charts/charts_003.png",
          author: "Anastasia Ailamaki",
          author_obj: {id: 3, fullname: "Anastasia Ailamaki", picture: "images/authors/75530b64.jpg"}
        }
      ]
    };

    return {

      search: function (label, filters) {

        // To clone object
        var result = JSON.parse(JSON.stringify(datas));

        if ((filters != null && Object.keys(filters).length > 0) || label != null) {
          var requests = [];
          for (var index in result.results) {
            if (result.results.hasOwnProperty(index)) {
              var request = result.results[index];
              var validResult = true;
              if (label != null && request.label.toLowerCase().search(label.toLowerCase()) == -1) {
                validResult = false;
              }
              for (var key in filters) {
                if (filters.hasOwnProperty(key) && (filters[key].indexOf(request[key]) == -1 && filters[key].length > 0)) {
                  validResult = false;
                }
              }
              if (validResult) {
                requests.push(request);
              }
            }
          }
          result.results = requests;
        }

        result.filters = filters;

        return result;
      },

      get: function (id) {
        for (var idx in datas.results) {
          if (datas.results.hasOwnProperty(idx) && datas.results[idx].id == id) {
            return datas.results[idx];
          }
        }
        return null;
      },

      create: function (request) {
        if (request.id == null) {
          var newId = 1;
          for (var idx in datas.results) {
            if (datas.results.hasOwnProperty(idx)) {
              newId = datas.results[idx].id + 1;
            }
          }
          request.id = newId;
        }
        datas.results.push(request);
        return request;
      },

      update: function (request) {
        if (request.id == null) {
          return request;
        }
        for (var idx in datas.results) {
          if (datas.results.hasOwnProperty(idx) && datas.results[idx].id == request.id) {
            datas.results[idx] = request;
            return request;
          }
        }
        return request;
      },

      remove: function (id) {
        for (var idx in datas.results) {
          if (datas.results.hasOwnProperty(idx) && datas.results[idx].id == id) {
            datas.results.splice(idx, 1);
            return;
          }
        }
      }
    };

  }]);
