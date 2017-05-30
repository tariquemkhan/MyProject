chatApp.controller('ConversationController', ['$rootScope', '$scope', '$state',
	function ($rootScope, $scope, $state) {
	$scope.controllerName = "Conversation controller";
	console.log("inside ConversationController : ");
	$rootScope.currentChat = null;
	//$scope.currentChatId = "reqrwqefdgr3ergfvdcedsa";
	$scope.openChatWindow = function (id) {
		console.log("inside click : "+id);
		$rootScope.currentChat = id;
		$state.go('conversation.chatwindow', {chatId : id});
	}
}]);

chatApp.filter('orderObjectBy', function() {
  return function(items, field, reverse) {
    var filtered = [];
    angular.forEach(items, function(item) {
      filtered.push(item);
    });
    filtered.sort(function (a, b) {
      return (a[field] > b[field] ? 1 : -1);
    });
    if(reverse) filtered.reverse();
    return filtered;
  };
});

