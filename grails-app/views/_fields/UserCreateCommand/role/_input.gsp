<%@ page import="io.threeohone.security.Role" %>
<g:select from="${Role.list()}" name="role" optionKey="id" optionValue="authority" class="form-control"></g:select>