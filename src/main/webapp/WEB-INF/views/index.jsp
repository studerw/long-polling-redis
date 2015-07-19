<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="context" value="${pageContext.request.contextPath}/app"/>


<!DOCTYPE html>
<html lang="en">

<head>
    <title>Redis Long Polling Example</title>
    <link rel="icon" href="${context}/resources/images/favicon.ico">
    <link href="${context}/resources/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
    <style type="text/css">
        body {
            padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
            padding-bottom: 40px;
            background-color: #f5f5f5;
        }
    </style>


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="${context}/resources/js/html5shiv.js" type="text/javascript"></script>
    <script src="${context}/resources/js/respond.min.js" type="text/javascript"></script>
    <![endif]-->

</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${context}/index">Long Polling Example</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <!--<li class="active"><a href="#" th:href="@{/}">Home</a></li>-->
                <!--<li><a href="#about">About</a></li>-->
                <!--<li><a href="#contact">Contact</a></li>-->
            </ul>
            <ul class="nav navbar-nav navbar-right">

            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>

<div class="container" style="background-color: white;">
    <div class="row">
        <div class="col-md-7">
            <p class="bg-info" style="padding: 1em;">
                Add messages using the form on the right. The spinners represent ongoing requests.
            </p>
            <hr/>
            <h3>Async Long Polling</h3>

            <p>
                The left column below shows the output from a front-end approach using async requests on the server and a
                recursive
                function on the front-end to re-call itself upon completion / timeout of each request. Requests on the
                back-end
                will simply block (though without holding the request thread) until Redis informs the waiting requests that a new message
                has been added.
                The spinner will constantly spin showing that the request has been started, but does not return immediately.

            </p>

            <h3>Synchronous Polling</h3>

            <p>
                The right column shows the traditional attempt with polling. Every 10 seconds (configurable in
                app.properties
                file),
                the front-end will make a call to the server requesting new messages. The server responds immediately with
                the
                result.
                If messages are created less frequently than the poll time, most of the requests are wasted.

                The right spinner will almost always be invisible due to the fact that the requests return immediately. However,
                notice that the number of sync requests occur far more than the async version.
            </p>
        </div>
        <div>
            <h4>Send message</h4>

            <form id="appMsgForm" class="form-inline">
                <div class="form-group">
                    <input type="text" class="form-control" id="postMsg" name="postMsg" placeholder="Message" required="true"/>
                </div>
                <button id="postBtn" class="btn btn-primary" type="button">Send</button>

            </form>
        </div>
    </div>

    <hr/>

    <div class="row">
        <div class="col-md-6" style="border-right: 1px solid lightgray">
            <img id="asyncSpinner" src="${context}/resources/images/spinner-small.gif" class="pull-right" style="display:block"/>
            <h4>Long Async Poller</h4>

            <p class="text-success"><span id="asyncCount" data-count="0">0</span> request(s)</p>
            <%--<h5>Timeout: ${asyncTimeout / 1000} seconds</h5>--%>
            <table id="asyncTable" class="table table-condensed table-striped" style="display:none">
                <thead>
                <tr>
                    <td>Index</td>
                    <td>Message</td>
                    <td>Latency</td>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <div class="col-md-6">
            <img id="syncSpinner" src="${context}/resources/images/spinner-small.gif" class="pull-right" style="display:none"/>
            <h4>Repeating Sync Poller</h4>

            <p class="text-success"><span id="syncCount" data-count="0">0</span> request(s) @ ${pollTime / 1000} secs/poll</p>
            <table id="syncTable" class="table table-condensed table-striped" style="display:none">
                <thead>
                <tr>
                    <td>Index</td>
                    <td>Message</td>
                    <td>Latency</td>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
    <hr/>
    <%--<div class="row" style="margin-top: 2em;">--%>
    <%--<button id="deleteBtn" class="btn btn-default pull-right" type="button">Delete All Messages</button>--%>
    <%--</div>--%>
</div>

<script type="application/javascript">
    var SERVLET_CONTEXT = '${pageContext.request.contextPath}' + '/app';
    var APP = APP || {};
    APP.pollTime = ${pollTime};
    <%--APP.asyncTimeout = ${asyncTimeout} + 2000;--%>
    APP.syncIndex = 0;
    APP.asyncIndex = 0;
</script>

<script src="${context}/resources/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="${context}/resources/js/json2.js" type="text/javascript"></script>
<script src="${context}/resources/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${context}/resources/js/underscore-min.js" type="text/javascript"></script>
<script src="${context}/resources/js/bootbox.min.js" type="text/javascript"></script>
<script src="${context}/resources/js/jquery.bootstrap-growl.min.js" type="text/javascript"></script>
<script src="${context}/resources/js/app.js" type="text/javascript"></script>

</body>


</html>
