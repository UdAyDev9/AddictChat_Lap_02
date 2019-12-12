package com.addictchat.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.addictchat.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements OnClickListener {

  Toolbar profileToolbar;
  BottomSheetDialog bottomSheetDialog;
  EditText statusEt;
  TextView textView,statusTv,displayName,displayPhone,allertModeTv;
  Button btn,statusSaveBtn,changeProfileBtn;
  ImageButton editstausImgBtn,editUserNameImgBtn,editAlertModeImgBtn;
  public  String statusStr = "";
  KeyboardUtil keyboardUtil;
  ConstraintLayout constraintLayout ;

  private FirebaseUser currentUser;
  public String current_Uid="";
  CircleImageView displayProfileImg;
  private ProgressDialog progressDialog;
  private ProgressDialog progressDialog_ChangeProfile;
  private DatabaseReference databaseReference;
  private DatabaseReference userDatabase;
  private StorageReference imageStorageReference;
  private DatabaseReference userDatabase_Setting;
  private FirebaseAuth mAuth;
  final Context c = this;
  private  String displayNameSTR = "",displayStatusSTR = "";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    profileToolbar = (findViewById(R.id.profile_toolbar));
    editstausImgBtn = findViewById(R.id.edtimageButton);
    changeProfileBtn = findViewById(R.id.change_profile_btn);
    editUserNameImgBtn = findViewById(R.id.editUserNameImgBtn);
    editAlertModeImgBtn = findViewById(R.id.alert_mode_img_btn);
    displayName = findViewById(R.id.displayName);
    displayPhone = findViewById(R.id.displayPhone);
    statusTv = findViewById(R.id.statusTv);
    allertModeTv = findViewById(R.id.alert_mode_tv);
    displayProfileImg = findViewById(R.id.displayProfileImage);
    setSupportActionBar(profileToolbar);
    getSupportActionBar().setTitle("Settings");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    editstausImgBtn.setOnClickListener(this);
    changeProfileBtn.setOnClickListener(this);
    editUserNameImgBtn.setOnClickListener(this);
    editAlertModeImgBtn.setOnClickListener(this);
    displayProfileImg.setOnClickListener(this);

    progressDialog=new ProgressDialog(this);
    progressDialog.setTitle("Loading...");
    progressDialog.setMessage("Please Wait");
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    currentUser= FirebaseAuth.getInstance().getCurrentUser();
    String current_Uid=currentUser.getUid();

    imageStorageReference= FirebaseStorage.getInstance().getReference();

    userDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_Uid);
    userDatabase.keepSynced(true);

    Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();


    mAuth=FirebaseAuth.getInstance();
    if(mAuth.getCurrentUser() != null)
    {

      userDatabase_Setting= FirebaseDatabase.getInstance().getReference().child("Users").child(current_Uid);

    }

    userDatabase_Setting.keepSynced(true);

    userDatabase_Setting.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    userDatabase.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        String displayNameStr=dataSnapshot.child("user_name").getValue().toString();
        String displayPhoneStr=dataSnapshot.child("user_phone").getValue().toString();
        String displayStatusStr=dataSnapshot.child("user_status").getValue().toString();
        final String displayImageStr=dataSnapshot.child("user_image").getValue().toString();


        displayName.setText(displayNameStr);
        displayPhone.setText(displayPhoneStr);
        statusTv.setText(displayStatusStr);
        displayNameSTR = displayNameStr;
        displayStatusSTR = displayStatusStr;



        Picasso.with(ProfileActivity.this).load(displayImageStr).networkPolicy(NetworkPolicy.OFFLINE)
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
        });
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

  }

  private void createBottomSheetDialog() {
    if (bottomSheetDialog == null) {
      View view = LayoutInflater.from(this).inflate(R.layout.sample_layout, null);
      statusEt = view.findViewById(R.id.etStatus);
      statusSaveBtn = view.findViewById(R.id.btnSavePhone);
      constraintLayout = view.findViewById(R.id.bottom_sheet);
      statusSaveBtn.setOnClickListener(this);
      bottomSheetDialog = new BottomSheetDialog(this);
      bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
      bottomSheetDialog.setContentView(view);
      bottomSheetDialog.show();
      keyboardUtil =   new KeyboardUtil(ProfileActivity.this, constraintLayout);


    }
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()){

      case R.id.btnSavePhone:
        statusStr = statusEt.getText().toString();
        statusTv.setText(statusStr);
        bottomSheetDialog.dismiss();

        break;
      case R.id. edtimageButton:
        statusSpinner();
        //displaystatusUpdate();
        //createBottomSheetDialog();

        break;

      case R.id.change_profile_btn:
        updateUser();
        Toast.makeText(ProfileActivity.this,"Profile Change",Toast.LENGTH_SHORT).show();
        break;

      case R.id.editUserNameImgBtn:
        displayNameUpdate();
       // Toast.makeText(ProfileActivity.this,"User Name Change",Toast.LENGTH_SHORT).show();
        break;

      case R.id.alert_mode_img_btn:
              alertModeSpinner();
             // Toast.makeText(ProfileActivity.this,"User Name Change",Toast.LENGTH_SHORT).show();
              break;

        case R.id.displayProfileImage:
        //Toast.makeText(ProfileActivity.this,"User Name Change",Toast.LENGTH_SHORT).show();
          CropImage.activity()
              .setGuidelines(CropImageView.Guidelines.ON)
              .start(ProfileActivity.this);
          break;

      default:
        break;

    }

  }

  private void displayNameUpdate(){

  /*  AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
    alertDialog.setTitle("Display Name");*/
    //alertDialog.setMessage("Enter Password");
    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
    View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
    alertDialogBuilderUserInput.setView(mView);

    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
    userInputDialogEditText.setText(displayNameSTR);
    userInputDialogEditText.setSelection(userInputDialogEditText.getText().length());
    alertDialogBuilderUserInput
        .setCancelable(false)
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogBox, int id) {
            // ToDo get user input here
             final ProgressDialog progressDialog_updateName;
            progressDialog_updateName=new ProgressDialog(c);
            progressDialog_updateName.setTitle("Saving Changes");
            progressDialog_updateName.setMessage("Please wait for a while");
            progressDialog_updateName.show();
            userDatabase.child("user_name").setValue(userInputDialogEditText.getText().toString()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {

                    progressDialog_updateName.dismiss();

                  }
                });
          }
        })

        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
              }
            });

    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
    alertDialogAndroid.show();
  }

  private void displaystatusUpdate(){

  /*  AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
    alertDialog.setTitle("Display Name");*/
    //alertDialog.setMessage("Enter Password");
    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
    View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
    alertDialogBuilderUserInput.setView(mView);

    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
    userInputDialogEditText.setText(displayStatusSTR);
    userInputDialogEditText.setSelection(userInputDialogEditText.getText().length());
    alertDialogBuilderUserInput
        .setCancelable(false)
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogBox, int id) {
            // ToDo get user input here
            final ProgressDialog progressDialog_updateName;
            progressDialog_updateName=new ProgressDialog(c);
            progressDialog_updateName.setTitle("Saving Changes");
            progressDialog_updateName.setMessage("Please wait for a while");
            progressDialog_updateName.show();
            userDatabase.child("user_status").setValue(userInputDialogEditText.getText().toString()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {

                    progressDialog_updateName.dismiss();

                  }
                });
          }
        })

        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
              }
            });

    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
    alertDialogAndroid.show();
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
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        progressDialog_ChangeProfile =new ProgressDialog(this);
        progressDialog_ChangeProfile.setTitle("Uploading Image");
        progressDialog_ChangeProfile.setMessage("Please wait for a while");
        progressDialog_ChangeProfile.setCanceledOnTouchOutside(false);
        progressDialog_ChangeProfile.show();
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
        final byte[] thumb_byte=byteArrayOutputStream.toByteArray();





        final StorageReference thumb_filepath_ref=imageStorageReference.child("profile_images").child("thumbs").child(current_user_id+".jpg");

        final StorageReference filePath=imageStorageReference.child("profile_images").child(current_user_id+".jpg");


        Log.d("img","Image Upload Firebase id"+resultUri.toString());


        /*filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            if(task.isSuccessful())
            {


//.On 02-11-19

              final String download_url= imageStorageReference.child("profile_images").child(current_user_id+".jpg").getDownloadUrl().toString();

              UploadTask uploadTask=thumb_filepath_ref.putBytes(thumb_byte);

              uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                  String thumb_downloadUrl=imageStorageReference.child("profile_images").child("thumbs").child(current_user_id+".jpg").getDownloadUrl().toString();
                  if(thumb_task.isSuccessful())
                  {

                    Log.d("img","Image Upload Firebase id"+thumb_downloadUrl+".jpg");

                    Map updateHashmap=new HashMap<>();
                    updateHashmap.put("user_image",download_url);
                    updateHashmap.put("user_thumb_image",thumb_downloadUrl);


                    userDatabase_Setting.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                          progressDialog_ChangeProfile.dismiss();
                          Toast.makeText(ProfileActivity.this,"Success Uploading",Toast.LENGTH_LONG).show();


                        }

                      }
                    });
                  }
                  else
                  {
                    Toast.makeText(ProfileActivity.this,"Error in uploading thumbnail",Toast.LENGTH_SHORT).show();
                    progressDialog_ChangeProfile.dismiss();

                  }

                }
              });



            }
            else
            {
              Toast.makeText(ProfileActivity.this,"Error in uploading image",Toast.LENGTH_SHORT).show();
              progressDialog_ChangeProfile.dismiss();
            }
          }
        });*/

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
                      updateHashmap.put("user_image",url_);
                    //  updateHashmap.put("user_thumb_image",thumb_downloadUrl);


                      userDatabase_Setting.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()) {

                            progressDialog_ChangeProfile.dismiss();

                            Toast.makeText(ProfileActivity.this,"Success Uploading",Toast.LENGTH_LONG).show();


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
              progressDialog_ChangeProfile.dismiss();
            } else {
              // Handle failures
              // ...
            }
          }
        });
