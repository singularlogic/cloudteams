angular.module('ct-widgets', ['ngResource', 'ngAnimate', 'toastr', 'ui.router', 'satellizer'])
        .config(function ($stateProvider, $urlRouterProvider, $authProvider) {
            $stateProvider
                    .state('home', {
                        url: '/',
                        controller: 'HomeCtrl',
                        templateUrl: 'partials/home.html',
                        resolve: {
                            skipIfLoggedIn: skipIfLoggedIn
                        }
                    })
                    .state('logout', {
                        url: '/logout',
                        template: null,
                        controller: 'LogoutCtrl'
                    })
                    
                    .state('dashboard', {
                        templateUrl: 'templates/dashboard.html',
                        controller: 'HomeCtrl'
                    });
                    
//                    .state('profile', {
//                        url: '/profile',
//                        templateUrl: 'partials/profile.html',
//                        controller: 'ProfileCtrl',
//                        resolve: {
//                            loginRequired: loginRequired
//                        }
//                    });

            $urlRouterProvider.otherwise('/');

            //Set the endpoint of github authentication service
            $authProvider.github({
                url: 'http://cloudteams.euprojects.net/api/v1/auth/github',
                clientId: 'fdd1aeb4d0737dc97652'
            });

            function skipIfLoggedIn($q, $auth) {
                var deferred = $q.defer();
                if ($auth.isAuthenticated()) {
                    deferred.reject();
                    console.log('3');
                } else {
                    deferred.resolve();
                    console.log('4');
                }
                return deferred.promise;
            }

            function loginRequired($q, $location, $auth) {
                var deferred = $q.defer();
                if ($auth.isAuthenticated()) {
                    deferred.resolve();
                    console.log('5');
                } else {
                    $location.path('/login');
                    console.log('6');
                }
                return deferred.promise;
            }
        });
