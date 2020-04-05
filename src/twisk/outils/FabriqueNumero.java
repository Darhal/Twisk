package twisk.outils;

public class FabriqueNumero
{
    private static FabriqueNumero fb = new FabriqueNumero();
    private int cptEtape = 0;
    private int cptSemaphore = 1;
    private int cptMonde = 0;
    private FabriqueNumero(){ }

    /***
     * Constructeur
     * @return
     */
    public static FabriqueNumero getInstance()
    {
        return fb;
    }

    /**
     *
     * @return le numero de l'etape
     */
    public int getNumeroEtape()
    {
        return cptEtape++;
    }

    /**
     *
     * @return le numeroSemaphore de l'etape
     */
    public int getNumeroSemaphore()
    {
        return cptSemaphore++;
    }

    /**
     *
     * @return
     */
    public int getNumeroMonde()
    {
        return cptMonde;
    }

    public int incrNumeroMonde()
    {
        return ++cptMonde;
    }

    /**
     * reset
     */
    public void reset()
    {
        this.cptEtape = 0;
        this.cptSemaphore=1;
        this.cptMonde=0;
    }
}
