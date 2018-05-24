<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<h2>
    <span class="fa fa-list"></span>
    <g:message code="default.list.label" args="[entityName]"/>
</h2>

<div class="row">
    <div class="col-sm-2 col-sm-push-10">

        <g:link elementId="addUser" class="btn btn-default pull-right" action="create">
            <span class="fa fa-plus"></span>
            <g:message code="default.new.label" args="[entityName]"/>
        </g:link>

    </div>

</div>

<table id="userList" class="table table-striped table-condensed" style="margin-top:20px;">
    <thead>
    <tr>

        <g:sortableColumn property="username" title="${message(code: 'user.username.label')}"/>

        <g:sortableColumn property="authorities" title="${message(code: 'role.label')}"/>


    </tr>
    </thead>
    <tbody>
    <g:each in="${userInstanceList}" status="i" var="userInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

            <td><g:link action="show"
                        id="${userInstance.username}">${fieldValue(bean: userInstance, field: "username")}</g:link></td>

            <td>${userInstance.authorities*.authority.join(", ")}</td>

        </tr>
    </g:each>
    </tbody>
</table>

<div class="pagination">
    <g:paginate total="${userInstanceCount ?: 0}"/>
</div>

</body>
</html>
