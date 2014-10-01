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


<p>
    Redirect-Counter (Total): <span class="badge">${redirectCounterTotal}</span>

</p>

<div class="row">
    <div class="col-sm-6">
        <g:render template="lastRedirectsPanel" />
    </div>



    <div class="col-sm-6">
        <g:render template="top5Panel" />

        <g:render template="totalRedirectsPerMonth"/>

    </div>

</div>


</body>
</html>



