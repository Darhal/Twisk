package TwiskIG.mondeIG;

import TwiskIG.vues.VueEtapeIG;

public class ServiceIG extends EtapeIG
{
    private int nbJetons;
    public static int NUM_POINTS = 2;


    public ServiceIG(MondeIG m, String nom, String idf, int larg, int haut) {
        super(m, nom, idf, larg, haut);
        setLargeur(200);
        setHauteur(35/2);
        nbJetons = 2;
        numPoints = NUM_POINTS;
        ajouterLesPoints();
    }

    public ServiceIG(MondeIG m, String nom, String idf) {
        super(m, nom, idf);
        setLargeur(200);
        setHauteur(35/2);
        nbJetons = 2;
        numPoints = NUM_POINTS;
        ajouterLesPoints();
    }

    public void setNbJetons(int n){
        nbJetons = n;
    }

    public int getNbJetons(){
        return nbJetons;
    }

    public VueEtapeIG getVueService(){
        return (VueEtapeIG)getVueEtape();
    }

    @Override
    public boolean estUnService(){
        return true;
    }


    public String serilizeObject()
    {
        String str = "";
        str += "{\n";
        str += "\tTYPE: "+"SERVICE"+";\n";
        str += "\tNB_JETONS: "+this.getNbJetons()+";\n";
        str += this.serilizeHelper();
        str += "}\n";
        return str;
    }
}
