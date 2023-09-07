//FACTORY PATTERN - there are only 2 base classes of employees, so a simple factory here suffices
import java.util.Random;
public class empFactory {
    public Employee getEmployee(String type){
        Employee newEmp;
        String names [] = {"Joe", "Brady", "Michael", "Austin", "Samuel", "Samantha", "Thomas", "Tabitha", "Toby", "Ingrid", "Isabel", "Bruce", "Carson", "Xavier", "Kayla", "Kylie", "Amy", "Brandi", "Hope", "Janice"};
        Random rand = new Random();
        String name = names[rand.nextInt(20)];

        if (type.equals("Jockey")){
            newEmp = new Jockey(name);
        } else {
            newEmp = new Trainer(name);
            //newEmp = new Trainer(index);
        }

        return newEmp;
    }
}
