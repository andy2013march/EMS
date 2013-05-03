package EMS_Database.impl;

import EMS_Database.DoesNotExistException;
import EMS_Database.DuplicateInsertionException;
import EMS_Database.InitDB;
import static EMS_Database.InitDB.debugLog;
import EMS_Database.Interface_EventData;
import EMS_Database.InputEventData;
import EMS_Database.Interface_FunctionWrapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * A class to control the Events Data.
 *
 * @author mike
 */
public class Events_Table extends InitDB implements Interface_EventData{

    private static String tableName = "EVENTS";

    /////////////////////SPECIAL FUNCTIONS///////////////////////////
//    /**
//     * A special function designed to format integer array lists into a string
//     * to be used for actual integer data return from the database.
//     *
//     * @param uidList A formatted string from the database representing the UID
//     * list.
//     * @return A nice array list of the UID's from the database.
//     * @throws NumberFormatException if you somehow try to put something that
//     * cannot be parsed into the conversion string.
//     */
//    @Override
//    public ArrayList<Integer> stringToList(String uidList) throws NumberFormatException {
//	//Split String
//	String[] uidStringList;
//	uidStringList = uidList.split(",");
//
//	ArrayList<Integer> uidIntList = new ArrayList<Integer>();	
//
//	//parse each item into arraylist
//	for (String uid : uidStringList) {
//	    try {
//		uidIntList.add(Integer.parseInt(uid));
//	    } catch (NumberFormatException nfe) {
//		debugLog.log(Level.SEVERE,"Parse Error while parsing "+uidList);
//		throw new NumberFormatException("Parse Error");
//	    }
//	}
//
//	return uidIntList;
//    }

    /**
     * Does the opposite of string to list and creates a nicely formatted string
     * for insertion into the database.
     *
     * @param list An ArrayList of Integers representing the UID numbers to be
     * stored.
     * @return A nicely formated String for insertion into the database.
     */
    @Override
    public String listToString(ArrayList<Integer> list) {
	StringBuilder returnQuery = new StringBuilder();
	for (int uid : list) {
	    returnQuery.append(uid);
	    returnQuery.append(",");
	}
	return returnQuery.toString();
    }   

