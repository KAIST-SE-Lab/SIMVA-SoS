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

        int i = random.nextInt(this.environment.size());
        int j = random.nextInt(this.environment.size());
        this.environment.get(i).set(j,this.environment.get(i).get(j) + 1);


        return super.behave() + "at (" + String.valueOf(i) + "," + String.valueOf(j) + ")";
    }
}
