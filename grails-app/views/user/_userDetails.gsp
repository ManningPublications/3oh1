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

    <tr>
        <th><g:message code="user.enabled.label"/></th>
        <td><g:fieldValue bean="${userInstance}" field="enabled"/></td>
    </tr>

    <tr>
        <th><g:message code="user.accountExpired.label"/></th>
        <td><g:fieldValue bean="${userInstance}" field="accountExpired"/></td>
    </tr>

    <tr>
        <th><g:message code="user.accountLocked.label"/></th>
        <td><g:fieldValue bean="${userInstance}" field="accountLocked"/></td>
    </tr>

    <tr>
        <th><g:message code="user.passwordExpired.label"/></th>
        <td><g:fieldValue bean="${userInstance}" field="passwordExpired"/></td>
    </tr>
</table>