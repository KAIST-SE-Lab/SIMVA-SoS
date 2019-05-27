package new_simvasos.adaptation;

import new_simvasos.log.Log;

public class main {
    public static void main (String[] args) {
        SmartHomeSimulation smartHomeSimulation = new SmartHomeSimulation("configuration.txt");
        smartHomeSimulation.initSimulation();
        Log log = smartHomeSimulation.runSimulation();
        log.printSnapshot();
    }
}
