package TwiskIG.mondeIG;

import TwiskIG.exceptions.MondeException;
import TwiskIG.vues.VueEtapeIG;
import twisk.simulation.Client;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class EtapeIG implements Iterable<PointDeControleIG>
{
    private String nom="N/A";
    private String identifiant;
    private double posX=0;
    private double posY=0;
    protected double largeur=150;
    protected double hauteur=35;
    private PointDeControleIG pointDeControles[]; // Array because its fixed size!
    private VueEtapeIG vueEtape;
    private MondeIG monde;
    protected int numPoints;

    private ArrayList<EtapeIG> listeEtapes, listePrede;
    private boolean reliePred, relieSucc;
    private PointDeControleIG pointDeDepart, pointDArrive;

    /***
     * Constructeur
     * @param m
     * @param nom
     * @param idf
     * @param larg
     * @param haut
     */
    public EtapeIG(MondeIG m, String nom, String idf, double larg, double haut)
    {
        this.nom=nom;this.identifiant=idf;
        this.largeur=larg;this.hauteur=haut;
        listeEtapes = new ArrayList<>();
        numPoints = 0;
	    listePrede=new ArrayList<>();
        reliePred=false;
        relieSucc=false;
        monde = m;
        //ajouterLesPoints();
    }

    public EtapeIG(MondeIG m, String nom, String idf)
    {
        this.nom=nom;this.identifiant=idf;
        listeEtapes = new ArrayList<>();
        numPoints = 0;
	    listePrede=new ArrayList<>();
        reliePred=false;
        relieSucc=false;
        monde = m;
        //ajouterLesPoints();
    }

    public void ajouterSuccesseur(EtapeIG etapeIG){
        listeEtapes.add(etapeIG);
    }
    public void effacerSuccesseur(EtapeIG etapeIG){
        listeEtapes.remove(etapeIG);
    }
    public ArrayList<EtapeIG> getListeSucc() {
        return listeEtapes;
    }
    public EtapeIG getSucc() {
        if (listeEtapes.size() == 0) return null;
        return listeEtapes.get(0);
    }

    protected void ajouterLesPoints()
    {
        this.pointDeControles = new PointDeControleIG[numPoints];
        double[][] pos_points = {
                {posX, posY+hauteur/2},
                {posX+largeur, posY+hauteur/2},
                {posX+largeur/2, posY+hauteur},
                {posX+largeur/2, posY},
        };
        for (int i =0;i<numPoints;i++){
            pointDeControles[i]=new PointDeControleIG(this, "test", pos_points[i][0], pos_points[i][1]);
            pointDeControles[i].setOrderID(i);
        }
    }

    public void CalculatePointsPos()
    {
        double[][] pos_points = {
                {posX, posY+(hauteur+(int) VueEtapeIG.SIZE_ADJUSTEMENT[1])/2},
                {posX+(largeur+(int)VueEtapeIG.SIZE_ADJUSTEMENT[0]), posY+(hauteur+(int)VueEtapeIG.SIZE_ADJUSTEMENT[1])/2},
                {posX+(largeur+(int)VueEtapeIG.SIZE_ADJUSTEMENT[0])/2, posY+(hauteur+(int)VueEtapeIG.SIZE_ADJUSTEMENT[1])},
                {posX+(largeur+(int)VueEtapeIG.SIZE_ADJUSTEMENT[0])/2, posY},
        };
        for (int i =0;i<numPoints;i++){
            pointDeControles[i].setCenterX(pos_points[i][0]);
            pointDeControles[i].setCenterY(pos_points[i][1]);
        }
    }

    /**
     *
     * @param p
     * @return true si p appartient a l'etape
     */
    public boolean isPointInEtape(PointDeControleIG p){
        for (PointDeControleIG pi : pointDeControles){
            if (pi == p){
                return true;
            }
        }
        return false;
    }
	 /***
     * Ne concerne que les services
     * @return true si le service est deja relié
     * false sinon
     */
    public boolean estReliePred() throws MondeException {
        if (estUneActivite()){
            throw new MondeException("Erreur d'etape", "la fonction ne concerne que les services");
        }
        return reliePred;
    }
    public boolean estRelieSucc() throws MondeException {
        if (estUneActivite()){
            throw new MondeException("Erreur d'etape", "la fonction ne concerne que les services");
        }
        return relieSucc;
    }
    /***
     * Ne concerne que les services
     * @param b
     */
    public void setReliePred(boolean b) throws  MondeException{
        if (estUneActivite()){
            throw new MondeException("Erreur d'etape", "la fonction ne concerne que les services");
        }
        reliePred=b;
    }
    public void setRelieSucc(boolean b) throws  MondeException{
        if (estUneActivite()){
            throw new MondeException("Erreur d'etape", "la fonction ne concerne que les services");
        }
        relieSucc=b;
    }
    /***
     *
     * @param etapeIG comme predecesseur de l'etape
     */
    public void ajouterpred(EtapeIG etapeIG){
        listePrede.add(etapeIG);
    }

    /**
     *
     * @param etapeIG supprime etpaeIG de la liste des predecesseurs
     */
    public void removePred(EtapeIG etapeIG){
        listePrede.remove(etapeIG);
    }

    /**
     * Ne concerne que les services
     * @param pointDArrive defini le point d'arriver ppour les clients
     */
    public void setPointDArrive(PointDeControleIG pointDArrive) {
        this.pointDArrive = pointDArrive;
    }
    public PointDeControleIG getPointDArrive(){
        return pointDArrive;
    }
    public int getNbSucc(){
        return listeEtapes.size();
    }
    /**
     *
     * @return le nombre de predecesseur
     */
    public int getNombrepred(){
        return listePrede.size();
    }

    /**
     * Ne concerne que les services
     * @return le point qui definie le sens de circulation des services
     */
    public PointDeControleIG getPointDeDepart(){
        return pointDeDepart;

    }
    public void setPointDeDepart(PointDeControleIG pointDeDepart){
        this.pointDeDepart=pointDeDepart;
    }
    public String getIdentifiant() {return identifiant;}

    public double getPosX() {return posX;}
    public void setPosX(double posX) {this.posX = posX;CalculatePointsPos();}

    public double getPosY() {return posY;}
    public void setPosY(double posY) {this.posY = posY;CalculatePointsPos();}

    public double getLargeur() {return largeur;}
    public void setLargeur(double l) {this.largeur = l;CalculatePointsPos();}

    public double getHauteur() {return hauteur;}
    public void setHauteur(double h) {this.hauteur = h;CalculatePointsPos();}

    public String getNom(){return nom;}
    public void setNom(String n){
        this.nom=n;
        if (vueEtape != null){
            this.vueEtape.setTitre(nom);
        }
    }

    public void ajouterClient(Client client)
    {
        if (vueEtape == null) return;
        vueEtape.ajouterClient(client); //delegate to vueEtape.
    }

    public PointDeControleIG[] getPointDeControle(){return pointDeControles;}

    @Override
    public Iterator<PointDeControleIG> iterator()
    {
        ArrayList<PointDeControleIG> al = new ArrayList();
        for(PointDeControleIG p : pointDeControles) {
            al.add(p);
        }
        return al.iterator();
    }

    public VueEtapeIG getVueEtape() {
        return vueEtape;
    }

    public void setVueEtape(VueEtapeIG vueEtape) {
        this.vueEtape = vueEtape;
    }

    public boolean estUnService(){
        return false;
    }

    public boolean estUneActivite(){
        return false;
    }

    public boolean estUneActiviteRestrainte(){
        return false;
    }

    public int getNumPoints()
    {
        return numPoints;
    }

    public boolean estAccessibleDepuis(EtapeIG etape){
        boolean chemin = false;
        EtapeIG verif = etape;
        EtapeIG etapeIG1 = verif.getSucc();
        while (!chemin && etapeIG1 != null){
            if (etapeIG1.estUneActivite()) {
                ActiviteIG act = (ActiviteIG) etapeIG1;
                if (act.equals(this)) {
                    chemin = true;
                } else {
                    etapeIG1=verif.getSucc();
                    verif = etapeIG1;
                }
            }else{
                etapeIG1 = verif.getSucc();
                verif = etapeIG1;
            }
        }
        return chemin;
    }
     /**
     *
     * @param etapeIG
     * @return true si etapeIG est un successeur de this
     * false sinon
     */
    public boolean estSuccesseur(EtapeIG etapeIG){
        if (listeEtapes.contains(etapeIG)){
            return true;
        }
        return false;
    }
    public ArrayList<EtapeIG> getSuccs()
    {
        return listeEtapes;
    }

    private PointDeControleIG getPointFromEtapes(EtapeIG e)
    {
        for(PointDeControleIG p : pointDeControles){
            if (p.getLinkedTo() == null) continue;
            String idOfLinkedTo = p.getLinkedTo().getEtape().getIdentifiant();
            if (idOfLinkedTo.equals(e.getIdentifiant())){
                return p;
            }
        }
        return null;
    }

    public PointDeControleIG getPointFromOrderID(int i)
    {
        return pointDeControles[i];
    }

    /**
     *
     * @return las erilization de l'etape pour la sauvegarde
     */
    public String serilizeHelper()
    {
        String str = "";
        str += "\tNOM: "+this.getNom()+";\n";
        str += "\tX: "+this.getPosX()+";\n";
        str += "\tY: "+this.getPosY()+";\n";
        str += "\tSIZE_X: "+this.getLargeur()+";\n";
        str += "\tSIZE_Y: "+this.getHauteur()+";\n";
        str += "\tSUCC: [";
        for(EtapeIG e : this.getSuccs()){
            PointDeControleIG src = this.getPointFromEtapes(e);
            PointDeControleIG dest = e.getPointFromEtapes(this);
            ArcIG arc = monde.getArcFromPoints(src, dest);
            str += "(NEXT: "+e.getNom()+" | SRC_P_ID: "+src.getOrderID()+" | DEST_P_ID: "+dest.getOrderID()+" | ARC_TYPE: "+arc.getType()+" | ";
            if (arc.getType() == ArcIG.TYPE.COURBE){
                CourbeIG c = (CourbeIG)arc;
                str += "V1: "+c.getV1().getX()+" - "+c.getV1().getY()+" | V2: "+c.getV2().getX()+" - "+c.getV2().getY();
            }
            str += "), ";
        }
        if (this.getSuccs().size() != 0) {
            str = str.substring(0, str.length() - 2);
        }
        str += "];\n";
        return str;
    }

    public abstract String serilizeObject();

    public void clearSuccs()
    {
        listeEtapes.clear();
    }
}
