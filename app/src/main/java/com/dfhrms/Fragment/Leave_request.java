package com.dfhrms.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
/*import android.app.DialogFragment;*/
/*import android.app.Fragment;
import android.app.FragmentManager;*/
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dfhrms.Class.AllTeamList;
import com.dfhrms.Class.Class_LeaveTypeOnGender;
import com.dfhrms.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/*
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
*/

public class Leave_request extends Fragment {
    int Teamcount;
    String slow_intenet = "no";
    AllTeamList[] ash;
    AllTeamList st;
    Calendar calendar, Fromcalendar1 = Calendar.getInstance(), Tocalendar1 = Calendar.getInstance();
    Button b;
    Spinner spinner;
    EditText phone, leavereason_et, taskdetails_et;
    int leave_type_id;

    //String phon_value,str_leavereason="empty",str_leave_type_one,leave_type_Originale , TupeValue[] ={"First Half", "Full Day","Second Half"},LeaveTypeValue[] ={"Casual Leave", "Medical Leave","Maternity Leave","Paternity Leave","Earned Leave"},send_leave_type;

    String phon_value, str_leavereason = "empty", str_leave_type_one, leave_type_Originale, LeaveTypeValue[] = {"Casual Leave", "Sick Leave", "Earned Leave"}, send_leave_type;

    String  TupeValue[] = {"Full Day", "First Half", "Second Half"};

    static String static_str_leave_type;
    ///////////////
  /*  String LeaveTypeValue_male[] = {"Casual Leave", "Sick Leave", "Earned Leave", "Paternity Leave"};
    String LeaveTypeValue_female[] = {"Casual Leave", "Sick Leave", "Earned Leave", "Miscarriage Leave", "Maternity Leave"};
*/

    SimpleDateFormat format;
    Context context;
    SoapPrimitive response;
    String servererror;
    String username, pwd, str_emp_id, internet_issue = "empty", gender,married_unmarried;

    int int_leavetypecount;
    Class_LeaveTypeOnGender[] objarray_class_leavetypeongenders;
    Class_LeaveTypeOnGender obj_class_leavetypeongenders;


    /*WheelView leave_type;
    OnWheelChangedListener listener4;
*/
    public Leave_request() {
    }


    static TextView clickfromdate_tv;
    static TextView clicktodate_tv;
    static String yyyyMMdd_fromdate = "";
    static String yyyyMMdd_todate = "";

    static TextView reasonfortodate_tv;
    CalendarView calendarView;
    static TextView paternityerror_tv;

    Date date_fromdate,date_todate;
    int days;
    int int_days;

