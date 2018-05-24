<div class="panel panel-default">
    <div class="panel-heading">
        <span class="fa fa-calendar"></span> <g:message
            code="statistics.totalRedirectsLastYearPerMonth.title"/>
    </div>


    <canvas id="totalRedirectsPerMonthDiagram"></canvas>

</div>




<asset:script>

        var labels = [];
        <g:each in="${totalNumberOfRedirectsPerMonth.monthNames}" var="month">
            labels.push('${month}');
        </g:each>

        Chart.defaults.global.responsive = true;

        var data = {
            labels: labels,
            datasets: [
                {
                    fillColor: "rgba(151,187,205,0.2)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: ${totalNumberOfRedirectsPerMonth.redirectCounters}
                }
            ]
        };

        var ctx = $("#totalRedirectsPerMonthDiagram").get(0).getContext("2d");
        var myLineChart = new Chart(ctx).Line(data, {});

</asset:script>