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
        <g:render template="lastRedirects/lastRedirectsPanel" />
    </div>



    <div class="col-sm-6">
        <g:render template="top5/top5Panel" />
    </div>

</div>

<div class="row">
    <div class="col-sm-6">
        <canvas id="myChart"></canvas>
    </div>

</div>


<asset:script>
    Chart.defaults.global.responsive = true;

    var data = {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        datasets: [
            {
                label: "My Second dataset",
                fillColor: "rgba(151,187,205,0.2)",
                strokeColor: "rgba(151,187,205,1)",
                pointColor: "rgba(151,187,205,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(151,187,205,1)",
                data: [28, 48, 40, 19, 86, 27, 90, 214, 190, 320, 100, 250]
            }
        ]
    };

    var ctx = $("#myChart").get(0).getContext("2d");
    var myLineChart = new Chart(ctx).Line(data, {});


</asset:script>
</body>
</html>



