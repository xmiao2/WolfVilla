<!DOCTYPE html>
<html>
    <head>
        <title>Hello World</title>
        <%@include file='/WEB-INF/views/head.jsp'%>
    </head>
    <body>
        <div id='sidebar'>
            <%@include file='/WEB-INF/views/sidebar.jsp'%>
        </div>
        <div id='content'>
            <p>Place content here</p>
            <center>
                <h2>
                    ${message}, ${name}
                </h2>
            </center>
        </div>
    </body>
</html>