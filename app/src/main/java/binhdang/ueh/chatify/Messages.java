package binhdang.ueh.chatify;

public class Messages {
    String message;
    String senderId;
    String time;

    public Messages(String message, String senderId, String time) {
        this.message = message;
        this.senderId = senderId;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTime() {
        return time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
