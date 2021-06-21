package org.foi.nwtis.djockovic.zadaca_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;
import org.foi.nwtis.djockovic.zadaca_2.podaci.KorisnikDAO;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Korisnik;

/**
 * Klasa koja sadrži metode koje omogućuju komunikaciju s bazom i dohvaćanje podataka o korisnicima
 * @author Denis Jocković
 */
@Path("korisnici")
public class KorisniciResource {

    @Inject
    ServletContext context;

    /**
     * Dohvaćanje svih korisnika
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @return Odgovor na get upit, lista korisnika ako su uspješno dohvaćeni
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnike(@HeaderParam("korisnik") String korisnik,
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

        List<Korisnik> korisnici = kdao.dohvatiSveKorisnike(pbp);
        return Response
                .status(Response.Status.OK)
                .entity(korisnici)
                .build();
    }

    /**
     * Dodavanje novog korisnika
     * @param noviKorisnik korisnik koji se želi dodati u bazu
     * @return Odgovor na post upit, korisnik ako je uspješno dodan
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajKorisnika(Korisnik noviKorisnik) {
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikDAO kdao = new KorisnikDAO();
        
        Boolean dodanKorisnik = kdao.dodajKorisnika(noviKorisnik, pbp);

        if (!dodanKorisnik) {
            return Response
                    .status(Response.Status.NOT_MODIFIED)
                    .entity("Korisnik nije uspješno dodan.")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(noviKorisnik)
                .build();
    }

    /**
     * 
     * @param korisnik Korisničko ime korisnika koji želi vidjeti aerodrome
     * @param lozinka Lozinka korisnika
     * @param pKorisnik Korisnik kojeg se želi dohvatiti
     * @return Odgovor na get upit, korisnik ako je uspješno dohvaćen
     */
    @Path("{korisnik}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnika(@HeaderParam("korisnik") String korisnik,
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

        Korisnik trazeniKorisnik = kdao.dohvatiKorisnika(pKorisnik, "", Boolean.FALSE, pbp);

        if (trazeniKorisnik == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Trazeni korisnik nije pronađen.")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(trazeniKorisnik)
                .build();
    }

}
