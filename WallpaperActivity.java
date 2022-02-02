package com.btsarmywallpaper.wallpaperapp;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;

public class WallpaperActivity extends AppCompatActivity {

    //on below line we are creating variables on below line for imageview and wallpaper manager
    WallpaperManager wallpaperManager;
    ImageView image;
    String url;
    private RelativeLayout wallpaperRL;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        wallpaperRL = findViewById(R.id.idRLWallpaper);
        //initializing all variables on below line.
        url = getIntent().getStringExtra("imgUrl");
        image = findViewById(R.id.image);
        loadingPB = findViewById(R.id.idPBLoading);
       // Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        //calling glide to load image from url on below line.
        Glide.with(this).load(Integer.parseInt(url))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                //making progress bar visibility to gone on below line.
                loadingPB.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //making progress bar visibility to gone on below line when image is ready.
                loadingPB.setVisibility(View.GONE);
                return false;
            }
        }).into(image);

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Button setWallpaper = findViewById(R.id.idBtnSetWallpaper);
        Button setLockScreenWallpaper = findViewById(R.id.idBtnSetLockScreenWallpaper);

        setLockScreenWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLockScreenWallpaper();
            }
        });

        //on below line we are adding on click listner to our set wallpaper button.
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(WallpaperActivity.this)
                        .asBitmap().load(Integer.parseInt(url))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .listener(new RequestListener<Bitmap>() {
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                          Toast.makeText(WallpaperActivity.this, "Fail to load image..", Toast.LENGTH_SHORT).show();
                                          return false;
                                      }

                                      @Override
                                      public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                          //on below line we are setting wallpaper using wallpaper manager on below line.
                                          try {
                                              wallpaperManager.setBitmap(bitmap);
                                          } catch (IOException e) {
                                              //on below line we are handling exception.
                                              Toast.makeText(WallpaperActivity.this, "Fail to set wallpaper", Toast.LENGTH_SHORT).show();
                                              e.printStackTrace();
                                          }
                                          return false;
                                      }
                                  }
                        ).submit();
                //displaying custom toast on below line.
//                new StyleableToast
//                        .Builder(WallpaperActivity.this)
//                        .text("Wallpaper Set to Home Screen")
//                        .textColor(Color.WHITE)
//                        .backgroundColor(getResources().getColor(R.color.black_shade_1))
//                        .show();
                Toast.makeText(WallpaperActivity.this, "Wallpaper Set to Home Screen", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void setLockScreenWallpaper() {
        Glide.with(WallpaperActivity.this)
                .asBitmap().load(Integer.parseInt(url))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  Toast.makeText(WallpaperActivity.this, "Fail to load image..", Toast.LENGTH_SHORT).show();
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  //on below line we are setting wallpaper using wallpaper manager on below line.
                                  try {
                                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                          Toast.makeText(WallpaperActivity.this, "Wallpaper Set to Lock Screen", Toast.LENGTH_LONG).show();
                                          wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                                      }
                                  } catch (IOException e) {
                                      //on below line we are handling exception.
                                      Toast.makeText(WallpaperActivity.this, "Fail to set lockscreen wallpaper", Toast.LENGTH_SHORT).show();
                                      e.printStackTrace();
                                  }
                                  return false;
                              }
                          }
                ).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }
}