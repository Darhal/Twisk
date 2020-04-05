package TwiskIG.vues;

import TwiskIG.mondeIG.EtapeIG;
import TwiskIG.mondeIG.MondeIG;
import TwiskIG.mondeIG.ServiceIG;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import twisk.simulation.Client;

public class VueService extends VueEtapeIG
{
    private Label label;
    private Pane box;

    public VueService(MondeIG monde, EtapeIG etape)
    {
        super(monde, etape);
        setBorderColors("67ce75", "55aa61");
        setBackgroundColors("e9f5fb", "e9f5fb");
        setPrefSize(etape.getLargeur()+SIZE_ADJUSTEMENT[0], etape.getHauteur()+SIZE_ADJUSTEMENT[1]);
        relocate(etape.getPosX(), etape.getPosY());
        box = new Pane();
        box.setStyle("-fx-border-color: #6CCE33; -fx-background-insets: 0 0 -1 0, 0, 1, 2; -fx-background-radius: 3px, 3px, 2px, 1px;-fx-background-color: #cee4ea;");
        box.setPrefSize(etape.getLargeur()+SIZE_ADJUSTEMENT[0]/2, etape.getHauteur()+SIZE_ADJUSTEMENT[1]/4);
        box.relocate(SIZE_ADJUSTEMENT[0]/4, SIZE_ADJUSTEMENT[1]/2);
        etape.setVueEtape(this);
        titre.setText(etape.getNom()+" : "+((ServiceIG)etape).getNbJetons()+" Jetons");
        getChildren().add(box);
    }

    @Override
    public void ajouterClient(Client client)
    {
        double DIV = 2.5;
        Circle vueClient = new Circle();
        vueClient.setRadius(CLIENT_RADUIS);
        vueClient.setFill(client.getColor());
        box.getChildren().add(vueClient);
        vueClient.setCenterX(Math.random()/DIV * box.getPrefWidth());
        vueClient.setCenterY(Math.random()/DIV * box.getPrefHeight());
        vueClient.relocate(vueClient.getCenterX(), vueClient.getCenterY());
        vueClient.setTranslateX(vueClient.getCenterX());
        vueClient.setTranslateY(vueClient.getCenterY());
    }

    @Override
    public void setTitre(String str)
    {
        titre.setText(etape.getNom()+" : "+((ServiceIG)etape).getNbJetons()+" Jetons");
    }
}
