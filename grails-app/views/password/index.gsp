<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<h1><g:message code="default.edit.label" args="[entityName]"/></h1>
<g:hasErrors bean="${passwordChangeCommandInstance}">
    <ul class="errors" role="alert">
        <g:eachError bean="${passwordChangeCommandInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                    error="${error}"/></li>
        </g:eachError>
    </ul>
</g:hasErrors>
<g:form url="[controller: 'Password', action: 'update']" method="PUT">

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
