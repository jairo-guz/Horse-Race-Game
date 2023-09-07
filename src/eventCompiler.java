import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
//OBSERVER PATTERN - this is the observer for events sent by the Stable. This records each week's events into a list that we can parse through to display to the UI
public class eventCompiler implements PropertyChangeListener{
    private ArrayList<String> events;
    public eventCompiler(){
        events = new ArrayList<String>();
    }
    public void propertyChange(PropertyChangeEvent e){
        String event  = e.getPropertyName();
        Horse horse = null;
        Employee employee = null;
        switch (event){
            case "injured":
                horse = (Horse)e.getNewValue();
                events.add(horse.getName() + " is injured! They will be out until week " + horse.getRecoveryTime());
                break;
            case "notInjured":
                horse = (Horse)e.getNewValue();
                events.add(horse.getName() + " is healed! They can race and train again.");
                break;
            case "sick":
                employee = (Employee)e.getNewValue();
                events.add(employee.getName() + " is sick this week.");
                break;
            case "notSick":
                employee = (Employee)e.getNewValue();
                events.add(employee.getName() + " is better this week.");
                break;
            case "cash":
                events.add("Your stable received a gift of $1000");
        }
    }

    public void clear(){
        events.clear();
    }

    public String getEvent (int index){
        return events.get(index);
    }

    public int getSize(){
        return events.size();
    }
}
