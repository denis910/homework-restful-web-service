package org.foi.nwtis.djockovic.zadaca_2.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.List;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_1;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisniciKlijent_2;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa u kojoj se nalazi metoda za prijavu
 * @author Denis Jocković
 */
@Path("prijavaRegistracija")
@Controller
@RequestScoped
public class KorisniciKontroler_2 {
    
    @FormParam("korisnik")
    String korisnik;
    
    @FormParam("lozinka")
    String lozinka;
    
    @Inject
    private Models model;
    
    public static String password;
    public static String korime;
    
    /**
     * Podaci koje je upisao korisnik šalju se na validaciju za prijavu
     * @return view na kojeg se treba preusmjeriti
     */
    @POST
    public String prijavaKorisnika() {
        System.out.println(korisnik + " " + lozinka);
        KorisniciKlijent_2 kk = new KorisniciKlijent_2(korisnik);
        Object rez;
        try{
            rez = kk.dajKorisnika(String.class, korisnik, lozinka);
        } catch(Exception ex){
            model.put("greska", "Upišite valjano korisničko ime i lozinku!");
            return "greska.jsp";
        }
        System.out.println(rez);
        korime = korisnik;
        password = lozinka;
        return "index.jsp";
    }
}
