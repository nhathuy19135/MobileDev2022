package com.example.mobiledev2022;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import database.Entity.Message;
import database.patient.Patient;
import database.services.MessageAdapter;
public class Chat extends AppCompatActivity {
    private Button button_Send;
    private EditText chatBox;
    private DatabaseReference databaseReference;
    private TextView textView;
    private RecyclerView recyclerView;
    private Integer limitsChatLine ;
    private ArrayList<Message> messageList;
    private MessageAdapter messageAdapter;
    private  LinearLayoutManager linearLayoutManager;

    private boolean isLoading ;
    private static int firstVisibleInListview;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private Patient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.limitsChatLine = 6;
        findUserById();
        initView();
        addDataToArrayList();
        setUpRecycle();
        initEndlessScrolling();
        addMessageToDB();
    }
    private void  initView() {
        this.isLoading = false;
        setContentView(R.layout.activity_chat);
        button_Send = findViewById(R.id.button_Send);
        chatBox = findViewById(R.id.chatBox);
        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
    }
    private void setUpRecycle() {
        messageList = new ArrayList<>();
        this.messageAdapter = new MessageAdapter(messageList);
        this.linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.getLayoutManager().scrollToPosition(messageList.size()-1);
    }

    public void addMessageToDB() {
        String userEmail = getIntent().getExtras().getString("userEmail");
        String avatar = getIntent().getExtras().getString("image");
        String roomdb = getIntent().getExtras().getString("roomdb");
        String name = getIntent().getExtras().getString("name");
        textView.setText(name);
        button_Send.setOnClickListener(view -> {
            String content = chatBox.getText().toString().trim();
            Message message = new Message();
            message.setMessage(content);
            message.setRoomID(roomdb);
            message.setSender(userEmail);
            message.setImageSender(avatar);
            sendMessage(message,roomdb);
            addDataToArrayList();
        });
    }
    private void addDataToArrayList() {
        String room = getIntent().getExtras().getString("roomdb");
        databaseReference = FirebaseDatabase.getInstance().getReference("Roomchat/"+room);
        this.limitsChatLine++;
        Query query = databaseReference.orderByKey().limitToLast(limitsChatLine);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot index: snapshot.getChildren()) {
                    Message snapshot1 =  index.getValue(Message.class);
                    messageList.add(snapshot1);
                }
                messageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this,"error "+error,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initEndlessScrolling() {
        firstVisibleInListview = this.linearLayoutManager.findFirstVisibleItemPosition();
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findFirstVisibleItemPosition() == 0
                    ) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
    private void loadMore() {
        messageList.add(null);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int positionScrolling = messageList.size();
                messageAdapter.notifyItemRemoved(positionScrolling);
                limitsChatLine +=4 ;
                messageAdapter.notifyDataSetChanged();
                addDataToArrayList();
                isLoading=false;
            }
        },1500);
    }
    private void sendMessage(Message message,String room) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Roomchat/"+room);
        databaseReference.push().setValue(message);
        chatBox.setText("");
    }

    private void findUserById() {
        firestore.collection("patient").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (DocumentSnapshot index : querySnapshot.getDocuments()) {
                    Patient a = index.toObject(Patient.class);
                    if (a.getEmail().equals(firebaseAuth.getCurrentUser().getEmail() )) {
                        user = a;
                    }
                }
            }
        });



    }
}