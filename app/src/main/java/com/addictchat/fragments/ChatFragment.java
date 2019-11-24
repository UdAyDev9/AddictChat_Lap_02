package com.addictchat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.addictchat.R;
import com.addictchat.adapters.ChatFragmentListAdapter;
import com.addictchat.model.Contact;
import com.addictchat.model.User;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        /*mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);*/

        /*mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");*/

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);

        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = (RecyclerView) view.findViewById(R.id.chats_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.e("snapshot--->",""+snapshot);

                    String userId = currentUserId;

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    String userName = snapshot.child("user_name").getValue().toString();
                    String userStatus = snapshot.child("user_status").getValue().toString();

                    Log.e("userId--->",""+firebaseUser.getUid());
                    Log.e("userName--->",""+userName);
                    Log.e("userStatus--->",""+userStatus);

                    User user = new User(userName, userStatus);

                    userList.add(user);

                }

                chatFragmentListAdapter = new ChatFragmentListAdapter(getActivity(), userList);
                recyclerView.setAdapter(chatFragmentListAdapter);
                chatFragmentListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.e("snapshot--->",""+snapshot);

                    String userId = currentUserId;

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if(!snapshot.getKey().equals(currentUserId)) {
                        String userName = snapshot.child("user_name").getValue().toString();
                        String userStatus = snapshot.child("user_status").getValue().toString();
                        String userProfileImage = snapshot.child("user_image").getValue().toString();

                        Log.e("userId--->",""+firebaseUser.getUid());
                        Log.e("userName--->",""+userName);
                        Log.e("userStatus--->",""+userStatus);
                        Log.e("userProfileImage--->",""+userProfileImage);

                        User user = new User(userName, userStatus,userProfileImage);

                        userList.add(user);
                    }else {
                        uname=snapshot.child("user_name").getValue().toString();
                    }

                }

                chatFragmentListAdapter = new ChatFragmentListAdapter(getActivity(), userList,uname);
                recyclerView.setAdapter(chatFragmentListAdapter);
                chatFragmentListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /* @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(databaseReference, User.class)
                .build();

        FirebaseRecyclerAdapter<User, ChatListViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<User, ChatListViewHolder>(options)
        {
            @NonNull
            @Override
            public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                return new ChatListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ChatListViewHolder holder, int position, @NonNull final User model) {

                //final String userIds = getRef(position).getKey();

                databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("user_name").getValue().toString();
                        String userStatus = dataSnapshot.child("user_status").getValue().toString();

                        holder.userName.setText(userName);
                        holder.userStatus.setText(userStatus);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView profileImage;
        TextView userName, userStatus;

        public ChatListViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tvName);
            userStatus = itemView.findViewById(R.id.tvStatus);
        }
    }*/
}
