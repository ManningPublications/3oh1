<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<h2>
    <span class="fa fa-list"></span>
    <g:message code="default.list.label" args="[entityName]"/>

    <g:if test="${user}">
        <small>${user.username}</small>
    </g:if>
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

        <g:form relativeUri="shorteners" method="get">

            <div class="form-group">
                <input
                        id="search"
                        class="form-control"
                        placeholder="<g:message code="default.search.label"/>"
                        name="search"
                        type="text"
                        value="${params.search}"/>

                <input name="validity" type="hidden" value="${params.validity}"/>
            </div>
        </g:form>
    </div>

    <div class="col-sm-2 col-sm-push-2">

        <g:link elementId="addShortener" class="btn btn-default pull-right" action="create">
            <span class="fa fa-plus"></span>
            <g:message code="default.new.label" args="[entityName]"/>
        </g:link>

    </div>
</div>

<table id="shortenerList" class="table table-striped table-condensed" style="margin-top:20px;">
    <thead>
    <tr>
        <g:set var="linkParams" value="[search : params.search, validity: params.validity, userId: params.userId]"/>

        <g:sortableColumn property="key" title="${message(code: 'shortener.shortUrl.label')}" params="${linkParams}"/>
        <g:sortableColumn property="destinationUrl" title="${message(code: 'shortener.destinationUrl.label')}" params="${linkParams}"/>
        <g:sortableColumn property="userCreated" title="${message(code: 'shortener.userCreated.label')}" params="${linkParams}"/>
        <g:sortableColumn property="validFrom" title="${message(code: 'shortener.validFrom.label')}" params="${linkParams}"/>
        <g:sortableColumn property="validFrom" title="${message(code: 'shortener.validUntil.label')}" params="${linkParams}"/>
    </tr>
    </thead>
    <tbody>
        <g:render template="shortener" collection="${shortenerInstanceList}"/>
    </tbody>
</table>

<g:if test="${!shortenerInstanceList}">
    <div class="alert alert-danger">
        <span class="fa fa-trash"></span>
        <g:message code="search.noResults"/>
    </div>
</g:if>

<g:if test="${params.userId}">
    <div class="paginate">
        <g:paginate total="${shortenerInstanceCount ?: 0}" action="index" controller="shortener"
                    params="['validity': params.validity, search: params.search, userId: params.userId]"/>
    </div>
</g:if>
<g:else>
    <div class="pagination">
        <g:paginate total="${shortenerInstanceCount ?: 0}" action="index" controller="shortener"
                    params="['validity': params.validity, search: params.search]"/>
    </div>
</g:else>
</body>
</html>



