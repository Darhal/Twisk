package TwiskIG.vues;

import TwiskIG.exceptions.MondeException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import TwiskIG.mondeIG.MondeIG;

import java.util.Observable;
import java.util.Observer;

public class VueOutils extends HBox implements Observer
{
    private MondeIG monde;
    private Button btnAjtAct;
    private Button btnAjtSer;
    private Button btnSimuler;

    public VueOutils(MondeIG fab)
    {
        this.monde = fab;
        this.setSpacing(15.f);
        this.btnAjtAct = new Button("Ajouter une activité");
        this.btnAjtAct.setOnAction(event -> monde.ajouter("Activité"));
        this.btnAjtAct.setTooltip(new Tooltip("Clicker pour ajouter une activité"));
        getChildren().add(this.btnAjtAct);

        this.btnAjtSer = new Button("Ajouter un service");
        this.btnAjtSer.setOnAction(event -> monde.ajouter("Service"));
        this.btnAjtSer.setTooltip(new Tooltip("Clicker pour ajouter un service"));
        getChildren().add(this.btnAjtSer);

        this.btnSimuler = new Button("Simuler");
        this.btnSimuler.setOnAction(event -> {
            try {
                monde.simuler();
            } catch (MondeException e) {
                VueExceptionAlert ve = new VueExceptionAlert(e);
            }
        });
        this.btnSimuler.setTooltip(new Tooltip("Clicker pour commencer la simulation"));
        getChildren().add(this.btnSimuler);

        Label infoLabel = new Label("Pret");
        getChildren().add(infoLabel);

        monde.setPrivateLabel(infoLabel);

        this.monde.addObserver(this);
        setStyle("-fx-background-color: #e1e5ed");
    }

    public void update(Observable o, Object a)
    {
        if (monde.IsSimulationActive()){
            this.btnSimuler.setText("Stop");
        }else{
            this.btnSimuler.setText("Simuler");
        }
        this.btnAjtAct.setDisable(monde.IsSimulationActive());
        this.btnAjtSer.setDisable(monde.IsSimulationActive());
    }
}
