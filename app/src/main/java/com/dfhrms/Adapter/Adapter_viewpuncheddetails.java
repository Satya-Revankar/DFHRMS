package com.dfhrms.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfhrms.Class.Class_attendancedetails;
import com.dfhrms.R;

public class Adapter_viewpuncheddetails extends BaseAdapter
{


   // Class_attendancedetails[] farmerpaymentdetails_arrayObj;
    //Class_farmerpaymentdetails farmerpaymentdetails_Obj;
    Class_attendancedetails[] attendancedetails_arrayObj;
    Class_attendancedetails attendancedetails_obj;
    private Context context;



    public Adapter_viewpuncheddetails(Context context, Class_attendancedetails[] attendancedetails ) {

        this.context = context;
        this.attendancedetails_arrayObj=attendancedetails;


    }





    @Override
    public int getCount() {
        return attendancedetails_arrayObj.length;
       // return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return attendancedetails_arrayObj[position];
    //    return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
        //return 0;
    }

    @Override
   // public View getView(int position, View convertView, ViewGroup parent)
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            //convertView1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_sofarmerdetails, parent, false);

            convertView = LayoutInflater.from(context).inflate(R.layout.listview_employeepunchedhistory, parent, false);

            //holder.jlgfarmerpaymenthistory_LL=(LinearLayout)convertView.findViewById(R.id.jlgfarmerpaymenthistory_LL);
            holder.slno_tv = (TextView) convertView.findViewById(R.id.slno_tv);
            holder.puncheddate_tv=(TextView) convertView.findViewById(R.id.puncheddate_tv);
            holder.latitude_tv = (TextView) convertView.findViewById(R.id.latitude_tv);
            holder.longitude_tv = (TextView) convertView.findViewById(R.id.longitude_tv);
            holder.punchedlocation_tv = (TextView) convertView.findViewById(R.id.punchedlocation_tv);


            Log.e("Inside If convertView1", "Inside If convertView1");

            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
            Log.d("else convertView", "else convertView1");
        }



        attendancedetails_obj = (Class_attendancedetails) getItem(position);

        if (attendancedetails_obj != null)
        {


            // holder.slno_tv.setText(so_fdetails_obj.getStr_slno());
            int int_pos=position+1;
            holder.slno_tv.setText(String.valueOf(int_pos));
            holder.puncheddate_tv.setText(attendancedetails_obj.getStr_punchedinDateTime());
            holder.latitude_tv.setText(attendancedetails_obj.getStr_latitude().toString());
            holder.longitude_tv.setText(attendancedetails_obj.getStr_longitude().toString());
            holder.punchedlocation_tv.setText(attendancedetails_obj.getStr_location().toString());


        }



        return convertView;
    }

    private class Holder {


       TextView slno_tv;
        TextView puncheddate_tv;
        TextView latitude_tv;
        TextView longitude_tv;
        TextView punchedlocation_tv;



    }
}
