package twisk.monde;

public class Entree extends Activite
{
    /**
     * la probabilité utilisée pour le transfert des clients
     */
    public enum PROBA_TYPE {
        UNIFORM(0), GAUSSIAN(1), EXPONENTIAL(2);
        private  int val;
        PROBA_TYPE(int i) { val = i; }
        int getID() { return val; }
    };


    private PROBA_TYPE probaType;
    private float delaiParams[];

    /**
     * Condstructeur
     */
    public Entree()
    {
        super("Entree");
        delaiParams = new float[2];
        delaiParams[0] = 6.f;
        delaiParams[1] = 3.f;
        probaType = PROBA_TYPE.UNIFORM;
    }

    /**
     *
     * @return le type de proba
     */
    public PROBA_TYPE getProbaType() {
        return probaType;
    }

    /**
     *
     * @param type set le type de la proba
     */
    public void setProbaType(PROBA_TYPE type){
        probaType = type;
    }

    /**
     *
     * @return le code C sdu delai
     */
    private String delaiCodeC()
    {
        switch(probaType)
        {
            case UNIFORM:
                return "PROBA_DELAI("+delaiParams[0]+","+delaiParams[1]+")";
            case GAUSSIAN:
                return "PROBA_DELAI("+delaiParams[0]+","+delaiParams[1]+")";
            case EXPONENTIAL:
                return "PROBA_DELAI("+delaiParams[0]+")";
            default:
                return "PROBA_DELAI("+delaiParams[0]+","+delaiParams[1]+")";
        }
    }

    /**
     *
     * @param params set les delai
     */
    public void setDelaiParams(float... params)
    {
        int len = params.length < 2 ? 1 : 2;
        for (int i = 0; i < len; i++){
            delaiParams[i] = params[i];
        }
    }

    /**
     *
     * @return le code C de l'entree
     */
    @Override
    public String toC(){
        if (this.getSuccesseur() == null){return "";}
        StringBuilder s = new StringBuilder(
                 "\tentrer("+this.getDefineNom()+");\n"
        ).append("\tusleep(").append(delaiCodeC()).append("*1000000);\n"
        ).append("\ttransfert("+ this.getDefineNom()+","+ this.getSuccesseur().getDefineNom()+");\n");
        s.append(this.getSuccesseur().toC());
        s.append("}");
        return s.toString();
    }
}
