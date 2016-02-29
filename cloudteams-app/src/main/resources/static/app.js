angular.module('ct-widgets', ['ngResource', 'ngAnimate', 'toastr', 'ui.router', 'satellizer'])
        .config(function ($stateProvider, $urlRouterProvider, $authProvider, $locationProvider) {
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
                        templateUrl: 'partials/dashboard.html',
                        controller: 'HomeCtrl'
                    });

            $locationProvider.html5Mode({
                enabled: true,
                requireBase: false
            });

            $urlRouterProvider.otherwise('/');

            // Production endpoint of github authentication service
            $authProvider.github({
                url: 'http://cloudteams.euprojects.net/api/v1/auth/github',
                scope: ['user', 'public_repo', 'repo', 'repo:status'],
                clientId: '9aababac7a8ec6c9659e',
                redirectUri: 'http://cloudteams.euprojects.net/api/v1/auth/github'
            });
// 
            // Developemnt endpoint of github authentication service
//            $authProvider.github({
//                url: 'http://localhost:8080/api/v1/auth/github',
//                scope: ['user', 'public_repo', 'repo', 'repo:status'],
//                clientId: '413dd46351dc0c48f306',
//            });

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
