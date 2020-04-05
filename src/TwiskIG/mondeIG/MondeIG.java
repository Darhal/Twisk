package TwiskIG.mondeIG;

import TwiskIG.outils.Pair;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import TwiskIG.outils.FabriqueIdentifiant;
import TwiskIG.outils.Vec2;
import TwiskIG.vues.VueEtapeIG;
import TwiskIG.vues.VueExceptionAlert;
import TwiskIG.exceptions.MondeException;
import sun.misc.IOUtils;
import twisk.monde.*;
import twisk.outils.FabriqueNumero;
import twisk.outils.ThreadsManager;
import twisk.simulation.Client;
import twisk.simulation.Simulation;

public class MondeIG extends Observable implements Iterable<EtapeIG>, Observer
{
    public HashMap<String, EtapeIG> etapes;
    private ArrayList<ArcIG> arcs;
    private VueEtapeIG vueEtapeSelectionner;
    private CorrespondanceEtapes correspondanceEtapes;
    private Simulation simulation;
    private Entree.PROBA_TYPE probaType;
    private float delaiParams[];
    private int nbClients;

    /**
     * Constructeur
     */
    public MondeIG() {
        etapes = new HashMap<>();
        arcs = new ArrayList<>();
        vueEtapeSelectionner = null;
        probaType = Entree.PROBA_TYPE.UNIFORM;
        delaiParams = new float[2];
        delaiParams[0] = 6.f;
        delaiParams[1] = 3.f;
        nbClients = 8;
    }

