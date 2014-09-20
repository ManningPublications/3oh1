<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<h2>
    <span class="glyphicon glyphicon-th-list"></span>
    <g:message code="default.list.label" args="[entityName]"/>
</h2>

<div class="row">

    <div class="col-sm-4">

        <div class="btn-group">
            <s:shortenerValidityButton property="expired"/>
            <s:shortenerValidityButton property="active"/>
            <s:shortenerValidityButton property="future"/>
        </div>
    </div>

    <div class="col-sm-4">

        <g:form url="[resource: shortenerInstance, action: 'index']" method="get">

            <div class="form-group">
                <input
                        id="search"
                        class="form-control"
                        placeholder="<g:message code="default.search.label"/>"
                        name="search"
                        type="text"
                        value="${params.search}"
                />

                <input name="validity" type="hidden" value="${params.validity}" />
            </div>
        </g:form>
    </div>

    <div class="col-sm-2 col-sm-push-2">

        <g:link elementId="addShortener" class="btn btn-default pull-right" action="create">
            <span class="glyphicon glyphicon-plus"></span>
            <g:message code="default.new.label" args="[entityName]"/>
        </g:link>

    </div>
</div>





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