    Spinner sp_leavetype,sp_duration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.date_layout1, container, false);
        context = rootView.getContext();
        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        str_emp_id = myprefs.getString("emp_id", "nothing");
        ///////////////
        gender = myprefs.getString("gender", "nothing");
        // gender = "F";
        System.out.println("gender..." + gender);


       // married_unmarried  = myprefs.getString("married_unmarried", "nothing");
      //  married_unmarried = "Married";
      //  System.out.println("married_unmarried..." + married_unmarried);




        //Calendar calendar = Calendar.getInstance();
        //Date date = new Date();
        ////////////////////////////////////
        calendar = Calendar.getInstance(Locale.getDefault());


        sp_leavetype=(Spinner)rootView.findViewById(R.id.sp_leavtype);
        sp_duration=(Spinner)rootView.findViewById(R.id.sp_duration);
        phone = (EditText) rootView.findViewById(R.id.phone);
        leavereason_et = (EditText) rootView.findViewById(R.id.leavereason_ET);
        reasonfortodate_tv =(TextView) rootView.findViewById(R.id.reasonfortodate_TV);
        paternityerror_tv=(TextView) rootView.findViewById(R.id.paternityerror_TV);


        clickfromdate_tv = (TextView) rootView.findViewById(R.id.clickfromdate_TV);
        clicktodate_tv = (TextView) rootView.findViewById(R.id.clicktodate_TV);


        taskdetails_et = (EditText) rootView.findViewById(R.id.taskdetails_ET);
        //   calendar1.setTime(calendar.get(Calendar .DAY_OF_MONTH) - 1);


       /* final WheelView month1 = (WheelView) rootView.findViewById(R.id.month1);
        final WheelView year1 = (WheelView) rootView.findViewById(R.id.year1);
        final WheelView day1 = (WheelView) rootView.findViewById(R.id.day1);

        final WheelView month2 = (WheelView) rootView.findViewById(R.id.month2);
        final WheelView year2 = (WheelView) rootView.findViewById(R.id.year2);
        final WheelView day2 = (WheelView) rootView.findViewById(R.id.day2);

        final WheelView month3 = (WheelView) rootView.findViewById(R.id.month3);
*/
       // leave_type = (WheelView) rootView.findViewById(R.id.LeaveType);

        spinner = (Spinner) rootView.findViewById(R.id.spinner);




        ArrayAdapter<String> typeofleave_adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.leavetypespinneritems, TupeValue);
        typeofleave_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_duration.setAdapter(typeofleave_adapter);





        // Spinner click listener
        GetTeamlistAsyncCallWS task = new GetTeamlistAsyncCallWS(context);
        task.execute();

        // Spinner Drop down elements

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                st = (AllTeamList) spinner.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });



        sp_leavetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                obj_class_leavetypeongenders=(Class_LeaveTypeOnGender)sp_leavetype.getSelectedItem();

                static_str_leave_type=obj_class_leavetypeongenders.getleavetype_ID().toString();

                clickfromdate_tv.setText("Click for Calendar");
                clicktodate_tv.setText("Click for Calendar");
                reasonfortodate_tv.setVisibility(View.GONE);

              //  Toast.makeText(context, "ID "+obj_class_leavetypeongenders.getleavetype_ID()+"Leave "+obj_class_leavetypeongenders.getleavetype().toString(), Toast.LENGTH_LONG).show();

                /*6 -CL / SL
                5-Earned Leave
                3-Maternity Leave //26 weeks 182days
                4-Paternity Leave // 15days
                7-Miscarriage Leave// 6weeks 42days*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        sp_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                str_leave_type_one=sp_duration.getSelectedItem().toString();


                //Toast.makeText(context,""+str_leave_type_one,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        b = (Button) rootView.findViewById(R.id.submit);

        b.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                 format = new SimpleDateFormat("yyyy-MM-dd");
                phon_value = phone.getText().toString();
                str_leavereason = leavereason_et.getText().toString();

                //Log.e("From date before", String.valueOf(Fromcalendar1.before(Tocalendar1)));
                //Log.e("From Date", String.valueOf(Fromcalendar1.getTime()));
                //Log.e("To date", String.valueOf(Tocalendar1.getTime()));
                //Log.e("From date equals", String.valueOf(Fromcalendar1.equals(Tocalendar1)));

                // if(Fromcalendar1.before(Tocalendar1) || Fromcalendar1.equals(Tocalendar1) )

                if (Validation())
                {

                    //  Toast.makeText(context,"correct " , 6000).show();
                    if (leavereason_et.getText().toString().equals("")) {
                        Toast.makeText(context, "Enter Reason of leave", Toast.LENGTH_SHORT).show();

                    } else {
                        if (str_leave_type_one.equals("Full Day")) {
                            send_leave_type = "1";
                        } else {
                            send_leave_type = "0.5";
                        }


                        AsyncCallWS2 task = new AsyncCallWS2(context);
                        task.execute();




                        try {
                            date_fromdate=new SimpleDateFormat("yyyy-mm-dd").parse(yyyyMMdd_fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                        try {
                            date_todate=new SimpleDateFormat("yyyy-mm-dd").parse(yyyyMMdd_todate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                       // int count =getDaysDifference(date_fromdate,date_todate);
    //*********************************************************************************************************
                        //count the days

                        /*int days_count = Days.daysBetween(new LocalDate(yyyyMMdd_fromdate), new LocalDate(yyyyMMdd_todate)).getDays();
                        days=days_count+1;
                        System.out.println ("Days: "+days);
                        Log.e("Leave_Request days", String.valueOf(days));*/
     //*********************************************************************************************************
                        //Toast.makeText(context, "count"+x, Toast.LENGTH_LONG).show();


//
//
//                        int weeks = Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
//                        System.out.println ("weeks: "+weeks);

                       // String newDate = format.format(calendar.getTime());
