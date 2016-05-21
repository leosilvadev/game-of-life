(function(angular){
    angular.module('main').controller('MainCtrl', Controller);

    function Controller(gameoflife){
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
            vm.started = false;
            gameoflife.stop();
        }

        var buildStartRequest = function(){
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
            return data;
        }

        var start = function(){
            var data = buildStartRequest();
            gameoflife.start(data, function(rows){
                vm.rows = rows;
            }, stop);
            vm.started = true;
        }
    }

})(angular);
