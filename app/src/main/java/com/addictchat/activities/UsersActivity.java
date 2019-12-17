package com.addictchat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.addictchat.R;
import com.addictchat.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;

    private DatabaseReference userRef;

    private FirebaseAuth mAuth;

    private ImageView onlineSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mToolbar=(Toolbar)findViewById(R.id.all_users_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  onlineSymbol=(ImageView) findViewById(R.id.onlineSymbol);

        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser()!=null)
        {
            userRef = FirebaseDatabase
                .getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());


            //ii
        }

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }





    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            userRef.child("online").setValue(ServerValue.TIMESTAMP);
            // onlineSymbol.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Users> options =
            new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(databaseReference, Users.class)
                .build();
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position,
                @NonNull final Users model) {

                holder.userName.setText(model.getUser_name());
                holder.userStatus.setText(model.getUser_status());

                Picasso.with(UsersActivity.this).load(model.getUser_image()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avtar_img).into(holder.userImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UsersActivity.this, MessageActivity.class);
                        intent.putExtra("userName", model.getUser_name());
                        intent.putExtra("login_unamne",model.getUser_phone());
                        intent.putExtra("userImage",model.getUser_image());
                        intent.putExtra("user_ID",model.getUser_id());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_row, parent, false);

                return new UsersViewHolder(myView);
            }
        };


        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            userRef.child("online").setValue(true);
            //onlineSymbol.setVisibility(View.VISIBLE);

        }
    }

    public  static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        TextView userName,userStatus;
        CircleImageView userImage;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            userName=  mView.findViewById(R.id.user_single_display_name);
            userStatus= mView.findViewById(R.id.user_single_status);
            userImage = mView.findViewById(R.id.circleImageView);

        }

        public void setName(String name) {
            TextView userNameView=mView.findViewById(R.id.user_single_display_name);
            userNameView.setText(name);
        }

        public void setSatus(String satus) {
            TextView userStatusView=mView.findViewById(R.id.user_single_status);
            userStatusView.setText(satus);
        }

        public void setUserImage(final String thumb_image, final Context context) {
            final CircleImageView thumb_imageView=mView.findViewById(R.id.circleImageView);

            //  imageView.setImageURI.(image);
            // Picasso.with(context).load(thumb_image).placeholder(R.drawable.profile_avatar).into(thumb_imageView);
            Picasso.with(context).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.avtar_img).into(thumb_imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(thumb_image).placeholder(R.drawable.avtar_img).into(thumb_imageView);
                }
            });
        }

    }
}