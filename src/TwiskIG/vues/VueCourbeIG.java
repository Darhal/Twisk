package TwiskIG.vues;

import javafx.event.Event;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import TwiskIG.mondeIG.CourbeIG;

public class VueCourbeIG extends VueArcIG {
    private CubicCurve cubicCurve;

    public VueCourbeIG(CourbeIG c) {
        super(c);
        cubicCurve = new CubicCurve();
        cubicCurve.setStartX(c.getPointDeControle1().getCenter()[0]);
        cubicCurve.setStartY(c.getPointDeControle1().getCenter()[1]);
        cubicCurve.setEndX(c.getPointDeControle2().getCenter()[0]);
        cubicCurve.setEndY(c.getPointDeControle2().getCenter()[1]);
        cubicCurve.setControlX1(c.getV1().getX());
        cubicCurve.setControlY1(c.getV1().getY());
        cubicCurve.setControlX2(c.getV2().getX());
        cubicCurve.setControlY2(c.getV2().getY());

        cubicCurve.setStroke(Color.rgb(211, 46, 159, 1.0));
        cubicCurve.setStrokeWidth(2);
        cubicCurve.setStrokeLineCap(StrokeLineCap.ROUND);
        cubicCurve.setFill(Color.TRANSPARENT);
        getChildren().add(cubicCurve);

        head = new VueCubicArrow(cubicCurve, 1.0f);
        getChildren().add(head);

        this.setPickOnBounds(false);
        this.setOnMouseClicked(event -> onMouseClick(event));
    }

    public void onMouseClick(Event event){
        setSelected(!isSelected());
    }

    protected void onSelectColorChange(){
        if (isSelected()){
            cubicCurve.setStroke(Color.rgb(168, 30, 59, 1.0));
            head.setStyle("-fx-fill:#A81E3B");
        }else{
            cubicCurve.setStroke(Color.rgb(211, 46, 159, 1.0));
            head.setStyle("-fx-fill:#D32E9F");
        }
    }

    @Override
    public void updateArc(){
        cubicCurve.setStartX(arc.getPointDeControle1().getCenter()[0]);
        cubicCurve.setStartY(arc.getPointDeControle1().getCenter()[1]);
        cubicCurve.setEndX(arc.getPointDeControle2().getCenter()[0]);
        cubicCurve.setEndY(arc.getPointDeControle2().getCenter()[1]);
        ((VueCubicArrow)head).update();
    }
}
