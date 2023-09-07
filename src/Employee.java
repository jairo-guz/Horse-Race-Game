import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class Employee
{
    protected String name;
    protected int speed;
    protected int endurance;
    protected Stable stable;
    protected boolean healthy;
    public RidingStyle style;
    protected int salary;
    
    public String getName()
    {
        return name;
    }
    
    public void changeHealth()
    {
        healthy = !healthy;
    }


    public void printStats(){
        System.out.println(this.name + ": SPD - " + this.speed + " END - " + this.endurance);
    }

    public int getSpeed(){
        return speed;
    }

    public int getEndurance(){
        return endurance;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getSalary(){
        return salary;
    }

  
}

