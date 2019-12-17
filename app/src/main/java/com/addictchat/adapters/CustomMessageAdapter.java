package com.addictchat.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addictchat.R;
import com.addictchat.activities.MessageActivity;
import com.addictchat.model.Message;

import java.util.ArrayList;

public class CustomMessageAdapter extends RecyclerView.Adapter<CustomMessageAdapter.MyViewHolder> {
    ArrayList<Message> messages = new ArrayList<>();
    Context context;

    public CustomMessageAdapter(MessageActivity messageActivity, ArrayList<Message> msgs) {
        context = messageActivity;
        messages = msgs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_message, parent, false);
        if (viewType == 1) {
            view.findViewById(R.id.to_message_layout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.from_message_layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.to_message_layout).setVisibility(View.GONE);
            view.findViewById(R.id.from_message_layout).setVisibility(View.VISIBLE);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Message messagess =messages.get(position);
        String fromMessageType = messagess.getType();

        if (!messages.get(position).getFrom()) {

            holder.from_message.setText(messages.get(position).getMsg());
            holder.fromTime.setText(messages.get(position).getTime());

        } else {
            holder.to_message.setText(messages.get(position).getMsg());
            holder.toTime.setText(messages.get(position).getTime());

        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getFrom())
            return 1;
        else return 0;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView from_cardView, to_cardView;
        public ConstraintLayout from_constraintLayout, to_constraintLayout;
        public TextView from_message, to_message,fromTime,toTime;
        public ImageView msgSenderImg,msgRecieverImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            from_constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.from_message_layout_constr);
            to_constraintLayout =(ConstraintLayout) itemView.findViewById(R.id.to_message_layout_constr);
            from_cardView = (CardView)itemView.findViewById(R.id.from_message_layout);
            to_cardView = (CardView) itemView.findViewById(R.id.to_message_layout);
            from_message = itemView.findViewById(R.id.from_message);
            to_message = itemView.findViewById(R.id.to_messagee);
            fromTime = itemView.findViewById(R.id.from_time);
            toTime = itemView.findViewById(R.id.to_time);
            msgSenderImg = (ImageView)itemView.findViewById(R.id.from_image_view);
            msgRecieverImg = (ImageView)itemView.findViewById(R.id.to_image_view);
        }
    }
}
