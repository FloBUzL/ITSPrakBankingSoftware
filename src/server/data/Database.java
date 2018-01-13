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
        	sb.append("sent " + tpl.y + " to " + tpl.x);
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

    public boolean checkAuthCR(String userName, String device, String nonce, String cr) throws Exception {
	synchronized (this.users) {
	    String sCR = this.users.get(userName).createAuthCR(device,nonce);

	    return sCR.equals(cr);
	}
    }

}
