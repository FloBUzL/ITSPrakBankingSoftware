package shared.connection;

import java.util.HashMap;

/**
 * the message object, wrapping some data
 * @author Florian
 */
public class Message {
    private HashMap<String,String> messageData;

    /**
     * the default constructor
     */
    public Message() {
	this.messageData = new HashMap<>();
    }

    /**
     * creates a message object from a string
     * @param line the string to convert
     * @return
     */
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

    /**
     * adds data to the object
     * @param key the data key
     * @param value the data value
     * @return the message object
     */
    public Message addData(String key, String value) {
	this.messageData.put(key, value);

	return this;
    }

    /**
     * checks if the object contains specific data field
     * @param key the data key
     * @return true if the object contains the data field
     */
    public boolean containsData(String key) {
	return this.messageData.containsKey(key);
    }

    /**
     * retrieves a data field
     * @param key the data key
     * @return the data value
     */
    public String getData(String key) {
	return this.messageData.get(key);
    }

    /**
     * creates a string from the object's data
     */
    public String toString() {
	StringBuilder sb = new StringBuilder();

	this.messageData.forEach((key,value) -> {
	    sb.append(key + "=" + value + ",");
	});

	return sb.toString();
    }
}
