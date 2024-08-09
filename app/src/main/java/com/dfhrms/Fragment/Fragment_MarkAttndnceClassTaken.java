package com.dfhrms.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.dfhrms.Activity.Geofencing_Activity;
import com.dfhrms.Utili.ConnectionDetector;
import com.dfhrms.GPS.GPSTracker;
import com.dfhrms.R;
import com.dfhrms.Activity.Slidebar_Activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;


public class Fragment_MarkAttndnceClassTaken extends Fragment {
    private LinearLayout lLayoutfragmentmarkattendance;

    private String username, pwd, emp_id,Name, internet_issue = "empty",str_classstarted_id;

    private long Employeeidlong;

    Context context;
    Resources resource;

    private String str_validateAttendance_response, str_signout_response, str_signin_response, currentdate_yyyyMMdd,
            str_classscheduled_response;

    private TextView markattendance_description_tv,markattendance_description_tv1, caution_note_tv,loginname_tv;
    private Button markattendance_signin_bt, markattendance_signout_bt;
    private ImageView markattendance_signin_IM, markattendance_signout_IM;
    private Button classstarted_bt,classscompleted_bt;

    private LinearLayout markattendance_signin_layout, markattendance_signout_layout;

    GPSTracker gpstracker_obj, gpstracker_obj_out,gpstracker_classstarted,gpstracker_classcompleted;
    Double currentlatitude = 0.0;
    Double currentlongitude = 0.0;
    Double currentlatitude_out = 0.0;
    Double currentlongitude_out = 0.0;

    Double currentlatitude_out_new = 0.0;
    Double currentlongitude_out_new = 0.0;

    Double currentlatitude_new = 0.0;
    Double currentlongitude_new = 0.0;

    Double classscheduled_latitude=0.0;
    Double classscheduled_longitude=0.0;


    ProgressBar progressBar;

    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;

    ////////////////////////////////////////////////////////////////////////
    private static final String TAG = Slidebar_Activity.class.getSimpleName();

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    GPSTracker gps;
    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    // UI Widgets.
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    private TextView mLastUpdateTimeTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    // Labels.
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;
    boolean boolean_signinvalue = false;
    boolean boolean_signoutvalue = false;
    boolean boolean_classstarted= false,boolean_classcompleted=false;
    SoapObject soapobject_recentattendance_response=null;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lLayoutfragmentmarkattendance = (LinearLayout) inflater.inflate(R.layout.fragment_markattndnceclasstaken, container, false);

        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");
        Name = myprefs.getString("Name", "nothing");

        getActivity().setTitle("Mark Attendance");
        Employeeidlong = Long.parseLong(emp_id); // for web service

        context = lLayoutfragmentmarkattendance.getContext();
        resource = context.getResources();

        markattendance_description_tv = (TextView) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_description_TV);
        markattendance_description_tv1 = (TextView) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_description_TV1);

       /* markattendance_signin_bt=(Button) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_signin_BT);
        markattendance_signout_bt=(Button) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_signout_BT);
*/

        markattendance_signin_layout = (LinearLayout) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_signin_layout);
        markattendance_signout_layout = (LinearLayout) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_signout_layout);
        markattendance_signin_IM = (ImageView) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_signin_IM);
        markattendance_signout_IM = (ImageView) lLayoutfragmentmarkattendance.findViewById(R.id.markattendance_signout_IM);
        caution_note_tv = (TextView) lLayoutfragmentmarkattendance.findViewById(R.id.caution_note_TV);
        classstarted_bt=(Button)lLayoutfragmentmarkattendance.findViewById(R.id.classstarted_bt);
        classscompleted_bt=(Button)lLayoutfragmentmarkattendance.findViewById(R.id.classscompleted_bt);
        loginname_tv=(TextView) lLayoutfragmentmarkattendance.findViewById(R.id.loginname_tv);

      //  markattendance_signin_layout.setVisibility(View.GONE);
        markattendance_signout_layout.setVisibility(View.GONE);
        markattendance_signin_layout.setVisibility(View.VISIBLE);// only for punchIN
        //getString(R.string.html_string));
        Typeface type = Typeface.createFromAsset(getResources().getAssets(), "fonts/GOTHICB.TTF");
        markattendance_description_tv.setTypeface(type);
        markattendance_description_tv.setText(getResources().getString(R.string.description_markattendance));
        markattendance_description_tv1.setTypeface(type);
        markattendance_description_tv1.setText(getResources().getString(R.string.description_markattendance1));

        caution_note_tv.setTypeface(type);
        caution_note_tv.setText(getResources().getString(R.string.caution_note));
        //caution_note

        loginname_tv.setText(Name);
        str_validateAttendance_response = "error";
        str_classscheduled_response="error";
        str_signout_response = "error";
        str_signin_response = "error";
        str_classstarted_id="0";

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //2018-12-18
        currentdate_yyyyMMdd = df.format(c);
        //Toast.makeText(getActivity(),"date: "+currentdate_yyyyMMdd,Toast.LENGTH_SHORT).show();


        // new BackgroundAsyncTask().execute();


        //will enable and disable punchIN and Punchout button
        AsyncCall_ValidateAttendance task = new AsyncCall_ValidateAttendance(context);
        task.execute();


        AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
        task1.execute();

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        // updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        gpstracker_obj = new GPSTracker(getActivity());
        //////////////////////////////////////////////changed
