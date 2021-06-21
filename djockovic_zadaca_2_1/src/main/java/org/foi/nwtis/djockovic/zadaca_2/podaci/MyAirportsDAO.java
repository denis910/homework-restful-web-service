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
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa za rad s tablicom myairports
 * @author Denis Jocković
 */
public class MyAirportsDAO {

    /**
     * Dohvaćanje svih aerodroma koje netko prati
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @return Lista svih aerodroma koje netko prati
     */
    public List<Aerodrom> dohvatiSveAerodrome(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit1 = "SELECT ident FROM myairports";
        AirportsDAO adao = new AirportsDAO();
        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit1)) {

                while (rs.next()) {
                    boolean sadrziAerodrom = false;
                    for(Aerodrom a : aerodromi){
                        if(a.getIcao().equals(rs.getString("ident"))){
                            sadrziAerodrom = true;
                            break;
                        }
                    }
                    if(!sadrziAerodrom)
                        aerodromi.add(adao.dohvatiAirport(rs.getString("ident"), pbp));
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Dohvaćanje svih korisnika koji prate određeni aerodrom
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param icao jedinstvena oznaka aerodroma za kojeg se gleda koji ga korisnici prate
     * @return Lista korisnika koji prate određeni aerodrom
     */
    public List<Korisnik> dohvatiKorisnikaAerodroma(PostavkeBazaPodataka pbp, String icao) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT username FROM myairports WHERE ident = ?";
        KorisnikDAO kdao = new KorisnikDAO();
        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Korisnik> korisnici = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {
                
                s.setString(1, icao);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    boolean sadrziKorisnika = false;
                    for(Korisnik k : korisnici){
                        if(k.getKorisnik().equals(rs.getString("username"))){
                            sadrziKorisnika = true;
                            break;
                        }
                    }
                    if(!sadrziKorisnika)
                        korisnici.add(kdao.dohvatiKorisnika(rs.getString("username"), "", Boolean.FALSE, pbp));
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Lista svih aerodroma koje prati neki korisnik
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param korisnik Korisničko ime korisnika za kojeg se dobavljaju aerodromi koje prati
     * @return Lista Aerodroma koje prati korisnik
     */
    public List<Aerodrom> dohvatiAerodromeKorisnika(PostavkeBazaPodataka pbp, String korisnik) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ident FROM myairports WHERE username = ?";
        AirportsDAO adao = new AirportsDAO();
        
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {
                
                s.setString(1, korisnik);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    boolean sadrziAerodrom = false;
                    for(Aerodrom a : aerodromi){
                        if(a.getIcao().equals(rs.getString("ident"))){
                            sadrziAerodrom = true;
                            break;
                        }
                    }
                    if(!sadrziAerodrom)
                        aerodromi.add(adao.dohvatiAirport(rs.getString("ident"), pbp));
                }
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dodavanje aerodroma za praćenje nekom korisniku
     * @param a Aerodrom koji se dodaje korisniku
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param korisnik korisničko ime korisnika kojem se dodaje aerodrom
     * @return True ako je unos bio uspješan, inače false
     */
    public boolean dodajAerodrom(Aerodrom a, PostavkeBazaPodataka pbp, String korisnik) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO myairports (ident, username, stored) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, a.getIcao());
                s.setString(2, korisnik);

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

    /**
     * Brisanje određenog aerodroma kao aerodroma kojeg određeni korisnik prati
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param korisnik korisničko ime korisnika koji više ne želi pratit aerodrom
     * @param icao jedinstvena oznaka aerodroma kojeg se ne želi pratiti
     * @return True ako je brisanje uspješno
     */
    public boolean brisiAerodrom(PostavkeBazaPodataka pbp, String korisnik, String icao) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "DELETE FROM myairports WHERE ident = ? AND username = ? ";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                     Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                     PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, korisnik);

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
