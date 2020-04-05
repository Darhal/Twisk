package TwiskIG.vues;

import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import TwiskIG.mondeIG.EtapeIG;
import TwiskIG.mondeIG.MondeIG;
import twisk.simulation.Client;

import java.util.ArrayList;

public abstract class VueEtapeIG extends Pane {
    public static final double[] SIZE_ADJUSTEMENT = {50.0, 50.0};

    protected Label titre;
    protected EtapeIG etape;
    protected MondeIG monde;
    private ArrayList<VuePointDeControleIG> pointDeControles;
    private boolean isSelected;
    // value at index 0 : Normal color, Value at index 1 : Color when selected.
    protected String border_color[] = {"1095e8", "ff5050"};
    protected String background_color[] = {"e9f5fb","e9f5fb"};
    protected static double CLIENT_RADUIS = 3.5;

    public VueEtapeIG(MondeIG monde, EtapeIG etape)
    {
        this.monde=monde;
        this.etape=etape;
        this.pointDeControles = new ArrayList<>();
        titre=new Label(etape.getNom());
        setStyle("-fx-border-color: #"+border_color[0]+"; -fx-background-color: #"+background_color[0]+"; -fx-border-width: 1.2; -fx-background-insets: 0 0 -1 0, 0, 1, 2; -fx-background-radius: 12;-fx-border-radius:12;");
        titre.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #1a3da5;");
        titre.relocate(0, (SIZE_ADJUSTEMENT[1]/6)-titre.getHeight());
        titre.layoutXProperty().bind(this.widthProperty().subtract(titre.widthProperty()).divide(2));
        getChildren().add(titre);
        this.setOnMouseClicked(event -> monde.selectionnerUneEtape(event, this));
        addDragHandlers();
        addDragHandlers();
    }

    public MondeIG getMonde() {
        return monde;
    }

    public EtapeIG getEtape() {
        return etape;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected){
            setStyle("-fx-border-color: #"+border_color[1]+"; -fx-background-color: #"+background_color[1]+"; -fx-border-width: 2; -fx-background-insets: 0 0 -1 0, 0, 1, 2; -fx-background-radius: 12;-fx-border-radius:12;");
        }else{
            setStyle("-fx-border-color: #"+border_color[0]+"; -fx-background-color: #"+background_color[0]+"; -fx-border-width: 1.2; -fx-background-insets: 0 0 -1 0, 0, 1, 2; -fx-background-radius: 12;-fx-border-radius:12;");
        }
    }

    public void setTitre(String str){
        titre.setText(str);
    }

    private void addDragHandlers(){
        VueEtapeIG source = this;
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(etape.getIdentifiant()+","+(event.getX()-source.getTranslateX())+","+(event.getY()-source.getTranslateY()));
                content.putImage(source.snapshot(new SnapshotParameters(),null));
                db.setContent(content);
                event.consume();
            }
        });
        source.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                }
                event.consume();
            }
        });
    }

    public void ajouterLesPoints(VueDessin vd){
        for (int i=0; i<etape.getNumPoints(); i++) {
            VuePointDeControleIG vcp = new VuePointDeControleIG(etape.getPointDeControle()[i], monde);
            vd.getChildren().add(vcp);
            pointDeControles.add(vcp);
            vcp.setArc(vd.getPointToArc().get(etape.getPointDeControle()[i]));
        }
    }

    public abstract void ajouterClient(Client client);

    public void setPos(double x, double y){
        this.relocate(x, y);
        etape.setPosX(x);
        etape.setPosY(y);
        etape.CalculatePointsPos();
        for (VuePointDeControleIG vpc : pointDeControles){
            vpc.updatePos();
            if (vpc.getArc() != null){
                vpc.getArc().updateArc();
            }
        }
    }

    public void setBorderColors(String... args)
    {
        int i = 0;
        for(String c : args){
            if (i >= border_color.length) break;
            border_color[i] = c;
            i++;
        }
        setSelected(isSelected); //Just to update colors.
    }

    public void setBackgroundColors(String... args)
    {
        int i = 0;
        for(String c : args){
            if (i >= background_color.length) break;
            background_color[i] = c;
            i++;
        }
        setSelected(isSelected); //Just to update colors.
    }
}
