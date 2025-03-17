package Model;

public class Contact {
    private int contactId;
    private String contactName;

    public Contact(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;

    }

        public int getContactId() {
        return contactId;
        }

        public String getContactName() {
        return contactName;
        }


        @Override
    public String toString() {
        return contactName;
        }


}

