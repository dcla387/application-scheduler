package Model;

public class Country {

    private int countryID;
    private String countryName;

    public Country(int countryID, String countryName){
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }

    public int getCountryId() {
        return countryID;
    }
}
