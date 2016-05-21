(function(SockJS, Stomp){
    angular.module('main').factory('gameClient', Factory);

    function Factory($stomp){
        var stompClient = $stomp.connect('/point');
        return {
            client: stompClient,
            stomp: $stomp
        };
    }

})(SockJS, Stomp);
