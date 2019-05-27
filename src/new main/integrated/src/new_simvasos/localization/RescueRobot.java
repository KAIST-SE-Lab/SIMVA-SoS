package new_simvasos.localization;

import javafx.util.Pair;
import new_simvasos.scenario.PatientOccurrence;

import java.util.*;

import static java.lang.Math.abs;


public class RescueRobot extends CS {

    double probability;
    int location_i;
    int location_j;
    int rescued;
    //Pair<Pair<Integer, Integer>, Integer> knowledge; // (i,j), lifetime
    ArrayList<Pair<Pair<Integer, Integer>, Integer>> knowledge;
    ArrayList<Message> message;
    ArrayList<PatrolDrone> drones;
    ArrayList<Integer> connections;
    
    /**
     * Instantiates a new CS.
     *
     * @param name the name
     */
    public RescueRobot(String name, double prob) {  //constructor

        super(name);
        this.probability = prob;
        this.location_i = -1;
        this.location_j = -1;
        this.rescued = 0;
        this.knowledge = new ArrayList<>();
        this.message = new ArrayList<>();
    }

    @Override
    public void addConnection(ArrayList CSs) {
        this.drones = CSs;
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

        if (random.nextFloat() < this.probability) {
            // knowledge update if there is unread messsage
            this.readMessage(tick);
            
            if(environment.get(this.location_i).get(this.location_j) > 0) { //rescue
                this.rescued += 1;
                //environment.get(this.location_i).set(this.location_j, environment.get(this.location_i).get(this.location_j) - 1);
                environment.get(this.location_i).set(this.location_j, 0);
                //ret = ret + ", Rescued Patient :" + environment.toString();
                ret = ret + " Rescue Patient: " + this.rescued;
                System.out.println("Rescued!");
            }
            else{
                //todo: rescue policy implementation
                if(this.knowledge.size() > 0) {
                    ArrayList<Integer> distanceArr = new ArrayList<>();
                    int minDistance = Integer.MAX_VALUE;
                    int minIdx = -1;
                    for (int i = 0; i < this.knowledge.size(); i++) {
                        //System.out.println("knowledge: " + this.knowledge.get(i));
                        // if there is any knowledge, make decision based on the knowledge #todo
                        if(knowledge.get(i).getKey().getKey() == location_i && knowledge.get(i).getKey().getValue() == location_j){
                            knowledge.remove(i);
                            i--;
                            continue;
                        }
                        int distance = abs(knowledge.get(i).getKey().getKey() - location_i) + abs(knowledge.get(i).getKey().getValue() - location_j);
                        distanceArr.add(distance);

                        if(distance < minDistance){
                            minDistance = distance;
                            minIdx = i;
                        }
                    }

                    if(minIdx == -1){
                        randomMovement(environment.size());
                    }
                    else {
                        int target_i = knowledge.get(minIdx).getKey().getKey();
                        int target_j = knowledge.get(minIdx).getKey().getValue();

                        if (target_i > location_i) {
                            this.location_i = (this.location_i + environment.size() + 1) % environment.size();
                        } else if (target_i < location_i) {
                            this.location_i = (this.location_i + environment.size() - 1) % environment.size();
                        } else {
                            if (target_j > location_j) {
                                this.location_j = (this.location_j + environment.size() + 1) % environment.size();
                            } else if (target_j < location_j) {
                                this.location_j = (this.location_j + environment.size() - 1) % environment.size();
                            } else {
                                System.out.println("ROBOT MOVEMENT ERROR!!");
                            }
                        }
                    }
                }
                else {  // random movement
                    randomMovement(environment.size());
                }
            }
        }
        return ret;
    }

    public void reset() {
        this.location_i = -1;
        this.location_j = -1;
        this.rescued = 0;
        this.knowledge.clear();
        this.message.clear();
    }

    // get resuced number for simulation log
    @Override
    public int getRescued() { return this.rescued; }

    public void addMessage(Message newMessage) { this.message.add(newMessage); }

    public void readMessage(int tick) {
        for (int i = 0; i < this.message.size(); i++){
            System.out.println("Robot Read Message");
            // read message and update knowlegde
            // pop the message
            Message newMessage = this.message.get(i);

            if (newMessage.openTime == tick){
                //System.out.println(newMessage.message + newMessage.sender + newMessage.receiver + newMessage.openTime);
                newMessage.printMessage(); // TODO print messages between Drones and Robots
                String messageStr = newMessage.message;
                ArrayList<String> tokens = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(messageStr, ",");
                while(st.hasMoreTokens()) {
                    tokens.add(st.nextToken());
                }

                int knowledge_i = Integer.parseInt(tokens.get(0).replace("(", ""));
                int knowledge_j = Integer.parseInt(tokens.get(1).replace(")", ""));
                int knowledge_lt = Integer.parseInt(tokens.get(2));
                
                //System.out.println(knowledge_lt);
                if(knowledge.size() == 0) {
                    knowledge.add(new Pair<>(new Pair<>(knowledge_i, knowledge_j), knowledge_lt));
                    message.remove(i);
                    i--;
                    
                    sendMessage(newMessage, tick, 2);
                } else {
                    int crnt_dist = abs(knowledge.get(0).getKey().getKey() - location_i) + abs(knowledge.get(0).getKey().getValue() - location_j);
                    int crnt_lifetime = knowledge.get(0).getValue();
                    int new_dist =  abs(knowledge_i - location_i) + abs(knowledge_j - location_j);
                    int new_lifetime = knowledge_lt;
                    
                    if(new_lifetime > new_dist) {
                        sendMessage(newMessage, tick, 0);
                    } else if (crnt_dist + crnt_lifetime < new_dist + new_lifetime) {
                        sendMessage(newMessage, tick, 1);
                    } else {
                        knowledge.clear();
                        knowledge.add(new Pair<>(new Pair<>(knowledge_i, knowledge_j), knowledge_lt));
                        message.remove(i);
                        i--;
        
                        sendMessage(newMessage, tick, 2);
                        // Removing same patient positions message
                        /*Set<Pair<Pair<Integer, Integer>, Integer>> set = new HashSet<>(knowledge);
                        knowledge.clear();
                        knowledge.addAll(set);*/
                    }
                }
            }
        }
    }

    private void sendMessage(Message rcvdMessage, int tick, int flag) {
        System.out.println("Robot Send Message");
        String senderId = rcvdMessage.getSender().replace("drone","");
        CS senderDrone = this.drones.get(Integer.parseInt(senderId));
        
        if(flag == 0) {
            Message newMessage = new Message("Impossible Mission", tick, tick + (rcvdMessage.openTime - rcvdMessage.sendingTime), name, senderDrone.getName());
            senderDrone.addMessage(newMessage);
        } else if (flag == 1){
            Message newMessage = new Message(rcvdMessage.message + " Another Critical Mission Exists", tick, tick + (rcvdMessage.openTime - rcvdMessage.sendingTime), name, senderDrone.getName());
            senderDrone.addMessage(newMessage);
        } else {
            Message newMessage = new Message("Get That Mission", tick, tick + (rcvdMessage.openTime - rcvdMessage.sendingTime), name, senderDrone.getName());
            senderDrone.addMessage(newMessage);
        }
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

    @Override
    public double getCapability(){ return probability; }
}
