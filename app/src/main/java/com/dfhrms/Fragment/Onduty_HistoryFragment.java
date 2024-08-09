package com.dfhrms.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dfhrms.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

public class Onduty_HistoryFragment extends Fragment {
    private LinearLayout lLayoutondutyhistory;

    String username,pwd,emp_id,internet_issue="empty";
    String slow_intenet="no";
    long Employeeidlong;


    OndutyhistoryClass[] ondutyhistoryclass_arrayObj;

    Context context;
    Resources resource;
    private ListView listView;
    int count1;
    int noOfobjects = 0;



    ArrayList<OndutyhistoryClass> listitems_arraylist = new ArrayList<>();
    private static RecyclerView.Adapter adapter_recycler;
    RecyclerView myrecyclerview_view;
    LinearLayoutManager MyLayoutManager;


    String str_cardview_location,str_cardview_noofdays,str_cardview_fromdate,str_cardview_status;
    String str_cardview_approvedon,str_cardview_typeofod,str_cardview_reason,str_cardview_todate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lLayoutondutyhistory=(LinearLayout) inflater.inflate(R.layout.fragment_ondutyhistory, container, false);
        myrecyclerview_view = (RecyclerView) lLayoutondutyhistory.findViewById(R.id.cardView);
        // context = lLayoutondutyhistory.getContext();
        SharedPreferences myprefs= this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");


        myrecyclerview_view.setHasFixedSize(true);
        MyLayoutManager = new LinearLayoutManager(getActivity());

        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview_view.setLayoutManager(MyLayoutManager);


        getActivity().setTitle("Onduty History");

        //listView = (ListView) lLayoutondutyhistory.findViewById(R.id.customlistview_ondutyhistory);

        Employeeidlong = Long.parseLong(emp_id); // for web service


        context = lLayoutondutyhistory.getContext();
        resource = context.getResources();


        AsyncCallWS2 task = new AsyncCallWS2(context);
        task.execute();



