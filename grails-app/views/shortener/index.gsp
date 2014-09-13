<%@ page import="com.manning.redirector.RedirectLog; com.manning.Shortener" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<g:form url="[resource: shortenerInstance, action: 'index']" method="get">
    <input id="search" name="search" type="text" value="${params.search}" />
    <button type="submit" styleClass="btn btn-default"><span class="glyphicon glyphicon-search"></span> <g:message code="default.search.label"/></button>

</g:form>

<h2>
    <span class="glyphicon glyphicon-th-list"></span>
    <g:message code="default.list.label" args="[entityName]"/>
</h2>

<div class="btn-group">
    <s:shortenerValidityButton property="expired"/>
    <s:shortenerValidityButton property="active"/>
    <s:shortenerValidityButton property="future"/>
</div>
<g:link elementId="addShortener" class="btn btn-default pull-right" action="create">
    <span class="glyphicon glyphicon-plus"></span>
    <g:message code="default.new.label" args="[entityName]"/>
</g:link>


<table class="table table-striped table-condensed" style="margin-top:20px;">
    <thead>
    <tr>
        <g:sortableColumn property="shortenerKey" title="${message(code: 'shortener.shortUrl.label')}"/>
        <g:sortableColumn property="destinationUrl" title="${message(code: 'shortener.destinationUrl.label')}"/>
        <g:sortableColumn property="userCreated" title="${message(code: 'shortener.userCreated.label')}"/>
        <g:sortableColumn property="validFrom" title="${message(code: 'shortener.validFrom.label')}"/>
        <g:sortableColumn property="validFrom" title="${message(code: 'shortener.validUntil.label')}"/>
    </tr>
    </thead>
    <tbody>
    <g:render template="shortener" collection="${shortenerInstanceList}"/>
    </tbody>
</table>

<g:paginate total="${shortenerInstanceCount ?: 0}" action="index" controller="shortener"
            params="['validity': params.validity]"/>

</body>
</html>



