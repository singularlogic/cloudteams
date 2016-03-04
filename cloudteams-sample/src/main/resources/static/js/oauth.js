/**
 * OAuth
 */


var AUTHORIZATION_HEADER = "Authorization";
var CLOUDTEAMS_REST_ENDPOINT = "http://cloudteams.euprojects.net/github/repository";

$(document).ready(function () {
    console.log("Loaded oauth.js");

    //Check if the project_id has been set

    if (typeof (ct_project_id) == 'undefined' || ct_project_id <= 0) {
        console.log("Invalid value for ct_project_id");
        return;
    }
    //Load Github widget
    loadGithubWidget();
});



/*
 *  Handlers
 */

function loadGithubWidget() {
    //Make the call to fect h github data
    $.post({
        beforeSend: function (xhr) {
            if (hasAccessToken()) {
                xhr.setRequestHeader(AUTHORIZATION_HEADER, localStorage.auth_token);
            }
        },
        data: {project_id : ct_project_id},
        url: CLOUDTEAMS_REST_ENDPOINT
    }).success(function (data, status, xhr) {
        $("#ct-content-github").html(data);
    }).fail(function (error) {
        console.error("[Cloudteams] Code: " + error.status + " Message: " + error.responseText);
    });
}

function hasAccessToken() {
    return null !== localStorage.getItem("auth_token");
}
