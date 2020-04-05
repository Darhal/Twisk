package TwiskIG.outils;

public class Vec2
{
    private double x,y;
    public Vec2(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Vec2){return false;}
        Vec2 o = (Vec2)obj;
        if (x == o.x && y == o.y){
            return true;
        }
        return false;
    }

    public static boolean isInBox(double x, double y, double xmin, double ymin, double xmax, double ymax){
        return (x > xmin && y > ymin) && (x < xmax && y < ymax);
    }

    public boolean isInBox(double xmin, double ymin, double xmax, double ymax){
        return (x > xmin && y > ymin) && (x < xmax && y < ymax);
    }
}
