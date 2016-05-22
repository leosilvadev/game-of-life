(function(angular){
    angular.module('main').controller('MainCtrl', Controller);

    function Controller($scope, gameoflife, message){
        var vm = this;
        var multSelectEnabled = false;

        vm.init = function(){
            vm.xAxis = 40;
            vm.yAxis = 40;
            vm.cicles = 100;
            vm.delay = 200;
            vm.started = false;
            vm.points = [];
            buildMap();
            bindMethods();
        }

        var bindMethods = function(){
            vm.start = start;
            vm.stop = stop;
            vm.point = point;
            vm.buildMap = buildMap;
            vm.cleanPoints = cleanPoints;
            vm.multSelect = multSelect;
            vm.startMultSelect = startMultSelect;
            vm.stopMultSelect = stopMultSelect;
        }

        var stopMultSelect = function(){
            multSelectEnabled = false;
        }

        var startMultSelect = function(){
            multSelectEnabled = true;
        }

        var buildMap = function(){
            vm.points = [];
            vm.streets = [];
            for ( var y=0 ; y<vm.yAxis ; y++ ) {
                var citizens = [];
                for ( var x=0 ; x<vm.xAxis ; x++ ) {
                    citizens.push({alive:false, dead:true, position:[y,x]});
                }
                vm.streets.push({citizens:citizens});
            }
        }

        var point = function(column){
            if ( !vm.started ) {
                column.dead = !column.dead;
                column.alive = !column.alive;

                var point = column.position[0]+'-'+column.position[1];
                if ( column.alive ) {
                    vm.points.push({text:point});
                } else {
                    var found = vm.points.findIndex(function(it){
                        return it.text == point
                    });
                    vm.points.splice(found, 1);
                }
            }
        }

        var stop = function(){
            gameoflife.stop();
            safeApply(function(){
                vm.started = false;
            });
        }

        var getInitialPoints = function(){
            if ( !vm.points ) {
                throw new Error('You must specify the start points');
            }

            if ( vm.points.length < 3 ) {
                throw new Error('You must specify at least three points');
            }

            var initialPoints = vm.points.map(function(point){
                var point = point.text.split('-');
                return {y:point[0], x:point[1] }
            });

            initialPoints.forEach(function(initialPoint){
                if ( !initialPoint.y || !initialPoint.x ) {
                    throw new Error('Invalid Points');
                }
                if ( initialPoint.y >= vm.yAxis || initialPoint.x >= vm.xAxis ) {
                    throw new Error('Start Points must be betwen the specified Y-X range');
                }
            });
            return initialPoints;
        }

        var buildStartRequest = function(){
            var initialPoints = getInitialPoints();

            var config = {
                x: vm.xAxis,
                y: vm.yAxis
            };
            var data = {
                initialPoints: initialPoints,
                config: config,
                cicles: vm.cicles,
                delay: vm.delay
            };
            return data;
        }

        var updateCity = function(streets){
            $scope.$apply(function(){
                vm.streets = streets;
            });
        }

        var cleanPoints = function(){
            vm.points = [];
            buildMap();
        }

        var start = function(){
            try {
                var data = buildStartRequest();
                gameoflife.start(data, updateCity, stop).catch(function(response){
                    message.warn('Error trying to start the Game, check the configuration and try again!')
                    stop();
                });
                vm.started = true;
            } catch(ex) {
                message.warn(ex.message)
            }
        }

        var multSelect = function(point) {
            if ( !vm.started ) {
                var pointText = point.position[0]+'-'+point.position[1];
                if(multSelectEnabled && point.dead) {
                    var found = vm.points.findIndex(function(it){
                        return it.text == pointText;
                    });
                    if ( found<0 ) {
                        point.alive = true;
                        point.dead = false;
                        vm.points.push({text: pointText});
                    }
                }
            }
        }

        var safeApply = function(fn) {
            var phase = $scope.$$phase;
            if(phase == '$apply' || phase == '$digest') {
                if(fn && (typeof(fn) === 'function')) {
                    fn();
                }
            } else {
                $scope.$apply(fn);
            }
        };
    }

})(angular);
