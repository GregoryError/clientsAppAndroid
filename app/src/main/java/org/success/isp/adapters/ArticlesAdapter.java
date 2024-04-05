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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.pojos.ExtraEntityPreview;
import org.success.isp.views.ArticleActivity;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {
    private Context context;

    public ArticlesAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<ExtraEntityPreview> previews;

    public void setPreviews(ArrayList<ExtraEntityPreview> previews) {
        this.previews = previews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ExtraEntityPreview preview = previews.get(position);
        if (preview != null) {

            GlideUrl glideUrl = new GlideUrl(preview.getImageUrl(),
                    new LazyHeaders.Builder()
                            .build());

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(10f);
            circularProgressDrawable.setCenterRadius(50f);

            circularProgressDrawable.start();

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imageViewPreview);


            holder.textViewDate.setText(preview.getLocalDate().toString()); // TODO: Make a good string
            holder.textViewDescription.setText(preview.getDescription());
            holder.textViewTitle.setText(preview.getTitle());

            holder.constraintMain.setOnClickListener(view -> {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("articleId", preview.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return previews.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewPreview;
        public TextView textViewDate;
        public TextView textViewDescription;
        public TextView textViewTitle;

        public ConstraintLayout constraintMain;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPreview = itemView.findViewById(R.id.imageViewPreview);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            constraintMain = itemView.findViewById(R.id.constraintMain);
        }
    }
}
