package sample;
/*
 * this class has been made
 * for the table view we have made in the
 * reservation class
 *
 * The table shows the customer data
 * */
public class TableDataForCustomerClass {
    private String name;
    private String phNumber;
    private String id ;
    private String city;
    private String nationality;
    private String departureDate;
    private String birthDate;
    private String roomNumber;
    private String emergencyContactNumber;
    private String emergencyContactName;
    private String arrivalDate;
    private String customerNumber;

    TableDataForCustomerClass(){}
    TableDataForCustomerClass(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11, String s12){
        this.roomNumber = s1;
        this.name = s2;
        this.phNumber = s3;
        this.id = s4;
        this.city = s5;
        this.nationality = s6;
        this.birthDate = s7;
        this.departureDate = s8;
        this.emergencyContactNumber = s9;
        this.emergencyContactName = s10;
        this.arrivalDate = s11;
        this.customerNumber = s12;
    }

    /* Giving
     * Values to datafields
     * So that table view can be
     * created for
     * Customer data
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public String getId() {
        return id;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getCity() {
        return city;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    @Override
    public String toString() {
        return "TableDataForCustomerClass{" +
                "name='" + name + '\'' +
                ", phNumber='" + phNumber + '\'' +
                ", id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", emergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", customerNumber='" + customerNumber + '\'' +
                '}';
    }
}
