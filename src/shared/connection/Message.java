package shared.connection;

import java.util.HashMap;

public class Message {
    private HashMap<String,String> messageData;

    public Message() {
	this.messageData = new HashMap<>();
    }

    public static Message fromString(String line) {
	Message newMessage = new Message();

	String[] dataPairs = line.split(",");
	for(String pair : dataPairs) {
	    String[] kv = pair.split("=", 2);
	    if(kv.length == 2) {
		newMessage.addData(kv[0], kv[1]);
	    }
	}

	return newMessage;
    }

    public Message addData(String key, String value) {
	this.messageData.put(key, value);

	return this;
    }

    public boolean containsData(String key) {
	return this.messageData.containsKey(key);
    }

    public String getData(String key) {
	return this.messageData.get(key);
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();

	this.messageData.forEach((key,value) -> {
	    sb.append(key + "=" + value + ",");
	});

	return sb.toString();
    }
}
