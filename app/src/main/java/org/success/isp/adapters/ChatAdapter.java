package org.success.isp.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.success.isp.R;
import org.success.isp.pojos.EntityMessage;

import androidx.constraintlayout.widget.ConstraintSet;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;

    private ArrayList<EntityMessage> messages;

    private Locale locale = new Locale("ru", "RU");

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void setMessages(ArrayList<EntityMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        EntityMessage message = messages.get(position);

        // Positioning message
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.constraintMessageRoot);

        if (!message.getFromUser()) {
            constraintSet.clear(holder.cardViewMessage.getId(), ConstraintSet.END);
            constraintSet.connect(holder.cardViewMessage.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            constraintSet.applyTo(holder.constraintMessageRoot);
            holder.imageViewUserLogo.setImageResource(R.drawable.dog_agent);
            holder.cardViewMessage.setCardBackgroundColor(context.getColor(R.color.main_green));
            // Paddings
            holder.constraintMessageRoot.setPaddingRelative(8, 8, 34, 8);
        } else {
            holder.imageViewUserLogo.setImageResource(R.drawable.chat_user);
            holder.cardViewMessage.setCardBackgroundColor(context.getColor(R.color.main_light));
            constraintSet.clear(holder.cardViewMessage.getId(), ConstraintSet.START);
            constraintSet.connect(holder.cardViewMessage.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
            constraintSet.applyTo(holder.constraintMessageRoot);
            // Paddings
            holder.constraintMessageRoot.setPaddingRelative(34, 8, 8, 8);
        }

        holder.textViewMessage.setText(message.getText());

        StringBuilder timeString = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(message.getTime()), TimeZone.getDefault().toZoneId());
            timeString.append(String.valueOf(localDateTime.getDayOfMonth()).length() == 1 ?
                    '0' + String.valueOf(localDateTime.getDayOfMonth()) + ' ' : String.valueOf(localDateTime.getDayOfMonth()) + ' ');
            timeString.append((localDateTime.getMonth().getDisplayName(TextStyle.SHORT, locale)) + ' ');
            timeString.append(String.valueOf(localDateTime.getYear()));

            String hours = "";
            String minutes = "";
            if (String.valueOf(localDateTime.getHour()).length() == 1) {
                hours = '0' + String.valueOf(localDateTime.getHour());
            } else {
                hours = String.valueOf(localDateTime.getHour());
            }

            if (String.valueOf(localDateTime.getMinute()).length() == 1) {
                minutes = '0' + String.valueOf(localDateTime.getMinute());
            } else {
                minutes = String.valueOf(localDateTime.getMinute());
            }

            holder.textViewMessageTime.setText(String.format("%s   %s:%s", timeString, hours, minutes));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(message.getTime() * 1000);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String formattedDay = String.format("%02d", day);
            String formattedMonth = new SimpleDateFormat("MMM", locale).format(calendar.getTime());
            String formattedHour = String.format("%02d", hour);
            String formattedMinute = String.format("%02d", minute);

            timeString.append(formattedDay + ' ');
            timeString.append(formattedMonth + ' ');
            timeString.append(String.valueOf(year));
            holder.textViewMessageTime.setText(String.format("%s   %s:%s", timeString, formattedHour, formattedMinute));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMessage;

        public TextView textViewMessageTime;

        public CardView cardViewMessage;

        public ConstraintLayout constraintMessageRoot;

        public ImageView imageViewUserLogo;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewMessageTime = itemView.findViewById(R.id.textViewMessageTime);
            cardViewMessage = itemView.findViewById(R.id.cardViewMessage);
            constraintMessageRoot = itemView.findViewById(R.id.constraintMessageRoot);
            imageViewUserLogo = itemView.findViewById(R.id.imageViewUserLogo);
        }
    }
}
