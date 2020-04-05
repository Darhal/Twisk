package twisk.simulation;

import twisk.monde.Monde;
import twisk.monde.Etape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import twisk.monde.Sortie;
import twisk.outils.KitC;

public class Simulation extends Observable implements Iterable<Client>
{
    private KitC kitC;
    private int nbClients;
    private ClientsManager clientsManager;
    private boolean IsSimulationActive;

    /**
     * Constructeur
     */
    public Simulation()
    {
        kitC = new KitC();
        kitC.creerEnvironnement();
    }

    /**
     * simulation
     * @param m
     */
    public void simuler(Monde m) {
        printEtapes(m); // Print debug info

        /*COMPILING C CODE*/
        //System.out.println("Compilation du code C generer...");
        kitC.creerFichier(m.toC());
        kitC.compiler();
        kitC.construireLaLibrairie();

        //LOADING AND RUNNING THE LIBRARY
        //System.out.println("Chargement de la biblio libTwisk.so...");
        System.load("/tmp/twisk/libTwisk.so") ;

        //System.out.println("Starting simultion...");
        CommecerSimultion(m); // Commencer l'excution du code.

        clientsManager.reset();

        if (IsActive()){
            stopSimulation(); // set flag to false..
            setChanged();
            notifyObservers();
            //System.out.println("SIMULATION TERMINE \n");
        }
    }

    /**
     * commencer la simulation
     * @param m
     */
    private void CommecerSimultion(Monde m)
    {
        clientsManager = new ClientsManager(nbClients);

        /*le code c de la simulation écrite en C*/
        int nbEtapes=m.nbEtapes();
        //System.out.println("Nombre total des etapes est = "+nbEtapes);
        int[] nbJettons = new int[m.getCptServices()];
        for (int i = 0; i<m.getCptServices();i++){
            nbJettons[i]=m.getServices().get(i).getNbJetons();
        }
        int sizetab=nbEtapes*nbClients+nbEtapes-1;
        int[] memoire_partager= new int[sizetab];
        for ( int i=0; i<start_simulation(nbEtapes, m.getCptServices(), nbClients, nbJettons).length;i++) {
            memoire_partager[i] = start_simulation(nbEtapes, m.getCptServices(), nbClients, nbJettons)[i];
        }
        memoire_partager = ou_sont_les_clients(nbEtapes, nbClients);

        //System.out.print("Les clients: ");
        afficher_clients(1, 1+memoire_partager[0], memoire_partager);
        clientsManager.setClients(getClients(1, 1+memoire_partager[0], memoire_partager));
        //System.out.print("\n\n");
        IsSimulationActive = true;

        try {
            while (memoire_partager[m.getNumeroDeSortie()*(nbClients+1)] != nbClients){
                memoire_partager = ou_sont_les_clients(nbEtapes, nbClients);
                int j = 0;
                int currentEtape = 0;
                int finalEtapeNumber = 0;
                int finalEtapeId = 0;

                while (j < nbClients*nbEtapes+nbEtapes){

                    Etape cEtape = m.getEtapeFromNumber(currentEtape);
                    if (cEtape instanceof Sortie){
                        finalEtapeNumber = currentEtape;
                        finalEtapeId = j;
                    }else{
                        if (memoire_partager[j] != 0){ // nb de client est !⁼ 0
                            //System.out.print("Etape "+currentEtape+" ("+cEtape.getNom()+") : "+memoire_partager[j]+" clients: ");
                            afficher_clients(j+1, 1+j+memoire_partager[j], memoire_partager);
                            updateClientManager(cEtape, j, memoire_partager);
                        }else{
                            //System.out.print("Etape "+currentEtape+" ("+cEtape.getNom()+") : "+memoire_partager[j]+" clients\n");
                        }
                    }
                    j+=nbClients+1;
                    currentEtape++;
                }
                if (memoire_partager[finalEtapeId] != 0){
                    Etape efinal = m.getEtapeFromNumber(finalEtapeNumber);
                    //System.out.print("Etape "+finalEtapeNumber+" ("+efinal.getNom()+") : "+memoire_partager[finalEtapeId]+" clients: ");
                    afficher_clients(finalEtapeId+1, 1+finalEtapeId+memoire_partager[finalEtapeId], memoire_partager);
                    updateClientManager(efinal, finalEtapeId, memoire_partager);
                }else{
                    //System.out.print("Etape "+finalEtapeNumber+" ("+m.getEtapeFromNumber(finalEtapeNumber).getNom()+") : "+memoire_partager[finalEtapeId]+" clients\n");
                }
                //System.out.print("__________________________________________________________\n");
                Thread.sleep(500);
            }
        }catch (InterruptedException e){
        }
        nettoyage();
    }

    /**
     * mise a jour du clientManager
     * @param e
     * @param j
     * @param memoire_partager
     */
    private void updateClientManager(Etape e, int j, int[] memoire_partager)
    {
        for (Integer clientId : getClients(j+1, 1+j+memoire_partager[j], memoire_partager)){
            clientsManager.allerA(clientId, e, 0);
        }
        setChanged();
        notifyObservers();
    }

    /**
     *
     * @param m
     */
    private void printEtapes(Monde m)
    {
        /*DEBUG OUTPUT*/
        /*Iterator<Etape> it = m.iterator();
        String s = it.next().getNom();
        System.out.println("\n********Les Etapes********\n");
        while (it.hasNext()){
            String s1 = it.next().getNom();
            System.out.println(s + " 1 successeur " + s1);
            s = s1;
        }

        System.out.println("********codeC********\n");
        System.out.println(m.toC());
        System.out.println("*********************");*/
    }

    /**
     *
     * @return true si la simulation est en cours
     */
    public boolean IsActive(){return IsSimulationActive;}
    public ClientsManager getClientsManager(){return clientsManager;}

    /**
     * arrete la simulation
     */
    public void stopSimulation(){ IsSimulationActive = false; }

    /**
     * set le nb de clients
     * @param nb
     */
    public void setNbClients(int nb){
        nbClients=nb;
    }

    /**
     * affiche les clients
     * @param s
     * @param c
     * @param v
     */
    private void afficher_clients(int s, int c, int[] v)
    {
        for(int i=s;i<c-1;i++){
            //System.out.print(v[i]+", ");
        }
        //System.out.println(v[c-1]);
    }

    /**
     *
     * @param s
     * @param c
     * @param v
     * @return
     */
    private ArrayList<Integer> getClients(int s, int c, int[] v)
    {
        ArrayList<Integer> clientsArr = new ArrayList<>(c-s);
        for(int i=s;i<c;i++){
            clientsArr.add(v[i]);
        }
        return clientsArr;
    }

    /**
     * iterateur de clients
     * @return
     */
    public Iterator<Client> iterator()
    {
        return clientsManager.iterator();
    }

    public native int[] start_simulation(int nbEtapes, int nbServices, int nbClients, int[] tabJetonsServices);
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    public native void nettoyage();
}
