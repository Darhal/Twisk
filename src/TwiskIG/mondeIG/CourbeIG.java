package TwiskIG.mondeIG;

import TwiskIG.outils.Vec2;

public class CourbeIG extends ArcIG
{
    private Vec2 v1, v2;

    /**
     * Constructeur pour faire une courbe au lieu d'un arc
     * @param p1
     * @param p2
     * @param v1
     * @param v2
     */
    public CourbeIG(PointDeControleIG p1, PointDeControleIG p2, Vec2 v1, Vec2 v2)
    {
        super(p1, p2);
        type = TYPE.COURBE;
        this.v1=v1;
        this.v2=v2;
    }

    public Vec2 getV1() {
        return v1;
    }

    public Vec2 getV2() {
        return v2;
    }
}
