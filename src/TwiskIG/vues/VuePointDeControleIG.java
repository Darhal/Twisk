package TwiskIG.vues;

import javafx.scene.shape.Circle;
import TwiskIG.mondeIG.MondeIG;
import TwiskIG.mondeIG.PointDeControleIG;

public class VuePointDeControleIG extends Circle
{
    public static final double RADUIS = 6.0;
    private MondeIG monde;
    private PointDeControleIG pointDeControleIG;
    private VueArcIG arc;

    public VuePointDeControleIG(PointDeControleIG p, MondeIG m)
    {
        this.setCenterX(p.getCenter()[0]);
        this.setCenterY(p.getCenter()[1]);
        this.setRadius(RADUIS);
        this.setStyle("-fx-fill:#D32E9F");
        this.monde = m;
        this.pointDeControleIG = p;
        this.setOnMouseEntered(event -> OnMouseEnter());
        this.setOnMouseExited(event -> OnMouseLeave());
        //this.setOnMouseClicked(event -> vd.onMouseClick(event));
        //addEventHandler(MouseEvent.MOUSE_CLICKED, event -> vd.onPointControleClicked(p));
    }

    private void OnMouseLeave()
    {
        this.setStyle("-fx-fill:#D32E9F");
    }

    private void OnMouseEnter()
    {
        this.setStyle("-fx-fill:#CC3300");
    }

    public MondeIG getMonde() {
        return monde;
    }

    public PointDeControleIG getPointDeControleIG() {
        return pointDeControleIG;
    }

    public void updatePos(){
        this.setCenterX(pointDeControleIG.getCenter()[0]);
        this.setCenterY(pointDeControleIG.getCenter()[1]);
    }

    public VueArcIG getArc() {
        return arc;
    }

    public void setArc(VueArcIG arc) {
        this.arc = arc;
    }
}
