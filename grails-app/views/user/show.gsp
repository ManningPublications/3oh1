<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<h1><g:message code="default.show.label" args="[entityName]"/></h1>

<div class="row">

    <div class="col-sm-6">
        <g:render template="userDetails"/>
    </div>

    <div class="col-sm-3 pull-right">
        <g:render template="showActions" />
    </div>

</div>

</body>
</html>
