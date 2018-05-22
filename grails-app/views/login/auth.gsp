<html>
<head>
    <title><g:message code="springSecurity.login.title"/></title>
    <meta name="layout" content="main">

</head>

<body>

<div class="row">

    <div class="col-md-6 col-md-push-3">
        <div class="panel panel-default">

            <div class="panel-heading">
                <h3 class="panel-title"><g:message code="springSecurity.login.header"/></h3>
            </div>


            <div class="panel-body">
                <form role="form" action='${postUrl}' method='POST' autocomplete='off'>
                    <fieldset>
                        <div class="form-group">
                            <input
                                    class="form-control"
                                    placeholder="${message(code: 'springSecurity.login.username.label')}"
                                    name="username"
                                    id="username"
                                    type="text"
                                    autofocus="">
                        </div>
                        <div class="form-group">

                            <input
                                    class="form-control"
                                    placeholder="${message(code: 'springSecurity.login.password.label')}"
                                    name="password"
                                    type="password"
                                    id="password"
                                    value="">
                        </div>
                        <div class="checkbox">
                            <label>

                                <input
                                        name="${rememberMeParameter}"
                                        type="checkbox"
                                        value="${message(code: 'springSecurity.login.remember.me.label')}"
                                        <g:if test='${hasCookie}'>checked='checked'</g:if>>
                                <g:message code="springSecurity.login.remember.me.label"/>
                            </label>
                        </div>

                        <input type='submit' id="submit" class="btn btn-sm btn-success" value='${message(code: "springSecurity.login.button")}'/>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>