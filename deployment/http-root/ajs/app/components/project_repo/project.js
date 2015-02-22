(function (angular) {
    'use strict';

    //// JavaScript Code ////
    function CodeineProjectFactory() {
        function CodeineProject(name) {
            this.name = name;
            this.nodes_count = 0;
            this.configuration = {};
            this.status = {};
            this.nodes_aliases = { data : []};
            this.runnableCommands = { data : []};
            this.statistics = { data : [] };
        }

        function shouldRefreshObject(obj) {
            return (new Date() - obj.retrieveTime > 300000);
        }

        CodeineProject.prototype = {
            setConfiguration : function(data) {
                data.retrieveTime = new Date();
                this.configLoaded = true;
                angular.extend(this.configuration, data);
            },
            setStatus : function(status) {
                status.retrieveTime = new Date();
                this.statusLoaded = true;
                angular.extend(this.status, status);
            },
            setStatistics : function(data) {
                this.statistics.data.retrieveTime = new Date();
                this.statisticsLoaded = true;
                this.statistics.data.length = 0;
                angular.copy(data,this.statistics.data);
            },
            setNodesAliases : function(nodesAliases) {
                this.nodes_aliases.retrieveTime = new Date();
                this.nodesAliasesLoaded = true;
                this.nodes_aliases.data.length = 0;
                angular.copy(nodesAliases,this.nodes_aliases.data);
            },
            setNodesCount : function(count) {
                this.nodes_count = count;
            },
            setRunnableCommands : function(runnableCommands) {
                this.runnableCommands.retrieveTime = new Date();
                this.runnableCommandsLoaded = true;
                this.runnableCommands.data.length = 0;
                angular.copy(runnableCommands,this.runnableCommands.data);
            },
            isConfigLoaded : function() {
                return this.configLoaded && !shouldRefreshObject(this.configLoaded);
            },
            isStatusLoaded : function() {
                return this.statusLoaded && !shouldRefreshObject(this.status);
            },
            isNodesAliasesLoaded : function() {
                return this.nodesAliasesLoaded && !shouldRefreshObject(this.nodes_aliases);
            },
            isRunnableCommandsLoaded : function() {
                return this.runnableCommandsLoaded && !shouldRefreshObject(this.runnableCommands);
            },
            isStatisticsLoaded : function() {
                return this.statisticsLoaded && !shouldRefreshObject(this.statistics);
            },
            canRerun : function(nodes_list) {
                var aliases = _.map(nodes_list, function(node){return node.alias;});
                for (var i12=0 ; i12 < this.status.nodes_for_version.length; i12++) {
                    for (var j12=0 ; j12 < this.status.nodes_for_version[i12].nodes.length; j12++) {
                        var index = aliases.indexOf(this.status.nodes_for_version[i12].nodes[j12].alias);
                        if (index !== -1) {
                            aliases.splice(index, 1);
                        }
                    }
                }
                return aliases.length === 0;
            },
            canCancel : function(nodes_list) {
                var aliases = _.map(nodes_list, function(node){return node.alias;});
                for (var i12=0 ; i12 < this.status.nodes_for_version.length; i12++) {
                    for (var j12=0 ; j12 < this.status.nodes_for_version[i12].nodes.length; j12++) {
                        var index = aliases.indexOf(this.status.nodes_for_version[i12].nodes[j12].alias);
                        if (index !== -1) {
                            return true;
                        }
                    }
                }
                return false;
            },
            cloneConfiguration : function() {
                return angular.fromJson(angular.toJson(this.configuration));
            }
        };

        return CodeineProject;
    }

    //// Angular Code ////
    angular.module('codeine').factory('CodeineProject', CodeineProjectFactory);
})(angular);