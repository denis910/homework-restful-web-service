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
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * Klasa koja služi za dohvaćanje podataka iz tablice o aerodromima 
 * @author Denis Jocković
 */
public class AirportsDAO {

    /**
     * Dohvaćanje određenog aerodroma
     * @param icao jednoznačna oznaka aerodroma za dohvaćanje
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @return Aerodrom koji je dohvaćen iz tablice
     */
    public Aerodrom dohvatiAirport(String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates  FROM airports "
                + "WHERE ident = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    //String type = rs.getString("type");
                    String name = rs.getString("name");/*
                    String elevation_ft = rs.getString("elevation_ft");
                    String continent = rs.getString("continent");*/
                    String iso_country = rs.getString("iso_country");/*
                    String iso_region = rs.getString("iso_region");
                    String municipality = rs.getString("municipality");
                    String gps_code = rs.getString("gps_code");
                    String iata_code = rs.getString("iata_code");
                    String local_code = rs.getString("local_code");*/
                    String coordinates = rs.getString("coordinates");
                    
                    Aerodrom a = new Aerodrom();
                    a.setDrzava(iso_country);
                    a.setIcao(ident);
                    a.setNaziv(name);
                    
                    Lokacija l = new Lokacija();
                    String[] koordinate = coordinates.split(",");
                    koordinate[1] = koordinate[1].replaceAll("\\s+","");
                    l.setLatitude(koordinate[1]);
                    l.setLongitude(koordinate[0]);
                    a.setLokacija(l);
                    
                    return a;
                }

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dohvaćanje svih aerodroma iz baze koji su iz navedene drzave i imaju slican naziv
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param naziv Naziv aerodroma
     * @param drzava Naziv države u kojoj se aerodrom nalazi
     * @return Svi aerodromi iz tablice
     */
    public List<Aerodrom> dohvatiSveAerodrome(PostavkeBazaPodataka pbp, String naziv, String drzava) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ident, type, name, elevation_ft, continent, iso_country, iso_region, municipality, gps_code, iata_code, local_code, coordinates  FROM airports";

        if (naziv != null && drzava == null) {
            upit += " WHERE name LIKE ?";
        }
        if (naziv == null && drzava != null) {
            upit += " WHERE iso_country = ?";
        }
        if (naziv != null && drzava != null) {
            upit += " WHERE name LIKE ? AND iso_country = ?";
        }
        
        

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                if (naziv != null && drzava == null) {
                    s.setString(1, "%" + naziv + "%");
                }
                if (naziv == null && drzava != null) {
                    s.setString(1, drzava);
                }
                if (naziv != null && drzava != null) {
                    s.setString(1, "%" + naziv + "%");
                    s.setString(2, drzava);
                }
                
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String ident = rs.getString("ident");
                    //String type = rs.getString("type");
                    String name = rs.getString("name");/*
                    String elevation_ft = rs.getString("elevation_ft");
                    String continent = rs.getString("continent");*/
                    String iso_country = rs.getString("iso_country");/*
                    String iso_region = rs.getString("iso_region");
                    String municipality = rs.getString("municipality");
                    String gps_code = rs.getString("gps_code");
                    String iata_code = rs.getString("iata_code");
                    String local_code = rs.getString("local_code");*/
                    String coordinates = rs.getString("coordinates");

                    Aerodrom a = new Aerodrom();
                    a.setDrzava(iso_country);
                    a.setIcao(ident);
                    a.setNaziv(name);
                    
                    Lokacija l = new Lokacija();
                    String[] koordinate = coordinates.split(",");
                    koordinate[1] = koordinate[1].replaceAll("\\s+","");
                    l.setLatitude(koordinate[1]);
                    l.setLongitude(koordinate[0]);
                    a.setLokacija(l);

                    aerodromi.add(a);
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
     * Broj letova koji su poletili s određenog aerodroma
     * @param icao jednoznačna oznaka aerodroma
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @return 
     */
    public int dajBrojPolazaka(String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT count(id) as broj FROM AIRPLANES "
                + "WHERE estDepartureAirport = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String broj = rs.getString("broj");
                    return Integer.parseInt(broj);
                }

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}
