

<g:link class="btn btn-default btn-block" elementId="editShortener" action="edit"
        resource="shortener" id="${shortenerInstance.shortenerKey}">
    <span class="glyphicon glyphicon-pencil"></span>
    <g:message code="default.button.edit.label"/>
</g:link>


<g:form class="delete" url="[resource: 'shortener', action: 'delete', id: shortenerInstance.shortenerKey]" method="DELETE">

    <button type="submit" class="btn btn-block btn-danger" onclick="submit();">
        <span class="glyphicon glyphicon-remove"></span>
        <g:message code="default.button.delete.label"/>
    </button>

</g:form>