    /**
     * Input a new event into the events using InputEvent class to create a
     * valid input object
     *
     * @param event of type InputEvent for row insertion.
     * @return the UID of the created event.
     * @throws DuplicateInsertionException
     */
    @Override
    public int createEvent(InputEventData event) throws DuplicateInsertionException {
	int newUID = nextValidUID();

	try {
	    //Creating Statement
	    PreparedStatement AddAddressStmt = dbConnection.prepareStatement("INSERT INTO EVENTS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    AddAddressStmt.setInt(1, newUID);
	    AddAddressStmt.setString(2, event.getDescription());
	    AddAddressStmt.setString(3, event.getDetails());
	    AddAddressStmt.setTimestamp(4, event.getStartDate());
	    AddAddressStmt.setTimestamp(5, event.getEndDate());
	    AddAddressStmt.setInt(6, event.getComplete());
	    AddAddressStmt.setString(7, event.getStreet());
	    AddAddressStmt.setString(8, event.getCity());
	    AddAddressStmt.setString(9, event.getState());
	    AddAddressStmt.setString(10, event.getZipcode());
	    AddAddressStmt.setString(11, event.getCountry());
	    AddAddressStmt.setString(12, listToString(event.getOrganizerList())); //inserted as a string
	    AddAddressStmt.setString(13, listToString(event.getSubEventList())); //inserted as a string
	    AddAddressStmt.setString(14, listToString(event.getParticipantList())); //inserted as a string
	    AddAddressStmt.setString(15, listToString(event.getCommittee())); //inserted as a string

	    //Execute Statement
	    AddAddressStmt.executeUpdate();

	    //checking to see if UID already exists.
	    for (int uid : currentUIDList()) {
		if (newUID == uid) {
		    throw new DuplicateInsertionException("EventsTable");
		}
	    }

	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	} finally {
	    return newUID;
	}
    }
    
    public ArrayList<Integer> currentUIDList() {
	int newUID = 0;
	ArrayList<Integer> UIDList = new ArrayList<Integer>();
	try {

	    PreparedStatement idQueryStmt = dbConnection.prepareStatement("SELECT * FROM EVENTS");
	    ResultSet rs = idQueryStmt.executeQuery();

	    while (rs.next()) {
		newUID = rs.getInt("UID");
		UIDList.add(newUID);
	    }
	    return UIDList;

	} catch (SQLException sqle) {
	    sqle.printStackTrace();
	    System.exit(1);
	}
	return UIDList; // should not be zero
    }

    /**
     * Gets the next vaild UID in the Events table
     *
     * @return the next valid UID that should be used.
     */
    @Override
    public int nextValidUID() {
	int newUID = 0;
	try {

	    PreparedStatement idQueryStmt = dbConnection.prepareStatement("SELECT * FROM EVENTS");
	    ResultSet rs = idQueryStmt.executeQuery();

	    while (rs.next()) {
		newUID = rs.getInt("UID");
		//System.out.println(newUID);
	    }
	    return (newUID + 1);

	} catch (SQLException sqle) {
	    sqle.printStackTrace();
	    System.exit(1);
	}
	return newUID; // should not be zero
    }

    /**
     * Debug function to return a string to view the entire table contents.
     *
     * @return the entire table as a formatted string.
     */
    @Override
    public String queryEntireTable() {
	StringBuilder returnQuery = new StringBuilder();
	try {
	    PreparedStatement idQueryStmt = dbConnection.prepareStatement("SELECT * FROM EVENTS");
	    ResultSet rs = idQueryStmt.executeQuery();

	    while (rs.next()) {
		returnQuery.append(rs.getString("UID"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("DESCRIPTION"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("DETAILS"));
		returnQuery.append(",");
		returnQuery.append(rs.getTimestamp("STARTDATE"));
		returnQuery.append(",");
		returnQuery.append(rs.getTimestamp("ENDDATE"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("COMPLETE"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("STREET"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("CITY"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("STATE"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("ZIPCODE"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("COUNTRY"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("ORGANIZER"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("SUBEVENT"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("PARTICIPANT"));
		returnQuery.append(",");
		returnQuery.append(rs.getString("COMMITTEE"));
		returnQuery.append("\n");
	    }

	} catch (SQLException sqle) {
	    sqle.printStackTrace();
	    System.exit(1);
	}

	return returnQuery.toString();
    }

    /**
     * A method to remove an event from the events table.
     *
     * @param uid the UID of the event to be removed.
     * @return a boolean that returns true if removal was successful.
     * @throws DoesNotExistException if the UID does not exist in the table.
     */
    @Override
    public boolean removeEvent(int uid) throws DoesNotExistException {
	try {

	    PreparedStatement idQueryStmt = dbConnection.prepareStatement("DELETE FROM EVENT WHERE UID=?");
	    idQueryStmt.setInt(1, uid);
	    idQueryStmt.executeUpdate();

	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    throw new DoesNotExistException("User does not exist.");
	}
	return true;
    }

    
    //////////////////////GETTERS////////////////////////////
    /**
     * Returns the specified users description.
     *
     * @param uid the user being searched for
     * @return the description as a String.
     * @throws DoesNotExistException if the user you are searching for does not
     * exist.
     */
    @Override
    public String getDescription(int uid) throws DoesNotExistException {
	return getDBString("DESCRIPTION",tableName,uid);
    }

    @Override
    public String getDetails(int uid) throws DoesNotExistException {
	return getDBString("DETAILS",tableName,uid);
    }

    /**
     * A method to get the start date of the user with UID specified.
     *
     * @param uid the UID of the user in question.
     * @return A timestamp of the start date
     * @throws DoesNotExistException if the user does not exist.
     */
    @Override
    public Timestamp getStartDate(int uid) throws DoesNotExistException {
	return getDBTimestamp("STARTDATE",tableName,uid);
    }

    /**
     * I think you can figure out what the rest of these functions do...
     *
     * @param uid
     * @return
     * @throws DoesNotExistException
     */
    @Override
    public Timestamp getEndDate(int uid) throws DoesNotExistException {
	return getDBTimestamp("ENDDATE",tableName,uid);
    }

    /**
     * If you cant figure out what these methods do by now then just ask me.
     *
     * @param uid
     * @return
     * @throws DoesNotExistException
     */
    @Override
    public int getComplete(int uid) throws DoesNotExistException {
	return getDBInt("COMPLETE",tableName,uid);
    }

    @Override
    public String getStreet(int uid) throws DoesNotExistException {
	return getDBString("STREET",tableName,uid);
    }

    @Override
    public String getCity(int uid) throws DoesNotExistException {
	return getDBString("CITY",tableName,uid);
    }

    @Override
    public String getState(int uid) throws DoesNotExistException {
	return getDBString("STATE",tableName,uid);
    }

    @Override
    public String getZipcode(int uid) throws DoesNotExistException {
	return getDBString("ZIPCODE",tableName,uid);
    }

    @Override
    public String getCountry(int uid) throws DoesNotExistException {
	return getDBString("COUNTRY",tableName,uid);
    }

    @Override
    public ArrayList<Integer> getOrganizerList(int uid) throws DoesNotExistException {
	return getDBArrayList("ORGANIZER",tableName,uid);
    }

    @Override
    public ArrayList<Integer> getSubEventList(int uid) throws DoesNotExistException {
	return getDBArrayList("SUBEVENT",tableName,uid);
    }

    @Override
    public ArrayList<Integer> getParticipantList(int uid) throws DoesNotExistException {
	return getDBArrayList("PARTICIPANT",tableName,uid);
    }

    @Override
    public ArrayList<Integer> getCommittee(int uid) throws DoesNotExistException {
	return getDBArrayList("COMMITTEE",tableName,uid);
    }

    ////////////////////////SETTERS///////////////////////////////
    @Override
    public void setDescription(int uid, String description) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET DESCRIPTION=? WHERE UID=?");
		idQueryStmt.setString(1, description);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setDetails(int uid, String details) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET DETAILS=? WHERE UID=?");
		idQueryStmt.setString(1, details);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setStartDate(int uid, Timestamp time) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET STARTDATE=? WHERE UID=?");
		idQueryStmt.setTimestamp(1, time);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setEndDate(int uid, Timestamp time) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET ENDDATE=? WHERE UID=?");
		idQueryStmt.setTimestamp(1, time);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setComplete(int uid, int complete) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET COMPLETE=? WHERE UID=?");
		idQueryStmt.setInt(1, complete);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setStreet(int uid, String street) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET STREET=? WHERE UID=?");
		idQueryStmt.setString(1, street);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setCity(int uid, String city) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET CITY=? WHERE UID=?");
		idQueryStmt.setString(1, city);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setState(int uid, String state) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET STATE=? WHERE UID=?");
		idQueryStmt.setString(1, state);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setZipcode(int uid, String zipcode) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET ZIPCODE=? WHERE UID=?");
		idQueryStmt.setString(1, zipcode);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setCountry(int uid, String country) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET COUNTRY=? WHERE UID=?");
		idQueryStmt.setString(1, country);
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setOrganizerList(int uid, ArrayList<Integer> organizerList) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET ORGANIZER=? WHERE UID=?");
		idQueryStmt.setString(1, listToString(organizerList));
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setSubEventList(int uid, ArrayList<Integer> subEventList) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET SUBEVENT=? WHERE UID=?");
		idQueryStmt.setString(1, listToString(subEventList));
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setParticipantList(int uid, ArrayList<Integer> participantList) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET PARTICIPANT=? WHERE UID=?");
		idQueryStmt.setString(1, listToString(participantList));
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }

    @Override
    public void setCommittee(int uid, ArrayList<Integer> committeeList) throws DoesNotExistException {
	try {
	    boolean exists = false;
	    for (int validID : currentUIDList()) {
		if (validID == uid) {
		    exists = true;
		    break;
		}
	    }
	    if (exists) {
		PreparedStatement idQueryStmt = dbConnection.prepareStatement("UPDATE EVENTS SET COMMITTEE=? WHERE UID=?");
		idQueryStmt.setString(1, listToString(committeeList));
		idQueryStmt.setInt(2, uid);
		idQueryStmt.executeUpdate();
	    } else {
		debugLog.log(Level.WARNING, "UID={0} does not exist in EVENT table.", uid);
		throw new DoesNotExistException("User does not exist in EVENT table.");
	    }
	} catch (SQLException sqle) {
	    System.err.println(sqle.getMessage());
	    debugLog.severe("Major SQL-Error in EVENT table.");
	    throw new DoesNotExistException("User does not exist in EVENT table.");
	}
    }
}
