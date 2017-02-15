/**
 * CloudTeams Integration JS
 */
var AUTHORIZATION_HEADER = "Authorization";
var CLOUDTEAMS_GITHUB_REST_ENDPOINT = "https://cloudteams.euprojects.net/github/api/v1";
var CLOUDTEAMS_SONARQUBE_REST_ENDPOINT = "https://cloudteams.euprojects.net/sonar/api/v1";
var CLOUDTEAMS_BITBUCKET_REST_ENDPOINT = "https://cloudteams.euprojects.net/bitbucket/api/v1";
var CLOUDTEAMS_JIRA_REST_ENDPOINT = "https://cloudteams.euprojects.net/jira/api/v1";
var CLOUDTEAMS_PAASPORT_REST_ENDPOINT = "https://cloudteams.euprojects.net/paasport/api/v1";

$(document).ready(function () {
    console.log("Loaded oauth.js");
    //Check if the project_id has been set
    if (typeof (ct_project_id) === 'undefined' || ct_project_id <= 0) {
        console.log("Invalid value for ct_project_id");
        return;
    }
    var LOADIND_DIV = '<div id="widget-loading" style="text-align: center;"><div class="loader-general loader-general-gray" id="loader-general-1"></div><p><a href="#loader-general-1">Please wait...</a></p></div>';
    //Show animation on ajax call
    /*var LOADIND_DIV = '<div id="widget-loading" align="middle" id="loading"> <img style="width:auto; margin:100px 0" align="center" src="data:image/gif;base64,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"/><br/><div class="alert alert-warning">Please wait...</div></div>';*/
    $(document).ajaxStart(function () {
        $("#ct-content-github").html(LOADIND_DIV);
        $("#ct-content-sonarqube").html(LOADIND_DIV);
        $("#ct-content-bitbucket").html(LOADIND_DIV);
        $("#ct-content-jira").html(LOADIND_DIV);
        $("#ct-content-paasport").html(LOADIND_DIV);
        $("#widget-loading").show();
    });
    //Load Github widget
    loadGithubWidget();
    //Load Sonarqube widget
    loadSonarqubeWidget();
    //Load Bitbucket widget
    loadBitbucketWidget();
    //Load Jira widget
    loadJiraWidget();
    //Load PaaSport widget
    loadPaaSportWidget();
});

// Replacement for classic js alert
function customModal(title, subtitle, body, btnLabel) {
    var modal = `<div id="custom-alert" class="delete-element-modal-popup modal fade" style="display:block;opacity:1" role="dialog">` +
                     `<div class="modal-dialog medium" style="transform: translate(0, 5%);">` +
                         `<div class="block block-fill">` +
                             `<header class="modal-header">` +
                                 `<div class="vertical-align">` +
                                     `<div class="middle">` +
                                         `<i class="icon icon-user"></i>` +
                                         `<h2 class="header-medium secondary">${title}</h2>` +
                                     `</div>` +
                                     `<div class="close" data-dismiss="modal" onclick="closeCustomModal(this)">` +
                                         `<i class="icon icon-close"></i>` +
                                     `</div>` +
                                 `</div>` +
                             `</header>` +

                             `<div class="modal-body">` +
                                 `<div class="row form-group">` +
                                     `<div class="col-md-12 text-center">` +
                                         `<p class="message">${subtitle}</p>` +
                                         `<p class="description">${body}</p>` +
                                     `</div>` +
                                 `</div>` +
                                 `<div class="row">` +
                                     `<fieldset class="col-md-12 form-submit" style="text-align:center">` +
                                         `<a href="#nowhere" data-dismiss="modal" class="btn-transparent" alt="cancel" onclick="closeCustomModal(this)">${btnLabel}</a>` +
                                     `</fieldset>` +
                                 `</div>` +
                             `</div>` +
                         `</div>` +
                     `</div>` +
                 `</div>`;

    $("body").append(modal);

    /*$("body #custom-alert").animate({
        opacity: 1
    }, 300);*/
}

