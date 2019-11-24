package com.addictchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import android.widget.Toast;
import com.addictchat.FirebaseMsging.MySingleTonClass;
import com.addictchat.R;
import com.addictchat.activities.MessageActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable{
    ArrayList<String> request_types=new ArrayList<>();
    Context context;
    RadioButton previous_Selected=null;
    String uname, login_unamne,req_type,req_status;
    AutoCompleteTextView autoCompleteTextView;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAADcU8SE8:APA91bFtZ1M-S-t1nRH_t1cAyj2IYPctSwJrctZskHxo49SDC5YmHXUpGtn-NpsWAOkhZqS3ZDS3izQZAfQcfRdsOeDBOOUrxT3u2z2hC_Xqynth2uNvA8iVqjuQjoKGNUO01reZ1Unb";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE = "Request Type";
    String NOTIFICATION_MESSAGE = "Request";
    String TOPIC;

    public AutoCompleteAdapter(MessageActivity messageActivity, String[] reuests, String userName, String login_unanme, String req_type, String req_status, AutoCompleteTextView autoCompleteTextView) {
        request_types.addAll(Arrays.asList(reuests));
        context=messageActivity;
        uname=userName;
        this.login_unamne =login_unanme;
        this.req_type=req_type;
        this.req_status=req_status;
        this.autoCompleteTextView=autoCompleteTextView;
    }

    @Override
    public int getCount() {
        return request_types.size();
    }

    @Override
    public Object getItem(int i) {
        return request_types.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.custom_request,null,false);
        final RadioButton radioButton=view.findViewById(R.id.text);

        radioButton.setText(request_types.get(i));
        if (req_type!=null){
            radioButton.setChecked(radioButton.getText().toString().equalsIgnoreCase(req_type));
            /*radioButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"546",Toast.LENGTH_LONG).show();
                }
            });*/

        }


        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.dismissDropDown();
                if (previous_Selected!=null){
                    previous_Selected.setChecked(false);
                }
                if (radioButton.getText().toString().equals("Love")){


                    Toast.makeText(context,"Love",Toast.LENGTH_LONG).show();

                    // radioButton.setText("Waiting!!");


                    TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to


                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", "Love");

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    //sendNotification(notification);



                }else if (radioButton.getText().toString().equals("Family")){
                    Toast.makeText(context,"Family",Toast.LENGTH_LONG).show();

                }

                previous_Selected=radioButton;
                String time=new SimpleDateFormat("dd MMM yyyy hh:mmaa").format(new Date());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(uname).child("requests").child(login_unamne);
                databaseReference.child("uname").setValue(login_unamne);
                databaseReference.child("time").setValue(time);
                databaseReference.child("req_type").setValue(radioButton.getText().toString());
                databaseReference.child("req_status").setValue("pending");
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(login_unamne).child("requests_sent").child(uname);
                databaseReference2.child("uname").setValue(uname);
                databaseReference2.child("time").setValue(time);
                databaseReference2.child("req_type").setValue(radioButton.getText().toString());
                databaseReference2.child("req_status").setValue("pending");
            }
        });
        return view;
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
                    Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }
            }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleTonClass.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults=new FilterResults();
                filterResults.values=request_types;
                filterResults.count=request_types.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }
}