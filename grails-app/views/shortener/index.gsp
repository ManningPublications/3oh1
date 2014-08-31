<%@ page import="com.manning.Shortener" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label', default: 'Shortener')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

    <g:link class="btn btn-default pull-right" action="create">
        <span class="glyphicon glyphicon-plus"></span>
        <g:message code="default.new.label" args="[entityName]"/>
    </g:link>
    <h2><g:message code="default.list.label" args="[entityName]"/></h2>

<div class="btn-group">
    <g:link class="btn btn-default ${params.validity == 'expired' ? 'active' : ''}" params="[validity: 'expired']">expired</g:link>
    <g:link class="btn btn-default ${params.validity == 'active' ? 'active' : ''}" params="[validity: 'active']">active</g:link>
    <g:link class="btn btn-default ${params.validity == 'future' ? 'active' : ''}" params="[validity: 'future']">future</g:link>
</div>

    <table class="table table-striped">
        <thead>
        <tr>
            <g:sortableColumn property="shortenerKey" title="${message(code: 'shortener.shortenerKey.label')}"/>
            <g:sortableColumn property="destinationUrl" title="${message(code: 'shortener.destinationUrl.label')}"/>
            <g:sortableColumn property="userCreated" title="${message(code: 'shortener.userCreated.label')}"/>
            <g:sortableColumn property="validFrom" title="${message(code: 'shortener.validity.label')}"/>
        </tr>
        </thead>
        <tbody>
            <g:render template="shortener" collection="${shortenerInstanceList}"/>
        </tbody>
    </table>

        <g:paginate total="${shortenerInstanceCount ?: 0}" action="index" controller="shortener" params="['validity' : params.validity]"/>
</body>
</html>



