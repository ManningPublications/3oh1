<tr>

    <td width="20%">

        <g:link action="show" id="${shortener.id}">
            ${fieldValue(bean: shortener, field: "shortenerKey")}
        </g:link>
        <g:link
                class="pull-right"
                target="_blank"
                absolute="true"
                url="${shortener.shortenerKey}"
        >
            <span class="glyphicon glyphicon-share"></span>
        </g:link>
    </td>

    <td><shortener:prettyDestinationUrl shortener="${shortener}" /></td>
    <td>${fieldValue(bean: shortener.userCreated, field: "username")}</td>

    <td>
        <g:formatDate date="${shortener.validFrom}" />
    </td>


    <g:if test="${shortener.validUntil}">
        <td><g:formatDate date="${shortener.validUntil}" /></td>
    </g:if>

</tr>