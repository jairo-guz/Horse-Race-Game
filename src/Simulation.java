import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    
    public ArrayList<Stable> stables;
    public ArrayList<Race> races;
    public horseFactory horseCreator;
    public empFactory trainerCreator;
    public empFactory jockeyCreator;
    public eventCompiler recorder;
    public int year;
    public int week;

    public ArrayList<Trainer> availableTrainers;
    public ArrayList<Jockey> availableJockeys;
    public ArrayList<Horse> availableHorses;

    public Simulation(){
        System.out.println("Initializing sim");
        horseCreator = new horseFactory();
        trainerCreator = new empFactory();
        jockeyCreator = new empFactory();
        this.year = 1;
        this.week = 1;
        this.stables = new ArrayList<Stable>();
        this.races = new ArrayList<Race>();
        System.out.println("Initializing stables");
        for (int i = 0; i < 9; i++){
            stables.add(new Stable("Placeholder #" + i, horseCreator, trainerCreator, jockeyCreator, false, null));
        }
        //Create observer, have it onyl observe user stable
        this.recorder = new eventCompiler();
        stables.add(new Stable("User", horseCreator, trainerCreator, jockeyCreator, true, this.recorder));
        for(Stable stable: stables){
            if(!stable.isUser()){
                stable.week(1);
            }
        }

        stables.get(9).summary();
        System.out.println("Initializing races");
        for (int j = 2; j <= 52; j+= 2){
            int raceNum = j/2;
            String raceName = "Race #" + raceNum;
            races.add(new Race(raceName));
        }

        this.availableJockeys = new ArrayList<Jockey>();
        this.availableTrainers = new ArrayList<Trainer>();
        this.availableHorses = new ArrayList<Horse>();

        this.getAvailable();
    }

    public void getAvailable(){
        availableJockeys.clear();
        availableTrainers.clear();
        availableHorses.clear();

        for (int i = 1; i <= 3; i++){
            this.availableTrainers.add((Trainer)this.trainerCreator.getEmployee("Trainer"));
            this.availableJockeys.add((Jockey)this.jockeyCreator.getEmployee("Jockey"));
            this.availableHorses.add((Horse)horseCreator.create(null));
        }
    }

    public void runRaces(){
        if (week % 2 == 0){
            this.getCurrentRace().run();
        }
    }

    public void nextWeek(){
        recorder.clear();
        this.getUserStable().week(week);
        week++;
        if (week % 2 == 0){
            int racer = week % 3;
            for (int i = 0; i < stables.size() - 1; i++){
                this.getCurrentRace().addRacer(stables.get(i).getRacer(racer, racer));
            }
        }
        
    }

    public Stable getUserStable(){
        return stables.get(stables.size() - 1);
    }

    public Race getCurrentRace(){
        if (week % 2 == 0){
            int raceNum = week/2 - 1;
            Race race = races.get(raceNum);
            return race;
        }

        return null;
    }

    public Trainer getTrainer(String name){
        Trainer trainer = null;

        for (Trainer currTrainer: this.availableTrainers){
            if (currTrainer.getName().equals(name)){
                trainer = currTrainer;
            }
        }

        return trainer;
    }

    public Jockey getJockey(String name){
        Jockey jockey = null;

        for (Jockey currJockey: this.availableJockeys){
            if (currJockey.getName().equals(name)){
                jockey = currJockey;
            }
        }

        return jockey;
    }

    public Horse getHorse(String name){
        Horse horse = null;

        for (Horse currHorse: this.availableHorses){
            if (currHorse.getName().equals(name)){
                horse = currHorse;
            }
        }

        return horse;
    }

    public boolean ended(){
        return (week == 52)|| (this.getUserStable().getCash() <= 0);
    }

    
}