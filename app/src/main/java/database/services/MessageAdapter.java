package database.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobiledev2022.R;
import database.Entity.Message;
import java.util.List;
public class MessageAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private String image ;
    private List<Message> chatLineList;
     public MessageAdapter(List<Message> messageList) {
        this.chatLineList = messageList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatline, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_loading, parent, false);
            return new MessageViewLoading(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder) {
            populateChatLinesRows((MessageViewHolder) holder, position);
        } else if (holder instanceof MessageViewLoading) {
            showLoadingView((MessageViewLoading) holder, position);
        }
    }
    private void populateChatLinesRows(MessageViewHolder viewHolder, int position) {
        Message item = chatLineList.get(position);
        viewHolder.avatarChatLine.setImageBitmap(decodeIamge(item.getImageSender()));
        viewHolder.chatLine.setText(item.getMessage());
        viewHolder.userName.setText(item.getDate());
    }
    @Override
    public int getItemCount() {
        return chatLineList == null ? 0 : chatLineList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatLineList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView userName,chatLine;
        private ImageView avatarChatLine;
        public MessageViewHolder(@NonNull View itemView) {

            super(itemView);
            avatarChatLine = itemView.findViewById(R.id.avatarChatLine);
            userName = itemView.findViewById(R.id.userName_recycle);
            chatLine = itemView.findViewById(R.id.chatline_recycle);
        }
    }
    private void showLoadingView(MessageViewLoading viewHolder, int position) {
        viewHolder.progressBar.getContext();
    }
    public class MessageViewLoading extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public MessageViewLoading(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    public Bitmap decodeIamge(String imageBitmap) {
        byte[] bytes = Base64.decode(imageBitmap,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}