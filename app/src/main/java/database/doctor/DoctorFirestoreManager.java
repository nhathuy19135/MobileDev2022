package database.doctor;

import static database.doctor.DoctorDBContract.COLLECTION_NAME;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorFirestoreManager {
    private static DoctorFirestoreManager doctorFirestoreManager;
    private CollectionReference doctorReference;
    public static DoctorFirestoreManager newInstance(){
        if (doctorFirestoreManager == null){
            doctorFirestoreManager = new DoctorFirestoreManager();
        }
        return doctorFirestoreManager;
    }
    private FirebaseFirestore fbfs;
    private DoctorFirestoreManager(){
        fbfs = FirebaseFirestore.getInstance();
        doctorReference = fbfs.collection(COLLECTION_NAME);
    }
    public void newDoctor (Doctor doctor){
        doctorReference.add(doctor);
    }
    public void getAllDoctors (OnCompleteListener<QuerySnapshot> onCompleteListener){
        doctorReference.get().addOnCompleteListener(onCompleteListener);
    }
    public void updateDoctor (String documentId, Doctor doctor){
        DocumentReference docRef = doctorReference.document(documentId);
        docRef.set(doctor);
    }
    public void deleteDoctor (String documentId){
        DocumentReference docRef = doctorReference.document(documentId);
        docRef.delete();
    }
    public void sendTestData(){
        newDoctor(new Doctor("Nguyen", "Nhat Huy", "test@mail.com","0133456789", "IT", "male","veteran"));
    }
}
