<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>


    <script src="http://code.highcharts.com/highcharts.js"></script>
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


<div id="test" style="min-width: 310px; height: 400px; margin: 0 auto"></div>


<g:javascript>

    console.log("hallo");

    $(function() {

        console.log("hallo dr");

        $('#test').highcharts({
            title: {
                text: 'Total Redirects per Month',
                x: -20 //center
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: '# Redirects'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: ''
            },
            series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }]
        });
    });

</g:javascript>

</body>
</html>



