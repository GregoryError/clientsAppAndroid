package org.success.isp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class ImageHelper {
    private static ImageHelper instance = null;

    private ImageHelper() {
    }

    static ImageHelper getInstance() {
        if (instance == null)
            instance = new ImageHelper();
        return instance;
    }

    public enum AnimKind {
        SIZE_IN,
        SIZE_OUT,
        OPACITY
    }

    public static void ImageViewAnimatedChange(final Context c, final ImageView v, final Bitmap new_image, AnimKind animKind) {


        Animation anim_in = null;
        Animation anim_out = null;

        switch (animKind) {
            case OPACITY: {
                //anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
                anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
                anim_in.setDuration(150);
               // anim_out.setDuration(100);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        v.setImageBitmap(new_image);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //v.startAnimation(finalAnim_in);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                v.startAnimation(anim_in);
                break;
            }

            case SIZE_IN: {
                anim_in = new ScaleAnimation(1, 2, 1, 2,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim_out = new ScaleAnimation(2, 1, 2, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                anim_in.setDuration(300);
                anim_out.setDuration(300);
                anim_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        v.setImageBitmap(new_image);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //v.startAnimation(finalAnim_in);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                v.startAnimation(anim_out);
                break;
            }

            case SIZE_OUT: {
                anim_in = new ScaleAnimation(2, 1, 2, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim_out = new ScaleAnimation(1, 2, 1, 2,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim_in.setDuration(300);
                anim_out.setDuration(300);
                anim_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        v.setImageBitmap(new_image);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //v.startAnimation(finalAnim_in);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                v.startAnimation(anim_out);
                break;

            }

            default: {
                anim_in = new ScaleAnimation(1, 2, 1, 2,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim_out = new ScaleAnimation(2, 1, 2, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim_in.setDuration(300);
                anim_out.setDuration(300);
                anim_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        v.setImageBitmap(new_image);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //v.startAnimation(finalAnim_in);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                v.startAnimation(anim_out);
            }
        }


    }

}