        return lLayoutondutyhistory;
    }


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
            //Log.i(TAG, "onProgressUpdate---
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i("DFTech", "doInBackground");
            //  GetAllEvents();
            onduty_history();  // call of details
            return null;
        }

        public AsyncCallWS2(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();

            if (ondutyhistoryclass_arrayObj != null) {
                adapter_recycler = new MyAdapter_Card(listitems_arraylist);
                myrecyclerview_view.setAdapter(adapter_recycler);
            } else {
                Log.d("onPostExecute", "ondutyhistoryclass_arrayObj == null");
            }
        }//end of onPostExecute
    }// end Async task


    public void onduty_history() {
        Vector<SoapObject> result1 = null;

        String URL = getResources().getString(R.string.main_url);
        String METHOD_NAME = "GetEmployeeODList";
        String Namespace = "http://tempuri.org/";
        String SOAPACTION = "http://tempuri.org/GetEmployeeODList";

        try {
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("id", Employeeidlong);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAPACTION, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("value at response", response.toString());
                count1 = response.getPropertyCount();

                noOfobjects = count1;

                ondutyhistoryclass_arrayObj = new OndutyhistoryClass[count1];

                listitems_arraylist.clear();
                for (int i = 0; i < count1; i++) {
                    SoapObject list = (SoapObject) response.getProperty(i);
                    SoapPrimitive location, days, status, typeofod, description, fromdate, todate, odapprovedon;
                    String approved = "";
                    location = (SoapPrimitive) list.getProperty("Location");
                    days = (SoapPrimitive) list.getProperty("No_of_days");
                    status = (SoapPrimitive) list.getProperty("Od_status");
                    typeofod = (SoapPrimitive) list.getProperty("Reason");
                    description = (SoapPrimitive) list.getProperty("Description");
                    fromdate = (SoapPrimitive) list.getProperty("From_Date");
                    todate = (SoapPrimitive) list.getProperty("To_Date");
                    odapprovedon = (SoapPrimitive) list.getProperty("Approved_Date");

                    OndutyhistoryClass innerObj = new OndutyhistoryClass();
                    Log.i("onduty location", location.toString());
                    innerObj.setLocation(location.toString());
                    innerObj.setDays(days.toString());
                    innerObj.setStatus(status.toString());
                    innerObj.setTypeofOD(typeofod.toString());
                    innerObj.setDescription(description.toString());
                    Log.e("Fromdate", fromdate.toString());
                    innerObj.setFromDate(fromdate.toString());
                    innerObj.setToDate(todate.toString());
                    innerObj.setODApprovedOn(odapprovedon.toString());

                    ondutyhistoryclass_arrayObj[i] = innerObj;
                    listitems_arraylist.add(innerObj);
                }
            } catch (Throwable t) {
                Log.e("request fail", "> " + t.getMessage());
            }
        } catch (Throwable t) {
            Log.e("UnRegister  Error", "> " + t.getMessage());
        }
    }//End of onduty_history method


    public class MyAdapter_Card extends RecyclerView.Adapter<MyViewHolder_X> {
        private ArrayList<OndutyhistoryClass> list;

        public MyAdapter_Card(ArrayList<OndutyhistoryClass> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder_X onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_odhistory_layout, parent, false);
            MyViewHolder_X holder = new MyViewHolder_X(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder_X holder, int position) {

            str_cardview_location =list.get(position).getLocation();
            str_cardview_noofdays = list.get(position).getDays();
            str_cardview_fromdate= list.get(position).getFromDate();
            str_cardview_todate=list.get(position).getToDate();
            str_cardview_status=list.get(position).getStatus();
            str_cardview_approvedon=list.get(position).getODApprovedOn();
            str_cardview_typeofod=list.get(position).getTypeofOD();
            str_cardview_reason=list.get(position).getDescription();

            Typeface type = Typeface.createFromAsset(getResources().getAssets(),"fonts/GOTHICB.TTF");
            holder.holder_cardview_locationlabel_tv.setTypeface(type);
            holder.holder_cardview_dayslabel_tv.setTypeface(type);
            holder.holder_cardview_fromdate_label_tv.setTypeface(type);
            holder.holder_cardview_todate_label_tv.setTypeface(type);
            holder.holder_cardview_status_label_tv.setTypeface(type);
            holder.holder_cardview_approved_label_tv.setTypeface(type);
            holder.holder_cardview_typeofod_label_tv.setTypeface(type);
            holder.holder_cardview_reason_label_tv.setTypeface(type);

            if(!listitems_arraylist.isEmpty())
            {
                holder.holder_cardview_location_tv.setText(str_cardview_location);
                holder.holder_cardview_noofdays_tv.setText(str_cardview_noofdays);
                holder.holder_cardview_fromdate_tv.setText(str_cardview_fromdate);
                holder.holder_cardview_todate_tv.setText(str_cardview_todate);
                holder.holder_cardview_status_tv.setText(str_cardview_status);
                holder.holder_cardview_approved_tv.setText(str_cardview_approvedon);
                holder.holder_cardview_typeofod_tv.setText(str_cardview_typeofod);
                holder.holder_cardview_reason_tv.setText(str_cardview_reason);


                if(str_cardview_status.equalsIgnoreCase("Unapproved"))
                {
                    holder.holder_cardview_status_tv.setTextColor((getResources().getColor(R.color.colorPinkdark)));
                }


            }//end of if statement

            else
            {

            }


        }

        @Override
        public int getItemCount() {
            Log.e("listsize", String.valueOf(list.size()));
            return list.size();

        }
    } //end of my class adapter





    public class MyViewHolder_X extends RecyclerView.ViewHolder
    {


        TextView holder_cardview_locationlabel_tv;
        TextView holder_cardview_location_tv;
        TextView holder_cardview_dayslabel_tv;
        TextView holder_cardview_noofdays_tv;
        TextView holder_cardview_fromdate_label_tv;
        TextView holder_cardview_fromdate_tv;
        TextView holder_cardview_todate_label_tv;
        TextView holder_cardview_todate_tv;
        TextView holder_cardview_status_label_tv;
        TextView holder_cardview_status_tv;
        TextView holder_cardview_approved_label_tv;
        TextView holder_cardview_approved_tv;
        TextView holder_cardview_typeofod_label_tv;
        TextView holder_cardview_typeofod_tv;
        TextView holder_cardview_reason_label_tv;
        TextView holder_cardview_reason_tv;








        public MyViewHolder_X(View v) {
            super(v);

            holder_cardview_locationlabel_tv = (TextView) v.findViewById(R.id.cardview_locationlabel_TV);
            holder_cardview_location_tv = (TextView) v.findViewById(R.id.cardview_location_TV);
            holder_cardview_dayslabel_tv = (TextView) v.findViewById(R.id.cardview_dayslabel_TV);
            holder_cardview_noofdays_tv = (TextView) v.findViewById(R.id.cardview_noofdays_TV);
            holder_cardview_fromdate_label_tv = (TextView) v.findViewById(R.id.cardview_fromdate_label_TV);
            holder_cardview_fromdate_tv = (TextView) v.findViewById(R.id.cardview_fromdate_TV);
            holder_cardview_todate_label_tv = (TextView) v.findViewById(R.id.cardview_todate_label_TV);
            holder_cardview_todate_tv = (TextView) v.findViewById(R.id.cardview_todate_TV);
            holder_cardview_status_label_tv = (TextView) v.findViewById(R.id.cardview_status_label_TV);
            holder_cardview_status_tv = (TextView) v.findViewById(R.id.cardview_status_TV);
            holder_cardview_approved_label_tv = (TextView) v.findViewById(R.id.cardview_approved_label_TV);
            holder_cardview_approved_tv = (TextView) v.findViewById(R.id.cardview_approved_TV);
            holder_cardview_typeofod_label_tv = (TextView) v.findViewById(R.id.cardview_typeofod_label_TV);
            holder_cardview_typeofod_tv = (TextView) v.findViewById(R.id.cardview_typeofod_TV);
            holder_cardview_reason_label_tv = (TextView) v.findViewById(R.id.cardview_reason_label_TV);
            holder_cardview_reason_tv = (TextView) v.findViewById(R.id.cardview_reason_TV);


        }

        }
    }



