<tr>

    <td>
        <g:link resource="shortener" action="show" id="${redirectLog.shortener.shortenerKey}">
            ${fieldValue(bean: redirectLog.shortener, field: "shortenerKey")}
        </g:link>
    </td>
    <td><shortener:prettyDestinationUrl shortener="${redirectLog.shortener}" link="${true}"/></td>
    <td><g:formatDate date="${redirectLog.dateCreated}" type="time" style="SHORT" /></td>
</tr>