<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="${assetPath(src: 'favicon.ico')}">

    <title><g:layoutTitle default="Grails"/></title>

    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
</head>

<body>

<div class="container">
    <div class="header">

            <sec:ifLoggedIn>

                <g:form controller="logout" method="POST">
                    <button
                            id="logout"
                            type="submit"
                            class="btn btn-danger pull-right"
                            style="margin-left:10px;"
                            data-toggle="tooltip"
                            data-placement="bottom"
                            title="${message(code: 'default.button.logout.label')}">
                        <span class="fa fa-power-off"></span>
                    </button>

                </g:form>

                <g:link
                        elementId="changePassword"
                        url="[
                                resource: 'user/password',
                                action: 'edit',
                                userId: sec.loggedInUserInfo(field:'username')]"
                        class="btn btn-primary pull-right"
                        style="margin-left:10px;"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title="${message(code: 'user.password.edit.label')}">
                    <span class="fa fa-cog"></span>
                </g:link>


                <sec:ifAllGranted roles="ROLE_ADMIN">
                    <g:link
                            elementId="users"
                            controller="user"
                            action="index"
                            class="btn btn-primary pull-right"
                            style="margin-left:10px;"
                            data-toggle="tooltip"
                            data-placement="bottom"
                            title="${message(code: 'button.users.label')}">
                        <span class="fa fa-user"></span>
                    </g:link>
                </sec:ifAllGranted>

                <g:link
                        elementId="statistics"
                        controller="statistics"
                        action="index"
                        class="btn btn-primary pull-right"
                        style="margin-left:10px;"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title="${message(code: 'button.statistics.label')}">
                    <span class="fa fa-chart-bar"></span>
                </g:link>

                <div class="btn-group pull-right">
                    <button
                            id="shortenersMenu"
                            type="button"
                            class="btn btn-primary dropdown-toggle"
                            data-toggle="dropdown">
                        <span class="fa fa-list"></span> <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li>
                            <g:link
                                    elementId="myShorteners"
                                    url="[resource: 'user/shortener', userId: sec.loggedInUserInfo(field:'username') ]"
                                    >
                                <g:message code="shortener.menu.my.label" />
                            </g:link>
                        </li>
                        <li>
                            <g:link
                                    elementId="allShorteners"
                                    url="[resource: 'shortener']"
                                    >
                                <g:message code="shortener.menu.all.label" />
                            </g:link>
                        </li>
                    </ul>
                </div>




            </sec:ifLoggedIn>
        <asset:image src="logo.png" class="pull-left" id="logo"/>
        <h3 class="text-muted">3oh1 URL-Shortener</h3>
    </div>


    <g:if test="${flash.message}">
        <div class="alert alert-warning alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span
                    class="sr-only">Close</span></button>
            ${flash.message}
        </div>
    </g:if>

    <g:if test="${flash.error}">
        <div class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span
                    class="sr-only">Close</span></button>
            ${flash.error}
        </div>
    </g:if>

    <g:hasErrors>
        <div class="alert alert-danger alert-dismissible" role="alert">
            <ul>
                <g:eachError var="error">
                    <li><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>

    <g:layoutBody/>

    <div class="footer">

        <div class="pull-right">
        <sec:ifLoggedIn>
         <g:link mapping="apiDocs">API</g:link> |
        </sec:ifLoggedIn>
        powered by <a href="http://3oh1.io">3oh1.io</a>
        </div>
    </div>

</div>


<asset:javascript src="application.js"/>
<asset:deferredScripts/>
</body>
</html>
