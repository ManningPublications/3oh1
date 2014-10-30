<table class="table table-striped">
    <tr>
        <th><g:message code="shortener.shortUrl.label"/></th>
        <td id="shortUrl"><shortener:shortLink shortener="${shortenerInstance}"/></td>
    </tr>
    <tr>
        <th><g:message code="shortener.destinationUrl.label"/></th>
        <td>
            <g:link url="${shortenerInstance.destinationUrl}">
                <g:fieldValue bean="${shortenerInstance}" field="destinationUrl"/>
            </g:link>
        </td>
    </tr>
    <tr>
        <th><g:message code="shortener.userCreated.label"/></th>
        <td><g:fieldValue bean="${shortenerInstance.userCreated}" field="username"/></td>
    </tr>
    <tr>
        <th><g:message code="shortener.validFrom.label"/></th>
        <td><g:formatDate date="${shortenerInstance.validFrom}"/></td>
    </tr>

    <g:if test="${shortenerInstance.validUntil}">
        <tr>
            <th><g:message code="shortener.validUntil.label"/></th>
            <td><g:formatDate date="${shortenerInstance.validUntil}"/></td>
        </tr>
    </g:if>
</table>