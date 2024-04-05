package org.success.isp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.imageview.ShapeableImageView;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityNews;
import org.success.isp.utils.Constants;
import org.success.isp.utils.ImageHelper;
import org.success.isp.utils.Session;
import org.success.isp.utils.SystemWorker;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsMainAdapter extends PagerAdapter {
    private Context context;
    private ViewPager pager;
    private ArrayList<EntityNews> newsArrayList;
    private LayoutInflater mLayoutInflater;
    private Integer userId;
    private String userPass;
    private int currentNews;
    private boolean isScrollRight;

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    public void setScrollRight(boolean scrollRight) {
        isScrollRight = scrollRight;
    }

    public void setCurrentNews(int currentNews) {
        this.currentNews = currentNews;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public NewsMainAdapter(Context context, ArrayList<EntityNews> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (newsArrayList != null)
            return newsArrayList.size();
        else
            return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (newsArrayList != null) {
            EntityNews entity = newsArrayList.get(position);

            View itemView = mLayoutInflater.inflate(R.layout.news_main_item, container,
                    false);

            TextView textViewName = itemView.findViewById(R.id.textViewName);
            TextView textViewContent = itemView.findViewById(R.id.textViewContent);
            TextView textViewTags = itemView.findViewById(R.id.textViewTags);
            TextView textViewDate = itemView.findViewById(R.id.textViewDate);

            if (entity.getName() != null)
                textViewName.setText(entity.getName());
            if (entity.getContent() != null)
                textViewContent.setText(entity.getContent());
            if (entity.getTags() != null)
                textViewTags.setText(entity.getTags().replaceAll(" ", "\n"));
            if (entity.getLocalDate() != null)
                textViewDate.setText(entity.getLocalDate().toString());

            ShapeableImageView newsImage = itemView.findViewById(R.id.shapeableImageViewNewsImage);

            GlideUrl glideUrl = new GlideUrl(entity.getImgUrl(),
                    new LazyHeaders.Builder()
                            .build());

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(10f);
            circularProgressDrawable.setCenterRadius(80f);
            circularProgressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.main_black), PorterDuff.Mode.SRC_IN);
            circularProgressDrawable.start();

            Glide.with(context)
                    .asBitmap()
                    .load(glideUrl)
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(newsImage);


            ImageView imageViewLike = itemView.findViewById(R.id.imageViewLike);

            if (entity.getWhoLikes() != null && userId != null) {
                if (entity.getWhoLikes().contains(userId)) {
                    imageViewLike.setImageResource(R.drawable.like_icon_filled);
                    //  ImageHelper.ImageViewAnimatedChange(context, imageViewLike, R.drawable.like_icon_filled);
                } else {
                    imageViewLike.setImageResource(R.drawable.like_icon);
                }
            }

            TextView likesCount = itemView.findViewById(R.id.likesCount);
            if (entity.getWhoLikes() != null)
                likesCount.setText(String.valueOf(entity.getWhoLikes().size()));

            imageViewLike.setOnClickListener(view -> {
                if (entity.getWhoLikes().contains(userId)) {
                    imageViewLike.setImageResource(R.drawable.like_icon);
                    entity.getWhoLikes().remove(userId);
                    likesCount.setText(String.valueOf(entity.getWhoLikes().size()));
                } else {
                    ImageHelper.ImageViewAnimatedChange(context, imageViewLike,
                            SystemWorker.drawableToBitmap(context.getDrawable(R.drawable.like_icon_filled)), ImageHelper.AnimKind.SIZE_IN);
                    entity.getWhoLikes().add(userId);
                    likesCount.setText(String.valueOf(entity.getWhoLikes().size()));
                }

                ApiFactory.getInstance().getApiService().likeNews(String.valueOf(Session.getInstance().getUserId()),
                                Session.getInstance().getUserPass(), entity.getId())
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });


            itemView.findViewById(R.id.imageViewShare).setOnClickListener(view -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Constants.SERVER_ADDRESS + "/news_/" + entity.getId());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            });


            ImageView imageViewLeftArrow = itemView.findViewById(R.id.imageViewLeftArrow);
            ImageView imageViewRightArrow = itemView.findViewById(R.id.imageViewRightArrow);

            if (Session.getInstance().isDarkMode()) {
                imageViewLeftArrow.setImageResource(R.drawable.swipe_left_white_24);
                imageViewRightArrow.setImageResource(R.drawable.swipe_right_white_24);
            }

            if (position == 0)
                imageViewLeftArrow.setVisibility(View.INVISIBLE);
            if (position == newsArrayList.size() - 1)
                imageViewRightArrow.setVisibility(View.INVISIBLE);

            imageViewLeftArrow.setOnClickListener(view -> {
                if (position > 0)
                    pager.setCurrentItem(position - 1);
            });

            imageViewRightArrow.setOnClickListener(view -> {
                if (position != newsArrayList.size() - 1)
                    pager.setCurrentItem(position + 1);
            });

            // Adding the View
            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
