package com.addictchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import com.addictchat.R;
import com.addictchat.activities.MessageActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AutoCompleteAdapter2 extends BaseAdapter implements Filterable{
    ArrayList<String> request_types=new ArrayList<>();
    Context context;
    RadioButton previous_Selected=null;
    String uname, login_unamne;
    boolean chatenabled;
    AutoCompleteTextView autoCompleteTextView;
    public AutoCompleteAdapter2(MessageActivity messageActivity, String[] reuests, String userName, String login_unanme, boolean chatenabled, AutoCompleteTextView received) {
        request_types.addAll(Arrays.asList(reuests));
        context=messageActivity;
        uname=userName;
        this.login_unamne =login_unanme;
        this.chatenabled=chatenabled;
        this.autoCompleteTextView=received;
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

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(uname).child("requests_sent").child(login_unamne);
                databaseReference2.child("req_status").setValue(radioButton.getText().toString());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(login_unamne).child("requests").child(uname);
                databaseReference.child("req_status").setValue(radioButton.getText().toString());
                if (radioButton.getText().toString().equals("Accept")){
                    FirebaseDatabase.getInstance().getReference().child(login_unamne).child("chats").child(uname);
                    FirebaseDatabase.getInstance().getReference().child(uname).child("chats").child(login_unamne);
                    chatenabled=true;
                }
                autoCompleteTextView.dismissDropDown();
            }
        });
        return view;
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
