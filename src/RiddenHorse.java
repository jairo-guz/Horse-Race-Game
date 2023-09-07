import java.util.Comparator;
//DECORATOR PATTERN - Ridden Horses act as Decorators for Horses, letting us use the jockey riding as an "add-on" to their stats
public class RiddenHorse extends Horse{
    private Jockey jockey;
    private Horse horse;
    private int speedBonus;
    private int endBonus;

    private int distanceRun;


    public RiddenHorse(Jockey jockey, Horse horse)
    {
        this.jockey = jockey;
        this.horse = horse;

        this.speed = horse.getSpeed() + jockey.getSpeed();
        this.endurance = horse.getEndurance() + jockey.getEndurance();
        this.distanceRun = 0;
    }
    
    public String getName()
    {
        return horse.getName(); 
    }
    //Speed bonus based on jockey riding style
    public void addSpeed(int toAdd){
        this.speedBonus = toAdd;
    }
    //Endurance bonus based on jockey riding style
    public void addEnd(int toAdd){
        this.endBonus = toAdd;
    }
    //Speed is base speed stat times the fraction of endurance the horse has left
    public int getSpeed(){
        return (horse.getSpeed() + jockey.getSpeed() + speedBonus)*(this.getEndurance()/(horse.getEndurance() + jockey.getEndurance()));
    }

    public int getEndurance(){
        return horse.getEndurance() + jockey.getEndurance() + endBonus - (this.distanceRun/400);
    }

    public int getDistRun(){
        return this.distanceRun;
    }
    //For sorting horses during race
    public static Comparator<RiddenHorse> compareTo = new Comparator<RiddenHorse>() {
        public int compare (RiddenHorse horse1, RiddenHorse horse2){
            int dist1 = horse1.distanceRun;
            int dist2 = horse2.distanceRun;
            return dist2 - dist1;
        }
    };

    public void runDist(int dist){
        this.distanceRun += dist;
    }

    public String getJockName(){
        return jockey.getName();
    }

    public void win (int prize){
        horse.getStable().addCash(prize);
    }
    
}


