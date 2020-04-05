package twisk.monde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Monde implements Iterable<Etape>
{
    private ArrayList<Etape> lesEtapes;
    private HashMap<Integer, Etape> numToEtape;
    private ArrayList<Service> lesServices;
    private Sortie sortie;
    private Entree entree;
    private int cptEtapes;
    private int cptServices;

    /**
     * Constructeur
     */
    public Monde()
    {
        this.lesEtapes = new ArrayList<Etape>();
        this.numToEtape = new HashMap<>();
        this.lesServices = new ArrayList<>();
        this.entree=new Entree();
        this.sortie=new Sortie();
        this.cptEtapes = 2;
        lesEtapes.add(entree);
        lesEtapes.add(sortie);
    }

    /**
     * ajoute e comme entree du monde
     * @param e
     */
    public void ajouterEntree(Etape e){
        entree.ajouterSuccesseur(e);
    }

    /**
     * ajoute e comme la sortie du monde
     * @param e
     */
    public void ajouterSortie (Etape e){
        e.ajouterSuccesseur(sortie);
    }

    /**
     * ajoute les etapes au monde
     * @param etapes
     */
    public void ajouter(Etape... etapes)
    {
        for (Etape e : etapes ) {
            if (lesEtapes.contains(e)) continue;
            lesEtapes.add(lesEtapes.size()-1, e);
            cptEtapes++;
            if (e instanceof Service){
                cptServices++;
                lesServices.add((Service)e);
            }
        }
    }

    /**
     *
     * @return l'iterateur des etapes du monde
     */
    public Iterator<Etape> iterator()
    {
        ArrayList<Etape> etapes = new ArrayList<>();
        Etape e = entree;
        while (e != null) {
            etapes.add(e);
            e = e.getSuccesseur();
        };
        return etapes.iterator();
    }

    /***
     *
     * @return le nb d'etape dans le monde
     */
    public int nbEtapes() {
        return cptEtapes; // ALL ETAPE + ENTRE AND SORTIE
    }

    /**
     *
     * @return l'entree du monde
     */
    public Etape getEntree(){
        return entree;
    }

    /**
     *
     * @return la sortie du monde
     */
    public Etape getSortie(){
        return sortie;
    }

    /**
     *
     * @return le code C du monde
     */
    public String toC(){
        StringBuilder code = new StringBuilder("void simulation(int ids){\n");
        int delaiID = entree.getProbaType().getID();
        StringBuilder codeH = new StringBuilder
                ("#include \"def.h\"\n" +
                "#include <stdlib.h>\n" +
                "#include <math.h>\n" +
                "#include <unistd.h>\n\n"+
                "#define PI 3.14\n" +
                "#define PROBA_DELAI "+delaiFuncName[delaiID]+"\n");
        for (Etape e : lesEtapes){
            codeH.append("#define "+e.getDefineNom()+" "+e.getnEtape()+"\n");
            numToEtape.put(e.getnEtape(), e);
        }
        codeH.append("\n"+delaiCode[delaiID]+"\n");
        code.append(entree.toC());
        codeH.append("\n").append(code);
        return codeH.toString();
    }

    /***
     *
     * @return le nb de service du monde
     */
    public int getCptServices(){
        return cptServices;
    }

    /***
     *
     * @param i
     * @return retoune l'etape i
     */
    public Etape getEtapeFromNumber(int i){
        return numToEtape.get(i);
    }

    /***
     *
     * @return le numero de la sortie
     */
    public int getNumeroDeSortie(){return sortie.getnEtape();}

    /**
     *
     * @return les service du monde
     */
    public ArrayList<Service> getServices(){return lesServices;}

    static String[] delaiFuncName = {"delaiUniforme", "delaiGauss", "delaiExponentiel"};
    static String[] delaiCode = {
                    "float delaiUniforme(int temps, int delta) {\n" +
                            "    int bi, bs ;\n" +
                            "    int n, nbSec ;\n" +
                            "    bi = temps - delta ;\n" +
                            "    if (bi < 0) bi = 0 ;\n" +
                            "    bs = temps + delta ;\n" +
                            "    n = bs - bi ;\n" +
                            "    nbSec = (rand()/ (float)RAND_MAX) * n ;\n" +
                            "    nbSec += bi ;\n" +
                            "    return (float)nbSec;\n" +
                            "}",

                    "float delaiGauss(float moyenne, float ecartype)\n" +
                            "{\n" +
                            "\tfloat u1 = rand()/(float) RAND_MAX;\n" +
                            "\tfloat u2 = rand()/(float) RAND_MAX;\n" +
                            "\tfloat x = (sqrt(-2 * log(u1)) * cos(2 * PI * u2) * ecartype) + moyenne;\n" +
                            "\treturn x;\n" +
                            "}",

                    "float delaiExponentiel(double lambda){\n" +
                            "    float u;\n" +
                            "    double x;\n" +
                            "    u = rand() / (float) RAND_MAX;\n" +
                            "    x = -log(u) / lambda;\n" +
                            "\treturn x;\n" +
                            "}"
    };
}
