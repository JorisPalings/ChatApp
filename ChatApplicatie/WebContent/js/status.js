// AngularJS (Angular 1) version

// ng-app="app"
var app = angular.module('app', []);

// ng-controller="controller"
app.controller('controller', function($scope, $http) {
    $http.get("Controller?action=getStatuses")
    .then(function(response) {
    	// {{online}}, {{away}}, {{donotdisturb}} and {{offline}}
        $scope.online = response.data["Online"];
        $scope.away = response.data["Away"];
        $scope.donotdisturb = response.data["Do not disturb"];
        $scope.offline = response.data["Offline"];
    });
});