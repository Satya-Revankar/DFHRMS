package com.dfhrms.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dfhrms.Class.Class_URL;
import com.dfhrms.Interfaces.CohSub_SpinnerInterface;
import com.dfhrms.SpinnerModels.CohSub_SM;
import com.dfhrms.Utili.ConnectionDetector;
import com.dfhrms.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Fragment_ClassFeedback extends Fragment {

    private LinearLayout lLayout_ClassFeedback;
    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;
    private TextView dateEdt_tv;
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    AppCompatSpinner college_name_Spinner, cohort_spinner;
    String datevalue_str, selectedLatitude, selectedLongitude, selectedRadius, college_selected_str,
            cohort_selected_str, emp_id;
    ArrayList<HashMap<String, String>> Summarylist = new ArrayList<HashMap<String, String>>();
    List<LocationData> locations_list;
    private static final String SOAP_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_METHOD_NAME = "GetEmployeeLocations";
    private static final String SOAP_ACTION = "http://tempuri.org/GetEmployeeLocations";
    private static final String SOAP_URL = "https://dfhrms.dfindia.org/PMSservice.asmx?WSDL";
    ProgressDialog progressDialog;
    private Toast currentToast;
    private Handler toastHandler = new Handler();
    List<String> location_list;
    Context context;
    private ArrayList<String> cohort_ArrayList = new ArrayList<>();
    private ArrayList<CohSub_SM> cohort_SM = new ArrayList<>();
    ListView cohort_Classes_lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lLayout_ClassFeedback = (LinearLayout) inflater.inflate(R.layout.fragment_classfeedback, container, false);
        context = lLayout_ClassFeedback.getContext();
        internetDectector = new ConnectionDetector(getActivity());
        isInternetPresent = internetDectector.isConnectingToInternet();
        if (isInternetPresent) {
            getDataFromSoapAPI();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setPositiveButton("OK", null)
                    .show();
        }

        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", MODE_PRIVATE);
        emp_id = myprefs.getString("emp_id", "nothing");

        college_name_Spinner = lLayout_ClassFeedback.findViewById(R.id.college_name_spinner);
        cohort_spinner = lLayout_ClassFeedback.findViewById(R.id.cohort_Spinner);
        dateEdt_tv = lLayout_ClassFeedback.findViewById(R.id.clickfromdate_TV);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        System.out.println("todayDate"+todayDate);
        String thisDate = currentDate.format(todayDate);
        dateEdt_tv.setText(thisDate);

        try {
            Date parsedDate = inputDateFormat.parse(thisDate);
            datevalue_str = outputDateFormat.format(parsedDate);

            System.out.println("Input date: " + thisDate);
            System.out.println("Output date: " + datevalue_str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateEdt_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below linle we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();
                System.out.println("Today's Date: "+ c);
                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                System.out.println(month);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                Date date = new Date(year, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-"+year);
                                String cdate = formatter.format(date);
                                dateEdt_tv.setText(cdate);
                                String formattedDate = dateEdt_tv.getText().toString();
                                try {
                                    // Parse the formatted date to obtain a Date object
                                    Date parsedDate = formatter.parse(formattedDate);

                                    SimpleDateFormat newFormat = new SimpleDateFormat(year+"-MM-dd", Locale.ENGLISH);
                                    String cdateNewFormat = newFormat.format(parsedDate);

                                    System.out.println("Original formatted date: " + formattedDate);
                                    System.out.println("Converted date to yyyy-mm-dd format: " + cdateNewFormat);

                                    // Store the converted date value
                                    datevalue_str = cdateNewFormat;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //dateEdt_tv.setText(dayOfMonth + "-" + mon + "-" + year);

                            }
                        },

                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        college_name_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // "Select" is selected, do nothing or show a message
                    return;
                }

                // Get the selected location data
                LocationData selectedLocation = locations_list.get(position - 1); // Subtract 1 to account for "Select" at position 0

                Log.e("selectedLocation", String.valueOf(selectedLocation));
                // Extract latitude, longitude, and radius from the selected location
                selectedLatitude = selectedLocation.getLatitude();
                selectedLongitude = selectedLocation.getLongitude();
                selectedRadius = selectedLocation.getRadius();

                // Log the selected location information
                Log.d("SelectedLatitude", selectedLatitude);
                Log.d("SelectedLongitude", selectedLongitude);
                Log.d("SelectedRadius", selectedRadius);
                college_selected_str = (String) parentView.getItemAtPosition(position);
                //college_selected_str = "JAGTIAL - NALANDA DEGREE COLLEGE";


                Log.e("selectedcollege",college_selected_str);
                System.out.println("check college selected"+college_selected_str);

                //college_selected_str=college_selected_str.replaceAll("&","%26");

                Log.e("parsedcollege",college_selected_str);

                            /*try {
                                college_selected_str = URLEncoder.encode(college_selected_str, "UTF-8").replace("+", "%20");
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }*/

                // Use these values as needed in your code

                // Reset spinner selections and related variables

                ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Getting Location", true);
                dialog.show();

                if (!college_selected_str.equals("Select")) {
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();
                    if (isInternetPresent) {
                        fetchCohSub(college_selected_str);
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                }
                         /*   AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                            task1.execute();*/

                //getDCheckSubjectWiseClassAttendance();

                dialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do something if nothing is selected
            }
        });

        return lLayout_ClassFeedback;
    }//End of onCreateView

    private static class LocationData {
        private String latitude;
        private String longitude;
        private String radius;
        private String locationName;

        public LocationData(String latitude, String longitude, String radius, String locationName) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.radius = radius;
            this.locationName = locationName;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getRadius() {
            return radius;
        }

        public String getLocationName() {
            return locationName;
        }
    }

    private void getDataFromSoapAPI() {
        new AsyncTask<Void, Void, List<LocationData>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog("Fetching Locations");
                    }
                });
            }

            @Override
            protected List<LocationData> doInBackground(Void... voids) {
                try {
                    SoapObject request = new SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME);
                    request.addProperty("EmployeeId", emp_id);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE transport = new HttpTransportSE(SOAP_URL);
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject response = (SoapObject) envelope.getResponse();
                    Log.e("Response", response.toString());
                    SoapObject userLatLong = (SoapObject) response.getProperty("UserLatLong");
                    List<LocationData> locations = new ArrayList<>();
                    location_list = new ArrayList<>();
                    location_list.add("Select");
                    for (int i = 0; i < userLatLong.getPropertyCount(); i++) {
                        SoapObject location = (SoapObject) userLatLong.getProperty(i);
                        String latitude = location.getProperty("Latitude").toString();
                        String longitude = location.getProperty("Longitude").toString();
                        String radius = location.getProperty("Radius").toString();
                        String locationName = location.getProperty("Location").toString();
                        LocationData locationData = new LocationData(latitude, longitude, radius, locationName);
                        locations.add(locationData);
                        location_list.add(locationName);
                        System.out.println("location_list : " + location_list);
                        Log.e("location_list", location_list.toString());
                        Log.e("Radius", radius);
                    }
                    return locations;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(List<LocationData> result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                });
                locations_list = result;
                if (locations_list != null) {
                    ArrayAdapter<String> adapter_custom = new ArrayAdapter<>(getActivity(), R.layout.spinner_text, location_list);
                    adapter_custom.setDropDownViewResource(R.layout.spinner_dropdown);
                    college_name_Spinner.setAdapter(adapter_custom);
                    /*ArrayAdapter<String> subtopic_adapter = new ArrayAdapter<>(getActivity(), simple_spinner_item, location_list);
                    subtopic_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    college_name_Spinner.setAdapter(subtopic_adapter);*/
                } else {
                    showToast("Failed to fetch data");
                }
            }
        }.execute();
    }


    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(context); // Replace 'context' with your activity or fragment context
        progressDialog.setMessage(message); // Set your message
        progressDialog.setCancelable(false); // Set if dialog can be canceled by user
        progressDialog.show();
    }

    // Dismiss Progress Dialog method
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showToast(String message) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        currentToast.show();

        // Schedule clearing the toast after a certain duration (e.g., 2 seconds)
        toastHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentToast != null) {
                    currentToast.cancel();
                }
            }
        }, 3000); // Change the delay as needed (e.g., 2000 milliseconds = 2 seconds)
    }

    private void fetchCohSub(String college_selected) {
        Log.e("API college", college_selected);
        showProgressDialog("Fetching values");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CohSub_SpinnerInterface.CohSub_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CohSub_SpinnerInterface api = retrofit.create(CohSub_SpinnerInterface.class);
        Call<String> call = api.getJSONString(college_selected,emp_id);
        Log.e("call",call.request().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonresponse = response.body();
                        spinCohSub(jsonresponse);
                        System.out.println("jsonresponse"+ jsonresponse);
                        //spinJSON(jsonresponse);
                        // Parse and handle the JSON response as needed
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }else {
                    Log.i("onEmptyResponse", "Returned empty response1");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dismissProgressDialog();
                Log.e("API Call Failure", t.getMessage());
                alerts_dialog_Error(t.getMessage());
            }
        });
    }

    private void spinCohSub(String response){

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                cohort_ArrayList.clear();
                JSONObject cohortsArray1 = obj.getJSONObject("vmCohortSubject");//getJSONArray("cohorts");
                if (cohortsArray1.getString("cohorts").equals("null")) {
                    // If cohortsArray is null or empty, set the spinner value to "No cohorts assigned"
                    List<String> list = new ArrayList<>();
                    list.add("No cohorts assigned");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                    cohort_spinner.setAdapter(adapter);
                } else {
                    JSONArray cohortsArray = cohortsArray1.getJSONArray("cohorts");
                    cohort_ArrayList.add("Select");
                    cohort_SM.clear();

                    for (int i = 0; i < cohortsArray.length(); i++) {

                        CohSub_SM spinnerModel = new CohSub_SM();
                        JSONObject dataobj = cohortsArray.getJSONObject(i);

                        spinnerModel.setCohort(dataobj.getString("cohortName"));
                        cohort_SM.add(spinnerModel);

                    }
                    for (int i = 0; i < cohort_SM.size(); i++){
                        cohort_ArrayList.add(cohort_SM.get(i).getCohort().toString());
                    }
                }
                ArrayAdapter<String> cohort_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, cohort_ArrayList);
                cohort_adapter.setDropDownViewResource(R.layout.spinner_dropdown); // The drop down view
                cohort_spinner.setAdapter(cohort_adapter);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void alerts_dialog_Error(String str_error) {

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("DFHRMS");
        dialog.setMessage("Error: You are using UAT Version"
                + "\n" + "Kindly install the Live apk from below link");

        final TextView linkTextView = new TextView(getActivity());
        linkTextView.setPadding(50, 50, 50, 50);  // Optional: add padding for better readability
        linkTextView.setTextSize(16);  // Optional: set text size for better readability

        SpannableString spannableString = new SpannableString("https://appstore.dfindia.org/Appstore/App/28");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://appstore.dfindia.org/Appstore/App/28";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), 0); // "here" is clickable
        linkTextView.setText(spannableString);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        dialog.setView(linkTextView);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Fragment fragment = null;
                fragment = new Fragment_MarkAttndnceClassTakenNew();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack( "tag" ).commit();

            }
        });


        final android.app.AlertDialog alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#004D40"));
            }
        });
        alert.show();


    }

    private boolean isDateBeforeCurrentDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date inputDate = dateFormat.parse(dateString);

            // Set the time part of inputDate to midnight (00:00:00)
            Calendar inputCalendar = Calendar.getInstance();
            inputCalendar.setTime(inputDate);
            inputCalendar.set(Calendar.HOUR_OF_DAY, 0);
            inputCalendar.set(Calendar.MINUTE, 0);
            inputCalendar.set(Calendar.SECOND, 0);
            inputCalendar.set(Calendar.MILLISECOND, 0);
            inputDate = inputCalendar.getTime();

            // Get the current date and set its time part to midnight (00:00:00)
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
            currentCalendar.set(Calendar.MINUTE, 0);
            currentCalendar.set(Calendar.SECOND, 0);
            currentCalendar.set(Calendar.MILLISECOND, 0);
            Date currentDate = currentCalendar.getTime();

            return inputDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception, possibly return a default value or throw an exception
        }
        return false;
    }

    // Get updated_summary_list Without deprecated code
    public void AynscCallWS_GetCohortClasses() {
        CompletableFuture.supplyAsync(() -> {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL+"getattendancesummary?Employee_Id="+emp_id+"&Date="+ datevalue_str);
                    System.out.println("GetCohortClasses url: "+url);
                    //open a URL coonnection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    if (urlConnection.getResponseCode() == 200) {
                        System.out.println("I am running");
                    } else {
                        System.out.println("API Connection Denied");
                    }
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
                    }

                    // return the data to onPostExecute method
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }).thenAccept(result -> {
            getActivity().runOnUiThread(() -> {
                dismissProgressDialog();
                try {
                    boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                    JSONObject jsonObject = new JSONObject(result);
                    //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                    //GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                    JSONArray summary_array = jsonObject.getJSONArray("employeeAttendanceSummary");
                    Summarylist.clear();
                    for (int i = 0; i < summary_array.length(); i++) {
                        JSONObject obj = summary_array.getJSONObject(i);
                        //String classMode_Id = obj.getString("classMode_Id");
                        //String trainerName = obj.getString("semesterName");
                        String trainerName = "Purnima Hanchinal";
                        String classMode = obj.getString("classMode");
                        String subjectName = obj.getString("subjectName");
                        String topicName = obj.getString("topicName");
                        String classStartTime = obj.getString("classStartTime");
                        String classEndTime = obj.getString("classEndTime");
                        String attendanceStarttime = obj.getString("attendanceStartDate");
                        String attendanceEndtime = obj.getString("attendanceEndDate");
                        String totalStudents = obj.getString("totalStudents");
                        String totalPresent = obj.getString("totalPresent");
                        String totalAbsent = obj.getString("totalAbsent");
                       // String ratings = obj.getString("totalAbsent");
                        String ratings = "5";

                        HashMap<String, String> summary_map = new HashMap();
                        Summarylist.add(summary_map);
                        summary_map.put("trainerName", trainerName);
                        summary_map.put("classmode", classMode);
                        summary_map.put("subjectName", subjectName);
                        summary_map.put("topicName", topicName);
                        summary_map.put("classStartTime", classStartTime);
                        summary_map.put("classEndTime", classEndTime);
                        summary_map.put("attendanceStarttime", attendanceStarttime);
                        summary_map.put("attendanceEndtime", attendanceEndtime);
                        summary_map.put("totalStudents", totalStudents);
                        summary_map.put("totalPresent", totalPresent);
                        summary_map.put("totalAbsent", totalAbsent);
                        summary_map.put("ratings", ratings);
                    }
                    System.out.println("Summarylist"+Summarylist);
                    ListAdapter summary_adapter = new SimpleAdapter(getActivity(),Summarylist,R.layout.summary_list,
                            new String[]{"trainerName","classmode", "subjectName", "topicName","classStartTime",
                                    "classEndTime","attendanceStarttime", "attendanceEndtime", "totalStudents",
                                    "totalPresent", "totalAbsent", "ratings"},
                            //use same id here, that you used in map, case sensitive
                            new int[]{R.id.trainername_tv, R.id.classMode_tv, R.id.subject_tv, R.id.topic_tv,
                                    R.id.classStartTime_tv, R.id.classEndTime_tv, R.id.attendanceStartTime_tv,
                                    R.id.attendanceEndTime_tv, R.id.total_students_tv, R.id.present_students_tv,
                                    R.id.absent_students_tv, R.id.ratings_tv});
                   /* {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            studentlist_iv = view.findViewById(R.id.view_studentattendance_iv);
                            studentlist_iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    studentlist_iv.setEnabled(false);
                                    String attendanceMainId = Summarylist.get(position).get("attendanceMain_Id");
                                    String attendanceEndtime = Summarylist.get(position).get("attendanceEndtime");
                                    info_Subject = Summarylist.get(position).get("subjectName");
                                    // Show popup with the main ID
                                    Log.e("clicked position", String.valueOf(position));
                                    Log.e("clicked Mainid", attendanceMainId);
                                    Log.e("attendanceEndtime", attendanceEndtime);
                                    StudentAttendance_Popup(attendanceMainId, String.valueOf(position));
                                    // Show another popup here
                                    //StudentAttendance_Popup("80");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            studentlist_iv.setEnabled(true);
                                        }
                                    }, 1000);
                                }
                            });

                            ImageView headcount_iv = view.findViewById(R.id.head_count_img);
                            ImageView headcont_camera = view.findViewById(R.id.headcount_camera);
                            final String isImageUpdated = Summarylist.get(position).get("isImageUpdated");
                            String classMode = Summarylist.get(position).get("classmode");
                            String classMode_Id = Summarylist.get(position).get("classMode_Id");

                            TextView classStartTimeTv = view.findViewById(R.id.classStartTime_tv);
                            TextView classEndTimeTv = view.findViewById(R.id.classEndTime_tv);
                            TextView classend_tv = view.findViewById(R.id.classend_tv);
                            TextView classstart_tv = view.findViewById(R.id.classstart_tv);
                            TextView attendanceEndTime_tv = view.findViewById(R.id.attendanceEndTime_tv);
                            TextView attendanceStartTime_tv = view.findViewById(R.id.attendanceStartTime_tv);
                            TextView attendanceStart_tv = view.findViewById(R.id.attendanceStart_tv);
                            TextView attendanceEnd_tv = view.findViewById(R.id.attendanceEnd_tv);
                            TextView total_students_tv = view.findViewById(R.id.total_students_tv);
                            TextView total_tv = view.findViewById(R.id.total_tv);
                            TextView present_students_tv = view.findViewById(R.id.present_students_tv);
                            TextView present_tv = view.findViewById(R.id.present_tv);
                            TextView absent_students_tv = view.findViewById(R.id.absent_students_tv);
                            TextView absent_tv = view.findViewById(R.id.absent_tv);
                            TextView imgupdated_tv = view.findViewById(R.id.imgupdated_tv);
                            TextView imgupdated = view.findViewById(R.id.imgupdated);
                            TextView head_count_tv = view.findViewById(R.id.head_count_tv);
                            TextView head_count = view.findViewById(R.id.head_count);
                            TextView classNotTaken_tv = view.findViewById(R.id.classNotTaken_tv);
                            TextView classNotTaken_reason_tv = view.findViewById(R.id.classNotTaken_reason_tv);
                            //classMode_Id
                        */
                    /*JSONObject configurable = summary_array.getJSONObject("employeeAttendanceSummary");
                        JSONArray configurablesArray = configurable.getJSONArray("configurables");
*/
                    /*
                            //JSONArray configurableArray = Summarylist.get(position).get("configurables")
                            JSONObject obj = null;
                            try {
                                obj = summary_array.getJSONObject(position);
                                JSONArray Configurable_array = obj.getJSONArray("configurables");
                                for (int j = 0; j < Configurable_array.length(); j++) {
                                    JSONObject configurable = Configurable_array.getJSONObject(j);
                                    String imageUploadVisiblity = configurable.getString("imageUploadVisible");
                                    String allowImageRetake = configurable.getString("allowImageReupload");

                                    if(classMode_Id.equals("2")) {
                                        if (isImageUpdated.equals("1") && imageUploadVisiblity.equals("1")) {
                                            imgupdated.setText("Yes");
                                            headcount_iv.setVisibility(View.VISIBLE);
                                            headcont_camera.setVisibility(View.GONE);
                                        } else if (isImageUpdated.equals("0") && imageUploadVisiblity.equals("1")) {
                                            imgupdated.setText("No");
                                            headcont_camera.setVisibility(View.VISIBLE);
                                            headcount_iv.setVisibility(View.GONE);
                                        } else {
                                            headcount_iv.setVisibility(View.GONE);
                                            headcont_camera.setVisibility(View.GONE);

                                        }
                                    }


                                    else if (classMode_Id.equals("1")) {
                                        if (isImageUpdated.equals("1") && imageUploadVisiblity.equals("1")) {
                                            imgupdated.setText("Yes");
                                            headcount_iv.setVisibility(View.VISIBLE);
                                            headcont_camera.setVisibility(View.GONE);

                                        }
                                        else if (isImageUpdated.equals("0") && imageUploadVisiblity.equals("1")) {
                                            imgupdated.setText("No");
                                            headcount_iv.setVisibility(View.GONE);
                                            if (!isBefore) {
                                                headcont_camera.setVisibility(View.VISIBLE);
                                            } else {
                                                headcont_camera.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                    else if (classMode_Id.equals("3")) {
                                        headcount_iv.setVisibility(View.GONE);
                                        headcont_camera.setVisibility(View.GONE);
                                    }

                                    if(imageUploadVisiblity.equals("0"))
                                    {
                                        headcount_iv.setVisibility(View.GONE);
                                        headcont_camera.setVisibility(View.GONE);

                                    }

                                    headcount_iv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String imageUrl = Summarylist.get(position).get("image_URL");
                                            String  headCount = Summarylist.get(position).get("headCount");
                                            String attendanceMainId = Summarylist.get(position).get("attendanceMain_Id");
                                            Headcount_popup(imageUrl,headCount, attendanceMainId, allowImageRetake);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            headcont_camera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String attendanceMainId = Summarylist.get(position).get("attendanceMain_Id");
                                    screen_str = "summarypopup";
                                    attendanceMainId_image = attendanceMainId;
                                    openCamera();
*/
                    /*

                                if (soapobject_recentattendance_response != null) {
                                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                                        classstarted_bt.setEnabled(true);
                                        classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                    }

                                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                                            equalsIgnoreCase("Success")) {
                                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                                equalsIgnoreCase("Class Completed")) {
                                            String attendanceMainId = Summarylist.get(position).get("attendanceMain_Id");
                                            attendanceMainId_image = attendanceMainId;
                                            openCamera();
                                        } else {
                                            showToast("Complete the on going class first");
                                        }
                                    }
                                }

*/
                    /*

                                }
                            });


                            // Check the classMode and show/hide the views accordingly
                            if ("3".equals(classMode_Id)) {
                                classStartTimeTv.setVisibility(View.GONE);
                                classEndTimeTv.setVisibility(View.GONE);
                                classstart_tv.setVisibility(View.GONE);
                                classend_tv.setVisibility(View.GONE);
                                attendanceStart_tv.setVisibility(View.GONE);
                                attendanceEnd_tv.setVisibility(View.GONE);
                                attendanceStartTime_tv.setVisibility(View.GONE);
                                attendanceEndTime_tv.setVisibility(View.GONE);
                                total_students_tv.setVisibility(View.GONE);
                                total_tv.setVisibility(View.GONE);
                                present_students_tv.setVisibility(View.GONE);
                                present_tv.setVisibility(View.GONE);
                                absent_students_tv.setVisibility(View.GONE);
                                absent_tv.setVisibility(View.GONE);
                                imgupdated_tv.setVisibility(View.GONE);
                                imgupdated.setVisibility(View.GONE);
                                head_count_tv.setVisibility(View.GONE);
                                head_count.setVisibility(View.GONE);
                                classNotTaken_tv.setVisibility(View.VISIBLE);
                                classNotTaken_reason_tv.setVisibility(View.VISIBLE);
                                studentlist_iv.setVisibility(View.GONE);
                            } else {
                                classStartTimeTv.setVisibility(View.VISIBLE);
                                classEndTimeTv.setVisibility(View.VISIBLE);
                                classstart_tv.setVisibility(View.VISIBLE);
                                classend_tv.setVisibility(View.VISIBLE);
                                attendanceStart_tv.setVisibility(View.VISIBLE);
                                attendanceEnd_tv.setVisibility(View.VISIBLE);
                                attendanceStartTime_tv.setVisibility(View.VISIBLE);
                                attendanceEndTime_tv.setVisibility(View.VISIBLE);
                                total_students_tv.setVisibility(View.VISIBLE);
                                total_tv.setVisibility(View.VISIBLE);
                                present_students_tv.setVisibility(View.VISIBLE);
                                present_tv.setVisibility(View.VISIBLE);
                                absent_students_tv.setVisibility(View.VISIBLE);
                                absent_tv.setVisibility(View.VISIBLE);
                                imgupdated_tv.setVisibility(View.VISIBLE);
                                imgupdated.setVisibility(View.VISIBLE);
                                head_count_tv.setVisibility(View.VISIBLE);
                                head_count.setVisibility(View.VISIBLE);
                                classNotTaken_tv.setVisibility(View.GONE);
                                classNotTaken_reason_tv.setVisibility(View.GONE);
                                studentlist_iv.setVisibility(View.VISIBLE);
                            }

                            return view;
                        }
                    };*/

                    //use same path here, id for id, name for name
                    cohort_Classes_lv.setAdapter(summary_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        getActivity().runOnUiThread(() -> {
            showProgressDialog("Processing results...");
        });
    }

}//End of applyonduty fragment