*/


        Log.d("Addict","Image Upload Url"+resultUri.toString());
        Toast.makeText(ProfileActivity.this,"Uri is"+resultUri.toString(),Toast.LENGTH_LONG).show();
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
      }
    }

  }
  private void updateUser() {

    FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
    String uid=currentUser.getUid();
    databaseReference= FirebaseDatabase.getInstance().getReference().child(uid).child("Info");

    HashMap<String,String> userMAp=new HashMap<>();
    userMAp.put("name","Krish");
    userMAp.put("status","Hey! there,I'm using Addict chat ");
    userMAp.put("image","default");
    userMAp.put("thumb_image","default");
    databaseReference.setValue(userMAp).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {


          //progressDialog.dismiss();
          Toast.makeText(ProfileActivity.this, "Welcome " + "", Toast.LENGTH_LONG).show();

        }
      }
    });

  }

  private void statusSpinner(){

    final String[] listStatuses = {"Sleeping", "Bike", "Available","Busy","At work", "Message only"};

    AlertDialog.Builder builder = new AlertDialog.Builder(c);
    builder.setTitle("Choose Status");

    int checkedItem = 0; //this will checked the item when user open the dialog
    builder.setSingleChoiceItems(listStatuses, checkedItem, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(final DialogInterface dialog, int which) {

        final ProgressDialog progressDialog_updateName;
        progressDialog_updateName=new ProgressDialog(c);
        progressDialog_updateName.setTitle("Saving Changes");
        progressDialog_updateName.setMessage("Please wait for a while");
        progressDialog_updateName.show();
        userDatabase.child("user_status").setValue(listStatuses[which]).addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {

                progressDialog_updateName.dismiss();
                dialog.dismiss();
              }
            });

     //   Toast.makeText(c, "Position: " + which + " Value: " + listStatuses[which], Toast.LENGTH_LONG).show();
      }
    });

   /* builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

        //Toast.makeText(c, "Position: " + which + " Value: " + listStatuses[which], Toast.LENGTH_LONG).show();
        dialog.dismiss();
      }
    });*/

    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       dialog.cancel();
      }
    });

    AlertDialog dialog = builder.create();
    dialog.show();

  }

  private void alertModeSpinner(){

     final String[] listModes = {"Vibrate","Ringing", "Silent"};

    AlertDialog.Builder builder = new AlertDialog.Builder(c);
    builder.setTitle("Choose Mode");

    int checkedItemm = 0; //this will checked the item when user open the dialog
    builder.setSingleChoiceItems(listModes, checkedItemm, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(final DialogInterface dialog, int which) {

     /*   final ProgressDialog progressDialog_updateName;
        progressDialog_updateName=new ProgressDialog(c);
        progressDialog_updateName.setTitle("Saving Changes");
        progressDialog_updateName.setMessage("Please wait for a while");*/
     //   progressDialog_updateName.show();
        /*userDatabase.child("user_status").setValue(listStatuses[which]).addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {

                progressDialog_updateName.dismiss();
                dialog.dismiss();
              }
            });*/



        //  Toast.makeText(c, "Position: " + which + " Value: " + listModes[which], Toast.LENGTH_LONG).show();
        dialog.dismiss();
        allertModeTv.setText(listModes[which]);


      }
    });

  /*  builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      //  allertModeTv.setText(listModes[which]);
          Toast.makeText(c, "Position: " + which + " Value: " + listModes[which], Toast.LENGTH_LONG).show();

        dialog.dismiss();
      }
    });*/

    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    AlertDialog dialog = builder.create();
    dialog.show();

  }


}
