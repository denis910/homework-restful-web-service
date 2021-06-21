<%-- 
    Document   : prijavaKorisnika
    Created on : Apr 28, 2021, 2:53:53 PM
    Author     : NWTiS_1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registracija korisnika</title>
    </head>
    <body>
        <h1>Registracija korisnika</h1>
        <pre>${greska}</pre>
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnikRegistracija">
            <table>
                <tr>
                    <td>Ime</td>
                    <td><input type="text" name="ime"/></td>
                </tr>
                <tr>
                    <td>Prezime</td>
                    <td><input type="text" name="prezime"/></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email"/></td>
                </tr>
                <tr>
                    <td>Korisničko ime</td>
                    <td><input type="text" name="korisnik"/></td>
                </tr>
                <tr>
                    <td>Lozinka</td>
                    <td><input type="password" name="lozinka"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="Prijavi me"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
