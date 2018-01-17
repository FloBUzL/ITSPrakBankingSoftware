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
import shared.exception.GeneralException;

/**
 * wrapper object for communication to UserData
 * 
 * @author Florian
 */
public class Database {
	private HashMap<String, UserData> users;
	private String databaseFile;

	/**
	 * initializes the UserData objects
	 * 
	 * @param databaseFile
	 *            the database file path
	 * @throws GeneralException
	 */
	public Database(String databaseFile) throws GeneralException {
		this.databaseFile = databaseFile;
		this.users = new HashMap<>();

		try (InputStream jsonFileStream = new FileInputStream(this.databaseFile)) {
			// Retrieve root object
			JsonReader jsonReader = Json.createReader(jsonFileStream);
			JsonObject rootObj = jsonReader.readObject();

			// Read user data
			JsonArray usersArr = rootObj.getJsonArray("users");
			for (JsonObject userDataObj : usersArr.getValuesAs(JsonObject.class)) {
				UserData uData = UserData.createFromJSONObject(userDataObj);
				String uName = uData.getName();

				this.users.put(uName, uData);
			}

			// Release reader resources
			jsonReader.close();
		} catch (FileNotFoundException e) {
			throw new GeneralException();
		} catch (IOException e) {
			throw new GeneralException();
		}
	}

	/**
	 * check if the cr for a device auth was correct
	 * 
	 * @param userName
	 *            the user's name
	 * @param device
	 *            the device
	 * @param nonce
	 *            the nonce used for the cr
	 * @param cr
	 *            the cr sent by the client
	 * @return true if the challenge-response was correct
	 * @throws Exception
	 */
	public boolean checkAuthCR(String userName, String device, String nonce, String cr) throws Exception {
		synchronized (this.users) {
			if (!Misc.ALLOW_PERMANENT_DEVICES) {
				return false;
			}
			String sCR = this.users.get(userName).createAuthCR(device, nonce);
			if (sCR == null) {
				return false;
			}

			return sCR.equals(cr);
		}
	}

	/**
	 * checks if a cr for the user login is correct
	 * 
	 * @param user
	 *            the user to check for
	 * @param nonce
	 *            the nonce used
	 * @param cr
	 *            the challenge-response, sent by the client
	 * @return true if the cr was correct
	 * @throws Exception
	 */
	public boolean checkCR(String user, String nonce, String cr) throws Exception {
		synchronized (this.users) {
			if (!this.users.containsKey(user)) {
				return false;
			}
			String sCR = this.users.get(user).createCR(nonce);

			return sCR.equals(cr);
		}
	}

	/**
	 * collects the user's balance
	 * 
	 * @param username
	 *            the user's name
	 * @return the balance string
	 */
	public String collectBalance(String username) {
		synchronized (this.users) {
			StringBuilder sb = new StringBuilder();
			UserData curr = this.users.get(username);
			sb.append("current balance: " + curr.getMoney());
			curr.getMoneyHistory().forEach((tpl) -> {
				if (tpl.y < 0) {
					sb.append("\n" + "sent " + ((-1) * tpl.y) + " to " + tpl.x);
				} else {
					sb.append("\n" + "received " + tpl.y + " from " + tpl.x);
				}
			});

			return sb.toString();
		}
	}

	/**
	 * delete a user's device
	 * 
	 * @param username
	 *            the user's name
	 * @param deviceCode
	 *            the device code
	 */
	public void deleteDevice(String username, String deviceCode) {
		synchronized (this.users) {
			this.users.get(username).removeDevice(deviceCode);
		}
	}

	/**
	 * do a transaction
	 * 
	 * @param userName
	 *            the sender's name
	 * @param receiver
	 *            the receiver's name
	 * @param amount
	 *            the amount
	 * @return false if there was an error
	 */
	public boolean doTransaction(String userName, String receiver, int amount) {
		synchronized (this.users) {
			if (!this.users.containsKey(receiver) || this.users.get(userName).getMoney() < amount) {
				return false;
			}

			this.users.get(userName).changeMoney(receiver, -amount);
			this.users.get(receiver).changeMoney(userName, amount);

			return true;
		}
	}

	/**
	 * gets the user's email address
	 * 
	 * @param username
	 *            the user's name
	 * @return the email address
	 */
	public String getUserMail(String username) {
		synchronized (this.users) {
			return this.users.get(username).getEmail();
		}
	}

	/**
	 * registers a device for a user
	 * 
	 * @param username
	 *            the user's name
	 * @param deviceCode
	 *            the device code
	 */
	public void registerDevice(String username, String deviceCode) {
		synchronized (this.users) {
			this.users.get(username).addDevice(deviceCode);
		}
	}

	/**
	 * delegates the creation of the initial auth code to the server
	 * @param username the user's name
	 * @param serverCodeFirstPart the first part of the device code, the server generates
	 * @return the initial device code
	 * @throws Exception
	 */
	public String createInitialDeviceAuthCode(String username, String serverCodeFirstPart) throws Exception {
		synchronized(this.users) {
			
			return this.users.get(username).createInitialDeviceAuthCode(serverCodeFirstPart);
		}
	}
}
