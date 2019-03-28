package new_simvasos.localization;

import new_simvasos.localization.CS;

import java.util.ArrayList;
import java.util.Random;

public class PatrolDrone extends CS {
    int location_i;
    int location_j;
    int rescued;
    ArrayList<RescueRobot> robotConnection;
    ArrayList<Message> message;
    ArrayList<Integer> connections;
    ArrayList<Integer> delays;
    int speed;
    double probability;

    private int target_i;
    private int target_j;

    //int oneToOneConnection;

    /**
     * Instantiates a new CS.
     *
     * @param name the name
     */
    public PatrolDrone(String name, double prob, ArrayList<RescueRobot> connection, ArrayList<Integer> delays) {  //constructor
        super(name);
        this.speed = 1;
        this.location_i = -1;
        this.location_j = -1;
        this.robotConnection = connection;
        this.delays = delays;
        this.probability = prob;
        target_i = -1;
        target_j = -1;
        this.message = new ArrayList<>();
        
        //oneToOneConnection = -1;
    }

    @Override
    public void addConnection(ArrayList CSs) {
        return;
    }
    
    @Override
    public void setConnection(ArrayList connections) {
        this.connections = connections;
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
            
            readMessage(tick);
            
            if(environment.get(this.location_i).get(this.location_j) > 0) {
                if (random.nextFloat() < this.probability) {
                    // send a message
                    System.out.println("Drone Send Message");
                    String contents = "(" + this.location_i + "," + this.location_j + ")," + environment.get(this.location_i).get(this.location_j);
                    
                    int index = connections.get(random.nextInt(this.connections.size()));
                    RescueRobot cs = robotConnection.get(index);

                    int openTime = tick + delays.get(index);
                    Message message = new Message(contents, tick, openTime, name, cs.getName());
                    cs.addMessage(message);
                    //System.err.println("tick :" + String.valueOf(tick));
                    //message.printMessage();
                    
                    /*for (int i = 0; i < robotConnection.size(); i++) {
                        RescueRobot cs = robotConnection.get(i);
                        int openTime = tick + delays.get(i);
                        Message message = new Message(contents, tick, openTime, name, cs.getName());
                        if (oneToOneConnection == -1)
                            cs.addMessage(message);
                        else if (oneToOneConnection == i)
                            cs.addMessage(message);
                        else ;
                    }*/
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

    public void addMessage(Message msg) {
        this.message.add(msg);
    }
    
    private void readMessage(int tick) {
        for (int i = 0; i < this.message.size(); i++) {
            System.out.println("Drone Read Message");
            // read message and update knowlegde
            // pop the message
            Message newMessage = this.message.get(i);
    
            if (newMessage.openTime == tick) {
                newMessage.printMessage(); // TODO print messages between Drones and Robots
                String messageStr = newMessage.message;
                if (messageStr.contains("Another") && robotConnection.size() != 1) {
                    Random random = new Random();
                    String contents = messageStr.replace(" Another Critical Mission Exists", "");
                    RescueRobot cs;
                    int index;
                    
                    while(true) {
                         index = random.nextInt(this.connections.size());
                         cs = robotConnection.get(index);
                         if(cs.getName() != newMessage.getSender()) break;
                    }
                    int openTime = tick + delays.get(index);
                    Message message = new Message(contents, tick, openTime, name, cs.getName());
                    cs.addMessage(message);
                }
                
                message.remove(i);
                i--;
            }
        }
    }
    
    @Override
    public double getCapability(){ return this.probability; }
    
    public ArrayList<Integer> getDelays() { return this.delays; }
}
