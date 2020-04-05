package TwiskIG.mondeIG;

import TwiskIG.vues.GrandBain;

public class ActiviteIG extends EtapeIG
{
    public enum TYPE {ENTREE, SORTIE, ACTIVITE};
    private TYPE type;
    private double delai;
    private double ecartTemp;
    public static int NUM_POINTS = 4;

    public ActiviteIG(MondeIG m, String nom, String idf, int larg, int haut) {
        super(m, nom, idf, larg, haut);
        delai = 2.0;
        ecartTemp = 10.0;
        numPoints = NUM_POINTS;
        type = TYPE.ACTIVITE;
        ajouterLesPoints();
    }

    /**
     * Constructeur
     * @param m
     * @param nom
     * @param idf
     */
    public ActiviteIG(MondeIG m, String nom, String idf) {
        super(m, nom, idf);
        delai = 10.0;
        ecartTemp = 2.0;
        numPoints = NUM_POINTS;
        type = TYPE.ACTIVITE;
        ajouterLesPoints();
    }

    /**
     * set le delai
     * @param d
     */
    public void setDelai(double d){
        delai = d;
    }

    /**
     * set l'ecart
     * @param et
     */
    public void setEcartTemp(double et){
        ecartTemp = et;
    }

    public double getDelai() {
        return delai;
    }

    public double getEcartTemp() {
        return ecartTemp;
    }

    public void setType(TYPE t){
        type=t;
    }

    public TYPE getType(){return type;}

    public GrandBain getVueActivite(){
        return (GrandBain)getVueEtape();
    }

    @Override
    public boolean estUneActivite(){
        return true;
    }

    /**
     *
     * @return true si c'est une activite Restrainte
     */
    @Override
    public boolean estUneActiviteRestrainte(){
        return type == TYPE.SORTIE;
    }

    /**
     *
     * @return la serilization de l'activite
     */
    public String serilizeObject()
    {
        String str = "";
        str += "{\n";
        str += "\tTYPE: "+this.getType()+";\n";
        str += "\tDELAI: "+this.getDelai()+";\n";
        str += "\tECART_TEMP: "+this.getEcartTemp()+";\n";
        str += this.serilizeHelper();
        str += "}\n";
        return str;
    }
}
