package simulator;

import java.util.ArrayList;
import java.util.Collections;

public class SoSManager extends Constituent {

    private int SoSBenefit;
    private ArrayList<Constituent> csList;


    public SoSManager(String name, Constituent[] csList) {
        super(name);
        this.csList = new ArrayList<Constituent>();
        Collections.addAll(this.csList, csList);
        this.SoSBenefit = 0;
    }


    public void ack(){
        // TODO: 2016-08-02 Implement SoS manager acknowledgement
    }
}
