/*package twisk.monde.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twisk.monde.*;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EtapeTest {
    private Activite mg ;
    private Activite zoo;
    private Entree e;
    private Sortie s ;
    private Service toboggan;
    @BeforeEach
    void setUp() {
         mg = new Activite("manger");
         zoo = new Activite("zoo");
         e= new Entree();
         s = new Sortie();
         toboggan = new Service("toboggan",2);
    }

    @Test
    void ajouterSuccesseur() {
        e.ajouterSuccesseur(new Etape[] {mg,zoo,toboggan,s});
        assertEquals(4,e.nbEtapes());
    }

    @Test
    void iterator() {
        e.ajouterSuccesseur(new Etape[] {mg,zoo,toboggan,s});
        Iterator<Etape> it = e.iterator();
        assertEquals(it.next().getNom(),mg.getNom());
        assertEquals(it.next().getNom(),zoo.getNom());
        assertEquals(it.next().getNom(),toboggan.getNom());
        assertEquals(it.next().getNom(),s.getNom());

    }
}*/