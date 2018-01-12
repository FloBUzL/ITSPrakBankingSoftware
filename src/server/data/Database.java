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


}
