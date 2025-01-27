package com.addictchat.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
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
import com.addictchat.model.Users;
import com.addictchat.widgets.RoundedImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment
{
    private View ContactsView;
    private RecyclerView myContactsList;

    private DatabaseReference ContacsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    SharedPreferences sp;
    String myUserNo = "";

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_contacts, container, false);


        myContactsList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        sp = getContext().getSharedPreferences("user_phone_no", Activity.MODE_PRIVATE);
        myUserNo = sp.getString("user_phone_no", "");
        ContacsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        return ContactsView;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options =
            new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(ContacsRef, Users.class)
                .build();


        final FirebaseRecyclerAdapter<Users, ContactsViewHolder> adapter
            = new FirebaseRecyclerAdapter<Users, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position,
                @NonNull Users model) {
                final String userIDs = getRef(position).getKey();

                UsersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Log.i("sff", "onDataChange: yes");
                        if (dataSnapshot.exists())
                        {
                            if (dataSnapshot.child("userState").hasChild("state"))
                            {
                                String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                if (state.equals("online"))
                                {
                                    holder.onlineIcon.setVisibility(View.VISIBLE);
                                }
                                else if (state.equals("offline"))
                                {
                                    holder.onlineIcon.setVisibility(View.INVISIBLE);
                                }
                            }
                            else
                            {
                                holder.onlineIcon.setVisibility(View.INVISIBLE);
                            }


                            if (dataSnapshot.hasChild("user_image"))
                            {
                                String userImage = dataSnapshot.child("user_image").getValue().toString();
                                String profileName = dataSnapshot.child("user_name").getValue().toString();
                                String profileStatus = dataSnapshot.child("user_status").getValue().toString();

                                Log.d("fg", "onDataChange: "+profileName);
                                holder.userName.setText(profileName);
                                holder.userStatus.setText(profileStatus);
                                Picasso.with(getContext()).load(userImage).placeholder(R.drawable.avtar_img).into(holder.profileImage);
                            }
                            else
                            {
                                String profileName = dataSnapshot.child("user_name").getValue().toString();
                                String profileStatus = dataSnapshot.child("user_status").getValue().toString();
                                Log.d("fg2", "onDataChange: "+profileName);

                                holder.userName.setText(profileName);
                                holder.userStatus.setText(profileStatus);
                            }
                        }
                        Log.i("sf22f", "onDataChange: yes");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout, viewGroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        myContactsList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        RoundedImageView profileImage;
        ImageView onlineIcon;


        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.tvName);
            userStatus = itemView.findViewById(R.id.tvStatus);
            profileImage = itemView.findViewById(R.id.rounded_iv_profile);
            onlineIcon = (ImageView) itemView.findViewById(R.id.online_green_iv);
        }
    }
}