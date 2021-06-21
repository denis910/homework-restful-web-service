/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.djockovic.zadaca_2.dretve;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.djockovic.zadaca_2.podaci.AirportsDAO;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisnikDAO;
import org.foi.nwtis.djockovic.zadaca_2.podaci.MyAirportsDAO;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * PreuzimanjeLetovaAviona služi za preuzimanje svih letova sa svih aerodroma koje neki od korisnika
 * prati PreuzimanjeLetovaAviona služi i za postavljanje vrijednosti iz konfiguracijskih datoteka
 *
 * @author Denis Jocković
 */
public class PreuzimanjeLetovaAviona extends Thread {

    private PostavkeBazaPodataka pbp;
    private OSKlijent osk;
    private int trajanjeCiklusa;
    private int trajanjePauze;
    private String pocetniDatum;
    private boolean kraj;

    public PreuzimanjeLetovaAviona(PostavkeBazaPodataka pbp) {
        this.pbp = pbp;
    }

    @Override
    public void interrupt() {
        this.kraj = true;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * U run metodi se na početku uzima početni datum iz konfiguracijske datoteke i pretvara u
     * Timestamp Nakon toga se izračunava sljedeći dan i današnji dan Tada se dobavljaju svi
     * aerodromi koje neki korisnik prati te se ulazi u petlju gdje se preuzimaju letovi s tih
     * aerodroma
     */
    @Override
    public void run() {
        System.out.println("Krećemo s preuzimanjem podataka");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date parsedDate;
        Timestamp odVrijeme = null;
        Date today = new Date();
        Timestamp danas = null;
        try {
            parsedDate = dateFormat.parse(pocetniDatum);
            odVrijeme = new java.sql.Timestamp(parsedDate.getTime());
            danas = new java.sql.Timestamp(System.currentTimeMillis());

        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAviona.class.getName()).log(Level.SEVERE, null, ex);
        }

        Timestamp doVrijeme = dodajDan(odVrijeme);
        MyAirportsDAO mdao = new MyAirportsDAO();
        List<Aerodrom> aerodromi = mdao.dohvatiSveAerodrome(pbp);
        List<AvionLeti> airplanes;
        int brojUnosa = 0;
        while (!kraj && danas.after(doVrijeme)) {
            for (Aerodrom a : aerodromi) {
                System.out.println(dohvatiAerodrom(pbp, a.getIcao(), odVrijeme.toString()));
                if (dohvatiAerodrom(pbp, a.getIcao(), odVrijeme.toString()) == null) {
                    System.out.println(a.getIcao() + " || " + odVrijeme);
                    airplanes = osk.getDepartures(a.getIcao(), odVrijeme, doVrijeme);
                    for (AvionLeti al : airplanes) {
                        if (!provjeriAirplane(pbp, al.getIcao24())) {
                            if (!unesiAirplanes(pbp, al)) {
                                break;
                            } else {
                                brojUnosa++;
                            }
                        } else {
                            brojUnosa++;
                        }
                    }
                    if (airplanes.size() == brojUnosa) {
                        unesiLog(pbp, a.getIcao(), odVrijeme.toString());
                    }
                    try {
                        sleep(this.trajanjePauze);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PreuzimanjeLetovaAviona.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            odVrijeme = doVrijeme;
            doVrijeme = dodajDan(odVrijeme);
            System.out.println("Novi dan!");
            try {
                sleep(this.trajanjeCiklusa);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAviona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Dretva zaustavljena");
    }

    /**
     * Računanje završnog Timestampa za uzimanje letova
     *
     * @param odVrijeme Početni Timestamp za uzimanje letova
     * @return Završni Timestamp za uzimanje letova
     */
    private Timestamp dodajDan(Timestamp odVrijeme) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(odVrijeme);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return new Timestamp(cal.getTime().getTime());
    }

    /**
     * Dobavljanje zapisa iz tablice myairportslog kako bi se odredilo jesu li letovi s određenog
     * aerodroma za određeni datum već upisani u tablicu
     *
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param icao Jednoznačna oznaka aerodroma
     * @param vrijeme Dan za kojeg se gleda uzimanje letova
     * @return
     */
    public String dohvatiAerodrom(PostavkeBazaPodataka pbp, String icao, String vrijeme) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT id FROM myairportslog WHERE ident = ? AND flightdate = ?";

        try (
                Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                PreparedStatement s = con.prepareStatement(upit)) {

            s.setString(1, icao);
            s.setString(2, vrijeme);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Provjeravanje je li određen let već upisan u tablicu
     *
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param icao24 Jednoznačna oznaka aerodroma
     * @return
     */
    private boolean provjeriAirplane(PostavkeBazaPodataka pbp, String icao24) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT id FROM airplanes WHERE icao24 = ?";

        try (
                Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                PreparedStatement s = con.prepareStatement(upit)) {

            s.setString(1, icao24);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Unošenje letova aerodroma
     *
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param a Let koji se unosi u bazu
     * @return True ako je uspješno inače false
     */
    private boolean unesiAirplanes(PostavkeBazaPodataka pbp, AvionLeti a) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO airplanes (icao24, firstseen, estdepartureairport, lastseen, estarrivalairport, callsign, estdepartureairporthorizdistance, estdepartureairportvertdistance, estarrivalairporthorizdistance, estarrivalairportvertdistance, departureairportcandidatescount, arrivalairportcandidatescount, stored) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (
                Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                PreparedStatement s = con.prepareStatement(upit)) {

            s.setString(1, a.getIcao24());
            s.setString(2, Integer.toString(a.getFirstSeen()));
            s.setString(3, a.getEstDepartureAirport());
            s.setString(4, Integer.toString(a.getLastSeen()));
            if (a.getEstArrivalAirport() != null) {
                s.setString(5, a.getEstArrivalAirport());
            } else {
                s.setString(5, "N/A");
            }
            s.setString(6, a.getCallsign());
            s.setString(7, Integer.toString(a.getEstDepartureAirportHorizDistance()));
            s.setString(8, Integer.toString(a.getEstDepartureAirportVertDistance()));
            s.setString(9, Integer.toString(a.getEstArrivalAirportHorizDistance()));
            s.setString(10, Integer.toString(a.getEstArrivalAirportVertDistance()));
            s.setString(11, Integer.toString(a.getDepartureAirportCandidatesCount()));
            s.setString(12, Integer.toString(a.getArrivalAirportCandidatesCount()));

            int brojAzuriranja = s.executeUpdate();

            return brojAzuriranja == 1;

        } catch (SQLException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Unošenje podataka u log nakon unosa svih letova određenog dana
     *
     * @param pbp Čitanje podataka potrebnih za rad s bazom
     * @param ident jednoznačna oznaka aerodroma
     * @param datum dan za kojeg su se unosili podaci
     */
    private void unesiLog(PostavkeBazaPodataka pbp, String ident, String datum) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO myairportslog (ident, flightdate, stored) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (
                Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                PreparedStatement s = con.prepareStatement(upit)) {

            s.setString(1, ident);
            s.setString(2, datum);

            s.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ,
     * Preuzimanje postavki iz konfiguracijske datoteke i početak rada dretve
     */
    @Override
    public synchronized void start() {
        boolean status = Boolean.parseBoolean(pbp.dajPostavku("preuzimanje.status"));
        if (!status) {
            System.out.println("Ne preuzimamo nove podatke");
            return;
        }
        this.trajanjeCiklusa = Integer.parseInt(pbp.dajPostavku("preuzimanje.ciklus"));
        this.pocetniDatum = pbp.dajPostavku("preuzimanje.pocetak");
        this.trajanjePauze = Integer.parseInt(pbp.dajPostavku("preuzimanje.pauza"));
        String korime = pbp.dajPostavku("OpenSkyNetwork.korisnik");
        String lozinka = pbp.dajPostavku("OpenSkyNetwork.lozinka");
        osk = new OSKlijent(korime, lozinka);
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
