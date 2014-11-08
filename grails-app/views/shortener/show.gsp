<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label', default: 'Shortener')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<shortener:showWarningIfNotActive shortener="${shortenerInstance}"/>



<h1><g:message code="default.show.label" args="[entityName]"/> <small>${shortenerInstance.key}</small></h1>


<div class="row">

    <div class="col-sm-9">
        <g:render template="shortenerDetails"/>
    </div>

    <div class="col-sm-3">

        <g:render template="showActions" />
    </div>

</div>


<h1><g:message code="button.statistics.label" /></h1>


<div class="row">
    <div class="col-sm-6">
        <div class="pull-right">
            Redirect-Counter: <span class="badge">${redirectCounter}</span>
            <g:link class="btn btn-default"
                    elementId="statistics-download-redirect-logs"
                    resource="shortener/redirectLog"
                    action="show"
                    shortenerId="${shortenerInstance.key}">
                <span class="glyphicon glyphicon-file"></span>
                <g:message code="shortener.redirectLogs.label"/>
            </g:link>
        </div>


    </div>
</div>

<div class="row">
    <div class="col-sm-6">
        <g:render template="/statistics/totalRedirectsPerMonth"/>
    </div>
</div>

</body>
</html>
