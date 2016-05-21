(function(angular){
    angular.module('main').controller('MainCtrl', Controller);

    function Controller($interval, $http){
        var interalTimer;
        var gameToken;
        var vm = this;

        vm.init = function(){
            vm.startWith = 40;
            vm.endWith = 40;
            vm.cicles = 10;
            vm.delay = 200;
            vm.points = '3,4-4,5-5,3-5,4-5,5';
            vm.started = false;
            bindMethods();
        }

        var bindMethods = function(){
            vm.start = start;
            vm.stop = stop;
        }

        var stop = function(){
            $interval.cancel(interalTimer);
            vm.started = false;
        }

        var start = function(){
            var initialPoints = vm.points.split('-').map(function(it){
                var point = it.split(',');
                return {x:point[0], y:point[1] }
            });
            var config = {
                x: vm.startWith,
                y: vm.endWith
            };
            var data = {
                initialPoints: initialPoints,
                config: config,
                cicles: vm.cicles,
                delay: vm.delay
            };
            $http.post('http://localhost:9000/v1/games', data).then(function(result){
                gameToken = result.data.token;
                vm.started = true;
                interalTimer = $interval(function(){
                    timer = $http.get('http://localhost:9000/v1/games/'+gameToken).then(function(res){
                        vm.rows = res.data.rows;
                    });
                }, 50);
            });
        }
    }

})(angular);
