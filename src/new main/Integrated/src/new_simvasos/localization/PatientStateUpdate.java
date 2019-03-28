package new_simvasos.localization;

import new_simvasos.scenario.Action;

import java.util.ArrayList;

public class PatientStateUpdate extends Action{
    ArrayList<ArrayList<Integer>> environment;
    
    public PatientStateUpdate(String name, ArrayList environment) {
        super(name);
        this.environment = environment;
    }
    
    public String behave() {
        String ret = "";
        int value;
        for(int i = 0 ; i < environment.size(); i++) {
            for(int j = 0; j < environment.get(i).size(); j++) {
                value = environment.get(i).get(j);
                if (value == 1) {
                    environment.get(i).set(j,0);
                     ret += "Patient at (" + String.valueOf(i) + "," + String.valueOf(j) + ") is dead.";
                } else if (value != 0) {
                    environment.get(i).set(j, value-1);
                }
            }
        }
        
        for(int i = 0; i < environment.size(); i++) {
            for(int j = 0; j < environment.get(i).size(); j++) {
                System.out.print(environment.get(i).get(j));
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        return super.behave() + ret;
    }
}
