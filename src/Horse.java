import java.util.Random;

public class Horse
{
    protected int age;
    protected boolean healthy;
    protected String name;
    protected String imgFile;
    protected int recoverTime;
    protected int trainable;
    protected Trainer trainer;
    protected Stable stable;

    protected int speed;
    protected int endurance;

    protected int value;


    protected String [] names = {"Bella", "Luna","Lucy","Daisy","Lola","Sadie","Molly","Bailey","Stella","Maggie","Chloe","Penny","Nala","Zoey","Lily","Coco","Sophie", "Rosie", "Ellie", "Ruby",
                                "Max","Charlie","Milo","Buddy","Rocky","Bear","Leo","Duke","Teddy","Tucker","Beau","Oliver","Jack","Winston","Ollie","Toby","Jax","Blue","Finn","Louie"};
    public void changeHealth()
    {
        healthy = !healthy;
    }
    
    public Horse(){
        name = "";
    }
    
    public Horse(String name, Stable stable){
        //Generate name
        Random rand = new Random();
        //int namePick = rand.nextInt(40);
        //name = names[namePick];
        this.name = name;
        healthy = true;
        this.trainable = 0;
        this.stable = stable;

        //Random generation
        this.age = rand.nextInt(3) + 3; //Generates age from 3 to 5
        this.speed = rand.nextInt(20) + 1;
        this.endurance = rand.nextInt(20) + 1;
        this.trainable = rand.nextInt(20) + 1;

        this.trainer = null;

        int valRand = rand.nextInt(201) - 100;
        this.value = 20*(speed + endurance + trainable) + valRand;
        if (this.value < 100){
            this.value = 100;
        }

    }

    public String getName()
    {
        return name;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getEndurance()
    {
        return endurance;
    }

    public int getRecoveryTime()
    {
        return recoverTime;
    }

    public boolean getHealth(){
        return healthy;
    }

    public void updateStat(String stat){
        switch (stat){
            case "speed":
                if (this.speed < 20){
                    this.speed++;
                    System.out.println(this.name + "'s speed is now " + this.speed);
                }
                break;
            case "endurance":
                if (this.endurance < 20){
                    this.endurance++;
                    System.out.println(this.name + "'s endurance is now " + this.endurance);
                }
                break;
            default:
                return;
        }
    }

    public int getTrain(){
        return trainable;
    }

    public void printStats(){
        System.out.println(this.name + ": SPD - " + this.speed + " END - " + this.endurance + " TRN - " + this.trainable);
    }

    public void assignTrainer(Trainer trainer){
        this.trainer = trainer;
    }

    public String getTrainerName(){
        if (this.trainer != null){
            return this.trainer.getName();
        }

        return "No assigned trainer";
    }

    public void setName(String name){
        this.name = name;
    }

    public void train(int week){
        this.trainer.train(this, week);
    }

    public void removeTrainer(){
        this.trainer = null;
    }

    public Stable getStable(){
        return this.stable;
    }

    public void assignStable(Stable stable){
        this.stable = stable;
    }

    public int getValue(){
        return this.value;
    }
}
