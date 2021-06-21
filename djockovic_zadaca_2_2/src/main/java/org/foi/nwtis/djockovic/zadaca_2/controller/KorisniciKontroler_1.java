package org.foi.nwtis.djockovic.zadaca_2.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_3;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_5;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;

/**
 * Klasa za preusmjeravanje na pravile viewe
 * @author Denis Jocković
 */
@Path("korisnik")
@Controller
public class KorisniciKontroler_1 {
    
    @Inject
    private Models model;

    @Path("prijavaKorisnika")
    @GET
    @View("prijavaKorisnika.jsp")
    public void prijavaKorisnika() {
        return;
    }

    @Path("korisnikPodaci")
    @GET
    @View("korisnikPodaci.jsp")
    public void registracijaKorisnika() {
        return;
    }

    /**
     * Dohvaćanje podataka potrebnih za ispravan rad web stranice
     * @return ime viewa na kojeg se treba preusmjeriti
     */
    @Path("operacijeAerodroma")
    @GET
    public String operacijeAerodroma() {
        if (KorisniciKontroler_2.password != null) {

            KorisniciKlijent_3 kk = new KorisniciKlijent_3(KorisniciKontroler_2.korime);
            KorisniciKlijent_5 kk_2 = new KorisniciKlijent_5();
            List<Object> mojiAerodromi;
            List<Object> sviAerodromi;
            try {
                mojiAerodromi = kk.dajAerodrome(List.class, KorisniciKontroler_2.korime, KorisniciKontroler_2.password);
                sviAerodromi = kk_2.dajAerodrome(List.class, null, null, KorisniciKontroler_2.korime, KorisniciKontroler_2.password);
            } catch (Exception ex) {
                model.put("greska", "Ne moguće dohvatiti aerodrome!");
                return "greska.jsp";
            }
            model.put("mojiAerodromi", mojiAerodromi);
            model.put("sviAerodromi", sviAerodromi);
            System.out.println();

            return "operacijeAerodroma.jsp";
        }
        return "prijavaKorisnika.jsp";
    }
    
    /**
     * Dohvaćanje podataka potrebnih za ispravan rad web stranice
     * @return ime viewa na kojeg se treba preusmjeriti
     */
    @Path("popisAerodroma")
    @GET
    public String pregledavanjeAerodroma() {
        if (KorisniciKontroler_2.password != null) {

            KorisniciKlijent_3 kk = new KorisniciKlijent_3(KorisniciKontroler_2.korime);
            List<Object> mojiAerodromi;
            try {
                mojiAerodromi = kk.dajAerodrome(List.class, KorisniciKontroler_2.korime, KorisniciKontroler_2.password);
            } catch (Exception ex) {
                model.put("greska", "Ne moguće dohvatiti aerodrome!");
                return "greska.jsp";
            }
            model.put("mojiAerodromi", mojiAerodromi);
            System.out.println();

            return "popisAerodroma.jsp";
        }
        return "prijavaKorisnika.jsp";
    }
}
