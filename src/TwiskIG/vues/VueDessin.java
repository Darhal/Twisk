package TwiskIG.vues;

import TwiskIG.exceptions.MondeException;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import TwiskIG.mondeIG.*;
import TwiskIG.outils.Vec2;
import twisk.simulation.Client;

import java.util.*;

public class VueDessin extends Pane implements Observer
{
    private MondeIG monde;
    private PointDeControleIG pointDeControleSelectionner;
    private Vec2 v1, v2;
    private ArrayList<VueEtapeIG> vueEtapes;
    private HashMap<PointDeControleIG, VueArcIG> pointToArc;

    public VueDessin(MondeIG fab)
    {
        this.vueEtapes = new ArrayList<>();
        this.pointToArc = new HashMap<>();
        this.monde=fab;
        v1=null;
        v2=null;
        this.monde.addObserver(this);
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.onMouseClick(event));
        setStyle("-fx-background-color: #e1e5ed");

        VueDessin target = this;
        target.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VueEtapeIG && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                /*Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    for(VueEtapeIG ve : vueEtapes){
                        if (ve.getEtape().getIdentifiant() == db.getString()){
                            ve.setPos(event.getX(), event.getY());
                            break;
                        }
                    }
                }*/
            }
            event.consume();
        });

        target.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                boolean success = false;
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    for(VueEtapeIG ve : vueEtapes){
                        String[] param = db.getString().split(",");
                        if (ve.getEtape().getIdentifiant().equals(param[0])){
                            ve.setPos(event.getX()-Double.parseDouble(param[1]), event.getY()-Double.parseDouble(param[2]));
                            break;
                        }
                    }
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    public void onMouseClick(MouseEvent  evt){
        PointDeControleIG clickedPoint = null;
        //EtapeIG clickedEtape = null;
        for(EtapeIG e : monde){
            if (clickedPoint != null /*|| clickedEtape != null*/){break;}
            for(PointDeControleIG p : e){
                if(Math.pow(evt.getX()-p.getCenter()[0], 2)+Math.pow(evt.getY()-p.getCenter()[1], 2) < Math.pow(VuePointDeControleIG.RADUIS, 2)) {
                    clickedPoint = p;
                    selectionnerPointDeControle(clickedPoint);
                    return;
                }
            }
            /*if (Vec2.isInBox(evt.getX(), evt.getY(), e.getPosX(), e.getPosY(), e.getLargeur(), e.getHauteur())) {
                clickedEtape = e;
                break;
            }*/
        }
        if (pointDeControleSelectionner != null) {
            if (v1 == null) {
                v1 = new Vec2(evt.getX(), evt.getY());
                return;
            }
            if (v2 == null && v1 != null) {
                v2 = new Vec2(evt.getX(), evt.getY());
                return;
            }
        }
    }

    public void selectionnerPointDeControle(PointDeControleIG p) {
        //System.out.println("Point Selectionner!");
        if (pointDeControleSelectionner != null){
            if (v2 != null && v2 != null){
                try{
                    monde.ajouter(pointDeControleSelectionner, p, v1, v2);
                }catch (MondeException e){
                    VueExceptionAlert ve = new VueExceptionAlert(e);
                    //PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    //pause.setOnFinished(event -> ve.close());
                    //pause.play();
                }
                v1=null;
                v2=null;
                pointDeControleSelectionner=null;
                return;
            }
            try{
                monde.ajouter(pointDeControleSelectionner, p);
            }catch (MondeException e){
                VueExceptionAlert ve = new VueExceptionAlert(e);
                //PauseTransition pause = new PauseTransition(Duration.seconds(1));
                //pause.setOnFinished(event -> ve.close());
                //pause.play();
            }
            pointDeControleSelectionner = null;
        }else{
            pointDeControleSelectionner = p;
        }
    }

    public void update(Observable o, Object a)
    {
        getChildren().clear();
        vueEtapes.clear();
        pointToArc.clear();
        for (ArcIG arc : monde.iteratorArc()){
            if (arc instanceof LigneDroiteIG) {
                VueLigneDroiteIG arcLigne = new VueLigneDroiteIG(arc);
                arc.setVueArcIG(arcLigne);
                getChildren().add(arcLigne);
                pointToArc.put(arc.getPointDeControle1(), arcLigne);
                pointToArc.put(arc.getPointDeControle2(), arcLigne);
            }else{
                VueCourbeIG arcCourbe = new VueCourbeIG((CourbeIG)arc);
                arc.setVueArcIG(arcCourbe);
                getChildren().add(arcCourbe);
                pointToArc.put(arc.getPointDeControle1(), arcCourbe);
                pointToArc.put(arc.getPointDeControle2(), arcCourbe);
            }
        }
        for (EtapeIG e : monde){
            e.setVueEtape(null);
            VueEtapeIG vue_etape = null;
            if (e.estUneActivite()){
                vue_etape = new GrandBain(monde, e);
            }else if(e.estUnService()){
                vue_etape = new VueService(monde, e);
            }
            getChildren().add(vue_etape);
            vue_etape.ajouterLesPoints(this);
            e.setVueEtape(vue_etape);
            vueEtapes.add(vue_etape);
        }
        Iterator<Client> citr = monde.IteratorClient();
        if (citr == null) return;
        while (citr.hasNext()){
            Client client = citr.next();
            if (client.getEtape() != null){
                EtapeIG etape = monde.getCorrespondance(client.getEtape());
                if (etape != null) {
                    etape.ajouterClient(client);
                }
            }
        }
    }

    public HashMap<PointDeControleIG, VueArcIG> getPointToArc() {
        return pointToArc;
    }
}
