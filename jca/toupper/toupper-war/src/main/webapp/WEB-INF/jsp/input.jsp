<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<! DOCTYPE HTML>
<html>
    <head>
        <title>To-Upper Web Client</title>
    </head>
    <body>
        <h1>Input Form</h1>
        <form method="POST">
            Text: <input type="text" name="input" /><br />
            Timeout: <input type="text" name="timeout" /><br />
            <input type="submit" value="Odeslat" />
        </form>

        <c:if test="${not empty result}">
            <br />
            <h2>Result: ${result}</h2>
        </c:if>
    </body>
</html>

