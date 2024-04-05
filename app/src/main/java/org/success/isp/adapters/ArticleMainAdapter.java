package org.success.isp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.utils.Constants;
import org.success.isp.utils.ImageHelper;
import org.success.isp.utils.Session;
import org.success.isp.utils.SystemWorker;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleMainAdapter extends PagerAdapter {
    private Context context;
    private ViewPager pager;
    private ArrayList<EntityArticle> articleArrayList;
    private LayoutInflater mLayoutInflater;
    private Integer userId;
    private String userPass;
    private int currentArticle;
    private boolean isScrollRight;

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    public void setScrollRight(boolean scrollRight) {
        isScrollRight = scrollRight;
    }

    public void setCurrentArticle(int currentArticle) {
        this.currentArticle = currentArticle;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ArticleMainAdapter(Context context, ArrayList<EntityArticle> articleArrayList) {
        this.context = context;
        this.articleArrayList = articleArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (articleArrayList != null)
            return articleArrayList.size();
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
        if (articleArrayList != null) {
            userId = Session.getInstance().getUserId();

            EntityArticle entity = articleArrayList.get(position);


            View itemView = mLayoutInflater.inflate(R.layout.article_main_item, container,
                    false);

            TextView textViewName = itemView.findViewById(R.id.textViewName);
            TextView textViewTags = itemView.findViewById(R.id.textViewTags);
            TextView textViewDate = itemView.findViewById(R.id.textViewDate);

            if (entity.getName() != null)
                textViewName.setText(entity.getName());


            LinearLayout linearLayoutMain = itemView.findViewById(R.id.linearLayoutMain);

            int h = 0, p = 0, i = 0;

            if (entity.getItemSequence() != null)
                for (Integer item : entity.getItemSequence()) {
                    switch (item) {
                        case 1:
                            TextView headerTextView = new TextView(context);
                            headerTextView.setText(entity.getHeaders().get(h));
                            linearLayoutMain.addView(headerTextView);
                            headerTextView.setTypeface(ResourcesCompat.getFont(context, R.font.montserratbold));  // Устанавливаем шрифт
                            headerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);                     // Устанавливаем размер текста в sp
                            LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            headerParams.setMargins(14, 20, 14, 5);                      // Устанавливаем отступы
                            headerTextView.setLayoutParams(headerParams);
                            ++h;
                            break;
                        case 2:
                            TextView paragraphTextView = new TextView(context);
                            paragraphTextView.setText(entity.getParagraphs().get(p));
                            linearLayoutMain.addView(paragraphTextView);
                            // Настройка стилей для параграфов
                            paragraphTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Устанавливаем размер текста в sp
                            LinearLayout.LayoutParams paragraphParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            paragraphParams.setMargins(14, 5, 14, 20); // Устанавливаем отступы
                            paragraphTextView.setLayoutParams(paragraphParams);
                            ++p;
                            break;
                        case 0:
                            ImageView imageView = new ImageView(context);

                            GlideUrl glideUrl = new GlideUrl(entity.getImgUrls().get(i),
                                    new LazyHeaders.Builder()
                                            .build());

                            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                            circularProgressDrawable.setStrokeWidth(10f);
                            circularProgressDrawable.setCenterRadius(80f);
                            circularProgressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.main_black), PorterDuff.Mode.SRC_IN);
                            circularProgressDrawable.start();


                            // Настройка параметров макета для изображения
                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (300 * context.getResources().getDisplayMetrics().density)); // Установка высоты в 300dp и ширины во весь экран
                            imageView.setLayoutParams(imageParams);


                            // Загрузка изображения с помощью Glide

                            Glide.with(context)
                                    .asBitmap()
                                    .load(glideUrl)
                                    .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            // imageView.setImageBitmap(resource);
                                            ImageHelper.ImageViewAnimatedChange(context, imageView, resource, ImageHelper.AnimKind.OPACITY);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });

                            // Установка масштабирования изображения
                            if (imageView.getWidth() < 1080) {
                                // if source width less than 1080 set CROP_CENTER, otherwice FIT_CENTER
                                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            } else {
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            linearLayoutMain.addView(imageView);
                            ++i;
                            break;
                    }

                }


            if (entity.getTags() != null)
                textViewTags.setText(entity.getTags().replaceAll(" ", "\n"));
            if (entity.getLocalDate() != null)
                textViewDate.setText(entity.getLocalDate().toString());

            ImageView imageViewLike = itemView.findViewById(R.id.imageViewLike);

            if (entity.getWhoLikes() != null && userId != null) {
                if (entity.getWhoLikes().contains(userId)) {
                    imageViewLike.setImageResource(R.drawable.like_icon_filled);
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

                ApiFactory.getInstance().getApiService().likeArticle(String.valueOf(Session.getInstance().getUserId()),
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, Constants.SERVER_ADDRESS + "/article_/" + entity.getId());
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
            if (position == articleArrayList.size() - 1)
                imageViewRightArrow.setVisibility(View.INVISIBLE);

            imageViewLeftArrow.setOnClickListener(view -> {
                if (position > 0)
                    pager.setCurrentItem(position - 1);
            });

            imageViewRightArrow.setOnClickListener(view -> {
                if (position != articleArrayList.size() - 1)
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
