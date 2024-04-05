package org.success.isp.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.success.isp.R;
import org.success.isp.pojos.DB_dry_pay;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


// useless:
// 410 - admin action;
// 426 - system action
// 700 - admin minus action
// 501 - admin edit
// 502 - admin remove

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.CountEventViewHolder> {

    private Locale locale = new Locale("ru", "RU");
    private ArrayList<DB_dry_pay> paymentEvents;
    private Boolean darkMode;
    private ArrayList<Integer> categories;

    public void setCategories(ArrayList<Integer> categories) {
        this.categories = categories;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setPaymentEvents(ArrayList<DB_dry_pay> paymentEvents) {
        if (paymentEvents != null && !paymentEvents.isEmpty()) {
            this.paymentEvents = paymentEvents;
            notifyDataSetChanged();
        }
    }

    private final Context context;

    public PaymentsAdapter(Context context) {
        this.context = context;
        darkMode = false;
    }

    @NonNull
    @Override
    public CountEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payments_item, parent, false);
        return new CountEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountEventViewHolder holder, int position) {
        clearItems(holder);
        DB_dry_pay event = paymentEvents.get(position);

        if (event != null) {
            int category = event.getCategory();

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getTime()), TimeZone.getDefault().toZoneId());
                holder.textViewYear.setText(String.valueOf(localDateTime.getYear()));
                holder.textViewMonth.setText(localDateTime.getMonth().getDisplayName(TextStyle.SHORT, locale));
                holder.textViewDay.setText(String.valueOf(localDateTime.getDayOfMonth()).length() == 1 ?
                        '0' + String.valueOf(localDateTime.getDayOfMonth()) : String.valueOf(localDateTime.getDayOfMonth()));
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
                holder.textViewTime.setText(String.format("%s:%s", hours, minutes));
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(event.getTime() * 1000); // Преобразование времени в миллисекунды
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                // Форматирование дня и месяца добавлением ведущего нуля, если нужно
                String formattedDay = String.format("%02d", day);
                String formattedMonth = new SimpleDateFormat("MMM", locale).format(calendar.getTime()); // Получение сокращенного названия месяца
                // Форматирование времени добавлением ведущего нуля, если нужно
                String formattedHour = String.format("%02d", hour);
                String formattedMinute = String.format("%02d", minute);
                holder.textViewYear.setText(String.valueOf(year));
                holder.textViewMonth.setText(formattedMonth);
                holder.textViewDay.setText(formattedDay);
                holder.textViewTime.setText(String.format("%s:%s", formattedHour, formattedMinute));
            }

            // Commentary for user
            holder.textViewComment.setVisibility(View.VISIBLE);
            switch (category) {
                case 95:
                case 96: {
                    holder.textViewComment.setText(R.string.pay_el_got);
                    break;
                }
                case 600: {
                    holder.textViewComment.setText(R.string.pay_cash_got);
                    break;
                }
                case 1000: {
                    holder.textViewComment.setText(R.string.pay_temp_user_got);
                    break;
                }
                case 426: {
                    holder.textViewComment.setText(R.string.pay_temp_remove);
                    break;
                }
                case 423: {
                    holder.textViewComment.setText(R.string.pay_blocked_pay_reason);
                    break;
                }
                case 110: {
                    holder.textViewComment.setText(R.string.pay_minus_for_services);
                    break;
                }
                case 0: {
                    if (event.getCash() > 0f) {
                        holder.textViewComment.setText(R.string.pay_plus);
                    }
                    if (event.getCash() < 0f) {
                        holder.textViewComment.setText(R.string.pay_minus);
                    }
                    break;
                }
                default: {
                    // holder.textViewComment.setVisibility(View.INVISIBLE);
                }
            }

            if (category == 1000) {
                holder.textViewSystemComment.setText(event.getReason());
            } else {
                if (event.getComent().length() > 0)
                    holder.textViewSystemComment.setText(event.getComent());
            }

            if (event.getCash() != 0f) {
                holder.textViewValue.setVisibility(View.VISIBLE);
                if (event.getCash() > 0f) {
                    holder.textViewValue.setTextColor(context.getColor(R.color.main_green));
                } else {
                    if (darkMode) {
                        holder.textViewValue.setTextColor(context.getColor(R.color.main_light));
                    } else {
                        holder.textViewValue.setTextColor(context.getColor(R.color.main_dark));
                    }
                }

                holder.textViewValue.setText(String.valueOf(event.getCash()));
            } else {
                holder.textViewValue.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (paymentEvents != null)
            return paymentEvents.size();
        return 0;
    }

    public class CountEventViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewYear;
        public TextView textViewMonth;
        public TextView textViewDay;
        public TextView textViewComment;
        public TextView textViewValue;
        public TextView textViewTime;
        public TextView textViewSystemComment;

        public CountEventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewYear = itemView.findViewById(R.id.textViewYear);
            textViewMonth = itemView.findViewById(R.id.textViewMonth);
            textViewDay = itemView.findViewById(R.id.textViewDay);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewSystemComment = itemView.findViewById(R.id.textViewSystemComment);
            textViewValue = itemView.findViewById(R.id.textViewValue);

            textViewValue.setVisibility(View.VISIBLE);

            if (darkMode) {
                textViewYear.setTextColor(context.getColor(R.color.main_light));
                textViewMonth.setTextColor(context.getColor(R.color.main_light));
                textViewTime.setTextColor(context.getColor(R.color.main_light));
                textViewComment.setTextColor(context.getColor(R.color.main_light));
                textViewSystemComment.setTextColor(context.getColor(R.color.main_light));
                textViewValue.setTextColor(context.getColor(R.color.main_light));
            }
        }
    }

    public void clear() {
        int size = paymentEvents.size();
        paymentEvents.clear();
        notifyItemRangeRemoved(0, size);
    }

    private void clearItems(CountEventViewHolder holder) {
        holder.textViewYear.setText("");
        holder.textViewMonth.setText("");
        holder.textViewDay.setText("");
        holder.textViewComment.setText("");
        holder.textViewValue.setText("");
        holder.textViewTime.setText("");
        holder.textViewSystemComment.setText("");
    }
}
