<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="${assetPath(src: 'favicon.ico')}">

    <title><g:layoutTitle default="Grails"/></title>

    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
</head>

<body>

<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li class="active">
                <g:link action="index">
                    <span class="glyphicon glyphicon-th-list"></span>
                    <g:message code="default.list.label" args="[message(code: 'shortener.label')]"/>
                </g:link></li>
        </ul>
        <asset:image src="logo.png" class="pull-left" id="logo"/>
        <h3 class="text-muted">Manning Url-Shortener</h3>
    </div>


    <g:if test="${flash.message}">
        <div class="alert alert-warning alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            ${flash.message}
        </div>
    </g:if>

    <g:layoutBody/>

    <div class="footer">
        <p>&copy; Manning Publications Co. 2014</p>
    </div>

</div>


<asset:javascript src="application.js"/>
</body>
</html>
