import java.util.Random;

public interface RidingStyle {
    // Strategy Pattern where each jockey will have their own riding style
    // Each riding style below will determine how they can change a horse's attributes during a race
    
    String style();
    public void ride(RiddenHorse horse);
}

    class AgressiveRiding implements RidingStyle{
        public void ride(RiddenHorse horse){
           horse.addSpeed(7);
           horse.addEnd(-5);
        }

        public String style(){
            return "Agressive Riding";
        }
    }

    class BalancedRiding implements RidingStyle{
        public void ride(RiddenHorse horse){
            horse.addSpeed(1);
            horse.addEnd(1);
         }
        public String style(){
            return "Balanced Riding";
        }
    }

    class ConservativeRiding implements RidingStyle{
        public void ride(RiddenHorse horse){
            horse.addSpeed(-5);
            horse.addEnd(7);
         }
        public String style(){
            return "Conservative Riding";
        }
    }