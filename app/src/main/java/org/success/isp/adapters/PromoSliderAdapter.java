package org.success.isp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.Target;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.ExtraEntityPreview;
import org.success.isp.views.NewsActivity;
import org.success.isp.views.PromoActivity;

import java.util.List;
import java.util.Objects;


public class PromoSliderAdapter extends PagerAdapter {

    // Context object
    private Context context;
    // Array of images
    private List<ExtraEntityPreview> promoItem;
    // Layout Inflater
    private LayoutInflater mLayoutInflater;

    private String userPass;

    private String userId;

    private boolean darkMode;

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public PromoSliderAdapter(Context context, List<ExtraEntityPreview> images) {
        this.context = context;
        this.promoItem = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return promoItem.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.promo_item, container, false);

        ExtraEntityPreview extraEntityPreview = promoItem.get(position);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PromoActivity.class);
            intent.putExtra("promoId", extraEntityPreview.getId());
            intent.putExtra("userPass", userPass);
            intent.putExtra("userId", userId);
            intent.putExtra("darkMode", darkMode);
            context.startActivity(intent);
        });

        // load the image in the imageView
//        GlideUrl glideUrl = new GlideUrl(ApiFactory.STATIC_URL + promoItem.get(position).getImgName(),
//                new LazyHeaders.Builder()
//                        .build());

//        Glide.with(context)
//                .load(glideUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(imageView);

        GlideUrl glideUrl = new GlideUrl(promoItem.get(position).getImageUrl(),
                new LazyHeaders.Builder()
                        .build());

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();

        Glide.with(context)
                .asBitmap()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .load(glideUrl)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        TextView textViewPromoName = (TextView) itemView.findViewById(R.id.textViewPromoName);
        textViewPromoName.setText(promoItem.get(position).getTitle());
        TextView textViewPromoDesc = (TextView) itemView.findViewById(R.id.textViewPromoDesc);
        textViewPromoDesc.setText(promoItem.get(position).getDescription());


        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

}