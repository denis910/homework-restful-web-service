package org.foi.nwtis.djockovic.zadaca_2.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.djockovic.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_1;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_2;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_3;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_4;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_6;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 * Klasa u kojoj se dobivaju detalji o aerodromu
 * @author Denis Jocković
 */
@Path("detaljiAerodroma")
@Controller
@RequestScoped
public class KorisniciKontroler_6 {
    
    @Inject
    private Models model;
    
    @FormParam("ident")
    String icao;
    
    @FormParam("longitude")
    String longitude;
    
    @FormParam("latitude")
    String latitude;
    
    @Context
    ServletContext servletContext;
    
    /**
     * Dohvaćanje korisnika koji prate aerodrom
     * @return view na kojeg se želi proslijediti
     */
    @POST
    public String dodavanjeAerodroma() {
        KorisniciKlijent_6 kk = new KorisniciKlijent_6(icao);
        List<Object> rez;
        try{
            rez = kk.dajKorisnike(List.class, KorisniciKontroler_2.korime, KorisniciKontroler_2.password);
            model.put("detalji", rez);
            model.put("odabraniAerodrom", icao);
        } catch(Exception ex){
            model.put("greska", "Nije moguće dobiti detalje aerodroma!");
            return "greska.jsp";
        }
        vrijemeAerodroma();
        
        System.out.println();
        return "detaljiAerodroma.jsp";
    }

    /**
     * Funkcija koja dohvaća podatke o vremenu za željeni aerodrom i zapisuje ih u Model
     */
    private void vrijemeAerodroma() {
        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF")+File.separator+servletContext.getInitParameter("konfiguracija");
        PostavkeBazaPodataka konfBP = new PostavkeBazaPodataka(putanjaKonfDatoteke);
        try {
            konfBP.ucitajKonfiguraciju();
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(KorisniciKontroler_6.class.getName()).log(Level.SEVERE, null, ex);
        }
        String kljuc = konfBP.dajPostavku("OpenWeatherMap.apikey");
        OWMKlijent owmk = new OWMKlijent(kljuc);
        MeteoPodaci podaci = owmk.getRealTimeWeather(latitude, longitude);
        model.put("temperaturaVrijednost", podaci.getTemperatureValue());
        model.put("temperaturaUnit", podaci.getTemperatureUnit());
        model.put("tlakVrijednost", podaci.getPressureValue());
        model.put("tlakUnit", podaci.getPressureUnit());
        model.put("vlagaVrijednost", podaci.getHumidityValue());
        model.put("vlagaUnit", podaci.getHumidityUnit());
    }
}
