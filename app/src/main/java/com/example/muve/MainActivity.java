package com.example.muve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/*
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
/*import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends Activity {

    TextView uv;
    Button but;
    TextView tpe;
    TextView fps;

    public static int mySt = 0;

    public static double lastLastLat = 0.0;
    public static double lastLastLng = 0.0;
    public static double degreeVar = 0.0;

    public static double lastLat = 0.0;
    public static double lastLng = 0.0;
    public static double lastAlt = 0.0;
    public static double lastUV = -1.0;
    public static int lastst1 = -1;
    public static int lastst2 = -1;
    public static int lastst3 = -1;
    public static int lastst4 = -1;
    public static int lastst5 = -1;
    public static int lastst6 = -1;
    public static double lastUVMax = -1.0;
    public static ZTime lastUVMaxTime = new ZTime();
    public static ZTime lastSolarNoon = new ZTime();

    public int i = 0;

    public static String fakeJson = "{\"result\":{\"uv\":0,\"uv_time\":\"2019-10-20T01:05:34.362Z\",\"uv_max\":13.3374,\"uv_max_time\":\"2019-10-19T15:24:35.942Z\",\"ozone\":273.3,\"ozone_time\":\"2019-10-20T00:06:37.604Z\",\"safe_exposure_time\":{\"st1\":25,\"st2\":45,\"st3\":75,\"st4\":99,\"st5\":121,\"st6\":145},\"sun_info\":{\"sun_times\":{\"solarNoon\":\"2019-10-19T15:24:35.942Z\",\"nadir\":\"2019-10-19T03:24:35.942Z\",\"sunrise\":\"2019-10-19T09:06:02.517Z\",\"sunset\":\"2019-10-19T21:43:09.367Z\",\"sunriseEnd\":\"2019-10-19T09:08:21.501Z\",\"sunsetStart\":\"2019-10-19T21:40:50.382Z\",\"dawn\":\"2019-10-19T08:43:29.850Z\",\"dusk\":\"2019-10-19T22:05:42.033Z\",\"nauticalDawn\":\"2019-10-19T08:17:04.474Z\",\"nauticalDusk\":\"2019-10-19T22:32:07.410Z\",\"nightEnd\":\"2019-10-19T07:50:18.285Z\",\"night\":\"2019-10-19T22:58:53.598Z\",\"goldenHourEnd\":\"2019-10-19T09:35:37.852Z\",\"goldenHour\":\"2019-10-19T21:13:34.032Z\"},\"sun_position\":{\"azimuth\":0.8967649826677742,\"altitude\":-0.7732137107920379}}}}\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        but = findViewById(R.id.button);
        uv = findViewById(R.id.uv);
        tpe = findViewById(R.id.tpe);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {

        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        2);
            }
        } else {
            System.out.println("Permissão préviamente concedida");
            final LocationManager lm = (LocationManager)
                    this.getSystemService(Context.LOCATION_SERVICE);

            System.out.println("Iniciando serviço de atualização de localização");
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 0, batato);
            if (lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
                batato.onLocationChanged(lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            else
                System.out.println("No old location");
        }

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UVInterpreter.notify(MainActivity.this);
                System.out.println("UV: " + lastUV
                    + "ST1~6: " + lastst1 + ", " + lastst2 + ", " + lastst3 + ", " + lastst4 + ", " + lastst5 + ", " + lastst6);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager lm = (LocationManager)
                            this.getSystemService(Context.LOCATION_SERVICE);

                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                        System.out.println("Permissão não concedida");
                        return;
                    }
                    System.out.println("Permissão concedida, iniciando serviço");
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 0, batato);

                } else {
                    System.out.println("Permissão bloqueada");
                }
                return;
            }
        }
    }

    LocationListener batato = new LocationListener() {
        @Override
        public void onLocationChanged (Location location){
            System.out.println("LOCATION CHANGED");
            lastLastLat = lastLat;
            lastLastLng = lastLng;
            lastLat = location.getLatitude();
            lastLng = location.getLongitude();
            lastAlt = location.getAltitude();

            degreeVar = Math.sqrt(Math.pow(lastLat - lastLastLat, 2) + Math.pow(lastLng - lastLastLng, 2));


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.openuv.io/api/v1/uv?lat="+lastLat+"&lng="+lastLng+"&alt="+lastAlt)
                    .get()
                    .addHeader("x-access-token", "38b82b597e0276bad387f3509484c757")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String strRes = response.body().string();
                System.out.println(strRes);
                try {
                    JsonParser parser = new JsonParser();
                    JsonObject jObject = parser.parse(strRes).getAsJsonObject();
                    updateProfileInfos(jObject);
                } catch (JSONException e) {
                    System.out.println("Could not load JSON.");
                }


            }
            catch(IOException ex)
            {
            }
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled (String provider){

        }

        @Override
        public void onProviderDisabled (String provider){

        }
    };

    public void updateProfileInfos(JsonObject jObject2) throws JSONException {
        JsonObject jObject = jObject2.getAsJsonObject("result");
        MainActivity.lastUV = jObject.get("uv").getAsDouble();
        MainActivity.lastUVMax = jObject.get("uv_max").getAsDouble();
        MainActivity.lastUVMaxTime = ZTime.timeParser(jObject.get("uv_max_time").getAsString());
        JsonObject jObject3 = jObject.getAsJsonObject("safe_exposure_time");
        try
        {
            MainActivity.lastst1 = jObject3.get("st1").getAsInt();
        }
        catch(Exception ex)
        {
            MainActivity.lastst1 = 0;
        }

        try {
            MainActivity.lastst2 = jObject3.get("st2").getAsInt();
        }
        catch(Exception ex)
        {
            MainActivity.lastst2 = 0;
        }

        try {
            MainActivity.lastst3 = jObject3.get("st3").getAsInt();
        }
        catch(Exception ex) {
            MainActivity.lastst3 = 0;
        }

        try{
            MainActivity.lastst4 = jObject3.get("st4").getAsInt();
        }
        catch(Exception ex)
        {
            MainActivity.lastst4 = 0;
        }

        try {
            MainActivity.lastst5 = jObject3.get("st5").getAsInt();
        }
        catch(Exception ex)
        {
            MainActivity.lastst5 = 0;
        }

        try {
            MainActivity.lastst6 = jObject3.get("st6").getAsInt();
        }
        catch(Exception ex)
        {
            MainActivity.lastst6 = 0;
        }
        UVInterpreter.notify(MainActivity.this);
        uv.setText("Nível de Incidência:\n" + UVInterpreter.getUVLevel(MainActivity.lastUV));
        int color = 0xFFFFFFFF;
        if(lastUV < 3)
            color = 0xFFFFFFFF;
        else if(lastUV < 6)
            color = 0xFF00FF00;
        else if(lastUV < 8)
            color = 0xFFFFFF00;
        else if(lastUV < 11)
            color = 0xFFFFBB00;
        else
            color = 0xFFFF0000;
        uv.setTextColor(color);

        if(mySt == 0)
            tpe.setText("Tempo de Exposição (SF): \n" + (lastst1 > 0 ? String.valueOf(lastst1) + "min" : "Suave"));
        if(mySt == 1)
            tpe.setText("Tempo de Exposição (SF): \n" + (lastst2 > 0 ? String.valueOf(lastst2) + "min" : "Suave"));
        if(mySt == 2)
            tpe.setText("Tempo de Exposição (SF): \n" + (lastst3 > 0 ? String.valueOf(lastst3) + "min" : "Suave"));
        if(mySt == 3)
            tpe.setText("Tempo de Exposição (SF): \n" + (lastst4 > 0 ? String.valueOf(lastst4) + "min" : "Suave"));
        if(mySt == 4)
            tpe.setText("Tempo de Exposição (SF): \n" + (lastst5 > 0 ? String.valueOf(lastst5) + "min" : "Suave"));
        if(mySt == 5)
            tpe.setText("Tempo de Exposição (SF): \n" + (lastst6 > 0 ? String.valueOf(lastst6) + "min" : "Suave"));


    }

    public void sendNotification(String title, String body) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,
                0 /* Request code */,
                i,
                PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pi);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(MainActivity.this.i++, builder.build());
    }
}
