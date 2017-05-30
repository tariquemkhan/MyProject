chatApp.controller('ChatWindowController', ['$rootScope', '$scope', '$location', '$timeout',
	function ($rootScope, $scope, $location, $timeout) {
	$scope.controllerName = "ChatWindow Controller";
	console.log("inside Chatwindow Controller : ");
	var path = $location.path();
	var urlId = path.substring(path.lastIndexOf("/")+1);
	console.log("Chat ID : "+urlId);
	$rootScope.currentChat = urlId;
	$scope.inputReferenceHeight = 41;
	$scope.inputHeight = "40px"
	//Initialize current chat information
	initilazeCurrentChat($scope, urlId);
	var composeInputElement = angular.element(document.querySelector('#inputMessageField'));
	//console.log(chatContainerElement1);

	//Adding watcher to message compose field
	$scope.$watch('inputMessage', function (oldValue, newValue) {
		var chatContainerElement = angular.element(document.querySelector('#chatContainer'));;
		var composeInputHeight = composeInputElement.height()+"px";
		//customLog($scope.controllerName, "inside watch : "+composeInputHeight);
		if ($scope.inputHeight != composeInputHeight) {
			//composeInputHeight = composeInputElement.height() - 10;
			$scope.inputHeight = composeInputHeight;
		}
	});

	$timeout(function() {
      var scroller = document.getElementById("chatContainer");
      scroller.scrollTop = scroller.scrollHeight;
    }, 0, false);

	//Send Message
	$scope.sendMessage = function(inputMessage) {
		customLog($scope.controllerName, "inside sendMessage() : ");
		if (typeof inputMessage != "undefined" && inputMessage != "") {
			$scope.messages[$rootScope.currentChat].push({
				"unique_id" : "3s2rv3ers5df5yfvds",
				"from_id" : "ere1a78db38b7ca37f4ef928bwew",
				"to_id" : "591a78db49fa9c12a3a6500c",
				"message" : inputMessage,
				"timestamp" : 1494918410
			})
			$timeout(function() {
		      var scroller = document.getElementById("chatContainer");
		      scroller.scrollTop = scroller.scrollHeight;
		    }, 0, false);
			$scope.inputMessage = "";
		}
	}
}]);

/**
 * initilaze current chat value
**/
function initilazeCurrentChat($scope, uniqueId) {
	$scope.currentChatObject = $scope.users[uniqueId];
	$scope.currentChatName = $scope.currentChatObject["name"];
	$scope.currentChatId = $scope.currentChatObject["_id"];
	//$scope.$parent.currentChat = $scope.currentChatObject["_id"];
}

chatApp.filter('messageFilter',function($filter){
	return function(items, field){
		var result = [];
		angular.forEach(items,function(item, key){
			if (key == field) {
				result.push(item);
			}
		});
		return result;
	}
});
//Directive for custon input field for sending messages
chatApp.directive("contenteditable", function() {
  return {
    restrict: "A",
    require: "ngModel",
    link: function(scope, element, attrs, ngModel) {

      	function read() {
          	// view -> model
          	var html = element.html();
          	//html = replaceHtmlEntites(html);
          	
          	//console.log("HTML : "+html)
          	ngModel.$setViewValue(html);
      	}
      // model -> view
      	ngModel.$render = function() {
          	element.html(ngModel.$viewValue || "");
      	};

      	element.bind("blur keyup change", function() {
        	scope.$apply(read);
      	});
    }
  };
});