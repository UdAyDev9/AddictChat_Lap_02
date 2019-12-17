package com.addictchat.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.addictchat.R;
import com.addictchat.activities.MessageActivity;
import com.addictchat.activities.UsersActivity;
import com.addictchat.activities.UsersActivity.UsersViewHolder;
import com.addictchat.model.Requests;
import com.addictchat.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */

public class RequestsFragment extends Fragment {

  private RecyclerView fReqList_recyclerView;
  private DatabaseReference fReqDatabase;
  private DatabaseReference usersDatabase;

  private FirebaseAuth auth;
  private String current_user_id;
  private View mainView;
  SharedPreferences sp;
  String myUserNo = "";


  public RequestsFragment() {
    // Required empty public constructor
  }

  public static RequestsFragment newInstance(String param1, String param2) {
    RequestsFragment fragment = new RequestsFragment();
    Bundle args = new Bundle();
      /*  args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mainView= inflater.inflate(R.layout.fragment_requests, container, false);
    auth=FirebaseAuth.getInstance();

    current_user_id=auth.getCurrentUser().getUid();

    fReqDatabase= FirebaseDatabase.getInstance().getReference().child("Friend_Req").child(current_user_id);
    fReqDatabase.keepSynced(true);

    fReqList_recyclerView=(RecyclerView)mainView.findViewById(R.id.req_friends_list);
    fReqList_recyclerView.setHasFixedSize(true);
    fReqList_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    sp = getContext().getSharedPreferences("user_phone_no", Activity.MODE_PRIVATE);
    myUserNo = sp.getString("user_phone_no", "");
    Log.d("check_no", "onStart: "+myUserNo);

    /*DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().child(myUserNo).child("requests");

    Log.d("ff", "onCreateView: "+FirebaseDatabase.getInstance().getReference().child(myUserNo).child("requests").getKey());*/


    usersDatabase= FirebaseDatabase.getInstance().getReference().child(myUserNo).child("requests");
    usersDatabase.keepSynced(true);


    return mainView;

  }

  @Override
  public void onStart() {
    super.onStart();

    usersDatabase= FirebaseDatabase.getInstance().getReference().child(myUserNo).child("requests");
//    usersDatabase.keepSynced(true);
//    Query queryRef = usersDatabase.orderByChild("req_status").equalTo("Accept");
//
//    queryRef.addChildEventListener(new ChildEventListener() {
//      @Override
//      public void onChildAdded(DataSnapshot snapshot, String previousChild) {
//        snapshot.getRef().setValue(null);
//      }
//
//      @Override
//      public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//      }
//
//      @Override
//      public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//      }
//
//      @Override
//      public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//      }
//
//      @Override
//      public void onCancelled(@NonNull DatabaseError databaseError) {
//
//      }
//    });
    sp = getContext().getSharedPreferences("user_phone_no", Activity.MODE_PRIVATE);
    myUserNo = sp.getString("user_phone_no", "");
    Log.d("check_no", "onStart: "+myUserNo);


    FirebaseRecyclerOptions<Requests> options =
        new FirebaseRecyclerOptions.Builder<Requests>()
            .setQuery(usersDatabase, Requests.class)
            .build();

    FirebaseRecyclerAdapter<Requests,RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Requests, RequestViewHolder>(options) {
      @Override
      protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position,
          @NonNull final Requests model) {

        holder.userName.setText(model.getUname());
        holder.userStatus.setText(model.getReq_type().concat("             ").concat(model.getTime()));

      /*  Picasso.with(getContext()).load(model.getUser_image()).networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.avtar_img).into(holder.userImage);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.putExtra("userName", model.getUname());
            intent.putExtra("login_unamne",model.getUname());
            intent.putExtra("userImage",model.getImg());
            intent.putExtra("user_ID",model.getUserId());
            startActivity(intent);
          }
        });
      }

      @NonNull
      @Override
      public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.req_single_row, parent, false);

        return new RequestViewHolder(myView);
      }
    };
    firebaseRecyclerAdapter.startListening();
    fReqList_recyclerView.setAdapter(firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.notifyDataSetChanged();
   /* FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {

      usersDatabase.child("online").setValue(true);
      //onlineSymbol.setVisibility(View.VISIBLE);

    }*/
  }

  public  static class RequestViewHolder extends RecyclerView.ViewHolder
  {
    View mView;

    TextView userName,userStatus;
    CircleImageView userImage;

    public RequestViewHolder(View itemView) {
      super(itemView);
      mView=itemView;
      userName=  mView.findViewById(R.id.req_user_name);
      userStatus= mView.findViewById(R.id.req_type);
      userImage = mView.findViewById(R.id.req_circle_image);

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
