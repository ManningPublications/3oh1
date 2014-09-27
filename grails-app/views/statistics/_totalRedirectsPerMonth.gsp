<div class="panel panel-default">
    <div class="panel-heading">
        <span class="glyphicon glyphicon-calendar"></span> <g:message
            code="statistics.totalRedirectsLastYearPerMonth.title"/>
    </div>


    <canvas id="totalRedirectsPerMonthDiagram"></canvas>

</div>




<asset:script>
    Chart.defaults.global.responsive = true;

    var data = {
        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        datasets: [
            {
                fillColor: "rgba(151,187,205,0.2)",
                strokeColor: "rgba(151,187,205,1)",
                pointColor: "rgba(151,187,205,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(151,187,205,1)",
                data: ${totalNumbersPerMonth}
            }
        ]
    };

    var ctx = $("#totalRedirectsPerMonthDiagram").get(0).getContext("2d");
    var myLineChart = new Chart(ctx).Line(data, {});

</asset:script>