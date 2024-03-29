<div class="panel panel-default">
    <div class="panel-heading">
        <span class="fa fa-signal"></span> <g:message code="statistics.topShorteners.title" />
    </div>


    <table class="table table-striped table-condensed" style="margin-top:20px;">
        <thead>
        <tr>
            <th class="col-sm-4"><g:message code="shortener.key.label" /></th>
            <th class="col-sm-4"><g:message code="shortener.destinationUrl.label" /></th>
            <th class="col-sm-4"><g:message code="Counter" /></th>
        </tr>
        </thead>
        <tbody>
            <g:each in="${top5}" var="topEntry">
                <tr>
                    <td>
                        <g:link resource="shortener" action="show" id="${topEntry.shortener.key}">
                            ${fieldValue(bean: topEntry.shortener, field: "key")}
                        </g:link>
                    </td>
                    <td><shortener:prettyDestinationUrl shortener="${topEntry.shortener}" link="${true}"/></td>
                    <td>${topEntry.redirectCounter}</td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>
