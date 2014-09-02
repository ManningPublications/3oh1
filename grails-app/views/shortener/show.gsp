<%@ page import="com.manning.Shortener" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label', default: 'Shortener')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<h1><g:message code="default.show.label" args="[entityName]"/></h1>

<div class="row">

    <div class="col-sm-9">

        <table class="table table-striped">
            <tr>
                <th><g:message code="shortener.shortenerKey.label"/></th>
                <td><g:fieldValue bean="${shortenerInstance}" field="shortenerKey"/></td>
            </tr>
            <tr>
                <th><g:message code="shortener.destinationUrl.label"/></th>
                <td>
                    <g:link url="${shortenerInstance.destinationUrl}">
                        <g:fieldValue bean="${shortenerInstance}" field="destinationUrl"/>
                    </g:link>
                </td>
            </tr>
            <tr>
                <th><g:message code="shortener.userCreated.label"/></th>
                <td><g:fieldValue bean="${shortenerInstance}" field="userCreated"/></td>
            </tr>
            <tr>
                <th><g:message code="shortener.validity.label"/></th>


                <td>
                    <s:showValidity bean="${shortenerInstance}" />
                </td>
            </tr>

        </table>
    </div>

    <div class="col-sm-3">

        <g:link class="btn btn-default btn-block" action="edit" resource="${shortenerInstance}">
            <span class="glyphicon glyphicon-pencil"></span>
            <g:message code="default.button.edit.label" />
        </g:link>


        <g:form class="delete" url="[resource: shortenerInstance, action: 'delete']" method="DELETE">

            <button type="submit" class="btn btn-block btn-danger" onclick="submit();">
                <span class="glyphicon glyphicon-remove"></span>
                <g:message code="default.button.delete.label" />
            </button>

        </g:form>
    </div>

</div>

</body>
</html>
