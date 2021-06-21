package org.foi.nwtis.djockovic.zadaca_2.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.List;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_1;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_2;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_3;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_4;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa u kojoj se nalazi metoda za brisanje aerodroma s liste praćenih
 * @author Denis Jocković
 */
@Path("brisanjeAerodroma")
@Controller
@RequestScoped
public class KorisniciKontroler_4 {
    
    @Inject
    private Models model;
    
    @FormParam("ident")
    String ident;
    
    /**
     * Podaci o aerodromu kojeg korisnik želi izbrisati s liste praćenih
     * @return view na kojeg se treba preusmjeriti
     */
    @POST
    public String pregledavanjeAerodroma() {
        KorisniciKlijent_4 kk = new KorisniciKlijent_4(KorisniciKontroler_2.korime, ident);
        try{
            kk.brisiAerodrome(String.class, KorisniciKontroler_2.korime, KorisniciKontroler_2.password);
        } catch(Exception ex){
            model.put("greska", "Nije moguće izbrisati aerodrom!");
            return "greska.jsp";
        }
        KorisniciKontroler_5 kk_5 = new KorisniciKontroler_5();
        kk_5.saljiPoruku("Korisnik: " + KorisniciKontroler_2.korime + "Aerodrom: " + ident + "Akcija: brisanje");
        System.out.println();
        return "index.jsp";
    }
}
