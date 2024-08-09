package com.dfhrms.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import android.widget.DatePicker;
import java.util.Locale;


import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dfhrms.Adapter.Adapter_viewclasstakendetails;
import com.dfhrms.Adapter.Adapter_viewpuncheddetails;
import com.dfhrms.Class.Class_attendanceclassTaken;
import com.dfhrms.Class.Class_attendancedetails;
import com.dfhrms.Utili.ConnectionDetector;
import com.dfhrms.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fragment_ViewAttendance extends Fragment
{

    private ScrollView lLayoutviewattendance;
    Context context;
    private static Context static_context = null;

    Button submit_bt;
    static TextView selectdate_tv;
    static String str_ddmmyyy= "";
    static String str_yyymmdd= "";
    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;
    Long long_Employeeid;
    int year;
    int month;
    int day;
    String str_emp_id,str_response;
    Class_attendancedetails[]  class_attendancedetails_arrayObj;
    TextView nodata_tv,punchedin_tv,punchedout_tv;
    int count=0;
    ListView employeemarkedattendance_listview;
    Adapter_viewpuncheddetails adapter_viewpuncheddetails_obj;
    Adapter_viewclasstakendetails adapter_viewclasstakendetails_obj;

    LinearLayout attendancehistory_ll;
    String str_punchedout_tv="",str_punchedin_tv="";
    Class_attendanceclassTaken[]  class_attendanceclasstaken_arrayObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //context=getActivity();
        lLayoutviewattendance = (ScrollView) inflater.inflate(R.layout.fragment_viewattendance, container, false);
        context = lLayoutviewattendance.getContext();
        static_context=lLayoutviewattendance.getContext();

        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        //Toast.makeText(getActivity(),myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        str_emp_id = myprefs.getString("emp_id", "nothing");
        Log.e("empid",str_emp_id);



        selectdate_tv = (TextView) lLayoutviewattendance.findViewById(R.id.selectdate_tv);
        // Set the current date as the default date
        setDefaultDate();

        //fetchAttendanceDetailsForCurrentDate();


        submit_bt = (Button) lLayoutviewattendance.findViewById(R.id.submit_bt);
        punchedin_tv=(TextView)lLayoutviewattendance.findViewById(R.id.punchedin_tv);
        punchedout_tv=(TextView)lLayoutviewattendance.findViewById(R.id.punchedout_tv);

        employeemarkedattendance_listview=(ListView)lLayoutviewattendance.findViewById(R.id.employeemarkedattendance_listview);
        nodata_tv=(TextView)lLayoutviewattendance.findViewById(R.id.nodata_tv);
        attendancehistory_ll=(LinearLayout)lLayoutviewattendance.findViewById(R.id.attendancehistory_ll);


        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = dateFormat.format(currentDate.getTime());
        selectdate_tv.setText(currentDateString);

        // Set the selected date to the current date
        SimpleDateFormat df_ddmmyyyy = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df_yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
        str_ddmmyyy = df_ddmmyyyy.format(currentDate.getTime());
        str_yyymmdd = df_yyyymmdd.format(currentDate.getTime());

        // Call the web service to fetch attendance details for the current date
        internetDectector = new ConnectionDetector(getActivity());
        isInternetPresent = internetDectector.isConnectingToInternet();

        if (isInternetPresent && Validation1()) {
            AsyncCallWS_GetAttendanceClassdetails task = new AsyncCallWS_GetAttendanceClassdetails(context);
            task.execute();
        }


        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                str_response="fail";
                if(Validation())
                {

                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent)
                    {

                       /* AsyncCallWS_GetEmployeeDayAttendance task = new AsyncCallWS_GetEmployeeDayAttendance(context);
                        task.execute();*/

                        AsyncCallWS_GetAttendanceClassdetails task = new AsyncCallWS_GetAttendanceClassdetails(context);
                        task.execute();
                    }

                }
            }
        });
        selectdate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog with the current date as the initial date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Handle the selected date
                        // You can update the selectdate_tv TextView here with the chosen date
                        String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                        selectdate_tv.setText(selectedDate);

                        // Update the str_ddmmyyy and str_yyymmdd variables if needed
                        str_ddmmyyy = selectedDate;
                        str_yyymmdd = getFormattedDateForServer(selectedDate);

                        // Call the web service or perform any action you need with the selected date
                        if (isInternetPresent && Validation()) {
                            AsyncCallWS_GetAttendanceClassdetails task = new AsyncCallWS_GetAttendanceClassdetails(context);
                            task.execute();
                        }
                    }
                }, year, month, dayOfMonth);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });


        return lLayoutviewattendance;
    }// end onCreate()

    public boolean Validation1() {
        boolean datevalidation = true;

        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat df_yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = df_yyyymmdd.format(currentDate.getTime());

        if (str_yyymmdd.isEmpty() || !str_yyymmdd.equals(currentDateString)) {
            Toast.makeText(getActivity(), "Please select the current date", Toast.LENGTH_SHORT).show();
            datevalidation = false;
        }

        return datevalidation;
    }


  /*  private void fetchAttendanceDetailsForCurrentDate() {
        // Check for internet connectivity
        internetDectector = new ConnectionDetector(getActivity());
        isInternetPresent = internetDectector.isConnectingToInternet();

        if (isInternetPresent) {
            AsyncCallWS_GetEmployeeDayAttendance task = new AsyncCallWS_GetEmployeeDayAttendance(context);
            task.execute();
        } else {
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }*/

    // Method to set the current date as the default date in selectdate_tv TextView
    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        str_ddmmyyy = currentDate;
        str_yyymmdd = getFormattedDateForServer(currentDate);
        selectdate_tv.setText(currentDate);
    }

    // Helper method to convert date format to yyyy-MM-dd format for server request
    private String getFormattedDateForServer(String date) {
        try {
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date currentDate = currentDateFormat.parse(date);
            SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return serverDateFormat.format(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }



    public boolean Validation()
    {
        boolean datevalidation=true;

        // if((str_ddmmyyy.toString().length()==0))
        if((str_yyymmdd.toString().length()==0))
        {
            Toast.makeText(getActivity(), "Kindly select the date", Toast.LENGTH_SHORT).show();
            datevalidation=false;
        }


        if(datevalidation)
        { return true; }
        else{return false; }
    }

    // Start of to date
    @SuppressLint("ValidFragment")
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        Calendar selectedDateTime = new GregorianCalendar(year, month, day, hour, minute);
        SimpleDateFormat df_12hr = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat df_ddmmyyyy = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat df_yyyymmdd = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        str_ddmmyyy = df_ddmmyyyy.format(selectedDateTime.getTime());
        str_yyymmdd = df_yyyymmdd.format(selectedDateTime.getTime());
        selectdate_tv.setText(str_ddmmyyy);
    }





    public class AsyncCallWS_GetAttendanceClassdetails extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;
        Context context;

        protected void onPreExecute() {
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            fetch_attendanceclasstaken();
            return null;
        }

        public AsyncCallWS_GetAttendanceClassdetails(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (str_response.equalsIgnoreCase("Success")) {
                // Update UI with fetched data
                punchedin_tv.setText(str_punchedin_tv);
                punchedout_tv.setText(str_punchedout_tv);

                if (class_attendanceclasstaken_arrayObj.length > 0) {
                    // Process class attendance data if available
                    attendancehistory_ll.setVisibility(View.VISIBLE);
                    employeemarkedattendance_listview.setVisibility(View.VISIBLE);
                    nodata_tv.setVisibility(View.GONE);
                    adapter_viewclasstakendetails_obj = new Adapter_viewclasstakendetails(getActivity(), class_attendanceclasstaken_arrayObj);
                    employeemarkedattendance_listview.setAdapter(adapter_viewclasstakendetails_obj);
                } else {
                    // No class attendance data found
                    attendancehistory_ll.setVisibility(View.VISIBLE);
                    employeemarkedattendance_listview.setVisibility(View.GONE);
                    nodata_tv.setVisibility(View.VISIBLE);
                }
            } else {
                // Handle other cases where response is not "Success"
                attendancehistory_ll.setVisibility(View.VISIBLE);
                employeemarkedattendance_listview.setVisibility(View.GONE);
                nodata_tv.setVisibility(View.VISIBLE);

                // Show toast for "There are no records" message
                if (str_response.equalsIgnoreCase("There are no records")) {
                    punchedin_tv.setText("");
                    punchedout_tv.setText("");
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void fetch_attendanceclasstaken()
    {
        long_Employeeid = Long.parseLong(str_emp_id); // for web service

        //http://dfhrms.dfindia.org/PMSservice.asmx?op=SendCompoffRequest
        //String URL ="http://dfhrms.dfindia.org/PMSservice.asmx?WSDL";
        String URL =getResources().getString(R.string.main_url);;
        String METHOD_NAME = "GetEmployeeClassAttendance";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/GetEmployeeClassAttendance";

        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("EmployeeId", long_Employeeid);//<EmployeeId>long</EmployeeId>
            request.addProperty("Date", str_yyymmdd.trim().toString());//<Date>string</Date>




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);



            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                Log.e("classattendnce_resp", response.toString());
                count = response.getPropertyCount();
                Log.e("count", String.valueOf(count));

                str_response=response.getProperty("Message").toString();

                Log.e("inTime",response.getProperty("InTime").toString());
                Log.e("outTime",response.getProperty("Outtime").toString());

                //anyType{Id=0; Status=false; Message=There are no records; }
                if (response.getProperty("Message").toString().equalsIgnoreCase("Success"))
                {

                    //anyType{Id=0; Date=2022-03-12; InTime=10:27:49; Outtime=10:54:30;

                    // Existing code to retrieve time strings
                    str_punchedin_tv = response.getProperty("InTime").toString();
                    str_punchedout_tv = response.getProperty("Outtime").toString();

// Format the punched out time into 12-hour format with AM/PM
                    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");

                    try {
                        Date outTime = sdf24.parse(str_punchedout_tv);
                        str_punchedout_tv = sdf12.format(outTime);
                    } catch (ParseException e) {
                        // Handle any parse exception
                        e.printStackTrace();
                    }

// Extract hours, minutes, and AM/PM from punched in time
                    try {
                        Date inTime = sdf24.parse(str_punchedin_tv);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(inTime);

                        int hours = calendar.get(Calendar.HOUR);
                        int minutes = calendar.get(Calendar.MINUTE);
                        String amPm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

                        // Format the punched in time into 12-hour format with AM/PM
                        str_punchedin_tv = String.format("%02d:%02d %s", hours, minutes, amPm);
                    } catch (ParseException e) {
                        // Handle any parse exception
                        e.printStackTrace();
                    }



                    if (2 > 1) {

                        int int_count = 0;

                        SoapObject list = (SoapObject) response.getProperty("list");
                        Log.e("listcount", String.valueOf(list.getPropertyCount()));
                        int_count = list.getPropertyCount();

                        class_attendanceclasstaken_arrayObj = new Class_attendanceclassTaken[int_count];

                        for (int i = 0; i < int_count; i++) {
                            SoapObject so_list = (SoapObject) list.getProperty(i);
                            //  Log.e("punch",so_list.getProperty("Punched_At").toString());
                            Class_attendanceclassTaken innerObj = new Class_attendanceclassTaken();

                            innerObj.setStr_classtakenon(so_list.getProperty("InTime").toString());
                            innerObj.setStr_classcompletedon(so_list.getProperty("Outtime").toString());

                            class_attendanceclasstaken_arrayObj[i] = innerObj;
                        }


                    }

                }

                // Handle "There are no records" message
                if (str_response.equalsIgnoreCase("There are no records")) {
                    // Clear the data
                    str_punchedin_tv = "";
                    str_punchedout_tv = "";
                }
            }


            catch (Throwable t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                // str_response="fail";

            }
        }catch (Throwable t) {
            //Toast.makeText(context, "UnRegister Receiver Error " + t.toString(),
            //		Toast.LENGTH_LONG).show();
            Log.e("UnRegister Recei Error", "> " + t.getMessage());
            //Str_response="fail";

        }

    }
    private class AsyncCallWS_GetEmployeeDayAttendance extends AsyncTask<String, Void, Void>
    {

        ProgressDialog dialog;

        Context context;
        protected void onPreExecute()
        {
            //	Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }

        @Override
        protected Void doInBackground(String... params)
        {
            Log.i("DF", "doInBackground");



            fetch_employeeattendance();



            return null;
        }

        public AsyncCallWS_GetEmployeeDayAttendance(Context context1) {
            context =  context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result)

        {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            //if(internet_issue.equals("slow internet"))
            if(1>2)
            {

            }
            else
            {
                if(str_response.toString().equalsIgnoreCase("Success"))
                {


                    if(class_attendancedetails_arrayObj.length>0)
                    {
                        //    Log.e("punchdate",class_attendancedetails_arrayObj[0].getStr_punchedinDateTime().toString());
                        attendancehistory_ll.setVisibility(View.VISIBLE);
                        employeemarkedattendance_listview.setVisibility(View.VISIBLE);
                        nodata_tv.setVisibility(View.GONE);
                        adapter_viewpuncheddetails_obj = new Adapter_viewpuncheddetails(getActivity(),class_attendancedetails_arrayObj);
                        employeemarkedattendance_listview.setAdapter(adapter_viewpuncheddetails_obj);


                    }else{
                        attendancehistory_ll.setVisibility(View.VISIBLE);
                        employeemarkedattendance_listview.setVisibility(View.GONE);
                        nodata_tv.setVisibility(View.VISIBLE);
                    }

                }else{
                    attendancehistory_ll.setVisibility(View.VISIBLE);
                    employeemarkedattendance_listview.setVisibility(View.GONE);
                    nodata_tv.setVisibility(View.VISIBLE);
                }

            }
        }

    }// End of AsyncCallWS3





    public void fetch_employeeattendance()
    {

        long_Employeeid = Long.parseLong(str_emp_id); // for web service

        //http://dfhrms.dfindia.org/PMSservice.asmx?op=SendCompoffRequest
        //String URL ="http://dfhrms.dfindia.org/PMSservice.asmx?WSDL";
        String URL =getResources().getString(R.string.main_url);;
        String METHOD_NAME = "GetEmployeeDayAttendance";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/GetEmployeeDayAttendance";

        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("EmployeeId", long_Employeeid);//<employee_id>long</employee_id>
            request.addProperty("Date", str_ddmmyyy.trim().toString());//<WorkDate>string</WorkDate>

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                Log.e("attendance_response", response.toString());
                count = response.getPropertyCount();
                Log.e("count", String.valueOf(count));
                // str_response="Success";

                // Log.e("responseS",response.getProperty("Message").toString());

                str_response=response.getProperty("Message").toString();
                // Log.e("listD", response.getProperty("list").toString());



                if(count>2)
                {

                    int int_count=0;

                    SoapObject list = (SoapObject)response.getProperty("list");
                    Log.e("listcount", String.valueOf(list.getPropertyCount()));
                    int_count=list.getPropertyCount();

                   /* SoapObject so_list = (SoapObject)  list.getProperty(0);
                    Log.e("punch",so_list.getProperty("Punched_At").toString());
*/
                    class_attendancedetails_arrayObj = new Class_attendancedetails[int_count];

                    for (int i = 0; i < int_count; i++)
                    {
                        SoapObject so_list = (SoapObject)  list.getProperty(i);
                        //  Log.e("punch",so_list.getProperty("Punched_At").toString());
                        Class_attendancedetails innerObj = new Class_attendancedetails();

                        innerObj.setStr_punchedinDateTime(so_list.getProperty("Punched_At").toString());
                        innerObj.setStr_latitude(so_list.getProperty("lat").toString());
                        innerObj.setStr_longitude(so_list.getProperty("lon").toString());
                        innerObj.setStr_location(so_list.getProperty("Location_Code").toString());

                        class_attendancedetails_arrayObj[i] = innerObj;


                    }


                }


            }
            catch (Throwable t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                // str_response="fail";

            }
        }catch (Throwable t) {
            //Toast.makeText(context, "UnRegister Receiver Error " + t.toString(),
            //		Toast.LENGTH_LONG).show();
            Log.e("UnRegister Recei Error", "> " + t.getMessage());
            //Str_response="fail";

        }

    }//End of applyonduty





}
