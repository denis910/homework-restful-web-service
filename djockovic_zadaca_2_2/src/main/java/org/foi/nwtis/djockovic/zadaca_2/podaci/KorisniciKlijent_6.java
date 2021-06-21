/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.djockovic.zadaca_2.podaci;

/**
 * Jersey REST client generated for REST resource:mojiAerodromi [mojiAerodromi/{icao}/prate]<br>
 * USAGE:
 * <pre>
 *        KorisniciKlijent_6 client = new KorisniciKlijent_6();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author NWTiS_1
 */
public class KorisniciKlijent_6 {

    private jakarta.ws.rs.client.WebTarget webTarget;
    private jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8084/djockovic_zadaca_2_1/rest/";

    public KorisniciKlijent_6(String icao) {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("mojiAerodromi/{0}/prate", new Object[]{icao});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String icao) {
        String resourcePath = java.text.MessageFormat.format("mojiAerodromi/{0}/prate", new Object[]{icao});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnik header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T dajKorisnike(Class<T> responseType, String korisnik, String lozinka) throws jakarta.ws.rs.ClientErrorException {
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
