package TwiskIG.vues;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import TwiskIG.mondeIG.ActiviteIG;
import TwiskIG.mondeIG.EtapeIG;
import TwiskIG.mondeIG.MondeIG;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import twisk.simulation.Client;

import java.util.Iterator;

public class GrandBain extends VueEtapeIG
{
    private Label label;
    private Pane box;
    private ImageView icon;

    public GrandBain(MondeIG monde, EtapeIG etape)
    {
        super(monde, etape);
        setPrefSize(etape.getLargeur()+SIZE_ADJUSTEMENT[0], etape.getHauteur()+SIZE_ADJUSTEMENT[1]);
        relocate(etape.getPosX(), etape.getPosY());
        box = new Pane();
        box.setStyle("-fx-border-color: #0059FF; -fx-background-insets: 0 0 -1 0, 0, 1, 2; -fx-background-radius: 3px, 3px, 2px, 1px;-fx-background-color: #cee4ea;");
        box.setPrefSize(etape.getLargeur()+SIZE_ADJUSTEMENT[0]/2, etape.getHauteur()+SIZE_ADJUSTEMENT[1]/4);
        box.relocate(SIZE_ADJUSTEMENT[0]/4, SIZE_ADJUSTEMENT[1]/2);
        etape.setVueEtape(this);
        titre.setText(etape.getNom()+" : "+(int)((ActiviteIG)etape).getEcartTemp()+" temps");
        getChildren().add(box);
        updateIcon();
        //getChildren().add(label);
    }

    public void updateIcon(){
        removeIcon();
        double SCALE = 0.45;
        double PAD_RATE_X = 0.1;
        double PAD_RATE_Y = 0.08;
        if (((ActiviteIG)getEtape()).getType() == ActiviteIG.TYPE.ENTREE){
            Image img = new Image(getClass().getResourceAsStream("/images/entree.png"));
            icon = new ImageView(img);
            icon.setScaleX(SCALE);
            icon.setScaleY(SCALE);
            double paddingX = img.getWidth()*icon.getScaleX()*PAD_RATE_X;
            double paddingY = (img.getHeight()*icon.getScaleX())*PAD_RATE_Y;
            icon.relocate((-img.getWidth()*icon.getScaleX())/2+paddingX, (-img.getHeight()*icon.getScaleX())/2+paddingY);
            getChildren().add(icon);
        }else if(((ActiviteIG)getEtape()).getType() == ActiviteIG.TYPE.SORTIE){
            Image img = new Image(getClass().getResourceAsStream("/images/sortie.png"));
            icon = new ImageView(img);
            icon.setScaleX(SCALE);
            icon.setScaleY(SCALE);
            double paddingX = img.getWidth()*icon.getScaleX()*PAD_RATE_X;
            double paddingY = (img.getHeight()*icon.getScaleX())*PAD_RATE_Y;
            icon.relocate((-img.getWidth()*icon.getScaleX())/2+paddingX, (-img.getHeight()*icon.getScaleX())/2+paddingY);
            getChildren().add(icon);
        }
    }

    private void removeIcon(){
        for (Iterator<Node> itr = getChildren().iterator(); itr.hasNext(); ) {
            Node n = itr.next();
            if (n instanceof ImageView && (ImageView)n == icon) {
                itr.remove();
            }
        }
        icon = null;
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
        titre.setText(etape.getNom()+" : "+(int)((ActiviteIG)etape).getEcartTemp()+" temps");
    }
}
