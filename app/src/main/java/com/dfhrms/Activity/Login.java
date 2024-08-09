package com.dfhrms.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dfhrms.Utili.ConnectionDetector;
import com.dfhrms.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;


public class Login extends AppCompatActivity {
    String deviceSrlno;
    String str_EXTRA_MESSAGE = "error";
    String email, str_error;
    String versioncode, approve_key = "no", Approve_result = "no", Reject_result = "no";
    EditText username, password;
    String strGetDeviceSlno;
    ProgressDialog progressDialog;
    Button loginBtn;
    TextView forgotPassword_tv;
    ProgressBar spinner;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String u1, d1,p1, login_result = "empty", GooglesigninverificationResult = "no", employee_info = "empty",
            user_id = "empty", worklocation_info = "empty", gender = "empty", Prefusername = "nothing",
            Prefpwd,Name,deviceSlno,str_deviceStatus,designations,PrefdeviseSlno,PrefdeviseSrlno,Prefdesignation,PrefdeviseStatus,Presname;
    SoapPrimitive messege;
    String regId, myVersion, deviceBRAND, deviceHARDWARE, devicePRODUCT, deviceUSER, deviceModelName, deviceId, tmDevice, tmSerial, androidId, simOperatorName, sdkver, mobileNumber;
    int sdkVersion, Measuredwidth = 0, Measuredheight = 0, update_flage = 0;
    TelephonyManager tm1 = null;
    private static final int CONST_ID =1 ;
    String MessegValue = "Message", MobileValue = "MobileNo", release_not = "nothing", continue_login = "No Update";
    SoapPrimitive messeg, Mobile, valueAt0, release_not1, version, messeg_getalldetail;
    final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    AsyncTask<Void, Void, Void> mRegisterTask;

    String orientation_str, timer_str;
    boolean orientation_bool;


    private static final String TAG = Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    //private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private ImageButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;

