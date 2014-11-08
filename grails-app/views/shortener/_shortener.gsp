<tr>

    <td>


        <g:link action="show" id="${shortener.shortenerKey}">
            <shortener:shortUrl shortener="${shortener}"/>
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


<td>
    <g:if test="${shortener.validUntil}">
        <g:formatDate date="${shortener.validUntil}" />
    </g:if>
</td>
</tr>