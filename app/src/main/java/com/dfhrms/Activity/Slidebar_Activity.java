package com.dfhrms.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dfhrms.Fragment.CancelLeaveFragments1;
import com.dfhrms.Fragment.Fragment_ClassFeedback;
import com.dfhrms.Fragment.Fragment_MarkAttndnceClassTakenNew;
import com.dfhrms.Fragment.Fragment_ViewAttendance;
import com.dfhrms.Fragment.Fragment_classAttendance;
import com.dfhrms.Fragment.Leave_request;
import com.dfhrms.Fragment.LocationAssignedFragment;
import com.dfhrms.Fragment.OndutyFragment;
import com.dfhrms.Fragment.Onduty_HistoryFragment;
import com.dfhrms.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Slidebar_Activity extends AppCompatActivity
{
    FragmentManager fragmentManager;
    int selectedReasonId;
    private String username, pwd, reason,employeeId,enteredRemark,selectedOption, internet_issue = "empty", str_classstarted_id, Name, deviceSlno,deviceSrlno,str_deviceStatus,designations;
    private boolean isPopupCancelable = false;
    int selectedOptionIndex;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    int deviceReasons;
    private Map<String, Integer> reasonIdMap = new HashMap<>();

    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    Spinner optionSpinner;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private MenuItem item;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME = "SaveDeviceReason";
    private final String SOAP_ACTION = "http://tempuri.org/SaveDeviceReason";
    private final String URL = "https://dfhrms.dfindia.org/PMSservice.asmx";


    private static final String SOAP_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_METHOD_NAME = "GetEmployeeLocations";
    private static final String SOAP_ACTION1= "http://tempuri.org/GetEmployeeLocations";
    private final String SOAP_URL = "https://dfhrms.dfindia.org/PMSservice.asmx";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidebar);
        fragmentManager = getSupportFragmentManager();

        SharedPreferences myprefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        employeeId = myprefs.getString("emp_id", "nothing");
        //   deviceSlno = myprefs.getString("DeviceSlno", "nothing");
        deviceSrlno = myprefs.getString("DeviceSrlNo", "nothing");
        str_deviceStatus = myprefs.getString("DeviceStatus", "nothing");
        designations = myprefs.getString("Designation", "nothing");


        Log.e("str_deviceStatus",str_deviceStatus);
        Log.e("DeviceSrlNo",deviceSrlno);
        Log.e("Designation",designations);

        optionSpinner = findViewById(R.id.popup_spinner);

        // Log.e("deviceslide",deviceStatus);

        //new device but old employee is "new"
        if (str_deviceStatus.equalsIgnoreCase("New"))
        {
            showDeviceMismatchPopup();
        }
        //first time loggin is "exist", 2. Same device also "Exist"
        else if (str_deviceStatus.equalsIgnoreCase("Exists")) {
            Fragment fragment = new Fragment_MarkAttndnceClassTakenNew();
            replaceFragment(fragment);
            setTitle("Mark Attendance");

        }
        //employee login with others employee device
        else if (str_deviceStatus.equalsIgnoreCase("Not Exists")){
            showRestrictPopup();
            /*Fragment fragment = new Fragment_MarkAttndnceClassTakenNew();
            replaceFragment(fragment);
            setTitle("Mark Attendance");*/
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open,
                R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();

        if (designations.equalsIgnoreCase("Intern")) {
            // Show all items for Intern
            menu.findItem(R.id.nav_Leave).setVisible(true);
            menu.findItem(R.id.nav_Onduty).setVisible(true);
            menu.findItem(R.id.leave_history).setVisible(true);
            menu.findItem(R.id.nav_Onduty_History).setVisible(true);
            menu.findItem(R.id.nav_Mark_Attendace).setVisible(true);
            menu.findItem(R.id.nav_markclass_Attendance).setVisible(true);
            menu.findItem(R.id.nav_View_Attenance).setVisible(true);
            menu.findItem(R.id.nav_Location).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(true);
        } else {
            // Show/hide items differently for non-Intern
            menu.findItem(R.id.nav_Leave).setVisible(false);
            menu.findItem(R.id.nav_Onduty).setVisible(false);
            menu.findItem(R.id.leave_history).setVisible(false);
            menu.findItem(R.id.nav_Onduty_History).setVisible(false);
            menu.findItem(R.id.nav_Mark_Attendace).setVisible(true);
            menu.findItem(R.id.nav_markclass_Attendance).setVisible(true);
            menu.findItem(R.id.nav_View_Attenance).setVisible(true);
            menu.findItem(R.id.nav_Location).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(true);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Fragment fragment = null;
                Class fragmentClass;
                int id = item.getItemId();

                if(id == R.id.nav_Mark_Attendace)
                {
                    replaceFragment(new Fragment_MarkAttndnceClassTakenNew());
                    setTitle("Mark Attendance");

                }

                else if (id == R.id.nav_View_Attenance)
                {
                    replaceFragment(new Fragment_ViewAttendance());
                    setTitle("View Attendance");


                }else if (id == R.id.nav_markclass_Attendance)
                {
                    replaceFragment(new Fragment_classAttendance());
                    setTitle("Mark Class Attendance");


                }
                else if (id == R.id.nav_classRoom_Feedback)
                {
                    replaceFragment(new Fragment_ClassFeedback());
                    setTitle("Class Feeback");


                }

                else if (id == R.id.leave_history)
                {
                    replaceFragment(new CancelLeaveFragments1());
                    setTitle("Leave History");


                }
                else if (id == R.id.nav_Location) {
                    replaceFragment(new LocationAssignedFragment());
                    setTitle("Location Assigned");
                    showToast("Location AssignedFragment");
                }

                else if (id == R.id.nav_Leave) {
                    replaceFragment(new Leave_request());
                    showToast("Leave Request");
                    setTitle("Leave Request");

                }

                else if (id == R.id.nav_Onduty) {
                    replaceFragment(new OndutyFragment());
                    showToast("On Duty");
                    setTitle("On Duty");

                }
                else if (id == R.id.nav_Onduty_History) {
                    replaceFragment(new Onduty_HistoryFragment());
                    setTitle("Onduty History");
                    showToast("Onduty History");
                }

                else if (id == R.id.nav_logout) {
                    // Logout
                    logout();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            private void logout() {
                startActivity(new Intent(Slidebar_Activity.this, Login.class));
                finish();
                SharedPreferences myprefs = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = myprefs.edit();
                editor.remove("pwd");// Clear all saved credentials
                editor.putBoolean("isLoggedIn", false); // Update the login status
                editor.apply();

            }
        });

    }

    private void showRestrictPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Device Restriction");
        builder.setMessage("Oops!, it seems that you are using a different device. Please use your own device to continue.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "OK" button click here
                // Perform any other action or close the app as needed
                logout();
            }
        });
        builder.setCancelable(false); // Prevent the user from dismissing the dialog by tapping outside
        builder.show();
    }




    private AlertDialog dialog;
    private boolean allowBackButton = false;
