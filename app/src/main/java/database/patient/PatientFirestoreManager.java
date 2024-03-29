package database.patient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PatientFirestoreManager {
    private static PatientFirestoreManager patientFirestoreManager;
    private CollectionReference patientReference;
    public static PatientFirestoreManager newInstance(){
        if (patientFirestoreManager == null){
            patientFirestoreManager = new PatientFirestoreManager();
        }
        return patientFirestoreManager;
    }
    private FirebaseFirestore fbfs;
    public PatientFirestoreManager(){
        fbfs = FirebaseFirestore.getInstance();
        patientReference = fbfs.collection(PatientDBContract.COLLECTION_NAME);
    }
    public void getAllPatients(OnCompleteListener<QuerySnapshot> onCompleteListener){
        patientReference.get().addOnCompleteListener(onCompleteListener);
    }
    public void newPatient (Patient patient){
        patientReference.add(patient);
    }
    public void updatePatient (String documentId, Patient patient){
        DocumentReference docRef = patientReference.document(documentId);
        docRef.set(patient);
    }
    public void deletePatient (String documentId){
        DocumentReference docRef = patientReference.document(documentId);
        docRef.delete();
    }
    public void sendTestData(){
        newPatient(new Patient("Nguyen", "Nhat Huy","male","test mail","0123456789"));
    }

}
