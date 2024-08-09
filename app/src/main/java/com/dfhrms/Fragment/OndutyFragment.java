package com.dfhrms.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dfhrms.Utili.ConnectionDetector;
import com.dfhrms.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OndutyFragment extends Fragment {

    private ScrollView lLayoutApplyonduty;

    private static Context static_context = null;
    Context context;

    String username, pwd, emp_id, internet_issue = "empty";
    String slow_intenet = "no";

    static TextView clickfromdateonduty_tv;
    static TextView clicktodateonduty_tv;
    EditText locationonduty_et, reasononduty_et;
    Spinner typeofonduty_sp;
    Button applyonduty_bt;

    long Employeeidlong;
    ByteArrayOutputStream response;
    static String yyyyMMdd_todate = "";
    static String yyyyMMdd_fromdate = "";
    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;

    String locationonduty_et_string, typeofonduty_sp_string, reasononduty_et_string;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lLayoutApplyonduty = (ScrollView) inflater.inflate(R.layout.fragment_applyonduty, container, false);

        static_context = requireContext();
        context = requireContext();
        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");

        Employeeidlong = Long.parseLong(emp_id); // for web service

        clickfromdateonduty_tv = (TextView) lLayoutApplyonduty.findViewById(R.id.clickfromdateonduty_TV);
        clicktodateonduty_tv = (TextView) lLayoutApplyonduty.findViewById(R.id.clicktodateonduty_TV);
        locationonduty_et = (EditText) lLayoutApplyonduty.findViewById(R.id.locationonduty_ET);
        reasononduty_et = (EditText) lLayoutApplyonduty.findViewById(R.id.reasononduty_ET);
        typeofonduty_sp = (Spinner) lLayoutApplyonduty.findViewById(R.id.typeofonduty_SP);
        applyonduty_bt = (Button) lLayoutApplyonduty.findViewById(R.id.applyonduty_BT);



        String[] typeofonduty_spinnerdropdown = {
                "At Office","Awareness","Field Visit","Marketing","Observation","Orientation","Presentation",
                "Surprise Visit","Visit"};

        ArrayAdapter<String> typeofonduty_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.leavetypespinneritems, typeofonduty_spinnerdropdown);
        typeofonduty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeofonduty_sp.setAdapter(typeofonduty_adapter);

        clicktodateonduty_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // settodate();
                DialogFragment dFragment = new DatePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getActivity().getFragmentManager(), "Date Picker");

            }
        });

        clickfromdateonduty_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment fromdateFragment = new DatePickerFragmentFromDate();
                fromdateFragment.show(getActivity().getFragmentManager(), "Date Picker");
            }
        });

        applyonduty_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                locationonduty_et_string = locationonduty_et.getText().toString();
                typeofonduty_sp_string = typeofonduty_sp.getSelectedItem().toString();
                reasononduty_et_string = reasononduty_et.getText().toString();

                //Toast.makeText(getActivity(), "Server busy", Toast.LENGTH_SHORT).show();
                if (Validation()) {
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();

                    if (isInternetPresent) {
                        AsyncCallWS3 task = new AsyncCallWS3(context);
                        task.execute();
                    }
                    else{
                        Toast.makeText(getActivity(),"No internet",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return lLayoutApplyonduty;
    }//End of onCreateView
    public boolean Validation()
    {
        boolean validationresult=true;
        boolean datevalidationresult1=true,datevalidationresult2=true,locationvalidationresult=true,reasonvalidationresult=true;
        if(yyyyMMdd_fromdate.toString().length()==0||yyyyMMdd_todate.toString().length()==0)
        {
            Toast.makeText(getActivity(), "Kindly enter the date", Toast.LENGTH_SHORT).show();
            datevalidationresult1=false;
        }
        if((yyyyMMdd_fromdate.toString().length()!=0)&&(yyyyMMdd_todate.toString().length()!=0) )
        {
        /*if(date1.compareTo(date2)<0){ //0 comes when two date are same,
            //1 comes when date1 is higher then date2
            //-1 comes when date1 is lower then date2 }*/

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22

            try {
                Date fromdate = mdyFormat.parse(yyyyMMdd_fromdate);
                Date todate = mdyFormat.parse(yyyyMMdd_todate);

                if(fromdate.compareTo(todate)<=0)
                {
                    datevalidationresult2=true;
                }
                else{
                    Toast.makeText(getActivity(), "Kindly enter valid date", Toast.LENGTH_SHORT).show();
                    datevalidationresult2=false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }//end of if

        if(locationonduty_et.getText().toString().length()==0||locationonduty_et.getText().toString().trim().length()<3)
        {
            locationonduty_et.setError("Minimum 3 character is required");
            locationonduty_et.requestFocus();
            locationvalidationresult=false;
        }
        if(reasononduty_et.getText().toString().length()==0||reasononduty_et.getText().toString().trim().length()<10)
        {
            reasononduty_et.setError("Minimum 10 character is required");
            reasononduty_et.requestFocus();
            reasonvalidationresult=false;
        }


        if(datevalidationresult1&&datevalidationresult2&&locationvalidationresult&&reasonvalidationresult)
        { return true; }
        else{return false; }

    }// End of validation

    public static class DatePickerFragment extends  DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);

            return dialog;


        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }

        public void setDate(final Calendar calendar) {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            //((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));

            clicktodateonduty_tv.setText(dateFormat.format(calendar.getTime()));

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22

            yyyyMMdd_todate = mdyFormat.format(calendar.getTime());

            //  System.out.println("To Date:"+ yyyyMMdd_todate);

            int days_count = Days.daysBetween(new LocalDate(yyyyMMdd_fromdate), new LocalDate(yyyyMMdd_todate)).getDays();
            int int_days=days_count+1;
            System.out.println ("Days: "+int_days);

            if((int_days<=6))
            {
            }
            else{
                alerts_dialog_fortodate();
            }
        }

    }

    // start of from date
    @SuppressLint("ValidFragment")
    public static class DatePickerFragmentFromDate extends  DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);

            return dialog;


        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }

        public void setDate(final Calendar calendar) {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            //((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));


            clickfromdateonduty_tv.setText(dateFormat.format(calendar.getTime()));

            // SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");//2017-06-22

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");
            yyyyMMdd_fromdate = mdyFormat.format(calendar.getTime());

            //  System.out.println("From date:"+ yyyyMMdd_fromdate);
        }

    }

    //****************************************** Date *******************************************************
    // Submit Async Leave request
    private class AsyncCallWS3 extends AsyncTask<String, Void, Void>
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

            applyonduty();

            return null;
        }

        public AsyncCallWS3(Context context1) {
            context =  context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result)

        {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if(internet_issue.equals("slow internet"))
            {

            }
            else
            {

                if(response.toString().equals("Already Leave Applied"))
                {
                    Toast.makeText(context, response.toString() ,Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(response.toString().equals("Fail"))
                    {
                        Toast.makeText(context, "Failed to apply OD" ,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(response.toString().equals("Request sent."))
                        {
                            Toast.makeText(context, "Request has been sent" ,Toast.LENGTH_LONG).show();
                            reasononduty_et.setText("");

                        }
                        else if(response.toString().equals("Already OD Applied"))
                        {
                            Toast.makeText(context, "Already On-Duty has been applied" ,Toast.LENGTH_LONG).show();
                        }
                        else if(response.toString().equalsIgnoreCase("Cannot apply OD more then 7 days"))
                        {
                            Toast.makeText(context, "Cannot apply OD more then 7 days" ,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, ""+response.toString() ,Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            Log.i("DF", "onPostExecute");


        }
    }// End of AsyncCallWS3


    public void applyonduty()
    {
        String URL =getResources().getString(R.string.main_url);
        String METHOD_NAME = "RequestforOD";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/RequestforOD";

        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);

            Log.e("empId", String.valueOf(Employeeidlong));
            Log.e("From_Date", yyyyMMdd_fromdate.trim().toString());
            Log.e("To_Date", yyyyMMdd_todate.trim().toString());
            Log.e("Location", locationonduty_et_string);
            Log.e("Reason", typeofonduty_sp_string);
            Log.e("Description", reasononduty_et_string);

            request.addProperty("empId", Employeeidlong);
            request.addProperty("From_Date", yyyyMMdd_fromdate.trim().toString());
            request.addProperty("To_Date", yyyyMMdd_todate.trim().toString());
            request.addProperty("Location", locationonduty_et_string);
            request.addProperty("Reason", typeofonduty_sp_string);
            request.addProperty("Description", reasononduty_et_string);

            // System.out.println("Onduty message: "+ yyyyMMdd_fromdate);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAPACTION, envelope);
                //	Log.i(TAG, "GetAllLoginDetails is running");
                //		result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                Log.e("string Onduty response",response.toString());
                //Onduty response: Cannot apply OD more then 7 days
                //Already OD Applied
            }
            catch (Throwable t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                internet_issue="slow internet";
            }
        }catch (Throwable t) {
            Log.e("UnRegister Recei Error", "> " + t.getMessage());

        }

    }//End of applyonduty()


    public static void alerts_dialog_fortodate()
    {

        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(static_context);
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("On Duty is for 6days only\n Note: Excluding Week-Off");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

                clicktodateonduty_tv.setText("Click for Calendar");
            }
        });


        final AlertDialog alert = dialog.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();

    }



}//End of applyonduty fragment