//                        int numbDays = dateDifference(yyyyMMdd_fromdate);
//                        System.out.println ("numbDays: "+numbDays);
//                        System.out.println ("newDate: "+yyyyMMdd_fromdate);
                    }
                }//end first if
                else {
                    Toast.makeText(context, "Select proper date", Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(MainActivity.this,"From Date = "+ format.format(Fromcalendar1.getTime()).toString() , 6000).show();
                //  Toast.makeText(MainActivity.this,"To Date = "+ format.format(Tocalendar1.getTime()).toString() , 6000).show();
                //  Toast.makeText(MainActivity.this,"Leave Type = "+leave_type  , 6000).show();
                //  Toast.makeText(MainActivity.this,"Phone = "+phon_value  , 6000).show();
                //  Toast.makeText(MainActivity.this,"Reason = "+str_leavereason  , 6000).show();


            }
        });


        /*OnWheelChangedListener listener1 = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //  Toast.makeText(MainActivity.this, "oldvalue ="+oldValue+" New Value "+newValue,Toast.LENGTH_LONG).show();
                updateDays1(year1, month1, day1);

                Log.e("updateDays1 value", String.valueOf(day1));
            }
        };*/

       /* OnWheelChangedListener listener3 = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //  Toast.makeText(MainActivity.this, "oldvalue ="+oldValue+" New Value "+newValue,Toast.LENGTH_LONG).show();
                updateDays3(month3);
            }
        };
*/

       /*  listener4 = new OnWheelChangedListener()
        {
            public void onChanged(WheelView wheel, int oldValue, int newValue)
            {
                //  Toast.makeText(MainActivity.this, "oldvalue ="+oldValue+" New Value "+newValue,Toast.LENGTH_LONG).show();
                updateDays4(leave_type);
            }
        };

*/
       /* OnWheelChangedListener listener2 = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays2(year2, month2, day2);
            }
        };*/


        //type
        //   int curvalue=1;

        //       type.setViewAdapter(new DateArrayAdapter(this, TupeValue, curvalue));
        //       type.setCurrentItem(curvalue);

        // month


        /*int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[]{"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"};
        month1.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
        month1.setCurrentItem(curMonth);
        month1.addChangingListener(listener1);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        year1.setViewAdapter(new DateNumericAdapter(context, curYear, curYear + 10, 0));
        year1.setCurrentItem(curYear);
        year1.addChangingListener(listener1);

        //day
        updateDays1(year1, month1, day1);
        day1.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day1.addChangingListener(listener1);

        month2.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
        month2.setCurrentItem(curMonth);
        month2.addChangingListener(listener2);


        year2.setViewAdapter(new DateNumericAdapter(context, curYear, curYear + 10, 0));
        year2.setCurrentItem(curYear);
        year2.addChangingListener(listener2);

        updateDays2(year2, month2, day2);
        day2.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day2.addChangingListener(listener2);

        updateDays3(month3);
        month3.setViewAdapter(new DateArrayAdapter(context, TupeValue, 1));
        month3.setCurrentItem(0);
        month3.addChangingListener(listener3);

        updateDays4(leave_type);

          leave_type.setViewAdapter(new DateArrayAdapter(context, LeaveTypeValue, 1));
          leave_type.setCurrentItem(0);
          leave_type.addChangingListener(listener4);
*/





        //////////////////////////////////////////////////////////////
      /*  updateDays4(leave_type);


            if (gender.equals("Male")) {
                leave_type.setViewAdapter(new DateArrayAdapter(context, LeaveTypeValue_male, 1));
            } else if (gender.equals("Female")) {
                leave_type.setViewAdapter(new DateArrayAdapter(context, LeaveTypeValue_female, 1));

            }

        leave_type.setCurrentItem(0);
        leave_type.addChangingListener(listener4);*/

        // year
        //  int curYear = calendar.get(Calendar.YEAR);

        //day


        clickfromdate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                DialogFragment fromdateFragment = new DatePickerFragmentFromDate();
                fromdateFragment.show(getFragmentManager(), "Date Picker");
            }
        });


        clicktodate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // settodate();
                DialogFragment dFragment = new DatePickerFragment();
                // Show the date picker dialog fragment
                //noinspection deprecation
                dFragment.show(getFragmentManager(), "Date Picker");

            }
        });


        return rootView;
    } //End of oncreate();


    public void onBackPressed() {

        getActivity().finish();

    }

    /***********************************************************************************************/
    private class GetTeamlistAsyncCallWS extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("", "onProgressUpdate---tab2");
        }

        public GetTeamlistAsyncCallWS(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i("TAG", "doInBackground---tab5");

            getTeamList();

            //  AddprojectDetail();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute---tab5");

            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if (slow_intenet.equals("no"))
            {
                if (Teamcount < 1)
             {
                    Toast.makeText(context, "No TeamMember", Toast.LENGTH_SHORT).show();
                } else
                {

                    ArrayAdapter dataAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, ash);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);


                    // LeaveType based on Gender
                    AsyncCallWS_GetLeaveTypeOnGender task_2 = new AsyncCallWS_GetLeaveTypeOnGender(context);
                    task_2.execute();

                    // LeaveType based on Gender

                }



            } else {
                Toast.makeText(context, "You are on slow internet", Toast.LENGTH_SHORT).show();
            }


        }


    }//End of GetTeamlistAsyncCallWS
    /********************************************************************************************/

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */

    /*void updateDays1(WheelView year, WheelView month, WheelView day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());


        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));

        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);


        if (day.getCurrentItem() != 0) {
            calendar.set(Calendar.DAY_OF_MONTH, curDay);
        }
        Fromcalendar1 = (Calendar) calendar.clone();


        format = new SimpleDateFormat("yyyy-MM-dd");

        // Toast.makeText(MainActivity.this, format.format(newCalendar.getTime()).toString(),Toast.LENGTH_LONG).show();

    }

    void updateDays3(WheelView month) {

        str_leave_type_one = TupeValue[month.getCurrentItem()];

        //   Toast.makeText(context, "selected leave_type "+leave_type,Toast.LENGTH_LONG).show();

    }
*/


    /*********************************************************************************/
    //Start o getTeamList
    public void getTeamList() {
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "TeamList";//"NewAppReleseDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/TeamList";

        //Dftestbed starts
    /*String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";
    String METHOD_NAME = "TeamList";
    String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/TeamList";*/
        //dftestbed end here


        SoapObject request = new SoapObject(Namespace, METHOD_NAME);
        request.addProperty("id", str_emp_id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAPACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Log.i("response empoylee list", response.toString());
            Teamcount = response.getPropertyCount();


            //   Log.i("string value at respose count",count+"");


            if (Teamcount > 0) {
                ash = new AllTeamList[Teamcount];
                for (int i = 0; i < Teamcount; i++) {
                    SoapObject messeg1 = (SoapObject) response.getProperty(i);
                    //     tt=  +i+" Title " +messeg1.getProperty("Title").toString()+ " Status "+messeg1.getProperty("ProjectStatus").toString();
                    //     Log.i("Project N0",tt);
                    AllTeamList project = new AllTeamList();
                    project.setid(messeg1.getProperty("Id").toString());
                    ;
                    project.setemployeeName(messeg1.getProperty("Name").toString());
                    ;
                    // ash[i].setproject_title(messeg1.getProperty("Title").toString()) ;
                    project.setemail(messeg1.getProperty("email").toString());
                    ;
                    project.setemployee_id(messeg1.getProperty("Employee_Id").toString());
                    ;
                    // ash[i].setStatus(messeg1.getProperty("ProjectStatus").toString()) ;
                    ash[i] = project;
                }


            }


        } catch (Exception e) {
            slow_intenet = "yes";
            e.printStackTrace();
        }
    }// End of getTeamList

    /*void updateDays4(WheelView LeaveType1)
    {

         leave_type_Originale= LeaveTypeValue[LeaveType1.getCurrentItem()];


        //////////////////////////////////////////////


       *//* if (gender.equals("Male")) {
            if ((LeaveTypeValue_male[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_male[0]) || ((LeaveTypeValue_male[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_male[1]))))) {
                leave_type_Originale = LeaveTypeValue_male[LeaveType1.getCurrentItem()];

                leave_type_id = 6;
                System.out.println("leave_type_Originale cl/sl.... " + leave_type_Originale);
            }
            if ((LeaveTypeValue_male[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_male[2]))) {

                leave_type_Originale = LeaveTypeValue_male[LeaveType1.getCurrentItem()];

                leave_type_id = 5;
                //System.out.println("leave_type_Originale el.... "+leave_type_Originale);

            }
            if ((LeaveTypeValue_male[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_male[3]))) {
                leave_type_id = 4;
                leave_type_Originale = LeaveTypeValue_male[LeaveType1.getCurrentItem()];

                // System.out.println("leave_type_Originale p.... "+leave_type_Originale);

            }


        } else if (gender.equals("Female")) {
            if ((LeaveTypeValue_female[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_female[0]) || ((LeaveTypeValue_female[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_female[1]))))) {
                leave_type_id = 6;
                leave_type_Originale = LeaveTypeValue_female[LeaveType1.getCurrentItem()];
                //  System.out.println("leave_type_Originale cl/sl.... "+leave_type_Originale);

            }
            if ((LeaveTypeValue_female[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_female[2]))) {
                leave_type_id = 5;
                leave_type_Originale = LeaveTypeValue_female[LeaveType1.getCurrentItem()];

                // System.out.println("leave_type_Originale el.... "+leave_type_Originale);

            }
            if ((LeaveTypeValue_female[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_female[3]))) {
                leave_type_Originale = LeaveTypeValue_female[LeaveType1.getCurrentItem()];
                leave_type_id = 7;
                //System.out.println("leave_type_Originale misca.... "+leave_type_Originale);

            }
            if ((LeaveTypeValue_female[LeaveType1.getCurrentItem()].equals(LeaveTypeValue_female[4]))) {
                leave_type_id = 3;
                leave_type_Originale = LeaveTypeValue_female[LeaveType1.getCurrentItem()];

                //  System.out.println("leave_type_Originale m.... "+leave_type_Originale);

            }
            //leave_type_Originale = LeaveTypeValue_female[LeaveType1.getCurrentItem()];

        }
        //  Toast.makeText(context, "selected leave_type "+leave_type,Toast.LENGTH_LONG).show();
*//*
    }
*/

    /*void updateDays2(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);

        if (day.getCurrentItem() != 0) {
            calendar.set(Calendar.DAY_OF_MONTH, curDay);
        }
        Tocalendar1 = (Calendar) calendar.clone();


        DateFormat format = new SimpleDateFormat("yy-mmm-dd");
        //        Toast.makeText(MainActivity.this,"yes "+ format.format(calendar.getTime()).toString() , 6000).show();
        // Toast.makeText(MainActivity.this, "curDayn ="+curDay,Toast.LENGTH_LONG).show();

    }*/

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
   /* private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        *//**
         * Constructor
         *//*
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;

            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF00665c);
            }
            view.setTypeface(Typeface.SANS_SERIF);

        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            //   Toast.makeText(MainActivity.this, "currentValue"+currentValue,Toast.LENGTH_LONG).show();
            //   Toast.makeText(MainActivity.this, "currentItem"+currentItem,Toast.LENGTH_LONG).show();
            return super.getItem(index, cachedView, parent);
        }
    }*/

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
  /*  private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        *//**
         * Constructor
         *//*
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF00665c);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }*/


    // Submit Async Leave request
    private class AsyncCallWS2 extends AsyncTask<String, Void, Void> {

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
            Log.i("df tech", "doInBackground");


            //  GetAllEvents();
            applyleave();


            return null;
        }

        public AsyncCallWS2(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();

            }
            if (internet_issue.equals("slow internet")) {

                Toast.makeText(context, "" + servererror.toString(), Toast.LENGTH_LONG).show();
            } else {
                if (response.toString().equals("Already Leave Applied")) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.toString().equals("Fail"))
                    {
                        Toast.makeText(context, "Fail to apply leave", Toast.LENGTH_LONG).show();
                    } else {
                        if (response.toString().equals("Success")) {
                            Toast.makeText(context, "successfully applied leave", Toast.LENGTH_LONG).show();
                            FindPeopleFragment fragment = new FindPeopleFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_frame, fragment).commit();

                            //.replace(R.id.frame_container, fragment).commit();
                            //content_frame
                        } else {
                            Toast.makeText(context, "" + response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
            Log.i("Dftech", "onPostExecute");


        }
    }

    public void applyleave() {

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";  //comJ06

        String URL = getResources().getString(R.string.main_url);
        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";

        // String METHOD_NAME = "ApplyLeavefortesting";//"NewAppReleseDetails"; //comJ06
        //String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/ApplyLeavefortesting";  //comJ06

        //df testbed
        // String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";

        //using upto17April2019
        /*String METHOD_NAME = "ApplyLeavefortesting";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ApplyLeavefortesting";*/


        //Changed to 17April2019
        String METHOD_NAME = "ApplyLeavewithAllTypeParam";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ApplyLeavewithAllTypeParam";
///////////////////////

       /* String METHOD_NAME = "ApplyLeavewithAllTypeParam";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/ApplyLeavewithAllTypeParam";

*/

       long long_emp_id= Long.parseLong(str_emp_id);
       int int_leavetype = Integer.parseInt(obj_class_leavetypeongenders.getleavetype_ID());

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
//        
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
           // request.addProperty("Employee_Id", emp_id);
            /*request.addProperty("employeeId", str_emp_id);
            request.addProperty("fromDate", yyyyMMdd_fromdate);
            request.addProperty("to", yyyyMMdd_todate);
            request.addProperty("LeaveType", leave_type_Originale);
            request.addProperty("Duration", send_leave_type);
            request.addProperty("Incharge", st.getid()+"-"+st.getemployeeName() );
            request.addProperty("InchargeDetails", task.getText().toString() );
            request.addProperty("reason", str_leavereason);*/










            Log.e("Employee_Id", String.valueOf(long_emp_id));//<Employee_Id>long</Employee_Id>
            Log.e("Leave_Type_Id", String.valueOf(int_leavetype));//<Leave_Type_Id>int</Leave_Type_Id>
            Log.e("From_Date", yyyyMMdd_fromdate);//<From_Date>string</From_Date>
            Log.e("To_Date", yyyyMMdd_todate);//<To_Date>string</To_Date>
            Log.e("InchargeName", st.getid()+"-"+st.getemployeeName());//<InchargeName>string</InchargeName>
            Log.e("Reason", str_leavereason);//<Reason>string</Reason>
            Log.e("Leave_Type", send_leave_type);// <Leave_Type>string</Leave_Type>
            Log.e("Incharge_Details", taskdetails_et.getText().toString());//<Incharge_Details>string</Incharge_Details>

            request.addProperty("Employee_Id", long_emp_id);//<Employee_Id>long</Employee_Id>
            request.addProperty("Leave_Type_Id", int_leavetype);//<Leave_Type_Id>int</Leave_Type_Id>
            request.addProperty("From_Date", yyyyMMdd_fromdate);//<From_Date>string</From_Date>
            request.addProperty("To_Date", yyyyMMdd_todate);//<To_Date>string</To_Date>
            request.addProperty("InchargeName", st.getid()+"-"+st.getemployeeName());//<InchargeName>string</InchargeName>
            request.addProperty("Reason", str_leavereason);//<Reason>string</Reason>
            request.addProperty("Leave_Type", send_leave_type);// <Leave_Type>string</Leave_Type>
            request.addProperty("Incharge_Details", taskdetails_et.getText().toString());//<Incharge_Details>string</Incharge_Details>










            //  request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            Log.e("LeaveRequest", request.toString());
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //  Log.i(TAG, "GetAllLoginDetails is running");
                //    result1 = (Vector<SoapObject>) envelope.getResponse();
                response = (SoapPrimitive) envelope.getResponse();
                Log.i("string value atsponse", response.toString());


                //   SoapPrimitive messege = (SoapPrimitive)response.getProperty("Status");
                // version = (SoapPrimitive)response.getProperty("AppVersion");
                // release_not = (SoapPrimitive)response.getProperty("ReleseNote");


                //Log.i("string value at messeg",messeg.toString());


            } catch (Throwable t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //    Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                servererror = t.getMessage().toString();
                internet_issue = "slow internet";
            }
        } catch (Throwable t) {
            //Toast.makeText(context, "UnRegister Receiver Error " + t.toString(),
            //    Toast.LENGTH_LONG).show();
            servererror = t.getMessage().toString();
            Log.e("UnRegister Recei Error", "> " + t.getMessage());
            internet_issue = "slow internet";

        }

    }
    
  /*    public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK) {
              // your code
              return true;
          }

          return super.onKeyDown(keyCode, event);
      }*/


    // start of from date
    //@SuppressLint("ValidFragment")
    public static class DatePickerFragmentFromDate extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            /*return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);*/


            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);


       /* dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
*/

       /* dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH,150);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
*/
            return dialog;


        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }

        public void setDate(final Calendar calendar)
        {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            //((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));


            clickfromdate_tv.setText(dateFormat.format(calendar.getTime()));

            // SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");//2017-06-22

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");
            yyyyMMdd_fromdate = mdyFormat.format(calendar.getTime());


            System.out.println("From date:"+ yyyyMMdd_fromdate);

            Calendar c = Calendar.getInstance();
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

            clicktodate_tv.setText("Click for Calendar");
            paternityerror_tv.setVisibility(View.GONE);

            if(static_str_leave_type.equals("4"))//Paternity Leave
            {
                try {
                    c.setTime(mdyFormat.parse(yyyyMMdd_fromdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 14);

                System.out.println("+15days:" + mdyFormat.format(c.getTime()));
                // clicktodate_tv.setText(mdyFormat.format(c.getTime()));
                /*clicktodate_tv.setText(outputFormat.format(c.getTime()));
                yyyyMMdd_todate=mdyFormat.format(c.getTime());
                clicktodate_tv.setClickable(false);*/
                reasonfortodate_tv.setVisibility(View.VISIBLE);
                reasonfortodate_tv.setText("Paternity Leave Max:15days");
            }

            else if(static_str_leave_type.equals("7"))//7-Miscarriage Leave// 6weeks 42days
            {
                try {
                    c.setTime(mdyFormat.parse(yyyyMMdd_fromdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 41);

                System.out.println("+42days:" + mdyFormat.format(c.getTime()));
                // clicktodate_tv.setText(mdyFormat.format(c.getTime()));
                clicktodate_tv.setText(outputFormat.format(c.getTime()));
                yyyyMMdd_todate=mdyFormat.format(c.getTime());
                reasonfortodate_tv.setVisibility(View.VISIBLE);
                reasonfortodate_tv.setText("Miscarriage Leave for 42days");
                clicktodate_tv.setClickable(false);

            }
            else if(static_str_leave_type.equals("3"))//3-Maternity Leave //26 weeks 182days
            {
                try {
                    c.setTime(mdyFormat.parse(yyyyMMdd_fromdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 181);

                System.out.println("+182days:" + mdyFormat.format(c.getTime()));
                // clicktodate_tv.setText(mdyFormat.format(c.getTime()));
                clicktodate_tv.setText(outputFormat.format(c.getTime()));
                yyyyMMdd_todate=mdyFormat.format(c.getTime());
                reasonfortodate_tv.setVisibility(View.VISIBLE);
                reasonfortodate_tv.setText("Maternity Leave for 182days");
                clicktodate_tv.setClickable(false);
            }
            else
            { clicktodate_tv.setClickable(true);

                reasonfortodate_tv.setVisibility(View.GONE);
              clicktodate_tv.setText("Click for Calendar");

            }

        }

    }

// end of from date


    // Start of to date
    //  @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            /*return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);*/


            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    this, year, month, day);

            // dialog.getDatePicker().setMaxDate(c.getTimeInMillis()); // this will set maximum date validation
            //dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            //dialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            // dialog.getDatePicker().setMaxDate(c.getTimeInMillis()+((1000*60*60*24*90)));//Error:fromDate: Sun Jun 18 12:50:44 GMT+05:30 2017 does not precede toDate: Fri Jun 09 02:45:11 GMT+05:30 2017


           /* dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            c.add(Calendar.DAY_OF_MONTH,150);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());*/

            return dialog;


        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }

        public void setDate(final Calendar calendar) {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            //((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));

            clicktodate_tv.setText(dateFormat.format(calendar.getTime()));

            SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22

            yyyyMMdd_todate = mdyFormat.format(calendar.getTime());

          //  getDaysDifference(mdyFormat,mdyFormat);

            // System.out.println("To Date:"+ yyyyMMdd_todate);



            if(static_str_leave_type.equals("4")) //paternity
            {
                int days_count = Days.daysBetween(new LocalDate(yyyyMMdd_fromdate), new LocalDate(yyyyMMdd_todate)).getDays();
                int int_days = days_count + 1;
                System.out.println("Days: " + int_days);
                Log.e("Leave_Request days", String.valueOf(int_days));
                if((int_days<=15||(int_days==1)))
                { paternityerror_tv.setVisibility(View.GONE);
                }
                else{
                    paternityerror_tv.setVisibility(View.VISIBLE);
                    paternityerror_tv.requestFocus();
                    paternityerror_tv.setError("Min15days");
                }
            }





        }

    }


    @SuppressLint("NewApi")
    public boolean Validation()
    {
        boolean datevalidationresult1 = true, datevalidationresult2 = true, datepaternityvalidation3=true;
        boolean alternativevalidationresult4=true,taskdetailsvalidationresult5=true,leavereasonvalidationresult6=true;

        if (yyyyMMdd_fromdate.toString().length() == 0 || yyyyMMdd_todate.toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Kindly enter the date", Toast.LENGTH_SHORT).show();
            datevalidationresult1 = false;
        }
///////
         //if(married_unmarried.equals("Married")) {
             /*if (gender.equals("Male")) {

                 if (leave_type_Originale.equals(LeaveTypeValue_male[3])) {
                     if (days >= 15) {
                         Toast.makeText(getActivity(), "Exceeds 15 days", Toast.LENGTH_SHORT).show();
                         datevalidationresult1 = false;
                     }
                 }
             }
        // }

            if (gender.equals("Female")) {
                System.out.println("leave_type_Originale. f....." + leave_type_Originale);
                System.out.println("LeaveTypeValue_female[3]. f....." + LeaveTypeValue_female[3]);


                if (leave_type_Originale.equals(LeaveTypeValue_female[3])) {
                    // ChronoUnit.DAYS.between(yyyyMMdd_fromdate,yyyyMMdd_todate);

                    if (days >= 42) {
                        System.out.println("entered valid. 42d.....");

                        Toast.makeText(getActivity(), "Exceeds 42 days", Toast.LENGTH_SHORT).show();
                        datevalidationresult1 = false;
                    }
                }
            }


        if (gender.equals("Female")) {
            System.out.println("leave_type_Originale. f....." + leave_type_Originale);
            System.out.println("LeaveTypeValue_female[4]. f....." + LeaveTypeValue_female[4]);


            if (leave_type_Originale.equals(LeaveTypeValue_female[4])) {
                if (days >= 182) {
                    Toast.makeText(getActivity(), "Exceeds 182 days", Toast.LENGTH_SHORT).show();
                    datevalidationresult1 = false;
                }
            }
        }*/



        if ((yyyyMMdd_fromdate.toString().length() != 0) && (yyyyMMdd_todate.toString().length() != 0))
        {
        /*if(date1.compareTo(date2)<0){ //0 comes when two date are same,
            //1 comes when date1 is higher then date2
            //-1 comes when date1 is lower then date2 }*/

             SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");  //2017-06-22



            try {
                Date fromdate = mdyFormat.parse(yyyyMMdd_fromdate);
                Date todate = mdyFormat.parse(yyyyMMdd_todate);

                if (fromdate.compareTo(todate) <= 0) {
                    datevalidationresult2 = true;
                } else {
                    Toast.makeText(getActivity(), "Kindly enter valid date", Toast.LENGTH_SHORT).show();
                    datevalidationresult2 = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }//end of try catch

        }

        //obj_class_leavetypeongenders.getleavetype_ID().toString()
        if(static_str_leave_type.equals("4")) //paternity
        {
            int days_count = Days.daysBetween(new LocalDate(yyyyMMdd_fromdate), new LocalDate(yyyyMMdd_todate)).getDays();
            int_days = days_count + 1;
            System.out.println("Days: " + int_days);
            Log.e("Leave_Request days", String.valueOf(int_days));
            if((int_days<=15||(int_days==1)))
            {
                datepaternityvalidation3=true;
            }
            else{ datepaternityvalidation3=false;
                Toast.makeText(getActivity(), "Todate is more than 15days", Toast.LENGTH_SHORT).show();
            }
        }





        if(phone.getText().toString().trim().length()==0)
        {
            phone.setError("Zero is not allowed");
            phone.requestFocus();
            alternativevalidationresult4= false;
        }else if(phone.getText().toString().trim().length()<=9||phone.getText().toString().trim().length()>10)
        {
            phone.setError("Min 10 digit");
            phone.requestFocus();
            alternativevalidationresult4 = false;
        }else
        {

        }

        if(taskdetails_et.getText().toString().trim().length()==0||taskdetails_et.getText().toString().trim().length()<=5)
        {
            taskdetails_et.setError("Min 6 character");
            taskdetails_et.requestFocus();
            taskdetailsvalidationresult5= false;
        }

       if(leavereason_et.getText().toString().trim().length()==0||leavereason_et.getText().toString().trim().length()<=9)
       {
           leavereason_et.setError("Min 10 character");
           leavereason_et.requestFocus();
           leavereasonvalidationresult6= false;
       }


        if (datevalidationresult1 && datevalidationresult2 && datepaternityvalidation3 && alternativevalidationresult4
                &&taskdetailsvalidationresult5&&leavereasonvalidationresult6) {
            return true;
        } else {
            return false;
        }

    }// end of validation





    public static int getDaysDifference(Date fromDate,Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }





//-------------------------------End of AsyncCallWS_GetLeaveTypeOnGender

    private class AsyncCallWS_GetLeaveTypeOnGender extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        Context context;

        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("", "onProgressUpdate---tab2");
        }

        public AsyncCallWS_GetLeaveTypeOnGender(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected Void doInBackground(String... params)
        {
            Log.i("TAG", "doInBackground---tab5");

            get_leavetype_basedon_gender();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute---tab5");

            if (dialog.isShowing()) {
                dialog.dismiss();


                if (int_leavetypecount<=0)
                {
                    Toast.makeText(context, "No Leave Type", Toast.LENGTH_SHORT).show();
                } else
              {
                  ArrayAdapter dataAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, objarray_class_leavetypeongenders);
                  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                  sp_leavetype.setAdapter(dataAdapter);


              }


                }

            }

        }


    //-------------------------------End of AsyncCallWS_GetLeaveTypeOnGender

    @SuppressLint("SuspiciousIndentation")
    public void get_leavetype_basedon_gender() {
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "GetLeaveTypeOnGender";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetLeaveTypeOnGender";


        Long emp_id_long= Long.valueOf(str_emp_id);

        //http://tempuri.org/GetLeaveTypeOnGender

        //Dftestbed starts
    /*String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";
    String METHOD_NAME = "TeamList";
    String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/TeamList";*/
        //dftestbed end here


        SoapObject request = new SoapObject(Namespace, METHOD_NAME);
        request.addProperty("EmployeeId", emp_id_long);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAPACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Log.e("response leavetype", response.toString());
            int_leavetypecount = response.getPropertyCount();


               Log.i("int_leavetypecount",int_leavetypecount+"");


            if (int_leavetypecount > 0)
            {
                objarray_class_leavetypeongenders = new Class_LeaveTypeOnGender[int_leavetypecount];
                for (int i = 0; i < int_leavetypecount; i++)
                {
                    SoapObject soapobject_leavetypeongender = (SoapObject) response.getProperty(i);

                    Class_LeaveTypeOnGender inner_object = new Class_LeaveTypeOnGender();
                    inner_object.setleavetype_ID(soapobject_leavetypeongender.getProperty("Id").toString());

                    inner_object.setleavetype(soapobject_leavetypeongender.getProperty("Leave_Type").toString());

                    Log.e("leaveType",soapobject_leavetypeongender.getProperty("Leave_Type").toString());
                    objarray_class_leavetypeongenders[i] = inner_object;
                }


            }


        } catch (Exception e) {
            slow_intenet = "yes";
            e.printStackTrace();
        }
    }// End of getTeamList







}// end of class