    /**
     * demander la simulation du monde
     * @throws MondeException
     */
    public void simuler() throws MondeException
    {
        if (simulation != null){
            if (simulation.IsActive()){ // Simulation active lets stop it
                stopSimulation();
                return;
            }else{ // Intermediate situation
                throw new MondeException("Simulation already active", "Attempt to activate simulation twice, simulation is already active.");
            }
        }
        MondeIG m = this;
        verifierMoneIG();
        infoLabel.setText("Chargement, veuillez patienter...");
        FabriqueNumero.getInstance().reset();
        correspondanceEtapes = new CorrespondanceEtapes();
        Monde monde = creerMonde();
        Entree ent = (Entree) monde.getEntree();
        ent.setProbaType(probaType);
        ent.setDelaiParams(delaiParams);
        simulation = new Simulation();
        simulation.setNbClients(nbClients);
        simulation.addObserver(m);
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception{
                try{
                    simulation.simuler(monde);
                    Thread.sleep(10);
                }catch(InterruptedException e){
                }
                return null;
            }
        };
        ThreadsManager.getInstance().lancer(task);
    }

    private void verifierMoneIG() throws MondeException
    {
        boolean trouve=false;
        Iterator<EtapeIG> it=etapes.values().iterator();
        while (!trouve && it.hasNext()){
            EtapeIG etape = it.next();
            if(etape.estUneActivite()){
                ActiviteIG act=(ActiviteIG)etape;
                if(act.getType()== ActiviteIG.TYPE.ENTREE){
                    trouve=true;
                }
            }

        }
        if(trouve==false){
            throw new MondeException("Erreur dans le Monde","Il n y a pas d'entrée ");
        }
        boolean sortie=false;
        Iterator<EtapeIG> itS=etapes.values().iterator();
        while (!sortie && itS.hasNext()){
            EtapeIG etapeS=itS.next();
            if(etapeS.estUneActivite()){
                ActiviteIG act=(ActiviteIG)etapeS;
                if(act.getType()== ActiviteIG.TYPE.SORTIE){
                    sortie=true;
                }
            }

        }
        if(sortie==false){
            throw new MondeException("Erreur dans le Monde","Il n y a pas de sortie ");
        }
        for(EtapeIG etapeIG:etapes.values()){
            if (etapeIG.estUnService()){
                if (etapeIG.getSucc().estUnService()){
                    throw new MondeException("Erreur ajout succ","Un service ne peut pas avoir un service comme successeur");
                }
            }
            if (etapeIG.estUneActivite()){
                if (((ActiviteIG)etapeIG).getType() == ActiviteIG.TYPE.SORTIE){
                    continue;
                }
            }
            boolean chemin=false;
            EtapeIG verif=etapeIG;
            EtapeIG etapeIG1=verif.getSucc();
            while (!chemin && etapeIG1 != null){
                if (etapeIG1.estUneActivite()) {
                    ActiviteIG act = (ActiviteIG) etapeIG1;
                    if (act.getType() == ActiviteIG.TYPE.SORTIE) {
                        chemin = true;
                    } else {
                        etapeIG1=verif.getSucc();
                        verif = etapeIG1;
                    }
                } else {
                    etapeIG1=verif.getSucc();
                    verif = etapeIG1;
                }
            }
            if(chemin==false){
                throw new MondeException("Erreur dans le Monde",etapeIG.getNom()+" n'a pas de chemin vers la sortie");
            }
        }
    }

    /**
     * arrete la simulation
     */
    public void stopSimulation()
    {
        if (simulation != null && simulation.IsActive()){ // simulation stopped.
            simulation.stopSimulation();
            ThreadsManager.getInstance().detruireTout();
            //System.out.println("SIMULATION STOPPED.");
        }
        simulation = null; // delete.
        infoLabel.setText("Pret");
        setChanged();
        notifyObservers();
    }

    private Etape convert(Monde monde, EtapeIG e)
    {
        Etape etape = null;
        if (correspondanceEtapes.doesContainKey(e)) return correspondanceEtapes.get(e);
        if (e.estUnService()){
            ServiceIG serviceIG = (ServiceIG)e;
            Service service = new Service(e.getNom(), serviceIG.getNbJetons());
            monde.ajouter(service);
            etape = service;
        }else if(e.estUneActivite()){
            ActiviteIG actIG = (ActiviteIG)e;
            Activite act = new Activite(e.getNom(), (int)actIG.getDelai(), (int)actIG.getEcartTemp());
            if(actIG.getType() == ActiviteIG.TYPE.ENTREE){
                monde.ajouterEntree(act);
            }else if(e.estUneActiviteRestrainte()){
                monde.ajouterSortie(act);
            }
            monde.ajouter(act);
            etape = act;
        }
        correspondanceEtapes.ajouter(e, etape);
        return etape;
    }

    /**
     * COnstruction du graphe
     * @param monde
     * @param etape
     * @param head
     */
    public void constrcutGraph(Monde monde, EtapeIG etape, Etape head)
    {
        //if (etape.getListeSucc() == null || etape.getListeSucc().size() == 0) return;
        //System.out.println("Head = "+head);
        for (EtapeIG e : etape.getListeSucc()){
            Etape succ = convert(monde, e);
            //System.out.println("    Succ = "+succ);
            head.ajouterSuccesseur(succ);
        }
        for(EtapeIG e : etape.getListeSucc()){
            constrcutGraph(monde, e, correspondanceEtapes.get(e));
        }
    }

    /**
     *
     * @return un nouveau monde
     */
    private Monde creerMonde()
    {
        FabriqueNumero.getInstance().reset();
        Monde monde = new Monde();
        ActiviteIG firstAct = null;
        for (EtapeIG e : this){
            if (e.estUneActivite()){
                ActiviteIG act = (ActiviteIG)e;
                if (act.getType() == ActiviteIG.TYPE.ENTREE){
                    firstAct = act;
                    break;
                }
            }
        }
        Etape head = convert(monde, firstAct);
        constrcutGraph(monde, firstAct, head);
        //System.out.println("Construction finished....");
        return monde;
    }

    public void ajouter(String type) {
        if (type.equals("Activité")){
            ActiviteIG act = new ActiviteIG(this, "Activité-", FabriqueIdentifiant.getInstance().getIdentifiantEtape());
            act.setNom(act.getNom()+act.getIdentifiant());
            Random ran = new Random();
            act.setPosX(ran.nextInt(1000));
            act.setPosY(ran.nextInt(750));
            etapes.put(act.getIdentifiant(), act);
        }else if(type.equals("Service")){
            ServiceIG ser = new ServiceIG(this, "Service-", FabriqueIdentifiant.getInstance().getIdentifiantEtape());
            ser.setNom(ser.getNom()+ser.getIdentifiant());
            Random ran = new Random();
            ser.setPosX(ran.nextInt(1000));
            ser.setPosY(ran.nextInt(750));
            etapes.put(ser.getIdentifiant(), ser);
        }
        setChanged();
        notifyObservers();
    }

    public void update(Observable o, Object a) // update called from simulation.
    {
        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                if (simulation != null && simulation.IsActive()) {
                    infoLabel.setText("Simulation en cours");
                    setChanged();
                    notifyObservers();
                }else{
                    stopSimulation();
                    infoLabel.setText("Pret");
                }
            }
        };
        Platform.runLater(cmd); // run in javafx thread
    }

     public void ajouter(PointDeControleIG p1, PointDeControleIG p2) throws MondeException
    {
        if(p2.getEtape().estUnService() || p1.getEtape().estUnService()){
            if (p2.getEtape().estUnService()) {
                if (p2.getEtape().estReliePred()) {
                    if (!p2.equals(p2.getEtape().getPointDeDepart())  ) {
                        throw new MondeException("Erreur ajout d'un arc", "le sens de circulation des clients est incorrect depart" );
                    }
                    else {
                        p2.getEtape().ajouterpred(p1.getEtape());
                    }
                } else {
                    if (p2.equals(p2.getEtape().getPointDArrive())){
                        throw new MondeException("Erreur ajout d'un arc", "le sens de circulation des clients est incorrect depart" );
                    }
                    p2.getEtape().setReliePred(true);
                    p2.getEtape().setPointDeDepart(p2);
                }
            }
            if (p1.getEtape().estUnService()){
                if (p1.getEtape().estRelieSucc()){
                    if (!p1.equals(p1.getEtape().getPointDArrive())){
                        throw new MondeException("Erreur ajout d'un arc", "le sens de circulation des clients est incorrect arrivee");
                    }
                }
                else {
                    if (p1.equals(p1.getEtape().getPointDeDepart())){
                        throw new MondeException("Erreur ajout d'un arc", "le sens de circulation des clients est incorrect arrivee" );
                    }
                    p1.getEtape().setRelieSucc(true);
                    p1.getEtape().setPointDArrive(p1);
                }
            }
        }

        if (p1.getEtape().isPointInEtape(p2) || p2.getEtape().isPointInEtape(p1)){ // Attempt to relink to the same step!
            throw new MondeException("Erreur ajout d'un arc", "Creation d'un arc est impossible: Attempt to relink to the same step!");
        }
        if (p1.getEtape().estSuccesseur(p2.getEtape()) || p2.getEtape().estSuccesseur(p1.getEtape())){
            throw  new MondeException("Erreur d'arc","les deux étapes ont deja ete liees");
        }
        arcs.add(new LigneDroiteIG(p1, p2));
        setChanged();
        notifyObservers();
    }

    public void ajouter(PointDeControleIG p1, PointDeControleIG p2, Vec2 v1, Vec2 v2) throws MondeException
    {
        if (p1.getLinkedTo() != null || p2.getLinkedTo() != null){ // No bifurcations supported !
            throw new MondeException("Erreur ajout d'un arc", "Creation d'un arc est impossible: No bifurcations supported!");
        }
        if (p1.getEtape().isPointInEtape(p2)){ // Attempt to relink to the same step!
            throw new MondeException("Erreur ajout d'un arc", "Creation d'un arc est impossible: Attempt to relink to the same step!");
        }
        arcs.add(new CourbeIG(p1, p2, v1, v2));
        setChanged();
        notifyObservers();
    }

    public void effacerLaSelection()
    {
        for(EtapeIG e : this){
            if (e.getVueEtape().isSelected()){
                e.getVueEtape().setSelected(false);
            }
        }
        for (Iterator<ArcIG> itr = arcs.iterator(); itr.hasNext(); ) {
            ArcIG arc = itr.next();
            if (arc.isSelected()) {
                arc.setSelected(false);
            }
        }
    }

    public void selectionnerUneEtape(MouseEvent e, VueEtapeIG vueEtape)
    {
        if (vueEtape.isSelected()){
            vueEtapeSelectionner = null;
        }else{
            vueEtapeSelectionner = vueEtape;
        }
        vueEtape.setSelected(!vueEtape.isSelected());
    }

    public ArcIG getArcFromPoints(PointDeControleIG p1, PointDeControleIG p2)
    {
        for (Iterator<ArcIG> itr = arcs.iterator(); itr.hasNext(); ) {
            ArcIG arc = itr.next();
            if (arc.getPointDeControle1() == p1 && arc.getPointDeControle2() == p2 || arc.getPointDeControle1() == p2 && arc.getPointDeControle2() == p1) {
                return arc;
            }
        }
        return null;
    }

    public void supprimerLaSelectionner(){
        for(EtapeIG e : this){
            if (e.getVueEtape().isSelected()){
                e.getVueEtape().setSelected(false);
                for (PointDeControleIG p : e){
                    if (p.getLinkedTo() != null){
                        arcs.remove(getArcFromPoints(p, p.getLinkedTo()));
                        p.getLinkedTo().reset();
                        p.reset();
                    }
                }
                etapes.remove(e.getIdentifiant());
            }
        }
        for (Iterator<ArcIG> itr = arcs.iterator(); itr.hasNext(); ) {
            ArcIG arc = itr.next();
            if (arc.isSelected()) {
                arc.setSelected(false);
                arc.effacerSucc();
                if (arc.getPointDeControle2().getEtape().estUnService()){
                    arc.getPointDeControle2().getEtape().removePred(arc.getPointDeControle1().getEtape());
                    if (arc.getPointDeControle2().getEtape().getNombrepred()==0){
                        try {
                            arc.getPointDeControle2().getEtape().setReliePred(false);
                        } catch (MondeException e) {
                            e.printStackTrace();
                        }
                    }
                }
                arc.getPointDeControle1().getEtape().effacerSuccesseur(arc.getPointDeControle2().getEtape());
                if (arc.getPointDeControle1().getEtape().estUnService()){
                    if (arc.getPointDeControle1().getEtape().getNbSucc()==0){
                        try {
                            arc.getPointDeControle1().getEtape().setRelieSucc(false);
                        } catch (MondeException e) {
                            e.printStackTrace();
                        }
                    }
                }
                arc.getPointDeControle1().reset();
                arc.getPointDeControle2().reset();
                itr.remove();
            }
        }
        setChanged();
        notifyObservers();
    }

    public void renommerEtapeSelectionner()
    {
        try {
            renameEtape();
        }catch (MondeException e){
            VueExceptionAlert ve = new VueExceptionAlert(e);
            //PauseTransition pause = new PauseTransition(Duration.seconds(1));
            //pause.setOnFinished(event -> ve.close());
            //pause.play();
        }
    }

    public void renameEtape() throws MondeException{
        if (vueEtapeSelectionner == null){
            throw new MondeException("Erreur avec renommage d'étape", "Erreur: Tentative de renommer une étape qui n'est pas sélectionnée ou qui n'existe plus.");
        }
        TextInputDialog renameDialog = new TextInputDialog();
        renameDialog.setTitle("Renommer une etape");
        renameDialog.setHeaderText("Entrez un nouveau nom d'étape");
        renameDialog.setContentText("Veuillez entrer le nouveau nom de l'étape:");
        Optional<String> name = renameDialog.showAndWait();
        if (name.isPresent()){
            vueEtapeSelectionner.getEtape().setNom(name.get());
            vueEtapeSelectionner.setSelected(false);
        }
    }

    public void toggleCommeSortie(){
        for(EtapeIG e : this){
            if (e.getVueEtape().isSelected() && e.estUneActivite()){
                ActiviteIG act = (ActiviteIG)e;
                if (act.getType() == ActiviteIG.TYPE.SORTIE){
                    act.setType(ActiviteIG.TYPE.ACTIVITE);
                }else{
                    act.setType(ActiviteIG.TYPE.SORTIE);
                }
                act.getVueActivite().updateIcon();
                act.getVueActivite().setSelected(false);
            }
        }
    }

    public void toggleCommeEntree(){
        for(EtapeIG e : this){
            if (e.getVueEtape().isSelected() && e.estUneActivite()){
                ActiviteIG act = (ActiviteIG)e;
                if (act.getType() == ActiviteIG.TYPE.ENTREE){
                    act.setType(ActiviteIG.TYPE.ACTIVITE);
                }else{
                    act.setType(ActiviteIG.TYPE.ENTREE);
                }
                act.getVueActivite().updateIcon();
                act.getVueActivite().setSelected(false);
            }
        }
    }

    public void attemptModifierDelai(){
        try {
            modifierDelai();
        }catch (MondeException e){
            VueExceptionAlert ve = new VueExceptionAlert(e);
            //PauseTransition pause = new PauseTransition(Duration.seconds(1));
            //pause.setOnFinished(event -> ve.close());
            //pause.play();
        }
    }

    public void attemptModifierEcartTemp(){
        try {
            modifierEcartTemp();
        }catch (MondeException e){
            VueExceptionAlert ve = new VueExceptionAlert(e);
            //PauseTransition pause = new PauseTransition(Duration.seconds(1));
            //pause.setOnFinished(event -> ve.close());
            //pause.play();
        }
    }

    public void modifierDelai() throws MondeException
    {
        if (vueEtapeSelectionner == null || !(vueEtapeSelectionner.getEtape().estUneActivite())){
            throw new MondeException("Erreur modifier delai d'une étape", "Erreur: Tentative de modifier le délai une activité qui n'est pas sélectionnée ou qui n'existe plus.");
        }
        TextInputDialog renameDialog = new TextInputDialog();
        renameDialog.setTitle("Modifier le délai d'une activité");
        renameDialog.setHeaderText("Entrez la nouvel valeur");
        renameDialog.setContentText("Veuillez entrer la nouvel valeur de délai pour cette activité:");
        Optional<String> delai = renameDialog.showAndWait();
        if (delai.isPresent()){
            double value = Double.parseDouble(delai.get());
            if (value < 0.0){
                throw new MondeException("Erreur modifier delai d'une étape", "Erreur: Tentative de modifier le délai d'une activité avec une valeur négative.");
            }
            ((ActiviteIG)vueEtapeSelectionner.getEtape()).setDelai(value);
            vueEtapeSelectionner.setTitre(null);
            vueEtapeSelectionner.setSelected(false);
        }
    }

    public void modifierEcartTemp() throws MondeException
    {
        if (vueEtapeSelectionner == null || !(vueEtapeSelectionner.getEtape().estUneActivite())){
            throw new MondeException("Erreur modifier ecart temps d'une étape", "Erreur: Tentative de modifier l'ecart temps une activité qui n'est pas sélectionnée ou qui n'existe plus.");
        }
        TextInputDialog renameDialog = new TextInputDialog();
        renameDialog.setTitle("Modifier l'ecart temps d'une activité");
        renameDialog.setHeaderText("Entrez la nouvel valeur");
        renameDialog.setContentText("Veuillez entrer la nouvel valeur de l'ecart temps pour cette activité:");
        Optional<String> et = renameDialog.showAndWait();
        if (et.isPresent()){
            double value = Double.parseDouble(et.get());
            if (value < 0.0){
                throw new MondeException("Erreur modifier ecart temps d'une étape", "Erreur: Tentative de modifier l'ecart temps une activité avec une valeur négative.");
            }
            ((ActiviteIG)vueEtapeSelectionner.getEtape()).setEcartTemp(value);
            vueEtapeSelectionner.setTitre(null);
            vueEtapeSelectionner.setSelected(false);
        }
    }

    public EtapeIG getCorrespondance(Etape e)
    {
        return correspondanceEtapes.get(e);
    }

    public boolean IsSimulationActive()
    {
        return simulation != null;
    }

    @Override
    public Iterator<EtapeIG> iterator() {
        ArrayList<EtapeIG> al = new ArrayList();
        for(Map.Entry<String, EtapeIG> entry: etapes.entrySet()) {
            al.add(entry.getValue());
        }
        return al.iterator();
    }

    public Iterator<Client> IteratorClient()
    {
        if (simulation == null) return null;
        return simulation.iterator();
    }

    public ArrayList<ArcIG> iteratorArc() {
        return arcs;
    }

    public void attemptModifierNbJetons()
    {
        try {
            modifierNbJetons();
        }catch (MondeException e){
            VueExceptionAlert ve = new VueExceptionAlert(e);
            //PauseTransition pause = new PauseTransition(Duration.seconds(1));
            //pause.setOnFinished(event -> ve.close());
            //pause.play();
        }
    }

    private void modifierNbJetons() throws MondeException
    {
        if (vueEtapeSelectionner == null || !(vueEtapeSelectionner.getEtape().estUnService())){
            throw new MondeException("Erreur modifier nombre de jetons", "Erreur: Tentative de modifier le nombre de jetons d'un service qui n'est pas sélectionnée ou qui n'existe plus.");
        }
        TextInputDialog renameDialog = new TextInputDialog();
        renameDialog.setTitle("Modifier le ombre de jetons d'un service");
        renameDialog.setHeaderText("Entrez la nouvel valeur");
        renameDialog.setContentText("Veuillez entrer la nouvel valeur :");
        Optional<String> et = renameDialog.showAndWait();
        if (et.isPresent()){
            int value = Integer.parseInt(et.get());
            if (value < 0.0){
                throw new MondeException("Erreur modifier nombre de jetons", "Erreur: Tentative de modifier le nombre de jetons avec un valeur négative.");
            }
            ((ServiceIG)vueEtapeSelectionner.getEtape()).setNbJetons(value);
            vueEtapeSelectionner.setTitre(null);
            vueEtapeSelectionner.setSelected(false);
        }
    }

    public void clearWorld()
    {
        arcs.clear();
        for(Map.Entry<String, EtapeIG> p : etapes.entrySet()) {
            p.getValue().clearSuccs();
        }
        etapes.clear();
        FabriqueIdentifiant.getInstance().reset();
        setChanged();
        notifyObservers();
    }

    public void open(String path) throws IOException
    {
        //Read from file
        String content = new String(Files.readAllBytes(Paths.get(path)));
        charger(content);
    }

    public void charger(String content)
    {
        this.clearWorld(); // clear everything before loading new world.
        String reg = "}";
        String[] str = content.split(reg);
        String[] etapeData = new String[str.length];
        int i = 0;
        HashMap<EtapeIG, ArrayList<String[]>> etapeSuccData = new HashMap<>();
        HashMap<String, EtapeIG> etapeByName = new HashMap<>();
        for(String s : str){
            etapeData[i] = s.replaceAll("(\\s+|\\{+)", "");
            Pair<EtapeIG, ArrayList<String[]>> pair = parse(etapeData[i]);
            if (pair.getFirst() != null && pair.getSecond() != null){
                etapeSuccData.put(pair.getFirst(), pair.getSecond());
                etapeByName.put(pair.getFirst().getNom(), pair.getFirst());
            }
            i++;
        }
        for(Map.Entry<EtapeIG, ArrayList<String[]>> p : etapeSuccData.entrySet())
        {
            EtapeIG e = p.getKey();
            ArrayList<String[]> s = p.getValue();
            for (String[] sData : s){
                EtapeIG dest = etapeByName.get(sData[0]);
                if (dest != null){
                    parseToArc(sData, e, dest);
                    //e.ajouterSuccesseur(dest); will be already done by the praseArc!
                }
            }
        }
        setChanged();
        notifyObservers();
    }

    public Pair<EtapeIG, ArrayList<String[]>> parse(String str)
    {
        final String keyWords[][] = {
                {"TYPE:", "DELAI:", "ECART_TEMP:", "NOM:", "X:", "Y:", "SIZE_X:", "SIZE_Y:", "SUCC:"},
                {"TYPE:", "NB_JETONS:", "NOM:", "X:", "Y:", "SIZE_X:", "SIZE_Y:", "SUCC:"},
        };
        String[] keyData = str.split(";");
        String[] data = new String[keyData.length];
        int typeIndex = 0;
        if (keyData[0].equals("TYPE:SERVICE")){
            typeIndex = 1;
        }
        int i = 0;
        for(String kd : keyData){
            data[i] = kd.replaceAll(keyWords[typeIndex][i], "");
            i++;
        }
        return interpretToEtape(data);
    }

    public ArrayList<String[]> parseSucc(String succData)
    {
        final String[] keyWords = {"NEXT:", "SRC_P_ID:", "DEST_P_ID:", "ARC_TYPE:", "V1:", "V2:"};
        ArrayList<String[]> list = new ArrayList();
        if (succData.equals("[]")) return list;
        String removeedBrackets = succData.replaceAll("[\\[\\]]", "");
        String[] sData = removeedBrackets.split(",");
        for(String d : sData){
            String datas = d.replaceAll("[\\(\\)]", "");
            String[] data = datas.split("\\|");
            String[] dataValues = new String[data.length];
            int i = 0;
            for(String v : data){
                dataValues[i] = v.replaceAll(keyWords[i], "");
                i++;
            }
            list.add(dataValues);
        }
        return list;
    }

    private Pair<EtapeIG, ArrayList<String[]>> interpretToEtape(String[] data)
    {
        EtapeIG etapeIG = null;
        ArrayList<String[]> arr = new ArrayList<>();
        if(data[0].equals("ACTIVITE") || data[0].equals("ENTREE") || data[0].equals("SORTIE")){
            ActiviteIG act = new ActiviteIG(this, data[3], FabriqueIdentifiant.getInstance().getIdentifiantEtape());
            act.setDelai(Double.parseDouble(data[1]));
            act.setEcartTemp(Double.parseDouble(data[2]));
            act.setPosX(Double.parseDouble(data[4]));
            act.setPosY(Double.parseDouble(data[5]));
            act.setLargeur(Double.parseDouble(data[6]));
            act.setHauteur(Double.parseDouble(data[7]));
            if (data[0].equals("ENTREE")){
                act.setType(ActiviteIG.TYPE.ENTREE);
            }else if(data[0].equals("SORTIE")){
                act.setType(ActiviteIG.TYPE.SORTIE);
            }
            arr = parseSucc(data[8]);
            etapeIG = act;
            etapes.put(act.getIdentifiant(), act);
        }else if(data[0].equals("SERVICE")){
            ServiceIG ser = new ServiceIG(this, data[2], FabriqueIdentifiant.getInstance().getIdentifiantEtape());
            ser.setNbJetons(Integer.parseInt(data[1]));
            ser.setPosX(Double.parseDouble(data[3]));
            ser.setPosY(Double.parseDouble(data[4]));
            ser.setLargeur(Double.parseDouble(data[5]));
            ser.setHauteur(Double.parseDouble(data[6]));
            arr = parseSucc(data[7]);
            etapeIG = ser;
            etapes.put(ser.getIdentifiant(), ser);
        }
        return new Pair(etapeIG, arr);
    }

    public ArcIG parseToArc(String[] arcData, EtapeIG src, EtapeIG dest)
    {
        ArcIG arc = null;
        if (arcData.length < 4) return arc;
        String type = arcData[3];
        if (type.equals("COURBE")){
            PointDeControleIG p1 = src.getPointFromOrderID(Integer.parseInt(arcData[1]));
            PointDeControleIG p2 = dest.getPointFromOrderID(Integer.parseInt(arcData[2]));
            String[] v1 = arcData[4].split("\\-");
            String[] v2 = arcData[5].split("\\-");
            Vec2[] vecs = {
                    new Vec2(Double.parseDouble(v1[0]), Double.parseDouble(v1[1])),
                    new Vec2(Double.parseDouble(v2[0]), Double.parseDouble(v2[1]))
            };
            arc = new CourbeIG(p1, p2, vecs[0], vecs[1]);
        }else{
            PointDeControleIG p1 = src.getPointFromOrderID(Integer.parseInt(arcData[1]));
            PointDeControleIG p2 = dest.getPointFromOrderID(Integer.parseInt(arcData[2]));
            arc = new LigneDroiteIG(p1, p2);
        }
        arcs.add(arc);
        return arc;
    }

    public void save(String path) throws IOException {
        //Save the files using the format.
        if (!path.contains(".twisk")){
            path = path+".twisk";
        }
        FileWriter fw = new FileWriter(path);
        for (Map.Entry<String, EtapeIG> e : etapes.entrySet()) {
            fw.write(e.getValue().serilizeObject());
        }
        fw.close();
    }

    public void setLoiEntree(Entree.PROBA_TYPE type, float field1, float field2)
    {
        probaType = type;
        delaiParams[0] = field1;
        delaiParams[1] = field2;
    }

    public void modifierNbCLients(int nb)
    {
        nbClients = nb;
    }

    public String getResourceFileAsString(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
    }

    public void chargerMonde(String path) {
        this.charger(getResourceFileAsString(path));
    }

    public void setPrivateLabel(Label lbl)
    {
        infoLabel = lbl;
    }

    private Label infoLabel;
}
