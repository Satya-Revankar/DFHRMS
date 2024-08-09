package com.dfhrms.GPS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.dfhrms.R;

public class GPSTracker extends Service implements LocationListener
{

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location=null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
       // requestlocationUpdates();
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation()
    {

        //-------------------testing----------------

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 2, this);
       // requestlocationUpdates();

        //-----------------testing-----------------



        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else
            {

                this.canGetLocation = true;
                // First get location from Network Provider

                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");

                    if (locationManager != null)
                    {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            Log.e("network",""+latitude);

                        }
                    }


                }
                // if GPS Enabled get lat/long using GPS Services

               /* if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled------", "GPS Enabled(Inside(isGPSEnabled)  ");
                        if (locationManager != null) {
                        	
                        	Log.d("GPS Enabled----", "GPS Enabled(locationManager != null)");
                        	
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                            	
                            	Log.d("GPS Enabled----", "GPS Enabled(location != null)");
                            	
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                Log.e("isGPSEnabled",""+latitude);
                            }
                        }
                    } //if location is null
                    else{
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("Network", "Network");

                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();

                                    Log.e("network", "" + latitude);

                                }
                            }
                        }
                    }


                }// ifGPS Enabled*/
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }





    public void requestlocationUpdates() {
        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }


     
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }       
    }
     
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
         
        // return latitude
        return latitude;
    }
     
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
         
        // return longitude
        return longitude;
    }
     
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert()
    {
        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);*/
      
        // Setting Dialog Title
        /*alertDialog.setTitle("Location/GPS is settings");*/
  
        // Setting Dialog Message
        /*alertDialog.setMessage("Click on setting and enable the Location/GPS");*/
  
        // On pressing Settings button
        /*alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);   
      } 
        });*/
  
        // on pressing cancel button
        /*alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });*/
  
        // Showing Alert Message
       /* alertDialog.show();*/
        
        
    	//custom code
        final Dialog dialog = new Dialog(mContext); 
		
		
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.locationoption);
		ImageView iv_locationimage = (ImageView) dialog.findViewById(R.id.locationdialog_IV);
		iv_locationimage.setImageResource(R.drawable.location5);
	Button dialogsettingbutton = (Button) dialog.findViewById(R.id.settingdialog_BT);
	dialogsettingbutton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent); 
            dialog.dismiss();
		}
	});
        
        
	dialog.show();


    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //Toast.makeText(mContext, "new Location is - \nLat: " + Double.toString(location.getLatitude()) + "\n Long:" + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
        Log.e("newlatitude", String.valueOf(location.getLatitude()));
        Log.e("newlongitude", String.valueOf(location.getLongitude()));




    }
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 

}// End public class GPSTracker
