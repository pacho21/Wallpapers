package pandpdesign.wallpapersapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import pandpdesign.wallpaperslowsdk.R;

/**
 * Created by Poli on 11/02/2018.
 */

public class WallpaperGalleryActivity extends AppCompatActivity {

    public static int NUMWPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy allowAll = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(allowAll);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wallpaper_gallery);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        WallpaperGalleryActivity.ImageGalleryAdapter adapter = new WallpaperGalleryActivity.ImageGalleryAdapter(this, Wallpaper.getWallpapers());
        recyclerView.setAdapter(adapter);
        int num = adapter.getItemCount();
        NUMWPS = num;

    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the layout
            View photoView = inflater.inflate(R.layout.item_wallpaper, parent, false);

            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        //loading the images to the item_wallpaper
        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            Wallpaper wpper = myWallpaperPhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(wpper.getImgUrl())
                    .placeholder(R.drawable.ic_cloud_download_black_24dp)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (myWallpaperPhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            //setting listeners so when we click we go to the image
            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.item_wp);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Wallpaper wpper = myWallpaperPhotos[position];
                    //send the image (wpper) to the WpActivity...->
                    Intent intent = new Intent(mContext, WallpaperActivity.class);
                    intent.putExtra(WallpaperActivity.EXTRA_WP, wpper);
                    startActivity(intent);
                }
            }
        }

        private Wallpaper[] myWallpaperPhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, Wallpaper[] wallPhotos) {
            mContext = context;
            myWallpaperPhotos = wallPhotos;
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(WallpaperGalleryActivity.this, Menu.class);
        startActivity(mainIntent);
        finish();





}


}