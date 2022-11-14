package database.doctor;

import static database.doctor.DoctorDAO.*;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//contact firestore manager
//init FireStore class
public class DoctorDTO {

    private static DoctorDTO doctorDTO;
    public static DoctorDTO newInstance(){
        if (doctorDTO == null){
            doctorDTO = new DoctorDTO();
        }
        return doctorDTO;
    }

    private FirebaseFirestore db;
    private CollectionReference doctorRef;
    private DoctorDTO(){
        db = FirebaseFirestore.getInstance();
        doctorRef = db.collection(COLLECTION_NAME);
    }
    // doctor CRUD
    public void newDoctor(DoctorModel doctor){
        doctorRef.add(doctor);
    }
    public void updateDoctor(DoctorModel doctor){
        String docID = doctor.getId();
        DocumentReference newDoctorRef = doctorRef.document(docID);
        newDoctorRef.set(doctor);
    }
    public void deleteDoctor(String id){
        DocumentReference docRef = doctorRef.document(id);
        docRef.delete();
    }
    public void sendTestData(){
        newDoctor(new DoctorModel("nn huy", "test img","male","information","grand master"));
        newDoctor(new DoctorModel("Nguyen Nhat Huy","insert link","male","A very long field to test Textview Display, Information Technology","insert the tittle here"));
        newDoctor(new DoctorModel("name","image","gender","field","tittle"));

    }

}
