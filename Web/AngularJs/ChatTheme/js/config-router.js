chatApp.config(function($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/conversation');
    
    $stateProvider
        
        // HOME STATES AND NESTED VIEWS ========================================
        .state('conversation', {
            url: '/conversation',
            templateUrl: 'views/conversation.html'
        });
        
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
        /*.state('about', {
            // we'll get to this in a bit   
            url: '/about',
            templateUrl: '<h1>Hello World !!!!!!!!!!!!!!!!</h1>'
        });*/
        
});