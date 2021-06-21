package org.foi.nwtis.djockovic.zadaca_2.controller;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_3;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * Klasa koja služi za slanje podataka o novom aerodromu za korisnika
 * @author Denis Jocković
 */
@Path("dodavanjeAerodroma")
@Controller
@RequestScoped
public class KorisniciKontroler_5 {
    
    @Inject
    private Models model;
    
    @FormParam("icao")
    String icao;
    
    @FormParam("drzava")
    String drzava;
    
    @FormParam("naziv")
    String naziv;
    
    @FormParam("latitude")
    String latitude;
    
    @FormParam("longitude")
    String longitude;
    
    @Inject
    @JMSConnectionFactory("jms/NWTiS_QF_djockovic_1")
    private JMSContext context;
    
    @Resource(lookup = "jms/NWTiS_djockovic_1")
    Queue requestQueue;
    
    /**
     * Upravljanje podacima aerodroma koji se želi unijeti kao praćeni
     * @return view na kojeg se želi proslijediti
     */
    @POST
    public String dodavanjeAerodroma() {
        KorisniciKlijent_3 kk = new KorisniciKlijent_3(KorisniciKontroler_2.korime);
        
        Lokacija l = new Lokacija();
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        
        Aerodrom a = new Aerodrom();
        a.setDrzava(drzava);
        a.setIcao(icao);
        a.setNaziv(naziv);
        a.setLokacija(l);
        
        try{
            kk.dodajKorisnika(a,Aerodrom.class,KorisniciKontroler_2.korime, KorisniciKontroler_2.password);
        } catch(Exception ex){
            model.put("greska", "Nije moguće dodati aerodrom!");
            return "greska.jsp";
        }
        saljiPoruku("Korisnik: " + KorisniciKontroler_2.korime + "Aerodrom: " + icao + "Akcija: dodavanje");
        System.out.println();
        return "index.jsp";
    }
    
    public void saljiPoruku (String textPoruke) {
        TextMessage poruka = context.createTextMessage(textPoruke);
        context.createProducer().send(requestQueue, poruka);
    }
}
