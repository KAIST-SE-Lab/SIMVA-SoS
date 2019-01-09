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
    ArrayList<Pair<Integer, Integer>> knowledge;
    ArrayList<Message> message;

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
                environment.get(this.location_i).set(this.location_j, environment.get(this.location_i).get(this.location_j) - 1);
                //ret = ret + ", Rescued Patient :" + environment.toString();
                ret = ret + " Rescue Patient: " + this.rescued;
            }
            else{   //todo: rescue policy implementation
                if(this.knowledge.size() > 0) {
                    ArrayList<Integer> distanceArr = new ArrayList<>();
                    int minDistance = Integer.MAX_VALUE;
                    int minIdx = -1;
                    for (int i = 0; i < this.knowledge.size(); i++) {
                        //System.out.println("knowledge: " + this.knowledge.get(i));
                        // if there is any knowledge, make decision based on the knowledge #todo
                        if(knowledge.get(i).getKey() == location_i && knowledge.get(i).getValue() == location_j){
                            knowledge.remove(i);
                            i--;
                            continue;
                        }
                        int distance = abs(knowledge.get(i).getKey() - location_i) + abs(knowledge.get(i).getValue() - location_j);
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
                        int target_i = knowledge.get(minIdx).getKey();
                        int target_j = knowledge.get(minIdx).getValue();

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
                                System.out.println("ERROR!!");
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
    }

    // get resuced number for simulation log
    @Override
    public int getRescued() { return this.rescued; }

    public void addMessage(Message newMessage) { this.message.add(newMessage); }

    public void readMessage(int tick) {
        for (int i = 0; i < this.message.size(); i++){
            // read message and update knowlegd #todo
            // pop the message #todo
            Message newMessage = this.message.get(i);

            if (newMessage.openTime == tick){
                //System.out.println(newMessage.message + newMessage.sender + newMessage.receiver + newMessage.openTime);
                String messageStr = newMessage.message;
                ArrayList<String> tokens = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(messageStr, ",");
                while(st.hasMoreTokens()) {
                    tokens.add(st.nextToken());
                }

                int knowledge_i = Integer.parseInt(tokens.get(0).replace("(", ""));
                int knowledge_j = Integer.parseInt(tokens.get(1).replace(")", ""));

                knowledge.add(new Pair<>(knowledge_i, knowledge_j));
                message.remove(i);
                i--;

                Set<Pair<Integer,Integer>> set = new HashSet<>(knowledge);
                knowledge.clear();
                knowledge.addAll(set);
            }
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
}
