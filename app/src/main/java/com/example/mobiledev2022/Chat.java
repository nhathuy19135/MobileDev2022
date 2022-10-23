package com.example.mobiledev2022;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import database.Entity.Message;
import database.services.MessageAdapter;
public class Chat extends AppCompatActivity {
    private Button button_Send;
    private EditText chatBox;
    private DatabaseReference databaseReference;
    private TextView textView;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private Integer limitsChatLine ;
    private ArrayList<Message> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.limitsChatLine = 0;
        setContentView(R.layout.activity_chat);
        addDataToArrayList();
        initView();
        sendMessage();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    public void sendMessage() {
        String room = getIntent().getExtras().getString("room");
        String userEmail = getIntent().getExtras().getString("userEmail");
        textView.setText(room);
        button_Send.setOnClickListener(view -> {
            String content = chatBox.getText().toString().trim();
            Message message = new Message();
            message.setMessage(content);
            message.setRoomID(room);
            message.setSender(userEmail);
            sendMessage(message,room);
        });}
    public void initView () {
        button_Send = findViewById(R.id.button_Send);
        chatBox = findViewById(R.id.chatBox);
        textView = findViewById(R.id.textView);
        setUpRecycleView();
    }
    public void setUpRecycleView(){
        recyclerView = findViewById(R.id.recycler1);
        messageList = new ArrayList<>();
        MessageAdapter messageAdapter = new MessageAdapter(messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);
    }
    private void addDataToArrayList() {
        String room = getIntent().getExtras().getString("room");
        databaseReference = FirebaseDatabase.getInstance().getReference(room);
        this.limitsChatLine = 7;
        Query query = databaseReference.limitToLast(limitsChatLine);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot index: snapshot.getChildren()) {
                    Message snapshot1 =  index.getValue(Message.class);
                    messageList.add(snapshot1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this,"error "+error,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendMessage(Message message,String room) {
        databaseReference = FirebaseDatabase.getInstance().getReference(room);
        databaseReference.push().setValue(message);
        textView.setText(message.getMessage());
        addDataToArrayList();
        setUpRecycleView();
        chatBox.setText("");
    }
}