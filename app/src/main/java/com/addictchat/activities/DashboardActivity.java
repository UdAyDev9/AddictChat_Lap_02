package com.addictchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import com.addictchat.R;
import com.addictchat.adapters.ViewPagerAdapter;
import com.addictchat.fragments.CallsFragment;
import com.addictchat.fragments.ChatFragment;
import com.addictchat.fragments.ContactsFragment;
import com.addictchat.fragments.RequestsFragment;
import com.addictchat.fragments.StatusFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

  //This is our tablayout
  private TabLayout tabLayout;

  //This is our viewPager
  private ViewPager viewPager;

  //Fragments
  RequestsFragment requestsFragment;
  ChatFragment chatFragment;
  StatusFragment statusFragment;
  CallsFragment callsFragment;
  ContactsFragment contactsFragment;

  FirebaseUser firebaseUser;
  String[] tabTitle={"CHAT","STATUS","FRIENDS","CALLS","REQUESTS"};
  int[] unreadCount={0,0,0};

  private DatabaseReference databaseReference,databaseReference2;
  private  FirebaseAuth mAuth;
  private FloatingActionButton fab;
  private DatabaseReference RootRef;
  private String currentUserID;
  private FirebaseUser currentUser;
  private  final String TAG = "life_cycle";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
    mAuth = FirebaseAuth.getInstance();
    currentUser = mAuth.getCurrentUser();
    currentUserID = mAuth.getCurrentUser().getUid();
    RootRef = FirebaseDatabase.getInstance().getReference();
    updateUserStatus("online");
    Log.d(TAG, "onCreate: called");
       /* Button btnLogout = (Button) findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });*/



    databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

    databaseReference2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });


    mAuth = FirebaseAuth.getInstance();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
    //databaseReference.child("user_online_status").setValue("yes");
    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Toast.makeText(DashboardActivity.this, "ContactsActivity", Toast.LENGTH_SHORT).show();
        //getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new ContactsActivity(), "ContactsActivity").addToBackStack(null).commit();

        //   Intent intent = new Intent(DashboardActivity.this, ContactsActivity.class);
        // startActivity(intent);

        Intent intent = new Intent(DashboardActivity.this, UsersActivity.class);
        startActivity(intent);

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ContactsFragment()).addToBackStack(null).commit();
      }
    });

    //Initializing viewPager
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setOffscreenPageLimit(4);
    setupViewPager(viewPager);

    //Initializing the tablayout
    tabLayout = (TabLayout) findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    try
    {
      setupTabIcons();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        viewPager.setCurrentItem(position, false);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.menu_home, menu);
    // Associate searchable configuration with the SearchView
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_logout:
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
        finish();
        //Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.action_settings:
        //Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(DashboardActivity.this,ProfileActivity.class));
        return true;

      case R.id.action_friends:
        //Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(DashboardActivity.this, FriendsActivity.class));
        return true;


      /*case R.id.action_with_icon:
       *//*Intent withicon=new Intent(this,TabWithIconActivity.class);
                startActivity(withicon);
                finish();*//*
                return true;
            case R.id.action_without_icon:
                *//*Intent withouticon=new Intent(this,TabWOIconActivity.class);
                startActivity(withouticon);
                finish();*//*
                return true;*/
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    requestsFragment = new RequestsFragment();
    chatFragment = new ChatFragment();
    statusFragment = new StatusFragment();
    callsFragment = new CallsFragment();
    contactsFragment = new ContactsFragment();
    adapter.addFragment(chatFragment,"CHAT");
    adapter.addFragment(statusFragment,"STATUS");
    adapter.addFragment(contactsFragment,"FRIENDS");
    adapter.addFragment(callsFragment,"CALLS");
    adapter.addFragment(requestsFragment,"REQUESTS");
    viewPager.setAdapter(adapter);
  }

  private View prepareTabView(int pos) {
    View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
    TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
    tv_title.setText(tabTitle[pos]);
    if(unreadCount[pos] > 0) {
      tv_count.setVisibility(View.VISIBLE);
      tv_count.setText(""+unreadCount[pos]);
    } else {
      tv_count.setVisibility(View.GONE);
    }

    return view;
  }

  private void setupTabIcons() {
    for (int i=0; i<tabTitle.length; i++) {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/

      tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
    }
  }


  @Override
  protected void onPause() {
    super.onPause();
    //databaseReference.child("user_online_status").setValue("yes");
    //    status("offline.");
    updateUserStatus("offline");
    Log.d(TAG, "onPause: "+ "called");


  }

  @Override
  protected void onStop() {
    super.onStop();
    if (currentUser != null)
    {
      updateUserStatus("offline");
      Log.d(TAG, "onStop: called");
    }
    databaseReference.child("user_online_status").setValue("no");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (currentUser != null)
    {
      updateUserStatus("offline");
      Log.d(TAG, "onDestroy: called");
    }
    //databaseReference.child("user_online_status").setValue("no");
  }


  @Override
  protected void onRestart() {
    super.onRestart();
    //databaseReference.child("user_online_status").setValue("yes");
    updateUserStatus("online");
    Log.d(TAG, "onRestart: called");

  }



  @Override
  protected void onResume() {
    super.onResume();
    //databaseReference.child("user_online_status").setValue("yes");
    // status("online.");
    updateUserStatus("online");
    Log.d(TAG, "onResume: called");
  }

  private void status(String status){
    databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    HashMap<String,Object> hashMap = new HashMap<>();
    hashMap.put("user_online_status",status);
    databaseReference2.updateChildren(hashMap);
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

    RootRef.child("Users").child(currentUserID).child("userState")
        .updateChildren(onlineStateMap);

  }

  private void VerifyUserExistance()
  {
    String currentUserID = mAuth.getCurrentUser().getUid();

    RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot)
      {
        if ((dataSnapshot.child("user_name").exists()))
        {
          //Toast.makeText(DashboardActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
        }
        else
        {
          //SendUserToSettingsActivity();
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    if (currentUser == null)
    {
      Log.d(TAG, "onStart: called");
      //SendUserToLoginActivity();
    }
    else
    {
      updateUserStatus("online");

      VerifyUserExistance();
    }
  }
}