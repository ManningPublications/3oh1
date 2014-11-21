<table class="table table-striped">
    <tr>
        <th class="col-sm-5"><g:message code="user.username.label"/></th>
        <td class="col-sm-7" id="username">
            <g:fieldValue bean="${userInstance}" field="username"/>
        </td>
    </tr>

    <tr>
        <th class="col-sm-5"><g:message code="role.label"/></th>
        <td class="col-sm-7">
            ${userInstance.authorities*.authority.join(", ")}
        </td>
    </tr>

</table>