package com.addictchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.addictchat.R;
import com.addictchat.activities.MessageActivity;
import com.addictchat.activities.ProfileActivity;
import com.addictchat.model.User;

import com.addictchat.widgets.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ChatFragmentListAdapter extends RecyclerView.Adapter<ChatFragmentListAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    String uname;

    public ChatFragmentListAdapter(Context context, List<User> userList, String uname) {
        this.context = context;
        this.userList = userList;
        this.uname=uname;
    }


    @NonNull
    @Override
    public ChatFragmentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatFragmentListAdapter.ViewHolder holder, int position) {

        final User user = userList.get(position);
        holder.userName.setText(user.getUserName());
        holder.userStatus.setText(user.getUserStatus());
        if (user.getUser_online_status().equals("yes")){

            holder.onlineGreenDot.setVisibility(View.VISIBLE);
        }else {
            holder.onlineGreenDot.setVisibility(View.INVISIBLE);
        }
        Picasso.with(context).load(user.getUserImage()).placeholder(R.drawable.avtar_img).into(holder.userProfileImage, new Callback() {
            @Override
            public void onSuccess() {
               // progressDialog.dismiss();

            }

            @Override
            public void onError() {
              /*  Picasso.with(context).load(user.getUserImage())
                    .placeholder(R.drawable.avtar_img).into(holder.);
                progressDialog.dismiss();*/


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userName", user.getUserName());
                intent.putExtra("login_unamne",uname);
                intent.putExtra("userImage",user.getUserImage());
                intent.putExtra("User_online_status",user.getUser_online_status());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userStatus;
        public RoundedImageView userProfileImage;
        public ImageView onlineGreenDot;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.tvName);
            userStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            userProfileImage = (RoundedImageView) itemView.findViewById(R.id.rounded_iv_profile);
            onlineGreenDot = (ImageView) itemView.findViewById(R.id.online_green_iv);


        }
    }
}
