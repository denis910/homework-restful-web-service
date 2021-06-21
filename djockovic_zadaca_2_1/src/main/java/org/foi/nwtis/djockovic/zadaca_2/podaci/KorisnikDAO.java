/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.djockovic.zadaca_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja služi za dohvaćanje podataka iz tablice o korisnicima
 * @author Denis Jocković
 */
public class KorisnikDAO {

    /**
     * Dohvaćanje određenog korisnika
     * @param korisnik korisničko ime korisnika čiji se podaci žele
     * @param lozinka lozinka korisnika čiji se podaci žele
     * @param prijava izvodi li se funkcija u svrhu prijave ili ne
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @return Dohvaćeni korisnik
     */
    public Korisnik dohvatiKorisnika(String korisnik, String lozinka, Boolean prijava, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ime, prezime, korisnik, lozinka, emailAdresa, vrijemeKreiranja, vrijemePromjene "
                + "FROM korisnici WHERE korisnik = ?";

        if (prijava) {
            upit += " and lozinka = ?";
        }

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
                if (prijava) {
                    s.setString(2, lozinka);
                }
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("emailAdresa");
                    Timestamp kreiran = rs.getTimestamp("vrijemeKreiranja");
                    Timestamp promjena = rs.getTimestamp("vrijemePromjene");

                    Korisnik k = new Korisnik(korisnik1, "******", prezime, ime, email, kreiran, promjena, 0);
                    return k;
                }

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dohvaćanje svih korisnika iz baze podataka
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @return Svi korisnici iz baze
     */
    public List<Korisnik> dohvatiSveKorisnike(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ime, prezime, emailAdresa, korisnik, lozinka, vrijemeKreiranja, vrijemePromjene FROM korisnici";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Korisnik> korisnici = new ArrayList<>();

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     Statement s = con.createStatement();
                     ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    String email = rs.getString("emailAdresa");
                    Timestamp kreiran = rs.getTimestamp("vrijemeKreiranja");
                    Timestamp promjena = rs.getTimestamp("vrijemePromjene");
                    Korisnik k = new Korisnik(korisnik1, "******", prezime, ime, email, kreiran, promjena, 0);

                    korisnici.add(k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Unošenje korisnika u bazu
     * @param k Korisnik koji se želi unijeti u bazu
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @return True ako je korisnik unešen u bazu, inače false
     */
    public boolean dodajKorisnika(Korisnik k, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO korisnici (ime, prezime, emailAdresa, korisnik, lozinka, vrijemeKreiranja, vrijemePromjene) "
                + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, k.getIme());
                s.setString(2, k.getPrezime());
                s.setString(3, k.getEmailAdresa());
                s.setString(4, k.getKorisnik());
                s.setString(5, k.getLozinka());

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
