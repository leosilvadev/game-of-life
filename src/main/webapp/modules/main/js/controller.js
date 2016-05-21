(function(angular){
    angular.module('main').controller('MainCtrl', Controller);

    function Controller($scope, gameoflife){
        var vm = this;

        vm.init = function(){
            vm.xAxis = 40;
            vm.yAxis = 40;
            vm.cicles = 10;
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
        }

        var buildMap = function(){
            vm.rows = [];
            for ( var y=0 ; y<vm.yAxis ; y++ ) {
                var columns = [];
                for ( var x=0 ; x<vm.xAxis ; x++ ) {
                    columns.push({alive:false, dead:true, position:[y,x]});
                }
                vm.rows.push(columns);
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

        var updateRows = function(rows){
            $scope.$apply(function(){
                vm.rows = rows;
            });
        }

        var cleanPoints = function(){
            vm.points = [];
            buildMap();
        }

        var start = function(){
            try {
                var data = buildStartRequest();
                gameoflife.start(data, updateRows, stop);
                vm.started = true;
            } catch(ex) {
                alert(ex.message);
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
