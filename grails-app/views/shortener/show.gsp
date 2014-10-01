<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label', default: 'Shortener')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<shortener:showWarningIfNotActive shortener="${shortenerInstance}"/>



<h1><g:message code="default.show.label" args="[entityName]"/></h1>


<div class="row">

    <div class="col-sm-9">
        <g:render template="shortenerDetails"/>
    </div>

    <div class="col-sm-3">

        <g:render template="showActions" />
    </div>

</div>


<h1><g:message code="button.statistics.label" /></h1>
<p>
    Redirect-Counter: <span class="badge">${redirectCounter}</span>
</p>


<div class="row">
    <div class="col-sm-6">
        <g:render template="/statistics/totalRedirectsPerMonth"/>
    </div>
</div>

</body>
</html>
