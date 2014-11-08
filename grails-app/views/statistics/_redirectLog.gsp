<tr>

    <td>
        <g:link resource="shortener" action="show" id="${redirectLog.shortener.key}">
            ${fieldValue(bean: redirectLog.shortener, field: "key")}
        </g:link>
    </td>
    <td><shortener:prettyDestinationUrl shortener="${redirectLog.shortener}" link="${true}"/></td>
    <td><g:formatDate date="${redirectLog.dateCreated}" type="time" style="SHORT" /></td>
</tr>