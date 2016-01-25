angular.module('ct-widgets')
        .controller('HomeCtrl', function ($scope, $http, $auth, toastr, $location) {
            $http.jsonp('https://api.github.com/repos/sahat/satellizer?callback=JSON_CALLBACK')
                    .success(function (data) {
                        if (data) {
                            if (data.data.stargazers_count) {
                                $scope.stars = data.data.stargazers_count;
                            }
                            if (data.data.forks) {
                                $scope.forks = data.data.forks;
                            }
                            if (data.data.open_issues) {
                                $scope.issues = data.data.open_issues;
                            }
                        }
                    });


            $scope.login = function () {
                $auth.login($scope.user)
                        .then(function () {
                            toastr.success('You have successfully signed in!');
                            $location.path('/');
                            console.log('1');
                        })
                        .catch(function (error) {
                            toastr.error(error.data.message, error.status);
                        });
            };
            $scope.authenticate = function (provider) {
                $auth.authenticate(provider)
                        .then(function () {
                            toastr.success('You have successfully signed in with ' + provider + '!');
                            console.log('2');
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
            };


        });

