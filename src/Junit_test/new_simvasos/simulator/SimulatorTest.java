package new_simvasos.simulator;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.not_decided.CS;
import new_simvasos.not_decided.FireFighter;
import new_simvasos.not_decided.SoS;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.PatientOccurrence;
import new_simvasos.scenario.Scenario;
import new_simvasos.timebound.ConstantTimeBound;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static org.junit.Assert.*;

public class SimulatorTest {
    SoS sos;
    Scenario scenario;
    Simulator simulator;
    
    void initModels(){
        System.out.println("Simulation_Firefighters > initModels()");
        double fireFighterPr = 1.0;
        int numFireFighter = 5;
        ArrayList<CS> CSs = new ArrayList();
        
        for (int i = 0; i < numFireFighter; i++) {      // start from zero or one?
            FireFighter fireFighter = new FireFighter(Integer.toString(i), fireFighterPr);
            CSs.add(fireFighter);
        }
        
        int mapSize = 5;
        ArrayList<Integer> MCIMap = new ArrayList<>();
        
        for (int i = 0; i < mapSize; i++) {
            MCIMap.add(0);
        }
        
        this.sos= new SoS(new ArrayList<>(CSs), MCIMap);
    }
    
    void initScenario(){
        System.out.println("Simulation_Firefighters > initScenario()");
        
        ConstantTimeBound constantTimeBound;
        PatientOccurrence patientOccurrence;
        Event event;
        
        ArrayList MCIEvents = new ArrayList();
        int numPatients = 5;
        
        for(int j = 0; j < numPatients; j++) {
            constantTimeBound = new ConstantTimeBound(0);
            patientOccurrence = new PatientOccurrence("patient + 1", this.sos.getEnvironment());
            event = new Event(patientOccurrence, constantTimeBound);
            MCIEvents.add(event);
        }
        this.scenario = new Scenario(MCIEvents);
    }
    
    @Test
    public void run() {
        initModels();
        initScenario();
        simulator = new Simulator(5);
        
        Log log = simulator.run(this.sos, this.scenario);
    
        HashMap<Integer, Snapshot> snapshotMap = log.getSnapshotMap();
        log.printSnapshot();
        for(int i = 0; i < snapshotMap.size(); i++) {
            Snapshot snapshot = snapshotMap.get(i);
            StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
            while(st.hasMoreTokens()) {
                if(st.nextToken().equals("RescuedRate:"))
                    break;
            }
    
            double rescueRate = Double.parseDouble(st.nextToken());
            
            if(i == 0) {
                assertEquals(0, (int)rescueRate);
            } else if (i == snapshotMap.size() - 1) {
                assertEquals(1, (int)rescueRate);
            }
        }
    }
}