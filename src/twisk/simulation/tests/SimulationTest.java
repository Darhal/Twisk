/*package twisk.simulation.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twisk.monde.Activite;
import twisk.monde.Etape;
import twisk.monde.Monde;
import twisk.monde.Service;
import twisk.simulation.Simulation;

class SimulationTest {
    private Monde monde;
    private Etape balade,accestoboggan,toboggan;
    private Simulation simulation;
    @BeforeEach
    void setUp() {
        monde=new Monde();
        balade=new Activite("balade au zoo");
        accestoboggan=new Service("Acces au toboggan",5);
        toboggan=new Activite("toboggan");
        simulation=new Simulation();
    }

    @Test
    void simuler() {
        monde.ajouter(balade,accestoboggan,toboggan);
        simulation.simuler(monde);
    }
}*/