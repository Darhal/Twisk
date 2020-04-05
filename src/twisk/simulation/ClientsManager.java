package twisk.simulation;

import twisk.monde.Etape;

import java.util.ArrayList;
import java.util.Iterator;

public class ClientsManager implements Iterable<Client>
{
    private ArrayList<Client> clients;
    private int nbCLients;

    /**
     * Constructeur
     * @param nbClients
     */
    public ClientsManager(int nbClients){
        clients = new ArrayList<>(nbClients);
        this.nbCLients = nbClients;
    }

    public void setClients(int... tabClients)
    {
        if (tabClients.length > nbCLients) return;
        for(int id : tabClients){
            clients.add(new Client(id));
        }
    }

    /**
     * mettre les clients dans une liste
     * @param tabClients
     */
    public void setClients(ArrayList<Integer> tabClients)
    {
        if (tabClients.size() > nbCLients) return;
        for(int id : tabClients){
            clients.add(new Client(id));
        }
    }

    /**
     *
     * @param numeroClient
     * @param etape
     * @param rang
     */
    public void allerA(int numeroClient, Etape etape, int rang)
    {
        for (Client c : this.clients){
            if (c.getNumeroClient() == numeroClient){
                c.allerA(etape, rang);
                break;
            }
        }
    }

    /**
     * tout effacer
     */
    public void reset()
    {
        clients.clear();
    }

    /**
     * iterateur client
     * @return
     */
    public Iterator<Client> iterator()
    {
        return clients.iterator();
    }
}
