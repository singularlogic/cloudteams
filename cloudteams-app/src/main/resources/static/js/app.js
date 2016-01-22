var widget = angular.module('widget', ['ngResource', 'ngMessages', 'ngAnimate', 'toastr', 'ui.router', 'satellizer'])
        .config(function ($stateProvider, $urlRouterProvider, $authProvider) {
            $stateProvider
                    .state('home', {
                        url: '/',
                        controller: 'HomeCtrl',
                        templateUrl: null
                    })
                    .state('login', {
                        url: '/login',
                        templateUrl: 'views/remote-data',
                        controller: 'LoginCtrl',
                        resolve: {
                            skipIfLoggedIn: skipIfLoggedIn
                        }
                    });

            $urlRouterProvider.otherwise('/');
            $authProvider.github({
                clientId: 'fdd1aeb4d0737dc97652'
            });


            function skipIfLoggedIn($q, $auth) {
                var deferred = $q.defer();
                if ($auth.isAuthenticated()) {
                    deferred.reject();
                } else {
                    deferred.resolve();
                }
                return deferred.promise;
            }

            function loginRequired($q, $location, $auth) {
                var deferred = $q.defer();
                if ($auth.isAuthenticated()) {
                    deferred.resolve();
                } else {
                    $location.path('/login');
                }
                return deferred.promise;
            }


        });

widget.controller('LoginCtrl', function ($scope, $location, $auth, toastr) {


    $scope.login = function () {
        console.log("Origin location: " + window.location.origin);

        $auth.login($scope.user)
                .then(function () {
                    toastr.success('You have successfully signed in!');
                    $location.path('/');
                })
                .catch(function (error) {
                    toastr.error(error.data.message, error.status);
                });
    };

    $scope.authenticate = function (provider) {
        $auth.authenticate(provider)
                .then(function () {
                    toastr.success('You have successfully signed in with ' + provider + '!');
                    $location.path('/');
                })
                .catch(function (error) {
                    if (error.error) {
                        // Popup error - invalid redirect_uri, pressed cancel button, etc.
                        toastr.error(error.error);
                    } else if (error.data) {
                        // HTTP response error from server
                        toastr.error(error.data.message, error.status);
                    } else {
                        toastr.error(error);
                    }
                });
        console.log("Login contoller is loaded!");
    };
});




widget.controller('HomeCtrl', function ($scope, $http) {
    console.log("Home contoller is loaded!");
});




//    var window_dimensions = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes,status=yes"
//    window.opener = self
//    window.open("https://github.com/login/oauth/authorize?client_id=fdd1aeb4d0737dc97652&scope=user&state=gf8g8fgfggf454&redirect_uri=http://cloudteams.euprojects.net?uid=1", "_blank", window_dimensions);
//    window.moveTo(0, 0);
//    window.resizeTo(screen.width, screen.height - 100);
