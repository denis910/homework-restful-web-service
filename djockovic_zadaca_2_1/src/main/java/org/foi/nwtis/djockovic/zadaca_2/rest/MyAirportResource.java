package org.foi.nwtis.djockovic.zadaca_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.djockovic.zadaca_2.podaci.AirportsDAO;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisnikDAO;
import org.foi.nwtis.djockovic.zadaca_2.podaci.MyAirportsDAO;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sadrži metode koje omogućuju komunikaciju s bazom i dohvaćanje podataka o praćenim aerodromima
 * @author Denis Jocković
 */
@Path("mojiAerodromi")
public class MyAirportResource {

    @Inject
    ServletContext context;

    /**
     * Dohvaća se lista aerodroma koje netko prati
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @return Odgovor na get upit, lista aerodroma koje prati bilo koji korisnik ako je upit uspješan
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik, 
            @HeaderParam("lozinka") String lozinka) {
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, Boolean.TRUE, pbp);
        
        if (k == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportsDAO mdao = new MyAirportsDAO();
        
        List<Aerodrom> aerodromi = mdao.dohvatiSveAerodrome(pbp);
        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }
    
    /**
     * 
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param icao jednoznačna identifikacija aerodroma
     * @return Odgovor na get upit, aerodrom ako je upit uspješan
     */
    @Path("{icao}/prate")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnike(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, Boolean.TRUE, pbp);

        if (k == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MyAirportsDAO mdao = new MyAirportsDAO();
        List<Korisnik> trazeniKorisnici = mdao.dohvatiKorisnikaAerodroma(pbp, icao);

        if (trazeniKorisnici == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Trazeni korisnici nisu pronađen.")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(trazeniKorisnici)
                .build();
    }
    
    /**
     * Lista aerodroma koje prati određeni korisnik
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param pKorisnik Korisnik za kojeg se pronalaze aerodromi koje prati 
     * @return Odgovor na get upit, lista aerodroma ako je upit uspješan
     */
    @Path("{korisnik}/prati")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pKorisnik) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, Boolean.TRUE, pbp);

        if (k == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MyAirportsDAO mdao = new MyAirportsDAO();
        List<Aerodrom> trazeniAerodromi = mdao.dohvatiAerodromeKorisnika(pbp, pKorisnik);

        if (trazeniAerodromi == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Trazeni aerodromi nisu pronađen.")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(trazeniAerodromi)
                .build();
    }
    
    /**
     * Zahtjev za dodavanje novog aerodroma kao praćenog
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param noviAirport Aerodrom koji se unosi kao praćeni
     * @param pKorisnik Korisnik za kojeg se unosi aerodrom 
     * @return Odgovor na post upit, aerodrom ako je upit uspješan
     */
    @Path("{korisnik}/prati")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, Aerodrom noviAirport, @PathParam("korisnik") String pKorisnik) {
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, Boolean.TRUE, pbp);

        if (k == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        
        MyAirportsDAO mdao = new MyAirportsDAO();
        Boolean dodanAirport = mdao.dodajAerodrom(noviAirport, pbp, pKorisnik);

        if (!dodanAirport) {
            return Response
                    .status(Response.Status.NOT_MODIFIED)
                    .entity("Korisnik nije uspješno dodan.")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(noviAirport)
                .build();
    }
    
    /**
     * Brisanje aerodroma kao praćenog za određenog korisnika
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param pKorisnik Korisnik za kojeg se briše aerodrom 
     * @param icao jednoznačna oznaka aerodroma koji se briše
     * @return Odgovor na delete upit, string koji šalje odgovor
     */
    @Path("{korisnik}/prati/{icao}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public Response brisiAerodrome(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pKorisnik, @PathParam("icao") String icao) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, Boolean.TRUE, pbp);

        if (k == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MyAirportsDAO mdao = new MyAirportsDAO();
        boolean izvrsenaAkcija = mdao.brisiAerodrom(pbp, pKorisnik, icao);

        if (!izvrsenaAkcija) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Trazeni aerodromi nisu pronađen.")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(k)
                .build();
    }
}
