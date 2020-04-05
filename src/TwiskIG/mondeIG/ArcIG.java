package TwiskIG.mondeIG;

import TwiskIG.vues.VueArcIG;

public abstract class ArcIG
{
    private PointDeControleIG pc1;
    private PointDeControleIG pc2;
    private VueArcIG vueArcIG;
    public enum TYPE{LIGNE, COURBE};
    protected TYPE type;

    public ArcIG(PointDeControleIG p1, PointDeControleIG p2) {
        pc1 = p1;
        pc2 = p2;
        p1.setLinkedTo(p2);
        p2.setLinkedTo(p1);
        ajouterSucc();
    }

    public PointDeControleIG getPointDeControle1(){return pc1;}
    public PointDeControleIG getPointDeControle2(){return pc2;}

    public VueArcIG getVueArcIG() {
        return vueArcIG;
    }

    public void setVueArcIG(VueArcIG vueArcIG) {
        this.vueArcIG = vueArcIG;
    }

    public boolean isSelected(){
        if (vueArcIG == null){return false;}
        return vueArcIG.isSelected();
    }

    public void setSelected(boolean selected){
        if (vueArcIG == null){return;}
        vueArcIG.setSelected(selected);
    }

    public TYPE getType() {
        return type;
    }

    private void ajouterSucc(){
        pc1.getEtape().ajouterSuccesseur(pc2.getEtape());
    }

    public void effacerSucc()
    {
        pc1.getEtape().effacerSuccesseur(pc2.getEtape());
    }
}
