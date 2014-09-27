<div class="panel panel-default">
    <div class="panel-heading">
        <span class="glyphicon glyphicon-share"></span> <g:message code="statistics.lastRedirects.title" />
    </div>


    <table class="table table-striped table-condensed" style="margin-top:20px;">
        <thead>
        <tr>
            <th class="col-sm-9"><g:message code="shortener.destinationUrl.label" /></th>
            <th class="col-sm-3"><g:message code="statistics.dateCreated.label" /></th>
        </tr>
        </thead>
        <tbody>
        <g:render template="redirectLog" collection="${redirectLogInstanceList}"/>
        </tbody>
    </table>
</div>
