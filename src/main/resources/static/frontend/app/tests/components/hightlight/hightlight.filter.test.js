/**
 * Created by Florent PERINEL on 17/08/2015.
 */

describe('Highlight filter tests', function () {

  // Load filter's modules
  beforeEach(module('chuvApp.components.filters'));

  describe('Highlight filter', function () {

    var highlight;

    // Initialize the filter
    beforeEach(inject(function($filter) {
        highlight = $filter('highlight');
    }));

    it('Filter with result', function () {
      var result = highlight("with result test string", "test");
      expect(result.$$unwrapTrustedValue()).toEqual('with result <span class="highlighted">test</span> string');
    });

    it('Filter without result', function () {
      var result = highlight("with result test string", "nofound");
      expect(result.$$unwrapTrustedValue()).toEqual("with result test string");
    });

  });

});
