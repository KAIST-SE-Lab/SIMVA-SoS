package new_simvasos.localization;

public class Message {
    String message;
    int openTime;
    String sender;
    String receiver;

    public Message(String message, int openTime, String sender, String receiver){
        this.message = message;
        this.openTime = openTime;
        this.sender = sender;
        this.receiver = receiver;
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
}
