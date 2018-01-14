package server.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import shared.constants.Misc;

public class Database {
    private HashMap<String,UserData> users;
    private String databaseFile;

    public Database(String databaseFile) {
        this.databaseFile = databaseFile;
        this.users = new HashMap<>();

        try(InputStream jsonFileStream = new FileInputStream(this.databaseFile))
        {
            // Retrieve root object
            JsonReader jsonReader = Json.createReader(jsonFileStream);
            JsonObject rootObj = jsonReader.readObject();

            // Read user data
            JsonArray usersArr = rootObj.getJsonArray("users");
            for(JsonObject userDataObj : usersArr.getValuesAs(JsonObject.class)) {
                    UserData uData = UserData.createFromJSONObject(userDataObj);
                    String uName = uData.getName();

                    this.users.put(uName, uData);
            }

            // Release reader resources
            jsonReader.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean checkCR(String user,String nonce,String cr) throws Exception {
        synchronized(this.users) {
            if(!this.users.containsKey(user)) {
        	return false;
            }
            String sCR = this.users.get(user).createCR(nonce);

            return sCR.equals(cr);
        }
    }

    public String collectBalance(String username) {
        synchronized(this.users) {
            StringBuilder sb = new StringBuilder();
            UserData curr = this.users.get(username);
            sb.append("current balance: " + curr.getMoney());
            curr.getMoneyHistory().forEach((tpl) -> {
        	if(tpl.y < 0) {
        	    sb.append("\n" + "sent " + ((-1) * tpl.y) + " to " + tpl.x);
        	} else {
        	    sb.append("\n" + "received " + tpl.y + " from " + tpl.x);
        	}
            });

            return sb.toString();
        }
    }

    public String getUserMail(String username) {
	synchronized (this.users) {
	    return this.users.get(username).getEmail();
	}
    }

    public void registerDevice(String username,String deviceCode) {
	synchronized (this.users) {
	    this.users.get(username).addDevice(deviceCode);
	}
    }

    public void deleteDevice(String username, String deviceCode) {
	synchronized (this.users) {
	    this.users.get(username).removeDevice(deviceCode);
	}
    }

    public boolean checkAuthCR(String userName, String device, String nonce, String cr) throws Exception {
	synchronized (this.users) {
	    if(!Misc.ALLOW_PERMANENT_DEVICES) {
		return false;
	    }
	    String sCR = this.users.get(userName).createAuthCR(device,nonce);
	    if(sCR == null) {
		return false;
	    }

	    return sCR.equals(cr);
	}
    }

    public boolean doTransaction(String userName, String receiver, int amount) {
	synchronized (this.users) {
	    if(!this.users.containsKey(receiver) || this.users.get(userName).getMoney() < amount) {
		return false;
	    }

	    this.users.get(userName).changeMoney(receiver, -amount);
	    this.users.get(receiver).changeMoney(userName, amount);

	    return true;
	}
    }

}
