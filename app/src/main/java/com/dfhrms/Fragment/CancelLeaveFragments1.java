package com.dfhrms.Fragment;

/*
import android.app.Fragment;
import android.app.FragmentManager;
*/
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dfhrms.Class.Class_LeaveBalanceSummary;
import com.dfhrms.Class.LeaveDetail;
import com.dfhrms.R;

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
import java.util.Vector;


/**
 * Created by User on 5/23/2017.
 */

public class CancelLeaveFragments1 extends Fragment {

    Button leaverequest_bt,opening_balance_bt,avil_leave_bt,balanceleave_bt,Brought_Forword_bt;

    int count1;
    TableLayout tl;
    LeaveDetail[] leavelist;
    String username,emp_id,gender;
    int i = 0;
    int noOfobjects = 0;

    long leaveidlong;


    Context context;
    Resources resource;
    private ListView listView;
    Context context1;
    Holder holder;


    int int_count_leavebalance=0;
    Class_LeaveBalanceSummary[] class_leavebalancesummary_obj;
    long Employeeidlong;

    String str_status_employeeleavesummary;

    public CancelLeaveFragments1() {
    }


    Button cl_balance_bt,cl_cf_bt,cl_al_bt,cl_total_bt;
    Button sl_balance_bt,sl_cf_bt,sl_al_bt,sl_total_bt;
    Button el_balance_bt,el_cf_bt,el_al_bt,el_total_bt;
    Button total_balance_bt,total_cf_bt,total_al_bt,total_total_bt;
    Button leavetype1,leavetype2,leavetype3,leavetype4;
    TableRow sl_tablerow_tr,el_tablerow_tr,tablerow_tr4;
    LinearLayout leavedetails_linearlayout;
    TextView view_bt;
    LinearLayout openlayout,closelayout;

