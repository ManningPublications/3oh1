<div class="panel panel-default">
    <div class="panel-heading">
        <span class="glyphicon glyphicon-signal"></span> <g:message code="statistics.topShorteners.title" />
    </div>


    <table class="table table-striped table-condensed" style="margin-top:20px;">
        <thead>
        <tr>
            <th class="col-sm-4"><g:message code="shortener.shortenerKey.label" /></th>
            <th class="col-sm-4"><g:message code="shortener.destinationUrl.label" /></th>
            <th class="col-sm-4"><g:message code="Counter" /></th>
        </tr>
        </thead>
        <tbody>
            <g:each in="${top5}" var="i">
                <tr>
                    <td>
                        <g:link action="show" id="${i.shortener.id}">
                            ${fieldValue(bean: i.shortener, field: "shortenerKey")}
                        </g:link>
                    </td>
                    <td><shortener:prettyDestinationUrl shortener="${i.shortener}" link="${true}"/></td>
                    <td>${i.counter}</td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>
