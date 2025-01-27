package com.addictchat.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.addictchat.FirebaseMsging.MySingleTonClass;
import com.addictchat.R;
import com.addictchat.adapters.AutoCompleteAdapter;
import com.addictchat.adapters.AutoCompleteAdapter2;
import com.addictchat.adapters.CustomMessageAdapter;
import com.addictchat.adapters.MessageAdapter;
import com.addictchat.model.Message;
import com.addictchat.model.Messages;
import com.addictchat.model.UserID;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import id.zelory.compressor.Compressor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class MessageActivity extends AppCompatActivity {

  private DatabaseReference UserRef, ChatRequestRef, ContactsRef, NotificationRef;
  private String receiverUserID, senderUserID, Current_State,myUserId = "";

  private String currentState;

  private CircleImageView profile_image;
  private TextView txtUserName,tvOnlineStatus;

  private FirebaseAuth mAuth;
  private DatabaseReference databaseReference;
  private ProgressDialog chatWallaperProgressDialog;

  private Intent intent;

  private ImageButton btn_send;
  private EditText text_send;
  AutoCompleteTextView request, received;
  String[] reuests = {"Friends", "Love", "Family", "Business", "Others"};
  String[] acceptance = {"Accept", "Reject"};
  String login_unamne = null, unamne = null, uimageUrl = null,User_online_status = null,userID = null;
  String req_type = null, req_status = null;
  CardView reqcard;
  ConstraintLayout received_card;
  ArrayList<Message> msgs = new ArrayList<>();
  RecyclerView recyclerView;
  RelativeLayout main;
  boolean chatenabled = false;
  private ProgressBar verticalProgressBar;
  SharedPreferences sp;
  String myUserNo = "";
  /**
   * Uday
   */
  final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
  final private String serverKey = "key=" + "your_key";
  final private String contentType = "application/json";
  final String TAG = "NOTIFICATION TAG";

  String NOTIFICATION_TITLE = "Notification Title";
  String NOTIFICATION_MESSAGE = "Notification Message";
  // Reference to an image file in Firebase Storage
  StorageReference storageReference ;
  private DatabaseReference userDatabase;
  private FirebaseUser currentUser;
  private StorageReference imageStorageReference;
  private DatabaseReference userDatabase_Setting,friendsDatabase,baseUserDatabaseRef;
  private ImageButton attachImageBtn;
  private Button acceptBtn,rejectBtn;
  private TextView requestTypeTv;
  private RelativeLayout relativeLayout;


  private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;

  private TextView userName, userLastSeen;
  private CircleImageView userImage;

  private Toolbar ChatToolBar;
  private DatabaseReference RootRef;

  private ImageButton SendMessageButton, SendFilesButton;
  private EditText MessageInputText;

  private final List<Messages> messagesList = new ArrayList<>();
  private LinearLayoutManager linearLayoutManager;
  private MessageAdapter messageAdapter;
  private RecyclerView userMessagesList;


  private String saveCurrentTime, saveCurrentDate,checkrType = "",myUrl = "";
  private StorageTask uploadTask;
  private  Uri fileUri;
  private ProgressDialog progressDialog_sendImage;

  private String userStateCheck = "";


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);
    sp = getSharedPreferences("user_phone_no", Activity.MODE_PRIVATE);
    myUserNo = sp.getString("user_phone_no", "");
    //myUserId = sp.getString("user_id", "");
    Log.d("check_no", "onStart: " + myUserNo);
    intent = getIntent();
    login_unamne = myUserNo;
    unamne = intent.getStringExtra("userName");
    uimageUrl = intent.getStringExtra("userImage");
    userID = intent.getStringExtra("user_ID");
    userStateCheck = intent.getStringExtra("userState");

  //  User_online_status = "no";
    RootRef = FirebaseDatabase.getInstance().getReference();


   /* if(User_online_status.equals("")){

      User_online_status = "no";

    }else
    {
      User_online_status = intent.getStringExtra("User_online_status");
    }
*/

    currentState = "not_friends";

    mAuth = FirebaseAuth.getInstance();
    myUserId = mAuth.getCurrentUser().getUid();
    UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    UserRef.keepSynced(true);
    ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
    ContactsRef.keepSynced(true);
    //NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
    receiverUserID = myUserId;
    senderUserID = userID;

    mAuth = FirebaseAuth.getInstance();
    messageSenderID = mAuth.getCurrentUser().getUid();
    messageReceiverID = userID;
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    // getSupportActionBar().setSubtitle("onle");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });



    profile_image = (CircleImageView) findViewById(R.id.profile_image);
    txtUserName = (TextView) findViewById(R.id.tv_user_name);
    MessageInputText = (EditText) findViewById(R.id.edit_send);
    btn_send = (ImageButton) findViewById(R.id.btn_send);
    request = findViewById(R.id.request);
    reqcard = findViewById(R.id.request_layout);
    // received = findViewById(R.id.received);
    received_card = (ConstraintLayout) findViewById(R.id.received_layout);
    recyclerView = findViewById(R.id.recyclerView);
    main = findViewById(R.id.main_layout);
    verticalProgressBar = findViewById(R.id.vertical_progressbar);
    attachImageBtn = (ImageButton)findViewById(R.id.img_btn_attach);
    acceptBtn = (Button)findViewById(R.id.accept_btn);
    rejectBtn = (Button)findViewById(R.id.reject_btn);
    requestTypeTv = (TextView)findViewById(R.id.request_type_tv);
    tvOnlineStatus = (TextView)findViewById(R.id.tv_online_ref);
    relativeLayout= (RelativeLayout)findViewById(R.id.relativeLayout);

    baseUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
    baseUserDatabaseRef.keepSynced(true);
    progressDialog_sendImage =new ProgressDialog(this);
    updateUserStatus("online");
    displayLastSeen();
    RootRef.child("Users").child(messageSenderID).child("userState").child("state").setValue(userStateCheck);
    Log.d("state", "onClick: is "+userStateCheck);

      /*if(User_online_status.equals("yes")){

        tvOnlineStatus.setVisibility(View.VISIBLE);

        tvOnlineStatus.setText("online");


      }else{

        tvOnlineStatus.setText("offline");
        tvOnlineStatus.setVisibility(View.VISIBLE);

      }*/

    MessageInputText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

        // TODO Auto-generated method stub

        //tvOnlineStatus.setText("typing...");

       // UserRef.child(messageSenderID).child("userState").child("state").setValue("typing");


      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        // TODO Auto-generated method stub
       // UserRef.child(messageSenderID).child("userState").child("state").setValue("online");

      }

      @Override
      public void afterTextChanged(Editable s) {

        // TODO Auto-generated method stub
       // UserRef.child(messageSenderID).child("userState").child("state").setValue("online");

      }
    });



    baseUserDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
          String parent = childSnapshot.getKey();
          UserID users = childSnapshot.getValue(UserID.class);
          //get your User ID
          String user = users.getUser_phone_number();
          Log.i("ddf",parent);
         // Log.i("fddf",user);


        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
      });

      Query mQuery = baseUserDatabaseRef.child("Users").child("online");


    mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        final String key = dataSnapshot.getKey();

        Log.i("sss", dataSnapshot.getRef().toString());
        Log.i("sss2", key);
        Log.i("sss23", dataSnapshot.getRef().getParent().getKey());



      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }

    });




    final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(login_unamne).child("requests").child(unamne);
    final DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child(unamne).child("requests_sent").child(login_unamne);

    acceptBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        databaseReference3.child("req_status").setValue("Accept");
        databaseReference4.child("req_status").setValue("Accept");

        Toast.makeText(MessageActivity.this, "Accept!!!", Toast.LENGTH_SHORT).show();
        acceptChatRequest();
      }
    });


    rejectBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        databaseReference3.child("req_status").setValue("Reject");
        databaseReference4.child("req_status").setValue("Reject");

        Toast.makeText(MessageActivity.this, "Reject!!!", Toast.LENGTH_SHORT).show();
      }
    });



    //recyclerView.setAdapter(new CustomMessageAdapter(this, msgs));
    attachImageBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        CharSequence options[] = new CharSequence[]{
            "Images",
            "PDF Files",
            "Ms Word Files"

        };

        AlertDialog.Builder builder= new AlertDialog.Builder(MessageActivity.this);
        builder.setTitle("Select File");

        builder.setItems(options, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            if(which == 0){

              checkrType = "image";

              Intent intent =  new Intent();
              intent.setAction(Intent.ACTION_GET_CONTENT);
              intent.setType("image/*");
              startActivityForResult(intent.createChooser(intent,"Select image"),999);

              //What if it is image
            }
            if(which == 1){

              checkrType = "pdf";


            }
            if(which == 2){

              checkrType = "docx";


            }

          }
        });

        builder.show();

       /* CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(MessageActivity.this);*/
      }
    });
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    currentUser= FirebaseAuth.getInstance().getCurrentUser();
    String current_Uid=currentUser.getUid();

    mAuth=FirebaseAuth.getInstance();

    if(mAuth.getCurrentUser() != null)
    {

      userDatabase_Setting= FirebaseDatabase.getInstance().getReference().child("Wallpapers").child(current_Uid);
      userDatabase_Setting.keepSynced(true);

    }
    userDatabase_Setting.keepSynced(true);

    userDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_Uid);
    userDatabase.keepSynced(true);
    chatBackground();
    imageStorageReference= FirebaseStorage.getInstance().getReference();

    chatBackground();
    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference()
        .child(login_unamne).child("requests_sent").child(unamne);
    databaseReference2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        req_type = String.valueOf(dataSnapshot.child("req_type").getValue());
        req_status = String.valueOf(dataSnapshot.child("req_status").getValue());
        if (req_status.equals("Accept")) {
          received_card.setVisibility(View.GONE);
          verticalProgressBar.setVisibility(View.VISIBLE);
          reqcard.setVisibility(View.GONE);
          verticalProgressBar.setVisibility(View.VISIBLE);
          chatenabled = true;
        }

        //Toast.makeText(MessageActivity.this, req_type, Toast.LENGTH_SHORT).show();
        request.setAdapter(
            new AutoCompleteAdapter(MessageActivity.this, reuests, unamne, login_unamne,
                req_type, req_status, request,uimageUrl,userID,mAuth.getCurrentUser().getUid()));

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
        .child(login_unamne).child("requests_sent").child(unamne);


    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


        if (String.valueOf(dataSnapshot.child("req_status").getValue()).equals("Accept")) {
          received_card.setVisibility(View.GONE);
          verticalProgressBar.setVisibility(View.VISIBLE);
          reqcard.setVisibility(View.GONE);
          verticalProgressBar.setVisibility(View.VISIBLE);
          setBg(String.valueOf(dataSnapshot.child("req_type").getValue()));
          chatenabled = true;

        }

        Log.e("REQ_TYP", String.valueOf(dataSnapshot.child("req_type").getValue()));

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    FirebaseDatabase.getInstance().getReference().child(login_unamne).child("chats")
        .child(unamne).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
          chatenabled = true;
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
    FirebaseDatabase.getInstance().getReference().child(login_unamne).child("requests")
        .child(unamne).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if (String.valueOf(dataSnapshot.child("req_status").getValue()).equals("pending") || String.valueOf(dataSnapshot.child("req_status").getValue()).equals("Reject")) {
          //received.setText(String.valueOf(dataSnapshot.child("req_type").getValue()));
          requestTypeTv.setText(new StringBuilder("Request Type :  ").append(String.valueOf(dataSnapshot.child("req_type").getValue())));

          received_card.setVisibility(View.VISIBLE);
          reqcard.setVisibility(View.GONE);



        } else if (String.valueOf(dataSnapshot.child("req_status").getValue())
            .equals("Accept")) {
          received_card.setVisibility(View.GONE);
          verticalProgressBar.setVisibility(View.VISIBLE);
          reqcard.setVisibility(View.GONE);
          verticalProgressBar.setVisibility(View.VISIBLE);
          chatenabled = true;
          setBg(String.valueOf(dataSnapshot.child("req_type").getValue()));
        }
        Log.e("REQ_TYP", String.valueOf(dataSnapshot.child("req_type").getValue()));

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    /**
     * chat1
     *
    FirebaseDatabase.getInstance().getReference().child(login_unamne).child("chat")
        .child(unamne).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        msgs.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          if (snapshot.child("msg").getValue() != null) {
            Message message = new Message();
            message.setFrom((Boolean) snapshot.child("from").getValue());
            message.setMsg(snapshot.child("msg").getValue().toString());
            message.setTime(snapshot.child("time").getValue().toString());
            msgs.add(message);
          }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scrollToPosition(msgs.size() - 1);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
     */

    request.setAdapter(
        new AutoCompleteAdapter(MessageActivity.this, reuests, unamne, login_unamne, req_type,
            req_status, request,uimageUrl,userID,mAuth.getCurrentUser().getUid()));

    request.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (request.getAdapter() != null) {
          request.showDropDown();
          request.setText("Waiting");

          //Toast.makeText(MessageActivity.this,"dmkjh",Toast.LENGTH_SHORT).show();
        }
      }
    });

    /*    request.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                request.showDropDown();
            }
        });
*/
    request.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
        //Do whatever you want with selected object
        Toast.makeText(MessageActivity.this, "546", Toast.LENGTH_LONG).show();
        request.setText("Waiting");
        request.setEnabled(false);

      }
    });

    request.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(MessageActivity.this, "546", Toast.LENGTH_LONG).show();

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(MessageActivity.this, "5446", Toast.LENGTH_LONG).show();

      }
    });
    mAuth = FirebaseAuth.getInstance();

    final String userName = unamne;

    Log.e("userName--->", "" + userName);

    txtUserName.setText(userName);

    Picasso.with(MessageActivity.this).load(uimageUrl).placeholder(R.drawable.avtar_img)
        .into(profile_image);

    btn_send.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
       /* String msg = text_send.getText().toString();
        if (!msg.equals("")) {
          //sendMessage(mAuth.getUid(), userName, msg);
          sendMsg(msg);
        } else {
          Toast.makeText(MessageActivity.this, "You can't send empty message",
              Toast.LENGTH_SHORT).show();
        }
        text_send.setText("");*/

        SendMessagee();
        MessageInputText.setText("");

      }
    });



    messageAdapter = new MessageAdapter(messagesList);
    userMessagesList = (RecyclerView) findViewById(R.id.recyclerView);
    linearLayoutManager = new LinearLayoutManager(this);
    userMessagesList.setLayoutManager(linearLayoutManager);
    userMessagesList.setAdapter(messageAdapter);

    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
    saveCurrentDate = currentDate.format(calendar.getTime());

    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
    saveCurrentTime = currentTime.format(calendar.getTime());

    updateUserStatus("online");

    loadMessages();

  }

  private void setBg(String req_type) {
      /*  if (req_type.equals("Friends")) {
            main.setBackground(getResources().getDrawable(R.drawable.friendship));
        } else if (req_type.equals("Love")) {
            main.setBackground(getResources().getDrawable(R.drawable.love_bg));
            main.setBackground(getResources().getDrawable(R.drawable.love_bg));
        } else if (req_type.equals("Family")) {
            main.setBackground(getResources().getDrawable(R.drawable.family));
        } else if (req_type.equals("Business")) {
            main.setBackground(getResources().getDrawable(R.drawable.business));
        } else if (req_type.equals("Others")) {
            main.setBackground(getResources().getDrawable(R.drawable.other));
        }*/

  }

  private void sendMsg(String msg) {
    if (!chatenabled) {
   /*   relativeLayout.setVisibility(View.GONE);
      btn_send.setVisibility(View.GONE);*/
      Toast.makeText(MessageActivity.this, "You are not friends to chat", Toast.LENGTH_SHORT)
          .show();
      return;
    }

    String time = new SimpleDateFormat("dd MMM yyyy hh:mmaa").format(new Date());
    String time2 = new SimpleDateFormat("hh:mmaa").format(new Date());
    String timeimmilli = String.valueOf(new Date().getTime());
    databaseReference = FirebaseDatabase.getInstance().getReference().child(login_unamne)
        .child("chat").child(unamne).child(timeimmilli);
    databaseReference.child("from").setValue(true);
    databaseReference.child("time").setValue(time);
    databaseReference.child("msg").setValue(msg);
    databaseReference.child("type").setValue("text");

    databaseReference = FirebaseDatabase.getInstance().getReference().child(unamne)
        .child("chat").child(login_unamne).child(timeimmilli);

    databaseReference.child("from").setValue(false);
    databaseReference.child("time").setValue(time);
    databaseReference.child("msg").setValue(msg);
    databaseReference.child("type").setValue("text");

    Log.d("msg_time", "sendMsg: " + time);
    Log.d("msg_time2", "sendMsg: " + time2);

  }


  private void sendMessage(String sender, String receiver, String message) {

    databaseReference = FirebaseDatabase.getInstance().getReference();

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender", sender);
    hashMap.put("receiver", receiver);
    hashMap.put("message", message);
    databaseReference.child("Chats").push().setValue(hashMap);

  }


  private void sendNotification(JSONObject notification) {
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.i(TAG, "onResponse: " + response.toString());

          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Toast.makeText(MessageActivity.this, "Request error", Toast.LENGTH_LONG).show();
            Log.i(TAG, "onErrorResponse: Didn't work");
          }
        }) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", serverKey);
        params.put("Content-Type", contentType);
        return params;
      }
    };
    MySingleTonClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_chat_icons, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_audio_call) {

      Toast.makeText(this, "Audio call", Toast.LENGTH_SHORT).show();
      // Do something
      return true;
    }
    if (id == R.id.action_video_call) {

      // Do something
      Toast.makeText(this, "Video call", Toast.LENGTH_SHORT).show();
      return true;
    }

    if (id == R.id.wallpaper) {

      // Do something
      //  Toast.makeText(this, "Wallpaper", Toast.LENGTH_SHORT).show();
      //   main.setBackground(getResources().getDrawable(R.drawable.waves));
      CropImage.activity()
          .setGuidelines(CropImageView.Guidelines.ON)
          .start(MessageActivity.this);

      //chatBackground();
      return true;
    }

    return super.onOptionsItemSelected(item);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
      /*  if(requestCode==GALLERY_PICK && resultCode==RESULT_OK)
        {
            Uri imgUri=data.getData();
            CropImage.activity(imgUri)
                    .setAspectRatio(1,1)
                    .start(this);
            //Toast.makeText(SettingsActivity.this,imgUri,Toast.LENGTH_LONG).show();
        }*/
        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                File f = new File(getRealPathFromURI(resultUri));
                // bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                main.setBackground(d);
            }
        }*/


        if (requestCode == 999 && resultCode == RESULT_OK && data!= null && data.getData()!= null){


          progressDialog_sendImage.setTitle("Sending file");
          progressDialog_sendImage.setMessage("Please wait for a while");
          progressDialog_sendImage.setCanceledOnTouchOutside(false);
          progressDialog_sendImage.show();

          fileUri = data.getData();

          if(!checkrType.equals("image")){


          }else if(checkrType.equals("image")){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(" Chat Image Files");

            final String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
            final String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(messageSenderID).child(messageReceiverID).push();

            final String messagePushID = userMessageKeyRef.getKey();

            final StorageReference filePath = storageReference.child(messagePushID+"."+"jpg");

            uploadTask = filePath.putFile(fileUri);
            uploadTask.continueWithTask(new Continuation() {
              @Override
              public Object then(@NonNull Task task) throws Exception {

                if(!task.isSuccessful()){

                  throw task.getException();
                }
                return filePath.getDownloadUrl();
              }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
              @Override
              public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()){

                  Uri downloadUrl = task.getResult();
                  myUrl = downloadUrl.toString();

                  Map messageTextBody = new HashMap();
                  messageTextBody.put("message", myUrl);
                  messageTextBody.put("name", fileUri.getLastPathSegment());
                  messageTextBody.put("type", checkrType);
                  messageTextBody.put("from", messageSenderID);
                  messageTextBody.put("to", messageReceiverID);
                  messageTextBody.put("messageID", messagePushID);
                  messageTextBody.put("time", saveCurrentTime);
                  messageTextBody.put("date", saveCurrentDate);

                  Map messageBodyDetails = new HashMap();
                  messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                  messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

                  RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                      if (task.isSuccessful())
                      {
                        progressDialog_sendImage.dismiss();
                        Toast.makeText(MessageActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                      }
                      else
                      {
                        progressDialog_sendImage.dismiss();
                        Toast.makeText(MessageActivity.this, "Error", Toast.LENGTH_SHORT).show();
                      }
                      MessageInputText.setText("");
                    }
                  });
                }

              }
            });
          }else {

            progressDialog_sendImage.dismiss();
            Toast.makeText(this, "Nothing Selected, Error!!!", Toast.LENGTH_SHORT).show();
          }


        }
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        chatWallaperProgressDialog =new ProgressDialog(this);
        chatWallaperProgressDialog.setTitle("Setting wallpaper");
        chatWallaperProgressDialog.setMessage("Please wait for a while");
        chatWallaperProgressDialog.setCanceledOnTouchOutside(false);
        chatWallaperProgressDialog.show();
        Uri resultUri = result.getUri();
        final String current_user_id=currentUser.getUid();

        File thumb_filePath=new File(resultUri.getPath());

        Bitmap thumb_bitmap = null;
        try {
          thumb_bitmap = new Compressor(this)
              .setMaxWidth(200)
              .setMaxHeight(200)
              .setQuality(75)
              .compressToBitmap(thumb_filePath);
        } catch (IOException e) {
          e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        Drawable drawable = new BitmapDrawable(thumb_bitmap);
        main.setBackgroundDrawable(drawable);
        final byte[] thumb_byte=byteArrayOutputStream.toByteArray();

        final StorageReference thumb_filepath_ref=imageStorageReference.child("chat_wallpaper").child("thumbs").child(current_user_id+".jpg");

        final StorageReference filePath=imageStorageReference.child("chat_wallpaper").child(current_user_id+".jpg");


        Log.d("img","Image Upload Firebase id"+resultUri.toString());


        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<TaskSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<TaskSnapshot> task) {

            if(task.isSuccessful()){

              filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                  final String url_ = task.getResult().toString();

                  Log.d("file", "onComplete: "+task.getResult().toString()+"\n"+url_);

                  UploadTask uploadTask=thumb_filepath_ref.putBytes(thumb_byte);


                  uploadTask.addOnCompleteListener(new OnCompleteListener<TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<TaskSnapshot> task) {

                      //   Log.d("img","Image Upload Firebase id"+thumb_downloadUrl+".jpg");



                      Map updateHashmap=new HashMap<>();
                      updateHashmap.put("chat_wallpaper",url_);
                      //  updateHashmap.put("user_thumb_image",thumb_downloadUrl);


                      userDatabase_Setting.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()) {

                            chatWallaperProgressDialog.dismiss();

                            Toast.makeText(MessageActivity.this,"Success Uploading",Toast.LENGTH_LONG).show();


                          }

                        }
                      });

                    }
                  });

                }
              });

            }

          }
        });


       /*filePath.putFile(resultUri).continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
          @Override
          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
              throw task.getException();
            }
            // Continue with the task to get the download URL
            return filePath.getDownloadUrl();
          }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
          @Override
          public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
              Uri downloadUrl = task.getResult();
              chatWallaperProgressDialog.dismiss();
            } else {
              // Handle failures
              // ...
            }
          }
        });
*/


        Log.d("Addict","Image Upload Url"+resultUri.toString());
        Toast.makeText(MessageActivity.this,"Uri is"+resultUri.toString(),Toast.LENGTH_LONG).show();
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
      }
    }

  }

  private String getRealPathFromURI(Uri contentURI) {
    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
    if (cursor == null) { // Source is Dropbox or other similar local file path
      return contentURI.getPath();
    } else {
      cursor.moveToFirst();
      int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      return cursor.getString(idx);
    }
  }

  private void chatBackground(){

    userDatabase_Setting.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        try {
          String displayImageStr = "";
          displayImageStr=dataSnapshot.child("chat_wallpaper").getValue().toString();
          Picasso.with(MessageActivity.this).load(displayImageStr).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
              main.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
          });
        } catch (Exception e) {

          e.printStackTrace();
        }}

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }



                /*Picasso.with(MessageActivity.this).load(displayImageStr).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avtar_img).into(displayProfileImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError() {
                        Picasso.with(ProfileActivity.this).load(displayImageStr)
                            .placeholder(R.drawable.avtar_img).into(displayProfileImg);
                        progressDialog.dismiss();
                    }
                });*/
    });
  }

  private void acceptChatRequest()
  {
    ContactsRef.child(senderUserID).child(receiverUserID)
        .child("Contacts").setValue("Saved")
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task)
          {
            if (task.isSuccessful())
            {
              ContactsRef.child(receiverUserID).child(senderUserID)
                  .child("Contacts").setValue("Saved")
                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                      if (task.isSuccessful())
                      {
                       /* ChatRequestRef.child(senderUserID).child(receiverUserID)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task)
                              {
                                if (task.isSuccessful())
                                {
                                  ChatRequestRef.child(receiverUserID).child(senderUserID)
                                      .removeValue()
                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                          Current_State = "friends";

                                        }
                                      });
                                }
                              }
                            });*/
                      }
                    }
                  });
            }
          }
        });
  }




  private void SendMessagee()
  {

    if (!chatenabled) {
      /*   relativeLayout.setVisibility(View.GONE);
      btn_send.setVisibility(View.GONE);*/
      Toast.makeText(MessageActivity.this, "You are not friends to chat", Toast.LENGTH_SHORT)
          .show();
      return;
    }

    String messageText = MessageInputText.getText().toString();

    if (TextUtils.isEmpty(messageText))
    {
      Toast.makeText(this, "First write your message...", Toast.LENGTH_SHORT).show();
    }
    else
    {
      String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
      String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

      DatabaseReference userMessageKeyRef = RootRef.child("Messages")
          .child(messageSenderID).child(messageReceiverID).push();

      String messagePushID = userMessageKeyRef.getKey();

      Map messageTextBody = new HashMap();
      messageTextBody.put("message", messageText);
      messageTextBody.put("type", "text");
      messageTextBody.put("from", messageSenderID);
      messageTextBody.put("to", messageReceiverID);
      messageTextBody.put("messageID", messagePushID);
      messageTextBody.put("time", saveCurrentTime);
      messageTextBody.put("date", saveCurrentDate);

      Map messageBodyDetails = new HashMap();
      messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
      messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

      RootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
          if (databaseError != null)
          {

            Log.d("Message",databaseError.getMessage().toString());
            //UserRef.child(messageSenderID).child("userState").child("state").setValue("online");
            MessageInputText.setText("");

           // Toast.makeText(MessageActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
          }
        }
      });
      /*RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task)
        {
          if (task.isSuccessful())
          {
            UserRef.child(messageSenderID).child("userState").child("state").setValue("online");

            Toast.makeText(MessageActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
          }
          else
          {
            Toast.makeText(MessageActivity.this, "Error", Toast.LENGTH_SHORT).show();
          }
          MessageInputText.setText("");
        }
      });*/
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateUserStatus("online");

  }

  @Override
  protected void onStart() {
    super.onStart();
    updateUserStatus("online");

    //fetchMessages();
  }
  @Override
  protected void onStop() {
    super.onStop();
    if (currentUser != null)
    {
      updateUserStatus("offline");
    }
//    databaseReference.child("user_online_status").setValue("no");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

      updateUserStatus("offline");

  }

  @Override
  protected void onPause() {
    super.onPause();
    updateUserStatus("offline");

  }

  @Override
  protected void onRestart() {
    super.onRestart();
    //databaseReference.child("user_online_status").setValue("yes");
    updateUserStatus("online");

  }
  private void displayLastSeen()
  {
    RootRef.child("Users").child(messageReceiverID)
        .addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot)
          {
            if (dataSnapshot.child("userState").hasChild("state"))
            {
              String state = dataSnapshot.child("userState").child("state").getValue().toString();
              String date  = dataSnapshot.child("userState").child("date").getValue().toString();
              String time = dataSnapshot.child("userState").child("time").getValue().toString();

              if (state.equals("online"))
              {
                tvOnlineStatus.setText("online");
              }
              else if (state.equals("offline"))
              {
                tvOnlineStatus.setText("Last Seen: " + date + " " + time);
              }else if (state.equals("typing")){

                tvOnlineStatus.setText("typing...");

              }
            }
            else
            {
              tvOnlineStatus.setText("offline..");
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });
  }

  private void loadMessages(){

    RootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
        .addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s)
          {
            Messages messages = dataSnapshot.getValue(Messages.class);

            messagesList.add(messages);

            messageAdapter.notifyDataSetChanged();

            userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });
  }

  private void updateUserStatus(String state)
  {
    String saveCurrentTime, saveCurrentDate;

    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
    saveCurrentDate = currentDate.format(calendar.getTime());

    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
    saveCurrentTime = currentTime.format(calendar.getTime());

    HashMap<String, Object> onlineStateMap = new HashMap<>();
    onlineStateMap.put("time", saveCurrentTime);
    onlineStateMap.put("date", saveCurrentDate);
    onlineStateMap.put("state", state);

    RootRef.child("Users").child(messageSenderID).child("userState")
            .updateChildren(onlineStateMap);

  }
}


