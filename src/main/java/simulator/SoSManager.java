package simulator;

import java.util.ArrayList;

public class SoSManager extends Constituent {

    private int SoSBenefit;
    private ArrayList<Constituent> csList;


    public SoSManager(String name, ArrayList<Constituent> csList) {
        super(name);
        this.csList = csList;
        this.SoSBenefit = 0;
    }


    public void ack(){
        // TODO: 2016-08-02 Implement SoS manager acknowledgement
    }
}
