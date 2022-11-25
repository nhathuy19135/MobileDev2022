package database.services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.Entity.Message;

public class MessageService{
    private FirebaseFirestore firebaseFirestore;
    public void addMessageToDb(Message message) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String,Object> newMessage = new HashMap<>();
        newMessage.put("sender",message.getSender());
        newMessage.put("message",message.getMessage());
        newMessage.put("room",message.getRoomID());
        firebaseFirestore.collection("RoomChat").document(message.getRoomID()).set(newMessage);
    }
//    public ArrayList<Message> getAllMessage(String room) {
//        ArrayList<Message> messageList = new ArrayList<>();
//        firebaseFirestore.collection("users").document(room).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    messageList.add();
//                }
//             }
//        });
//
//    }
}
