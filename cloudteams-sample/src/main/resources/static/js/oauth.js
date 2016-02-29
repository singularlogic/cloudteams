/**
 * oAuth
 */

$(document).ready(function () {
    console.log("Loaded oauth.js");
});

// Static variables
var GITHUB_AUTHORIZE_URL= "https://github.com/login/oauth/authorize?response_type=code&client_id=413dd46351dc0c48f306&redirect_uri=http://192.168.7.196:8080/github/auth?username=" + ct_user_name + "&scope=user%20public_repo%20repo%20repo:status";
var CLOUDTEAMS_URL = "http://192.168.7.196:8080/api/v1/auth/token?username=" + ct_user_name;
var POPUP;

/*
 *  Handlers
 */

function authorizeGithubHandler() {

    //Make the authorize call
    $.post({
        url: CLOUDTEAMS_URL
    }).success(function (data, status, xhr) {
        //console.log(data);
        var res = JSON.parse(data);
        if ("SUCCESS" === res.code) {
            console.log(data);
            localStorage.auth_token = res.token;
        } else {
            console.log("Could not authorize");
        }
        POPUP.close();
    });
}

function authorizeGithub(){
    POPUP = window.open(GITHUB_AUTHORIZE_URL, "mywindow","status=0,toolbar=0,resizable=0,scrollbars=0,width=800,height=600");
    authorizeGithubHandler();
}