    ImageButton downarrow_ib,uparrow_ib;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.dfhrms.R.layout.cancelleavefragment1, container, false);
        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        username = myprefs.getString("user1", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");
        Employeeidlong = Long.parseLong(emp_id); // for web service





       // System.out.println("usernameFrompref:"+ username);

        getActivity().setTitle("HRMS");

        leaverequest_bt = (Button) rootView.findViewById(R.id.leave_request1);


        /*opening_balance_bt = (Button) rootView.findViewById(R.id.opening_balance3);
        Brought_Forword_bt = (Button) rootView.findViewById(R.id.Brought_Forward3);
        avil_leave_bt = (Button) rootView.findViewById(R.id.avil_leave3);
        balanceleave_bt = (Button) rootView.findViewById(R.id.close_leave3);*/


        leavetype1=(Button)rootView.findViewById(R.id.leavetype1);
        leavetype2=(Button)rootView.findViewById(R.id.leavetype2);
        leavetype3=(Button)rootView.findViewById(R.id.leavetype3);
        leavetype4=(Button)rootView.findViewById(R.id.leavetype4);

        cl_balance_bt=(Button)rootView.findViewById(R.id.cl_balance_BT);
        cl_cf_bt =(Button)rootView.findViewById(R.id.cl_cf_BT);
        cl_al_bt=(Button)rootView.findViewById(R.id.cl_al_BT);
        cl_total_bt=(Button)rootView.findViewById(R.id.cl_total_BT);

        sl_balance_bt=(Button)rootView.findViewById(R.id.sl_balance_BT);
        sl_cf_bt =(Button)rootView.findViewById(R.id.sl_cf_BT);
        sl_al_bt=(Button)rootView.findViewById(R.id.sl_al_BT);
        sl_total_bt=(Button)rootView.findViewById(R.id.sl_total_BT);

        el_balance_bt=(Button)rootView.findViewById(R.id.el_balance_BT);
        el_cf_bt =(Button)rootView.findViewById(R.id.el_cf_BT);
        el_al_bt=(Button)rootView.findViewById(R.id.el_al_BT);
        el_total_bt=(Button)rootView.findViewById(R.id.el_total_BT);


        total_balance_bt=(Button)rootView.findViewById(R.id. total_balance_BT);
        total_cf_bt =(Button)rootView.findViewById(R.id. total_cf_BT);
        total_al_bt=(Button)rootView.findViewById(R.id. total_al_BT);
        total_total_bt=(Button)rootView.findViewById(R.id. total_total_BT);

        sl_tablerow_tr =(TableRow) rootView.findViewById(R.id. sl_tablerow_TR);
        el_tablerow_tr =(TableRow) rootView.findViewById(R.id. el_tablerow_TR);
        tablerow_tr4=(TableRow)rootView.findViewById(R.id.tablerow_TR4);
        sl_tablerow_tr.setVisibility(View.GONE);
        el_tablerow_tr.setVisibility(View.GONE);
        tablerow_tr4.setVisibility(View.GONE);

        leavedetails_linearlayout=(LinearLayout)rootView.findViewById(R.id.leavedetails_linearlayout);
        leavedetails_linearlayout.setVisibility(View.GONE);

       downarrow_ib=(ImageButton)rootView.findViewById(R.id.downarrow_IB);
        uparrow_ib=(ImageButton)rootView.findViewById(R.id.uparrow_ib);
        uparrow_ib.setVisibility(View.GONE);




       // view_bt=(TextView)rootView.findViewById(R.id.view_BT);

       /* openlayout=(LinearLayout)rootView.findViewById(R.id.layoutview);
        closelayout=(LinearLayout)rootView.findViewById(R.id.layoutview2);
        closelayout.setVisibility(View.GONE);
        openlayout.setVisibility(View.VISIBLE);*/

        listView = (ListView) rootView.findViewById(R.id.customlistview1);

        context = rootView.getContext();
        resource = context.getResources();

       /* AsyncCallWS2_GetEmployeeLeaveSummary task2 =new AsyncCallWS2_GetEmployeeLeaveSummary(context);
        task2.execute();*/

        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();

       /* openlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leavedetails_linearlayout.setVisibility(View.VISIBLE);
                closelayout.setVisibility(View.VISIBLE);
            }
        });
        closelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leavedetails_linearlayout.setVisibility(View.GONE);
                closelayout.setVisibility(View.GONE);
                openlayout.setVisibility(View.VISIBLE);
            }
        });*/


       downarrow_ib.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               if(str_status_employeeleavesummary.equalsIgnoreCase("Success")) {
                   leavedetails_linearlayout.setVisibility(View.VISIBLE);
                   downarrow_ib.setVisibility(View.GONE);
                   uparrow_ib.setVisibility(View.VISIBLE);
               }else{
                   Toast.makeText(getActivity(),""+str_status_employeeleavesummary,Toast.LENGTH_LONG).show();
               }
           }
       });

        uparrow_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                leavedetails_linearlayout.setVisibility(View.GONE);
                downarrow_ib.setVisibility(View.VISIBLE);
                uparrow_ib.setVisibility(View.GONE);
            }
        });







        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Log.d("onItemClick", "position: " + position);
                Log.d("onItemClick", "Item.Name: " + leavelist[position].getName());
                Log.d("onItemClick", "Item.Name: " + leavelist[position].getFrom_Date());
                Log.d("onItemClick", "Item.Name: " + leavelist[position].getEmployee_Id());
                Log.d("onItemClick", "Item.Name: " + leavelist[position].getLeave_Id());*/

                String x = leavelist[position].getFrom_Date();
                String leaveidString = leavelist[position].getLeave_Id();
                leaveidlong = Long.parseLong(leaveidString); // for web service


                //Toast.makeText(getActivity(),x+"_"+y,Toast.LENGTH_LONG).show();

                String formateTodate = "";// in format 05 Mar 2017
                formateTodate = leavelist[position].getTo_DateC();
                formateTodate = formateTodate.substring(5).trim();
               // String formateTodate_changed = Dateformatechange(formateTodate);


               /* if (compareEndDate2CurrentDate(formateTodate_changed))
                {

                    Toast.makeText(getActivity(), x + "_" + leaveidString, Toast.LENGTH_LONG).show();




                    final Dialog dialog = new Dialog(getActivity());

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.leaverollback_dialog);
                    dialog.setCancelable(false);
                    Button dialogokbutton = (Button) dialog.findViewById(R.id.rollback_BT);
                    Button dialogcancelbutton =(Button) dialog.findViewById(R.id.cancelrollback_BT);

                    dialogokbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Toast.makeText(getActivity(),"Request sent to your reporting manager please wait for approval",Toast.LENGTH_LONG).show();
                         //   Toast.makeText(getActivity(),"Request sent",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            AsyncCalltoleaverollback task = new AsyncCalltoleaverollback(context);
                            task.execute();
                        }
                    });

                    dialogcancelbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
*/

            }
        });// End of listview


        leaverequest_bt.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0) {

                //	 Intent i  = new Intent (context ,Practice.class);
                //		startActivity(i);
                Leave_request fragment = new Leave_request();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();

               /* FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();
*/

//content_frame
                //.replace(R.id.frame_container, fragment).addToBackStack( "tag" ).commit();

            }
        });







        return rootView;
    }//end of oncreate


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
            Log.i("DFTech", "doInBackground");
            //  GetAllEvents();
            leave_detaile();  // call of details
            return null;
        }

        public AsyncCallWS2(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();

            if (leavelist != null) {
                CustomAdapter adapter = new CustomAdapter();
                listView.setAdapter(adapter);

                int x = leavelist.length;

            //    System.out.println("Inside the if list adapter" + x);
            } else {
                Log.d("onPostExecute", "leavelist == null");
            }

           // System.out.println("Reached the onPostExecute");

            AsyncCallWS2_GetEmployeeLeaveSummary task2 =new AsyncCallWS2_GetEmployeeLeaveSummary(context);
            task2.execute();

            if (count1 > 0) {


                /*AsyncCallWS2_GetEmployeeLeaveSummary task2 =new AsyncCallWS2_GetEmployeeLeaveSummary(context);
                task2.execute();*/

               /* opening_balance_bt.setText(Double.parseDouble(leavelist[0].getCasualLeave())+
                Double.parseDouble(leavelist[0].getSickLeave())+ " ");*/

               // opening_balance_bt.setText(leavelist[0].getOpening_Balance() + "  ");

               /* Brought_Forword_bt.setText(leavelist[0].getBrought_Forword() + "  ");
                avil_leave_bt.setText(leavelist[0].getAvailedLeaves() + "  ");*/

               // balanceleave_bt.setText((Double.parseDouble(leavelist[0].getBrought_Forword()) +
                 //Double.parseDouble(leavelist[0].getOpening_Balance())+

               /* balanceleave_bt.setText((Double.parseDouble(leavelist[0].getBrought_Forword()) +
                        Double.parseDouble(leavelist[0].getCasualLeave())+
                                Double.parseDouble(leavelist[0].getSickLeave())-
                                Double.parseDouble(leavelist[0].getAvailedLeaves()) + "  "));*/
            }


        }//end of onPostExecute
    }//End of Async task


    public class CustomAdapter extends BaseAdapter {


        public CustomAdapter() {

            super();
            Log.d("Inside CustomAdapter()", "Inside CustomAdapter()");
        }

        @Override
        public int getCount() {

            String x = Integer.toString(leavelist.length);
            Log.d("leavelist.length", x);
            return leavelist.length;

        }

        @Override
        public Object getItem(int position) {
            String x = Integer.toString(position);
          //  System.out.println("getItem position" + x);
            Log.d("getItem position", "x");
            return leavelist[position];
        }

        @Override
        public long getItemId(int position) {
            String x = Integer.toString(position);
          //  System.out.println("getItemId position" + x);
            Log.d("getItemId position", x);
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            Log.d("CustomAdapter", "position: " + position);

            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item, parent, false);

                holder.date = (TextView) convertView.findViewById(R.id.approved_on2);
                holder.days = (TextView) convertView.findViewById(R.id.days2);
                holder.fromView = (TextView) convertView.findViewById(R.id.from2);
                holder.toView = (TextView) convertView.findViewById(R.id.to2);
                holder.reason = (TextView) convertView.findViewById(R.id.reason2);
                holder.status = (TextView) convertView.findViewById(R.id.status2);
                holder.cancel = (TextView) convertView.findViewById(R.id.cancelLeave_TV);

                Log.d("Inside If convertView", "Inside If convertView");

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                Log.d("Inside else convertView", "Inside else convertView");
            }

            LeaveDetail detail = (LeaveDetail) getItem(position);

            holder.fromView.setText(detail.getFrom_Date());
            holder.toView.setText(detail.getTo_Date());
            holder.days.setText(detail.getno_days());
            holder.date.setText(detail.getApproved_On());
            holder.reason.setText(detail.getReason());
            holder.status.setText(detail.getLEave_Status());

            String formateTodate = "";// in format 05 Mar 2017
            formateTodate = detail.getTo_DateC();
            formateTodate = formateTodate.substring(5).trim();
          //  String formateTodate_changed = Dateformatechange(formateTodate);

           // System.out.println("detail.getTo_DateC():" + formateTodate_changed);


           /* if (compareEndDate2CurrentDate(formateTodate_changed)) {

                holder.cancel.setText("Cancel");
                System.out.println("compareEndDate2CurrentDateCancelText:" + formateTodate_changed);
            }*/

            // holder.cancel.setText("Cancel");


            return convertView;


        }


    }//End of CustomAdapter

    private class Holder {
        TextView fromView;
        TextView toView;
        TextView date;
        TextView status;
        TextView days;
        TextView reason;
        TextView cancel;
    }


    public void leave_detaile() {
        Vector<SoapObject> result1 = null;


       // String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL"; //com on June6,2017

        // String METHOD_NAME = "intCount";//"NewAppReleseDetails";
        // String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
        // String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";

        //http://dfhrms.cloudapp.net/PMSservice.asmx
        //http://dfhrms.cloudapp.net/PMSservice.asmx?op=LeaveCancelRequest

       // String METHOD_NAME = "GetEmployeeDetails";//"NewAppReleseDetails";  //com on June6,2017
       // String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";  //com on June6,2017

        //Df hrms
      //  String URL="http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

        String URL=getResources().getString(R.string.main_url);
        String METHOD_NAME = "GetEmployeeDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";


        //Df hrms

        //dftestbed starts
       /* String URL="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";
        String METHOD_NAME = "GetEmployeeDetails";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeDetails";
*/
        //dftestbed ends

        try {
            // String  versioncode = this.getPackageManager()
            //        .getPackageInfo(this.getPackageName(), 0).versionName;
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);

           request.addProperty("email", username);
          //  request.addProperty("email", "raghavendra.tech@dfmail.org");

            //  request.addProperty("to", 9);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                //  Log.i(TAG, "GetAllLoginDetails is running");
                //    result1 = (Vector<SoapObject>) envelope.getResponse();
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());
                count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

                Log.i("number of rows", "" + count1);

                noOfobjects = count1;


              //  System.out.println("Number of object" + noOfobjects);
                // leavelist[0].setName("kya");
                leavelist = new LeaveDetail[count1];

                for (int i = 0; i < count1; i++) {
                    SoapObject list = (SoapObject) response.getProperty(i);
                    SoapPrimitive no_of_Days, Leave_Id, Name, email, Program, Employee_Id, Opening_Balance, AvailedLeaves, manager_email, From_Date, To_Date, Reason, LEave_Status, Approved_On,soapprimitive_casualleave,soapprimitive_clcarryforward,soapprimitive_sickleave,soapprimitive_slcarryforward;
                    String approved = "";
                    Leave_Id = (SoapPrimitive) list.getProperty("Leave_Id");
                    Name = (SoapPrimitive) list.getProperty("Name");
                    email = (SoapPrimitive) list.getProperty("email");
                    Program = (SoapPrimitive) list.getProperty("Program");
                    Employee_Id = (SoapPrimitive) list.getProperty("Employee_Id");
                    // Opening_Balance = (SoapPrimitive) list.getProperty("Opening_Balance");

                    soapprimitive_casualleave =(SoapPrimitive) list.getProperty("Casual_Leave");
                    //soapprimitive_clcarryforward =(SoapPrimitive)list.getProperty("CL_carry_forward");
                    soapprimitive_sickleave =(SoapPrimitive)list.getProperty("Sick_Leave");
                    //soapprimitive_slcarryforward=(SoapPrimitive)list.getProperty("SL_carry_forward");


                    AvailedLeaves = (SoapPrimitive) list.getProperty("AvailedLeaves");
                    // manager_email= (SoapPrimitive)list.getProperty("manager_email");
                    From_Date = (SoapPrimitive) list.getProperty("From_Date");
                    To_Date = (SoapPrimitive) list.getProperty("To_Date");
                    Reason = (SoapPrimitive) list.getProperty("Reason");
                    no_of_Days = (SoapPrimitive) list.getProperty("No_of_Days");
                    LEave_Status = (SoapPrimitive) list.getProperty("LEave_Status");


                    if (list.getProperty("Approved_On").toString().equals("anyType{}")) {
                        // Approved_On.add("");
                        // Approved_On = (SoapPrimitive);
                    } else {
                        approved = list.getProperty("Approved_On").toString();
                    }
                    LeaveDetail project = new LeaveDetail();
                    // leavelist[i].setLeave_Id(Leave_Id.toString());
                    Log.i("value at name premitive", Name.toString());
                    project.setLeave_Id(Leave_Id.toString());
                    project.setName(Name.toString());
                    project.setemail(email.toString());
                    project.setProgram(Program.toString());
                    project.setEmployee_Id(Employee_Id.toString());

                    //project.setOpening_Balance(Opening_Balance.toString());

                    project.setOpening_Balance("20");

                    project.setCasualLeave(soapprimitive_casualleave.toString());
                  //  project.setCLCarryForward(soapprimitive_clcarryforward.toString());
                    project.setSickLeave(soapprimitive_sickleave.toString());
                 //   project.setSLCarryForward(soapprimitive_slcarryforward.toString());
                    project.setAvailedLeaves(AvailedLeaves.toString());
                    project.setno_days(no_of_Days.toString());
                    //   leavelist[i].setmanager_email(manager_email.toString());
                    project.setFrom_Date(From_Date.toString().substring(4, From_Date.toString().length() - 5));
                    // Log.i("string value at messege",From_Date.toString().substring(3));
                    project.setTo_Date(To_Date.toString().substring(4, To_Date.toString().length() - 5));
                    project.setReason(Reason.toString());
                    project.setLEave_Status(LEave_Status.toString());
                    project.setApproved_On(approved);
                    project.setBrought_Forword(list.getProperty("carry_forward").toString());

                    project.setFrom_DateC(From_Date.toString());
                    project.setTo_DateC(To_Date.toString());
                    //   leavelist[i].setApproved_On(Approved_On.toString());
                    leavelist[i] = project;

                }// End of for loop


                //   version = (SoapPrimitive)response.getProperty("AppVersion");
                // release_not = (SoapPrimitive)response.getProperty("ReleseNote");


                //Log.i("string value at messeg",messeg.toString());


            } catch (Throwable t) {
                //Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                //    Toast.LENGTH_LONG).show();

                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Throwable t) {
            Log.e("UnRegister  Error", "> " + t.getMessage());

        }

    }//End of leaveDetail method


    public String Dateformatechange(String receviedDate) {
        //DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy"); // for parsing input
        DateFormat df1 = new SimpleDateFormat("dd MMM yyyy"); // for parsing input
        DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");  // for formatting output
        //String inputDate = "03 mar 2016";
        String inputDate = receviedDate;
        Date d = null;
        try {
            d = (Date) df1.parse(inputDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String outputDate = df2.format(d); // => "30/03/2016"
       // System.out.println("output" + outputDate);

        return outputDate;
    }// end of DateformatChange


    public Boolean compareEndDate2CurrentDate(String receviedTodate) {

        String receviedTodateWithAdd = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarObj = null;
        calendarObj = Calendar.getInstance();
        try {
            calendarObj.setTime(sdf1.parse(receviedTodate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        calendarObj.add(Calendar.DATE, 8);
        //sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date resultdate = new Date(calendarObj.getTimeInMillis());
        receviedTodateWithAdd = sdf1.format(resultdate);
      //  System.out.println("receviedTodateWithAddCancel:" + receviedTodateWithAdd);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String pattern = "dd/MM/yyyy";
        String currentDate2compareTodate = "";
        currentDate2compareTodate = sdf.format(new Date());
      //  System.out.println("currentDateInformat:" + currentDate2compareTodate);

        Date date4 = null;
        Date date5 = null;

        SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date4 = sdformat1.parse(receviedTodateWithAdd);
            Log.e("Receivedtodate: ",date4.toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            date5 = sdformat1.parse(currentDate2compareTodate);
            Log.e("currentdate: ",date5.toString());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (date4.compareTo(date5) >= 0) {
            //System.out.println("date4 is greater than/equal to date5"+date4 +"###"+date5);
            return true;

        } else {
            //System.out.println("Date4 is less than to Date5"+date4+"###"+date5);\
            return false;
        }


    }// End of comapareEndDate2currentDate


    private class AsyncCalltoleaverollback extends AsyncTask<String, Void, Void>
    {

        String response_AsyncCalltoleaverollback="";

        ProgressDialog dialog;






        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");

            String wait="Please wait..";
            SpannableString ss2=  new SpannableString(wait);
            //ss2.setSpan(new ForegroundColorSpan(#00635a), 0, ss2.length(), 0);



            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f2f2f2")));

            ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.greenTextcolor)), 0, ss2.length(), 0);
            dialog.setMessage(ss2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params) {
            Log.i("df leaverollback", "doInBackground");
            //  GetAllEvents();
             // call of the leavecancelwebservice
            if(2>1)
            {

              //  String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";

                //http://dftestbed.cloudapp.net/PMSservice.asmx  //list of webservice
                //http://dftestbed.cloudapp.net/PMSservice.asmx?op=LeaveCancelRequest  //contains detailas of LeaveCancelRequest

              //String URL ="http://dftestbed.cloudapp.net/PMSservice.asmx?WSDL";  // WSDL file

                String URL =getResources().getString(R.string.main_url);  // WSDL file
                String METHOD_NAME = "LeaveCancelRequest";//leave
                String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/LeaveCancelRequest";



                try{

                    SoapObject request = new SoapObject(Namespace, METHOD_NAME);
                    request.addProperty("id", leaveidlong);   // <id>long</id>
                    //request.addProperty("id", 4015);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    //Set output SOAP object
                    envelope.setOutputSoapObject(request);
                    //Create HTTP call object
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    try
                    {

                        androidHttpTransport.call(SOAPACTION, envelope);
                        SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();

                        response_AsyncCalltoleaverollback=response.toString();
                    //    System.out.println("leaveid in long:"+leaveidlong);
                     //   System.out.println("cancel response:"+response_AsyncCalltoleaverollback);
                        Log.d("response leavecancel",response.toString());
                    }
                    catch (Throwable t) {
                        //Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                        //		Toast.LENGTH_LONG).show();
                        Log.e("request fail", "> " + t.getMessage());
                    }
                }catch (Throwable t) {
                    Log.e("UnRegister  Error", "> " + t.getMessage());

                }


            }// end of if statement


            return null;
        }

        public AsyncCalltoleaverollback(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

           /* if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }*/

            dialog.dismiss();


            if(response_AsyncCalltoleaverollback.equals("Request sent"))
            {
                Toast.makeText(getActivity(),"LeaveRoll back,sent to your Functional Head",Toast.LENGTH_LONG).show();
            }
            else
            {
               Toast.makeText(getActivity(),"Server busy,Please try later...",Toast.LENGTH_LONG).show();
    }



}
    }//End of AsyncCalltoleaverollback












    private class AsyncCallWS2_GetEmployeeLeaveSummary extends AsyncTask<String, Void, Void> {
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

            employeeleavesummary();  // call of details
            return null;
        }

        public AsyncCallWS2_GetEmployeeLeaveSummary(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result)
        {

            dialog.dismiss();



            if(str_status_employeeleavesummary.equalsIgnoreCase("Success"))
            {

                if (int_count_leavebalance > 0) {

                    if (int_count_leavebalance >= 1) {
                        leavetype1.setText(class_leavebalancesummary_obj[0].getLeavesType().toString());
                        cl_balance_bt.setText(class_leavebalancesummary_obj[0].getLeaveBalanceType().toString());
                        cl_cf_bt.setText(class_leavebalancesummary_obj[0].getCarryForward().toString());
                        cl_al_bt.setText(class_leavebalancesummary_obj[0].getLeavesTaken().toString());
                        cl_total_bt.setText(class_leavebalancesummary_obj[0].getTotalBalance().toString());
                    }


                    if (int_count_leavebalance >= 2) {

                        sl_tablerow_tr.setVisibility(View.VISIBLE);

                        leavetype2.setText(class_leavebalancesummary_obj[1].getLeavesType().toString());
                        sl_balance_bt.setText(class_leavebalancesummary_obj[1].getLeaveBalanceType().toString());
                        sl_cf_bt.setText(class_leavebalancesummary_obj[1].getCarryForward().toString());
                        sl_al_bt.setText(class_leavebalancesummary_obj[1].getLeavesTaken().toString());
                        sl_total_bt.setText(class_leavebalancesummary_obj[1].getTotalBalance().toString());
                    }

                    if (int_count_leavebalance >= 3) {
                        leavetype3.setText(class_leavebalancesummary_obj[2].getLeavesType().toString());
                        el_tablerow_tr.setVisibility(View.VISIBLE);
                        el_balance_bt.setText(class_leavebalancesummary_obj[2].getLeaveBalanceType().toString());
                        el_cf_bt.setText(class_leavebalancesummary_obj[2].getCarryForward().toString());
                        el_al_bt.setText(class_leavebalancesummary_obj[2].getLeavesTaken().toString());
                        el_total_bt.setText(class_leavebalancesummary_obj[2].getTotalBalance().toString());
                    }


                    if (int_count_leavebalance >= 4) {
                        leavetype4.setText(class_leavebalancesummary_obj[3].getLeavesType().toString());
                        tablerow_tr4.setVisibility(View.VISIBLE);
                        total_balance_bt.setText(class_leavebalancesummary_obj[3].getLeaveBalanceType().toString());
                        el_cf_bt.setText(class_leavebalancesummary_obj[3].getCarryForward().toString());
                        el_al_bt.setText(class_leavebalancesummary_obj[3].getLeavesTaken().toString());
                        el_total_bt.setText(class_leavebalancesummary_obj[3].getTotalBalance().toString());
                    }

                    // Double.parseDouble(leavelist[0].getCasualLeave())
                }

            }
            else{
                //Toast.makeText(getActivity(),""+str_status_employeeleavesummary,Toast.LENGTH_LONG).show();
            }


        }//end of onPostExecute
    }//End of Async task







    public void employeeleavesummary() {



        String URL=getResources().getString(R.string.main_url);
        String METHOD_NAME = "GetEmployeeLeaveSummary";
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeLeaveSummary";





        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);

            request.addProperty("EmployeeId", Employeeidlong);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


            /*<vmleaveSummary>
<Leave_Type>1</Leave_Type>
<Leaves_Type>CL</Leaves_Type>
<Opening_Balance>10</Opening_Balance>
<carry_forward>0</carry_forward>
<carry_forward_Year>NA</carry_forward_Year>
<Leaves_Taken>12.5</Leaves_Taken>
<Balance>-2.5</Balance>
<Status>Success</Status>
</vmleaveSummary>*/

            /*<vmleaveSummary>
            <Leave_Type>5</Leave_Type>
            <Leaves_Type>EL</Leaves_Type>
            <Opening_Balance>0</Opening_Balance>
            <carry_forward>30</carry_forward>
            <carry_forward_Year>2018-19</carry_forward_Year>
            <Leaves_Taken>0</Leaves_Taken>
            <Balance>30</Balance>
            <Status>Success</Status>

            <Leave_Type>0</Leave_Type>
            <Opening_Balance>0</Opening_Balance>
            <carry_forward>0</carry_forward>
            <Leaves_Taken>0</Leaves_Taken>
            <Balance>0</Balance>
            <Status>leave Details not Found</Status>



            */


            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());

                SoapObject so_status = (SoapObject) response.getProperty(0);

               str_status_employeeleavesummary= String.valueOf((SoapPrimitive) so_status.getProperty("Status"));

               if(str_status_employeeleavesummary.equalsIgnoreCase("Success")) {

                   int_count_leavebalance = response.getPropertyCount();  // number of count in array in response 6,0-5,5


                   noOfobjects = int_count_leavebalance;


                   //  System.out.println("Number of object" + noOfobjects);
                   // leavelist[0].setName("kya");
                   class_leavebalancesummary_obj = new Class_LeaveBalanceSummary[int_count_leavebalance];


                   for (int i = 0; i < int_count_leavebalance; i++) {
                       SoapObject list = (SoapObject) response.getProperty(i);
                       SoapPrimitive leavestype, leavebalancetype, carryforwad, leavestaken, totalbalance, status;
                       String approved = "";
                       leavestype = (SoapPrimitive) list.getProperty("Leaves_Type");
                       leavebalancetype = (SoapPrimitive) list.getProperty("Opening_Balance");
                       carryforwad = (SoapPrimitive) list.getProperty("carry_forward");
                       leavestaken = (SoapPrimitive) list.getProperty("Leaves_Taken");
                       totalbalance = (SoapPrimitive) list.getProperty("Balance");
                       status = (SoapPrimitive) list.getProperty("Status");

                       Log.e("totalbalance", totalbalance.toString());


                       Class_LeaveBalanceSummary inner_leavebalanceSummaryobject = new Class_LeaveBalanceSummary();

                       inner_leavebalanceSummaryobject.setLeavesType(leavestype.toString());
                       inner_leavebalanceSummaryobject.setLeaveBalanceType(leavebalancetype.toString());
                       inner_leavebalanceSummaryobject.setCarryForward(carryforwad.toString());
                       inner_leavebalanceSummaryobject.setLeavesTaken(leavestaken.toString());
                       inner_leavebalanceSummaryobject.setTotalBalance(totalbalance.toString());


                       inner_leavebalanceSummaryobject.setStatus(status.toString());


                       class_leavebalancesummary_obj[i] = inner_leavebalanceSummaryobject;

                       Log.e("class", class_leavebalancesummary_obj[i].getTotalBalance().toString());

                   }// End of for loop


               }

            } catch (Throwable t) {

                Log.e("request fail", "> " + t.getMessage());
                str_status_employeeleavesummary=t.getMessage().toString();
            }
        } catch (Throwable t) {
            Log.e("UnRegister  Error", "> " + t.getMessage());
            str_status_employeeleavesummary=t.getMessage().toString();

        }

    }//End of leaveDetail method










}//end of class
