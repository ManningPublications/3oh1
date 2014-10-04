<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">

    <title>30h1 - API Documentation</title>

</head>

<body>

<h3>API Documentation</h3>

<p>
    The most features of software are available via a JSON API. The API you'll find at <code>/api</code>.
All API endpoints are secured via HTTP Basic Authentication.
</p>

<div class="alert alert-danger">HTTP Basic Authentication transfers authentication credentials without encryption.
This means your Passwords will be transfered in <b>plaintext</b>.</div>

<div class="alert alert-info">If you want to secure these API calls, you have to secure
your Application via <a href="http://en.wikipedia.org/wiki/Transport_Layer_Security">TLS</a> (HTTPS).</div>

<br/><br/><br/><br/>
<h4>Read Shortener Information</h4>

<p>Details on a specific shortener can be received via the following HTTP Request:</p>

<p><code>GET /api/shorteners/[:id]</code></p>

<p>The response will contain metadata information about the requested shortener. A sample JSON respone looks like the following:</p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;class: "io.threeohone.Shortener",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;id: 1,<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;destinationUrl: "http://www.example.com",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;shortenerKey: "5N",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;userCreated: {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class: "io.threeohone.security.User",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;id: 2<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;},<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;validFrom: "2014-10-04T19:35:34Z",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;validUntil: "2014-10-05T19:35:34Z"<br/>
        }
    </samp>
</div>

<h4>Read statistical Shortener Information</h4>

<p>Additionally statistical information for a specific shortener can be received via the following HTTP Request:</p>

<p><code>GET /api/shorteners/[:id]/statistics</code></p>

<p>The response will contain statistical information about the requested shortener. A sample JSON respone looks like the following:</p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;...<br/>
        }
    </samp>
</div>



<br/><br/><br/><br/>
<h4>Create Shortener Information</h4>

<p>A new shortener can be created via the following HTTP Request:</p>

<p><code>POST /api/shorteners</code></p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;destinationUrl: "http://www.example.com",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;validFrom: "2014-10-04T19:35:34Z",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;validUntil: "2014-10-05T19:35:34Z"<br/>
        }
    </samp>
</div>

<div class="alert alert-warning">
    <h4>Import existing shorteners</h4>
    <p>
        If you want to import existing shorteners, that already have shortenerKeys assigned,
        you can just add the shortenerKey to the request: <code>shortenerKey: "5N"</code>. If a shortener with this
        shortenerKey already exists in the system, the HTTP Status Code <code>422 - Unprocessable Entity</code> is returned
        with additional information about the validation errors.
    </p>
</div>

</body>
</html>