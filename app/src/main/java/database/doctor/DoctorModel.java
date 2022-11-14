package database.doctor;

public class DoctorModel {
    private String id;
    private String name;
    private String image;
    private String gender;
    private String field;
    private String title;


    public DoctorModel(String name, String image, String gender, String field, String title) {
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.field = field;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", gender='" + gender + '\'' +
                ", field='" + field + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
