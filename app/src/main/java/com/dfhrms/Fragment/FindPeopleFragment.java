package com.dfhrms.Fragment;


/*
import android.app.Fragment;
import android.app.FragmentManager;
*/
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dfhrms.Class.LeaveDetail;
import com.dfhrms.Fragment.CancelLeaveFragments1;
import com.dfhrms.Fragment.Leave_request;
import com.dfhrms.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class FindPeopleFragment extends Fragment
{
	Button submit,opening_balance,avil_leave,close_leave,Brought_Forword,cancelleave_bt1;
	int count1;
	 TableLayout tl;
	 LeaveDetail[] Leavelist;
	 String pwd,username,emp_id;

	LinearLayout LinearLayCust;
	//FloatingActionButton menu1;

	public FindPeopleFragment(){}

	int i=0;
	Boolean datestrike=false;
	CharSequence[] items;
	 int noOfobjects=0;
	int x;

	int numberofbuttons=0;

	String valueOfcheckedDate="";
	 Context context;
	 Resources resource;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);

		SharedPreferences myprefs= this.getActivity().getSharedPreferences("user", Context.MODE_WORLD_READABLE);
     	 //Toast.makeText(Instant.this,myprefs.getAll().toString(),Toast.LENGTH_LONG).show();
  	 username = myprefs.getString("user1", "nothing");
  	 pwd = myprefs.getString("pwd", "nothing");
  	 emp_id = myprefs.getString("emp_id", "nothing");

         
        submit = (Button) rootView.findViewById(R.id.leave_request);
        opening_balance = (Button) rootView.findViewById(R.id.opening_balance);
		Brought_Forword = (Button) rootView.findViewById(R.id.Brought_Forword);
        avil_leave = (Button) rootView.findViewById(R.id.avil_leave);
        close_leave = (Button) rootView.findViewById(R.id.close_leave);
		cancelleave_bt1=(Button)rootView.findViewById(R.id.CancelLeave_BT1);

        tl = (TableLayout) rootView.findViewById(R.id.table22);




        TextView tx = (TextView)rootView.findViewById(R.id.Detail);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHICB.TTF");
        tx.setTypeface(custom_font);	
         context = rootView.getContext();
         resource = context.getResources();
        AsyncCallWS2 task=new AsyncCallWS2(context);
		task.execute(); 
        
    /*    TableLayout.LayoutParams tableRowParams=
        		  new TableLayout.LayoutParams
        		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        		int leftMargin=10;
        		int topMargin=4;
        		int rightMargin=10;
        		int bottomMargin=4;

        		tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        for(int i=0;i<3; i++ )
        {
        TableRow newRow = new TableRow(context);
        newRow.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        newRow.setLayoutParams(tableRowParams);
        
        newRow.setPadding(0, 2, 0, 2);
       
        newRow.setBackgroundColor(resource.getColor(R.color.first_row_code));
       
       // newRow.setBackgroundColor(color)

        TextView column1 = new TextView(context);
        TextView column2 = new TextView(context);
        TextView column3 = new TextView(context);
        TextView  column4 = new TextView(context);

        column1.setTextColor(Color.WHITE);
        column2.setTextColor(Color.WHITE);
        column3.setTextColor(Color.WHITE);
        column4.setTextColor(Color.WHITE);
        column1.setSingleLine(false);
        column2.setSingleLine(false);
        column3.setSingleLine(false);
        column4.setSingleLine(false);

       
            column1.setText("From \n11 apr 2016");
            column1.setGravity(Gravity.CENTER);
            column2.setText("To \n11 apr 2016");
            column2.setGravity(Gravity.CENTER);
            column3.setText("Days \n23.5");
            column3.setGravity(Gravity.CENTER);
            column4.setText("Status \ncolumn 4");
            column4.setGravity(Gravity.CENTER);
       

       
        //Now add a row 
        newRow.addView(column1);
        newRow.addView(column2);
        newRow.addView(column3);
        newRow.addView(column4);
        //Add row to table
        tl.addView(newRow, tableRowParams);
        
        TableRow newRow2 = new TableRow(context);
        newRow2.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        newRow2.setLayoutParams(tableRowParams);
        newRow2.setBackgroundColor(resource.getColor(R.color.secend_row_code));
        TextView ReasonRow = new TextView(context);
       ReasonRow.setTextColor(Color.WHITE);
        ReasonRow.setText("Resason Row ddfgdfgfgfgfgfgdfgdfgdfgfgfdgdfgdfdfg \n");
        newRow2.addView(ReasonRow);
     //   View editText = null;
		TableRow.LayoutParams params = (TableRow.LayoutParams)ReasonRow.getLayoutParams();
        params.span = 4;
        ReasonRow.setLayoutParams(params);
        
        
        tl.addView(newRow2, tableRowParams);
        
        TableRow newRow3 = new TableRow(context);
        newRow3.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        newRow3.setLayoutParams(tableRowParams);
        newRow3.setBackgroundColor(resource.getColor(R.color.thired_row_code));
        TextView Approved_on = new TextView(context);
        Approved_on.setTextColor(Color.WHITE);
        Approved_on.setText("Approved_on");
        newRow3.addView(Approved_on);
        
        TableRow.LayoutParams params1 = (TableRow.LayoutParams)Approved_on.getLayoutParams();
        params1.span = 4;
        Approved_on.setLayoutParams(params1); 
        
        tl.addView(newRow3, tableRowParams);
        
        TableRow newRow4 = new TableRow(context);
        newRow4.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
       
        TextView gap = new TextView(context);
        gap.setBackgroundColor(resource.getColor(R.color.gapcolor));
        
        newRow4.addView(gap);
        
        TableRow.LayoutParams params2 = (TableRow.LayoutParams)gap.getLayoutParams();
        params2.span = 4;
        gap.setLayoutParams(params2); 
        
        tl.addView(newRow4, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        

        }*/

        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
		//	 Intent i  = new Intent (context ,Practice.class);
			//		startActivity(i);
				Leave_request fragment = new Leave_request();


				FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();

				/*FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();
*/

				/*.replace(R.id.frame_container, fragment).addToBackStack( "tag" ).commit();*/

				
				
			}
		});



		cancelleave_bt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Toast.makeText(getActivity(),"hi",Toast.LENGTH_LONG).show();
                CancelLeaveFragments1 fragment = new CancelLeaveFragments1();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();

    //.replace(R.id.frame_container, fragment).addToBackStack( "tag" ).commit();
				//content_frame
			}
		});


       
	/*	rootView.setOnKeyListener( new OnKeyListener()
{
    @Override
    public boolean onKey( View v, int keyCode, KeyEvent event )
    {
       if( keyCode == KeyEvent.KEYCODE_BACK )
       {
            return true;
        }
        return false;
    	if (event.getAction() == KeyEvent.ACTION_DOWN) {
    	    if (keyCode == KeyEvent.KEYCODE_BACK) {

    	        return true;   
    	    }   
    	
    //	return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    	}
    	return false;  
    }
} 
);*/





        return rootView;
    }//end of oncreate
	
	
	private class AsyncCallWS2 extends AsyncTask<String, Void, Void>
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
			Log.i("DFTech", "doInBackground");
	
			
		//	GetAllEvents();
			leave_detaile();  // call of details
			
			
			
		return null;
		}

		 public AsyncCallWS2(Context context1) {
	  	    	context =  context1;
	  	        dialog = new ProgressDialog(context1);
		 }

		@Override
		protected void onPostExecute(Void result)
		
		{
			Boolean returnValueOfdateformat=false;
			Boolean returnValueOfcompareEndDate2CurrentDate=false;
			int numberofbt=0;

			  if ((this.dialog != null) && this.dialog.isShowing()) { {
	  	            dialog.dismiss();
	  	         
	  	        }  
			
			TableLayout.LayoutParams tableRowParams=
	        		  new TableLayout.LayoutParams
	        		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

	        		int leftMargin=10;
	        		int topMargin=4;
	        		int rightMargin=10;
	        		int bottomMargin=4;

	        		tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);


				  for(int countobject=0; countobject<noOfobjects;countobject++)
				  {
					  String	formateTodate="";// in format 05 Mar 2017
					  formateTodate = Leavelist[countobject].getTo_DateC();
					  formateTodate=formateTodate.substring(5).trim();

					  System.out.println("formatedToDate"+formateTodate);

					  String formateTodate_changed=Dateformatechange(formateTodate);

					  System.out.println("formateTodate_changed"+formateTodate_changed);

					  returnValueOfcompareEndDate2CurrentDate=compareEndDate2CurrentDate(formateTodate_changed);

					  if(returnValueOfcompareEndDate2CurrentDate)
					  {
						  numberofbt++;
						  System.out.println("numberofbt"+numberofbt);
					  }
				  }

				  final int N = numberofbt;

				   //TextView[] myTextViews = new TextView[N];
				  for (int i = 0; i < N; i++) {
					  // create a new textview
					  //final TextView rowTextView = new TextView(getActivity());
					  //myTextViews[i] = rowTextView;
				  }








	        for(int i=0;i<count1; i++ )
	        {
				x=i;
	        final TableRow newRow = new TableRow(context);

	        newRow.setLayoutParams(new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT));
	        newRow.setLayoutParams(tableRowParams);
	        
	        newRow.setPadding(0, 2, 0, 2);
	       
	        newRow.setBackgroundColor(resource.getColor(R.color.first_row_code));

				System.out.println("getY():"+newRow.getY());

	       // newRow.setBackgroundColor(color)

	        final TextView column1 = new TextView(context);
	        TextView column2 = new TextView(context);
	        TextView column3 = new TextView(context);
	        TextView  column4 = new TextView(context);
			//TextView column5 =new TextView(context);
				TextView[] myTextViews = new TextView[count1];
				TextView rowTextView = new TextView(context);
				myTextViews[i] = rowTextView;


	        column1.setTextColor(Color.WHITE);
	        column2.setTextColor(Color.WHITE);
	        column3.setTextColor(Color.WHITE);
	        column4.setTextColor(Color.WHITE);

			//column5.setTextColor(Color.RED);
				myTextViews[i].setTextColor(Color.RED);

	        column1.setSingleLine(false);
	        column2.setSingleLine(false);
	        column3.setSingleLine(false);
	        column4.setSingleLine(false);
			//column5.setSingleLine(false);

				myTextViews[i].setSingleLine(false);


	       
	            column1.setText("From \n"+Leavelist[i].getFrom_Date());
	            column1.setGravity(Gravity.CENTER);
	            column2.setText("To \n"+Leavelist[i].getTo_Date());
	            column2.setGravity(Gravity.CENTER);
	            column3.setText("Days \n"+Leavelist[i].getno_days());
	            column3.setGravity(Gravity.CENTER);
	            column4.setText("Status \n"+Leavelist[i].getLEave_Status());
	            column4.setGravity(Gravity.CENTER);

				/*myTextViews[i].setText("Cancel \n");
				myTextViews[i].setGravity(Gravity.CENTER);*/

				//column5.setText("Cancel \n");
				//column5.setGravity(Gravity.CENTER);

				//Log.v("from date: ",Leavelist[i].getFrom_DateC());

				String From_DateCString=Leavelist[i].getFrom_DateC();
				//Log.v("from date:_",subx.substring(5));
				System.out.println("fromDate:"+From_DateCString.substring(5).trim());
				From_DateCString=From_DateCString.substring(5).trim();

				String To_DateCString=Leavelist[i].getTo_DateC();
				       To_DateCString=To_DateCString.substring(5).trim();
				//Log.v("To date:_",To_DateCString.substring(5).trim());
				System.out.println("To Date:"+To_DateCString);




				//dateformat(From_DateCString,To_DateCString);



				for(int countobject=0; countobject<noOfobjects;countobject++)
				{
				String	formateTodate="";// in format 05 Mar 2017
					formateTodate = Leavelist[countobject].getTo_DateC();
					formateTodate=formateTodate.substring(5).trim();

					System.out.println("formatedToDate"+formateTodate);

					String formateTodate_changed=Dateformatechange(formateTodate);

					System.out.println("formateTodate_changed"+formateTodate_changed);

					//compareEndDate2CurrentDate(formateTodate_changed);
					if(compareEndDate2CurrentDate(formateTodate_changed))
					{
						numberofbuttons++;
						System.out.println("numberofbuttons"+numberofbuttons);
					}
				}


				String Noofday_String=Leavelist[i].getno_days().toString().trim();

				//column5.setText("Cancel \n");
				//column5.setGravity(Gravity.CENTER);


// comment here

			if(Noofday_String.equals("0.5")){

					/*column5.setText("Cancel \n");
					column5.setGravity(Gravity.CENTER);*/

				}
				else{
					returnValueOfdateformat=dateformat(From_DateCString,To_DateCString,Noofday_String);

					if(returnValueOfdateformat)
					{
						String Dateformatechange2cancel=Dateformatechange(From_DateCString);


						int Noofday_Int=0;
						Noofday_Int=Integer.parseInt(Noofday_String);




					if(Noofday_Int>1) {
						items = new CharSequence[Noofday_Int];
						for(int k=0;k<Noofday_Int;k++)
					{
						String ChangedFromDate_WithDays = "";
						ChangedFromDate_WithDays = Dateformatechange2cancel;  // ChangedFromDate 22/05/2017
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Calendar calendarObj = null;
						calendarObj = Calendar.getInstance();
						try {
							calendarObj.setTime(sdf.parse(ChangedFromDate_WithDays));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						calendarObj.add(Calendar.DATE, k);

						Date resultdate = new Date(calendarObj.getTimeInMillis());
						String singledayfordisplay = sdf.format(resultdate);
						items[k]=singledayfordisplay;
					}// end of for loop
					}//end of if statement

						//
						/*column5.setText("Cancel \n");
						column5.setGravity(Gravity.CENTER);*/

						//myTextViews[i].setText("Cancel \n");
						//myTextViews[i].setGravity(Gravity.CENTER);

						final ArrayList seletedItems=new ArrayList();



						myTextViews[i].setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v) {
								//Toast.makeText(getActivity(), "Your applied Leave has been Cancelled,Thank You", Toast.LENGTH_LONG).show();

								TextView items= (TextView) newRow.getChildAt(0);
								String myText = items.getText().toString();
								System.out.println("myText:"+myText);
								Toast.makeText(getActivity(), Leavelist[x].getFrom_DateC().toString(), Toast.LENGTH_LONG).show();

							/*	Dialog dialog = new Dialog(getActivity());
								AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
								builder.setTitle("Leave Cancellation");

								String y=Leavelist[x].getFrom_DateC();
								builder.setMultiChoiceItems(items, null,
										new DialogInterface.OnMultiChoiceClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int indexSelected,
																boolean isChecked) {
												if (isChecked) {
													// If the user checked the item, add it to the selected items

													//seletedItems.add(indexSelected);
													//seletedItems.add(items[indexSelected]);
													seletedItems.add(Leavelist[x].getFrom_DateC());


													System.out.println("out side"+Leavelist[x].getFrom_DateC());
												} else if (seletedItems.contains(indexSelected)) {
													// Else, if the item is already in the array, remove it
													seletedItems.remove(Integer.valueOf(indexSelected));
												}

											}
										})
										.setPositiveButton("OK", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int id) {

												System.out.println("arraylist: "+seletedItems.size());
												System.out.println("arraylist: "+seletedItems.get(0).toString());
												System.out.println("arraylist: "+seletedItems.get(1).toString());
												//System.out.println("arraylist: "+seletedItems.get(2).toString());
												//System.out.println("arraylist: "+seletedItems.get(3).toString());

												//  Your code when user clicked on OK
												//  You can write the code  to save the selected item here

											}
										});

								dialog = builder.create();//AlertDialog dialog; create like this outside onClick
								dialog.show();*/

							}

								});// end of


					}
			}//end of else

	//comment here
	       
	        //Now add a row 
	        newRow.addView(column1);
	        newRow.addView(column2);
	        newRow.addView(column3);
	        newRow.addView(column4);
			//newRow.addView(column5);
				newRow.addView(myTextViews[i]);

	        //Add row to table
	        tl.addView(newRow, tableRowParams);
	        
	        TableRow newRow2 = new TableRow(context);
	        
	        newRow2.setLayoutParams(new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT));
	        newRow2.setLayoutParams(tableRowParams);
	        newRow2.setBackgroundColor(resource.getColor(R.color.secend_row_code));
	        TextView ReasonRow = new TextView(context);
	        ReasonRow.setPadding(22, 1, 10, 1);
	        
	       ReasonRow.setTextColor(Color.WHITE);
	        ReasonRow.setText("Reason: \n"+Leavelist[i].getReason());
	        newRow2.addView(ReasonRow);
	     //   View editText = null;
			TableRow.LayoutParams params = (TableRow.LayoutParams)ReasonRow.getLayoutParams();
	        params.span = 4;
	        ReasonRow.setLayoutParams(params);
	        
	        
	        tl.addView(newRow2, tableRowParams);
	        
	        TableRow newRow3 = new TableRow(context);
	        newRow3.setLayoutParams(new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT));
	        newRow3.setLayoutParams(tableRowParams);
	        newRow3.setBackgroundColor(resource.getColor(R.color.thired_row_code));
	        TextView Approved_on = new TextView(context);
	        Approved_on.setPadding(22, 1, 10, 1);
	        Approved_on.setTextColor(Color.WHITE);
	        Approved_on.setText("Approved_on:  "+Leavelist[i].getApproved_On());
	        newRow3.addView(Approved_on);
	        
	        TableRow.LayoutParams params1 = (TableRow.LayoutParams)Approved_on.getLayoutParams();
	        params1.span = 4;
	        Approved_on.setLayoutParams(params1); 
	        
	        tl.addView(newRow3, tableRowParams);
	        
	        TableRow newRow4 = new TableRow(context);
	        newRow4.setLayoutParams(new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT));
	       
	        TextView gap = new TextView(context);
	        gap.setBackgroundColor(resource.getColor(R.color.gapcolor));
	        final LayoutParams layoutparams =  gap.getLayoutParams();
	      //  layoutparams.width = 4;
	   //     layoutparams.height = ;
	        
	      //  gap.setLayoutParams(layoutparams);
	        
	        newRow4.addView(gap);
	        
	        TableRow.LayoutParams params2 = (TableRow.LayoutParams)gap.getLayoutParams();
	        params2.span = 4;
	        gap.setLayoutParams(params2); 
	        gap.setTextSize(3);
	        gap.setTextColor(000000);
	        gap.setText("hi");
	        
	        tl.addView(newRow4, new TableLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT));


				/*column5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getActivity(), "Your applied Leave has been Cancelled,Thank You", Toast.LENGTH_LONG).show();
					}
				});*/




	        }// End of for loop

	        if(count1>0)
	        {
	        opening_balance.setText(Leavelist[0].getOpening_Balance()+"  ");
				Brought_Forword.setText(Leavelist[0].getBrought_Forword()+"  ");
	        avil_leave.setText(Leavelist[0].getAvailedLeaves()+"  ");
	        close_leave.setText((Double.parseDouble(Leavelist[0].getBrought_Forword())+Double.parseDouble(Leavelist[0].getOpening_Balance())-Double.parseDouble(Leavelist[0].getAvailedLeaves())+"  "));
	        }
		}
	}//On onPostExecute
	
	
	public void leave_detaile()
	{
		Vector<SoapObject> result1 = null;
		 String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
		// String METHOD_NAME = "intCount";//"NewAppReleseDetails";
		// String Namespace="http://www.example.com", SOAPACTION="http://www.example.com/intCount";
		// String URL = "http://192.168.1.196:8080/deterp_ws/server4.php?wsdl";//"Login.asmx?WSDL";
		 String METHOD_NAME = "GetEmployeeDetails";//"NewAppReleseDetails";
		 String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/GetEmployeeDetails";
		try{
			// String  versioncode = this.getPackageManager()
	   		//		    .getPackageInfo(this.getPackageName(), 0).versionName;
		SoapObject request = new SoapObject(Namespace, METHOD_NAME);
	 	request.addProperty("email", username);
	  	
	  //	request.addProperty("to", 9);
	  	
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
			SoapObject  response = (SoapObject ) envelope.getResponse();

			 Log.i("value at response",response.toString());
			   count1 = response.getPropertyCount();  // number of count in array in response 6,0-5,5

			  Log.i("number of rows",""+count1);
			  
				noOfobjects=count1;







			System.out.println("Number of object"+noOfobjects);
				// Leavelist[0].setName("kya");
				Leavelist= new LeaveDetail[count1];
				
			 for(int i=0;i<count1;i++)
			 {
				 SoapObject list = (SoapObject ) response.getProperty(i);

				 SoapPrimitive no_of_Days,Leave_Id, Name ,  email, Program,  Employee_Id, Opening_Balance, AvailedLeaves,  manager_email, From_Date, To_Date, Reason, LEave_Status, Approved_On,casualleave,sickleave,CLcarryforward,SLcarryforward;
				 String approved ="";
				  Leave_Id= (SoapPrimitive)list.getProperty("Leave_Id");
				 Name= (SoapPrimitive)list.getProperty("Name");
				 email= (SoapPrimitive)list.getProperty("email");
				 Program= (SoapPrimitive)list.getProperty("Program");
				 Employee_Id= (SoapPrimitive)list.getProperty("Employee_Id");


				 casualleave=(SoapPrimitive) list.getProperty("Casual_Leave");
				 sickleave =(SoapPrimitive)list.getProperty("Sick_Leave");

				// Opening_Balance= (SoapPrimitive)list.getProperty("Opening_Balance");

				 AvailedLeaves= (SoapPrimitive)list.getProperty("AvailedLeaves");
				// manager_email= (SoapPrimitive)list.getProperty("manager_email");
				 From_Date= (SoapPrimitive)list.getProperty("From_Date");
				 To_Date= (SoapPrimitive)list.getProperty("To_Date");
				 Reason= (SoapPrimitive)list.getProperty("Reason");
				 no_of_Days = (SoapPrimitive)list.getProperty("No_of_Days");
				 LEave_Status= (SoapPrimitive)list.getProperty("LEave_Status");
				 if (list.getProperty("Approved_On").toString().equals("anyType{}")){
					// Approved_On.add("");
					// Approved_On = (SoapPrimitive);
				 }
				 else {
					 approved = list.getProperty("Approved_On").toString();
				 }
				 LeaveDetail project = new LeaveDetail();
				// Leavelist[i].setLeave_Id(Leave_Id.toString());
				 Log.i("value at name premitive",Name.toString());

				 float CL= Float.parseFloat(casualleave.toString());
				 float SL =Float.parseFloat(sickleave.toString());
				 float balance=CL+SL;

				 project.setName(Name.toString());
				 project.setemail(email.toString());
				 project.setProgram(Program.toString());
				 project.setEmployee_Id(Employee_Id.toString());
				 project.setOpening_Balance(Float.toString(balance));
				 project.setAvailedLeaves(AvailedLeaves.toString());
				 project.setno_days(no_of_Days.toString());
			//	 Leavelist[i].setmanager_email(manager_email.toString());
				 project.setFrom_Date(From_Date.toString().substring(4,From_Date.toString().length()-5));
				// Log.i("string value at messege",From_Date.toString().substring(3));
				 project.setTo_Date(To_Date.toString().substring(4,To_Date.toString().length()-5));
				 project.setReason(Reason.toString());
				 project.setLEave_Status(LEave_Status.toString());
				 project.setApproved_On(approved);
				 project.setBrought_Forword(list.getProperty("carry_forward").toString());

				 project.setFrom_DateC(From_Date.toString());
				 project.setTo_DateC(To_Date.toString());
			//	 Leavelist[i].setApproved_On(Approved_On.toString());
				 Leavelist[i]=project;
				 
			 }// End of for loop
			
			
			
		//	 version = (SoapPrimitive)response.getProperty("AppVersion");
			// release_not = (SoapPrimitive)response.getProperty("ReleseNote");
			 
			  
			   
			//Log.i("string value at messeg",messeg.toString()); 
			
			
			 				
				
		    
			
		          
			  
		  
		}
		catch (Throwable t) {
     		//Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
     		//		Toast.LENGTH_LONG).show();
			 Log.e("request fail", "> " + t.getMessage());
     	}
		}catch (Throwable t) {
			 Log.e("UnRegister  Error", "> " + t.getMessage());
			 
     	}
	
	}//End of leaveDetail method



		public boolean dateformat(String fromdateC,String todateC,String Noofday_String )
		{

			String ChangedFromDate="";
            ChangedFromDate=Dateformatechange(fromdateC);
			String ChangedToDate="";
            ChangedToDate=Dateformatechange(todateC);

			int Noofday_Int=0;
            Noofday_Int=Integer.parseInt(Noofday_String);

			String ChangedFromDate_WithDays ="";
                    ChangedFromDate_WithDays=ChangedFromDate;  // ChangedFromDate 22/05/2017
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar calendarObj =null;
            calendarObj = Calendar.getInstance();
			try {
				calendarObj.setTime(sdf.parse(ChangedFromDate_WithDays));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(Noofday_Int==1){//no addition of days for 1day coz sameday
				}
			else{
			calendarObj.add(Calendar.DATE, Noofday_Int);
			}

			calendarObj.add(Calendar.DATE, 4);
			//sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date resultdate = new Date(calendarObj.getTimeInMillis());
			ChangedFromDate_WithDays = sdf.format(resultdate);
			System.out.println("ChangedFromDate_WithDays:"+ChangedFromDate_WithDays);

			String pattern = "dd/MM/yyyy";
			String currentDateInformat="";
			currentDateInformat	=sdf.format(new Date());
			System.out.println("currentDateInformat1:"+currentDateInformat);

			//Comparison of ChangedFromDate_WithDays to the currentDateInformat1
			/*//if (ChangedFromDate_WithDays.compareTo(currentDateInformat) >= 0) {
			if (currentDateInformat.compareTo(ChangedFromDate_WithDays) >= 0) {
				System.out.println("ChangedFromDate_WithDays is greater than/equal to currentDateInformat"+ChangedFromDate_WithDays +"###"+currentDateInformat);

			}
			else{
				System.out.println("ChangedFromDate_WithDays is less than to currentDateInformat"+ChangedFromDate_WithDays +"###"+currentDateInformat);
			}*/

			//Comparison of ChangedFromDate_WithDays to the currentDateInformat1
			Date date4=null;
			Date date5=null;

			SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yyyy");
			try {
				date4 = sdformat1.parse(ChangedFromDate_WithDays);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				date5 = sdformat1.parse(currentDateInformat);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (date4.compareTo(date5) >= 0) {
				//System.out.println("date4 is greater than/equal to date5"+date4 +"###"+date5);
					return true;
			}
			else{
				//System.out.println("Date4 is less than to Date5"+date4+"###"+date5);\
				return false;
			}




		}//end off dateformat


		public String Dateformatechange(String receviedDate)
		{
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
			System.out.println("output" + outputDate);

			return outputDate;
		}// end of DateformatChange

		public Boolean compareEndDate2CurrentDate(String receviedTodate)
		{

           String receviedTodateWithAdd="";
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendarObj =null;
            calendarObj = Calendar.getInstance();
            try {
                calendarObj.setTime(sdf1.parse(receviedTodate));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            calendarObj.add(Calendar.DATE, 4);
            //sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date resultdate = new Date(calendarObj.getTimeInMillis());
            receviedTodateWithAdd = sdf1.format(resultdate);
            System.out.println("receviedTodateWithAdd:"+receviedTodateWithAdd);




			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String pattern = "dd/MM/yyyy";
			String currentDate2compareTodate="";
			currentDate2compareTodate	=sdf.format(new Date());
			System.out.println("currentDateInformat:"+currentDate2compareTodate);

			Date date4=null;
			Date date5=null;

			SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yyyy");
			try {
				date4 = sdformat1.parse(receviedTodateWithAdd);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				date5 = sdformat1.parse(currentDate2compareTodate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (date4.compareTo(date5) >= 0) {
				//System.out.println("date4 is greater than/equal to date5"+date4 +"###"+date5);
				return true;

			}
			else{
				//System.out.println("Date4 is less than to Date5"+date4+"###"+date5);\
				return false;
			}



		}// End of comapareEndDate2currentDate

	}
	
}
	
	
	   
	

