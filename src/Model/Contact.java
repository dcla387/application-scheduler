package Model;

/**
 * This class holds basic contact information
 *
 * @author Donna Clark
 * @version 1.0
 */

public class Contact {

    /**
     * Field stores the contact's unique ID number
     */
    private int contactId;

    /**
     * This field stores the contact's name
     */
    private String contactName;

    /**
     * Constructs a new contact with the specified ID and name
     * @param contactId The unique ID for the contact
     * @param contactName The unique name for the contact
     */

    public Contact(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;

    }

    /**
     * Retrieves the contact's ID
     * @return the contact's ID as an INT
     */

        public int getContactId() {
        return contactId;
        }

    /**
     * Retrieves the contact's name
     * @return the contact's name
     */
    public String getContactName() {
        return contactName;
        }

    /**
     * Returns a strgin representation of the contact
     * @return the contact's name
     */
        @Override
    public String toString() {
        return contactName;
        }


}

