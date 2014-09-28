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
                            title="${message(code: 'default.button.logout.label')}"
                    >
                        <span class="glyphicon glyphicon-off"></span>
                    </button>

                </g:form>

                <g:link
                        elementId="statistics"
                        controller="statistics"
                        action="index"
                        class="btn btn-primary pull-right"
                        style="margin-left:10px;"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title="${message(code: 'button.statistics.label')}"
                >
                    <span class="glyphicon glyphicon-stats"></span>
                </g:link>

                <g:link
                        elementId="shorteners"
                        controller="shortener"
                        action="index"
                        class="btn btn-primary pull-right"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title="${message(code: 'default.list.label', args: [message(code: 'shortener.label')])}"
                >
                    <span class="glyphicon glyphicon-th-list"></span>
                </g:link>





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
    </div>

</div>


<asset:javascript src="application.js"/>
<asset:deferredScripts/>
</body>
</html>
