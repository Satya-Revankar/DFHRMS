package com.dfhrms.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.dfhrms.Class.Class_geolocation;
import com.dfhrms.Utili.ConnectionDetector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dfhrms.GPS.GPSTracker;
import com.dfhrms.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Trainer_Geofence extends FragmentActivity implements
        OnMapClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private ProgressDialog progressDialog;
    private Handler handler;
    private static final int MAX_WAIT_TIME = 10000;
    private double currentRadius = -1;
    GPSTracker gpstracker_obj1;
    Double double_currentlatitude = 0.0;
    Double double_currentlongitude = 0.0;

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Marker myMarker;
    // CircleOptions circleOptions;

    CircleOptions[] circleOptions_arry;
    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;
    String str_geolocation_response="error";
    SoapObject soapobj_geolocation_response;
    int count;
    String str_username,str_pwd,str_empid;
    private long Employeeidlong;
    TextView username_tv;
    Button back_bt,punch_bt;

    double latitude_async = 0.0;
    double longitude_async =0.0;

    private String str_validateAttendance_response, str_signout_response, str_signin_response, currentdate_yyyyMMdd,
            str_classscheduled_response;

    String str_punch;

    Class_geolocation class_geolocation_arry[];
    int int_i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_geofence);

        System.out.println("Trainer geofence is called");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false); // Prevent dismissal on outside touch
        handler = new Handler();
        str_punch = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {

            str_punch = extras.getString("punch");

/*
//			Log.d("notMsag",notMsg);
            if (notMsg != null && !notMsg.isEmpty()) {
                if (notMsg.contains("mark")) {
                    displayView(4);
                }
*/
        }


        //Toast.makeText(getApplicationContext(),""+str_punch,Toast.LENGTH_SHORT).show();


        SharedPreferences myprefs = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        str_username = myprefs.getString("user1", "nothing");
        str_pwd = myprefs.getString("pwd", "nothing");
        str_empid = myprefs.getString("emp_id", "nothing");
        Employeeidlong = Long.parseLong(str_empid); // for web service
        username_tv=(TextView)findViewById(R.id.username_tv);
        back_bt=(Button)findViewById(R.id.back_bt);
        punch_bt=(Button)findViewById(R.id.punch_bt);
        getLocationAndShowProgressDialog();
        username_tv.setText(str_username);



       /* username_tv.setText("madhura.halakarni@dfmail.org");
        Employeeidlong = Long.parseLong("1703");
*/

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.dfhrms.R.id.mark_areaonmap_fg);
        mapFragment.getMapAsync(this);






        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Trainer_Geofence.this);
                dialog.setCancelable(false);
                dialog.setTitle(R.string.alert);
                dialog.setMessage("Are you sure want to go back");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("passed_lat", String.valueOf(latitude_async)); // Change "YourValue" to the value you want to store
                                editor.putString("passed_long", String.valueOf(longitude_async));
                                editor.apply();

                                System.out.println("Passing Lat and long : "+latitude_async+longitude_async);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
                    }
                });
                alert.show();


            }
        });

        boolean showButton = getIntent().getBooleanExtra("showButton", true);

        System.out.println("Punch button : "+showButton);
        if (showButton) {
            punch_bt.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) back_bt.getLayoutParams();


            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            // Apply the updated layout parameters to the button
            back_bt.setLayoutParams(layoutParams);
            // Show the button
        } else {
            punch_bt.setVisibility(View.GONE); // Hide the button
        }
        if(str_punch.equalsIgnoreCase("signin"))
        {
            punch_bt.setText("Get Location");
        }
        if(str_punch.equalsIgnoreCase("signout"))
        {
            punch_bt.setText("Punch-OUT");
        }


        punch_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Location findme = mMap.getMyLocation();

                    // Dismiss the progress dialog when a valid location is obtained
                    progressDialog.dismiss();

                    // Process the obtained location
                    latitude_async = findme.getLatitude();
                    longitude_async = findme.getLongitude();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("passed_lat", String.valueOf(latitude_async)); // Change "YourValue" to the value you want to store
                    editor.putString("passed_long", String.valueOf(longitude_async));
                    editor.apply();
                    System.out.println("Passing Lat and long : " + latitude_async + longitude_async);
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Location obtained: Lat: " + latitude_async + " Long: " + longitude_async, Toast.LENGTH_SHORT).show();
                locationset();
            }
        });



    }//oncreate()



    public void locationset()
    {
      /*  mMap.setOnMyLocationButtonClickListener(Geofencing_Activity.this);
        mMap.setOnMapClickListener(Geofencing_Activity.this);*/
        onMyLocationButtonClick();
    }

    @Override
    /*public void onMapReady(GoogleMap googleMap) {

    }*/

    public void onMapReady(final GoogleMap googleMap)
    {


        gpstracker_obj1 = new GPSTracker(Trainer_Geofence.this);
        if (gpstracker_obj1.canGetLocation())
        {
            double_currentlatitude = gpstracker_obj1.getLatitude();
            double_currentlongitude = gpstracker_obj1.getLongitude();

        }


//        if (str_fromname.equalsIgnoreCase("addfarmpond"))
//        {
//            double_currentlatitude = Double.valueOf(str_lat);
//            double_currentlongitude = Double.valueOf(str_long);
//        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);




        mMap = googleMap;

        // LatLng Currentlocation = new LatLng(double_currentlatitude, double_currentlongitude);

        LatLng Currentlocation = new LatLng(double_currentlatitude, double_currentlongitude);

        if (myMarker == null)
        {
           /* myMarker = googleMap.addMarker(new MarkerOptions()
                    .position(Currentlocation)
                    .title("You are here"));*/

            //  Toast.makeText(getApplicationContext(),"mymarker null",Toast.LENGTH_SHORT).show();

        }else{
            myMarker = googleMap.addMarker(new MarkerOptions()
                    .position(Currentlocation)
                    .title("You are here"));
        }



        LatLng latLng = mMap.getCameraPosition().target;

        //LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Log.e("Lat", String.valueOf(latLng.latitude));
        Log.e("Long", String.valueOf(latLng.longitude));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(Currentlocation));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        float zoomLevel = 20.0f; //This goes up to 21
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Currentlocation, zoomLevel));

        // drawCircle(new LatLng(15.370988, 75.123490),googleMap);



       /* mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                myMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("You are here")
                        .snippet(String.valueOf(latLng)));
            }
        });
*/
        mMap.setOnMapClickListener(this);
        //mMap.setOnMarkerClickListener(this);
        // googleMap.setOnMapLongClickListener(this);

        mMap.setOnMyLocationButtonClickListener(this);

        getgeolocation();
