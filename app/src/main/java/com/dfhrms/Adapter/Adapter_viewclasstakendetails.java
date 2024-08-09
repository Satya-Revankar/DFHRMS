package com.dfhrms.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfhrms.Class.Class_attendanceclassTaken;
import com.dfhrms.R;

public class Adapter_viewclasstakendetails extends BaseAdapter
{


   // Class_attendancedetails[] farmerpaymentdetails_arrayObj;
    //Class_farmerpaymentdetails farmerpaymentdetails_Obj;
    Class_attendanceclassTaken[] attendanceclasstaken_arrayObj;
    Class_attendanceclassTaken attendanceclasstaken_obj;
    private Context context;



    public Adapter_viewclasstakendetails(Context context, Class_attendanceclassTaken[] attendanceclasstaken ) {

        this.context = context;
        this.attendanceclasstaken_arrayObj=attendanceclasstaken;


    }





    @Override
    public int getCount() {
        return attendanceclasstaken_arrayObj.length;
       // return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return attendanceclasstaken_arrayObj[position];
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

            convertView = LayoutInflater.from(context).inflate(R.layout.listview_classpunchedhistory, parent, false);

            // classstartedon_tv
            // classcompletedon_tv
            // holder.classstartedon_tv holder.classcompletedon_tv
            //holder.jlgfarmerpaymenthistory_LL=(LinearLayout)convertView.findViewById(R.id.jlgfarmerpaymenthistory_LL);
            holder.slno_tv = (TextView) convertView.findViewById(R.id.slno_tv);
            holder.classstartedon_tv=(TextView) convertView.findViewById(R.id.classstartedon_tv);
            holder.classcompletedon_tv = (TextView) convertView.findViewById(R.id.classcompletedon_tv);

            Log.e("Inside If convertView1", "Inside If convertView1");

            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
            Log.d("else convertView", "else convertView1");
        }



        attendanceclasstaken_obj = (Class_attendanceclassTaken) getItem(position);

        if (attendanceclasstaken_obj != null)
        {


            // holder.slno_tv.setText(so_fdetails_obj.getStr_slno());
            int int_pos=position+1;
            holder.slno_tv.setText(String.valueOf(int_pos));
            holder.classstartedon_tv.setText(attendanceclasstaken_obj.getStr_classtakenon());
            holder.classcompletedon_tv.setText(attendanceclasstaken_obj.getStr_classcompletedon());
        }



        return convertView;
    }

    private class Holder {


       TextView slno_tv;
       TextView classstartedon_tv;
       TextView classcompletedon_tv;
    }
}
