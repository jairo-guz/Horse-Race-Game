import java.util.ArrayList;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Random;

public class Stable
{
    protected String name;

    protected ArrayList<Horse> allHorses; //every horse, used to display data
    protected ArrayList<Horse> horses; //only healthy horses

    public ArrayList <Horse> injuredHorses;

    protected ArrayList<Trainer> trainers;
    protected ArrayList<Jockey> allJockeys;
    protected ArrayList<Jockey> jockeys;
    protected ArrayList<Jockey> sickjockeys;
    protected int cash;
    protected int salaries;
    protected int rent;

    protected int points;
    protected boolean user;
    //OBSERVER PATTERN - user's stable acts as subject for all observers, so we implement the ability to add observers here
    protected PropertyChangeSupport observable;

    public void addObserver(PropertyChangeListener observer)
    {
        observable.addPropertyChangeListener(observer);
    }

    public void removeObserver(PropertyChangeListener observer)
    {
        observable.removePropertyChangeListener(observer);
    }

    public Stable(String name, horseFactory horseMaker, empFactory trainerMaker, empFactory jockeyMaker, boolean user, eventCompiler recorder){
        this.name = name;
        this.horses = new ArrayList<Horse>();
        this.allHorses = new ArrayList<Horse>();
        this.injuredHorses = new ArrayList<Horse>();
        this.jockeys = new ArrayList<Jockey>();
        this.allJockeys = new ArrayList<Jockey>();
        this.trainers = new ArrayList<Trainer>();
        this.sickjockeys = new ArrayList<Jockey>();

        


        for (int i = 0; i < 3; i++){
            horses.add(horseMaker.create(this));
            trainers.add((Trainer)trainerMaker.getEmployee("Trainer"));
            jockeys.add((Jockey)jockeyMaker.getEmployee("Jockey"));
            //Assign trainers for non-user stables
            if (!user){
                horses.get(i).assignTrainer(trainers.get(i));
            }
        }

        this.cash = 25000;
        this.rent = 500;
        this.salaries = 0;

        for (Trainer trainer: trainers){
            salaries += trainer.getSalary();
        }

        for (Jockey jockey: jockeys){
            salaries += jockey.getSalary();
        }

        this.allHorses.addAll(horses);
        this.allJockeys.addAll(jockeys);

        this.user = user;

        observable = new PropertyChangeSupport(this);       
        
        this.addObserver(recorder);
    }

    public String getName()
    {
        return name;
    }

    public boolean isUser(){
        return user;
    }


    public void fire(Employee emp){
        salaries -= emp.getSalary();
        for(int i = 0; i < jockeys.size(); i++){
            if(emp == jockeys.get(i)){
                jockeys.remove(emp);
                allJockeys.remove(emp);
            }
        }

        for(int i = 0; i < trainers.size(); i++){
            if(emp == trainers.get(i)){
                for (Horse horse: horses){
                    if (emp.getName().equals(horse.getTrainerName())){
                        horse.removeTrainer();
                    }
                }
                trainers.remove(emp);
            }
        }
    }

    public void hire(Employee emp){
        if (emp.getClass().getSimpleName().equals("Trainer")){
            trainers.add((Trainer)emp);
        } else {
            jockeys.add((Jockey)emp);
            allJockeys.add((Jockey)emp);
        }

        salaries += emp.getSalary();
    }

    public void sell(Horse horse){
        
        for(int i = 0; i < injuredHorses.size(); i++){
            
            if(horse == injuredHorses.get(i)){
                injuredHorses.remove(injuredHorses.get(i));
            }
        }

        for(int i = 0; i < horses.size(); i++){
            
            if(horse == horses.get(i)){
                horses.remove(horses.get(i));
            }
        }

    }

    public void buy(Horse horse){

        horses.add(horse);
        allHorses.add(horse);
        horse.assignStable(this);
    }

