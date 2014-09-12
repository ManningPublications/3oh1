<%@ page import="com.manning.redirector.RedirectLog; com.manning.Shortener" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<h2>
    <span class="glyphicon glyphicon-stats"></span>
    <g:message code="button.statistics.label"/>
</h2>


<div class="row">
    <div class="col-sm-6">

        <div class="panel panel-default">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-share"></span> <g:message code="statistics.lastRedirects.title" />
            </div>


            <table class="table table-striped table-condensed" style="margin-top:20px;">
                <thead>
                <tr>
                    <th class="col-sm-9"><g:message code="shortener.destinationUrl.label" /></th>
                    <th class="col-sm-3"><g:message code="statistics.dateCreated.label" /></th>
                </tr>
                </thead>
                <tbody>
                    <g:render template="redirectLog" collection="${redirectLogInstanceList}"/>
                </tbody>
            </table>
        </div>

    </div>
</div>


</body>
</html>



