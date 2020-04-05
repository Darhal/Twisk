/*package twisk.monde.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twisk.monde.*;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MondeTest {
    private Monde monde;
    private Etape entree;
    private Etape sortie;
    private Etape manger, promenade, filedattente, cafe ;
    @BeforeEach
    void setUp() {
        monde = new Monde();
        entree= new Entree();
        sortie=new Sortie();
        manger=new Activite("manger");
        promenade=new Activite("promenade");
        filedattente=new Service("file d'attente ",2);
        cafe=new Activite("cafe");
    }

    @Test
    void ajouter() {
        monde.ajouter(entree);
        monde.ajouterSortie(sortie);
        monde.ajouter(manger,promenade,filedattente);
        monde.ajouter(cafe);
        assertEquals(8,monde.nbEtapes());
    }

    @Test
    void iterator() {
        monde.ajouterEntree(entree);
        monde.ajouter(manger,promenade,filedattente);
        monde.ajouter(cafe);
        monde.ajouterSortie(sortie);
        assertEquals(8,monde.nbEtapes());
        Iterator<Etape> it = monde.iterator();
        it.next();
        Etape etape1=it.next();
        assertEquals(etape1,entree);
        Etape etape2=it.next();
        assertEquals(etape2,manger);
        Etape etape3= it.next();
        assertEquals(etape3,promenade);
        Etape etape4=it.next();
        assertEquals(etape4,filedattente);
        Etape etape5=it.next();
        assertEquals(etape5,cafe);
        Etape etape6=it.next();
        assertEquals(etape6,sortie);
    }

    @Test
    void toC() {

        entree.ajouterSuccesseur(manger);
        manger.ajouterSuccesseur(promenade);
        promenade.ajouterSuccesseur(filedattente);
        filedattente.ajouterSuccesseur(cafe);
        cafe.ajouterSuccesseur(sortie);
        monde.ajouterEntree(entree);
        monde.ajouter(manger,promenade,filedattente);
        monde.ajouter(cafe);
        monde.ajouterSortie(sortie);
        System.out.println(monde.toC());
    }
}*/