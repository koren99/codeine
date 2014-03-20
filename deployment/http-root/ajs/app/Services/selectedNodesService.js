'use strict';
angular.module('codeine').factory('SelectedNodesService', ['$log', '$localStorage', function ($log,$localStorage) {

    $localStorage.$default({
       nodes : [],
       url : ''
    });

    var Api = {
        setSelectedNodes : function(nodes,url) {
            $log.debug('selectedNodesService: Storing nodes for url \'' + url + '\' : ' + nodes.join(','));
            $localStorage.nodes = nodes;
            $localStorage.url = url;


        },

        getSelectedNodes : function(url) {
            if ($localStorage.url === url) {
                return $localStorage.nodes;
            }
            $log.error('selectedNodesService: given url \'' + url + '\' is not like the stored one \'' + $localStorage.url + '\'');
        }
    };

    return Api;
}]);