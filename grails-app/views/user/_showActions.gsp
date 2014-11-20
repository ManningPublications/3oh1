<g:link class="btn btn-default btn-block" elementId="changePassword" action="edit"
        resource="user/password" userId="${userInstance.username}">
    <span class="glyphicon glyphicon-pencil"></span>
    <g:message code="user.password.edit.label"/>
</g:link>


<g:form class="delete" url="[resource: userInstance, action: 'delete']" method="DELETE">

    <button id="delete" type="submit" class="btn btn-block btn-danger" onclick="submit();">
        <span class="glyphicon glyphicon-remove"></span>
        <g:message code="default.button.delete.label"/>
    </button>

</g:form>