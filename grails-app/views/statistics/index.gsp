<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="button.statistics.label" /></title>
</head>

<body>

<h2>
    <span class="fa fa-chart-bar"></span>
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
                <span class="fa fa-file"></span>
                <g:message code="shortener.redirectLogs.label"/>
            </g:link>
        </div>

    </div>
</div>


<div class="row">
    <div class="col-sm-5">

        <g:render template="top5Panel" />

        <g:render template="lastRedirectsPanel" />
    </div>



    <div class="col-sm-7">

        <g:render template="totalRedirectsPerMonth"/>


        <div class="row">
            <div class="col-sm-6">
                <g:render template="redirectCountsPerBrowser"/>
                </div>
            <div class="col-sm-6">
                <g:render template="redirectCountsPerOperatingSystem"/>
            </div>
        </div>


    </div>

</div>


</body>
</html>



