<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="button.statistics.label" /></title>
</head>

<body>

<h2>
    <span class="glyphicon glyphicon-stats"></span>
    <g:message code="button.statistics.label"/>
</h2>



<div class="row">
    <div class="col-sm-12">

        <div class="pull-right">
            Total Redirects: <span class="badge">${redirectCounterTotal}</span>

            <g:link class="btn btn-default"
                    elementId="statistics-download-redirect-logs"
                    controller="redirectLog"
                    action="index">
                <span class="glyphicon glyphicon-file"></span>
                <g:message code="shortener.redirectLogs.label"/>
            </g:link>
        </div>

    </div>
</div>


<div class="row">
    <div class="col-sm-6">

        <g:render template="top5Panel" />

        <g:render template="lastRedirectsPanel" />
    </div>



    <div class="col-sm-6">

        <g:render template="totalRedirectsPerMonth"/>

        <g:render template="redirectCountsPerOperatingSystem"/>

    </div>

</div>


</body>
</html>



