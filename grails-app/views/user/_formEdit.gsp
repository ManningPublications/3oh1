<%@ page import="io.threeohone.security.Role" %>

<f:with bean="${userChangeCommandInstance}">
    <f:field property="username" input-class="form-control"/>
    <g:select from="${Role.list()}" name="role"
              optionKey="id" optionValue="authority"
              value="${userChangeCommandInstance.role.id}"
              class="form-control"></g:select>
</f:with>