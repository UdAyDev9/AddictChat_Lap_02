package com.addictchat.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.addictchat.R;
import com.addictchat.adapters.ContactFetcher;
import com.addictchat.adapters.ContactsAdapter;
import com.addictchat.adapters.ContactsListAdapter;
import com.addictchat.model.Contact;
import com.addictchat.utils.Utils;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    ArrayList<Contact> listContacts;
    ListView lvContacts;

    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private int PERMISSION_REQUEST_CONTACT = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("Select Contact");

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });

        //listContacts = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.lvContacts);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        checkPermission();

        /*lvContacts = (ListView) findViewById(R.id.lvContacts);
        ContactsListAdapter adapterContacts = new ContactsListAdapter(this, listContacts);
        lvContacts.setAdapter(adapterContacts);*/
    }

    private void showContacts(){

        /*listContacts = new ContactFetcher(this).fetchAll();
        lvContacts = (ListView) findViewById(R.id.lvContacts);
        ContactsListAdapter adapterContacts = new ContactsListAdapter(this, listContacts);
        lvContacts.setAdapter(adapterContacts);*/

        //ContactsListAdapter adapterContacts = new ContactsListAdapter(this, listContacts);

        listContacts = new ContactFetcher(this).fetchAll();
        ContactsAdapter contactsAdapter = new ContactsAdapter(this, listContacts);
        recyclerView.setAdapter(contactsAdapter);
    }

    private void checkPermission() {
        //int result = ContextCompat.checkSelfPermission(getApplicationContext(), RE);
        //int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(ContactsActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else{
                showContacts();
            }
        }
        else{
            showContacts();
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PERMISSION_REQUEST_CONTACT) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                Utils.showToast(ContactsActivity.this, "No permission for contacts");
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    private int PERMISSION_REQUEST_CONTACT = 101;

    // TODO: Implement a more advanced example that makes use of this
    private static final String SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";

    // Defines a variable for the search string
    private String mSearchString = "@hotmail.com";
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };

    private RecyclerView mContactListView;
    private TextView tvContactCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

        tvContactCount = (TextView) findViewById(R.id.tv_toolbar_contact_count);
        mContactListView = (RecyclerView) findViewById(R.id.rv_contact_list);
        mContactListView.setLayoutManager(new LinearLayoutManager(this));
        mContactListView.setItemAnimator(new DefaultItemAnimator());
        checkPermission();
    }

   *//* // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private void requestContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            showContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Log.e("Permissions", "Access denied");
            }
        }
    }*//*

    private void showContacts(){

        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                *//*
                 * Makes search string into pattern and
                 * stores it in the selection array
                 *//*
                Uri contentUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mSearchString));
                // Starts the query
                return new CursorLoader(getBaseContext(), contentUri, PROJECTION, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                // Put the result Cursor in the adapter for the ListView
                mContactListView.setAdapter(new ContactsAdapter(cursor));

                int count = cursor.getCount();

                //String count = ContactsContract.CommonDataKinds.Phone._COUNT;
                String contact = "\t" + "contacts";

                tvContactCount.setText(count + " " + contact);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // TODO do I need to do anything here?
            }
        });
    }

    private void checkPermission() {
        //int result = ContextCompat.checkSelfPermission(getApplicationContext(), RE);
        //int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(ContactsActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else{
                showContacts();
            }
        }
        else{
            showContacts();
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PERMISSION_REQUEST_CONTACT) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                Utils.showToast(ContactsActivity.this, "No permission for contacts");
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/
}
