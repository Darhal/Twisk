package TwiskIG.exceptions;

public  abstract class TwiskException extends Exception
{
    private String type;

    public TwiskException(String type, String msg){
        super(msg);
        this.type=type;
    }

    public TwiskException(String msg){
        super(msg);
        this.type="Erreur";
    }

    public String getType() {
        return type;
    }
}
