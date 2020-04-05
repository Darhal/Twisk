package TwiskIG.mondeIG;

public class PointDeControleIG
{
    private double cx;
    private double cy;
    private int orderID;
    private String id;
    private EtapeIG etape;
    private PointDeControleIG linkedTo;

    public PointDeControleIG(EtapeIG e, String id, double cx, double cy){
        this.etape=e;
        this.id=id;
        this.cx=cx;
        this.cy=cy;
        this.linkedTo=null;
        orderID = 0;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderID()
    {
        return this.orderID;
    }

    public double[] getCenter(){
        double[] cp = new double[2];
        cp[0]=cx;
        cp[1]=cy;
        return cp;
    }

    public void reset()
    {
        linkedTo = null;
    }

    public EtapeIG getEtape(){return etape;}
    public void setCenterX(double x){this.cx=x;}
    public void setCenterY(double y){this.cy=y;}
    public void setLinkedTo(PointDeControleIG p){linkedTo=p;}
    public PointDeControleIG getLinkedTo(){return linkedTo;}
}
