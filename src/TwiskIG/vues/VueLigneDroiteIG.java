package TwiskIG.vues;

import javafx.event.Event;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;;
import javafx.scene.shape.StrokeLineCap;
import TwiskIG.mondeIG.ArcIG;

public class VueLigneDroiteIG extends VueArcIG
{
    private Line line;

    public VueLigneDroiteIG(ArcIG a)
    {
        super(a);
        line = new Line();
        line.setStartX(a.getPointDeControle1().getCenter()[0]);
        line.setStartY(a.getPointDeControle1().getCenter()[1]);
        line.setEndX(a.getPointDeControle2().getCenter()[0]);
        line.setEndY(a.getPointDeControle2().getCenter()[1]);
        line.setStroke(Color.rgb(211, 46, 159, 1.0));
        line.setStrokeWidth(2);
        line.setStrokeLineCap(StrokeLineCap.SQUARE);
        line.setFill(Color.TRANSPARENT.deriveColor(0, 1.2, 1, 0.6));
        ArrowHead();
        this.getChildren().add(line);
        this.setPickOnBounds(false);
        this.setOnMouseClicked(event -> onMouseClick(event));
    }

    public void onMouseClick(Event event){
        setSelected(!isSelected());
    }

    protected void onSelectColorChange(){
        if (isSelected()){
            line.setStroke(Color.rgb(168, 30, 59, 1.0));
            head.setStyle("-fx-fill:#A81E3B");
        }else{
            line.setStroke(Color.rgb(211, 46, 159, 1.0));
            head.setStyle("-fx-fill:#D32E9F");
        }
    }

    //https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4
    protected void ArrowHead(){
        final double ARROW_HEAD_SIZE = 15.0;

        double startX = arc.getPointDeControle1().getCenter()[0];
        double startY = arc.getPointDeControle1().getCenter()[1];
        double endX = arc.getPointDeControle2().getCenter()[0];
        double endY = arc.getPointDeControle2().getCenter()[1];

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;

        head = new Polygon();
        head.strokeProperty().bind(head.fillProperty());
        head.setStyle("-fx-fill:#D32E9F");
        head.getPoints().addAll(new Double[]{x1, y1, x2, y2, endX, endY, x1, y1});
        getChildren().add(head);
    }

    protected void updateArrowHead()
    {
        double startX = arc.getPointDeControle1().getCenter()[0];
        double startY = arc.getPointDeControle1().getCenter()[1];
        double endX = arc.getPointDeControle2().getCenter()[0];
        double endY = arc.getPointDeControle2().getCenter()[1];
        if (head != null){
            head.getPoints().clear();
            final double ARROW_HEAD_SIZE = 15.0;
            //ArrowHead
            double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            //point1
            double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
            double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;
            //point2
            double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
            double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;
            head.getPoints().addAll(new Double[]{x1, y1, x2, y2, endX, endY, x1, y1});
        }
    }

    @Override
    public void updateArc(){
        line.setStartX(arc.getPointDeControle1().getCenter()[0]);
        line.setStartY(arc.getPointDeControle1().getCenter()[1]);
        line.setEndX(arc.getPointDeControle2().getCenter()[0]);
        line.setEndY(arc.getPointDeControle2().getCenter()[1]);
        updateArrowHead();
    }
}