function closeCustomModal($this) {
    //var parent = $($this).parents(".modal");

    $("body #custom-alert").animate({
        'opacity': 0
    }, 300, function() {
        $("body #custom-alert").remove();
    });
}

/*
 *  Handlers for Github widget
 */

var GITHUB_AUTHORIZE_URL = "https://github.com/login/oauth/authorize?response_type=code&amp;client_id=9aababac7a8ec6c9659e&amp;redirect_uri=https://cloudteams.euprojects.net/github/api/v1/github/auth?username=" + ct_user_name + "&amp;scope=public_repo%20repo%20repo:status";
var POPUP;

function githubOnDOMLoad() {
    console.log("githubOnDOMLoad()");

    //Check if an outdated token exists then removed
    if (hasGithubAccessToken()) {
        localStorage.removeItem("github_auth_token");
    }

    /*if ($("section.developer-dashboard-project-campaigns-content .custom-alert").length == 0) {
        $("section.developer-dashboard-project-campaigns-content").prepend('<div style="display:none" class="alert alert-danger custom-alert"></div>');
    }*/
}

function authorizeGithub() {
    console.log("authorizeGithub()");

    POPUP = window.open(GITHUB_AUTHORIZE_URL, "mywindow", "status=0,toolbar=0,resizable=0,scrollbars=0,width=1020,height=618");

    $.post({
        data: {
            username: ct_user_name
        },
        url: CLOUDTEAMS_GITHUB_REST_ENDPOINT + "/auth/token"
    }).success(function (data, status, xhr) {
        var res = JSON.parse(data);

        if ("SUCCESS" === res.code) {
            console.log("authorizeGithub() => Success");
            localStorage.github_auth_token = res.token;
            loadGithubWidget();
            if ("message" in res) {
                customModal("Info", "", res.message, "OK");
            }
        }
    });
};

function loadGithubWidget() {
    //Make the call to fect h github data
    $.post({
        beforeSend: function (xhr) {
            if (hasGithubAccessToken()) {
                xhr.setRequestHeader(AUTHORIZATION_HEADER, localStorage.github_auth_token);
            }
        },
        data: {
            project_id: ct_project_id
        },
        url: CLOUDTEAMS_GITHUB_REST_ENDPOINT + "/github/repository"
    }).success(function (data, status, xhr) {
        $("#ct-content-github").html(data);
    }).fail(function (error) {
        console.error("[Cloudteams] Code: " + error.status + " Message: " + error.statusText);
    });
};

function hasGithubAccessToken() {
    return null !== localStorage.getItem("github_auth_token");
};

function githubDisconnect() {
    $.post({
        url: CLOUDTEAMS_GITHUB_REST_ENDPOINT + "/github/disconnect",
        data: {project_id: ct_project_id, projectName: $("#projects :selected").val()},
        beforeSend: function (xhr) {
            if (hasGithubAccessToken()) {
                xhr.setRequestHeader("Authorization", localStorage.github_auth_token);
            }
        },
    }).success(function (data, status, xhr) {
        var res = JSON.parse(data);
        if ("SUCCESS" === res.code) {
            console.log("Success disconnected account, reload GitHub widget");
            //Remove current accessToken
            removeAccessToken("github_auth_token");
            //Reload Github widget
            loadGithubWidget();
        } else {
            customModal("Info", "", res.message, "OK");
            console.log("Could not disconnect account");
        }
    }).error(function(jqXHR, textStatus, errorThrown) {
        var res = JSON.parse(jqXHR.responseText);
        console.error("Error Status: " + jqXHR.status + ". Error: " + res.error);
        /*if (res.message == "401 Unauthorized") {
            customModal("Info", "", "Wrong username or password", "OK");
        }*/
        loadGithubWidget();
    });
};

