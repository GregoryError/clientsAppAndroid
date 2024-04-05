package org.success.isp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.Target;

import org.success.isp.R;
import org.success.isp.pojos.ExtraEntityPreview;
import org.success.isp.views.NewsActivity;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private ArrayList<ExtraEntityPreview> previews;

    private Integer userId;

    private String userPass;

    private boolean darkMode;

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public NewsAdapter(Context context) {
        this.context = context;
    }
    public void setPreviews(ArrayList<ExtraEntityPreview> previews) {
        this.previews = previews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        ExtraEntityPreview extraEntityPreview = previews.get(position);

        holder.textViewNewsTitle.setText(extraEntityPreview.getTitle().length() > 30 ? extraEntityPreview.getTitle().substring(0, 30) + "..." :
                extraEntityPreview.getTitle());

        GlideUrl glideUrl = new GlideUrl(extraEntityPreview.getImageUrl(),
                new LazyHeaders.Builder()
                        .build());

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.main_black), PorterDuff.Mode.SRC_IN);
        circularProgressDrawable.start();





        Glide.with(context)
                .asBitmap()
                .load(glideUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.dog_agent)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.item_image);

        holder.cardViewStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("newsId", extraEntityPreview.getId());
                intent.putExtra("userPass", userPass);
                intent.putExtra("userId", userId);
                intent.putExtra("darkMode", darkMode);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return previews.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNewsTitle;

        public ImageView item_image;

        public CardView cardViewStationary;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNewsTitle = itemView.findViewById(R.id.textViewNewsTitle);
            item_image = itemView.findViewById(R.id.item_image);
            cardViewStationary = itemView.findViewById(R.id.cardViewStationary);
        }
    }
}
