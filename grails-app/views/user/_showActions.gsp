<g:link class="btn btn-default btn-block" elementId="changePassword" action="edit"
        resource="user/password" userId="${userInstance.username}">
    <span class="fa fa-pencil-alt"></span>
    <g:message code="user.password.edit.label"/>
</g:link>

<g:link class="btn btn-default btn-block" elementId="edit" action="edit" controller="user"
        id="${userInstance.username}">
    <span class="fa fa-cog"></span>
    <g:message code="default.edit.label" args="[message(code:'user.label')]"/>
</g:link>

<g:form class="delete" url="[resource: userInstance, action: 'delete']" method="DELETE">

    <button id="delete" type="submit" class="btn btn-block btn-danger" onclick="submit();">
        <span class="fa fa-trash"></span>
        <g:message code="default.button.delete.label"/>
    </button>

</g:form>