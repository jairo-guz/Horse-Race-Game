//FACTORY PATTERN - This is a simple factory used to create horse objects
import java.util.Random;
public class horseFactory {
    public Horse create(Stable stable){
        String names [] = {"Joe", "Brady", "Michael", "Austin", "Samuel", "Samantha", "Thomas", "Tabitha", "Toby", "Ingrid", "Isabel", "Bruce", "Carson", "Xavier", "Kayla", "Kylie", "Amy", "Brandi", "Hope", "Janice"};
        Random rand = new Random();
        String name = names[rand.nextInt(20)];
        Horse newHorse = new Horse(name, stable);
        return newHorse;
    }
}