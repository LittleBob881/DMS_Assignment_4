<%-- 
    Document   : index
    Created on : 21/05/2021, 9:30:46 AM
    Author     : Elizabeth and Bernadette
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>KPop Service Test Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="<%= response.encodeURL(request.getContextPath())%>/kpopService/userprofile/login" method="POST">
            <p>
                User Name:
                <input type="text" name="username"/>
            </p>
            <input type="submit" value="Login"/>
        </form>
         <a href="/KPopProfileService/test-resbeans.html">
         Use the NetBeans RESTful web service tester
         </a>
    </body>
</html>
