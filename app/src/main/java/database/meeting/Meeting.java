package database.meeting;

import java.sql.Date;

public class Meeting {
    private String doctorID;
    private String patientID;
    private String meetID;
    private Date meetTime;

    public Meeting() {
    }

    public Meeting(String doctorID, String patientID, String meetID, Date meetTime) {
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.meetID = meetID;
        this.meetTime = meetTime;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getMeetID() {
        return this.meetID;
    }

    public void setMeetID(String meetID) {
        this.meetID = meetID;
    }

    public Date getMeetTime() {
        return this.meetTime;
    }

    public void setMeetTime(Date meetTime) {
        this.meetTime = meetTime;
    }

}
