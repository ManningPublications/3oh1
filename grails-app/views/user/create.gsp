<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<h1><g:message code="default.create.label" args="[entityName]"/></h1>

<g:form url="[resource: userInstance, action: 'save']">
    <g:render template="formCreate"/>


    <button type="submit" class="btn btn-success save" onclick="submit();">
        <span class="glyphicon glyphicon-disk"></span>
        <g:message code="default.button.create.label"/>
    </button>
</g:form>
</body>
</html>
