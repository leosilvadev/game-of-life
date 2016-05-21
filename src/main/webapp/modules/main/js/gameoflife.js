(function(angular){
    angular.module('main').factory('gameoflife', Factory);

    function Factory($http, gameClient){
        var URL = 'v1/games/';
        var subscription;

        var start = function(data, onMessage, onFinish){
            if ( !subscription ) {
                $http.post(URL, data).then(function(result){
                    var gameToken = result.data.token;
                    gameClient.client.then(function(frame){
                        var queue = '/queue/playing-'+gameToken;
                        subscription = gameClient.stomp.subscribe(queue, function(data){
                            if ( data.status == 'RUNNING' ) {
                                onMessage(data.rows);
                            } else {
                                onFinish();
                            }
                        });
                    })
                });
            }
        }

        var stop = function(){
            subscription.unsubscribe();
            subscription = null;
        }

        return {
            start: start,
            stop: stop
        };
    }

})(angular);
