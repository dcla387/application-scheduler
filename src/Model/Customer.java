package Model;



public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private String divisionName;
    private String countryName;

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String divisionName) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;

    }

    @Override
    public String toString() {
        return customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}

