<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">

    <title>30h1 - API Documentation</title>

</head>

<body>

<h3>API Documentation</h3>

<p>The most features of software are available via a JSON API. The API you'll find at <code>/api</code>.
All API endpoints are secured via HTTP Basic Authentication.
</p>

<div class="alert alert-danger">HTTP Basic Authentication transfers authentication credentials without encryption.
This means your Passwords will be transfered in <b>plaintext</b>.</div>

<div class="alert alert-info">If you want to secure these API calls, you have to secure
your Application via <a href="http://en.wikipedia.org/wiki/Transport_Layer_Security">TLS</a> (HTTPS).</div>

<br/><br/><br/><br/>
<h3>Read Shortener Information</h3>

<p>Details on a specific shortener can be received via the following HTTP Request:</p>

<p><code>GET /api/shorteners/[:key]</code></p>

<p>The response will contain metadata information about the requested shortener. A sample JSON respone looks like the following:</p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;destinationUrl: "http://www.example.com",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;key: "5N",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;userCreated: "username",<br />
        &nbsp;&nbsp;&nbsp;&nbsp;validFrom: "2014-10-04T19:35:34Z",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;validUntil: "2014-10-05T19:35:34Z"<br/>
        }
    </samp>
</div>


<div class="alert alert-warning">
    <p>Setting the <code>validUntil</code> attribute is optional. If it is not set, the shortener is infinite valid.</p>
</div>

<h3>Read statistical Shortener Information</h3>

<p>Additionally statistical information for a specific shortener can be received via the following HTTP Request:</p>

<p><code>GET /api/shorteners/[:key]/statistics</code></p>

<p>The response will contain statistical information about the requested shortener. A sample JSON respone looks like the following:</p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;key: '5N',<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;redirectCounter: 0,<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;totalNumberOfRedirectsPerMonth: [<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;month: "10",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;year: "2013",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redirectCounter: 5,<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;month: "11",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;year: "2013",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redirectCounter: 10,<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// ...<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;]<br/>
        }<br/>
    </samp>
</div>



<br/><br/><br/><br/>
<h3>Create new Shortener</h3>

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
        If you want to import existing shorteners, that already have keys assigned,
        you can just add the key to the request: <code>key: "5N"</code>.
    </p>
</div>

<p>If validation errors occur while creating the new shortener, the HTTP Status Code <code>422 - Unprocessable Entity</code> is returned
with additional information about the validation errors. An example of a response with errors could look like this:</p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;"errors": [<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"object": "io.threeohone.Shortener",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"field": "key",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"rejected-value": "5N",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"message": "Shortener with value [5N] must be unique"<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"object": "io.threeohone.Shortener",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"field": "destinationUrl",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"rejected-value": "invalidUrl",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"message": "Destination with value [invalidUrl] is not a valid URL"<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;]<br/>
        }
    </samp>
</div>


<br/><br/><br/><br/>
<h3>Create new Users</h3>

<p>To create users via the API, send the following JSON Request this URL:</p>

<p><code>POST /api/users</code></p>

<div class="well">
    <samp>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;username: "john@family-doe.com",<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;password: "theSecretPassword"<br/>
        }
    </samp>
</div>

<div class="alert alert-danger">As already described above, we encourage you to secure your 3oh1 installation via a SSL-Certificate. Otherwise the JSON content, as well as the Basic Authentication Information are transfered in plaintext.</div>

<p>If validation errors occur while creating the new shortener, the HTTP Status Code <code>422 - Unprocessable Entity</code> is returned
with additional information about the validation errors. The schema of the response object looks like the one above.</p>

<p>To check if a user alredy exists in the system, you can do a <code>GET /api/users/<i>usernameToCheckFor</i></code>. This will either return a <code>200 - OK</code> in case that the user is already in the system. Otherwise a <code>404 - Not found</code> is returned.</p>


</body>
</html>