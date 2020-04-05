package twisk.monde;

public class Activite extends Etape
{
    protected int temps;
    protected int ecartTemps;
    protected boolean delayCalculated;

    /**
     * Constructeur
     * @param nom
     */
    public Activite(String nom)
    {
        super(nom);
        this.temps = 0;
        this.ecartTemps = 0;
        this.delayCalculated=false;
    }

    /**
     * * Constructeur
     * @param nom
     * @param t
     * @param e
     */
    public Activite(String nom, int t, int e)
    {
        super(nom);
        this.temps=t;
        this.ecartTemps=e;
        this.delayCalculated=false;
    }

    /**
     *
     * @return la duree de l'activite
     */
    public int getTemps() {return this.temps; }

    /**
     *
     * @return l'ecart temps de l'activité
     */
    public int getEcartTemps(){return this.ecartTemps;}
    public void setDelayCalculated(boolean b){this.delayCalculated=b;}
    public boolean isDelayCalculated(){return this.delayCalculated;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Activite{ nom=").append(nom).append(", temps = ").append(temps).append(", ecartTemps = ").append(ecartTemps).append(", nActivité= ").append(nEtape).append(" }");
        return sb.toString();
    }

    /**
     *
     * @param i
     * @return une partie du code C
     */
    private String toCHelper(int i)
    {
        int id = i+1;
        StringBuilder s = new StringBuilder("");
        if (delayCalculated){
            s =  s.append("\ttransfert(").append(this.getDefineNom()).append("," + this.getSuccesseurs().get(id).getDefineNom()).append(");\n");
            delayCalculated = false;
        }else{
            s =  s.append("\tdelai(").append(temps).append(",").append(ecartTemps).append(");\n")
                    .append("\ttransfert(").append(
                    this.getDefineNom()).append("," + this.getSuccesseurs().get(id).getDefineNom()).append(");\n");
        }
        return s.toString();
    }

    /**
     *
     * @return le code C à generer
     */
    @Override
    public String toC(){
        if (this.getSuccesseurs().size() == 1){return "";}
        StringBuilder s = new StringBuilder("");
        if (cptEtape == 1) {
            s.append(toCHelper(0));
            Etape next = this.getSuccesseur();
            if (next != null){
                s.append(next.toC());
            }
        }else{
            // Handle bifurcations!
            String code = "\n\tint alea = (int)(rand() % "+(getSuccesseurs().size() - 1)+");\n";
            int count = 0;
            for (Etape etp : getSuccesseurs()){
                if (etp.equals(this)) continue; // TODO: Maybe remove this later.
                String nextC = etp.toC().replaceAll("\t", "\t\t");
                String currentC = toCHelper(count).replaceAll("\t", "\t\t");
                if (count == 0) {
                    code += "\tif (alea == " + count + "){\n";
                    code += currentC+""+nextC+"\t}";
                }else{
                    code += "else if (alea == " + count + "){\n";
                    code += currentC+""+nextC+"\n\t}\n";
                }
                count++;
            }
            s.append(code);
        }
        return s.toString();
    }
}
