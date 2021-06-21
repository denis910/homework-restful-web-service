<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Popis aerodroma</title>
    </head>
    <body>
        <h1>Popis aerodroma</h1>
        <table border>
            <th>
                <td>ICAO</td>
                <td>Naziv</td>
                <td>Zemlja</td>
                <td>Geografska širina</td>
                <td>Geografska duljina</td>
                <td>Detalji</td>
            </th>
        <c:forEach items="${mojiAerodromi}" var="aerodrom">
            <tr>
                <td/>
                <td>${aerodrom.icao}</td>
                <td>${aerodrom.naziv}</td>
                <td>${aerodrom.drzava}</td>
                <td>${aerodrom.lokacija.latitude}</td>
                <td>${aerodrom.lokacija.longitude}</td>
                <td>
                    <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/detaljiAerodroma">
                        <input type="hidden" name="ident" value="${aerodrom.icao}"/>
                        <input type="hidden" name="longitude" value="${aerodrom.lokacija.longitude}"/>
                        <input type="hidden" name="latitude" value="${aerodrom.lokacija.latitude}"/>
                        <input type="submit" name="Detalji aerodroma" value="Detalji aerodroma"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>