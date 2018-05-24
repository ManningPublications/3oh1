

<g:link class="btn btn-default btn-block" elementId="editShortener" action="edit"
        resource="shortener" id="${shortenerInstance.key}">
    <span class="fa fa-pencil-alt"></span>
    <g:message code="default.button.edit.label"/>
</g:link>


<g:form class="delete" url="[resource: 'shortener', action: 'delete', id: shortenerInstance.key]" method="DELETE">

    <button type="submit" class="btn btn-block btn-danger" onclick="submit();">
        <span class="fa fa-share"></span>
        <g:message code="default.button.delete.label"/>
    </button>

</g:form>
