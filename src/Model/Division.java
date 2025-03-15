package Model;

public class Division {

    private int divisionId;

    private String name;

    private int countryId;


    public Division(int divisionId, String name, int countryId) {
        this.divisionId = divisionId;
        this.name = name;
        this.countryId = countryId;
    }


    public String getName() { return name;}
    public int getCountryID() { return countryId;}



    @Override
    public String toString() {return name;}

    public int getDivisionId() {
        return divisionId;
    }
}
