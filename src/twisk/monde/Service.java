package twisk.monde;

import twisk.outils.FabriqueNumero;

public class Service extends Etape
{
    private int nbJetons;
    private int nServiceSemaphore;

    /**
     * Constructeur
     * @param nom
     */
    public Service(String nom)
    {
        super(nom);
        this.nServiceSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
        this.nbJetons = 0;
    }

    /**
     * Constructeur
     * @param nom
     * @param nbJetons
     */
    public Service(String nom, int nbJetons)
    {
        super(nom);
        this.nbJetons=nbJetons;
        this.nServiceSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
    }

    /**
     *
     * @return le nb de jeton du service
     */
    public int getNbJetons()
    {
        return nbJetons;
    }

    /**
     *
     * @return
     */
    public int getnServiceSemaphore() {
        return nServiceSemaphore;
    }

    @Override
    public String toString() {
        return "Service{ nom="+nom+", nbJetons="+nbJetons+" nService = "+nEtape+", nServiceSemaphore="+nServiceSemaphore+" }";
    }

    /***
     *
     * @return le code C du sevice
     */
    @Override
    public String toC(){
        if (this.getSuccesseur() == null){return "";}
        StringBuilder s = new StringBuilder();
        if (!(this.getSuccesseur() instanceof Activite)){
            s.append(
                    "\tP(ids,"+this.nServiceSemaphore+");\n"
            ).append(   "\t\ttransfert("
            ).append(   this.getDefineNom()).append(",").append(this.getSuccesseur().getDefineNom()).append(");\n"
            ).append(   "\t\tV(ids,"+this.nServiceSemaphore+");\n"
            );
        }else {
            Activite actSuivante = (Activite) this.getSuccesseur();
            actSuivante.setDelayCalculated(true);
            s.append(
                    "\tP(ids," + this.nServiceSemaphore + ");\n"
            ).append("\t\ttransfert("
            ).append(this.getDefineNom()).append(",").append(this.getSuccesseur().getDefineNom()).append(");\n"
            ).append("\t\tdelai(").append(actSuivante.getTemps()).append(",").append(actSuivante.getEcartTemps()).append(");\n"
            ).append("\tV(ids," + this.nServiceSemaphore + ");\n"
            );
        }
        Etape next = this.getSuccesseur();
        if (next != null){
            s.append(next.toC());
        }
        return s.toString();
    }
}
