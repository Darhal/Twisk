package TwiskIG.vues;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import TwiskIG.mondeIG.ArcIG;

public abstract class VueArcIG extends Pane
{
    protected ArcIG arc;
    protected Polygon head;
    private boolean isSelected;

    public VueArcIG(ArcIG a)
    {
        this.arc=a;
    }

    public abstract void updateArc();

    public void setSelected(boolean selected) {
        isSelected=selected;
        onSelectColorChange();
    }

    protected abstract void onSelectColorChange();

    public ArcIG getArc(){
        return arc;
    }

    public boolean isSelected(){return isSelected;}
}
