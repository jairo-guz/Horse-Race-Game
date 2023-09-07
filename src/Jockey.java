import java.util.ArrayList;
import java.util.Random;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Jockey extends Employee 
{
    RidingStyle style;

    Jockey(String name)
    {
        Random rand = new Random();
        int index = rand.nextInt(5);
        this.name = name;

        double whatStyle = Math.random();

        //Generate style
        if (whatStyle < .333)
        {
            style = new AgressiveRiding();
        } 
        else if (whatStyle < .667)
        {
            style = new BalancedRiding();
        } 
        else 
        {
            style = new ConservativeRiding();
        }

        this.speed = rand.nextInt(20) + 1;
        this.endurance = rand.nextInt(20) + 1;
        int salRand = rand.nextInt(201) - 100;
        this.salary = 10*(speed+endurance+10) + salRand;

        if (this.salary < 100){
            this.salary = 100;
        }

    }

    public String ridingStyle(){
        return style.style();
    }

    public void setRidingStyle(RidingStyle newRidingStyle){
        style = newRidingStyle;
    }

    public int getSpeed(){
        return this.speed;
    }   
    
    public int getEndurance(){
        return this.endurance;
    }

    public RiddenHorse ride(Horse horse){
        RiddenHorse racer = new RiddenHorse(this, horse);

        this.style.ride(racer);

        return racer;
    }

    public String getStyle(){
        return this.style.style();
    }
    
}