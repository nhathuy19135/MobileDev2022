package database.patient;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PatientService {
    private FirebaseFirestore firebaseFirestore;

    public void addNewUser (Patient user) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference dbUsers = firebaseFirestore.collection("Patients");
        dbUsers.add(user);
    }
    public boolean isUserExisted(Patient user) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference dbUsers = firebaseFirestore.collection("Patients");
        return dbUsers.equals(user);
    }




}
