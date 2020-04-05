package TwiskIG.mondeIG;

import twisk.monde.Etape;

import java.util.HashMap;

public class CorrespondanceEtapes
{
    private HashMap<EtapeIG, Etape> etapeIG_To_Etape;
    private HashMap<Etape, EtapeIG> etape_To_EtapeIG;

    public CorrespondanceEtapes()
    {
        etapeIG_To_Etape = new HashMap<>();
        etape_To_EtapeIG = new HashMap<>();
    }

    /**
     * a fin de convertir les etapesIG en etape et vice versa
     * @param eti
     * @param et
     */
    public void ajouter(EtapeIG eti, Etape et){
        etapeIG_To_Etape.put(eti, et);
        etape_To_EtapeIG.put(et, eti);
    }

    public Etape get(EtapeIG e){
        return etapeIG_To_Etape.get(e);
    }


    public EtapeIG get(Etape e){
        return etape_To_EtapeIG.get(e);
    }

    public boolean doesContainKey(EtapeIG e){
        return etapeIG_To_Etape.containsKey(e);
    }
}
