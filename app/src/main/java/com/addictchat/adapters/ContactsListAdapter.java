package com.addictchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.addictchat.R;
import com.addictchat.model.Contact;

import java.util.ArrayList;

public class ContactsListAdapter extends ArrayAdapter<Contact> {

    public ContactsListAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        //TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvName.setText(contact.name);
        //tvEmail.setText("");
        tvPhone.setText("");
        /*if (contact.emails.size() > 0 && contact.emails.get(0) != null) {
            tvEmail.setText(contact.emails.get(0).address);
        }*/
        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            tvPhone.setText(contact.numbers.get(0).number);
        }
        return view;
    }

}
