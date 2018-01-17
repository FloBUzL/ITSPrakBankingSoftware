package server.data;

import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import shared.security.Hash;
import shared.security.Hex;

public class UserData {
	public static UserData createFromJSONObject(JsonObject userData) {
		UserData newUser = new UserData(userData);

		return newUser;
	}

	private String userName;

	/**
	 * The user's e-mail address.
	 */
	private String userEmail;

	/**
	 * The user's password.
	 */
	private String userPassword;

	/**
	 * The user's amount of money.
	 */
	private int userMoney;

	/**
	 * The authentication codes of the user's devices.
	 */
	private LinkedList<String> userDeviceAuthenticationStrings;

	/**
	 * Determines whether a message should be output when this user looses money.
	 */
	private boolean notify;

	/**
	 * The history of money changes on this account.
	 */
	private LinkedList<Tuple<String, Integer>> userMoneyHistory;

	/**
	 * Reads the user data from the given JSON object.
	 */
	private UserData(JsonObject userDataObj) {
		// Read attributes
		userName = userDataObj.getString("name");
		userEmail = userDataObj.getString("email");
		userPassword = userDataObj.getString("password");
		userMoney = userDataObj.getInt("money");
		notify = userDataObj.getBoolean("notify");

		// Read device list
		userDeviceAuthenticationStrings = new LinkedList<>();
		for (JsonValue val : userDataObj.getJsonArray("devices"))
			if (val.getValueType() == ValueType.STRING)
				userDeviceAuthenticationStrings.add(((JsonString) val).getString());

		// Initialize empty history
		userMoneyHistory = new LinkedList<>();
	}

	/**
	 * Adds the device with the given authentication code.
	 *
	 * @param deviceCode
	 *            The device code to be added.
	 */
	public void addDevice(String deviceCode) {
		// Add device
		userDeviceAuthenticationStrings.add(deviceCode);
	}

	/**
	 * Changes the amount of money this user has, and tracks the change in the
	 * history.
	 *
	 * @param userId
	 *            The ID of the changing user.
	 * @param money
	 *            The amount of money added (positive value) or removed (negative
	 *            value) from this user. This function simply does an addition, the
	 *            checks must be done by the caller!
	 */
	public void changeMoney(String userName, int money) {
		// Add history entry
		userMoneyHistory.add(new Tuple<String, Integer>(userName, money));

		// Update money amount
		userMoney += money;

		// Show notification
		// if(money < 0 && _notify)
		// Utility.safePrintln(_name + ": Lost money to user #" + userId);
	}

	/**
	 * creates a cr
	 * 
	 * @param device
	 *            the device's code
	 * @param nonce
	 *            the nonce to use
	 * @return the cr
	 * @throws Exception
	 */
	public String createAuthCR(String device, String nonce) throws Exception {
		if (!this.hasDevice(device)) {
			return null;
		}
		String devCodeFirstServerPart = device.substring(16, 24);
		
		return new Hash(this.createInitialDeviceAuthCode(devCodeFirstServerPart) + nonce).toString();
	}

	/**
	 * create's a cr
	 * 
	 * @param nonce
	 *            the nonce to use
	 * @return the cr created
	 * @throws Exception
	 */
	public String createCR(String nonce) throws Exception {
		return new Hash(this.userName + this.userPassword + nonce).toString();
	}

	/**
	 * Returns the user's e-mail address.
	 *
	 * @return The user's e-mail address.
	 */
	public String getEmail() {
		return userEmail;
	}

	/**
	 * Returns the user's amount of money.
	 *
	 * @return The user's amount of money.
	 */
	public int getMoney() {
		return userMoney;
	}

	/**
	 * Returns a list containing the user's full money sending/receiving history.
	 *
	 * @return A list containing the given user's full money sending/receiving
	 *         history.
	 */
	public LinkedList<Tuple<String, Integer>> getMoneyHistory() {
		return userMoneyHistory;
	}

	/**
	 * Returns the user's name.
	 *
	 * @return The user's name.
	 */
	public String getName() {
		return userName;
	}

	/**
	 * Checks whether this user has a device with the given code.
	 *
	 * @param deviceCode
	 *            The device code to be searched.
	 * @return Whether this user has a device with the given code.
	 */
	public boolean hasDevice(String deviceCode) {
		// Find device
		for (String code : userDeviceAuthenticationStrings) {
			if (code.equals(deviceCode))
				return true;
		}
		return false;
	}

	/**
	 * deletes a device with given code
	 * 
	 * @param deviceCode
	 *            the device's code
	 */
	public void removeDevice(String deviceCode) {
		this.userDeviceAuthenticationStrings.remove(deviceCode);
	}

	/**
	 * Saves the user's data into a JSON object.
	 *
	 * @return A JSON object with the user's data.
	 * @deprecated Only for testing purposes.
	 */
	public JsonObject toJson() {
		// Put device list into JSON array
		JsonArrayBuilder deviceArrayBuilder = Json.createArrayBuilder();
		for (String device : userDeviceAuthenticationStrings)
			deviceArrayBuilder.add(device);

		// Create user data JSON object
		JsonObjectBuilder objBuilder = Json.createObjectBuilder();
		objBuilder.add("name", userName);
		objBuilder.add("email", userEmail);
		objBuilder.add("password", userPassword);
		objBuilder.add("money", userMoney);
		objBuilder.add("devices", deviceArrayBuilder.build());
		objBuilder.add("notify", notify);
		return objBuilder.build();
	}

	/**
	 * Checks whether the given credentials are valid for this user.
	 *
	 * @param name
	 *            The name of the user.
	 * @param password
	 *            The password of the user.
	 * @return A boolean value indicating whether the given credentials are valid
	 *         for this user.
	 */
	public boolean verifyLogin(String name, String password) {
		// Compare variables
		return name.equalsIgnoreCase(userName) && password.equals(userPassword);
	}

	/**
	 * creates the initial authcode for a device
	 * @param serverCodeFirstPart the first part of the device code, the server generates
	 * @return the initial authcode
	 * @throws Exception
	 */
	public String createInitialDeviceAuthCode(String serverCodeFirstPart) throws Exception {
		Hex hex = new Hex(this.userName);
		String email = this.userEmail;
		email = hex.toHex(email.getBytes());
		return new Hash(serverCodeFirstPart + email).toString();
	}
}
