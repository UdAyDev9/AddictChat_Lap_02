package com.addictchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
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
    databaseReference.child("user_online_status").setValue("yes");
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
    databaseReference.child("user_online_status").setValue("yes");
    //    status("offline.");


  }

  @Override
  protected void onStop() {
    super.onStop();
    databaseReference.child("user_online_status").setValue("no");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    databaseReference.child("user_online_status").setValue("no");
  }


  @Override
  protected void onRestart() {
    super.onRestart();
    databaseReference.child("user_online_status").setValue("yes");

  }


  @Override
  protected void onResume() {
    super.onResume();
    databaseReference.child("user_online_status").setValue("yes");
    // status("online.");
  }

  private void status(String status){
    databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    HashMap<String,Object> hashMap = new HashMap<>();
    hashMap.put("user_online_status",status);
    databaseReference2.updateChildren(hashMap);
  }
}