package database.doctor;

public class Doctor {
    private String doctorID;
    private String name;
    private String image;
    private String gender;
    private String field;
    private String title;

    public Doctor() {
    }

    public Doctor(String doctorID, String name, String image, String gender, String field, String title) {
        this.doctorID = doctorID;
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.field = field;
        this.title = title;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
