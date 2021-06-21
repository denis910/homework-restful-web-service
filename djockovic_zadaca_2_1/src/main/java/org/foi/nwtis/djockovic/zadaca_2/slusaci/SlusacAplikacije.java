/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.djockovic.zadaca_2.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.djockovic.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.djockovic.zadaca_2.dretve.PreuzimanjeLetovaAviona;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.djockovic.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 *
 * @author NWTiS_1
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {
    
    private PreuzimanjeLetovaAviona pla;
    
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        if(pla != null && pla.isAlive())
            pla.interrupt();
        
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("Postavke");
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce){
        ServletContext servletContext = sce.getServletContext();
        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF")+File.separator+servletContext.getInitParameter("konfiguracija");
        PostavkeBazaPodataka konfBP = new PostavkeBazaPodataka(putanjaKonfDatoteke);
        
        try{
            konfBP.ucitajKonfiguraciju();
            servletContext.setAttribute("Postavke", konfBP);
            pla = new PreuzimanjeLetovaAviona(konfBP);
            pla.start();
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
