package pandpdesign.wallpapersapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pandpdesign.wallpaperslowsdk.R;

public class Menu extends AppCompatActivity {
    private RelativeLayout mealLayout;
    private ColorDrawable col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu);
        mealLayout = (RelativeLayout) findViewById(R.id.layout);

        mealLayout.setBackgroundColor(Color.WHITE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Drawable background = (Drawable) mealLayout.getBackground();
            col = (ColorDrawable) background;

            Timer timer = new Timer();

            MyTimer mt = new MyTimer();
            timer.schedule(mt, 2700, 2700);
        }
    }

    class MyTimer extends TimerTask {

        public void run() {


            runOnUiThread(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void run() {
                    Random rand = new Random();
                    ColorDrawable newCol = new ColorDrawable(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));

                    ColorDrawable[] color = {col, newCol};
                    col = newCol;
                    TransitionDrawable trans = new TransitionDrawable(color);
                    mealLayout.setBackground(trans);
                    trans.startTransition(2500);

                }
            });
        }
    }


    public void goGallery(View v) {
        if (isNetworkAvailable()) {
            Intent mainIntent = new Intent(Menu.this, WallpaperGalleryActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            Toast.makeText(Menu.this, "You don't have internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goUpload(View v) {
        if (isNetworkAvailable()) {
            Intent mainIntent = new Intent(Menu.this, WallpaperUpload.class);
            startActivity(mainIntent);
            finish();
        } else {
            Toast.makeText(Menu.this, "You don't have internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goHelp(View v) {

        Intent mainIntent = new Intent(Menu.this, HelpActivity.class);
        startActivity(mainIntent);
        finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}


