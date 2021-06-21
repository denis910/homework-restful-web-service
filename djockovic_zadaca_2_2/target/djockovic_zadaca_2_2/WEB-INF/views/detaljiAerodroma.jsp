<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Detalji Aerodroma</title>
    </head>
    <body>
        <h1>Detalji aerodroma ${odabraniAerodrom}</h1>
        <h2>Korisnici aerodroma ${odabraniAerodrom}</h2>
        <table border>
            <th>
                <td>Korisničko ime</td>
                <td>Prezime</td>
                <td>Ime</td>
                <td>Email adresa</td>
            </th>
            <c:forEach items="${detalji}" var="korisnik">
                <tr>
                    <td/>
                    <td>${korisnik.korisnik}</td>
                    <td>${korisnik.prezime}</td><
                    <td>${korisnik.ime}</td>
                    <td>${korisnik.emailAdresa}</td>
                </tr>
            </c:forEach>
        </table>
        <h2>Vrijeme aerodroma ${odabraniAerodrom}</h2>
        <table border>
            <th>
                <td>Temperatura zraka</td>
                <td>Mjerna jedinica temperature</td>
                <td>Tlak zraka</td>
                <td>Mjerna jedinica tlaka</td>
                <td>Vlaga zraka</td>
                <td>Mjerna jedinica vlage</td>
            </th>
                <tr>
                    <td/>
                    <td>${temperaturaVrijednost}</td>
                    <td>${temperaturaUnit}</td>
                    <td>${tlakVrijednost}</td>
                    <td>${tlakUnit}</td>
                    <td>${vlagaVrijednost}</td>
                    <td>${vlagaUnit}</td>
                </tr>
        </table>
</body>
</html>