//        if(gpstracker_obj.canGetLocation()) {
//            currentlatitude = gpstracker_obj.getLatitude();
//            currentlongitude = gpstracker_obj.getLongitude();
//
//           /* double x=gpstracker_obj.getLatitude();
//            double y=gpstracker_obj.getLongitude();*/
//
//          //  Toast.makeText(getActivity(), "Zero Location is - \nLat: " + Double.toString(currentlatitude)+"\n Long:"+Double.toString(currentlongitude), Toast.LENGTH_LONG).show();
//            if(currentlatitude.equals(0.0)||currentlongitude.equals(0.0))
//            {
//              //  Toast.makeText(getActivity(), "Zero Location is - \nLat: " + Double.toString(currentlatitude)+"\n Long:"+Double.toString(currentlongitude), Toast.LENGTH_LONG).show();
//            }else {
//                //Toast.makeText(getActivity(), "Your Location is - \nLat: " + Double.toString(currentlatitude) + "\n Long:" + Double.toString(currentlongitude), Toast.LENGTH_LONG).show();
//            }
//        }
//        else{gpstracker_obj.showSettingsAlert();}
//////////////////////////////////////////////////////changed









        markattendance_signin_IM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpstracker_obj = new GPSTracker(getActivity());
                if (gpstracker_obj.canGetLocation())
                {

                    System.out.println("Entered case 4....onclick.signin....");
                    startLocationUpdates();
                    boolean_signinvalue = true;






//                    //  new BackgroundAsyncTask().execute();
//
//                    currentlatitude = gpstracker_obj.getLatitude();
//                    currentlongitude = gpstracker_obj.getLongitude();
//
//                    // Toast.makeText(getActivity(), "current Location is - \nLat: " + Double.toString(currentlatitude)+"\n Long:"+Double.toString(currentlongitude), Toast.LENGTH_LONG).show();
//
//                    AsyncCall_MarkEmployee_PunchINTime task = new AsyncCall_MarkEmployee_PunchINTime(context);
//                    task.execute();
                } else {
                    gpstracker_obj.showSettingsAlert();
                }




            }
        });


        markattendance_signout_IM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
