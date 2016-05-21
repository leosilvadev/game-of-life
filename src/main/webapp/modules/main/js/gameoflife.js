(function(angular){
    angular.module('main').factory('gameoflife', Factory);

    function Factory($interval, $http){
        var TIMEOUT_DELAY = 200;
        var URL = 'http://localhost:9000/v1/games/';
        var timer;

        var cancelTimer = function(){
            if ( timer ) $interval.cancel(timer);
        }

        var checkUpdates = function(gameToken, onMessage, onFailure){
            return function(){
                $http.get(URL + gameToken)
                    .then(function(res){
                        onMessage(res.data.data.rows);
                    })
                    .catch(function(res){
                        if ( res.status == 404 ) {
                            cancelTimer();
                            onFailure();
                        }
                    });
            }
        };

        var start = function(data, onMessage, onFailure){
            $http.post(URL, data).then(function(result){
                var gameToken = result.data.token;
                timer = $interval(checkUpdates(gameToken, onMessage, onFailure), TIMEOUT_DELAY);
            });
        }

        return {
            start: start,
            stop: cancelTimer
        };
    }

})(angular);
