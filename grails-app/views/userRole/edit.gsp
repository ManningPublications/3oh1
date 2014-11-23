<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="userRole.edit.label"/></title>
</head>

<body>

<h1><g:message code="userRole.edit.label"/></h1>
<g:hasErrors bean="${roleChangeCommandInstance}">
    <ul class="errors" role="alert">
        <g:eachError bean="${roleChangeCommandInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                    error="${error}"/></li>
        </g:eachError>
    </ul>
</g:hasErrors>
<g:form url="[resource: userInstance, action: 'update']" method="PUT">
    <g:render template="form"/>

    <br/>

    <button type="submit" class="btn btn-success save" onclick="submit();">
        <span class="glyphicon glyphicon-disk"></span>
        <g:message code="default.button.update.label"/>
    </button>
</g:form>
</body>
</html>
