package new_simvasos.localization;

public class Message {
    String message;
    int openTime;
    String sender;
    String receiver;
    int sendingTime;

    public Message(String message,int sendingTime, int openTime, String sender, String receiver){
        this.message = message;
        this.openTime = openTime;
        this.sender = sender;
        this.receiver = receiver;
        this.sendingTime = sendingTime;
    }

    public int getOpenTime() {
        return openTime;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
    
    public int getSendingTime() {return this.sendingTime; }
    
    public void printMessage() {
        System.err.println(this.sender + ", " + this.receiver + ", " + this.message + ", " + this.sendingTime + ", " + this.openTime);
    }
}
