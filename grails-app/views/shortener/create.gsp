<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label', default: 'Shortener')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:hasErrors bean="${shortenerInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${shortenerInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form url="[resource: shortenerInstance, action: 'save']">
        <g:render template="form"/>
        <button type="submit" class="btn btn-success" onclick="submit();">
            <span class="glyphicon glyphicon-disk"></span>
            <g:message code="default.button.create.label" />
        </button>

    </g:form>
</body>
</html>
