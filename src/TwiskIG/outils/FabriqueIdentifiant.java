package TwiskIG.outils;

public class FabriqueIdentifiant
{
    private static FabriqueIdentifiant fii = new FabriqueIdentifiant();
    private int noEtape=0;
    public static FabriqueIdentifiant getInstance(){return fii;}
    public String getIdentifiantEtape() { noEtape++; return "E"+noEtape; }

    public void reset() {
        noEtape = 0;
    }
}
