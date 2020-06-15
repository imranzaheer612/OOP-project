package sample;
/*
* this class has been made
* for the table view we have made in the
* reservation class
*
* The table shows the customer data
* */
public class TableDataForReservations {
    private String name;
    private String phNumber;
    private String id ;
    private String city;
    private String departureDate;
    private String arrivalDate;
    private String roomNumber;
    private String reservationNumber;

    TableDataForReservations(){}
    TableDataForReservations(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8){
        this.roomNumber = s1;
        this.name = s2;
        this.phNumber = s3;
        this.id = s4;
        this.city = s5;
        this.arrivalDate = s6;
        this.departureDate = s7;
        this.reservationNumber = s8;
    }
    /* Giving
     * Values to data-fields
     * So that table view can be
     * created for
     * reservation data
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

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
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

    public String getReservationNumber() {
        return reservationNumber;
    }

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", phNumber='" + phNumber + '\'' +
                ", id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", reservationNumber='" + reservationNumber + '\'' +
                '}';
    }
}
