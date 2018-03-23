package pandpdesign.wallpapersapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Poli on 11/02/2018.
 */

public class Wallpaper implements Parcelable {

    private String imgUrl;
    private String imgRatio;

    public Wallpaper(String url, String ratio) {
        this.imgUrl = url;
        this.imgRatio = ratio;
    }

    //========== Parcel class creator ======

    protected Wallpaper(Parcel in) {
        this.imgUrl = in.readString();
        this.imgRatio = in.readString();
    }


    public static final Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel in) {
            return new Wallpaper(in);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };

    //============= Setter y Getter ==========

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String url) {
        this.imgUrl = url;
    }

    public String getImgRatio() {
        return this.imgRatio;
    }

    public void setImgRatio(String ratio) {
        this.imgRatio = ratio;
    }

    public static Wallpaper[] getWallpapers() {
        return parsJson();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.imgUrl);
        parcel.writeString(this.imgRatio);
    }

    //We load the txt from the Servlet
    public static String getJson() {

        URL url;
        HttpURLConnection urlConnection = null;
        StringBuilder html = new StringBuilder();
        try {
            url = new URL("http://35.204.4.192/Wallpapers/GetAllImages");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                html.append(line);
            }
            in.close();
            return html.toString();
        } catch (Exception ree) {
            ree.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    //we parse the HTML to "Wallpaper"
    public static Wallpaper[] parsJson() {
        try {
            JSONArray jsonArray;
            JSONParser parser = new JSONParser();
            JSONObject jobj;
            Object obj = parser.parse(getJson());
            jsonArray = (org.json.simple.JSONArray) obj;
            Wallpaper[] sps = new Wallpaper[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                jobj = (JSONObject) jsonArray.get(i);
                //for each object in the jsonArray we create one Wallpaper...->xº
                sps[i] = new Wallpaper(jobj.get("imgLink").toString(), jobj.get("imgRatio").toString());
            }
            return sps;
        } catch (Exception e) {

        }
        return null;
    }

}
