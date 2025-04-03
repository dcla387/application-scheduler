package Model;

/**
 * this class encapsulates division information including unique ID's and the country it belongs to.
 *
 * @author Donna Clark
 * @version 1.0
 */

public class Division {

    /**
     * Unique ID for the division
     */
    private int divisionId;
    /**
     * Name of the division
     */

    private String name;
    /**
     * ID of the country to which the division belongs
     */

    private int countryID;

    /**
     * constructs a new Division with the specified info.
     * @param divisionId Unique ID for the division
     * @param name Name of the Division
     * @param countryID ID of the country assoicated with the Division
     */


    public Division(int divisionId, String name, int countryID) {
        this.divisionId = divisionId;
        this.name = name;
        this.countryID = countryID;
    }

    /**
     * Retrieves the name of the division
     * @return division's name as a string
     */


    public String getName() { return name;}
    public int getCountryID() { return countryID;}

    /**
     * Returns a string representation of the division
     * @return the divisiong's name
     */

    @Override
    public String toString() {return name;}

    /**
     * Retrieves the unique ID of the division
     * @return Division's ID as an INT
     */

    public int getDivisionId() {
        return divisionId;
    }
}