///////////////////////////changed
                gpstracker_obj_out = new GPSTracker(getActivity());
                if(gpstracker_obj_out.canGetLocation())
                {
                    System.out.println("Entered case 4...signout......");
                    startLocationUpdates();
                    boolean_signoutvalue = true;


                    Intent i = new Intent(getActivity(),Geofencing_Activity.class);
                    startActivity(i);

//                    currentlatitude_out = gpstracker_obj_out.getLatitude();
//                    currentlongitude_out = gpstracker_obj_out.getLongitude();
//
//                  // Toast.makeText(getActivity(), "Your Location is - \nLat: " + Double.toString(currentlatitude_out)+"\n Long:"+Double.toString(currentlongitude_out), Toast.LENGTH_LONG).show();
//
//                    AsyncCall_MarkEmployeeOutTime task = new AsyncCall_MarkEmployeeOutTime(context);
//                    task.execute();
                }
                else{gpstracker_obj_out.showSettingsAlert();}



                //  Toast.makeText(getActivity(),"signout",Toast.LENGTH_SHORT).show();

            }
        });




        classstarted_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                gpstracker_classstarted = new GPSTracker(getActivity());
                if(gpstracker_classstarted.canGetLocation())
                {
                    System.out.println("Entered case 4...signout......");
                    startLocationUpdates();
                    boolean_signoutvalue = false;
                    boolean_signinvalue = false;
                    boolean_classstarted=true;
                   boolean_classcompleted=false;
                }
                else{gpstracker_classstarted.showSettingsAlert();}

            }
        });




        classscompleted_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {



                gpstracker_classcompleted = new GPSTracker(getActivity());
                if(gpstracker_classcompleted.canGetLocation())
                {
                    System.out.println("Entered case 4...signout......");
                    startLocationUpdates();
                    boolean_signoutvalue = false;
                    boolean_signinvalue = false;
                    boolean_classstarted=false;
                    boolean_classcompleted=true;

                }
                else{gpstracker_classcompleted.showSettingsAlert();}

            }
        });

        return lLayoutfragmentmarkattendance;
    }//end of oncreate()


    private class AsyncCall_ValidateAttendance extends AsyncTask<String, Void, Void> {
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
            ValidateAttendance_verify(); //check Whether already punched-IN or not
            return null;
        }

        public AsyncCall_ValidateAttendance(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            if (str_validateAttendance_response.equalsIgnoreCase("Attendance not Marked")) {
                markattendance_signin_layout.setVisibility(View.VISIBLE);
                markattendance_signout_layout.setVisibility(View.GONE);


                // enable sign button
            } else if (str_validateAttendance_response.equalsIgnoreCase("Attendance Already Marked"))
            {
                markattendance_signout_layout.setVisibility(View.VISIBLE);
                //enable punch out button
                markattendance_signin_layout.setVisibility(View.GONE);


            } else {
                alerts_dialog_Error(str_validateAttendance_response, "ValidateAttendance");

            }

            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task


    public void ValidateAttendance_verify() {
        Vector<SoapObject> result1 = null;

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "ValidateAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ValidateAttendance";
        try {

			/*<ValidateAttendance xmlns="http://tempuri.org/">
      <id>long</id>    </ValidateAttendance>*/


            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", Employeeidlong);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                str_validateAttendance_response = response.toString().trim();

                Log.e("ValidateAttenda resp", response.toString());
                //ValidateAttenda resp: Attendance Already Marked
                //ValidateAttenda resp: Attendance not Marked


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_validateAttendance_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_validateAttendance_response = t.getMessage();

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

                alerts_dialog_for_ThankYou("Thank You For Punching IN", String.valueOf(currentlatitude_new),
                        String.valueOf(currentlongitude_new));
                // Toast.makeText(getActivity(),"Sign in has been updated",Toast.LENGTH_LONG).show();

            } else if (str_signin_response.equalsIgnoreCase("location is not valid") || str_signin_response.equalsIgnoreCase("location is not valid.")) {
                alerts_dialog_location_OutOfRange(String.valueOf(currentlatitude_new), String.valueOf(currentlongitude_new));
            } else if (str_signin_response.equalsIgnoreCase("Working location not assigned") || str_signin_response.equalsIgnoreCase("Working location not assigned.")) {
                alerts_dialog_forSignIN_location_NotAssigned();
            } else {
                alerts_dialog_Error(str_signin_response, "MarkSelfAttendance");
            }


            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task


    public void MarkEmployee_PunchINTime_insertion_onlypunchIN() {

        String str_currentlatitude = String.valueOf(currentlatitude_new);
        String str_currentlongitude = String.valueOf(currentlongitude_new);
        long long_userid = 0;


        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "MarkSelfAttendanceV2";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkSelfAttendanceV2";
        try {

           /*  <MarkSelfAttendanceV2 xmlns="http://tempuri.org/">
      <employee_id>long</employee_id>
      <date>string</date>
      <userId>long</userId>
      <lat>string</lat>
      <lon>string</lon>
    </MarkSelfAttendanceV2>*/




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



    public void MarkEmployee_PunchINTime_insertion() {

        String str_currentlatitude = String.valueOf(currentlatitude_new);
        String str_currentlongitude = String.valueOf(currentlongitude_new);
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
                alerts_dialog_for_ThankYou("Thank You For Punching OUT", Double.toString(currentlatitude_out_new), Double.toString(currentlongitude_out_new));
                // Toast.makeText(getActivity(),"Sign out has been updated",Toast.LENGTH_LONG).show();

            } else if (str_signout_response.equalsIgnoreCase("location is not valid") || str_signout_response.equalsIgnoreCase("location is not valid.")) {
                alerts_dialog_location_OutOfRange(String.valueOf(currentlatitude_out_new), String.valueOf(currentlongitude_out_new));
            } else if (str_signout_response.equalsIgnoreCase("Working location not assigned") || str_signin_response.equalsIgnoreCase("Working location not assigned.")) {
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


        String str_currentlatitude = String.valueOf(currentlatitude_out_new);
        String str_currentlongitude = String.valueOf(currentlongitude_out_new);

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


//alert dialog

    public void alerts_dialog_for_ThankYou(String thankyou, String lat, String longitude) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("" + thankyou + "\n" + "\n" + "Lat:" + lat + "\n" + "Long:" + longitude);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

                Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTaken();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();




               /* Intent i = new Intent(getActivity(), Slide_MainActivity.class);
                i.putExtra("mark", "mark");
                startActivity(i);
                getActivity().finish();*/

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

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Sorry! Not in office location range." + "\n" + "\n" + "lat:" + lat + "\n" + "long:" + longitude);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTaken();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();


                /*Intent i = new Intent(getActivity(), Slide_MainActivity.class);
                i.putExtra("mark", "mark");
                startActivity(i);
                getActivity().finish();*/
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


    public void alerts_dialog_forSignIN_location_NotAssigned() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Work location not assigned, Contact HR Department!");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTaken();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();



                /*Intent i = new Intent(getActivity(), Slide_MainActivity.class);
                i.putExtra("mark", "mark");
                startActivity(i);
                getActivity().finish();*/

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


    public void alerts_dialog_sure2punchout() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Do you want to Punch-out..!");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {


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

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Error: Contact Technology Team:-\n" + ws + "\n" + str_error);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTaken();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();

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


    public class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {

        int myProgressCount;

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(),
                    "onPreExecute Start Progress Bar", Toast.LENGTH_LONG)
                    .show();
            progressBar.setProgress(0);
            myProgressCount = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            while (myProgressCount < 100) {
                myProgressCount++;
                /**
                 * Runs on the UI thread after publishProgress(Progress...) is
                 * invoked. The specified values are the values passed to
                 * publishProgress(Progress...).
                 *
                 * Parameters values The values indicating progress.
                 */

                publishProgress(myProgressCount);
                SystemClock.sleep(100);
            }
            return null;
        }

        /**
         * This method can be invoked from doInBackground(Params...) to publish
         * updates on the UI thread while the background computation is still
         * running. Each call to this method will trigger the execution of
         * onProgressUpdate(Progress...) on the UI thread.
         * <p>
         * onProgressUpdate(Progress...) will not be called if the task has been
         * canceled.
         * <p>
         * Parameters values The progress values to update the UI with.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getActivity(), "onPostExecute End Progress Bar",
                    Toast.LENGTH_LONG).show();
            /**
             * enable Button back so user can click again
             */

        }
    }

    ///////////////////////////////////////////////////////////////////////
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
//        // location updates if the user has requested them.
//        if (mRequestingLocationUpdates && checkPermissions()) {
//            startLocationUpdates();
//        } else if (!checkPermissions()) {
//            requestPermissions();
//        }
//
//       updateLocationUI();
//    }
    @SuppressLint("NewApi")
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

//    @SuppressLint("NewApi")
//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                        android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//            showSnackbar(R.string.permission_rationale,
//                    android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(getActivity(),
//                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                                    REQUEST_PERMISSIONS_REQUEST_CODE);
//                        }
//                    });
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//
//        }
//    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                System.out.println("mCurrentLocation....inside fragment.." + mCurrentLocation);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

//    private void updateValuesFromBundle(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
//            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
//            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
//                mRequestingLocationUpdates = savedInstanceState.getBoolean(
//                        KEY_REQUESTING_LOCATION_UPDATES);
//            }
//
//            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
//            // correct latitude and longitude.
//            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
//                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
//                // is not null.
//                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            }
//
//            // Update the value of mLastUpdateTime from the Bundle and update the UI.
//            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
//                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
//            }
//            updateLocationUI();
//        }
//    }

    private void updateLocationUI()
    {
        System.out.println("entered  updateLocationUI()....");

            if (mCurrentLocation != null)
            {


                if (boolean_signinvalue)
                {


                    boolean_signinvalue = false;


                    currentlatitude_new = mCurrentLocation.getLatitude();
                    currentlongitude_new = mCurrentLocation.getLongitude();

                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent)
                    {


                       /* AsyncCall_MarkEmployee_PunchINTime task = new AsyncCall_MarkEmployee_PunchINTime(context);
                        task.execute(); // working
*/
                        //https://dfhrms.dfindia.org/PMSservice/GetEmpolyeeLatLong?employee_id=12


                    } else{
                        Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                    }


                 //   Toast.makeText(getActivity(), "signin.." + currentlatitude_new + "," + currentlongitude_new, Toast.LENGTH_LONG).show();

                } else if (boolean_signoutvalue) {
                    boolean_signoutvalue = false;
                    currentlatitude_out_new = mCurrentLocation.getLatitude();
                    currentlongitude_out_new = mCurrentLocation.getLongitude();

                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent)
                    {
                        AsyncCall_MarkEmployeeOutTime task = new AsyncCall_MarkEmployeeOutTime(context);
                        task.execute();
                    }
                    else{
                        Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                    }




                    //   Toast.makeText(getActivity(), "signout.."+mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLatitude(), Toast.LENGTH_LONG).show();

                    // Toast.makeText(getActivity(), "signout.."+currentlatitude_out_new+ "," + currentlongitude_out_new, Toast.LENGTH_LONG).show();

                }


                else if(boolean_classstarted)
                {
                    classscheduled_latitude = mCurrentLocation.getLatitude();
                    classscheduled_longitude = mCurrentLocation.getLongitude();

                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent)
                    {
                        AsyncCall_ClassStarted task = new AsyncCall_ClassStarted(context);
                        task.execute();
                    }
                    else{
                        Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                    }
                }
                else if(boolean_classcompleted)
                {

                    classscheduled_latitude = mCurrentLocation.getLatitude();
                    classscheduled_longitude = mCurrentLocation.getLongitude();

                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent)
                    {
                        AsyncCall_ClassStarted task = new AsyncCall_ClassStarted(context);
                        task.execute();
                    }
                    else{
                        Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                    }
                }



            }

            else {
                //  Toast.makeText(getActivity(), "Try once again....Location=null", Toast.LENGTH_LONG).show();

            }


    }
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
//        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
//        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
//        super.onSaveInstanceState(savedInstanceState);
//    }

    @SuppressLint("NewApi")
    private void startLocationUpdates() {
        //////probar.setVisibility(View.VISIBLE);
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
//                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                                mLocationCallback,
//                                null /* Looper */);

                        updateLocationUI();
                        System.out.println("in onSuccess...above stop updates..");
                        //   stopLocationUpdates();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateLocationUI();
                    }
                });
    }


    // class started and Class completed



    private class AsyncCall_Validate_ClassAttendance extends AsyncTask<String, Void, Void> {
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

            Validate_classattendance(); //check Whether class started punched or not
            return null;
        }

        public AsyncCall_Validate_ClassAttendance(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


             if(soapobject_recentattendance_response!=null)
             {
                 if(soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records"))
                 {
                     classstarted_bt.setEnabled(true);
                     classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                 }

                 if(soapobject_recentattendance_response.getProperty("Message").toString().
                         equalsIgnoreCase("Success"))
                 {
                     //<Message>Success</Message>
                     // <Attendance>Class Started</Attendance>
                     //anyType{Id=5; EmployeeId=116; LocationId=0; Intime=09:59:13; InLatitude=15.3710447;
                     // InLongitude=75.1235969; outtime=anyType{}; OutLatitude=anyType{}; OutLongitude=anyType{};
                     // Attendance=Class Started; Status=true; Message=Success; }

                     if(soapobject_recentattendance_response.getProperty("Attendance").toString().
                             equalsIgnoreCase("Class Started"))
                     {
                         classstarted_bt.setEnabled(false);
                         classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                         classscompleted_bt.setEnabled(true);
                         classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                         str_classstarted_id=soapobject_recentattendance_response.getProperty("Id").toString();
                     }


                    // //anyType{Id=7; EmployeeId=896; LocationId=0; Intime=11:30:13; InLatitude=15.3710426;
                     // InLongitude=75.1236045; outtime=11:30:53; OutLatitude=15.3710326; OutLongitude=75.1235852;
                     // Attendance=Class Completed; Status=true; Message=Success; }
                     if(soapobject_recentattendance_response.getProperty("Attendance").toString().
                             equalsIgnoreCase("Class Completed"))
                     {
                         classstarted_bt.setEnabled(true);
                         classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                         classscompleted_bt.setEnabled(false);
                         classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                         str_classstarted_id="0";
                     }


                 }
             }





            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task



    public void Validate_classattendance()
    {

        Vector<SoapObject> result1 = null;

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "GetEmployeeRecentAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeRecentAttendance";


        try {


            /*<GetEmployeeRecentAttendance xmlns="http://tempuri.org/">
      <EmployeeId>long</EmployeeId>
    </GetEmployeeRecentAttendance>*/


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

                //SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                SoapObject response = (SoapObject) envelope.getResponse();
               // str_validateAttendance_response = response.toString().trim();

                soapobject_recentattendance_response=response;

                Log.e("Validateclass resp", response.toString());
                //anyType{Id=0; EmployeeId=0; LocationId=0; Status=false; Message=There are no records; }

                if(response.getProperty("Message").toString().equalsIgnoreCase("There are no records"))
                {
                    /*classstarted_bt.setEnabled(true);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));*/
                }


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
               // str_validateAttendance_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            //str_validateAttendance_response = t.getMessage();

        }


    }




    private class AsyncCall_ClassStarted extends AsyncTask<String, Void, Void> {
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

            if(boolean_classstarted) {
                Validate_classstarted();
            }else{
                Validate_classcompleted();
            }
            return null;
        }

        public AsyncCall_ClassStarted(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();

                if(str_classscheduled_response.equalsIgnoreCase("Attendance captured successfully"))
                {

                    if(boolean_classstarted) {
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        classscompleted_bt.setEnabled(true);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        Toast.makeText(getActivity(),"Class started Attendance captured",Toast.LENGTH_SHORT).show();
                    }
                    if(boolean_classcompleted)
                    {
                        classstarted_bt.setEnabled(true);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        classscompleted_bt.setEnabled(false);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        Toast.makeText(getActivity(),"Class completed Attendance captured",Toast.LENGTH_SHORT).show();
                        boolean_classcompleted=false;
                        str_classstarted_id="0";

                    }
                }else{
                    boolean_classcompleted=false;
                    boolean_classstarted=false;
                    AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
                    task1.execute();

                }

            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }// end Async task




    public void Validate_classstarted()
    {

        long long_userid = 0;
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "MarkClassStartAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkClassStartAttendance";


        String str_classstartedlatitude= String.valueOf(classscheduled_latitude);
        String str_classstartedlongitude= String.valueOf(classscheduled_longitude);
        try {

            /*<MarkClassStartAttendance xmlns="http://tempuri.org/">
      <employee_id>long</employee_id>
      <date>string</date>
      <userId>long</userId>
      <lat>string</lat>
      <lon>string</lon>
    </MarkClassStartAttendance>*/

//currentdate_yyyyMMdd
            //long_userid


                    SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("employee_id", Employeeidlong);
            request.addProperty("date", currentdate_yyyyMMdd);
            request.addProperty("userId", long_userid);
            request.addProperty("lat", str_classstartedlatitude);
            request.addProperty("lon", str_classstartedlongitude);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();


                 str_classscheduled_response = response.toString().trim();

                Log.e("clsscheduledresp", response.toString());
               //<string xmlns="http://tempuri.org/">Attendance captured successfully</string>



            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_classscheduled_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_classscheduled_response = t.getMessage();

        }


    }




    public void Validate_classcompleted()
    {
//https://dfhrms.dfindia.org/pmsservice.asmx?op=MarkEndClassAttendance
        long long_userid = Long.parseLong(str_classstarted_id);
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "MarkEndClassAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkEndClassAttendance";

        String str_classstartedlatitude= String.valueOf(classscheduled_latitude);
        String str_classstartedlongitude= String.valueOf(classscheduled_longitude);

        try {

           /* <MarkEndClassAttendance xmlns="http://tempuri.org/">
      <Id>long</Id>
      <employee_id>long</employee_id>
      <date>string</date>
      <userId>long</userId>
      <lat>string</lat>
      <lon>string</lon>
    </MarkEndClassAttendance>*/

//currentdate_yyyyMMdd
            //long_userid

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("Id", long_userid);
            request.addProperty("employee_id", Employeeidlong);
            request.addProperty("date", currentdate_yyyyMMdd);
            request.addProperty("userId", long_userid);
            request.addProperty("lat", str_classstartedlatitude);
            request.addProperty("lon", str_classstartedlongitude);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            Log.e("request", String.valueOf(request));
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();


                str_classscheduled_response = response.toString().trim();

                Log.e("clsscheduledresp", response.toString());
                //<string xmlns="http://tempuri.org/">Attendance captured successfully</string>



            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_classscheduled_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_classscheduled_response = t.getMessage();

        }


    }









//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        Log.i(TAG, "onRequestPermissionResult");
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.length <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.");
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (mRequestingLocationUpdates) {
//                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
//                    startLocationUpdates();
//                }
//            } else {
//                // Permission denied.
//
//                // Notify the user via a SnackBar that they have rejected a core permission for the
//                // app, which makes the Activity useless. In a real app, core permissions would
//                // typically be best requested during a welcome-screen flow.
//
//                // Additionally, it is important to remember that a permission might have been
//                // rejected without asking the user for permission (device policy or "Never ask
//                // again" prompts). Therefore, a user interface affordance is typically implemented
//                // when permissions are denied. Otherwise, your app could appear unresponsive to
//                // touches or interactions which have required permissions.
//                showSnackbar(R.string.permission_denied_explanation,
//                        R.string.settings, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Build intent that displays the App settings screen.
//                                Intent intent = new Intent();
//                                intent.setAction(
//                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package",
//                                        BuildConfig.APPLICATION_ID, null);
//                                intent.setData(uri);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        });
//            }
//        }
//    }


//////////////////change

//    private void showSnackbar(final int mainTextStringId, final int actionStringId,
//                              View.OnClickListener listener) {
//        Snackbar.make(progressBar.findViewById(R.id.progressBar1),
//                getString(mainTextStringId),
//                Snackbar.LENGTH_INDEFINITE)
//                .setAction(getString(actionStringId), listener).show();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.i(TAG, "User agreed to make required location settings changes.");
//                        // Nothing to do. startLocationupdates() gets called in onResume again.
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.i(TAG, "User chose not to make required location settings changes.");
//                        mRequestingLocationUpdates = false;
//                        updateLocationUI();
//                        break;
//                }
//                break;
//        }
//    }



}// end of class

