function githubAssignRepository() {
    $.post({
        url: CLOUDTEAMS_GITHUB_REST_ENDPOINT + "/github/add",
        data: {project_id: ct_project_id, reponame: $("#repository :selected").text()},
        beforeSend: function (xhr) {
            if (hasGithubAccessToken()) {
                xhr.setRequestHeader("Authorization", localStorage.github_auth_token);
            }
        },
    }).success(function (data, status, xhr) {
        var res = JSON.parse(data);
        if ("SUCCESS" === res.code) {
            //Reload Github widget
            loadGithubWidget();
        }
        console.log(res.message);
    }).error(function(jqXHR, textStatus, errorThrown) {
        var res = JSON.parse(jqXHR.responseText);
        console.error("Error Status: " + jqXHR.status + ". Error: " + res.error);
        /*if (res.message == "401 Unauthorized") {
            customModal("Info", "", "Wrong username or password", "OK");
        }*/
        loadGithubWidget();
    });
};

function githubUnassignRepository() {
    $.post({
        url: CLOUDTEAMS_GITHUB_REST_ENDPOINT + "/github/delete",
        data: {project_id: ct_project_id},
        beforeSend: function (xhr) {
            if (hasGithubAccessToken()) {
                xhr.setRequestHeader("Authorization", localStorage.github_auth_token);
            }
        },
    }).success(function (data, status, xhr) {
        var res = JSON.parse(data);
        if ("SUCCESS" === res.code) {
            //Reload Github widget
            loadGithubWidget();
        }
        console.log(res.message);
    }).error(function(jqXHR, textStatus, errorThrown) {
        var res = JSON.parse(jqXHR.responseText);
        console.error("Error Status: " + jqXHR.status + ". Error: " + res.error);
        /*if (res.message == "401 Unauthorized") {
            customModal("Info", "", "Wrong username or password", "OK");
        }*/
        loadGithubWidget();
    });
};

/*
 *  Handlers for Sonarqube widget
 */

function loadSonarqubeWidget() {
    //Make the call to fect h github data
    $.post({
        beforeSend: function (xhr) {
            if (hasSonarqubeAccessToken()) {
                xhr.setRequestHeader(AUTHORIZATION_HEADER, localStorage.sonarqube_auth_token);
            }
        },
        data: {project_id: ct_project_id},
        url: CLOUDTEAMS_SONARQUBE_REST_ENDPOINT + "/sonarqube/project"
    }).success(function (data, status, xhr) {
        $("#ct-content-sonarqube").html(data);
    }).fail(function (error) {
        console.error("[Cloudteams] Code: " + error.status + " Message: " + error.statusText);
    });
}

function hasSonarqubeAccessToken() {
    return null !== localStorage.getItem("sonarqube_auth_token");
}


/*
 *  Handlers for Bitbucket widget
 */

function loadBitbucketWidget() {
    //Make the call to fect h github data
    $.post({
        beforeSend: function (xhr) {
            if (hasBitbucketAccessToken()) {
                xhr.setRequestHeader(AUTHORIZATION_HEADER, localStorage.bitbucket_auth_token);
            }
        },
        data: {project_id: ct_project_id},
        url: CLOUDTEAMS_BITBUCKET_REST_ENDPOINT + "/bitbucket/repository"
    }).success(function (data, status, xhr) {
        $("#ct-content-bitbucket").html(data);
    }).fail(function (error) {
        console.error("[Cloudteams] Code: " + error.status + " Message: " + error.statusText);
    });
}

function hasBitbucketAccessToken() {
    return null !== localStorage.getItem("bitbucket_auth_token");
}

/*
 *  Handlers for Jira widget
 */

