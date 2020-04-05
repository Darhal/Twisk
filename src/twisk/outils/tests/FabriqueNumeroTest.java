/*package twisk.IG.outils.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twisk.monde.Activite;
import twisk.monde.Etape;
import twisk.monde.Monde;
import twisk.monde.Service;
import twisk.IG.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class FabriqueNumeroTest {
    private Monde monde;
    private Etape balade,accestoboggan,toboggan;
    private FabriqueNumero fabriqueNumero;
    @BeforeEach
    void setUp() {
        monde=new Monde();
        balade=new Activite("balade au zoo");
        accestoboggan=new Service("acces au toboggan",2);

    }
    @Test
    void getNumeroEtape() {
        assertEquals(2,balade.getnEtape()); // vu qu'on a initialisé un monde (l'etape entree et l'etape sortie de ce monde ont eu respectivement le numéro 0 et 1 ! )
        assertEquals(3,accestoboggan.getnEtape());


    }

    //@Test
    void getNumeroSemaphore() {
        assertEquals(3,balade.getnSemaphore());
        assertEquals(4,accestoboggan.getnSemaphore());
    }
}*/