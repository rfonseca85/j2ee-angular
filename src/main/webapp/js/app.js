var app = angular.module('j2eeAngular', [
  'ngRoute',
  'ngResource',
  'ngGrid',
  'ui.bootstrap',
  
  'homeControllers',
  'bookControllers'
]);


app.config([ '$routeProvider', function($routeProvider) {
	
	$routeProvider

	.when('/home', {
		templateUrl : 'partials/home.html',
		controller : 'homeCtrl'})

		
		.when('/page1-1', {
			templateUrl : 'partials/page1-1.html',
			controller : 'page1-1Ctrl'})		
	
		.when('/page1-2', {
			templateUrl : 'partials/page1-2.html',
			controller : 'page1-2Ctrl'})	

		.when('/page1-3', {
			templateUrl : 'partials/page1-3.html',
			controller : 'page1-3Ctrl'})				
			
			
	.when('/book', {
		templateUrl : 'partials/book.html',
		controller : 'bookCtrl'})

	.when('/page3', {
		templateUrl : 'partials/page3.html',
		controller : 'page3Ctrl'})		

	.otherwise({
		redirectTo : '/home'});
	
}]);


