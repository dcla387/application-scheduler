package Model;

/**
 * Represents a customer with detailed contact and location information.
 * This class holds cusomter details.
 *
 * @author Donna Clark
 * @version 1.0
 */

public class Customer {

    /**
     * Unique ID for the customer - field storage
     */

    private int customerId;

    /**
     * Name of the customer for field storage
     */
    private String customerName;

    /**
     * Address of the customer - field to store this information
     */
    private String address;

    /**
     * Postal code of the customer and field storage
     */
    private String postalCode;

    /**
     * Phone number of the customer and field storage
     */
    private String phone;

    /**
     * ID for the customer's division
     */
    private int divisionId;

    /**
     * Name of the customers division
     */
    private String divisionName;

    /**
     * Name of the customer's country
     */
    private String countryName;

    /**
     * Constructs a new customer with the details below
     *
     * @param customerId Unique ID for the customer
     * @param customerName NAme of the customer
     * @param address Address of the customer
     * @param postalCode Postal Code of the customer
     * @param phone Phone number of the customer
     * @param divisionId ID for the customer's division
     * @param divisionName Name of the csutomer's division
     */

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String divisionName) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;

    }

    /**
     * Returns a string representation of the customer
     * @return the customer's name
     */

    @Override
    public String toString() {
        return customerName;
    }

    /**
     * Retrieves the customer's ID
     * @return the customer's ID as an INT
     */

    public int getCustomerId() {
        return customerId;
    }

    /**
     * retrieves the customer's name
     * @return the customer's name
     */

    public String getCustomerName() {
        return customerName;
    }

    /**
     * Retrieves the customer's address
     * @return The customer's address
     */

    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the customers postal code
     * @return the customer's postal code as a STR
     */


    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Retrieves the customer's phone number
     * @return the customer's phone number as a String
     */

    public String getPhone() {
        return phone;
    }

    /**
     * Retrieves the customer's division ID
     * @return The division ID as an INT
     */

    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Retrieves the name of the customer's division
     * @return the division name as a String
     */

    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Retrieves the name of the customer's country
     * @return the country name as a String
     */

    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the customer's ID
     * @param customerId The new customer ID to set
     */

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Sets the customer's name
     * @param customerName The new customer name to set
     */

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Sets the customer's sphysical address
     * @param address the new address to set
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the customers postal code
     * @param postalCode the new postal code to set
     */

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the customer's phone number
     * @param phone The new phone number to set
     */

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the customer's division ID
     * @param divisionId the new division ID to set
     */

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Sets the name of the customer's division
     * @param divisionName the new division name to set
     */

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Sets the name of the customer's country
     * @param countryName the new contry to set
     */

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}

