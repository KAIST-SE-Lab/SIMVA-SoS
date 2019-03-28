package new_simvasos.localization;

import new_simvasos.scenario.Action;

import java.util.ArrayList;
import java.util.Random;

public class PatientOccurrence2D extends Action{
    ArrayList<ArrayList<Integer>> environment;

    /**
     * Instantiates a new Action.
     *
     * @param name the name
     */
    public PatientOccurrence2D(String name, ArrayList environment) {
        super(name);
        this.environment = environment;
    }

    public String behave(){
        Random random = new Random();
        int i, j;
        
        // No patient occurrence on same place
        while(true) {
            i = random.nextInt(this.environment.size());
            j = random.nextInt(this.environment.size());
            if(this.environment.get(i).get(j) == 0) break;
        }
        this.environment.get(i).set(j, random.nextInt(131));
        
        return super.behave() + "at (" + String.valueOf(i) + "," + String.valueOf(j) + ")";
    }
}
