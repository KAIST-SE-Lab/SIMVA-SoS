package new_simvasos.localization;

import new_simvasos.scenario.PatientOccurrence;

import java.util.ArrayList;
import java.util.Random;

public class RescueRobot extends CS {

    double probability;
    int location_i;
    int location_j;
    int rescued;
    ArrayList<String> knowledge;
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
            this.readMessage();

            if(environment.get(this.location_i).get(this.location_j) > 0) { //rescue
                this.rescued += 1;
                environment.get(this.location_i).set(this.location_j, environment.get(this.location_i).get(this.location_j) - 1);
                //ret = ret + ", Rescued Patient :" + environment.toString();
                ret = ret + " Rescue Patient: " + this.rescued;
            }
            else{   //todo: rescue policy implementation
                float decision = random.nextFloat() * 4;
                if(this.knowledge.size() > 0){
                    // if there is any knowledge, make decision based on the knowledge #todo
                }

                if (decision < 1){
                    this.location_i = (this.location_i + environment.size() - 1) % environment.size();
                }
                else if (decision < 2){
                    this.location_j = (this.location_i + environment.size() + 1) % environment.size();
                }
                else if (decision < 3){
                    this.location_i = (this.location_i + environment.size() + 1) % environment.size();
                }
                else{
                    this.location_j = (this.location_i + environment.size() - 1) % environment.size();
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

    public void readMessage() {
        if (this.message.size() > 0){
            // read message and update knowlegd #todo
            // pop the message #todo
            Message newMessage = this.message.get(this.message.size()-1);

            System.out.println(newMessage.message + newMessage.sender + newMessage.receiver + newMessage.openTime);
        }
    }
}
