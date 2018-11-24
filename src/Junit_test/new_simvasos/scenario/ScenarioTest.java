package new_simvasos.scenario;

import new_simvasos.not_decided.CS;
import new_simvasos.not_decided.FireFighter;
import new_simvasos.not_decided.SoS;
import new_simvasos.timebound.ConstantTimeBound;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScenarioTest {
    
    @Test
    public void scenario() {
    
        double fireFighterPr = 0.8;
        int numFireFighter = 2;
        ArrayList<CS> CSs = new ArrayList();
    
        for (int i = 0; i < numFireFighter; i++) {      // start from zero or one?
            FireFighter fireFighter = new FireFighter(Integer.toString(i), fireFighterPr);
            CSs.add(fireFighter);
        }
    
        //int mapSize = 300;
        // for Mutation Testing
        int mapSize = 10;
        ArrayList<Integer> MCIMap = new ArrayList<>();
    
        for (int i = 0; i < mapSize; i++) {
            MCIMap.add(0);
        }
    
        //targetSoS = new SoS(CSs, MCIMap);
        SoS sos = new SoS(new ArrayList<>(CSs), MCIMap);
        
        ConstantTimeBound constantTimeBound;
        PatientOccurrence patientOccurrence;
        Event event;
    
        ArrayList MCIEvents = new ArrayList();
        //int numPatients = 100;
        // for Mutation Testing
        int numPatients = 5;
    
        for(int j = 0; j < numPatients; j++) {
            constantTimeBound = new ConstantTimeBound(0);
            patientOccurrence = new PatientOccurrence("patient + 1", sos.getEnvironment());
            event = new Event(patientOccurrence, constantTimeBound);
            MCIEvents.add(event);
        }
        Scenario scenario = new Scenario(MCIEvents);
    
        scenario.reset();
    }
}