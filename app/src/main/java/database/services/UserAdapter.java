package database.services;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledev2022.R;
import database.patient.Patient;

import java.util.List;
public class UserAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Patient> userList;
    private Patient user;
    public UserAdapter(List<Patient> userList) {this.userList = userList;}

    public Patient getUser() {
        return user;
    }

    public void setUser(Patient user) {
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ic_container_user,parent,false);
        return new UserView(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        addUserToList((UserView) holder,position);
    }
    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }
    private void addUserToList(UserView viewHolder, int position) {
        Patient user = userList.get(position);
        Bitmap bitmap = decodeIamge(user.getImage());
        viewHolder.avatar.setImageBitmap(bitmap);
        viewHolder.userName.setText(user.getFirstName());
        viewHolder.email.setText(user.getEmail());
    }
    class UserView extends RecyclerView.ViewHolder{
        private TextView userName,email;
        private ImageView avatar;
        public UserView(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.recylerUserName);
            email = itemView.findViewById(R.id.recyleEmail);
            avatar = itemView.findViewById(R.id.recylerAvatar);
        }
    }
    public Bitmap decodeIamge(String imageBitmap) {
        byte[] bytes = Base64.decode(imageBitmap,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
