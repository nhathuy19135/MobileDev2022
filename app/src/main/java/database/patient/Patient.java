package database.patient;

public class Patient {
    private String patientID;
    private String image;
    private String name;
    private String email;
    private String phone;
    private String gender;

    public Patient() {
    }

    public Patient(String patientID, String image, String name, String email, String phone, String gender) {
        this.patientID = patientID;
        this.image = image;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}