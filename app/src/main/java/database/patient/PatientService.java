package database.patient;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PatientService {
    FirebaseFirestore firebaseFirestore;

    public void addNewUser (Patient user) {
        Map<String, Object> userDTO = userDTO(user);
        firebaseFirestore.collection("users").document(user.getPatientID()).set(userDTO);
    }
    public Map<String, Object> userDTO(Patient user) {

        HashMap<String,Object> userDTO = new HashMap<>();
        userDTO.put("email",user.getEmail());
        userDTO.put("userID",user.getPatientID());
        userDTO.put("userName",user.getName());
        return userDTO;
    }



}
