package twisk.simulation;

import javafx.scene.paint.Color;
import twisk.monde.Etape;

public class Client
{
    private int numeroClient;
    private int rang;
    private Etape etape;
    private Color color;

    /**
     * Constucteur
     * @param numero
     */
    public Client(int numero){
        this.numeroClient = numero;
        color = Color.rgb((int)(Math.random() * 255 + 1), (int)(Math.random() * 255 + 1), (int)(Math.random() * 255 + 1));
    }

    /***
     * l'etape vers la quelle le client va aller
     * @param etape
     * @param rang
     */
    public void allerA(Etape etape, int rang)
    {
        this.etape = etape;
        this.rang = rang;
    }

    /**
     *
     * @return le numero du client
     */
    public int getNumeroClient()
    {
        return numeroClient;
    }

    /**
     *
     * @return le rang du client
     */
    public int getRang()
    {
        return rang;
    }

    /**
     * l'etape ou se trouve le client
     * @return
     */
    public Etape getEtape()
    {
        return etape;
    }

    /**
     * couleur du client
     * @return
     */
    public Color getColor() {
        return color;
    }
}
