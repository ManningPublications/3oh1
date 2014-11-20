<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<h1><g:message code="default.edit.label" args="[entityName]"/></h1>

<g:form url="[resource: 'user/password', userId: params.userId, action: 'update']" method="PUT">

    <f:with bean="${passwordChangeCommandInstance}">
        <f:field property="username" input-class="form-control"/>
        <f:field property="password" input-class="form-control"/>
        <f:field property="confirmPassword" input-class="form-control"/>
    </f:with>

    <button type="submit" class="btn btn-success save" onclick="submit();">
        <span class="glyphicon glyphicon-disk"></span>
        <g:message code="default.button.update.label"/>
    </button>
</g:form>
</body>
</html>