    String mailsent_message;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    Button map_bt;
    String forgotPassword_Url = "https://www.dfindia.org:81/api/hrms/forgottonpassword?MailId=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String notMsg = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            notMsg = extras.getString("messege");

//			Log.d("notMsag",notMsg);
            if (notMsg != null && !notMsg.isEmpty()) {
                if (notMsg.contains("PMNote")) {
                    showCustomDialog(notMsg);
                } else if (notMsg.contains("PMOnduty")) {
                    //System.out.println("PMOnduty"+notMsg.toString());
                    showOndutyCustomDialog(notMsg);
                } else if (notMsg.contains("RHRequest")) {
                    //System.out.println("PMOnduty"+notMsg.toString());
                    show_RHoliday_CustomDialog(notMsg);
                } else {
                    Normalnotification(notMsg);
                }


            }

        }

        username = (EditText) findViewById(R.id.usernameET);
        password = (EditText) findViewById(R.id.passwordET);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
        loginBtn = (Button) findViewById(R.id.loginBtn);
        btnSignIn = (ImageButton) findViewById(R.id.googleloginBtn);
        forgotPassword_tv = (TextView) findViewById(R.id.forgotPassword_tv);
        /*map_bt=(Button)findViewById(R.id.map_bt);*/

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();

                if (isInternetPresent) {
                    //signIn();
                } else {
                    Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        SharedPreferences myprefs = getSharedPreferences("user", MODE_PRIVATE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        Prefusername = myprefs.getString("user1", "nothing");
        Prefpwd = myprefs.getString("pwd", "nothing");
        //  PrefdeviseSlno = myprefs.getString("DeviceSlno", "nothing");
        PrefdeviseSrlno = myprefs.getString("DeviceSrlNo", "nothing");
        Presname= myprefs.getString("Name", "nothing");
        PrefdeviseStatus = myprefs.getString("DeviceStatus", "nothing");
        Prefdesignation = myprefs.getString("Designation", "nothing");

        if (!Prefusername.equals("nothing")) {
            username.setText(Prefusername);
        }
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();


        if (checkPermissions()) {
            //  permissions  granted.
        }

        boolean isLoggedIn = myprefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(this, Slidebar_Activity.class));
            finish();
            return; // Skip the rest of the onCreate method
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.READ_PHONE_STATE)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Login.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        }

        //   deviceSlno = myprefs.getString("DeviceSlno", "nothing");
        deviceSrlno = myprefs.getString("DeviceSrlNo", "nothing");

        Log.e("DeviceSrlNo",deviceSrlno);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();


                if (isInternetPresent) {
                    if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                        Toast.makeText(Login.this, "Blank Fields", Toast.LENGTH_SHORT).show();
                    } else {
                        u1 = username.getText().toString();
                        p1 = password.getText().toString();
                        AsyncCallWS2 task = new AsyncCallWS2(Login.this);
                        task.execute();
                    }
			/*		 Intent i  = new Intent (getApplicationContext(),Slide_MainActivity.class);
							startActivity(i);
							finish();*/

                } else {

                    //Notificationdialogue(Login.this, "No Internet Connection", "");
                    new androidx.appcompat.app.AlertDialog.Builder(Login.this)
                            .setTitle("No Internet Connection")
                            .setPositiveButton("OK", null)
                            .show();

                }
                // TODO Auto-generated method stub


            }
        });

        forgotPassword_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();


                if (isInternetPresent) {
                    if (username.getText().toString().equals("")) {
                        Toast.makeText(Login.this, "Enter the Email ID", Toast.LENGTH_SHORT).show();
                    } else {
                        u1 = username.getText().toString();
                        AsyncCallWS_forgotPassword asyncCallWS_forgotPassword = new AsyncCallWS_forgotPassword();
                        asyncCallWS_forgotPassword.execute();
                    }
			/*		 Intent i  = new Intent (getApplicationContext(),Slide_MainActivity.class);
							startActivity(i);
							finish();*/

                } else {

                    //Notificationdialogue(Login.this, "No Internet Connection", "");

                }
            }
        });

    }


    //Normal login AsyncTask
    private class AsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public AsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");


            ChekVersion();
            if (continue_login.equals("No Update"))
            {
                Login_Verify(u1, p1);

                if (login_result.equals("Success")) {///Success,sucess
                    /*setGCM();
                    setGCM();
                    */
                    //Add_setGCM1();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            Log.i(TAG, "onPostExecute");

            if (login_result.equals("slow internet")) {
                Notificationdialogue(Login.this, "Alert", str_error);
            } else {
                if (continue_login.equals("No Update"))
                {
                    if (login_result.equals("Success")) {///sucess changed
                        //int policy=0;
                        //if(orientation_bool==true){
                        //policy=1;
                        System.out.println("Result is success");
                        if (orientation_str.equalsIgnoreCase("false")) {
                            // Intent i = new Intent(getApplicationContext(), PolicyHomeActivity.class);
                            Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                            SharedPreferences myprefs = Login.this.getSharedPreferences("user", MODE_PRIVATE);
                            myprefs.edit().putString("pwd", password.getText().toString()).commit();
                            myprefs.edit().putString("manual", "no").commit();
                            myprefs.edit().putString("user1", username.getText().toString()).commit();
                            myprefs.edit().putString("emp_id", employee_info).commit();
                            myprefs.edit().putString("min_timer", timer_str).commit();
                            myprefs.edit().putString("user_id", user_id).commit();
                            myprefs.edit().putString("Name", Name).commit();
                            //  myprefs.edit().putString("DeviceSlno", deviceSlno).commit();
                            myprefs.edit().putString("DeviceStatus", str_deviceStatus).commit();
                            myprefs.edit().putString("Designation", designations).commit();

                            ///////////////
                            myprefs.edit().putString("worklocation", worklocation_info).commit();
                            myprefs.edit().putString("gender", gender).commit();
                            startActivity(i);

                        } else {

                            Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                            SharedPreferences myprefs = Login.this.getSharedPreferences("user", MODE_PRIVATE);
                            myprefs.edit().putString("pwd", password.getText().toString()).commit();
                            myprefs.edit().putString("manual", "no").commit();
                            myprefs.edit().putString("user1", username.getText().toString()).commit();
                            myprefs.edit().putString("emp_id", employee_info).commit();
                            myprefs.edit().putString("user_id", user_id).commit();
                            myprefs.edit().putString("min_timer", timer_str).commit();
                            ///////////////
                            myprefs.edit().putString("worklocation", worklocation_info).commit();
                            myprefs.edit().putString("gender", gender).commit();
                            myprefs.edit().putString("Name", Name).commit();
                            //  myprefs.edit().putString("DeviceSlno", deviceSlno).commit();
                            myprefs.edit().putString("DeviceStatus", str_deviceStatus).commit();
                            myprefs.edit().putString("Designation", designations).commit();
                            myprefs.edit().putBoolean("isLoggedIn", true).commit(); // Set the login status

                            //////////
                            startActivity(i);
                        }
                        finish();
                    } else {
                        System.out.println("Result is not valid");
                        if (login_result.equals("Invalid user details")) {
                            Toast.makeText(Login.this, "Wrong user Id or password", Toast.LENGTH_LONG).show();
                        } else {
                            if (login_result.equals("error")) {
                                Toast.makeText(Login.this, "error while retriving data", Toast.LENGTH_LONG).show();
                            }
                        }
                    }


                } else {
                    if (continue_login.equals("Force Update")) {

                        ForcefullUpdate("A newer version of this app is  available. Please update it now.");
                    } else {
                        if (continue_login.equals("Normal Update")) {

                            NormalUpdate("A newer version of this app is  available. You can update it now.", context);
                        }
                    }
                }

            }
        }
    }

    /****************************************************************************************************************************************/

    private class AsyncCallWS_googlelogin extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;
        String email123;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public AsyncCallWS_googlelogin(Login activity, String email1) {
            email123 = email1;
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");


            ChekVersion();
            if (continue_login.equals("No Update")) {
                //GooglesigninverificationResult = GoogleLogin_Verify(email);
                GoogleLogin_Verify(email);
                //if (!GooglesigninverificationResult.equals("Email id is not exists.")) {
                //if (!GooglesigninverificationResult.equals("Invalid user details"))
                if (GooglesigninverificationResult.equalsIgnoreCase("Success")) {

					/*setGCM();
					setGCM();*/
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            Log.i(TAG, "onPostExecute");


            if (continue_login.equals("No Update")) {

                //	if (!GooglesigninverificationResult.equals("Email id is not exists.")) {
                //if (!GooglesigninverificationResult.equalsIgnoreCase("Invalid user details"))//<Status>Invalid user details</Status>
                String str_googleresponse = GooglesigninverificationResult.toString();
                Toast.makeText(getApplicationContext(), "" + str_googleresponse, Toast.LENGTH_SHORT).show();
                if (str_googleresponse.equalsIgnoreCase("Success")) {


                    if (orientation_str.equalsIgnoreCase("false")) {
                        //Intent i = new Intent(getApplicationContext(), PolicyHomeActivity.class);
                        Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                        SharedPreferences myprefs = Login.this.getSharedPreferences("user", MODE_PRIVATE);
                        myprefs.edit().putString("pwd", password.getText().toString()).commit();
                        myprefs.edit().putString("manual", "no").commit();
                        myprefs.edit().putString("user1", username.getText().toString()).commit();
                        myprefs.edit().putString("emp_id", employee_info).commit();
                        myprefs.edit().putString("min_timer", timer_str).commit();
                        myprefs.edit().putString("user_id", user_id).commit();
                        myprefs.edit().putString("Name", Name).commit();
                        //   myprefs.edit().putString("DeviceSlno", deviceSlno).commit();
                        myprefs.edit().putString("DeviceStatus", str_deviceStatus).commit();
                        myprefs.edit().putString("Designation", designations).commit();

                        ///////////////
                        myprefs.edit().putString("worklocation", worklocation_info).commit();
                        myprefs.edit().putString("gender", gender).commit();
                        startActivity(i);

                    } else {

                        //   Intent i = new Intent(getApplicationContext(), Slide_MainActivity.class);
                        Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                        SharedPreferences myprefs = Login.this.getSharedPreferences("user", MODE_PRIVATE);
                        myprefs.edit().putString("pwd", password.getText().toString()).commit();
                        myprefs.edit().putString("manual", "no").commit();
                        myprefs.edit().putString("user1", username.getText().toString()).commit();
                        myprefs.edit().putString("emp_id", employee_info).commit();
                        myprefs.edit().putString("user_id", user_id).commit();
                        myprefs.edit().putString("min_timer", timer_str).commit();
                        ///////////////
                        myprefs.edit().putString("worklocation", worklocation_info).commit();
                        myprefs.edit().putString("gender", gender).commit();
                        myprefs.edit().putString("Name", Name).commit();
                        //  myprefs.edit().putString("DeviceSlno", deviceSlno).commit();
                        myprefs.edit().putString("DeviceStatus", str_deviceStatus).commit();
                        myprefs.edit().putString("Designation", designations).commit();

                        //////////
                        startActivity(i);
                    }
                    finish();

                } else {
                    //signOut();
                    //Google_signOut();

                    Toast.makeText(Login.this, "Use Official Email ID" + GooglesigninverificationResult, Toast.LENGTH_LONG).show();
                }


            } else {
                if (continue_login.equals("Force Update")) {
                    ForcefullUpdate("A newer version of this app is  available. Please update it now.");
                } else {
                    if (continue_login.equals("Normal Update")) {
                        NormalUpdate("A newer version of this app is  available. You can update it now.", context);
                    }
                }
            }

        }

    }


    /****************************************************************************************************************************************/

    protected void Notificationdialogue(Context context, String title, String msg1) {
        // TODO Auto-generated method stub
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg1);
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                // TODO Auto-generated method stub
                // Toast.makeText(getApplicationContext(), "yoy have pressed cancel", 1).show();
                dialog.dismiss();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
        alertDialog.show();
    }


    private class AsyncCallWS3 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public AsyncCallWS3(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");


            Login_Verify(u1, p1);

            if (login_result.equals("Success")) {//Success,sucess
				/*setGCM();
				setGCM();*/
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            Log.i(TAG, "onPostExecute");

            if (login_result.equals("slow internet")) {
                Notificationdialogue(Login.this, "Alert", str_error);
            } else {

                if (login_result.equals("Success")) {//sucess
                    Intent i = new Intent(getApplicationContext(), Slidebar_Activity.class);
                    SharedPreferences myprefs = Login.this.getSharedPreferences("user", MODE_PRIVATE);
                    myprefs.edit().putString("manual", "yes").commit();
                    myprefs.edit().putString("pwd", password.getText().toString()).commit();
                    myprefs.edit().putString("user1", username.getText().toString()).commit();
                    myprefs.edit().putString("emp_id", employee_info).commit();
                    myprefs.edit().putString("min_timer", timer_str).commit();
                    myprefs.edit().putString("Name", Name).commit();
                    // myprefs.edit().putString("DeviceSlno", deviceSlno).commit();
                    myprefs.edit().putString("DeviceStatus", str_deviceStatus).commit();
                    myprefs.edit().putString("Designation", designations).commit();

                    ////////////
                    myprefs.edit().putString("worklocation", worklocation_info).commit();
                    myprefs.edit().putString("gender", gender).commit();
                    ////////
                    startActivity(i);
                    finish();
                } else {
                    if (login_result.equals("Invalid user details")) {
                        Toast.makeText(Login.this, "Wrong user Id or password", Toast.LENGTH_LONG).show();
                    } else {
                        if (login_result.equals("error")) {
                            Toast.makeText(Login.this, "error while retriving data", Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }

        }


    }


    public void GetAllEvents(String username2, String password2) {
        Vector<SoapObject> result1 = null;
        String URL = "http://192.168.1.133:81/webservice/server6.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "getUserInfo";//"NewAppReleseDetails";
        String Namespace = "http://localhost:8080", SOAPACTION = "http://localhost:8080/getUserInfo";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("do", "admin");
            request.addProperty("password1", "admin@123");

            //	request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                result1 = (Vector<SoapObject>) envelope.getResponse();

                //Log.i("string value at messeg",messeg.toString());


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                //Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Throwable t) {
//            Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }

    /*******************************************************************************************************/

    public void setGCM() {


        try {
            versioncode = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        // Fetch Device info

        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        //   final String tmDevice, tmSerial, androidId;
        String NetworkType;
        //TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        simOperatorName = tm.getSimOperatorName();
        Log.v("Operator", "" + simOperatorName);
        NetworkType = "GPRS";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        int simSpeed = tm.getNetworkType();
        if (simSpeed == 1)
            NetworkType = "Gprs";
        else if (simSpeed == 4)
            NetworkType = "Edge";
        else if (simSpeed == 8)
            NetworkType = "HSDPA";
        else if (simSpeed == 13)
            NetworkType = "LTE";
        else if (simSpeed == 3)
            NetworkType = "UMTS";
        else
            NetworkType = "Unknown";


        Log.v("SIM_INTERNET_SPEED", "" + NetworkType);
        tmDevice = "" + tm.getDeviceId();
        Log.v("DeviceIMEI", "" + tmDevice);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mobileNumber = "" + tm.getLine1Number();
        Log.v("getLine1Number value", "" + mobileNumber);

        String mobileNumber1 = "" + tm.getPhoneType();
        Log.v("getPhoneType value", "" + mobileNumber1);
        tmSerial = "" + tm.getSimSerialNumber();
        //  Log.v("GSM devices Serial Number[simcard] ", "" + tmSerial);
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        Log.v("androidId CDMA devices", "" + androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();
        //  Log.v("deviceIdUUID universally unique identifier", "" + deviceId);
        deviceModelName = Build.MODEL;
        Log.v("Model Name", "" + deviceModelName);
        deviceUSER = Build.USER;
        Log.v("Name USER", "" + deviceUSER);
        devicePRODUCT = Build.PRODUCT;
        Log.v("PRODUCT", "" + devicePRODUCT);
        deviceHARDWARE = Build.HARDWARE;
        Log.v("HARDWARE", "" + deviceHARDWARE);
        deviceBRAND = Build.BRAND;
        Log.v("BRAND", "" + deviceBRAND);
        myVersion = Build.VERSION.RELEASE;
        Log.v("VERSION.RELEASE", "" + myVersion);
        sdkVersion = Build.VERSION.SDK_INT;
        Log.v("VERSION.SDK_INT", "" + sdkVersion);
        sdkver = Integer.toString(sdkVersion);
        // Get display details

        Measuredwidth = 0;
        Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //   w.getDefaultDisplay().getSize(size);
            Measuredwidth = w.getDefaultDisplay().getWidth();//size.x;
            Measuredheight = w.getDefaultDisplay().getHeight();//size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        Log.v("SCREEN_Width", "" + Measuredwidth);
        Log.v("SCREEN_Height", "" + Measuredheight);


        Log.v("regId", "" + regId);


        if (!regId.equals("")) {
            //String WEBSERVICE_NAME = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

            String WEBSERVICE_NAME = getResources().getString(R.string.main_url);  //http://dfhrms.dfindia.org/PMSservice.asmx?WSDL
            String SOAP_ACTION1 = "http://tempuri.org/SaveDeviceDetails";
            String METHOD_NAME1 = "SaveDeviceDetails";
            String MAIN_NAMESPACE = "http://tempuri.org/";
            String URI = getResources().getString(R.string.main_url);

            SoapObject request = new SoapObject(MAIN_NAMESPACE, METHOD_NAME1);
            //	request.addProperty("LeadId", Password1);
            request.addProperty("emailId", username.getText().toString());
            request.addProperty("DeviceId", regId);
            request.addProperty("OSVersion", myVersion);
            request.addProperty("Manufacturer", deviceBRAND);
            request.addProperty("ModelNo", deviceModelName);
            request.addProperty("SDKVersion", sdkver);
            request.addProperty("DeviceSrlNo", tmDevice);
            request.addProperty("ServiceProvider", simOperatorName);
            request.addProperty("SIMSrlNo", tmSerial);
            request.addProperty("DeviceWidth", Measuredwidth);
            request.addProperty("DeviceHeight", Measuredheight);
            //request.addProperty("AppVersion", versioncode);
            request.addProperty("AppVersion", getString(R.string.Version));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            // Set output SOAP object
            envelope.setOutputSoapObject(request);
            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URI);

            try {
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                SharedPreferences myprefs = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.putString("DeviceSrlNo", tmDevice);
                Log.e("DeviceSrlNo",tmDevice);
                // Store the DeviceSrlNo value
                editor.apply();


                //   Log.i("response after sending device detail", response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }//end of GCM()

    @SuppressLint("HardwareIds")
    public String GetDeviceSlno() {
        Log.e("Entered ", "Add_setGCM1");

        tm1 = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tmDevice = Settings.Secure.getString(
                    Login.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            if (tm1.getDeviceId() != null) {
                tmDevice = tm1.getDeviceId();
            } else {
                tmDevice = Settings.Secure.getString(
                        Login.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        regId = FirebaseInstanceId.getInstance().getToken();
        return tmDevice;
    }

/*
    @SuppressLint("HardwareIds")
    public void Add_setGCM1() {
        Log.e("Entered ", "Add_setGCM1");

        tm1 = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        String NetworkType;
        simOperatorName = tm1.getSimOperatorName();
        Log.e("Operator", "" + simOperatorName);
        NetworkType = "GPRS";

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int simSpeed = tm1.getNetworkType();
        if (simSpeed == 1)
            NetworkType = "Gprs";
        else if (simSpeed == 4)
            NetworkType = "Edge";
        else if (simSpeed == 8)
            NetworkType = "HSDPA";
        else if (simSpeed == 13)
            NetworkType = "LTE";
        else if (simSpeed == 3)
            NetworkType = "UMTS";
        else
            NetworkType = "Unknown";

        Log.e("SIM_INTERNET_SPEED", "" + NetworkType);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
//        tmDevice = "" + tm1.getDeviceId();
//        Log.e("DeviceIMEI", "" + tmDevice);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tmDevice = Settings.Secure.getString(
                    Login.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            if (tm1.getDeviceId() != null) {
                tmDevice = tm1.getDeviceId();
            } else {
                tmDevice = Settings.Secure.getString(
                        Login.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        /////////////////////////////////////////////

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    CONST_ID);
            //  ActivityCompat.requestPermissions(this, new String[{android.permission.READ_PHONE_NUMBERS}, RC_PN);}
        }  else {
//            TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//            String mPhoneNumber = tMgr.getLine1Number();

            tm1 = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            mobileNumber = "" + tm1.getLine1Number();//error2310
            Log.e("getLine1Number value", "" + mobileNumber);

            String mobileNumber1 = "" + tm1.getPhoneType();
            Log.e("getPhoneType value", "" + mobileNumber1);
            //tmSerial = "" + tm1.getSimSerialNumber();

        }



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tmSerial = Settings.Secure.getString(
                    Login.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            // tmSerial = "" + tm1.getSimSerialNumber();

        } else {
            if (tm1.getSimSerialNumber() != null) {
                tmSerial = tm1.getSimSerialNumber();
            } else {
                tmSerial = Settings.Secure.getString(
                        Login.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        Log.e("tmSerial", "" + tmSerial);


        //  Log.v("GSM devices Serial Number[simcard] ", "" + tmSerial);
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        Log.e("androidId CDMA devices", "" + androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();
        //  Log.v("deviceIdUUID universally unique identifier", "" + deviceId);


        deviceModelName = Build.MODEL;
        Log.v("Model Name", "" + deviceModelName);
        deviceUSER = Build.USER;
        Log.v("Name USER", "" + deviceUSER);
        devicePRODUCT = Build.PRODUCT;
        Log.v("PRODUCT", "" + devicePRODUCT);
        deviceHARDWARE = Build.HARDWARE;
        Log.v("HARDWARE", "" + deviceHARDWARE);
        deviceBRAND = Build.BRAND;
        Log.v("BRAND", "" + deviceBRAND);
        myVersion = Build.VERSION.RELEASE;
        Log.v("VERSION.RELEASE", "" + myVersion);
        sdkVersion = Build.VERSION.SDK_INT;
        Log.v("VERSION.SDK_INT", "" + sdkVersion);
        sdkver = Integer.toString(sdkVersion);
        // Get display details

        Measuredwidth = 0;
        Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //   w.getDefaultDisplay().getSize(size);
            Measuredwidth = w.getDefaultDisplay().getWidth();//size.x;
            Measuredheight = w.getDefaultDisplay().getHeight();//size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        Log.e("SCREEN_Width", "" + Measuredwidth);
        Log.e("SCREEN_Height", "" + Measuredheight);
        regId = FirebaseInstanceId.getInstance().getToken();
        Log.e("regId_DeviceID", "" + regId);
        if (!regId.equals("")) {
            //String WEBSERVICE_NAME = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
            System.out.println("SIMSrlNo"+tmSerial);
            System.out.println("DeviceSrlNo"+tmDevice);
            System.out.println("ModelNo"+deviceModelName);
            System.out.println("DeviceId"+regId);
            System.out.println("SIMSrlNo"+tmSerial);
            String WEBSERVICE_NAME = getResources().getString(R.string.main_url);  //http://dfhrms.dfindia.org/PMSservice.asmx?WSDL
            String SOAP_ACTION1 = "http://tempuri.org/SaveDeviceDetails";
            String METHOD_NAME1 = "SaveDeviceDetails";
            String MAIN_NAMESPACE = "http://tempuri.org/";
            String URI = getResources().getString(R.string.main_url);
            Log.e("emailId", username.getText().toString());
            Log.e("DeviceId", regId);
            Log.e("OSVersion", myVersion);
            Log.e("Manufacturer", deviceBRAND);
            Log.e("ModelNo", deviceModelName);
            Log.e("SDKVersion", sdkver);
            Log.e("DeviceSrlNo", tmDevice);
            Log.e("ServiceProvider", simOperatorName);
            Log.e("SIMSrlNo", tmSerial);
            Log.e("DeviceWidth", String.valueOf(Measuredwidth));
            Log.e("DeviceHeight", String.valueOf(Measuredheight));
            Log.e("AppVersion", versioncode);
            SoapObject request = new SoapObject(MAIN_NAMESPACE, METHOD_NAME1);
            //	request.addProperty("LeadId", Password1);
            request.addProperty("emailId", username.getText().toString());
            request.addProperty("DeviceId", regId);
            request.addProperty("OSVersion", myVersion);
            request.addProperty("Manufacturer", deviceBRAND);
            request.addProperty("ModelNo", deviceModelName);
            request.addProperty("SDKVersion", sdkver);
            request.addProperty("DeviceSrlNo", tmDevice);
            request.addProperty("ServiceProvider", simOperatorName);
            request.addProperty("SIMSrlNo", tmSerial);
            request.addProperty("DeviceWidth", Measuredwidth);
            request.addProperty("DeviceHeight", Measuredheight);
            String appversion = getString(R.string.Version);
            appversion = extractNumber(appversion);
            request.addProperty("AppVersion", appversion);

            Log.e("app version", appversion);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            // Set output SOAP object
            envelope.setOutputSoapObject(request);
            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URI);

            try {
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                // Save the DeviceSrlNo to SharedPreferences
                SharedPreferences myprefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.putString("DeviceSrlNo", tmDevice); // Store the DeviceSrlNo value
                editor.apply();
                //   Log.i("response after sending device detail", response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }*/



    public void ChekVersion() {

        //String URL111 = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL111 = getResources().getString(R.string.main_url);

        String METHOD_NAME = "NewAppReleseDetails";
        String MAIN_NAMESPACE1 = "http://tempuri.org/";
        String SOAP_ACTION1 = "http://tempuri.org/NewAppReleseDetails";
        try {
            versioncode = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(MAIN_NAMESPACE1, METHOD_NAME);
            Log.e("version", versioncode.toString());

            request.addProperty("UserAppVersion", versioncode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL111);

            try {

                androidHttpTransport.call(SOAP_ACTION1, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("at response", response.toString());
                //	 valueAt0 = (SoapPrimitive)response.getProperty(0);
                messeg = (SoapPrimitive) response.getProperty("Response");
                version = (SoapPrimitive) response.getProperty("AppVersion");
                //release_not1 = (SoapPrimitive) response.getProperty("ReleseNote");

                //release_not = release_not1.toString();


                Log.e("string value at messeg", messeg.toString());


                continue_login = messeg.toString();


            } catch (Exception t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Exception t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());

        }
    }

    //Login from normal login web service
   /* public void Login_Verify(String username1, String password1) {
        Vector<SoapObject> result1 = null;

        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "GetEmloyeeOnUserDetails";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmloyeeOnUserDetails";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("username", username1);
            request.addProperty("password", password1);

            //	request.addProperty("to", 9);

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
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                SoapPrimitive messege = (SoapPrimitive) response.getProperty("Status");
                login_result = messege.toString();
                if (login_result.equals("Success")) {
                    SoapPrimitive employee_id = (SoapPrimitive) response.getProperty("Id");
                    employee_info = employee_id.toString();

                    SoapPrimitive work_location = (SoapPrimitive) response.getProperty("Work_Location");
                    worklocation_info = work_location.toString();


                    SoapPrimitive gender_fetch = (SoapPrimitive) response.getProperty("Gender");
                    gender = gender_fetch.toString();

                    SoapPrimitive orientation_fetch = (SoapPrimitive) response.getProperty("Orientation");
                    orientation_str = orientation_fetch.toString();

                    SoapPrimitive User_Id = (SoapPrimitive) response.getProperty("User_Id");
                    user_id = User_Id.toString();

                    SoapPrimitive name = (SoapPrimitive) response.getProperty("Name");
                    Name = name.toString();

                    *//*SoapPrimitive deviseslno = (SoapPrimitive) response.getProperty("DeviceSlno");
                    deviceSlno = deviseslno.toString();*//*

                    SoapPrimitive devisestatus = (SoapPrimitive) response.getProperty("DeviceStatus");
                    str_deviceStatus = devisestatus.toString();

                    orientation_bool = Boolean.valueOf(orientation_str);
                    Log.e(TAG, gender);
                    Log.e(TAG, worklocation_info);
                    Log.e(TAG, "orientation_str=" + orientation_str);
                }


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";
                str_error=t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());

        }

    }
*/

    //Login from normal login new api
    public void Login_Verify(String username1, String password1) {
        Vector<SoapObject> result1 = null;

        strGetDeviceSlno=GetDeviceSlno();
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "GetEmloyeeOnUserDetailsNew";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmloyeeOnUserDetailsNew";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("username", username1);
            request.addProperty("password", password1);
            request.addProperty("DeviceSlno", strGetDeviceSlno);
            //	request.addProperty("to", 9);

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
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                SoapPrimitive messege = (SoapPrimitive) response.getProperty("Status");
                login_result = messege.toString();
                if (login_result.equals("Success")) {
                    SoapPrimitive employee_id = (SoapPrimitive) response.getProperty("Id");
                    employee_info = employee_id.toString();

                    SoapPrimitive work_location = (SoapPrimitive) response.getProperty("Work_Location");
                    worklocation_info = work_location.toString();


                    SoapPrimitive gender_fetch = (SoapPrimitive) response.getProperty("Gender");
                    gender = gender_fetch.toString();

                    // WeekOff value is used for sending the timer value as the minimun class duration time
                    SoapPrimitive timer_fetch = (SoapPrimitive) response.getProperty("WeekOff");
                    timer_str = timer_fetch.toString();
                    Log.e("Timer", timer_str);
                    SoapPrimitive orientation_fetch = (SoapPrimitive) response.getProperty("Orientation");
                    orientation_str = orientation_fetch.toString();

                    Log.e("Orientation", orientation_str);
                    SoapPrimitive User_Id = (SoapPrimitive) response.getProperty("User_Id");
                    user_id = User_Id.toString();

                    SoapPrimitive name = (SoapPrimitive) response.getProperty("Name");
                    Name = name.toString();

                    /*SoapPrimitive deviseslno = (SoapPrimitive) response.getProperty("DeviceSlno");
                    deviceSlno = deviseslno.toString();*/

                    SoapPrimitive devisestatus = (SoapPrimitive) response.getProperty("DeviceStatus");
                    str_deviceStatus = devisestatus.toString();

                    SoapPrimitive designation = (SoapPrimitive) response.getProperty("Designation");
                    designations = designation.toString();



                    orientation_bool = Boolean.valueOf(orientation_str);
                    Log.e(TAG, gender);
                    Log.e(TAG, worklocation_info);
                    Log.e(TAG, "orientation_str=" + orientation_str);
                }


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";
                str_error=t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());

        }

    }










    /***************************************************************************/
    ///public String GoogleLogin_Verify(String username1) {
    public void GoogleLogin_Verify(String username1) {
        Vector<SoapObject> result1 = null;
        String GoogleLogin_Verify_output;

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL = getResources().getString(R.string.main_url);

        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "ValideEmployeeDetailsByUsername";//"NewAppReleseDetails";//GetEmployeeId
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ValideEmployeeDetailsByUsername";


        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            //	request.addProperty("email", username1);

            request.addProperty("username", username1);


            //	request.addProperty("to", 9);

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
                ////////SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());
                SoapPrimitive messege = (SoapPrimitive) response.getProperty("Status");
                System.out.println("messege....google.." + messege);
                //SoapPrimitive messege = (SoapPrimitive) response.getProperty("Status");

                GooglesigninverificationResult = messege.toString();
                System.out.println("GooglesigninverificationResult....google.." + GooglesigninverificationResult);
                //if (!response.toString().equals("Invalid user details")&& GooglesigninverificationResult.equals("Success"))
                if (GooglesigninverificationResult.equalsIgnoreCase("Success"))
                {
					/*employee_info = response.toString();
					worklocation_info=response.toString();*/


                    SoapPrimitive employee_id = (SoapPrimitive) response.getProperty("Id");

                    employee_info = employee_id.toString();

                    SoapPrimitive work_location = (SoapPrimitive) response.getProperty("Work_Location");
                    worklocation_info = work_location.toString();

                    SoapPrimitive gender_fetch = (SoapPrimitive) response.getProperty("Gender");
                    gender = gender_fetch.toString();

                    SoapPrimitive orientation_fetch = (SoapPrimitive) response.getProperty("Orientation");
                    orientation_str = orientation_fetch.toString();

                    SoapPrimitive User_Id = (SoapPrimitive) response.getProperty("User_Id");
                    user_id = User_Id.toString();

                    orientation_bool = Boolean.valueOf(orientation_str);


                    System.out.println("worklocation_info google sign in.." + worklocation_info);

                    /*gender=response.toString();*/



                }



            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                GoogleLogin_Verify_output = "slow internet";

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            //return (UnRegister);
            GoogleLogin_Verify_output = "UnRegister";
        }

        //	return GoogleLogin_Verify_output;

    }


    protected void NormalUpdate(String msg1, Context contextn) {
        // TODO Auto-generated method stub
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(contextn);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Update");

        // Setting Dialog Message
        alertDialog.setMessage(msg1);

        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Add your code for the button here.
                //   Toast.makeText(getApplicationContext(), "well come", 1).show();
                dialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "")));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                update_flage = 1;

                // TODO Auto-generated method stub
                // Toast.makeText(getApplicationContext(), "yoy have pressed cancel", 1).show();
                dialog.dismiss();
                AsyncCallWS3 task2 = new AsyncCallWS3(Login.this);
                task2.execute();

            }
        });
        alertDialog.show();
    }


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(str_EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            //  lblMessage.append(newMessage + "\n");
            //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

        }
    };

    /********************************************************************************************/
    private class RejectAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public RejectAsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (!approve_key.equals("no")) {
                RejectLeave();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {


                dialog.dismiss();

            }
            if (approve_key.equals("no")) {
                Toast.makeText(Login.this, "sending String not available ", Toast.LENGTH_LONG).show();
            } else {
                if (Reject_result.equals("no")) {
                    Toast.makeText(Login.this, "error in webservice connection due to slow network", Toast.LENGTH_LONG).show();
                } else {
                    if (Reject_result.equalsIgnoreCase("success")) {
                        Toast.makeText(Login.this, "Rejected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Not Rejected", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    /****************************************************************************************/
    private class ApproveAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public ApproveAsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (!approve_key.equals("no")) {
                approveLeave();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if (approve_key.equals("no")) {
                Toast.makeText(Login.this, "sending String not available ", Toast.LENGTH_LONG).show();
            } else {
                if (Approve_result.equals("no")) {
                    Toast.makeText(Login.this, "error in webservice connection due to slow network", Toast.LENGTH_LONG).show();
                } else {
                    if (Approve_result.equals("success")) {
                        Toast.makeText(Login.this, "Approved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Not Approved", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    /***************************************************************************************/

    public void approveLeave() {
        Vector<SoapObject> result1 = null;
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL = getResources().getString(R.string.main_url);
        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "ApproveLeave";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ApproveLeave";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            Log.e("id", approve_key);
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

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
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Approve_result = response.toString();


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";

            }
        } catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of approveleave

    /**************************************************************************************/
    public void RejectLeave() {
        Vector<SoapObject> result1 = null;
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";


        String URL = getResources().getString(R.string.main_url);
        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "RejectLeave";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/RejectLeave";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

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
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //SoapPrimitive messege = (SoapPrimitive)response.getProperty("RejectLeaveResult");
                Reject_result = response.toString();


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());


            }
        } catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }

    protected void showCustomDialog(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdailog);

        final TextView editText = (TextView) dialog.findViewById(R.id.editText1);
        final String[] parts = msg1.split("approval.");
        editText.setText(parts[0] + " approval.");
        approve_key = parts[1];
        editText.setGravity(Gravity.CENTER);
        Button button = (Button) dialog.findViewById(R.id.button1);
        Button Approve = (Button) dialog.findViewById(R.id.approve);
        Button Reject = (Button) dialog.findViewById(R.id.reject);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });

        Approve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                ApproveAsyncCallWS2 task = new ApproveAsyncCallWS2(Login.this);

                task.execute();

            }
        });
        Reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                RejectAsyncCallWS2 task = new RejectAsyncCallWS2(Login.this);
                task.execute();

            }
        });


        dialog.show();
    }

    protected void Normalnotification(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.normal_notification);

        final TextView editText = (TextView) dialog.findViewById(R.id.editText1);
        editText.setText(msg1);
        editText.setGravity(Gravity.CENTER);
        Button button = (Button) dialog.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

            }
        });

        dialog.show();
    }


    protected void ForcefullUpdate(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.normal_update);

        final TextView editText = (TextView) dialog.findViewById(R.id.editText1);
        editText.setText(msg1);
        editText.setGravity(Gravity.CENTER);
        Button button = (Button) dialog.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "")));
            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //	getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            //GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiv", "> " + e.getMessage());
        }
        super.onDestroy();
    }


    // Marshmellow fix

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request



        }
    }

    // marshmellow fix


    protected void showOndutyCustomDialog(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ondutycustomdialog);

        final TextView editText = (TextView) dialog.findViewById(R.id.ondutynotification_ET);
        final String[] parts = msg1.split("approval.");
        editText.setText(parts[0] + " approval.");
        approve_key = parts[1];
        Log.d("approve_key", approve_key);
        editText.setGravity(Gravity.CENTER);
        Button lateronduty_bt = (Button) dialog.findViewById(R.id.LaterOnduty_BT);
        Button approveonduty_bt = (Button) dialog.findViewById(R.id.ApproveOnduty_BT);
        Button rejectonduty_bt = (Button) dialog.findViewById(R.id.RejectOnduty_BT);
        lateronduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });

        approveonduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                ApproveOndutyAsyncCallWS2 task = new ApproveOndutyAsyncCallWS2(Login.this);
                task.execute();

            }
        });
        rejectonduty_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                RejectOndutyAsyncCallWS2 task = new RejectOndutyAsyncCallWS2(Login.this);
                task.execute();

            }
        });
        dialog.show();
    }


    // *********************ApproveOndutyAsyncCallWS2*******************************************************************/


    private class ApproveOndutyAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public ApproveOndutyAsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (!approve_key.equals("no")) {
                approveOnduty();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if (approve_key.equals("no")) {
                Toast.makeText(Login.this, "sending String not available ", Toast.LENGTH_LONG).show();
            } else {
                if (Approve_result.equals("no")) {
                    Toast.makeText(Login.this, "error in webservice connection due to slow network", Toast.LENGTH_LONG).show();
                } else {
                    if (Approve_result.equals("success")) {
                        Toast.makeText(Login.this, "Approved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Not Approved", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }

    public void approveOnduty() {
        Vector<SoapObject> result1 = null;
        //String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        //http://dfhrms.cloudapp.net


        String URL = getResources().getString(R.string.main_url);

        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "ApproveOD";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ApproveOD";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

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
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Approve_result = response.toString();


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";

            }
        } catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of approveleave

// *********************ApproveOndutyAsyncCallWS2*******************************************************************/

    // *********************RejectOndutyAsyncCallWS2******************************************************************/
    private class RejectOndutyAsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public RejectOndutyAsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (!approve_key.equals("no")) {
                RejectOnduty();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {


                dialog.dismiss();

            }
            if (approve_key.equals("no")) {
                Toast.makeText(Login.this, "sending String not available ", Toast.LENGTH_LONG).show();
            } else {
                if (Reject_result.equals("no")) {
                    Toast.makeText(Login.this, "error in webservice connection due to slow network", Toast.LENGTH_LONG).show();
                } else {
                    if (Reject_result.equals("success")) {
                        Toast.makeText(Login.this, "Rejected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Not Rejected", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }


    public void RejectOnduty() {
        Vector<SoapObject> result1 = null;
        //String URL = "http://dethrms.cloudapp.net/PMSservice.asmx?WSDL";
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL = getResources().getString(R.string.main_url);

        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
        String METHOD_NAME = "RejectOD";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/RejectOD";
        try {
            // String  versioncode = this.getPackageManager()
            //		    .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);


            //	request.addProperty("to", 9);

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
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Reject_result = response.toString();
                //Log.d("Reject_result onduty",Reject_result);

            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";

            }
        } catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());

        }

    }//end of approveleave


    protected void show_RHoliday_CustomDialog(String msg1) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rh_notification_customdialog);

        final TextView editText = (TextView) dialog.findViewById(R.id.rh_notification_ET);
        final String[] parts = msg1.split("approval.");
        editText.setText(parts[0] + " approval.");
        approve_key = parts[1];
        Log.d("approve_key", approve_key);
        editText.setGravity(Gravity.CENTER);
        Button rh_later_bt = (Button) dialog.findViewById(R.id.rh_later_BT);
        Button rh_approve_bt = (Button) dialog.findViewById(R.id.rh_approve_BT);
        Button rh_reject_bt = (Button) dialog.findViewById(R.id.rh_reject_BT);

        rh_later_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });

        rh_approve_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                Approve_RH_AsyncCallWS2 task = new Approve_RH_AsyncCallWS2(Login.this);
                task.execute();

            }
        });
        rh_reject_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                Reject_RH_AsyncCallWS2 task = new Reject_RH_AsyncCallWS2(Login.this);
                task.execute();

            }
        });
        dialog.show();
    }


    private class Approve_RH_AsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Login context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public Approve_RH_AsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (!approve_key.equals("no")) {
                approve_RH();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if (approve_key.equals("no")) {
                Toast.makeText(Login.this, "sending String not available ", Toast.LENGTH_LONG).show();
            } else {
                if (Approve_result.equals("no")) {
                    Toast.makeText(Login.this, "error in webservice connection due to slow network", Toast.LENGTH_LONG).show();
                } else {
                    if (Approve_result.equals("success")) {
                        Toast.makeText(Login.this, "Approved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Not Approved", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }


    public void approve_RH() {

        String URL = getResources().getString(R.string.main_url);


        String METHOD_NAME = "ApproveRH";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ApproveRH";
        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);

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
                //	Log.i("string value at response",response.toString());
                //	 Object valueAt0 = response.getProperty(0);
                //	 Object user_id = response.getProperty("user_id");
                //	SoapPrimitive messege = (SoapPrimitive)response.getProperty("ApproveLeaveResult");
                Approve_result = response.toString();


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";
                //Approve_result=t.getMessage().toString();

            }
        } catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());
            Log.e("request fail", "> " + t.getMessage());
            //Approve_result=t.getMessage().toString();
        }

    }//end of approveleave


    private class Reject_RH_AsyncCallWS2 extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate---tab2");
        }

        public Reject_RH_AsyncCallWS2(Login activity) {
            context = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if (!approve_key.equals("no")) {
                reject_RH();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if (approve_key.equals("no")) {
                Toast.makeText(Login.this, "sending String not available ", Toast.LENGTH_LONG).show();
            } else {
                if (Approve_result.equals("no")) {
                    Toast.makeText(Login.this, "error in webservice connection due to slow network", Toast.LENGTH_LONG).show();
                } else {
                    if (Approve_result.equals("success")) {
                        Toast.makeText(Login.this, "Rejected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Not Rejected", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, "onPostExecute");
        }

    }


    public void reject_RH() {

        String URL = getResources().getString(R.string.main_url);


        String METHOD_NAME = "RejectRH";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/RejectRH";
        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", approve_key);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                Approve_result = response.toString();


            } catch (Throwable t) {

                Log.e("request fail", "> " + t.getMessage());
                login_result = "slow internet";
                //Approve_result=t.getMessage().toString();

            }
        } catch (Throwable t) {
            //Log.e("UnRegister Receiver Error", "> " + t.getMessage());
            Log.e("request fail", "> " + t.getMessage());
            //Approve_result=t.getMessage().toString();
        }

    }//end of approveleave


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

    // Send otp to Student post API integration
    private class AsyncCallWS_forgotPassword extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog("Please wait");
        }

        @Override
        protected String doInBackground(String... params) {

            String response = "";

            try {
                URL url = new URL(forgotPassword_Url+u1);
                Log.e("forgotPassword_Url: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
            dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String mailsent_Status = jsonObject.optString("status");
                mailsent_message = jsonObject.optString("message");
                if (mailsent_Status.equals("true")){
                    showmailsent_popup(mailsent_message);
                } else {
                    Invalidemail_popup(mailsent_message);
                }
                Log.e("mailsent result", String.valueOf(jsonObject));
                Log.e("mailsent_Status", mailsent_Status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void showmailsent_popup(String mailsent_message) {
        new AlertDialog.Builder(this)
                .setMessage(mailsent_message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void Invalidemail_popup(String mailsent_message) {
        new AlertDialog.Builder(this)
                .setMessage(mailsent_message)
                .setPositiveButton("OK", null)
                .show();
    }
    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(Login.this); // Replace 'context' with your activity or fragment context
        progressDialog.setMessage(message); // Set your message
        progressDialog.setCancelable(false); // Set if dialog can be canceled by user
        progressDialog.show();
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static String extractNumber(String input) {
        StringBuilder number = new StringBuilder();

        // Iterate through each character in the input string
        for (char c : input.toCharArray()) {
            // Check if the character is a digit or a dot (for decimal numbers)
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            }
        }

        return number.toString();
    }

}