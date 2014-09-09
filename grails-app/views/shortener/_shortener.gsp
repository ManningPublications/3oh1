<tr>

    <td>
        <g:link action="show" id="${shortener.id}">
            ${fieldValue(bean: shortener, field: "shortenerKey")}
        </g:link>
    </td>

    <td>${fieldValue(bean: shortener, field: "destinationUrl")}</td>
    <td>${fieldValue(bean: shortener.userCreated, field: "username")}</td>

    <td>
        <g:formatDate date="${shortener.validFrom}" />
    </td>


    <g:if test="${shortener.validUntil}">
        <td><g:formatDate date="${shortener.validUntil}" /></td>
    </g:if>

</tr>