// Declare dialog as a class variable

    private void showDeviceMismatchPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Device Mismatch");
        builder.setMessage("This is a new Device. Please confirm that you would like to shift to a new Device");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reason_spinner, null);
        builder.setView(dialogView);

        final EditText reasonEditText = dialogView.findViewById(R.id.reasonEditText);
        final Spinner optionSpinner = dialogView.findViewById(R.id.dropdown_spinner);

        final String[] options = {"Select Reason", "Shifting to a new phone", "Sent for repair"};
        final int[] reasonIds = {0, 1, 2}; // Mapping of options to ReasonIds

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionSpinner.setAdapter(adapter);

        // Inside showDeviceMismatchPopup method
        // Inside showDeviceMismatchPopup method
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reason = reasonEditText.getText().toString();
                selectedOptionIndex = optionSpinner.getSelectedItemPosition();

                if (selectedOptionIndex != 0) {
                    int selectedReasonId = reasonIds[selectedOptionIndex];
                    String enteredRemark = reason;

                    // Get EmployeeId and DeviceSlno from SharedPreferences
                    SharedPreferences myprefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                    String employeeId = myprefs.getString("emp_id", "nothing");
                    String deviceSrlno = myprefs.getString("DeviceSrlNo", "nothing");

                    // Execute the SOAP request
                    new SoapRequestTask(selectedReasonId,  employeeId, deviceSrlno,enteredRemark).execute();
                } else {
                    // If "Select Reason" is still selected, show an error message
                    Toast.makeText(Slidebar_Activity.this, "Please select a reason", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Your Cancel button logic here
                logout();
                dialog.dismiss(); // Dismiss the dialog after handling the Cancel action
            }
        });
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create(); // Create the dialog

        // Set the AlertDialog to be not cancelable when the user taps outside
        dialog.setCanceledOnTouchOutside(false);

        // Disable the "Submit" button initially
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        // Add a text change listener to enable/disable the "Submit" button based on field completion
        reasonEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the "Submit" button if a reason is entered
                String reason = reasonEditText.getText().toString();
                int selectedOptionIndex = optionSpinner.getSelectedItemPosition();
                boolean isSubmitEnabled = selectedOptionIndex != 0 && !reason.isEmpty();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isSubmitEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Show the dialog
        dialog.show();
    }




    public class SoapRequestTask extends AsyncTask<Void, Void, String> {
        private final int selectedReasonId;
        private final String remark;
        private final String employeeId;
        private final String deviceSlno;
        //  private final Activity activity = new Activity(); // Add an activity reference

        // Constructor to pass the activity reference
        public SoapRequestTask(int selectedReasonId,  String employeeId, String deviceSrlno,String remark) {
            this.selectedReasonId = selectedReasonId;

            this.employeeId = employeeId;
            this.deviceSlno = deviceSrlno;
            this.remark = remark;
            // this.activity = activity;
        }

        @Override
        protected String doInBackground(Void... voids) {
            final String NAMESPACE = "http://tempuri.org/";
            final String METHOD_NAME = "SaveDeviceReason";
            final String SOAP_ACTION = "http://tempuri.org/SaveDeviceReason";
            final String URL = "https://dfhrms.dfindia.org/PMSservice.asmx";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("ReasonId", selectedReasonId);
            request.addProperty("Remark", remark);
            request.addProperty("Employee_Id", employeeId);
            request.addProperty("DeviceSlno", deviceSrlno);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (response instanceof SoapObject) {
                    SoapObject soapObject = (SoapObject) response;
                    String result = soapObject.getProperty("result").toString();
                    return result;
                } else if (response instanceof SoapPrimitive) {
                    SoapPrimitive soapPrimitive = (SoapPrimitive) response;
                    String result = soapPrimitive.toString();
                    return result;
                } else {
                    // Handle unknown response type
                    Log.e("SOAP Error", "Unknown SOAP response type");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("SOAP Error", "IO Exception: " + e.getMessage());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.e("SOAP Error", "XmlPullParser Exception: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("SOAP Error", "Exception: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result.equals("Success")) {
                // The SOAP request was successful, and the result is "Success"
                Log.d("SOAP Response", "Request was successful");

                // Show a toast message
                Toast.makeText(getApplicationContext(), "Request was successful", Toast.LENGTH_SHORT).show();
                // Replace the fragment or perform any other actions
                Fragment fragment = new Fragment_MarkAttndnceClassTakenNew();
                replaceFragment(fragment);
                setTitle("Mark Attendance");
            }
        }


    }

    private void logout() {
        startActivity(new Intent(Slidebar_Activity.this, Login.class));
        finish();
        SharedPreferences myprefs = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = myprefs.edit();
        editor.remove("pwd");// Clear all saved credentials
        editor.putBoolean("isLoggedIn", false); // Update the login status
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    private void replaceFragment(Fragment fragment)
    {
       /* FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();*/
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Allows the user to navigate back
        fragmentTransaction.commit();
    }
    private void showToast(String message) {
        Toast.makeText(Slidebar_Activity.this, message, Toast.LENGTH_SHORT).show();
    }
}


