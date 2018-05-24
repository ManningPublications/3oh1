<div class="panel panel-default">
    <div class="panel-heading">
        <span class="fa fa-chart-bar"></span> <g:message
            code="statistics.redirectCountsPerBrowser.title"/>
    </div>


    <canvas id="redirectsPerBrowser"  width="250" height="100"></canvas>

</div>


<asset:script>

    // create some random colors
    var colors = []
    colors = colors.concat(randomColor({ count: 5, hue: 'green' }));
    colors = colors.concat(randomColor({ count: 5, hue: 'orange' }));
    colors = colors.concat(randomColor({ count: 5, hue: 'blue', luminosity: 'light'  }));
    colors = colors.concat(randomColor({ count: 5, hue: 'red' }));
    colors = colors.concat(randomColor({ count: 5, hue: 'yellow', luminosity: 'dark'  }));
    colors = colors.concat(randomColor({ count: 5, hue: 'purple' }));


    var data = [];
    <g:each in="${redirectCountersPerBrowser}" var="browser">
        var randomColorIndex = Math.floor((Math.random() * 30) + 1);
        data.push({
            value: ${browser.redirectCounter},
            label: "${browser.label}",
            color: colors[randomColorIndex]
        });

        // remove the color from the list
        colors = $.grep(colors, function(value) { return value != randomColorIndex; });
    </g:each>


    Chart.defaults.global.responsive = true;

    var browserPieChartCtx = $("#redirectsPerBrowser").get(0).getContext("2d");
    var browserPieChart = new Chart(browserPieChartCtx).Pie(data, {});


</asset:script>