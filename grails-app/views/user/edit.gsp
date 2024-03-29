<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<h1><g:message code="default.edit.label" args="[entityName]"/></h1>
<g:hasErrors bean="${userChangeCommandInstance}">
    <ul class="errors" role="alert">
        <g:eachError bean="${userChangeCommandInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                    error="${error}"/></li>
        </g:eachError>
    </ul>
</g:hasErrors>
<g:form url="[resource: 'user', id: userChangeCommandInstance.username, action: 'update']" method="PUT">
    <g:render template="formEdit"/>

    <br/>

    <button type="submit" class="btn btn-success save" onclick="submit();">
        <span class="fa fa-save"></span>
        <g:message code="default.button.update.label"/>
    </button>
</g:form>
</body>
</html>
