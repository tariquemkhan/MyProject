chatApp.controller('MainController',['$scope', function($scope) {
	console.log("inside MainController : ");
	$scope.ownerData = ownerData;
	$scope.users = userData;
	$scope.messages = messageData;
}]);

/**
 * Custom logger to print log
 * @param controllerName Name of controller whose log is to print
 * @param message text to print in log
**/
function customLog(controllerName, message) {
	console.log(controllerName+" : "+message);
}