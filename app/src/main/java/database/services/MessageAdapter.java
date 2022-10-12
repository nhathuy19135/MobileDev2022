package database.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledev2022.R;
import database.Entity.Message;
import java.util.List;

public class MessageAdapter extends  RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> chatLineList;
    public MessageAdapter(List<Message> messageList) {
        this.chatLineList = messageList;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatline,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder,final int position) {
        Message message = chatLineList.get(position);
        if (message == null) {
            return;
        }
        holder.userName.setText(message.getSender());
        holder.chatLine.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        if (chatLineList != null) {
            return chatLineList.size();
        } else {
            return 0;

        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView userName,chatLine;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName_recycle);
            chatLine = itemView.findViewById(R.id.chatline_recycle);
        }
    }

}
