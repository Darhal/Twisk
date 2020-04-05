package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape>
{
    protected String nom;
    protected ArrayList<Etape> succ;
    protected int nEtape;
    protected int cptEtape;

    /**
     * Constructeur
     * @param nom
     */
    public Etape(String nom)
    {
        this.nom = nom;
        this.cptEtape=0;
        FabriqueNumero fabriqueNumero= FabriqueNumero.getInstance();
        this.nEtape = fabriqueNumero.getNumeroEtape();
        this.succ = new ArrayList<>();
        this.succ.add(this);
    }

    /**
     * ajouter des successeurs
     * @param e
     */
    public void ajouterSuccesseur(Etape[] e) {
        for (Etape et : e) {
            succ.add(et);
            cptEtape++;
        }
    }

    /**
     * ajouter un successeur
     * @param e
     */
    public void ajouterSuccesseur(Etape e) {
        succ.add(e);
        cptEtape++;
    }

    /**
     * Iterateur d'etape
     * @return
     */
    @Override
    public Iterator<Etape> iterator() {
        return succ.iterator();
    }

    /**
     *
     * @return le nom de l'etape
     */
    public String getNom()
    {
        return nom;
    }

    /**
     *
     * @return le code C de l'etape
     */
    public String toC() {return "";}

    /**
     *
     * @return le nombre d'etapes qui succedent
     */
    public int getnEtape(){
        return nEtape;
    }
    public int nbEtapes(){
        return cptEtape;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return nom;
    }

    /**
     *
     * @return le premier successeur
     */
    public Etape getSuccesseur(){
        if (succ.isEmpty() || succ.size() == 1){
            return null;
        }
        return succ.get(1);
    }

    /**
     *
     * @return les tableu des sucesseurs
     */
    public ArrayList<Etape> getSuccesseurs()
    {
        return succ;
    }

    /***
     *THIS FUNCTION REMOVES ALL ACCEENTS AND REPLACE SPACES WITH UNDERSCORES!
     */
    public String getDefineNom() {
        String  dnom = nom;
        dnom  = dnom.replaceAll("\\s", "_")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[àâ]", "a")
                .replaceAll("[ç]", "c")
                .replaceAll("[îìï]", "i")
                .replaceAll("[ôò]", "o")
                .replaceAll("[ùûü]", "u")
                .replaceAll("[^A-Za-z0-9]", "_") // remove symbols!
                .toUpperCase();
        return dnom;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Etape){
            return nEtape == ((Etape)obj).nEtape;
        }
        return false;
    }
}
