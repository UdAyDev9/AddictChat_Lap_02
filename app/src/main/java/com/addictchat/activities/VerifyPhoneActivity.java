package com.addictchat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.addictchat.R;
import com.addictchat.custom.OtpView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private Button btnSignIn;
    private OtpView otpView;

    private DatabaseReference databaseReference;

    private String phonenumber;
    private DatabaseReference RootRef;
    private boolean haveUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        RootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        otpView = (OtpView) findViewById(R.id.editTextCode);

        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        btnSignIn = (Button) findViewById(R.id.buttonSignIn);

        //otpView.setOtpCompletionListener(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otpView.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    otpView.setError("Enter code...");
                    otpView.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }



    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                       /* String currentUserId = mAuth.getCurrentUser().getUid();
                        String deviveTokenId = FirebaseInstanceId.getInstance().getToken();
                        SharedPreferences sp = getSharedPreferences("user_phone_no", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("user_phone_no", phonenumber);
                        editor.putString("user_id", currentUserId);
                        editor.commit();*/
                      /*
                       // verifyUserExistance(mAuth.getCurrentUser().getUid());
                        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if ((dataSnapshot.child("user_name").exists()))
                                {
                                    haveUser = true;
                                    Toast.makeText(VerifyPhoneActivity.this, "Exists...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //SendUserToSettingsActivity();
                                    haveUser = false;

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

*/
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        String deviveTokenId = FirebaseInstanceId.getInstance().getToken();
                      SharedPreferences sp = getSharedPreferences("user_phone_no", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("user_phone_no", phonenumber);
                        editor.putString("user_id", currentUserId);
                        editor.commit();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("Users").child(mAuth.getCurrentUser().getUid()).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    // use "username" already exists
                                    // Let the user know he needs to pick another username.
                                    Intent intent = new Intent(VerifyPhoneActivity.this, DashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    // User does not exist. NOW call createUserWithEmailAndPassword
                                    // Your previous code here.
                                    String currentUserId = mAuth.getCurrentUser().getUid();
                                    String deviveTokenId = FirebaseInstanceId.getInstance().getToken();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                                    //databaseReference.child("Users").child(currentUserId);
                                    databaseReference.child("user_name").setValue(phonenumber);
                                    databaseReference.child("user_phone").setValue(phonenumber);
                                    databaseReference.child("user_status").setValue("Hey there, I am using Addict Chat App");
                                    databaseReference.child("user_image").setValue("default_image");
                                    databaseReference.child("user_thumb_image").setValue("default_image");
                                    databaseReference.child("user_online_status").setValue("no");
                                    databaseReference.child("user_id").setValue(currentUserId);
                                    databaseReference.child("device_token_id").setValue(deviveTokenId);



                                    Intent intent = new Intent(VerifyPhoneActivity.this, DashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null) {
                otpView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void verifyUserExistance(String currentUserID)
    {
      //  String currentUserID = mAuth.getCurrentUser().getUid();

        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.child("user_name").exists()))
                {
                    haveUser = true;
                    Toast.makeText(VerifyPhoneActivity.this, "Exists", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //SendUserToSettingsActivity();
                    haveUser = false;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
