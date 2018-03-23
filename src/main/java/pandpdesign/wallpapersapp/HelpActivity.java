package pandpdesign.wallpapersapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import pandpdesign.wallpaperslowsdk.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);
        TextView txt=(TextView)findViewById(R.id.helpText);
        txt.setText(Html.fromHtml("<h1>Help</h1>\n" +
                "<h4>How to use our aplication:<h4>\n" +
                "<p>First step: You can use the galery to set a wallpaper to your phone sreen, just press the image that you like or touch once and confirm!<br><br>Second step: You can upload an image from Imgur or any link that contains an image and then this will be displayed in the gallery and ready to be set as a wallpaper!</p>"
        ));
    }
    @Override
    public void onBackPressed()
    {
        Intent mainIntent= new Intent(HelpActivity.this,Menu.class);
        startActivity(mainIntent);
        finish();
    }
}
