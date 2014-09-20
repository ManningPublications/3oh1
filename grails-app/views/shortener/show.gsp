<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'shortener.label', default: 'Shortener')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>



<shortener:showWarningIfNotActive shortener="${shortenerInstance}" />



<h1><g:message code="default.show.label" args="[entityName]"/></h1>


<ul class="nav nav-tabs" role="tablist">
    <li class="active"><a href="#information" role="tab" data-toggle="tab">Shortner Details</a></li>
    <li><a href="#statistics" role="tab" data-toggle="tab">Statistics</a></li>
</ul>


<div class="tab-content">
    <div class="tab-pane active" id="information">



        <div class="row">

            <div class="col-sm-9">

                <table class="table table-striped">
                    <tr>
                        <th><g:message code="shortener.shortUrl.label"/></th>
                        <td id="shortUrl"><shortener:shortLink shortener="${shortenerInstance}"/></td>
                    </tr>
                    <tr>
                        <th><g:message code="shortener.destinationUrl.label"/></th>
                        <td>
                            <g:link url="${shortenerInstance.destinationUrl}">
                                <g:fieldValue bean="${shortenerInstance}" field="destinationUrl"/>
                            </g:link>
                        </td>
                    </tr>
                    <tr>
                        <th><g:message code="shortener.userCreated.label"/></th>
                        <td><g:fieldValue bean="${shortenerInstance}" field="userCreated"/></td>
                    </tr>
                    <tr>
                        <th><g:message code="shortener.validFrom.label"/></th>
                        <td><g:formatDate date="${shortenerInstance.validFrom}"/></td>
                    </tr>

                    <g:if test="${shortenerInstance.validUntil}">
                        <tr>
                            <th><g:message code="shortener.validUntil.label"/></th>
                            <td><g:formatDate date="${shortenerInstance.validUntil}"/></td>
                        </tr>

                    </g:if>

                </table>
            </div>

            <div class="col-sm-3">

                <g:link class="btn btn-default btn-block" elementId="editShortener" action="edit"
                        resource="${shortenerInstance}">
                    <span class="glyphicon glyphicon-pencil"></span>
                    <g:message code="default.button.edit.label"/>
                </g:link>


                <g:form class="delete" url="[resource: shortenerInstance, action: 'delete']" method="DELETE">

                    <button type="submit" class="btn btn-block btn-danger" onclick="submit();">
                        <span class="glyphicon glyphicon-remove"></span>
                        <g:message code="default.button.delete.label"/>
                    </button>

                </g:form>
            </div>

        </div>


    </div>
    <div class="tab-pane" id="statistics">


        <p>
            Redirect-Counter: <span class="badge">
            ${io.threeohone.RedirectLog.where { shortener == shortenerInstance }.count()}
        </span>
        </p>

    </div>
</div>



</body>
</html>
