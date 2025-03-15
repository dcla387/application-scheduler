package Model;

public class Division {

    private int divisionId;

    private String name;

    private int countryID;


    public Division(int divisionId, String name, int countryID) {
        this.divisionId = divisionId;
        this.name = name;
        this.countryID = countryID;
    }


    public String getName() { return name;}
    public int getCountryID() { return countryID;}



    @Override
    public String toString() {return name;}

    public int getDivisionId() {
        return divisionId;
    }
}