//anyType{UserLatLong=anyType{Locationdetails=anyType{Latitude=15.37107; Longitude=75.12360; Radius=0.03; Location=DCSE Building; }; Locationdetails=anyType{Latitude=15.353684; Longitude=75.066531; Radius=0.06; Location=DET Hubli; }; Locationdetails=anyType{Latitude=18.082809; Longitude=78.856955; Radius=0.02; Location=Siddipeth; }; Locationdetails=anyType{Latitude=15.354421; Longitude=75.078309; Radius=0.5; Location=Sandbox Startup; }; }; Status=true; Message=Success; }


    }



    @Override
    public void onMapClick(LatLng latLng) {

    }



    //private void drawCircle(LatLng point,GoogleMap googleMap)
    private void drawCircle(LatLng point,GoogleMap googleMap,int int_radius)
    {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();



        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        //circleOptions.radius(15);


        circleOptions.radius(int_radius);

        // circleOptions.radius(5);

        // Border color of the circle
        circleOptions.strokeColor(Color.GREEN);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // circleOptions.fillColor(android.R.color.holo_green_dark);
        // Border width of the circle
        circleOptions.strokeWidth(5);

        //double latitude = circleOptions.getCenter().latitude;



        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);


    }


/*
    @Override
    public boolean onMyLocationButtonClick()
   // public void onMyLocationButtonClick()
    {

        Location findme = mMap.getMyLocation();
        double latitude = findme.getLatitude();
        double longitude = findme.getLongitude();


        LatLng latLng = new LatLng(latitude, longitude);


        Log.e("Lat", String.valueOf(findme.getLatitude()));
        Log.e("Long", String.valueOf(findme.getLongitude()));


         latitude_async = findme.getLatitude();
         longitude_async = findme.getLongitude();

         Toast.makeText(getApplicationContext(),"Lat: "+latitude_async+" "+"Long: "+longitude_async,Toast.LENGTH_SHORT).show();


            float[] distance = new float[count];
        ArrayList<String> list1=new ArrayList<String>();//Creating arraylist
      //  list1.clear();
                    for(int i=0;i<class_geolocation_arry.length;i++)
                    {
                        CircleOptions circleOptions_inner = new CircleOptions();
                        double doublecurrentlatitude = 0,doublecurrentlongitude=0;
                        doublecurrentlatitude= Double.parseDouble(class_geolocation_arry[i].getStr_geolat());
                        doublecurrentlongitude= Double.parseDouble(class_geolocation_arry[i].getStr_geolong());

                        Log.e("radius alloted",class_geolocation_arry[i].getStr_radius());

                        Double value1=Double.valueOf(class_geolocation_arry[i].getStr_radius());
                        value1=value1*1000;
                        int val_int=value1.intValue();
                        //Log.e("Int", String.valueOf(val_int));
                        //int int_radius= Double.valueOf(str_lat);
                        int int_radius=val_int;

                        //int int_radius= (int) circleOptions_inner.getRadius();
                        LatLng point=new LatLng(doublecurrentlatitude,doublecurrentlongitude);

                        circleOptions_inner.center(point);

                        circleOptions_inner.radius(int_radius);

                       // circleOptions_inner.radius(5);

                        float[] distance1 = new float[1];

                        Location.distanceBetween(findme.getLatitude(), findme.getLongitude(),
                                circleOptions_inner.getCenter().latitude,
                                circleOptions_inner.getCenter().longitude, distance1);

                        Log.e("distance1", String.valueOf(i)+" "+String.valueOf(distance1[0]));
                        Log.e("radius1", String.valueOf(i)+" "+circleOptions_inner.getRadius());

                        if( distance1[0] > circleOptions_inner.getRadius()  )
                        {
                            list1.add("no");

                        } else {
                            list1.add("yes");
                        }
                    }

                    for(int i=0;i<list1.size();i++)
                    {
                            Log.e("flist1",list1.get(i));
                    }

        boolean yes_bol=list1.contains("yes");



        int_i=int_i+1;
        if(int_i>=2) {
            if (yes_bol) {
                if (str_punch.equalsIgnoreCase("signout")) {

                    AsyncCall_MarkEmployeeOutTime task = new AsyncCall_MarkEmployeeOutTime(Geofencing_Activity.this);
                    task.execute();
                }

                if (str_punch.equalsIgnoreCase("signin")) {
                    AsyncCall_MarkEmployee_PunchINTime task = new AsyncCall_MarkEmployee_PunchINTime(Geofencing_Activity.this);
                    task.execute();
                }

                Toast.makeText(getBaseContext(), "You are in office", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "You are not in office", Toast.LENGTH_SHORT).show();
            }

        }else{

            Toast.makeText(getBaseContext(), "Kindly click once again", Toast.LENGTH_SHORT).show();
        }



        for(int i=0;i<list1.size();i++)
        {
            Log.e("listvalue",list1.get(i));
        }

        return false;
    }
*/


    @Override
    public boolean onMyLocationButtonClick() {
        Location findme = mMap.getMyLocation();
        double latitude = findme.getLatitude();
        double longitude = findme.getLongitude();

        Log.e("Lat", String.valueOf(findme.getLatitude()));
        Log.e("Long", String.valueOf(findme.getLongitude()));

        latitude_async = findme.getLatitude();
        longitude_async = findme.getLongitude();

        Toast.makeText(getApplicationContext(),"Lat: "+latitude_async+" "+"Long: "+longitude_async,Toast.LENGTH_SHORT).show();

        ArrayList<Integer> inRadiusIndexes = new ArrayList<>();

        for (int i = 0; i < class_geolocation_arry.length; i++) {
            CircleOptions circleOptions_inner = new CircleOptions();
            double doublecurrentlatitude = Double.parseDouble(class_geolocation_arry[i].getStr_geolat());
            double doublecurrentlongitude = Double.parseDouble(class_geolocation_arry[i].getStr_geolong());

            Log.e("radius alloted",class_geolocation_arry[i].getStr_radius());

            Double value1 = Double.valueOf(class_geolocation_arry[i].getStr_radius());
            value1 = value1 * 1000;
            int val_int = value1.intValue();

            int int_radius = val_int;

            LatLng point = new LatLng(doublecurrentlatitude, doublecurrentlongitude);

            circleOptions_inner.center(point);
            circleOptions_inner.radius(int_radius);

            float[] distance1 = new float[1];

            Location.distanceBetween(findme.getLatitude(), findme.getLongitude(),
                    circleOptions_inner.getCenter().latitude,
                    circleOptions_inner.getCenter().longitude, distance1);

            Log.e("distance1", String.valueOf(i)+" "+String.valueOf(distance1[0]));
            Log.e("radius1", String.valueOf(i)+" "+circleOptions_inner.getRadius());

            if (distance1[0] > circleOptions_inner.getRadius()) {
                // Outside the radius
            } else {
                // Inside the radius
                inRadiusIndexes.add(i); // Add the index of the current location
            }
        }

        for (int i = 0; i < inRadiusIndexes.size(); i++) {
            int inRadiusIndex = inRadiusIndexes.get(i);
            Log.e("InRadiusIndex", String.valueOf(inRadiusIndex));

            // Access class_geolocation_arry[inRadiusIndex] to get details about that radius
            Log.e("RadiusDetails", "Latitude: " + class_geolocation_arry[inRadiusIndex].getStr_geolat() +
                    ", Longitude: " + class_geolocation_arry[inRadiusIndex].getStr_geolong() +
                    ", Radius: " + class_geolocation_arry[inRadiusIndex].getStr_radius());

            saveCurrentRadius(Double.parseDouble(class_geolocation_arry[inRadiusIndex].getStr_radius()));

        }

        boolean yes_bol = !inRadiusIndexes.isEmpty();

        int_i = int_i + 1;
        if (int_i >= 2) {
            if (yes_bol) {
                if (str_punch.equalsIgnoreCase("signout")) {
                    AsyncCall_MarkEmployeeOutTime task = new AsyncCall_MarkEmployeeOutTime(Trainer_Geofence.this);
                    task.execute();
                }
                if (str_punch.equalsIgnoreCase("signin")) {
                    AsyncCall_MarkEmployee_PunchINTime task = new AsyncCall_MarkEmployee_PunchINTime(Trainer_Geofence.this);
                    task.execute();
                }
                Toast.makeText(getBaseContext(), "You are in office", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "You are not in office", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Kindly click once again", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void saveCurrentRadius(double radius) {
        SharedPreferences myprefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myprefs.edit();
        editor.putFloat("currentRadius", (float) radius);
        editor.apply();
    }



    public void getgeolocation()
    {
        internetDectector = new ConnectionDetector(getApplicationContext());
        isInternetPresent = internetDectector.isConnectingToInternet();

        if (isInternetPresent) {
            //getgeolocationinfo_new();
//            GetVillageLocationTask task = new GetVillageLocationTask(Activity_MarkerGoogleMaps.this);
//            task.execute();
            AsyncCall_getgeolocationinfo task = new AsyncCall_getgeolocationinfo(Trainer_Geofence.this);
            task.execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }


    }



    private class AsyncCall_getgeolocationinfo extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("DFTech", "doInBackground");
            //  GetAllEvents();
            fetch_geolocation(); //
            return null;
        }

        public AsyncCall_getgeolocationinfo(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            //dialog.dismiss();

            /*property                com.dfhrms                           E  anyType{Locationdetails=anyType{Latitude=15.37107; Longitude=75.12360; Radius=0.03; Location=DCSE Building; };
             Locationdetails=anyType{Latitude=15.353684; Longitude=75.066531; Radius=0.06; Location=DET Hubli; };
              Locationdetails=anyType{Latitude=18.082809; Longitude=78.856955; Radius=0.02; Location=Siddipeth; };
               Locationdetails=anyType{Latitude=15.354421; Longitude=75.078309; Radius=0.5; Location=Sandbox Startup; }; }
            2023-07-26 11:57:03.581 26409-26409 property                com.dfhrms                           E  true
            2023-07-26 11:57:03.581 26409-26409 property                com.dfhrms                           E  Success
*/
            /*anyType{Latitude=15.37107; Longitude=75.12360; Radius=0.03; Location=DCSE Building; }
            2023-07-26 12:12:35.523 28525-28525 property                com.dfhrms                           E  anyType{Latitude=15.353684; Longitude=75.066531; Radius=0.06; Location=DET Hubli; }
            2023-07-26 12:12:35.523 28525-28525 property                com.dfhrms                           E  anyType{Latitude=18.082809; Longitude=78.856955; Radius=0.02; Location=Siddipeth; }
            2023-07-26 12:12:35.523 28525-28525 property                com.dfhrms                           E  anyType{Latitude=15.354421; Longitude=75.078309; Radius=0.5; Location=Sandbox Startup; }*/



            if(count>0) {
                class_geolocation_arry = new Class_geolocation[count];
            }
            for(int i=0;i<count;i++)
            {
                //anyType{Latitude=15.353684; Longitude=75.066531; Radius=0.06; Location=DET Hubli; }
                Log.e("property", String.valueOf(soapobj_geolocation_response.getProperty(i)));

                SoapObject list = (SoapObject) soapobj_geolocation_response.getProperty(i);
                SoapPrimitive lat_sp, Longitude_sp, radius_sp, location_sp;
                String str_approvalstatus;
                String approved = "";
                lat_sp = (SoapPrimitive) list.getProperty("Latitude");
                Longitude_sp = (SoapPrimitive) list.getProperty("Longitude");
                radius_sp = (SoapPrimitive) list.getProperty("Radius");
                location_sp = (SoapPrimitive) list.getProperty("Location");

                Class_geolocation innerObj = new Class_geolocation();

                innerObj.setStr_geolat(lat_sp.toString());
                innerObj.setStr_geolong(Longitude_sp.toString());
                innerObj.setStr_radius(radius_sp.toString());
                innerObj.setStr_location(location_sp.toString());

                class_geolocation_arry[i] = innerObj;

            }

            if(class_geolocation_arry.length>0)
            {

                circleOptions_arry= new CircleOptions[count];
                for(int i=0;i<class_geolocation_arry.length;i++)
                {
                    //Double double_radius = 0.0;


                    String str_lat =class_geolocation_arry[i].getStr_geolat();
                    String str_long=class_geolocation_arry[i].getStr_geolong();

                    Double value1=Double.valueOf(class_geolocation_arry[i].getStr_radius());
                    value1=value1*1000;
                    int val_int=value1.intValue();
                    Log.e("Int", String.valueOf(val_int));
                    //int int_radius= Double.valueOf(str_lat);
                    int int_radius=val_int;
                    Double doublecurrentlatitude = Double.valueOf(str_lat);
                    Double doublecurrentlongitude = Double.valueOf(str_long);

                    //drawCircle(new LatLng(15.370988, 75.123490),googleMap);




                    // Specifying the center of the circle
                    LatLng point=new LatLng(doublecurrentlatitude,doublecurrentlongitude);
                    CircleOptions circleOptions_inner = new CircleOptions();

                    circleOptions_inner.center(point);
                    circleOptions_inner.radius(int_radius);
                    circleOptions_arry[i]=circleOptions_inner;

                    drawCircle(new LatLng(doublecurrentlatitude,doublecurrentlongitude),
                            mMap,int_radius);
                }
            }

            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task





    public void fetch_geolocation() {
/*
        String str_currentlatitude = String.valueOf(currentlatitude_out);
        String str_currentlongitude = String.valueOf(currentlongitude_out);*/


       /* String str_currentlatitude = String.valueOf(currentlatitude_out_new);
        String str_currentlongitude = String.valueOf(currentlongitude_out_new);
*/
        //https://dfhrms.dfindia.org/PMSService.asmx/GetEmployeeLocations?EmployeeId=948
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);
//http://tempuri.org/GetEmployeeLocations
        String METHOD_NAME = "GetEmployeeLocations";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeLocations";
        try {

			 /*<GetEmployeeLocations xmlns="http://tempuri.org/">
      <EmployeeId>long</EmployeeId>
    </GetEmployeeLocations>*/


            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("EmployeeId", Employeeidlong);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                soapobj_geolocation_response = (SoapObject) envelope.getResponse();
                // SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                str_geolocation_response = soapobj_geolocation_response.toString().trim();

                soapobj_geolocation_response = (SoapObject) envelope.getResponse();
                Log.e("HolidayList response", soapobj_geolocation_response.toString());
                soapobj_geolocation_response=(SoapObject) soapobj_geolocation_response.getProperty(0);

                count = soapobj_geolocation_response.getPropertyCount();




                // SignOut resp: Out time updated.
                // SignOut resp: location is not valid


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_geolocation_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_geolocation_response = t.getMessage();

        }

    }


    @Override
    public void onBackPressed()
    {

        Toast.makeText(getApplicationContext(),"Kindly Click on Back or Punch button",Toast.LENGTH_LONG).show();
    }




    private class AsyncCall_MarkEmployeeOutTime extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("DFTech", "doInBackground");
            //  GetAllEvents();
            MarkEmployeeOutTime_insertion(); //Mark the employee signout time
            return null;
        }

        public AsyncCall_MarkEmployeeOutTime(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            if (str_signout_response.equalsIgnoreCase("Out time updated.") || str_signout_response.equalsIgnoreCase("Out time updated")) {

                alerts_dialog_for_ThankYou("Thank You For Punching OUT", Double.toString(latitude_async),
                        Double.toString(longitude_async));
                // Toast.makeText(getActivity(),"Sign out has been updated",Toast.LENGTH_LONG).show();

            } else if (str_signout_response.equalsIgnoreCase("location is not valid") || str_signout_response.equalsIgnoreCase("location is not valid.")) {
                alerts_dialog_location_OutOfRange(String.valueOf(latitude_async), String.valueOf(longitude_async));
            } else if (str_signout_response.equalsIgnoreCase("Working location not assigned") ||
                    str_signin_response.equalsIgnoreCase("Working location not assigned.")) {
                alerts_dialog_forSignIN_location_NotAssigned();
            } else {
                alerts_dialog_Error(str_signout_response, "MarkEmployeeOutTime");
            }

            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task



    public void MarkEmployeeOutTime_insertion() {
/*
        String str_currentlatitude = String.valueOf(currentlatitude_out);
        String str_currentlongitude = String.valueOf(currentlongitude_out);*/


        String str_currentlatitude = String.valueOf(latitude_async);
        String str_currentlongitude = String.valueOf(longitude_async);

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "MarkEmployeeOutTime";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkEmployeeOutTime";
        try {

			/*<ValidateAttendance xmlns="http://tempuri.org/">
      <id>long</id>    </ValidateAttendance>*/

			/*<employee_Id>long</employee_Id>
      <lat>string</lat>
      <lon>string</lon>*/


            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("employee_Id", Employeeidlong);
            request.addProperty("lat", str_currentlatitude);
            request.addProperty("lon", str_currentlongitude);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                str_signout_response = response.toString().trim();

                Log.e("SignOut resp", response.toString());
                // SignOut resp: Out time updated.
                // SignOut resp: location is not valid


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_signout_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_signout_response = t.getMessage();

        }

    }





    private class AsyncCall_MarkEmployee_PunchINTime extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("DFTech", "doInBackground");

            MarkEmployee_PunchINTime_insertion(); //Mark the employee IN time

            // MarkEmployee_PunchINTime_insertion_onlypunchIN(); // Only PunchIN

            return null;
        }

        public AsyncCall_MarkEmployee_PunchINTime(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            //str_signin_response

            if (str_signin_response.equalsIgnoreCase("Attendance Marked") || str_signin_response.equalsIgnoreCase("Attendance Marked.")) {

                alerts_dialog_for_ThankYou("Thank You For Punching IN", String.valueOf(latitude_async),
                        String.valueOf(longitude_async));

                // Toast.makeText(getActivity(),"Sign in has been updated",Toast.LENGTH_LONG).show();

            } else if (str_signin_response.equalsIgnoreCase("location is not valid") || str_signin_response.equalsIgnoreCase("location is not valid.")) {
                alerts_dialog_location_OutOfRange(String.valueOf(latitude_async), String.valueOf(longitude_async));

            } else if (str_signin_response.equalsIgnoreCase("Working location not assigned") || str_signin_response.equalsIgnoreCase("Working location not assigned.")) {
                alerts_dialog_forSignIN_location_NotAssigned();
            } else {
                alerts_dialog_Error(str_signin_response, "MarkSelfAttendance");
            }


            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task




    public void MarkEmployee_PunchINTime_insertion() {

        String str_currentlatitude = String.valueOf(latitude_async);
        String str_currentlongitude = String.valueOf(longitude_async);
        long long_userid = 0;


        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "MarkSelfAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkSelfAttendance";
        try {

			/*<MarkSelfAttendance xmlns="http://tempuri.org/">
      <employee_id>long</employee_id>
      <date>string</date>
      <userId>long</userId>
      <lat>string</lat>
      <lon>string</lon>
    </MarkSelfAttendance>*/


           /* 15.368345
            75.807045
            Invalid location  */

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("employee_id", Employeeidlong);
            request.addProperty("date", currentdate_yyyyMMdd);
            request.addProperty("userId", long_userid);
           /* request.addProperty("lat", "15.368345");
            request.addProperty("lon", "75.807045");*/
            request.addProperty("lat", str_currentlatitude);
            request.addProperty("lon", str_currentlongitude);


            Log.e("SignINRequest", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //		result1 = (Vector<SoapObject>) envelope.getResponse();

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                str_signin_response = response.toString().trim();

                Log.e("Signin resp: ", response.toString());
                // Signin resp: location is not valid
                //// Signin resp: Working location not assigned
                // Signin resp: Attendance Marked


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_signin_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_signin_response = t.getMessage();

        }

    }








    public void alerts_dialog_for_ThankYou(String thankyou, String lat, String longitude)
    {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //2018-12-18
        String currentdate_ddMMyyyy = df.format(c);

        AlertDialog.Builder dialog = new AlertDialog.Builder(Trainer_Geofence.this);
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("" + thankyou + "\n" + "\n" + "Lat:" + lat + "\n" + "Long:" + longitude
                +"\n"+str_username+"\n"+currentdate_ddMMyyyy);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

                // FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
                /*HomeFragment regcomplainfragment = new HomeFragment();
                fragmenttransaction.replace(R.id.content_frame, regcomplainfragment).addToBackStack("HomeFragment");
                fragmenttransaction.commit();
*/
               /* Fragment_MarkAttndnceClassTakenNew fragment = new Fragment_MarkAttndnceClassTakenNew();
                fragmenttransaction.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName()).
                        addToBackStack(null).commit();
*/


                /*Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTakenNew();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();
*/



                Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                startActivity(i);
                finish();

                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();*/
            }
        });


        final AlertDialog alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();
    }


    public void alerts_dialog_location_OutOfRange(String lat, String longitude) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //2018-12-18
        String currentdate_ddMMyyyy = df.format(c);

        AlertDialog.Builder dialog = new AlertDialog.Builder(Trainer_Geofence.this);
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Sorry! Not in office location range." + "\n" + "\n" + "lat:" + lat + "\n" +
                "long:" + longitude+"\n"+str_username+"\n"+currentdate_ddMMyyyy);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

               /* Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTakenNew();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();
*/

                /*Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                startActivity(i);
                finish();*/
            }
        });


        final AlertDialog alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();


    }


    public void alerts_dialog_forSignIN_location_NotAssigned()
    {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //2018-12-18
        String currentdate_ddMMyyyy = df.format(c);


        AlertDialog.Builder dialog = new AlertDialog.Builder(Trainer_Geofence.this);
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Work location not assigned, Contact Technology Department!"+"\n"+
                str_username+"\n"+currentdate_ddMMyyyy);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                /*Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTakenNew();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();

*/

                /*Intent i = new Intent(getActivity(), Slide_MainActivity.class);
                i.putExtra("mark", "mark");
                startActivity(i);
                getActivity().finish();*/

               /* Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                startActivity(i);
                finish();*/

            }
        });


        final AlertDialog alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();


    }

    public void alerts_dialog_Error(String str_error, String ws) {


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //2018-12-18
        String currentdate_ddMMyyyy = df.format(c);

        AlertDialog.Builder dialog = new AlertDialog.Builder(Trainer_Geofence.this);
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Error: Contact Technology Team:-\n" + ws + "\n" + str_error+"\n"+
                str_username+currentdate_ddMMyyyy);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                /*Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTakenNew();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();
*/
            }
        });


        final AlertDialog alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();


    }

