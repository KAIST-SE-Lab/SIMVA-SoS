package new_simvasos.localization;

import new_simvasos.localization.CS;

import java.util.ArrayList;
import java.util.Random;

public class PatrolDrone extends CS {
    int location_i;
    int location_j;
    int rescued;
    ArrayList<RescueRobot> messageConnection;
    ArrayList<Integer> delays;
    int speed;
    double probability;

    private int target_i;
    private int target_j;

    int oneToOneConnection;

    /**
     * Instantiates a new CS.
     *
     * @param name the name
     */
    public PatrolDrone(String name, double prob, ArrayList<RescueRobot> connection, ArrayList<Integer> delays) {  //constructor
        super(name);
        this.speed = 5;
        this.location_i = -1;
        this.location_j = -1;
        this.messageConnection = connection;
        this.delays = delays;
        this.probability = prob;
        target_i = -1;
        target_j = -1;

        oneToOneConnection = -1;
    }

    @Override
    public String act(int tick, ArrayList<ArrayList<Integer>> environment) {
        String ret = "CS : ";
        Random random = new Random();

        if (this.location_i == -1 || this.location_j == -1) {
            this.location_i = random.nextInt(environment.size());
            this.location_j = random.nextInt(environment.size());
        }
        ret = ret + this.name + " at Location: (" + this.location_i + "," + this.location_j + ")";

        for(int s = 0; s < speed; s++){
            //System.out.println(this.location_i + ", " + this.location_j);
            if(environment.get(this.location_i).get(this.location_j) > 0) { //sending message
                if (random.nextFloat() < this.probability) {
                    // send a message
                    String contents = "(" + this.location_i + "," + this.location_j + ")";
                    for (int i = 0; i < messageConnection.size(); i++) {
                        RescueRobot cs = messageConnection.get(i);
                        int openTime = tick + delays.get(i);
                        Message message = new Message(contents, openTime, name, cs.getName());
                        if (oneToOneConnection == -1)
                            cs.addMessage(message);
                        else if (oneToOneConnection == i) //todo: one-to-one connection 임시 구현: Yong-Jun Shin: a drone can send a message to only a robot.
                            cs.addMessage(message);
                        else ;
                    }
                }

                //randomMovement(environment.size());
                targetMovement(environment.size());
            }
            else{   //todo: patrol policy implementation
                //randomMovement(environment.size());
                targetMovement(environment.size());
            }
        }
        return ret;
    }

    @Override
    public void setSomething(int something){
        setOneToOneConnection(something);
    }

    public void setOneToOneConnection(int idx){
        oneToOneConnection = idx;
        System.out.println(oneToOneConnection);
    }

    public void reset() {
        this.location_i = -1;
        this.location_j = -1;

        this.target_j = -1;
        this.target_i = -1;
    }

    private void randomMovement(int envSize){
        Random random = new Random();
        float decision = random.nextFloat() * 4;

        if (decision < 1) {
            this.location_i = (this.location_i + envSize - 1) % envSize;
        } else if (decision < 2) {
            this.location_j = (this.location_j + envSize + 1) % envSize;
        } else if (decision < 3) {
            this.location_i = (this.location_i + envSize + 1) % envSize;
        } else {
            this.location_j = (this.location_j + envSize - 1) % envSize;
        }
    }

    private void targetMovement(int envSize){
        while(target_i == -1 || target_j == -1 || (target_i == this.location_i && target_j == this.location_j)){
            Random random = new Random();
            target_i = random.nextInt(envSize);
            target_j = random.nextInt(envSize);
        }

        if (target_i > location_i) {
            this.location_i = (this.location_i + envSize + 1) % envSize;
        } else if (target_i < location_i) {
            this.location_i = (this.location_i + envSize - 1) % envSize;
        } else {
            if (target_j > location_j) {
                this.location_j = (this.location_j + envSize + 1) % envSize;
            } else if (target_j < location_j) {
                this.location_j = (this.location_j + envSize - 1) % envSize;
            } else {
                System.out.println("DRONE MOVEMENT ERROR!!");
            }
        }
    }

    @Override
    public double getCapability(){ return speed; }
}
