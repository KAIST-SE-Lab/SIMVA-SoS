package new_simvasos.simulation;

import new_simvasos.log.Log;
import new_simvasos.simulator.Simulator;
import new_simvasos.not_decided.SoS;
import new_simvasos.scenario.Scenario;

/**
 * @author ymbaek
 *
 * Simulation is an (abstract) execution unit for executing simulation objects and scenario.
 * Simulation objects include SoS models such as Org, CS, Infra, Environment.
 * A Scenario represents sequence of events
 */
public abstract class Simulation {
    Simulator simulator;        //Simulator for executing targetSoS & targetScenario
    SoS targetSoS;              //Simulation Object 1: Target SoS
    Scenario targetScenario;    //Simulation Object 2: Target Scenario (Event Sequence)

    public Simulation() {
        System.out.println("Simulation > Constructor 1");
        initSimulation(15);
    }

    public Simulation(int simulationTime) {
        System.out.println("Simulation > Constructor 2");
        initSimulation(simulationTime);
    }

    /**
     * A method for initializing Simulation
     *
     * @param simulationTime    limited time (num of ticks) for a single simulation execution
     */
    public void initSimulation(int simulationTime) {
        initModels();
        initScenario();
        initSimulator(simulationTime, targetSoS, targetScenario);
    }

    /**
     * A method for running a simulation
     * @return results (accumulated text logs) of a single simulation execution
     */
    public Log runSimulation() {
        System.out.println("Simulation > runSimulation()");

        //TODO: write the Log
        //TODO: modify Verifier code to read the Log
        return simulator.run();
    }

    //TODO: Find usage
    public void setSimulationTime(int newSimulationTime) {
        simulator = new Simulator(newSimulationTime, targetSoS, targetScenario);
    }

    abstract void initModels();

    abstract void initScenario();

    private void initSimulator(int simulationTime, SoS targetSoS, Scenario targetScenario) {
        simulator = new Simulator(simulationTime, targetSoS, targetScenario);
    }


}


/**
Class Simulation{   //기본적으로는 주어진 모델과 시나리오에 맞춰 1회(정의 공유 필요) 시뮬레이션, verifier에 의한 반복적인 시뮬레이션이 가능하도록 runSimulation 함수도 제공
    private SoS model
    private scenario scene
    public simulator sim

    public Simulation(){ //실제론 의미 없음
        initializeModel();
        initializeScenario();
        int time = INFINITY;
        sim = new Simulator(time, model, scene);
    }
    public Simulation(int t){
        initializeModel();
        initializeScenario();
        int time = t;
        sim = new Simulator(time, model, scene);
    }

    private initializeModel(){ //GUI에서 입력을 받아 이 부분이 동적으로 생성되어야 하지만 우선 predefine
        SoS sos1;
        FireFighter f1;
        FireFighter f2;
        organization o1;
        o1.addCS(f1);
        o1.addCS(f2);
    }
    private initializeScenario(){ //GUI에서 입력을 받아 이 부분이 동적으로 생성되어야 하지만 우선 predefine

    }

    public static Log main(simulationConfiguration conf){ //consol에서 입력받을 수 있는 무언가가 파라미터
        //configuration 수정 부분?
        sim.setTime(conf);
        return runSimulation();
    }
    // slicing module execution code
// public void run() {
//     runProcess ("javac "+"simulationExample.java");
//     runProcess ("java "+"simulationExample.class conf");
// }
public static Log runSimulation(){ //verifier가 반복 호출
    if sim != null{
        return sim.run();
    }
        else{
        return null;
    }

}

}

 **/