package org.success.isp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.success.isp.R;
import org.success.isp.pojos.EntitiesPack;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.pojos.EntityNews;
import org.success.isp.pojos.EntityPromo;
import org.success.isp.pojos.ExtraEntityPreview;
import org.success.isp.views.ArticleActivity;
import org.success.isp.views.NewsActivity;
import org.success.isp.views.PromoActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class HybridEntityAdapter extends RecyclerView.Adapter<HybridEntityAdapter.HybridEntityHolder> {

    private Context context;

    private EntitiesPack pack;

    // private ArrayList<Object> entities;

    private ArrayList<Object> entities;

    private boolean darkMode;

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public HybridEntityAdapter(Context context) {
        this.context = context;
    }

    public void setPack(EntitiesPack pack) {
        this.pack = pack;
        entities = new ArrayList<>();

        entities.addAll(pack.getArticles());
        entities.addAll(pack.getNews());
        entities.addAll(pack.getPromos());

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HybridEntityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hybrid_entity_item, parent, false);
        return new HybridEntityAdapter.HybridEntityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HybridEntityHolder holder, int position) {

        Object entity = entities.get(position);

        if (entity instanceof EntityNews) {
            // make News item
            EntityNews entityNews = (EntityNews) entity;
                holder.textViewDate.setText(entityNews.getLocalDate().toString());
                holder.textViewTitle.setText(entityNews.getName().length() > 30 ? entityNews.getName().substring(0, 30) + "..." :
                        entityNews.getName());
                holder.textViewDescription.setText(entityNews.getContent().length() > 90 ? entityNews.getContent().substring(0, 90) + "..." :
                        entityNews.getContent() + "...");
                holder.textViewKindOfEntity.setText("Новость");

                GlideUrl glideUrl = new GlideUrl(entityNews.getImgUrl(),
                        new LazyHeaders.Builder()
                                .build());

                Glide.with(context)
                        .asBitmap()
                        .load(glideUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.imageViewPreview);

            holder.constraintMain.setOnClickListener(view -> {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("newsId", ((EntityNews) entity).getId());
                context.startActivity(intent);
            });

        } else if (entity instanceof EntityArticle) {
            // make Article items
            EntityArticle entityArticle = (EntityArticle) entity;
            holder.textViewDate.setText(entityArticle.getLocalDate().toString());
            holder.textViewTitle.setText(entityArticle.getName().length() > 30 ? entityArticle.getName().substring(0, 30) + "..." :
                    entityArticle.getName());

            if (entityArticle.getParagraphs().size() > 0) {
                holder.textViewDescription.setText(entityArticle.getParagraphs().get(0).length() > 90 ? entityArticle.getParagraphs().get(0).substring(0, 90) + "..." :
                        entityArticle.getParagraphs().get(0) + "...");
            }
            holder.textViewKindOfEntity.setText("Статья");

            if (entityArticle.getImgUrls().size() > 0) {
                GlideUrl glideUrl = new GlideUrl(entityArticle.getImgUrls().get(0),
                        new LazyHeaders.Builder()
                                .build());

                Glide.with(context)
                        .asBitmap()
                        .load(glideUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.imageViewPreview);
            }

            holder.constraintMain.setOnClickListener(view -> {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("articleId", ((EntityArticle) entity).getId());
                context.startActivity(intent);
            });

        } else if (entity instanceof EntityPromo) {
            //make Promo items
            EntityPromo entityPromo = (EntityPromo) entity;
            holder.textViewDate.setText(entityPromo.getLocalDate().toString());
            holder.textViewTitle.setText(entityPromo.getName().length() > 30 ? entityPromo.getName().substring(0, 30) + "..." :
                    entityPromo.getName());
            holder.textViewDescription.setText(entityPromo.getDescription().length() > 90 ? entityPromo.getDescription().substring(0, 90) + "..." :
                    entityPromo.getDescription() + "...");
            holder.textViewKindOfEntity.setText("Акция");

            GlideUrl glideUrl = new GlideUrl(entityPromo.getImgUrl(),
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

            holder.constraintMain.setOnClickListener(view -> {
                Intent intent = new Intent(context, PromoActivity.class);
                intent.putExtra("promoId", ((EntityPromo) entity).getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public class HybridEntityHolder extends RecyclerView.ViewHolder {

        public TextView textViewKindOfEntity;
        public ImageView imageViewPreview;

        public TextView textViewDate;
        public TextView textViewDescription;
        public TextView textViewTitle;

        public ConstraintLayout constraintMain;

        public HybridEntityHolder(@NonNull View itemView) {
            super(itemView);
            textViewKindOfEntity = itemView.findViewById(R.id.textViewKindOfEntity);
            imageViewPreview = itemView.findViewById(R.id.imageViewPreview);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            constraintMain = itemView.findViewById(R.id.constraintMain);
        }
    }
}











