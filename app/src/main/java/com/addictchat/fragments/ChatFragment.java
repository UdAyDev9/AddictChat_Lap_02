package com.addictchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.addictchat.R;
import com.addictchat.activities.MessageActivity;
import com.addictchat.adapters.ChatFragmentListAdapter;
import com.addictchat.model.Contact;
import com.addictchat.model.User;
import com.addictchat.model.Users;
import com.addictchat.widgets.RoundedImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {


    private View view;

    private DatabaseReference databaseReference, userRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private RecyclerView recyclerView;

    private List<User> userList;

    private ChatFragmentListAdapter chatFragmentListAdapter;
    String uname=null;

    private DatabaseReference ChatsRef, UsersRef;
    private View PrivateChatsView;





    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.fragment_chat, container, false);
        PrivateChatsView = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatsRef.keepSynced(true);
        UsersRef.keepSynced(true);

        recyclerView = (RecyclerView) PrivateChatsView.findViewById(R.id.chats_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        return PrivateChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
            new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(ChatsRef, Users.class)
                .build();


        FirebaseRecyclerAdapter<Users, ChatsViewHolder> adapter =
            new FirebaseRecyclerAdapter<Users, ChatsViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position,
                    @NonNull Users model) {
                    final String usersIDs = getRef(position).getKey();
                    final String[] retImage = {"default_image"};

                    UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists())
                            {
                                if (dataSnapshot.hasChild("user_image"))
                                {
                                    retImage[0] = dataSnapshot.child("user_image").getValue().toString();
                                    Picasso.with(getContext()).load(retImage[0]).placeholder(R.drawable.avtar_img).into(holder.profileImage);
                                }

                                final String retName = dataSnapshot.child("user_name").getValue().toString();
                                final String retStatus = dataSnapshot.child("user_status").getValue().toString();

                                holder.userName.setText(retName);
                                holder.userStatus.setText(retStatus);


                                if (dataSnapshot.child("userState").hasChild("state"))
                                {
                                    String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                    String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                    String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                    if (state.equals("online"))
                                    {
                                      //  holder.userStatus.setText("online");
                                        holder.greenDotView.setVisibility(View.VISIBLE);
                                    }
                                    else if (state.equals("offline"))
                                    {
                                        holder.userStatus.setText("Last Seen: " + date + " " + time);
                                    }
                                }
                                else
                                {
                                    holder.userStatus.setText("offline");
                                }

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent chatIntent = new Intent(getContext(), MessageActivity.class);
                                        chatIntent.putExtra("user_ID", usersIDs);
                                        chatIntent.putExtra("userName", retName);
                                        chatIntent.putExtra("userImage", retImage[0]);
                                        startActivity(chatIntent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }



                @NonNull
                @Override
                public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout, viewGroup, false);
                    return new ChatsViewHolder(view);
                }
            };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class  ChatsViewHolder extends RecyclerView.ViewHolder
    {
        RoundedImageView profileImage;
        TextView userStatus, userName;
        ImageView greenDotView;


        public ChatsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.rounded_iv_profile);
            userStatus = itemView.findViewById(R.id.tvStatus);
            userName = itemView.findViewById(R.id.tvName);
            greenDotView = itemView.findViewById(R.id.online_green_iv);
        }
    }
}
