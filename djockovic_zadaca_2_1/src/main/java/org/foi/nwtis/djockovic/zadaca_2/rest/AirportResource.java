package org.foi.nwtis.djockovic.zadaca_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.djockovic.zadaca_2.podaci.AirportsDAO;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisnikDAO;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sadrži metode koje omogućuju komunikaciju s bazom i dohvaćanje podataka o aerodromima
 * @author Denis Jocković
 */
@Path("aerodromi")
public class AirportResource {

    @Inject
    ServletContext context;

    /**
     * Vraćanje svih aerodroma nakon što se netko izvrši određeni get upit
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param naziv Naziv aerodroma ili dio naziva aerodroma koji se žele vidjeti
     * @param drzava Država u kojoj se aerodrom nalazi
     * @return Odgovor na get upit, lista aerodroma ako su uspješno dohvaćeni
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik, 
            @HeaderParam("lozinka") String lozinka, @QueryParam("naziv") String naziv,
            @QueryParam("drzava") String drzava) {
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, Boolean.TRUE, pbp);
        
        if (k == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }
        AirportsDAO adao = new AirportsDAO();
        List<Aerodrom> aerodromi = adao.dohvatiSveAerodrome(pbp,naziv,drzava);
        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }
    
    /**
     * Dohvaćanje određenog aerodroma
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param icao Jednoznačna oznaka aerodroma kojeg se želi vidjeti
     * @return Odgovor na get upit, aerodrom ako je uspješno dohvaćen
     */
    @Path("{icao}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrom(@HeaderParam("korisnik") String korisnik,
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

        AirportsDAO adao = new AirportsDAO();
        Aerodrom trazeniAerodrom = adao.dohvatiAirport(icao, pbp);

        if (trazeniAerodrom == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Trazeni aerodrom nije pronađen.")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(trazeniAerodrom)
                .build();
    }
    
    /**
     * Dohvaćanje broja polazaka s određenog aerodroma
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param icao Jednoznačna oznaka aerodroma kojeg se želi vidjeti
     * @return Odgovor na get upit, broj letova s aerodroma ako je uspješno dohvaćen
     */
    @Path("{icao}/letovi")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajLetove(@HeaderParam("korisnik") String korisnik,
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

        AirportsDAO adao = new AirportsDAO();
        int brojLetova = adao.dajBrojPolazaka(icao, pbp);

        if (brojLetova == -1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Trazeni aerodrom nije pronađen.")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(brojLetova)
                .build();
    }
}
