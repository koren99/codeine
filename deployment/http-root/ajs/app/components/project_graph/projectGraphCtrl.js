'use strict';
angular.module('codeine').controller('projectGraphCtrl',['$scope', '$log','$filter','$routeParams', 'graphData',
    function($scope, $log, $filter,$routeParams,graphData) {
        $scope.projectName = $routeParams.project_name;
        $scope.options = {
            axes: {
                x: {key: 'x', type: 'date', tooltipFormatter: function(x) { return ' ' + $filter('date')(x,'short') + ' ';}},
                y: {type: 'linear'}
                //y2: {type: 'linear'}
            },
            series: [
                {y: 'total', color: 'green',thickness : '3px', label: 'Total'},
                {y: 'fail', color: 'red',thickness : '3px', label: 'Fail'},
                {y: 'command', color: 'steelblue',thickness : '10px', label: 'Commands', type: 'column'},
            ],
            lineMode: "cardinal"
        };
        $scope.data = [];
        for (var i=0 ;i < graphData.length; i++) {
            if (graphData[i].date_long !== 0) {
                $scope.data.push({
                    x: new Date(graphData[i].date_long),
                    fail: parseInt(graphData[i].fail),
                    total: parseInt(graphData[i].total),
                    command: graphData[i].nodes
                });
            }
        }
    }]);