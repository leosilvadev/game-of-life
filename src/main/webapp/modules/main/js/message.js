(function(angular){
    angular.module('main').factory('message', Message);

    function Message(notify){
        var warn = function(message){
            notify({message:message, position:'right', duration:2000, classes:'alert-warning'});
        }
        return {
            warn: warn
        };
    }

})(angular);
