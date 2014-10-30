<div class="panel panel-default">
    <div class="panel-heading">
        <span class="glyphicon glyphicon-stats"></span> <g:message
            code="statistics.redirectCountsPerOperatingSystem.title"/>
    </div>


    <canvas id="redirectsPerOperatingSystem"  width="400" height="180"></canvas>

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
    <g:each in="${redirectCountersPerOperatingSystem}" var="os">
        var randomColorIndex = Math.floor((Math.random() * 30) + 1);
        data.push({
            value: ${os.redirectCounter},
            label: "${os.operatingSystem}",
            color: colors[randomColorIndex]
        });

        // remove the color from the list
        colors = $.grep(colors, function(value) { return value != randomColorIndex; });
    </g:each>


    Chart.defaults.global.responsive = true;

    var ctx = $("#redirectsPerOperatingSystem").get(0).getContext("2d");
    var myLineChart = new Chart(ctx).Pie(data, {});


</asset:script>