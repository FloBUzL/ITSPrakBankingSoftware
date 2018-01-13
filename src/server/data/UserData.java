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

public class UserData {
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
    private UserData(JsonObject userDataObj)
    {
        // Read attributes
        userName = userDataObj.getString("name");
        userEmail = userDataObj.getString("email");
        userPassword = userDataObj.getString("password");
        userMoney = userDataObj.getInt("money");
        notify = userDataObj.getBoolean("notify");

        // Read device list
        userDeviceAuthenticationStrings = new LinkedList<>();
        for(JsonValue val : userDataObj.getJsonArray("devices"))
            if(val.getValueType() == ValueType.STRING)
                userDeviceAuthenticationStrings.add(((JsonString)val).getString());

        // Initialize empty history
        userMoneyHistory = new LinkedList<>();
    }

    /**
     * Saves the user's data into a JSON object.
     *
     * @return A JSON object with the user's data.
     * @deprecated Only for testing purposes.
     */
    public JsonObject toJson()
    {
        // Put device list into JSON array
        JsonArrayBuilder deviceArrayBuilder = Json.createArrayBuilder();
        for(String device : userDeviceAuthenticationStrings)
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

    public static UserData createFromJSONObject(JsonObject userData) {
	UserData newUser = new UserData(userData);

	return newUser;
    }

    /**
     * Returns the user's name.
     *
     * @return The user's name.
     */
    public String getName()
    {
        return userName;
    }

    /**
     * Returns the user's e-mail address.
     *
     * @return The user's e-mail address.
     */
    public String getEmail()
    {
        return userEmail;
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
    public boolean verifyLogin(String name, String password)
    {
        // Compare variables
        return name.equalsIgnoreCase(userName) && password.equals(userPassword);
    }

    public String createCR(String nonce) throws Exception {
	return new Hash(this.userName + this.userPassword + nonce).toString();
    }

    /**
     * Adds the device with the given authentication code.
     *
     * @param deviceCode
     *            The device code to be added.
     */
    public void addDevice(String deviceCode)
    {
        // Add device
        userDeviceAuthenticationStrings.add(deviceCode);
    }

    /**
     * Checks whether this user has a device with the given code.
     *
     * @param deviceCode
     *            The device code to be searched.
     * @return Whether this user has a device with the given code.
     */
    public boolean hasDevice(String deviceCode)
    {
        // Find device
        for(String code : userDeviceAuthenticationStrings)
            if(code.equals(deviceCode))
                return true;
        return false;
    }

    /**
     * Returns the user's amount of money.
     *
     * @return The user's amount of money.
     */
    public int getMoney()
    {
        return userMoney;
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
    public void changeMoney(String userName, int money)
    {
        // Add history entry
        userMoneyHistory.add(new Tuple<String, Integer>(userName, money));

        // Update money amount
        userMoney += money;

        // Show notification
        //if(money < 0 && _notify)
            //Utility.safePrintln(_name + ": Lost money to user #" + userId);
    }

    /**
     * Returns a list containing the user's full money sending/receiving history.
     *
     * @return A list containing the given user's full money sending/receiving
     *         history.
     */
    public LinkedList<Tuple<String, Integer>> getMoneyHistory()
    {
        return userMoneyHistory;
    }

    public String createAuthCR(String device, String nonce) throws Exception {
	String authCode = device.substring(16, 24);
	return new Hash(authCode + this.userEmail + nonce).toString();
    }
}
