package twisk;

import twisk.monde.Activite;
import twisk.monde.Monde;
import twisk.simulation.Simulation;

public class ClientTwisk {
    public static void main(String[] args){
        Monde monde = new Monde();

        Activite act1 = new Activite("Balade au zoo", 10, 2);
        Activite act2 = new Activite("Toboggan", 10, 2);

        act1.ajouterSuccesseur(act2);
        act2.ajouterSuccesseur(monde.getSortie());

        monde.ajouter(new twisk.monde.Etape[] { act1 });
        monde.ajouter(new twisk.monde.Etape[] { act2 });

        monde.ajouterEntree(act1);
        monde.ajouterSortie(act2);

        for (twisk.monde.Etape etape : monde) {
            System.out.println(etape);
        }

        Simulation simulation=new Simulation();
        simulation.setNbClients(6);
        simulation.simuler(monde);
    }
}
