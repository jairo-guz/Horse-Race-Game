import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

public class Trainer extends Employee 
{
    private int trainingSkill;
    Trainer(String name)
    {   
        this.name = name;

        Random rand = new Random();

        speed = rand.nextInt(20) + 1;
        endurance = rand.nextInt(20) + 1;
        trainingSkill = rand.nextInt(20) + 1;

        int salRand = rand.nextInt(201) - 100;
        this.salary = 10*(speed+endurance+trainingSkill) + salRand;
        if (this.salary < 100){
            this.salary = 100;
        }
    }

    public void train(Horse horse, int week){
        if (!this.healthy){
            return;
        }
        double probTrain = ((double)trainingSkill + (double)horse.getTrain()) / 50;
        double trained = Math.random();

        if (trained <= probTrain){
            double skillToChange = Math.random();
            int totalSkill = this.speed + this.endurance;
            double speedChance = (double)this.speed/totalSkill;

            if (skillToChange <= speedChance){
                horse.updateStat("speed");
            } else {
                horse.updateStat("endurance");
            }
        }
    }

    public int getTrain(){
        return trainingSkill;
    }
}