    //Controls weekly activities for stable - training, deducting expenses from cash, sickness and injuries
    public void week(int week){

        this.cash -= rent;
        this.cash -= salaries;
        ArrayList<Horse> healed = new ArrayList<Horse>();
        //See if injure horses have healed
        for(int i = 0; i < injuredHorses.size(); i++){
            
            if(week == injuredHorses.get(i).recoverTime){
                observable.firePropertyChange("notInjured", null, injuredHorses.get(i));
                injuredHorses.get(i).changeHealth();
                healed.add(injuredHorses.get(i));
            }
        }

        for (Horse horse: healed){
            horses.add(horse);
            injuredHorses.remove(horse);
        }

        for (int i = 0; i < horses.size(); i++){
            Horse horse = horses.get(i);
            Trainer trainer = this.getTrainer(horse.getTrainerName());
            if (trainer != null){
                trainer.train(horse, week);
            }
        }

        if (!user){
            return;
        }

        ArrayList<Horse> nowInjured = new ArrayList<Horse>();
        
        for (Horse horse: horses){
            double injuryProb = Math.random();
            if(injuryProb <= 0.1){
                horse.changeHealth();
                nowInjured.add(horse);
                double recovery = Math.random();
                if(recovery < 0.50){
                    horse.recoverTime = 3 + week;
                }
                else{
                    horse.recoverTime = 2 + week;
                }
                observable.firePropertyChange("injured", null, horse);
                
            }
        }

        for(Horse horse: nowInjured){
            this.injuredHorses.add(horse);
            this.horses.remove(horse);
        }

        
        double sicko = Math.random();
        ArrayList<Jockey> healedEmps = new ArrayList<Jockey>();
        ArrayList<Jockey> sickEmps = new ArrayList<Jockey>();
        for (int i = 0; i < sickjockeys.size(); i++){
            Jockey jockey = sickjockeys.get(i);
        
            jockey.changeHealth();
            healedEmps.add(jockey);
            
                observable.firePropertyChange("notSick", null, jockey);
            
        }

        for (Jockey jockey: healedEmps){
            sickjockeys.remove(jockey);
            jockeys.add(jockey);
        }

        
            if (sicko < .20)
            {
               Random rand = new Random();
               int index = rand.nextInt(jockeys.size()); 
               Jockey jockey = jockeys.get(index);
                jockey.changeHealth();
                sickjockeys.add(jockey);
                jockeys.remove(jockey);
                if (this.user){
                    observable.firePropertyChange("sick", null, jockey);
                }
                
            }

            double cashEvent = Math.random();

            if (cashEvent <= 0.1){
                this.cash += 1000;
                observable.firePropertyChange("cash", 0, 1000);
            }

            for (Horse horse: horses){
                System.out.println(horse.getName());
            }
    }

    public RiddenHorse getRacer(int jockIndex, int horseIndex){
        Jockey jockey = jockeys.get(jockIndex);
        Horse horse = horses.get(horseIndex);

        return jockey.ride(horse);
    }

    public void summary(){
        System.out.println(this.name + " Summary");
        System.out.println("Horses:");
        for (Horse horse: horses){
            horse.printStats();
        }
        System.out.println("Trainers:");
        for (Trainer trainer: trainers){
            trainer.printStats();
        }
        System.out.println("Jockeys:");
        for (Jockey jockey: jockeys){
            jockey.printStats();
        }

    }

    public Horse getHorse(String name){
        Horse horse = null;

        for (Horse currHorse: this.horses){
            if (currHorse.getName().equals(name)){
                horse = currHorse;
            }
        }

        return horse;
    }

    public Trainer getTrainer(String name){
        Trainer trainer = null;

        for (Trainer currTrainer: this.trainers){
            if (currTrainer.getName().equals(name)){
                trainer = currTrainer;
            }
        }

        return trainer;
    }

    public Jockey getJockey(String name){
        Jockey jockey = null;

        for (Jockey currJockey: this.jockeys){
            if (currJockey.getName().equals(name)){
                jockey = currJockey;
            }
        }

        return jockey;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addCash(int added){
        this.cash += added;
    }

    public int getCash(){
        return this.cash;
    }

    public int getRent(){
        return this.rent;
    }
    public int getSalaries(){
        return this.salaries;
    }

    public int getExpenses(){
        return this.salaries+this.rent;
    }
}