<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Upravljanje aerodromima</title>
    </head>
    <body>
        <h1>Upravljanje aerodromima</h1>
        <h2>Aerodromi koje korisnik prati</h2>
        <table border>
            <th>
                <td>ICAO</td>
                <td>Naziv</td>
                <td>Zemlja</td>
                <td>Geografska širina</td>
                <td>Geografska duljina</td>
                <td>Brisanje</td>
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
                    <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/brisanjeAerodroma">
                        <input type="hidden" name="ident" value="${aerodrom.icao}"/>
                        <input type="submit" name="Izbriši aerodrom" value="Izbriši aerodrom"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <h2>Aerodromi koje korisnik ne prati</h2>
        <table border>
            <th>
                <td>ICAO</td>
                <td>Naziv</td>
                <td>Zemlja</td>
                <td>Geografska širina</td>
                <td>Geografska duljina</td>
                <td>Dodavanje</td>
            </th>
        <c:forEach items="${sviAerodromi}" var="aerodrom">
            <c:forEach items="${mojiAerodromi}" var="mojAerodrom">
                <c:if test="${aerodrom.icao==mojAerodrom.icao}">
                    <c:set var="prolazak" value="true"/>
                </c:if>
            </c:forEach>
            <c:if test="${prolazak=='false'}">
                <tr>
                    <td/>
                    <td>${aerodrom.icao}</td>
                    <td>${aerodrom.naziv}</td>
                    <td>${aerodrom.drzava}</td>
                    <td>${aerodrom.lokacija.latitude}</td>
                    <td>${aerodrom.lokacija.longitude}</td>
                    <td>
                        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/dodavanjeAerodroma">
                            <input type="hidden" name="icao" value="${aerodrom.icao}"/>
                            <input type="hidden" name="latitude" value="${aerodrom.lokacija.latitude}"/>
                            <input type="hidden" name="longitude" value="${aerodrom.lokacija.longitude}"/>
                            <input type="submit" name="Dodaj aerodrom" value="Dodaj Aerodrom"/>
                        </form>
                    </td>
                </tr>
            </c:if>
            <c:set var="prolazak" value="false"/>
        </c:forEach>
    </table>
</body>
</html>