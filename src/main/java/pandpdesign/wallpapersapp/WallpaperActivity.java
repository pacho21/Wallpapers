package pandpdesign.wallpapersapp;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import pandpdesign.wallpaperslowsdk.R;

public class WallpaperActivity extends AppCompatActivity {


    public static final String EXTRA_WP = "WallpaperActivity.Wallpaper";

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wallpaper_fullsize);

        mImageView = (ImageView) findViewById(R.id.wallpaperView);
        Wallpaper wpaper = getIntent().getParcelableExtra(EXTRA_WP);

        //long click -> set's as wp
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setwp();
                return true;
            }
        });

        //on click -> request if he would like to set it up as wp
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(v);
            }
        });

        //read the image and set it as ImageView
        Glide.with(this)
                .load(wpaper.getImgUrl())
                .asBitmap()
                .error(R.drawable.ic_cloud_download_black_24dp)
                .listener(new RequestListener<String, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        onPalette(Palette.from(resource).generate());
                        mImageView.setImageBitmap(resource);

                        return false;
                    }

                    public void onPalette(Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
                        }
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);

    }

    //set an image as Wallpaper
    private void setwp() {
        ImageView imageView = (ImageView) findViewById(R.id.wallpaperView);
        imageView.setDrawingCacheEnabled(true);
        //read the bitmap from the imageview
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        //get the dm so we can resize the bm
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        imageView.setDrawingCacheEnabled(false);
        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

        bitmap = prepareBitmap(bitmap,wallManager);

        //vibration ;D
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            wallManager.setBitmap(bitmap);
            vib.vibrate(100); //deprecated on higher Api
            Toast.makeText(WallpaperActivity.this, "Wallpaper Set Successfully!!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            Toast.makeText(WallpaperActivity.this, "Setting WallPaper Failed!!", Toast.LENGTH_SHORT).show();
        }
    }

    //show the alertDialog
    private void showAlert(View v) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(v.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(v.getContext());
        }
        builder.setTitle("Set Wallpaper")
                .setMessage("Are you sure you want to set this as your Wallpaper??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setwp();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(WallpaperActivity.this, "Wallpaper setup canceled!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    // Crop or inflate bitmap to desired device height and/or width
    public Bitmap prepareBitmap(final Bitmap sampleBitmap,
                                final WallpaperManager wallpaperManager) {
        Bitmap changedBitmap = null;
        final int heightBm = sampleBitmap.getHeight();
        final int widthBm = sampleBitmap.getWidth();
        final int heightDh = wallpaperManager.getDesiredMinimumHeight();
        final int widthDh = wallpaperManager.getDesiredMinimumWidth();
        if (widthDh > widthBm || heightDh > heightBm) {
            final int xPadding = Math.max(0, widthDh - widthBm) / 2;
            final int yPadding = Math.max(0, heightDh - heightBm) / 2;
            changedBitmap = Bitmap.createBitmap(widthDh, heightDh,
                    Bitmap.Config.ARGB_8888);
            final int[] pixels = new int[widthBm * heightBm];
            sampleBitmap.getPixels(pixels, 0, widthBm, 0, 0, widthBm, heightBm);
            changedBitmap.setPixels(pixels, 0, widthBm, xPadding, yPadding,
                    widthBm, heightBm);
        } else if (widthBm > widthDh || heightBm > heightDh) {
            changedBitmap = Bitmap.createBitmap(widthDh, heightDh,
                    Bitmap.Config.ARGB_8888);
            int cutLeft = 0;
            int cutTop = 0;
            int cutRight = 0;
            int cutBottom = 0;
            final Rect desRect = new Rect(0, 0, widthDh, heightDh);
            Rect srcRect = new Rect();
            if (widthBm > widthDh) { // crop width (left and right)
                cutLeft = (widthBm - widthDh) / 2;
                cutRight = (widthBm - widthDh) / 2;
                srcRect = new Rect(cutLeft, 0, widthBm - cutRight, heightBm);
            } else if (heightBm > heightDh) { // crop height (top and bottom)
                cutTop = (heightBm - heightDh) / 2;
                cutBottom = (heightBm - heightDh) / 2;
                srcRect = new Rect(0, cutTop, widthBm, heightBm - cutBottom);
            }
            final Canvas canvas = new Canvas(changedBitmap);
            canvas.drawBitmap(sampleBitmap, srcRect, desRect, null);

        } else {
            changedBitmap = sampleBitmap;
        }
        return changedBitmap;
    }

}
