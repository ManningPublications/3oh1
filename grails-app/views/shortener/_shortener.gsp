<tr>

    <td>
        <g:link action="show" id="${shortener.id}">
            ${fieldValue(bean: shortener, field: "shortenerKey")}
        </g:link>
    </td>

    <td>${fieldValue(bean: shortener, field: "destinationUrl")}</td>
    <td>${fieldValue(bean: shortener, field: "userCreated")}</td>

    <td>
        <g:formatDate date="${shortener.validFrom}" style="SHORT"/> -
        <g:formatDate date="${shortener.validUntil}" style="SHORT"/>
    </td>

</tr>