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
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa u kojoj se nalazi metoda za registraciju
 * @author Denis Jocković
 */
@Path("korisnikRegistracija")
@Controller
@RequestScoped
public class KorisniciKontroler_3 {
    
    @FormParam("korisnik")
    String korisnik;
    
    @FormParam("lozinka")
    String lozinka;
    
    @FormParam("ime")
    String ime;
    
    @FormParam("prezime")
    String prezime;
    
    @FormParam("email")
    String email;
    
    @Inject
    private Models model;
    
    /**
     * Podaci koje je upisao korisnik šalju se na validaciju za registraciju
     * @return view na kojeg se treba preusmjeriti
     */
    @POST
    public String registracijaKorisnika() {
        Korisnik k = new Korisnik(email, ime, korisnik, lozinka, prezime, 0);
        KorisniciKlijent_1 kk = new KorisniciKlijent_1();
        Object rez;
        System.out.println(korisnik + " " + lozinka);
        try{
            rez = kk.dodajKorisnika(k,Korisnik.class);
        } catch(Exception ex){
            model.put("greska", "Greška prilikom registracije!");
            return "greska.jsp";
        }
        System.out.println();
        return "prijavaKorisnika.jsp";
    }
}
