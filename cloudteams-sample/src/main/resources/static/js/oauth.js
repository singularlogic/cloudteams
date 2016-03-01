/**
 * oAuth
 */

$(document).ready(function () {
    console.log("Loaded oauth.js");
});

/*
 *  Handlers
 */

function authorizeGithubHandler() {
    //Make the authorize call
    $.post({
        beforeSend: function (xhr){
            if (hasAccessToken()){
                xhr.setRequestHeader("Authorization", localStorage.auth_token);

            }
        },
        url: "http://localhost:8080/github/repository?project_id=" + ct_project_id
    }).success(function (data, status, xhr) {
        $("#ct-content-github").html(data);
    });
}

function hasAccessToken() {
    return null !== localStorage.getItem("auth_token");
}
