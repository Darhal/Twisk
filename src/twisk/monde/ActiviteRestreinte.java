package twisk.monde;

public class ActiviteRestreinte extends Activite
{
    public ActiviteRestreinte(String nom)
    {
        super(nom);
    }
    public ActiviteRestreinte(String nom, int t, int e)
    {
        super(nom, t, e);
    }

    /**
     *
     * @return le code c d'un aR
     */
    @Override
    public String toC(){
        if (this.getSuccesseurs().size() == 1){return "";}
        StringBuilder s = new StringBuilder("\ttransfert(").append(
                this.getDefineNom()).append(","+this.getSuccesseur().getDefineNom()).append(");\n");
        return s.toString();

    }
}

