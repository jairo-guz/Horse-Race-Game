import java.util.ArrayList;
import java.util.*;
import java.util.Random;

public class Race 
{
    private String name;
    private ArrayList<RiddenHorse> racers;
    private ArrayList<RiddenHorse> finished;
    private int length; 
    private int prizePool;

    public Race(String name){
        this.name = name;
        this.racers = new ArrayList<RiddenHorse>();
        this.finished = new ArrayList<RiddenHorse>();
        Random rand = new Random();
        this.length = (10*rand.nextInt(10)) + 200;
        this.prizePool = 10000;
    }

    public void addRacer(RiddenHorse horse){
        racers.add(horse);
    }

    public String getName(){
        return name;
    }

    public int getSize(){
        return racers.size();
    }

    public ArrayList <RiddenHorse> getRacers(){
        return racers;
    }

    public void run(){
        ArrayList<RiddenHorse> finishers = new ArrayList<RiddenHorse>();
        ArrayList<RiddenHorse> dnfers = new ArrayList<RiddenHorse>();
        Random rand = new Random();

        int numRacers = this.racers.size();

        while (this.racers.size() > 0){
            //Have horses run for a little
            for (RiddenHorse racer: this.racers){
                int dist = racer.getSpeed() + rand.nextInt(21) - 10; //Distance traveled per segemnt is spedd plus or minus 10
                if (dist < 0){
                    dist = 0;
                }
                racer.runDist(dist);
            }

            Collections.sort(this.racers, RiddenHorse.compareTo);
            for (int i = 1; i <= racers.size(); i++){
                RiddenHorse racer = racers.get (i - 1);
                int distToGo = this.length - racer.getDistRun();
                if (distToGo <= 0){
                    finishers.add(racer);
                }

                if (racer.getSpeed() == 0){
                    dnfers.add(racer);
                }
            }

            for (RiddenHorse racer: finishers){
                this.racers.remove(racer);
            }
            for (RiddenHorse racer: dnfers){
                this.racers.remove(racer);
            }

            finished.addAll(finishers);

            finishers.clear();
            
        }

        Collections.sort(dnfers, RiddenHorse.compareTo);
        finished.addAll(dnfers);

        this.awardPrizes();
    }

    public RiddenHorse getFinisher(int place){
        return finished.get(place - 1);
    }

    public int getPrize(int place){
        switch (place){
            case 1:
                return prizePool/2;
            case 2:
                return prizePool/5;
            case 3:
                return 3*prizePool/20;
            case 4: 
                return prizePool/10;
            case 5:
                return prizePool/20;
            default:
                return 0;
        }
    }

    public void awardPrizes(){
        for (int i = 0; i < finished.size(); i++){
            finished.get(i).win(this.getPrize(i + 1));
        }
    }

    public int getLength(){
        return length;
    }
}