package TwiskIG.mondeIG;

public class LigneDroiteIG extends ArcIG
{
    public LigneDroiteIG(PointDeControleIG p1, PointDeControleIG p2) {
        super(p1, p2);
        type = TYPE.LIGNE;
    }
}
