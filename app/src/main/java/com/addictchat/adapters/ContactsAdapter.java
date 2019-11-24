package com.addictchat.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.addictchat.R;
import com.addictchat.model.Contact;
import com.addictchat.widgets.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> contactList;
    private Context context;

    public ContactsAdapter(Context context, ArrayList<Contact> contactList) {
       this.context = context;
       this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contact_item, parent, false);

        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get the data item
        Contact contact = contactList.get(position);

        holder.tvName.setText(contact.name);
        //holder.tvEmail.setText("");
        holder.tvPhone.setText("");

        /*Picasso.with(context)
                .load(contactList.get(position))*/

       /* if (contact.emails.size() > 0 && contact.emails.get(0) != null) {
            holder.tvEmail.setText(contact.emails.get(0).address);
        }*/
        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            holder.tvPhone.setText(contact.numbers.get(0).number);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvEmail, tvPhone;
        private RoundedImageView rounded_iv_profile;

        public ViewHolder(View itemView) {
            super(itemView);

            rounded_iv_profile = (RoundedImageView) itemView.findViewById(R.id.rounded_iv_profile);
            // Populate the data into the template view using the data object
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            //tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
        }
    }

   /* private Cursor mCursor;
    private final int mNameColIdx, mIdColIdx;

    public ContactsAdapter(Cursor cursor) {
        mCursor = cursor;
        mNameColIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        mIdColIdx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);

        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder contactViewHolder, int pos) {
        // Extract info from cursor
        mCursor.moveToPosition(pos);
        String contactName = mCursor.getString(mNameColIdx);
        long contactId = mCursor.getLong(mIdColIdx);

        // Create contact model and bind to viewholder
        //Contact c = new Contact();
        *//*c.name = contactName;
        c.profilePic = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);*//*

        //contactViewHolder.bind(c);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView mImage;
        private TextView mLabel;
        private Contact mBoundContact; // Can be null

        public ViewHolder(final View itemView) {
            super(itemView);
            mImage = (RoundedImageView) itemView.findViewById(R.id.rounded_iv_profile);
            mLabel = (TextView) itemView.findViewById(R.id.tv_label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBoundContact != null) {
                        Toast.makeText(
                                itemView.getContext(),
                                "Hi, I'm " + mBoundContact.name,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void bind(Contact contact) {
            mBoundContact = contact;
            mLabel.setText(contact.name);
            Picasso.with(itemView.getContext())
                    .load(contact.profilePic)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mImage);
        }
    }*/
}
