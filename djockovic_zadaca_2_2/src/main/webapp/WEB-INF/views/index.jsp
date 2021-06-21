<%-- 
    Document   : index
    Created on : Apr 28, 2021, 2:50:57 PM
    Author     : NWTiS_1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Denis Jocković - Zadaća 2</title>
    </head>
    <body>
        <h1>Denis Jocković - Zadaća 2</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/korisnikPodaci">Registracija korisnika</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/prijavaKorisnika">Prijava korisnika</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/operacijeAerodroma">Pregled aerodroma</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/popisAerodroma">Pregled korisnika</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/">Pregled aviona</a>
            </li>
        </ul>
    </body>
</html>
