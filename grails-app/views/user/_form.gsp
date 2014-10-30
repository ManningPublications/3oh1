<f:with bean="${userEditCommandInstance?:userCreateCommandInstance}">
    <f:field property="username" input-class="form-control"/>
    <f:field property="password" input-class="form-control"/>
    <f:field property="confirmPassword" input-class="form-control"/>
</f:with>