/*
    private void getLocationAndShowProgressDialog() {
        System.out.println("getlocation is called");
        progressDialog.show();
        progressDialog.dismiss();
        final long startTime = System.currentTimeMillis();
        // Start a loop to continuously check for location updates until a valid location is obtained or timeout occurs
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Location findme = mMap.getMyLocation();
                if (findme != null) {
                    // Dismiss the progress dialog when a valid location is obtained
                    progressDialog.dismiss();

                    // Process the obtained location
                    latitude_async = findme.getLatitude();
                    longitude_async = findme.getLongitude();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("passed_lat", String.valueOf(latitude_async)); // Change "YourValue" to the value you want to store
                    editor.putString("passed_long", String.valueOf(longitude_async));
                    editor.apply();
                    System.out.println("Passing Lat and long : " + latitude_async + longitude_async);
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Location obtained: Lat: " + latitude_async + " Long: " + longitude_async, Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the maximum wait time is reached and dismiss the dialog if necessary
                    if (System.currentTimeMillis() - startTime >= MAX_WAIT_TIME) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to get location within the specified time", Toast.LENGTH_SHORT).show();
                    } else {
                        // Continue checking for location updates until a valid location is obtained or timeout occurs
                        handler.postDelayed(this, 1000); // Check every second
                    }
                }
            }
        }, 1000); // Initial check after 1 second
    }
*/

    private void getLocationAndShowProgressDialog() {
        progressDialog.show();
        handler.postDelayed(new Runnable() {
            int locationCheckCount = 0;

            @Override
            public void run() {
                Location findme = mMap.getMyLocation();
                if (findme != null) {
                    Log.e("iteration", String.valueOf(locationCheckCount));
                    Log.e("iteration", String.valueOf(latitude_async));
                    Log.e("iteration", String.valueOf(longitude_async));
                    if (locationCheckCount < 1) {
                        locationCheckCount++;
                        handleLocation(findme);
                        handler.postDelayed(this, 5000); // Check every second if location is available
                    } else {
                        progressDialog.dismiss();
                        System.out.println("Passing Lat and long : " + latitude_async + longitude_async);
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Location obtained: Lat: " + latitude_async + " Long: " + longitude_async, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handler.postDelayed(this, 1000); // Check again in 1 second
                }
            }
        }, 1000); // Initial check after 1 second
    }

    private void handleLocation(Location location) {
        // Process the obtained location
        latitude_async = location.getLatitude();
        longitude_async = location.getLongitude();

        // Store the obtained location in SharedPreferences or use it as needed
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("passed_lat", String.valueOf(latitude_async));
        editor.putString("passed_long", String.valueOf(longitude_async));
        editor.apply();

        Toast.makeText(getApplicationContext(), "Location obtained: Lat: " + latitude_async + " Long: " + longitude_async, Toast.LENGTH_SHORT).show();
    }

}