function loadJiraWidget() {
    //Make the call to fect h github data
    $.post({
        beforeSend: function (xhr) {
            if (hasJiraAccessToken()) {
                xhr.setRequestHeader(AUTHORIZATION_HEADER, localStorage.jira_auth_token);
            }
        },
        data: {project_id: ct_project_id},
        url: CLOUDTEAMS_JIRA_REST_ENDPOINT + "/jira/repository"
    }).success(function (data, status, xhr) {
        $("#ct-content-jira").html(data);
    }).fail(function (error) {
        console.error("[Cloudteams] Code: " + error.status + " Message: " + error.statusText);
    });
}

function hasJiraAccessToken() {
    return null !== localStorage.getItem("jira_auth_token");
}

/*
 *  Handlers for PaaSport widget
 */

function paasportOnDOMLoad() {
    console.log("paasportOnDOMLoad()");
    //Check if an outdated token exists then removed
    if (hasPaaSportAccessToken()) {
        localStorage.removeItem("paasport_auth_token");
    }

    /*if ($("section.developer-dashboard-project-campaigns-content .custom-alert").length == 0) {
        $("section.developer-dashboard-project-campaigns-content").prepend('<div style="display:none" class="alert alert-danger custom-alert"></div>');
    }*/
}

function synchronizePaaSport() {
    console.log("synchronizePaaSport()");

    var username = $("#paasport-username").val();
    var password = $("#paasport-password").val();

    if (!(username && password)) {
        console.log("Empty fields");
        customModal("Info", "", "Please fill out all the required fields", "OK");

        return;
    }

    $.post({
        data: {
            username: username,
            password: password
        },
        url: CLOUDTEAMS_PAASPORT_REST_ENDPOINT + "/auth/token"
    }).success(function (data, status, xhr) {
        var res = JSON.parse(data);
        //TODO - temporary alert message functionality
        //resetErrorMessage();
        if ("SUCCESS" === res.code) {
            localStorage.paasport_auth_token = res.token;
            loadPaaSportWidget();
            console.log("synchronizePaaSport() => Success");
        } else {
            //$("section.developer-dashboard-project-campaigns-content .custom-alert").html(res.message).fadeIn(400);
            customModal("Info", "", res.message, "OK");
            loadPaaSportWidget();
            console.log("synchronizePaaSport() => Could not synchronize");
        }
    }).error(function(jqXHR, textStatus, errorThrown) {
        var res = JSON.parse(jqXHR.responseText);
        console.error("Error Status: " + jqXHR.status + ". Error: " + res.error);
        if (res.message == "401 Unauthorized") {
            //$("section.developer-dashboard-project-campaigns-content .custom-alert").html("Wrong username or password").fadeIn(400);
            customModal("Info", "", "Wrong username or password", "OK");
        }
        loadPaaSportWidget();
    });
}

function loadPaaSportWidget() {
    console.log("loadPaaSportWidget()");

    //if (localStorage.paasport_auth_token === undefined) return;

    //Make the call to fetch PaaSport data
    $.post({
        beforeSend: function (xhr) {
            if (hasPaaSportAccessToken()) {
                xhr.setRequestHeader(AUTHORIZATION_HEADER, localStorage.paasport_auth_token);
            }
        },
        data: {project_id: ct_project_id},
        url: CLOUDTEAMS_PAASPORT_REST_ENDPOINT + "/paasport/project"
    }).success(function (data, status, xhr) {
        $("#ct-content-paasport").html(data);
        console.log("loadPaaSportWidget() => success");
    }).fail(function (error) {
        console.error("[Cloudteams] Code: " + error.status + " Message: " + error.statusText);
        console.log("loadPaaSportWidget() => Error");
    });
}

function hasPaaSportAccessToken() {
    console.log("hasPaaSportAccessToken()");
    return null !== localStorage.getItem("paasport_auth_token");
}

function resetErrorMessage() {
    $(".custom-alert").css('display', 'none');
    $(".custom-alert").html("");
}


/**
 * 
 * @param {type} TOKEN_KEY_NAME
 * @returns {undefined}
 */
function removeAccessToken(TOKEN_KEY_NAME) {
    localStorage.removeItem(TOKEN_KEY_NAME);
}