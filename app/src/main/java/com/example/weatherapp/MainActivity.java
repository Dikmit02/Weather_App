package com.example.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ImageView weather;
    ImageView idofweather;
    Button button;
    TextView textView2;
    EditText edittext;
    String ans;
    String temp;
    String pressure;
    String humidity;
    int again=0;

    public void checkagain(View view){
        again=1;
        idofweather.setVisibility(View.INVISIBLE);
        weather.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        edittext.setVisibility(View.VISIBLE);
        againfunctioncall();

    }


    public void againfunctioncall()
    {
        if(again==0||again==1)
        {
            textView2.setVisibility(View.INVISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weather.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    edittext.setVisibility(View.INVISIBLE);
                    DownloadTask task = new DownloadTask();
                    InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(edittext.getWindowToken(),0);

                    task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + edittext.getText().toString() + "&units=metric&appid=92b6eee1b96fa751857c27add2f82bd7");


                }
            });

        }
    }
        public class DownloadImage extends  AsyncTask<String,Void, Bitmap>{

            @Override
            protected Bitmap doInBackground(String... strings) {

                URL url;
                HttpsURLConnection httpsURLConnection=null;

                try {
                    url=new URL(strings[0]);

                    Log.i("imageee", String.valueOf(url));

                    httpsURLConnection=(HttpsURLConnection)url.openConnection();
                    Log.i("imageee1","imageee");
                   // httpsURLConnection.connect();
                    Log.i("imageee2","imageee");

                    InputStream inputStream=httpsURLConnection.getInputStream();
                    Log.i("imageee3","imageee");
                    Bitmap myimage= BitmapFactory.decodeStream(inputStream);
                    Log.i("imageee4","imageee");
                    return myimage;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                return null;
            }

        }
        public class DownloadTask extends AsyncTask<String ,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection=null;

            try {
                url=new URL(strings[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();


                InputStream inputStream=httpURLConnection.getInputStream();

                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);


                int data=inputStreamReader.read();
                while(data!=-1)
                {
                    char cc=(char)data;
                    result+=cc;
                    data=inputStreamReader.read();
                }


                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "NO STRING";
            } catch (IOException e) {
                e.printStackTrace();
                return "NO STRING";
            }



        } @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //{"coord":{"lon":77.22,"lat":28.65},"weather":[{"id":701,"main":"Mist","description":"mist","icon":"50n"}],"base":"stations","main":
                //{"temp":16,"pressure":1008,"humidity":87,"temp_min":16,"temp_max":16},"visibility":2500,"wind":{"speed":2.1,"deg":130},"clouds":{"all":40},"dt":1550518200,"sys":
                //{"type":1,"id":9165,"message":0.0048,"country":"IN","sunrise":1550453198,"sunset":1550493825},"id":1273294,"name":"Delhi","cod":200}
                 

                try {

                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject jsonObject1=jsonObject.getJSONObject("main");


                    temp=jsonObject1.getString("temp");
                    pressure=jsonObject1.getString("pressure");
                    humidity=jsonObject1.getString("humidity");


                     Log.i("temp",temp);
                    Log.i("pressure",pressure);
                    Log.i("humidity",humidity);
                    textView2.setText("TEMPERATURE: "+temp+" @C\nPRESSURE: "+pressure+"Pa\nHUMIDITY: "+humidity);


                    JSONArray jsonArray=jsonObject.getJSONArray("weather");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonnew = jsonArray.getJSONObject(i);
                        String id = jsonnew.getString("icon");
                        String description = jsonnew.getString("description");

                        Log.i("idddd", id);
                        Log.i("description", description);

                        Log.i("NORESULT", "cannot return image");
                        DownloadImage imagetask = new DownloadImage();
                        Log.i("NORESULT11", "cannot return image");
                        Bitmap image;
                        if(again==0||again==1)
                        {
                        try {

                            image = imagetask.execute("https://openweathermap.org/img/w/" + id + ".png").get();
                            Log.i("prrrrrrrrrrrr", String.valueOf(image));
                            idofweather.setImageBitmap(image);
                            idofweather.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "CLICK ON WEATHER ICON TO CHECK AGAIN ", Toast.LENGTH_SHORT).show();


                        } catch (InterruptedException e) {
                            Log.i("NOO", "NOOOOO");

                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            Log.i("YES", "YES");
                            e.printStackTrace();
                        }

                    }


                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        weather = (ImageView) findViewById(R.id.imageView2);
        textView2 = (TextView) findViewById(R.id.textView2);

        edittext = (EditText) findViewById(R.id.editText);
        idofweather = (ImageView) findViewById(R.id.imageView3);

        againfunctioncall();



    }
}

//mmmmmmmit











