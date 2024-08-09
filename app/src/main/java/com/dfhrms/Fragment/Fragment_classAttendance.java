package com.dfhrms.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dfhrms.Class.CaptureAct;
import com.dfhrms.Class.Class_URL;
import com.dfhrms.Interfaces.classNotTaken_reason_SI;
import com.dfhrms.SpinnerModels.AbsentReason_SM;
import com.dfhrms.SpinnerModels.BSSIDData_Model;
import com.dfhrms.Utili.ConnectionDetector;
import com.dfhrms.Activity.ErrorActivity;
import com.dfhrms.Interfaces.ApiService;
import com.dfhrms.Interfaces.CohSub_SpinnerInterface;
import com.dfhrms.Interfaces.Topic_SpinnerInterface;
import com.dfhrms.Interfaces.student_absent_reason_SI;
import com.dfhrms.R;
import com.dfhrms.Activity.Slidebar_Activity;
import com.dfhrms.SpinnerModels.CohSub_SM;
import com.dfhrms.SpinnerModels.ClassNoTakenReason_SM;
import com.dfhrms.SpinnerModels.SubTopic_SM;
import com.dfhrms.SpinnerModels.Topic_SM;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.collection.CircularArray;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dfhrms.Activity.Trainer_Geofence;
import com.dfhrms.GPS.GPSTracker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Fragment_classAttendance extends Fragment {
    public static final int RequestPermissionCode = 1;
    private static final int REQUEST_CODE_TRAINER_GEOFENCE = 1;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private ArrayList<String> filtered_subtopic_AL;
    private List<SubTopic_SM> subTopic_List;
    List<SubTopic_SM> selected_subTopicList = new ArrayList<>();
    EditText OTP_ET;
    private Toast currentToast;
    private Handler toastHandler = new Handler();
    private LinearLayout lLayoutfragmentclassattendance;
    private Boolean mRequestingLocationUpdates;
    private String username, pwd, emp_id, min_timer, internet_issue = "empty", str_classstarted_id,currentRadius;
    private long Employeeidlong;
    Context context;
    Resources resource;
    private String str_validateAttendance_response, str_signout_response, str_signin_response, currentdate_yyyyMMdd, str_classscheduled_response;
    private Button classstarted_bt, classscompleted_bt, classNotTaken_bt;
    TextView summaryHeading_tv;
    final Handler handler = new Handler(Looper.getMainLooper());
    private CountDownTimer countDownTimer;
    String manulotp_trainer_str;
    GPSTracker gpstracker_obj, gpstracker_classstarted, gpstracker_classcompleted;
    Double currentlatitude = 0.0;
    Double currentlongitude = 0.0;
    String selectedLatitude;
    String selectedLongitude;
    String selectedRadius;
    Double classscheduled_latitude = 0.0;
    Double classscheduled_longitude = 0.0;
    ConnectionDetector internetDectector;
    Boolean isInternetPresent = false;
    private static final String TAG = Slidebar_Activity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    boolean boolean_signinvalue = false;
    boolean boolean_signoutvalue = false;
    boolean boolean_classstarted = false, boolean_classcompleted = false;
    SoapObject soapobject_recentattendance_response = null;
    private TextView dateEdt_tv;
    private TextView student_name_otp_tv;
    private TextView student_appno_otp_tv;
    private TextView student_mobno_otp_tv;
    private String scanned_appNo_str;
    ImageView ScanQRCode_iv, update_summary_list_iv;
    String datevalue_str;
    ImageView view_attendance_IV, get_location_iv,refresh_screen_iv, Refresh_stuList_iv, imageUpload_iv,bluetooth_iv;
    TextView timer_count_tv, class_not_completed_tv, student_not_assigned_tv, pressBack_tv;
    private Set<String> checkedItems_set;
    private List<String> newCheckedItems_list;
    private ListView subtopics_lv;
    AppCompatSpinner ClassMode_spinner, college_name_Spinner, cohort_spinner, subject_spinner, Topic_spinner;
    private ArrayList<String> cohort_ArrayList = new ArrayList<>();
    private ArrayList<String> ClassMode_ArrayList = new ArrayList<>();
    private ArrayList<String> Topic_ArrayList = new ArrayList<>();
    private ArrayList<CohSub_SM> cohort_SM = new ArrayList<>();
    private ArrayList<CohSub_SM> ClassMode_SM = new ArrayList<>();
    private ArrayList<com.dfhrms.SpinnerModels.Topic_SM> Topic_SM = new ArrayList<>();
    private ArrayList<String> subject_ArrayList = new ArrayList<>();
    private ArrayList<CohSub_SM> subject_SM = new ArrayList<>();
    String college_selected_str, cohort_selected_str, subject_selected_str, topic_selected_str, classMode_selected_str;
    String selected_classmodeId_str, classmode_id_str="0", selectedSubjectId_str, subject_id_str="0",
            secret_key_status_str, attendance_main_id_str, topic_id_str, selectedTopicId_str, cohort_id_str="0",
            attendanceMainId_image, screen_str;
    private static final String SOAP_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_METHOD_NAME = "GetEmployeeLocations";
    private static final String SOAP_ACTION = "http://tempuri.org/GetEmployeeLocations";
    private static final String SOAP_URL = "https://dfhrms.dfindia.org/PMSservice.asmx?WSDL";
    private List<LocationData> locationDataList;
    String Class_index;
    List<String> location_list;
    int imageUploadVisible ;
    String imageUrl;
    int imageUploadMandatory ;
    private int isImageUpdated ;
    int allowImageReupload ;
    ProgressDialog progressDialog;
    JSONObject GetSecretKey_Response;
    ListView student_lv, summary_lv, studentPopup_lv;
    String attendance_mainid;
    String clicked_Student_name_str, clicked_Student_Appno_str, clicked_Student_Mobno_str, mainTopic_Id_str, clicked_student_reasonID_str;
    JSONArray GetSecretKey_studentlist_Response;
    boolean isPresent_bool;
    String attendance_status_str;
    CircularArray<Object> locations;
    int CAMERA_PERM_CODE=106;
    private String currentPhotoPath;

    int CAMERA_REQUEST_CODE = 103;
    ArrayList<HashMap<String, Object>> Studentlist = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> StudentAttendancelist = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> Summarylist = new ArrayList<HashMap<String, String>>();
    String info_Subject;
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    Button start_attendance_btn, send_OTP_btn, SubmitReason_btn;
    LinearLayout start_attendance_ll, classtaken_ll, classNottaken_ll;
    RelativeLayout secret_key_cv;
    String is_attendance_completed_str, timer_str;
    int timer_in_mili;
    TextView no_summaryvalues_tv, location_not_set_tv;
    String passed_lat = "0.0",passed_long= "0.0";
    ListAdapter subtopic_adapter, studentlist_adapter;
    private EditText selectedsubtopics_et;
    TextView secret_key_tv, total_count_tv, present_count_tv, absent_count_tv,
            present_percent_tv, absent_percent_tv, previous_date_tv, out_of_office_tv, date_trainer_summary_tv;
    String subtopic_jsonResult_str;
    SharedPreferences sharedPreferences;
    Typeface boldTypeface;
    private boolean settingsDialogShown = false;
    List<LocationData> locations_list;
    Long long_Employeeid;
    String str_response, class_typr_str;
    static String str_yyymmdd= "";
    int count=0;
    String classStartTime_str, classEndTime_str;
    Boolean classStarted_bool = false, classCompleted_bool = false;
    EditText reasonEditText;
    LinearLayout reason_ll;
    String Reason_str;
    Boolean is_classCompletedClicked;
    private AlertDialog dialog;
    private AlertDialog headcount_dialog;
    ArrayList<String> arraylist_classroom_base64 = new ArrayList<>();
    private AlertDialog completeClassDialog;
    private Spinner classNotTaken_spinner;
    private Spinner absent_Reason_spinner;
    String reason_selected_str, reason_id_str, absentReasonID_str, absentReason_selected_str;
    EditText classNotTaken_reason_ET;
    String reason_remark_tv;
    int reasonselected_intindex;
    private ArrayList<String> reason_ArrayList = new ArrayList<>();
    private ArrayList<String> Absentreason_ArrayList = new ArrayList<>();
    private ArrayList<ClassNoTakenReason_SM> reasons_spinnermodel = new ArrayList<>();
    private ArrayList<AbsentReason_SM> Absent_reasons_spinnermodel = new ArrayList<>();
    ImageView studentlist_iv;
    Boolean camera_clicked;
    ImageView ver_Colour_iv_mca;

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1;

   /* private WifiManager wifiManager;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> wifiList;
    private TextView wifiListTextView;
    String bssid_jsonResult_str;
    private TextView bssidTextView;*/

  /*  private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                Log.e("broadcastID", attendance_main_id_str);
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    };*/

    @Override
    public void onResume() {
        super.onResume();

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(wifiScanReceiver, intentFilter);*/
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        attendance_main_id_str = sh.getString("attendance_main_id_str","");
        Log.e("MainID", attendance_main_id_str);
        selected_classmodeId_str = sh.getString("selected_classmodeId_str","");
        is_attendance_completed_str = sh.getString("is_attendance_completed_str", "");
        is_classCompletedClicked = Boolean.valueOf(sh.getString("is_classCompletedClicked", ""));

        isImageUpdated = sh.getInt("isImageUpdated", 0);
        imageUploadMandatory = sh.getInt("imageUploadMandatory", 0);
        allowImageReupload = sh.getInt("allowImageReupload", 0);
        //imageUploadVisible = sh.getString("imageUploadVisible", "");
        imageUploadVisible = sh.getInt("imageUploadVisible", 0);

        Log.d("isImageUpdateddddddd", String.valueOf(isImageUpdated));
        Log.d("imageUploadMandatory", String.valueOf(imageUploadMandatory));

        Log.d("imageUploadVisible", String.valueOf(imageUploadVisible));
        Log.d("allowImageReupload", String.valueOf(allowImageReupload));

        if (countDownTimer != null) {
            countDownTimer = startCountdownTimer(timer_in_mili);
            countDownTimer.start();
        }
        if (settingsDialogShown){
            if (passed_lat.equals("0.0")) {
                location_not_set_tv.setVisibility(View.VISIBLE);
                gpstracker_obj = new GPSTracker(getActivity());
                if (gpstracker_obj.canGetLocation())
                {
                    Intent i = new Intent(getActivity(), Trainer_Geofence.class);
                    TrainerGeofenceResultLauncher.launch(i);
                } else {
                    gpstracker_obj.showSettingsAlert();
                }
            } else {
                location_not_set_tv.setVisibility(View.GONE);
                AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                task1.execute();
            }
        }

       /* SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        passed_lat = sharedPreferences.getString("passed_lat", "0.0");
        passed_long = sharedPreferences.getString("passed_long", "0.0");
        if(!passed_lat.equals("0.0")){
            location_not_set_tv.setVisibility(View.GONE);
            AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
            task1.execute();
        }
        System.out.println("Passed onresume Lat and long: " + passed_lat + " " + passed_long);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });

        /*if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
        } else {
            getBSSID();
        }
*/
        boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        lLayoutfragmentclassattendance= (LinearLayout) inflater.inflate(R.layout.fragment_classattendance, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("passed_lat", "0.0");
        editor.putString("passed_long", "0.0");
        editor.apply();
        EnableRuntimePermission();
        AsyncCallWS_getClassNotTakenReasons();
        AsyncCallWS_getStudent_absent_Reasons();
        timer_count_tv = lLayoutfragmentclassattendance.findViewById(R.id.timer_count_tv);
        class_not_completed_tv = lLayoutfragmentclassattendance.findViewById(R.id.class_not_completed_tv);
        pressBack_tv = lLayoutfragmentclassattendance.findViewById(R.id.pressBack_tv);
        student_not_assigned_tv = lLayoutfragmentclassattendance.findViewById(R.id.student_not_assigned_tv);
        locationDataList = new ArrayList<>();
        //latitude = lLayoutfragmentclassattendance.findViewById(R.id.latitude_TV);
        //longitude = lLayoutfragmentclassattendance.findViewById(R.id.longitude_TV);
        secret_key_cv = lLayoutfragmentclassattendance.findViewById(R.id.secret_key_cv);
        dateEdt_tv = lLayoutfragmentclassattendance.findViewById(R.id.clickfromdate_TV);
        secret_key_tv = lLayoutfragmentclassattendance.findViewById(R.id.secret_key_tv);
        total_count_tv = lLayoutfragmentclassattendance.findViewById(R.id.total_count_tv);
        present_count_tv = lLayoutfragmentclassattendance.findViewById(R.id.present_count_tv);
        absent_count_tv = lLayoutfragmentclassattendance.findViewById(R.id.absent_count_tv);
        //total_percent_tv = lLayoutfragmentclassattendance.findViewById(R.id.total_percent_tv);
        present_percent_tv = lLayoutfragmentclassattendance.findViewById(R.id.present_percent_tv);
        absent_percent_tv = lLayoutfragmentclassattendance.findViewById(R.id.absent_percent_tv);
        student_lv = lLayoutfragmentclassattendance.findViewById(R.id.listView_lv);
        start_attendance_ll = lLayoutfragmentclassattendance.findViewById(R.id.attendance_details);
        classtaken_ll = lLayoutfragmentclassattendance.findViewById(R.id.classtaken_ll);
        classNottaken_ll = lLayoutfragmentclassattendance.findViewById(R.id.classNottaken_ll);
        start_attendance_btn = lLayoutfragmentclassattendance.findViewById(R.id.start_attendance_btn);
        classstarted_bt = (Button) lLayoutfragmentclassattendance.findViewById(R.id.classstarted_bt);
        classNotTaken_bt = (Button) lLayoutfragmentclassattendance.findViewById(R.id.classNotTaken_bt);
        classscompleted_bt = (Button) lLayoutfragmentclassattendance.findViewById(R.id.classscompleted_bt);
        previous_date_tv = (TextView) lLayoutfragmentclassattendance.findViewById(R.id.previous_date_tv);
        out_of_office_tv = (TextView) lLayoutfragmentclassattendance.findViewById(R.id.out_of_location_tv);
        location_not_set_tv = (TextView) lLayoutfragmentclassattendance.findViewById(R.id.location_not_set_tv);
        summaryHeading_tv = (TextView) lLayoutfragmentclassattendance.findViewById(R.id.summaryHeading_tv);
        ver_Colour_iv_mca =(ImageView) lLayoutfragmentclassattendance.findViewById(R.id.ver_Colour_iv_mca);

        int imageResource = Class_URL.getImageResource();
        ver_Colour_iv_mca.setImageResource(imageResource);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        ver_Colour_iv_mca.startAnimation(anim);
        double savedRadius = getCurrentRadius();
        class_not_completed_tv.setVisibility(View.GONE);
        pressBack_tv.setVisibility(View.GONE);
        student_not_assigned_tv.setVisibility(View.GONE);
        selectedsubtopics_et = lLayoutfragmentclassattendance.findViewById(R.id.selectedValuesEditText);
        /*selectedsubtopics_et.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SecondActivity.class);
            intent.putExtra("Topic_id", topic_id_str);
            startActivityForResult(intent, REQUEST_CODE);
        });*/
//showListPopup()
        Calendar currentDate1 = Calendar.getInstance();
        SimpleDateFormat df_yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
        str_yyymmdd = df_yyyymmdd.format(currentDate1.getTime());


        start_attendance_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //&& Topic_spinner.getSelectedItem().equals("Select")) {
                System.out.println("Check topic and subtopic"+ selectedsubtopics_et.getText().toString());
                System.out.println("Check topic and subtopic"+ Topic_spinner.getSelectedItem());
                //!TextUtils.isEmpty(textValue)

                if (subject_id_str == null && cohort_id_str == null && classmode_id_str == null &&
                        topic_id_str == null && college_selected_str.equals("Select")) {
                    showToast("Select all fields");
                } else if (selectedsubtopics_et.getText().toString().equals("")) {
                    showToast("Select all fields");
                } else {
                    AsyncCallWS_Update_ClassId();
                    pressBack_tv.setVisibility(View.VISIBLE);
                    System.out.println("MyAsyncTasks in start_attendance_btn");
                    class_typr_str = "New";
                    summaryHeading_tv.setText("Summary");
                    summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();
                    if (isInternetPresent) {
                        //student_lv.setEnabled(false);
                        AsyncCallWS_GetSecretKey asyncCallWSGetSecretKey = new AsyncCallWS_GetSecretKey();
                        asyncCallWSGetSecretKey.execute();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    classstarted_bt.setEnabled(false);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    start_attendance_ll.setVisibility(View.VISIBLE);
                    secret_key_cv.setVisibility(View.VISIBLE);
                    start_attendance_btn.setEnabled(false);
                    start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                    classscompleted_bt.setEnabled(true);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    college_name_Spinner.setEnabled(false);
                    ClassMode_spinner.setEnabled(false);
                    cohort_spinner.setEnabled(false);
                    subject_spinner.setEnabled(false);
                    Topic_spinner.setEnabled(false);
                    selectedsubtopics_et.setEnabled(false);

                    if (imageUploadVisible == 1) {
                        imageUpload_iv.setVisibility(View.VISIBLE);
                        // Do something if image upload is visible
                        Log.d("Configurable", "Image upload is visible.");
                    } else {
                        imageUpload_iv.setVisibility(View.GONE);
                        // Handle case where image upload is not visible
                        Log.d("Configurable", "Image upload is not visible.");
                    }
                }
            }
        });

        gpstracker_obj = new GPSTracker(getActivity());
        Refresh_stuList_iv = lLayoutfragmentclassattendance.findViewById(R.id.Refresh_iv);

        Refresh_stuList_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSubjectId_str.equals(0) || cohort_selected_str.equals("Select")){}else {
                    if (isInternetPresent) {
                        Refresh_stuList_iv.setEnabled(false);
                        class_typr_str = "Exists";
                        AsyncCallWS_Update_student_list updateStudentList = new AsyncCallWS_Update_student_list();
                        updateStudentList.execute();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                }
                //Studentlist.clear();
                /*try {
                    asyncTask.updateStudentList(Studentlist);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }*/

                // After updating the Studentlist, notify the subtopic_adapter of the changes
                //((SimpleAdapter) student_lv.getAdapter()).notifyDataSetChanged();
            }
        });
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
                                start_attendance_ll.setVisibility(View.GONE);
                                ClassMode_spinner.setSelection(0);
                                college_name_Spinner.setSelection(0);
                                cohort_spinner.setSelection(0);
                                subject_spinner.setSelection(0);
                                Topic_spinner.setSelection(0);
                                selectedsubtopics_et.setText("");
                                Summarylist.clear();
                                college_selected_str = "";
                                subject_selected_str = "";
                                cohort_selected_str = "";
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

        view_attendance_IV = lLayoutfragmentclassattendance.findViewById(R.id.view_attendance_iv);
        view_attendance_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_attendance_IV.setEnabled(false);
                // Create and show the custom dialog
                showDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view_attendance_IV.setEnabled(true);
                    }
                }, 1000);
            }
        });



        refresh_screen_iv = lLayoutfragmentclassattendance.findViewById(R.id.refresh_screen_iv);
        refresh_screen_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshUI();
            }
        });

        imageUpload_iv = lLayoutfragmentclassattendance.findViewById(R.id.imageUpload_iv);

        imageUpload_iv.setVisibility(View.GONE);
        imageUpload_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    camera_clicked = true;
                    screen_str = "main";
                    attendanceMainId_image = attendance_main_id_str;
                    openCamera();
                }
            }
        });

        bluetooth_iv =lLayoutfragmentclassattendance.findViewById(R.id.bluetooth);
        bluetooth_iv.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                startActivity(intent);
            }
        });


        get_location_iv = lLayoutfragmentclassattendance.findViewById(R.id.get_location_iv);
        get_location_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_location_iv.setEnabled(false);
                gpstracker_obj = new GPSTracker(getActivity());

                if (gpstracker_obj.canGetLocation())
                {
                    System.out.println("Entered case 4....onclick.signin....");

                    //   startLocationUpdates(); //commented working
                    boolean_signinvalue = true;

                    Intent i = new Intent(getActivity(), Trainer_Geofence.class);

                    startActivity(i);
                } else {
                    gpstracker_obj.showSettingsAlert();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        get_location_iv.setEnabled(true);
                    }
                }, 1000);
            }
        });
        college_name_Spinner = lLayoutfragmentclassattendance.findViewById(R.id.college_name_spinner);
        ClassMode_spinner = lLayoutfragmentclassattendance.findViewById(R.id.mode_of_class_spinner);
        cohort_spinner = lLayoutfragmentclassattendance.findViewById(R.id.cohort_Spinner);
        subject_spinner = lLayoutfragmentclassattendance.findViewById(R.id.subject_spinner);
        Topic_spinner = lLayoutfragmentclassattendance.findViewById(R.id.Topic_Spinner);
        // Fetch data from SOAP API in a separate thread (AsyncTask is used here)
        SharedPreferences myprefs = this.getActivity().getSharedPreferences("user", MODE_PRIVATE);
        username = myprefs.getString("user1", "nothing");
        pwd = myprefs.getString("pwd", "nothing");
        emp_id = myprefs.getString("emp_id", "nothing");
        //min_timer = myprefs.getString("min_timer", "nothing");

        // Reading the Last started classes Attendance main Id if the App is uninstalled and installed.
        if (attendance_main_id_str == null){
            Log.e("Reading Id: ", "Reading Id");
            Trainee_summary_list();
        }

        /*wifiList = new ArrayList<>();
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            showToast("WiFi Manager is not available");
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
        } else {
            startScan();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(wifiScanReceiver, intentFilter);

*/
        //Log.e("min_timer", min_timer);
       /* float savedRadius = myprefs.getFloat("currentRadius", -1f);
        currentRadius = String.valueOf((double) savedRadius);l
        Log.e("currentRadius",currentRadius);*/

        getActivity().setTitle("Mark Class Attendance");
        Employeeidlong = Long.parseLong(emp_id); // for web service




        context = lLayoutfragmentclassattendance.getContext();
        resource = context.getResources();

        str_validateAttendance_response = "error";
        str_classscheduled_response = "error";
        str_signout_response = "error";
        str_signin_response = "error";
        str_classstarted_id = "0";
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //2018-12-18
        currentdate_yyyyMMdd = df.format(c);

        AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
        task1.execute();

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        // updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();


        classstarted_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_classCompletedClicked = false;
                gpstracker_classstarted = new GPSTracker(getActivity());
                if (gpstracker_classstarted.canGetLocation()) {
                    System.out.println("Entered case 4...signout......");
                    startLocationUpdates();
                    ClassMode_spinner.setEnabled(false);
                    cohort_spinner.setEnabled(false);
                    subject_spinner.setEnabled(false);
                    college_name_Spinner.setEnabled(false);
                    Topic_spinner.setEnabled(false);
                    dateEdt_tv.setEnabled(false);
                    boolean_signoutvalue = false;
                    boolean_signinvalue = false;
                    boolean_classstarted = true;
                    boolean_classcompleted = false;
                    classstarted_bt.setEnabled(false);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    classscompleted_bt.setEnabled(false);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false); // Prevent dismissing by tapping outside
                    progressDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            // Actions to do after 5 seconds
                            start_attendance_btn.setEnabled(true);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        }
                    }, 3000);
                    if (secret_key_status_str.equals("Exists")){
                        selectedsubtopics_et.setText("");
                        start_attendance_ll.setVisibility(View.GONE);
                        if (selected_classmodeId_str.equals("2")){
                            passed_lat = sharedPreferences.getString("passed_lat", "0.0");
                            passed_long = sharedPreferences.getString("passed_long", "0.0");
                            if (passed_lat.equals("0.0")) {
                                location_not_set_tv.setVisibility(View.VISIBLE);
                                gpstracker_obj = new GPSTracker(getActivity());
                                if (gpstracker_obj.canGetLocation())
                                {
                                    Intent i = new Intent(getActivity(), Trainer_Geofence.class);
                                    TrainerGeofenceResultLauncher.launch(i);
                                } else {
                                    gpstracker_obj.showSettingsAlert();
                                    settingsDialogShown = true;
                                }
                            } else {
                                location_not_set_tv.setVisibility(View.GONE);
                                AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                                task1.execute();
                            }
                        }
                    }
                } else {
                    gpstracker_classstarted.showSettingsAlert();
                }

            }
        });

        classscompleted_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the button to prevent double-clicks
                classscompleted_bt.setEnabled(false);

                // Your existing code
                is_classCompletedClicked = true;
                camera_clicked = false;

                Trainee_summary_list();
                Log.d("isImageUpdated12", String.valueOf(isImageUpdated));

                // Introduce a delay using Handler
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gpstracker_classcompleted = new GPSTracker(getActivity());
                        Log.d("isImageUpdated11", String.valueOf(isImageUpdated));
                        Log.d("imageUploadMandatoryyyy", String.valueOf(imageUploadMandatory));
                        Log.d("selected_classmodeId_stryy", selected_classmodeId_str);

                        if (imageUploadMandatory == 1 && isImageUpdated == 0 && "2".equals(selected_classmodeId_str)) {
                            screen_str = "completeclass";
                            attendanceMainId_image = attendance_main_id_str;
                            openCamera();
                        } else {
                            // Proceed with the GPS location logic
                            gpstracker_classcompleted = new GPSTracker(getActivity());
                            if (gpstracker_classcompleted.canGetLocation()) {
                                System.out.println("Entered case 4...signout......");
                                completeclass_ConfirmDialog();
                            } else {
                                gpstracker_classcompleted.showSettingsAlert();
                            }
                        }

                        // Re-enable the button after the delay
                        classscompleted_bt.setEnabled(true);
                    }
                }, 1000); // Adjust the delay as needed
            }
        });

        /*classscompleted_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_classCompletedClicked = true;
                gpstracker_classcompleted = new GPSTracker(getActivity());
                if (gpstracker_classcompleted.canGetLocation()) {
                    System.out.println("Entered case 4...signout......");
                    completeclass_ConfirmDialog();
                }
                else {
                    gpstracker_classcompleted.showSettingsAlert();
                }
            }
        });*/

        classNotTaken_bt.setEnabled(true);
        classNotTaken_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
        classNotTaken_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subject_id_str == null && cohort_id_str == null && classmode_id_str == null &&
                        topic_id_str == null && college_selected_str.equals("Select")) {
                    showToast("Select all fields");
                } else if (selectedsubtopics_et.getText().toString().equals("")) {
                    showToast("Select all fields");
                } else {
                    classNotTaken_bt.setEnabled(false);
                    ClassNotTaken_Popup();
                    if (soapobject_recentattendance_response != null) {
                        if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                            //classstarted_bt.setEnabled(true);
                            //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        }
                        if (soapobject_recentattendance_response.getProperty("Message").toString().
                                equalsIgnoreCase("Success")) {
                            if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                    equalsIgnoreCase("Class Started") &&
                                    soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                            equalsIgnoreCase("0")) {
                                AsyncCallWS_DeleteStartedClass();
                            }
                        }
                    }
                }
            }
        });


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





        //call here

        college_name_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // "Select" is selected, do nothing or show a message
                    return;
                }

                imageUpload_iv.setVisibility(View.GONE);
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
                ClassMode_spinner.setSelection(0);
                cohort_spinner.setSelection(0);
                subject_spinner.setSelection(0);
                Topic_spinner.setSelection(0);
                selectedsubtopics_et.setText("");
                subject_selected_str = "";
                cohort_selected_str = "";
                ClassMode_SM.clear();
                subject_SM.clear();
                cohort_SM.clear();

                ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Getting Location", true);
                dialog.show();

                if (!college_selected_str.equals("Select")) {
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();
                    if (isInternetPresent) {
                        fetchJSON(college_selected_str);
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

        ClassMode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classMode_selected_str = (String) parent.getItemAtPosition(position);
                imageUpload_iv.setVisibility(View.GONE);
                try {
                    classMode_selected_str = URLEncoder.encode(classMode_selected_str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                if (position == 0) {
                    // Handle the "Select" case without processing subjectId
                    System.out.println("Select option is selected");
                } else {
                    // Retrieve the selected subject_SM model
                    CohSub_SM selectedModel = ClassMode_SM.get(position - 1);  // Subtract 1 to account for "Select"
                    selected_classmodeId_str = selectedModel.getClassMode_Id();

                    // Now you have the selected subjectId, do whatever you need with it
                    System.out.println("Selected classmodeid: " + selected_classmodeId_str);
                    String later_modeID = selected_classmodeId_str;
                    if (later_modeID.equals("3")){
                        classNottaken_ll.setVisibility(View.VISIBLE);
                        classtaken_ll.setVisibility(View.GONE);
                    } else {
                        classNottaken_ll.setVisibility(View.GONE);
                        classtaken_ll.setVisibility(View.VISIBLE);
                    }
                }


                Topic_spinner.setSelection(0);
                selectedsubtopics_et.setText("");
                start_attendance_ll.setVisibility(View.GONE);
                classmode_id_str = String.valueOf(position);
                classscompleted_bt.setEnabled(false);
                classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                classstarted_bt.setEnabled(false);
                classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                if (soapobject_recentattendance_response != null) {
                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                        //classstarted_bt.setEnabled(true);
                        //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    }

                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                            equalsIgnoreCase("Success")) {
                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                equalsIgnoreCase("Class Started") &&
                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                        equalsIgnoreCase("0")) {
                            start_attendance_btn.setEnabled(true);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            classscompleted_bt.setEnabled(false);
                            classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        } else {
                            start_attendance_btn.setEnabled(false);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        }
                    }
                }
                clearCheckedItems();
                clearTopicListAndSetDefault();
                Log.d("classmode_selected_str", classMode_selected_str);
                Log.d("classmode_id_str", classmode_id_str);
                //Log.e("Selected topic id in cohort: ", topic_id_str);
                if (subject_id_str == null || cohort_id_str == null || classmode_id_str == null || topic_id_str == null ||
                        subject_id_str.equals("0") || cohort_id_str.equals("0") || classmode_id_str.equals("0") || topic_id_str.equals("0")) {
                    showToast("Select all fields");
                } else {
                    Log.e("Check_Secret_Key_Status", "class mode");
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();
                    if (isInternetPresent) {
                        if (!selected_classmodeId_str.equals("3")){
                            Check_Secret_Key_Status();
                        }
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cohort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cohort_id_str = String.valueOf(position);
                imageUpload_iv.setVisibility(View.GONE);
                if (subject_id_str == null){
                    subject_id_str = "0";
                }
                cohort_selected_str = (String) parent.getItemAtPosition(position);
                try {
                    cohort_selected_str = URLEncoder.encode(cohort_selected_str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                student_not_assigned_tv.setVisibility(View.GONE);
                //subject_spinner.setSelection(0);
                Topic_spinner.setSelection(0);
                selectedsubtopics_et.setText("");
                subject_selected_str ="";
                classscompleted_bt.setEnabled(false);
                classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                classstarted_bt.setEnabled(false);
                classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                if (soapobject_recentattendance_response != null) {
                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                        //classstarted_bt.setEnabled(true);
                        //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    }

                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                            equalsIgnoreCase("Success")) {
                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                equalsIgnoreCase("Class Started") &&
                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                        equalsIgnoreCase("0")) {
                            start_attendance_btn.setEnabled(true);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            classscompleted_bt.setEnabled(true);
                            classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            imageUpload_iv.setVisibility(View.GONE);
                        } else {
                            start_attendance_btn.setEnabled(false);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        }
                    }
                }
                Log.d("cohort_selected_str", cohort_selected_str);
                //subject_ArrayList.clear();
                start_attendance_ll.setVisibility(View.GONE);
                clearCheckedItems();
                //Log.e("Selected topic id in cohort: ", topic_id_str);
                if (subject_id_str == null || cohort_id_str == null || classmode_id_str == null || topic_id_str == null ||
                        subject_id_str.equals("0") || cohort_id_str.equals("0") || classmode_id_str.equals("0") || topic_id_str.equals("0")) {
                    showToast("Select all fields");
                } else {
                    Log.e("Check_Secret_Key_Status", "cohortspinner");
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();
                    if (isInternetPresent) {
                        if (!selected_classmodeId_str.equals("3")){
                            Check_Secret_Key_Status();
                        }
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageUpload_iv.setVisibility(View.GONE);
                subject_selected_str = (String) parent.getItemAtPosition(position);
                try {
                    subject_selected_str = URLEncoder.encode(subject_selected_str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                if (position == 0) {
                    // Handle the "Select" case without processing subjectId
                    System.out.println("Select option is selected");
                } else {
                    // Retrieve the selected subject_SM model
                    CohSub_SM selectedModel = subject_SM.get(position - 1);  // Subtract 1 to account for "Select"
                    selectedSubjectId_str = selectedModel.getSubjectId();

                    // Now you have the selected subjectId, do whatever you need with it
                    System.out.println("Selected SubjectId: " + selectedSubjectId_str);
                }

                Topic_spinner.setSelection(0);
                topic_id_str = "0";
                selectedsubtopics_et.setText("");
                start_attendance_ll.setVisibility(View.GONE);
                subject_id_str = String.valueOf(position);
                classscompleted_bt.setEnabled(false);
                classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                classstarted_bt.setEnabled(false);
                classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                if (soapobject_recentattendance_response != null) {
                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                        //classstarted_bt.setEnabled(true);
                        //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    }

                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                            equalsIgnoreCase("Success")) {
                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                equalsIgnoreCase("Class Started") &&
                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                        equalsIgnoreCase("0")) {
                            start_attendance_btn.setEnabled(true);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            classscompleted_bt.setEnabled(false);
                            classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));

                            imageUpload_iv.setVisibility(View.GONE);
                            //showToast("Complete the previous class to start next class");
                        } else {
                            start_attendance_btn.setEnabled(false);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        }
                    }
                }
                clearCheckedItems();
                clearTopicListAndSetDefault();

                internetDectector = new ConnectionDetector(getActivity());
                isInternetPresent = internetDectector.isConnectingToInternet();
                if (isInternetPresent) {
                    fetch_Topic(selectedSubjectId_str);
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("No Internet Connection")
                            .setPositiveButton("OK", null)
                            .show();
                }
                Log.d("subject_selected_str", subject_selected_str);
                Log.d("subject_id_str", subject_id_str);
                //Log.e("Selected topic id in subject: ", topic_id_str);
                if (subject_id_str == null || cohort_id_str == null || classmode_id_str == null || topic_id_str == null ||
                        subject_id_str.equals("0") || cohort_id_str.equals("0") || classmode_id_str.equals("0") || topic_id_str.equals("0")) {
                    showToast("Select all fields");
                } else {
                    Log.e("Check_Secret_Key_Status", "subjectspinner");
                    internetDectector = new ConnectionDetector(getActivity());
                    isInternetPresent = internetDectector.isConnectingToInternet();
                    if (isInternetPresent) {
                        if (!selected_classmodeId_str.equals("3")){
                            Check_Secret_Key_Status();
                        }
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Topic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                imageUpload_iv.setVisibility(View.GONE);
                //Toast.makeText(getContext(),"spinner entered",Toast.LENGTH_SHORT).show();
                //int i=2;i++;
                topic_selected_str = (String) parent.getItemAtPosition(position);
                try {
                    topic_selected_str = URLEncoder.encode(topic_selected_str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                topic_id_str = String.valueOf(position);
                selectedsubtopics_et.setText("");
                // Retrieve the selected topic model
                int adjustedPosition = position - 1; // Adjusted position without "Select"
                if (adjustedPosition >= 0 && adjustedPosition < Topic_SM.size()) {
                    com.dfhrms.SpinnerModels.Topic_SM selectedModel = Topic_SM.get(adjustedPosition);
                    selectedTopicId_str = selectedModel.getTopicId();

                    // Now you have the selected topicId, do whatever you need with it
                    System.out.println("Selected TopicId: " + selectedTopicId_str);
                } else {
                    // Handle the case when adjustedPosition is out of bounds
                    System.out.println("Invalid adjustedPosition: " + adjustedPosition);
                    selectedTopicId_str = ""; // Set to an appropriate default value
                }
                Log.d("topic_selected_str", topic_selected_str);
                Log.d("topic_id_str", topic_id_str);
                // Check if the selected item is "Select"
                if ("Select".equals(topic_selected_str)) {
                    // Handle the "Select" case without processing topicId
                    System.out.println("Select option is selected");
                    selectedTopicId_str = ""; // Set to an appropriate default value
                } else {
                    Log.e("Selected topic id in topic: ", topic_id_str);
                    if (subject_id_str == null || cohort_id_str == null || classmode_id_str == null || topic_id_str == null ||
                            subject_id_str.equals("0") || cohort_id_str.equals("0") || classmode_id_str.equals("0") || topic_id_str.equals("0")) {
                        showToast("Select all fields");
                    } else {
                        Log.e("Check_Secret_Key_Status", "topicspinner");
                        internetDectector = new ConnectionDetector(getActivity());
                        isInternetPresent = internetDectector.isConnectingToInternet();
                        if (isInternetPresent) {
                            if (!selected_classmodeId_str.equals("3")){
                                Check_Secret_Key_Status();
                            }
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("No Internet Connection")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        selectedsubtopics_et.setFocusable(false);
        selectedsubtopics_et.setFocusableInTouchMode(false);

        selectedsubtopics_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                System.out.println("topic_id_str"+ topic_id_str);
                if (topic_id_str != null && !topic_id_str.equals("0")) {
                    showsubtopic_ListPopup();
                    selectedsubtopics_et.setEnabled(true);
                } else {
                    showToast("Topic not selected");
                }
            }
        });

        return lLayoutfragmentclassattendance;
    }


    private void handleUncaughtException(Thread thread, Throwable e) {
        // Log the exception (optional)
        e.printStackTrace();

        // Create an intent to start your error activity
        Intent intent = new Intent(getActivity(), ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("error_message", e.getMessage());
        startActivity(intent);


        // Close the current process
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.dfhrms.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            ImageActivityResultLauncher.launch(intent);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    ActivityResultLauncher<Intent> ImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // File is stored in the path returned by createImageFile()
                        showProgressDialog("Uploading Image");
                        if (currentPhotoPath != null) {
                            File imageFile = new File(currentPhotoPath);
                            if (imageFile.exists()) {
                                // Check file size
                                long fileSizeInBytes = imageFile.length();
                                if (fileSizeInBytes > 1 * 1024 * 1024) { // If file size is more than MB certain size
                                    imageFile = compressImageFile(imageFile);
                                }

                                byte[] imageBytes = convertFileToByteArray(imageFile);
                                String str_base64imagestring = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                Log.e("Base64 Image", str_base64imagestring);

                                //attendanceMainId_image = attendance_main_id_str;
                                arraylist_classroom_base64.clear();
                                arraylist_classroom_base64.add(str_base64imagestring);
                                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri contentUri = Uri.fromFile(imageFile);
                                mediaScanIntent.setData(contentUri);
                                requireActivity().sendBroadcast(mediaScanIntent);

                                AsyncTask_studentImageUpload(attendanceMainId_image, str_base64imagestring);
                            } else {
                                Log.e("Error", "Image file does not exist: " + currentPhotoPath);
                            }
                        } else {
                            Log.e("Error", "currentPhotoPath is null");
                        }
                    }
                }
            }
    );

    // Trainer Geofence Result launcher
    ActivityResultLauncher<Intent> TrainerGeofenceResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        System.out.println("Onactivityresult is called");
                        passed_lat = sharedPreferences.getString("passed_lat", "0.0");
                        passed_long = sharedPreferences.getString("passed_long", "0.0");
                        if (!passed_lat.equals("0.0")) {
                            location_not_set_tv.setVisibility(View.GONE);
                            AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                            task1.execute();
                        }
                        System.out.println("Passed onresume Lat and long: " + passed_lat + " " + passed_long);
                    } else {
                        // Handle the case where Trainer_Geofence activity was not successful
                        location_not_set_tv.setVisibility(View.VISIBLE);
                    }
                }
            }
    );

    private File compressImageFile(File imageFile) {
        try {
            // Decode image to bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            // Create a file to save the compressed image
            File compressedImageFile = new File(imageFile.getParent(), "COMPRESSED_" + imageFile.getName());

            // Set initial compression quality
            int quality = 100;

            do {
                FileOutputStream fos = new FileOutputStream(compressedImageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos); // Compress bitmap
                fos.close();

                // Check file size after compression
                long fileSizeInBytes = compressedImageFile.length();
                if (fileSizeInBytes <= 1 * 1024 * 1024) {
                    // If file size is under 1 MB, break the loop
                    break;
                }

                // Decrease quality for next iteration
                quality -= 5;

                // If quality is 0, stop compressing to prevent infinite loop
                if (quality <= 0) {
                    break;
                }

            } while (compressedImageFile.length() > 1 * 1024 * 1024);

            return compressedImageFile;
        } catch (IOException e) {
            Log.e("Error", "Error compressing image file: " + e.getMessage());
            return imageFile; // Return original file if compression fails
        }
    }
    private byte[] convertFileToByteArray(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            fis.close();
            return bos.toByteArray();
        } catch (IOException e) {
            Log.e("Error", "Error converting file to byte array: " + e.getMessage());
            return new byte[0];
        }
    }
/*
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }*/

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace(); // Handle exception appropriately
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // Start the camera activity for result
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
*/
    // Register activity result launcher
/*private ActivityResultLauncher<Intent> ImageActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.get("data") != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        String imageBase64 = encodeBitmapToBase64(imageBitmap);
                        sendImageToAPI(imageBase64);

                    }
                }
            }*/
  /*  ActivityResultLauncher<Intent> ImageActivityResultLauncher = registerForActivityResult
            (
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result)
                        {
                            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                            {
                                Bundle extras = result.getData().getExtras();
                                if (extras != null && extras.get("data") != null)
                                {
                                    showProgressDialog("Uploading Image");
                                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                                    // String imageBase64 = encodeBitmapToBase64(imageBitmap);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                                    byte[] b = baos.toByteArray();
                                    String str_base64imagestring = Base64.encodeToString(b, Base64.DEFAULT);

                                    String attendanceMainId = attendance_main_id_str;

                                    arraylist_classroom_base64.clear();
                                    arraylist_classroom_base64.add(str_base64imagestring);
                                    Log.e("mainID",attendanceMainId);

                                    AsyncTask_studentImageUpload(attendanceMainId, str_base64imagestring);

                                }
                            }
                        }
                    });


*/
    // Define API service interface



 /*   private String encodeBitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }*/


    private void AsyncTask_studentImageUpload(String attendanceMainId,String imageBase64)
    {
        // Construct Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Class_URL.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service
        ApiService apiService = retrofit.create(ApiService.class);

        imageBase64=arraylist_classroom_base64.get(0);

        JsonObject postParam = new JsonObject();
        try{
            postParam.addProperty("AttendanceMain_Id", attendanceMainId);
            postParam.addProperty("ImageBase64format", imageBase64);
            Log.e("ImageBase64format", imageBase64);
        }catch(Exception ex)
        {
            Log.e("error", String.valueOf(ex));
        }


        Call<Void> call = apiService.Post_studentimages(postParam);


        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                dismissProgressDialog();
                Log.e("response",response.toString());
                if (response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    Trainee_summary_list();
                    //Updated_summary_list updated_summary_list = new Updated_summary_list();
                    if (screen_str.equals("completeclass")) {
                        completeclass_ConfirmDialog();
                    } else if (screen_str.equals("summarypopup")){
                        Updated_summary_list();
                        imageUpload_iv.setVisibility(View.GONE);
                    } else if (screen_str.equals("headpopup")) {
                        headcount_dialog.dismiss();
                        Updated_summary_list();
                        imageUpload_iv.setVisibility(View.GONE);
                    }
                    /*if (soapobject_recentattendance_response != null) {
                        if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                            //classstarted_bt.setEnabled(true);
                            //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        }
                        if (soapobject_recentattendance_response.getProperty("Message").toString().
                                equalsIgnoreCase("Success")) {
                            if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                    equalsIgnoreCase("Class Started") &&
                                    soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                            equalsIgnoreCase("1")) {
                                gpstracker_classcompleted = new GPSTracker(getActivity());
                                if (!camera_clicked) {
                                    if (gpstracker_classcompleted.canGetLocation()) {
                                        completeclass_ConfirmDialog();
                                    } else {
                                        gpstracker_classcompleted.showSettingsAlert();
                                    }
                                }
                            } else if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                    equalsIgnoreCase("Class Completed") &&
                                    soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                            equalsIgnoreCase("1")) {
                                if (isImageUpdated == 1){
                                    headcount_dialog.dismiss();
                                }
                                Updated_summary_list updated_summary_list = new Updated_summary_list();
                                updated_summary_list.execute();
                                imageUpload_iv.setVisibility(View.GONE);
                            }
                        }
                    }*/
                    //refresh the upadate summery list
                    // refreshSummaryList();
                } else {
                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "Timeout: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Erroqwerr", "SocketTimeoutException: " + t.getMessage(), t);


                } else {
                    Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Errorinapi", "General Exception: " + t.getMessage(), t);


                }
            }
        });
    }









/*
    private ActivityResultLauncher<Intent> ImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle extras = data.getExtras();
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            if (bitmap != null) {
                                // Convert bitmap to byte array
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] imageBytesArray = stream.toByteArray();

                                // Encode byte array to Base64 string
                                studPickBase64 = Base64.encodeToString(imageBytesArray, Base64.DEFAULT);
                                Log.d("studPickBase64:", studPickBase64);

                                // Get attendance ID
                                String attendanceMainId = "1135"; // Hardcoded attendance ID
                                new UploadImageTask().execute();
                            }
                        }
                    }
                }
            }
    );
*/

/*
    private class UploadImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            */
/*attendance_main_id_str = params[0];
            studPickBase64 = params[1];*//*

            String attendanceMainId = "1135"; // Hardcoded attendance ID
            String studPickBase641 = "R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

            String response = "";

            try {
                String apiUrl = "http://testingrdp.dfindia.org:9000/api/skillattendance/attendanceimagecapture";
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // Create JSON object for request body
                JSONObject requestBody = new JSONObject();
                requestBody.put("AttendanceMain_Id", attendanceMainId);
                requestBody.put("ImageBase64format", studPickBase641);
                Log.d("studPickBase641", studPickBase641);

                // Write JSON object to output stream
                OutputStream os = conn.getOutputStream();
                os.write(requestBody.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("UploadImageTask", "API Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                } else {
                    Log.e("UploadImageTask2", "Error: Unexpected response code - " + responseCode);
                }

                conn.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.e("UploadImageTask1", "Error: " + e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ", result);
            Log.d("result", result);

            if (result == null || result.isEmpty()) {
                // Notify user of error
                Toast.makeText(getActivity(), "Error uploading image", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void completeclass_ConfirmDialog() {
        if (completeClassDialog != null && completeClassDialog.isShowing()) {
            // The dialog is already being shown
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Complete Class");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.complete_class_popup, null);
        builder.setView(dialogView);

        reasonEditText = dialogView.findViewById(R.id.reason2hrLess_tv);
        reason_ll = dialogView.findViewById(R.id.reason_ll);

        LocalTime previousTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            previousTime = LocalTime.parse(classStartTime_str);
        }

        // Current time
        LocalTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
        }

        // Calculate time difference
        Duration timeDifference = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            timeDifference = Duration.between(previousTime, currentTime);
        }

        // Get time difference in seconds
        long secondsDifference = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            secondsDifference = timeDifference.getSeconds();
        }

        // Convert seconds to hours, minutes, and seconds
        long hours = secondsDifference / 3600;
        long minutes = (secondsDifference % 3600) / 60;
        long seconds = secondsDifference % 60;

        Log.e("Time difference: " , hours + " hours, " + minutes + " minutes, " + seconds + " seconds.");
        long totalMinutes = hours * 60 + minutes + (seconds >= 30 ? 1 : 0); // Round seconds to the nearest minute

        Log.e("min_timer", min_timer);
        String[] parts = min_timer.split(":");
        int mintime_hour = Integer.parseInt(parts[0]);
        int mintime_mins = Integer.parseInt(parts[1]);

        // Threshold duration: 1 hour and 50 minutes
        long thresholdMinutes = mintime_hour * 60 + mintime_mins;

        // Check if time difference is less than threshold
        if (totalMinutes < thresholdMinutes) {
            reason_ll.setVisibility(View.VISIBLE);
            builder.setMessage("Class Taken Duration is less than 1 hour 50 mins \n\nAre you sure you want to complete the class?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked Yes, execute the code to complete the class
                    if (TextUtils.isEmpty(reasonEditText.getText().toString().trim())) {
                        reasonEditText.setError("Please enter the Reason");
                        reasonEditText.requestFocus();
                        return;
                    } else {
                        Reason_str = reasonEditText.getText().toString();
                        completeClass();
                        startLocationUpdates();
                        student_lv.setEnabled(true);
                        boolean_signoutvalue = false;
                        boolean_signinvalue = false;
                        boolean_classstarted = false;
                        boolean_classcompleted = true;
                        pressBack_tv.setVisibility(View.GONE);
                        completeClassDialog = null;
                        college_name_Spinner.setEnabled(true);
                        ClassMode_spinner.setEnabled(true);
                        cohort_spinner.setEnabled(true);
                        subject_spinner.setEnabled(true);
                        Topic_spinner.setEnabled(true);
                        selectedsubtopics_et.setEnabled(true);
                        countDownTimer = null;
                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked No, do nothing
                    completeClassDialog = null;
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            completeClassDialog = builder.create(); // Create the dialog

            // Set the AlertDialog to be not cancelable when the user taps outside
            completeClassDialog.setCanceledOnTouchOutside(false);

            // Disable the "Submit" button initially
            completeClassDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    completeClassDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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
                    boolean isSubmitEnabled = !reason.isEmpty();
                    completeClassDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isSubmitEnabled);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            // Show the dialog
            completeClassDialog.show();
        } else {
            reason_ll.setVisibility(View.GONE);
            builder.setMessage("Are you sure you want to complete the class?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked Yes, execute the code to complete the class
                            //Reason_str = "''";
                            completeClass();
                            startLocationUpdates();
                            student_lv.setEnabled(true);
                            boolean_signoutvalue = false;
                            boolean_signinvalue = false;
                            boolean_classstarted = false;
                            boolean_classcompleted = true;
                            pressBack_tv.setVisibility(View.GONE);
                            completeClassDialog = null;
                            college_name_Spinner.setEnabled(true);
                            ClassMode_spinner.setEnabled(true);
                            cohort_spinner.setEnabled(true);
                            subject_spinner.setEnabled(true);
                            Topic_spinner.setEnabled(true);
                            selectedsubtopics_et.setEnabled(true);
                            imageUpload_iv.setVisibility(View.GONE);
                            countDownTimer = null;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked No, do nothing
                            completeClassDialog = null;
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

    }

    private void completeClass() {
        gpstracker_classcompleted = new GPSTracker(getActivity());

        if (is_attendance_completed_str.equals("0")) {
            AsyncCallWS_Attendance_complete();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the countdown timer_str
        }
        class_not_completed_tv.setVisibility(View.GONE);
        start_attendance_ll.setVisibility(View.GONE);
        student_not_assigned_tv.setVisibility(View.GONE);
        secret_key_cv.setVisibility(View.GONE);
        classscompleted_bt.setEnabled(false);
        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
        ClassMode_spinner.setEnabled(true);
        college_name_Spinner.setEnabled(true);
        cohort_spinner.setEnabled(true);
        subject_spinner.setEnabled(true);
        classstarted_bt.setEnabled(false);
        dateEdt_tv.setEnabled(true);
        Topic_spinner.setEnabled(true);
        selectedsubtopics_et.setEnabled(true);
        classstarted_bt.setEnabled(false);
        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
        start_attendance_btn.setEnabled(false);
        start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
            /*Update_student_list updateStudentList = new Update_student_list();
            updateStudentList.execute();*/
        System.out.println("MyAsyncTasks in complete class");
        class_typr_str = "Exists";
        if (isInternetPresent) {
            summaryHeading_tv.setText("Class Completed Summary");
            summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_500));
            Refresh_stuList_iv.setEnabled(false);
            AsyncCallWS_GetSecretKey asyncCallWSGetSecretKey = new AsyncCallWS_GetSecretKey();
            asyncCallWSGetSecretKey.execute();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setPositiveButton("OK", null)
                    .show();
        }
        // Add your code to refresh the UI or perform other actions
        // refreshUI(); // Custom method to handle UI refresh or other actions

    }


    private double getCurrentRadius() {
        SharedPreferences myprefs = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
        float savedRadius = myprefs.getFloat("currentRadius", -1f);
        double convertedRadius = (double) savedRadius;

        Log.d("CurrentRadius", "Value: " + convertedRadius);

        return convertedRadius;
    }

    private void refreshUI() {
        // Add your code here to refresh the UI or perform other actions
        // For example, you can update the spinner, reset text fields, etc.
        if (soapobject_recentattendance_response != null) {
            if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                ClassMode_spinner.setSelection(0);
                classmode_id_str = "0";
                ClassMode_spinner.setEnabled(true);
                college_name_Spinner.setSelection(0);
                college_name_Spinner.setEnabled(true);
                cohort_spinner.setSelection(0);
                cohort_id_str = "0";
                cohort_spinner.setEnabled(true);
                subject_spinner.setSelection(0);
                subject_id_str = "0";
                subject_spinner.setEnabled(true);
                Topic_spinner.setSelection(0);
                topic_id_str = "0";
                Topic_spinner.setEnabled(true);
                selectedsubtopics_et.setText("");
                selectedsubtopics_et.setEnabled(true);
                classtaken_ll.setVisibility(View.VISIBLE);
                classNottaken_ll.setVisibility(View.GONE);
                classstarted_bt.setEnabled(false);
                classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                classscompleted_bt.setEnabled(false);
                classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                start_attendance_btn.setEnabled(false);
                start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
            }

            if (soapobject_recentattendance_response.getProperty("Message").toString().
                    equalsIgnoreCase("Success")) {
                if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                        equalsIgnoreCase("Class Started") &&
                        soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                equalsIgnoreCase("0")) {
                    ClassMode_spinner.setSelection(0);
                    classmode_id_str = "0";
                    ClassMode_spinner.setEnabled(true);
                    college_name_Spinner.setSelection(0);
                    college_name_Spinner.setEnabled(true);
                    cohort_spinner.setSelection(0);
                    cohort_id_str = "0";
                    cohort_spinner.setEnabled(true);
                    subject_spinner.setSelection(0);
                    subject_id_str = "0";
                    subject_spinner.setEnabled(true);
                    Topic_spinner.setSelection(0);
                    topic_id_str = "0";
                    Topic_spinner.setEnabled(true);
                    selectedsubtopics_et.setText("");
                    selectedsubtopics_et.setEnabled(true);
                    classtaken_ll.setVisibility(View.VISIBLE);
                    classNottaken_ll.setVisibility(View.GONE);
                    classstarted_bt.setEnabled(false);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    classscompleted_bt.setEnabled(false);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    start_attendance_btn.setEnabled(true);
                    start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    imageUpload_iv.setVisibility(View.GONE);
                    //showToast("Complete the previous class to start next class");
                } else if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                        equalsIgnoreCase("Class Started") &&
                        soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                equalsIgnoreCase("1")) {
                    showToast("Complete the class to Refresh");
                    ClassMode_spinner.setEnabled(false);
                    college_name_Spinner.setEnabled(false);
                    cohort_spinner.setEnabled(false);
                    subject_spinner.setEnabled(false);
                    Topic_spinner.setEnabled(false);
                    selectedsubtopics_et.setEnabled(false);
                    classtaken_ll.setVisibility(View.VISIBLE);
                    classNottaken_ll.setVisibility(View.GONE);
                    classstarted_bt.setEnabled(false);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    classscompleted_bt.setEnabled(true);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    start_attendance_btn.setEnabled(false);
                    start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                } else if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                        equalsIgnoreCase("Class Completed") &&
                        soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                equalsIgnoreCase("1")) {
                    dateEdt_tv.setEnabled(true);
                    ClassMode_spinner.setSelection(0);
                    classmode_id_str = "0";
                    ClassMode_spinner.setEnabled(true);
                    college_name_Spinner.setSelection(0);
                    college_name_Spinner.setEnabled(true);
                    cohort_spinner.setSelection(0);
                    cohort_id_str = "0";
                    cohort_spinner.setEnabled(true);
                    subject_spinner.setSelection(0);
                    subject_id_str = "0";
                    subject_spinner.setEnabled(true);
                    Topic_spinner.setSelection(0);
                    topic_id_str = "0";
                    Topic_spinner.setEnabled(true);
                    selectedsubtopics_et.setText("");
                    selectedsubtopics_et.setEnabled(true);
                    classtaken_ll.setVisibility(View.VISIBLE);
                    classNottaken_ll.setVisibility(View.GONE);
                    classstarted_bt.setEnabled(false);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    classscompleted_bt.setEnabled(false);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    start_attendance_btn.setEnabled(false);
                    start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));

                }
            }
        }// Add more UI refresh actions as needed
    }

    @SuppressLint("NewApi")
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                //System.out.println("mCurrentLocation....inside activity.." + mCurrentLocation);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        //////probar.setVisibility(View.VISIBLE);
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                        System.out.println("in onSuccess...above stop updates..");
                        //stopLocationUpdates();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                showToast(errorMessage);
                                mRequestingLocationUpdates = false;
                        }

                        // updateLocationUI();
                    }
                });
    }

    private void updateLocationUI() {
        //System.out.println("entered  updateLocationUI()....");

        if (mCurrentLocation != null) {

            if (boolean_classstarted) {
                classscheduled_latitude = mCurrentLocation.getLatitude();
                classscheduled_longitude = mCurrentLocation.getLongitude();

                internetDectector = new ConnectionDetector(getActivity());
                isInternetPresent = internetDectector.isConnectingToInternet();

                if (isInternetPresent) {
                    AsyncCall_ClassStarted task = new AsyncCall_ClassStarted(context);
                    task.execute();
                } else {
                    showToast("No internet");
                }
            } else if (boolean_classcompleted) {

                classscheduled_latitude = mCurrentLocation.getLatitude();
                classscheduled_longitude = mCurrentLocation.getLongitude();

                internetDectector = new ConnectionDetector(getActivity());
                isInternetPresent = internetDectector.isConnectingToInternet();

                if (isInternetPresent) {
                    AsyncCall_ClassStarted task = new AsyncCall_ClassStarted(context);
                    task.execute();
                } else {
                    showToast("No internet");
                }
            }


        } else {
            //  Toast.makeText(getActivity(), "Try once again....Location=null", Toast.LENGTH_LONG).show();

        }
    }


    private class AsyncCall_ClassStarted extends AsyncTask<String, Void, Void> {
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

            if (boolean_classstarted) {
                Validate_classstarted();
            } else {
                Validate_classcompleted();
            }
            return null;
        }

        public AsyncCall_ClassStarted(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

            dialog.dismiss();

            if (str_classscheduled_response.equalsIgnoreCase("Attendance captured successfully")) {

                if (boolean_classstarted) {
                    classstarted_bt.setEnabled(false);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    boolean_classstarted = false;
                    //classscompleted_bt.setEnabled(true);
                    //classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    showToast("Class started Attendance captured");
                    AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
                    task1.execute();
                    Log.e("reading classid", "after class started");
                }
                if (boolean_classcompleted) {
                    //classstarted_bt.setEnabled(true);
                    //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    classscompleted_bt.setEnabled(false);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                    showToast("Class completed Attendance captured");
                    boolean_classcompleted = false;
                    str_classstarted_id = "0";
                    AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
                    task1.execute();
                }
            } else if (str_classscheduled_response.equalsIgnoreCase("location is not valid")) {
                boolean_classcompleted = false;
                boolean_classstarted = false;
                if (is_classCompletedClicked){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("You are not in Office Range")
                            .setPositiveButton("OK", null)
                            .show();
                    classscompleted_bt.setEnabled(true);
                    classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                }
            }

            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
    }

    private void Validate_classcompleted() {
//https://dfhrms.dfindia.org/pmsservice.asmx?op=MarkEndClassAttendance
        long long_userid = Long.parseLong(str_classstarted_id);
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);
        int classmode = Integer.parseInt(selected_classmodeId_str);
        String METHOD_NAME = "MarkEndClassAttendance_5_Version";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkEndClassAttendance_5_Version";

        String str_classstartedlatitude = String.valueOf(classscheduled_latitude);
        String str_classstartedlongitude = String.valueOf(classscheduled_longitude);

        try {
            Log.e("mark endclassattendance", String.valueOf(long_userid));
            Log.e("Id", String.valueOf(long_userid));
            Log.e("employee_id", String.valueOf(Employeeidlong));
            Log.e("date", currentdate_yyyyMMdd);
            Log.e("userId", String.valueOf(long_userid));
            Log.e("lat", str_classstartedlatitude);
            Log.e("lon", str_classstartedlongitude);
            Log.e("ClassMode", String.valueOf(classmode));

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("Id", long_userid);
            request.addProperty("employee_id", Employeeidlong);
            request.addProperty("date", currentdate_yyyyMMdd);
            request.addProperty("userId", long_userid);
            request.addProperty("lat", str_classstartedlatitude);
            request.addProperty("lon", str_classstartedlongitude);//ClassMode
            request.addProperty("ClassMode", classmode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            Log.e("request", String.valueOf(request));
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();


                str_classscheduled_response = response.toString().trim();

                Log.e("clsscheduledrespstarted", response.toString());
                //<string xmlns="http://tempuri.org/">Attendance captured successfully</string>
                classStarted_bool = false;
                classCompleted_bool = true;
                fetch_attendanceclasstaken();

            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_classscheduled_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_classscheduled_response = t.getMessage();

        }


    }

    private void Validate_classstarted(){

        long long_userid = 0;
        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);
        int classmode = Integer.parseInt(selected_classmodeId_str);
        String METHOD_NAME = "MarkClassStartAttendance_5_Version";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/MarkClassStartAttendance_5_Version";


        String str_classstartedlatitude = String.valueOf(classscheduled_latitude);
        String str_classstartedlongitude = String.valueOf(classscheduled_longitude);
        try {
            Log.e("mark start class attendance", String.valueOf(long_userid));
            Log.e("employee_id", String.valueOf(Employeeidlong));
            Log.e("date", currentdate_yyyyMMdd);
            Log.e("userId", String.valueOf(long_userid));
            Log.e("lat", str_classstartedlatitude);
            Log.e("lon", str_classstartedlongitude);
            Log.e("ClassMode", String.valueOf(classmode));
            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("employee_id", Employeeidlong);
            request.addProperty("date", currentdate_yyyyMMdd);
            request.addProperty("userId", long_userid);
            request.addProperty("lat", str_classstartedlatitude);
            request.addProperty("lon", str_classstartedlongitude);
            request.addProperty("ClassMode", classmode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();


                str_classscheduled_response = response.toString().trim();

                Log.e("clsscheduledrespcompleted", response.toString());
                //<string xmlns="http://tempuri.org/">Attendance captured successfully</string>

            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                str_classscheduled_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_classscheduled_response = t.getMessage();

        }


    }


    private class AsyncCall_Validate_ClassAttendance extends AsyncTask<String, Void, Void> {
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

            Validate_classattendance(); //check Whether class started punched or not
            return null;
        }

        public AsyncCall_Validate_ClassAttendance(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

            dialog.dismiss();


            if (soapobject_recentattendance_response != null) {
                if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                    //classstarted_bt.setEnabled(true);
                    //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    imageUpload_iv.setVisibility(View.GONE);
                }

                if (soapobject_recentattendance_response.getProperty("Message").toString().
                        equalsIgnoreCase("Success")) {
                    classStartTime_str = soapobject_recentattendance_response.getProperty("Intime").toString();
                    classEndTime_str = soapobject_recentattendance_response.getProperty("outtime").toString();
                    Log.e("Last InTime: " , classStartTime_str);
                    if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                            equalsIgnoreCase("Class Started") &&
                            soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                    equalsIgnoreCase("0")) {
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        start_attendance_btn.setEnabled(true);
                        imageUpload_iv.setVisibility(View.GONE);
                        start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        classscompleted_bt.setEnabled(false);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        college_name_Spinner.setEnabled(true);
                        ClassMode_spinner.setEnabled(true);
                        dateEdt_tv.setEnabled(true);
                        cohort_spinner.setEnabled(true);
                        subject_spinner.setEnabled(true);
                        Topic_spinner.setEnabled(true);
                        selectedsubtopics_et.setEnabled(true);

                        //showToast("Complete the previous class to start next class");

                        //class_not_completed_tv.setText("Complete the previous class to start next class");
                        str_classstarted_id = soapobject_recentattendance_response.getProperty("Id").toString();
                        Log.e("Class id=", str_classstarted_id);
                    } else if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                            equalsIgnoreCase("Class Started") &&
                            soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                    equalsIgnoreCase("1")) {
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        start_attendance_btn.setEnabled(false);
                        start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        classscompleted_bt.setEnabled(true);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        college_name_Spinner.setEnabled(false);
                        ClassMode_spinner.setEnabled(false);
                        dateEdt_tv.setEnabled(false);

                        if(imageUploadVisible == 1 && isImageUpdated ==0){
                            imageUpload_iv.setVisibility(View.VISIBLE);
                        } else {

                        }

                        if (imageUploadVisible == 1 && isImageUpdated == 1 && allowImageReupload ==1){
                            imageUpload_iv.setVisibility(View.VISIBLE);
                        }
                        else{
                        }

                        //showToast("Complete the previous class to start next class");
                        class_not_completed_tv.setVisibility(View.VISIBLE);
                        //start_attendance_ll.setVisibility(View.GONE);*/
                        str_classstarted_id = soapobject_recentattendance_response.getProperty("Id").toString();
                        Log.e("Class id=", str_classstarted_id);
                    }
                    // //anyType{Id=7; EmployeeId=896; LocationId=0; Intime=11:30:13; InLatitude=15.3710426;
                    // InLongitude=75.1236045; outtime=11:30:53; OutLatitude=15.3710326; OutLongitude=75.1235852;
                    // Attendance=Class Completed; Status=true; Message=Success; }
                    if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                            equalsIgnoreCase("Class Completed")) {
                        // classstarted_bt.setEnabled(true);
                        // classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        classscompleted_bt.setEnabled(false);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        str_classstarted_id = "0";
                        imageUpload_iv.setVisibility(View.GONE);
                        Log.e("classStarted_bool : " , String.valueOf(classStarted_bool));
                        Log.e("classCompleted_bool : " , String.valueOf(classCompleted_bool));
                        if (!classStarted_bool & classCompleted_bool) {
                            AsyncCallWS_AttendanceStartEndClass("0", classEndTime_str);
                        }
                    }

                }
            }

        }//end of onPostExecute

/*
        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();

            if (soapobject_recentattendance_response != null) {
                if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                    // Handle case when there are no records
                } else if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("Success")) {
                    String attendanceStatus = soapobject_recentattendance_response.getProperty("Attendance").toString();

                    if (attendanceStatus.equalsIgnoreCase("Class Started")) {
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        classscompleted_bt.setEnabled(true);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        str_classstarted_id = soapobject_recentattendance_response.getProperty("Id").toString();

                        // Show toast indicating that the class is already in progress
                        Toast.makeText(context, "Class is already in progress. Complete the current class first.", Toast.LENGTH_SHORT).show();
                    } else if (attendanceStatus.equalsIgnoreCase("Class Completed")) {
                        // Handle case when the class is already completed
                    }
                }
            }
        }
*/
    }

    private void Validate_classattendance() {

        Vector<SoapObject> result1 = null;

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "GetEmployeeRecentAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/GetEmployeeRecentAttendance";


        try {


            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("EmployeeId", Employeeidlong);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);

                //SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                SoapObject response = (SoapObject) envelope.getResponse();
                // str_validateAttendance_response = response.toString().trim();

                soapobject_recentattendance_response = response;

                Log.e("Validateclass resp", response.toString());
                //anyType{Id=0; EmployeeId=0; LocationId=0; Status=false; Message=There are no records; }

                if (response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                    /*classstarted_bt.setEnabled(true);
                    classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));*/
                }


            } catch (Throwable t) {
                //Toast.makeText(login.this, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                // str_validateAttendance_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            //str_validateAttendance_response = t.getMessage();

        }


    }
    private void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("Datewise Attendance History");
        dialog.setContentView(R.layout.trainersummary_popup);
        update_summary_list_iv = dialog.findViewById(R.id.update_summary_list_iv);
        no_summaryvalues_tv = dialog.findViewById(R.id.no_values_tv);
        date_trainer_summary_tv = dialog.findViewById(R.id.date_trainer_summary);
        summary_lv = dialog.findViewById(R.id.attendance_summary_list);
        String outputDate = convertDateFormat(datevalue_str);
        date_trainer_summary_tv.setText(outputDate);
        System.out.println("summary_lv"+summary_lv);
        if (isInternetPresent) {
            Updated_summary_list();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setPositiveButton("OK", null)
                    .show();
        }

        update_summary_list_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_summary_list_iv.setEnabled(false);
                Updated_summary_list();
            }
        });

        Button closeDialogButton = dialog.findViewById(R.id.closeDialogButton);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the close button is clicked
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    public class AsyncTask_GPSFetching extends AsyncTask<String, Void, Void>
    {
        ProgressDialog dialog;
        private double currentRadius;


        Context context;

        protected void onPreExecute() {
            //  Log.i(TAG, "onPreExecute---tab2");
            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            //  dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.i(TAG, "onProgressUpdate---tab2");
        }


        @Override
        protected Void doInBackground(String... params)
        {
            Log.i("DFTech", "doInBackground");
            gpstracker_obj.canGetLocation();
            return null;
        }

        public AsyncTask_GPSFetching(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

/*
        @Override
        protected void onPostExecute(Void result) {



            dialog.dismiss();



            if(gpstracker_obj.canGetLocation())
            {
                currentlatitude = gpstracker_obj.getLatitude();
                currentlongitude = gpstracker_obj.getLongitude();
             if (currentlatitude.equals(0.0) || currentlongitude.equals(0.0)) {


                    //Toast.makeText(StudentOTP.this, "Zero Location is - \nLat: " + Double.toString(currentlatitude)+"\n Long:"+Double.toString(currentlongitude), Toast.LENGTH_LONG).show();
                } else {
                    showToast("Your Location is - \nLat: " + Double.toString(currentlatitude) + "\n Long:" + Double.toString(currentlongitude));

                }
            }else {
                showToast("GPS Location turned off");
            }
            //subject_SM.clear();
            if (!college_selected_str.equals("Select")){
                fetchJSON(college_selected_str);
            }

            // System.out.println("Reached the onPostExecute");

        }//end of onPostExecute
*/

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();

            if (gpstracker_obj.canGetLocation()) {
                currentlatitude = gpstracker_obj.getLatitude();
                currentlongitude = gpstracker_obj.getLongitude();

                if (currentlatitude != 0.0 && currentlongitude != 0.0) {
                    //showToast("Your Location is - \nLat: " + currentlatitude + "\n Long:" + currentlongitude);

                    AsyncTask_DCheckSubjectWiseClassAttendance task = new AsyncTask_DCheckSubjectWiseClassAttendance(getActivity());
                    task.execute();

                    // Store the current latitude and longitude in SharedPreferences
                    // saveCurrentLocation(currentlatitude, currentlongitude);
                }
            } else {
                showToast("GPS Location turned off");
            }

/*
            if (!college_selected_str.equals("Select")) {
                fetchJSON(college_selected_str);
            }
*/
        }


    }
    @SuppressLint("StaticFieldLeak")
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
                    fetch_attendanceclasstaken();
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

    private void fetchJSON(String college_selected) {
        Log.e("API college", college_selected);
        showProgressDialog("Fetching values");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CohSub_SpinnerInterface.CohSub_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CohSub_SpinnerInterface api = retrofit.create(CohSub_SpinnerInterface.class);

        // Call<String> call = api.getJSONString(college_selected_str, emp_id);

        Call<String> call = api.getJSONString(college_selected,emp_id);
        Log.e("call",call.request().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonresponse = response.body();
                        spinJSON(jsonresponse);
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
    private void spinJSON(String response){

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                cohort_ArrayList.clear();
                subject_ArrayList.clear();
                ClassMode_ArrayList.clear();
                ClassMode_SM.clear();
                cohort_SM.clear();
                subject_SM.clear();
                Studentlist.clear();
                classstarted_bt.setEnabled(false);
                classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                classscompleted_bt.setEnabled(false);
                classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                if (soapobject_recentattendance_response != null) {
                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                        //classstarted_bt.setEnabled(true);
                        //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    }

                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                            equalsIgnoreCase("Success")) {
                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                equalsIgnoreCase("Class Started") &&
                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                        equalsIgnoreCase("0")) {
                            start_attendance_btn.setEnabled(true);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            classscompleted_bt.setEnabled(false);
                            classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            imageUpload_iv.setVisibility(View.GONE);
                        } else {
                            start_attendance_btn.setEnabled(false);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        }
                    }
                }
                JSONObject cohortsArray1 = obj.getJSONObject("vmCohortSubject");//getJSONArray("cohorts");
                if (cohortsArray1.getString("cohorts").equals("null")) {
                    // If cohortsArray is null or empty, set the spinner value to "No cohorts assigned"
                    List<String> list = new ArrayList<>();
                    list.add("No cohorts assigned");
                    ClassMode_SM.clear();
                    subject_SM.clear();
                    start_attendance_ll.setVisibility(View.GONE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                    cohort_spinner.setAdapter(adapter);
                } else {
                    JSONArray cohortsArray = cohortsArray1.getJSONArray("cohorts");
                    JSONArray subjectsArray = obj.getJSONObject("vmCohortSubject").getJSONArray("subjects");
                    JSONArray modeArray = cohortsArray1.getJSONArray("classMode");
                    JSONArray configurablesArray = cohortsArray1.getJSONArray("configurables");
                    System.out.println("modeArray"+ modeArray);
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
                    subject_SM.clear();
                    subject_ArrayList.add("Select");
                    for (int i = 0; i < subjectsArray.length(); i++) {

                        CohSub_SM spinnerModel = new CohSub_SM();
                        JSONObject dataobj = subjectsArray.getJSONObject(i);

                        spinnerModel.setSubject(dataobj.getString("subectName"));
                        spinnerModel.setSubjectId(dataobj.getString("subjectId"));
                        subject_SM.add(spinnerModel);

                    }
                    for (int i = 0; i < subject_SM.size(); i++){
                        subject_ArrayList.add(subject_SM.get(i).getSubject().toString());
                    }

                    ClassMode_SM.clear();
                    ClassMode_ArrayList.add("Select");
                    for (int i = 0; i < modeArray.length(); i++) {

                        CohSub_SM spinnerModel = new CohSub_SM();
                        JSONObject dataobj = modeArray.getJSONObject(i);
                        System.out.println(dataobj.getString("classMode_Name"));
                        spinnerModel.setClassMode_Name(dataobj.getString("classMode_Name"));
                        spinnerModel.setClassMode_Type(dataobj.getString("classMode_Type"));
                        spinnerModel.setClassMode_Id(dataobj.getString("classMode_Id"));
                        ClassMode_SM.add(spinnerModel);
                    }

                    for (int i = 0; i < configurablesArray.length(); i++) {
                        JSONObject configurable = configurablesArray.getJSONObject(i);
                        //  imageUploadVisible = configurable.getString("imageUploadVisible");
                        imageUploadVisible = configurable.getInt("imageUploadVisible");
                        imageUploadMandatory = configurable.getInt("imageUploadMandatory");
                        allowImageReupload = configurable.getInt("allowImageReupload");

                        // Log or use these values as needed
                        Log.d("Configurable", "imageUploadVisible: " + imageUploadVisible);
                        Log.d("Configurable", "imageUploadMandatory: " + imageUploadMandatory);
                        Log.d("Configurable", "allowImageReupload: " + allowImageReupload);

                        // Check if image upload is visible


/*
                    if (isImageUpdated == 1 && allowImageReupload == 1) {
                        imageUpload_iv.setVisibility(View.VISIBLE);
                        // Do something if image upload is visible
                        Log.d("Configurable", "Image upload is visible.");
                    } else {
                        imageUpload_iv.setVisibility(View.GONE);
                        // Handle case where image upload is not visible
                        Log.d("Configurable", "Image upload is not visible.");
                    }*/


                    }

                    System.out.println("ClassMode_SM"+ClassMode_SM.size());
                    for (int i = 0; i < ClassMode_SM.size(); i++){
                        ClassMode_ArrayList.add(ClassMode_SM.get(i).getClassMode_Name().toString());
                    }

                    ArrayAdapter<String> classmode_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, ClassMode_ArrayList);
                    classmode_adapter.setDropDownViewResource(R.layout.spinner_dropdown); // The drop down view
                    ClassMode_spinner.setAdapter(classmode_adapter);
                    ArrayAdapter<String> cohort_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, cohort_ArrayList);
                    cohort_adapter.setDropDownViewResource(R.layout.spinner_dropdown); // The drop down view
                    cohort_spinner.setAdapter(cohort_adapter);
                    ArrayAdapter<String> subject_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, subject_ArrayList);
                    subject_adapter.setDropDownViewResource(R.layout.spinner_dropdown); // The drop down view
                    subject_spinner.setAdapter(subject_adapter);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clearTopicListAndSetDefault() {
        Topic_ArrayList.clear();
        Topic_SM.clear();
        List<String> defaultTopic = new ArrayList<>();
        defaultTopic.add("No Topic Assigned");
        Topic_ArrayList.addAll(defaultTopic);

        // Set the default subtopic_adapter for Topic_spinner
        ArrayAdapter<String> Topic_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text, Topic_ArrayList);
        Topic_adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Topic_spinner.setAdapter(Topic_adapter);
    }

    private void clearCheckedItems() {
        if (checkedItems_set != null) {
            checkedItems_set.clear();
        }
    }

    public class AsyncCallWS_GetSecretKey extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Refresh_stuList_iv.setEnabled(false);
            String result = "";
            fetch_attendanceclasstaken();
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    currentlatitude = Double.valueOf(passed_lat);
                    currentlongitude = Double.valueOf(passed_long);

                    Log.e("currentlatitude3", String.valueOf(currentlatitude));
                    Log.e("currentlongitude3", String.valueOf(currentlongitude));
                    if (gpstracker_obj.canGetLocation()) {
                        currentlatitude = gpstracker_obj.getLatitude();
                        currentlongitude = gpstracker_obj.getLongitude();
                    }


                    if (selected_classmodeId_str.equals("3")){
                        if(college_selected_str.contains("&")) {
                            try {
                                college_selected_str = URLEncoder.encode(college_selected_str, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        class_typr_str = "New";
                    }
                    //http://testingrdp.dfindia.org:9000/api/skillattendance/
                    // getsecretkey?Employee_Id=
                    url = new URL(Class_URL.API_URL+"getsecretkey?Employee_Id="+ emp_id+"&CollegeName="+ college_selected_str +"&Cohort="+
                            cohort_selected_str + "&SubjectId="+ selectedSubjectId_str +"&SubjectName="+
                            subject_selected_str +"&Date="+ datevalue_str + "&Latitude="+currentlatitude+
                            "&Longitude="+currentlongitude+"&MainTopicId="+ selectedTopicId_str +
                            "&MainTopicName="+ topic_selected_str +"&SubTopicJson="+ subtopic_jsonResult_str+
                            "&ClassMode_Id="+ selected_classmodeId_str+ "&ClassType="+class_typr_str);
                    System.out.println("myurl MyAsyncTasks: "+url);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getContext(),"URL "+url.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog("Fetching class Details");
                }
            });
        }
        @Override
        protected void onPostExecute(String s) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                }
            });

            if (selected_classmodeId_str.equals("3")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                    attendance_main_id_str = GetSecretKey_Response.getString("attendanceMainId");
                    Log.e("Actual ID:", attendance_main_id_str);
                    AsyncCallWS_Save_classnottaken_Reason();
                    showToast("Class Not Taken Updated");
                    refreshUI();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    //present_percent_tv.setText(presentPercent.)
                    class_typr_str = "Exists";
                    JSONObject jsonObject = new JSONObject(s);
                    //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                    String status = jsonObject.getString("status");
                    if (status.equals("false")) {
                        imageUpload_iv.setVisibility(View.GONE);
                        student_not_assigned_tv.setVisibility(View.VISIBLE);
                        start_attendance_btn.setEnabled(true);
                        start_attendance_btn.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        classscompleted_bt.setEnabled(false);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        start_attendance_ll.setVisibility(View.GONE);
                    } else {
                        student_not_assigned_tv.setVisibility(View.GONE);
                    }
                    //summaryHeading_tv.setText("Class Completed Summary");
                    //summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_500));
                    System.out.println("Status" + status);
                    GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                    attendance_main_id_str = GetSecretKey_Response.getString("attendanceMainId");
                    System.out.println("attendanceMainId" + attendance_main_id_str);
                    AsyncCallWS_AttendanceStartEndClass(classStartTime_str, "0");
                    Log.e("classStarted_bool", String.valueOf(classStarted_bool));
                    Log.e("classCompleted_bool", String.valueOf(classCompleted_bool));

                    is_attendance_completed_str = GetSecretKey_Response.getString("isAttendance_Completed");
                    System.out.println("is_attendance_completed_str in myasynctask" + is_attendance_completed_str);
                    selectedsubtopics_et.setText("");
                    if (is_attendance_completed_str.equals("0")) {
                        /*Topic_spinner.setEnabled(true);
                        selectedsubtopics_et.setEnabled(true);*/
                        //fetch_Topic(selectedSubjectId_str);
                        start_attendance_btn.setEnabled(false);
                        start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        classscompleted_bt.setEnabled(true);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        start_attendance_ll.setVisibility(View.VISIBLE);
                        secret_key_cv.setVisibility(View.VISIBLE);
                        JSONArray selected_subTopicArray = GetSecretKey_Response.getJSONArray("subTopicList");
                        System.out.println("selected_subTopicArray" + selected_subTopicArray);
                        System.out.println("topic list" + Topic_ArrayList);
                        mainTopic_Id_str = GetSecretKey_Response.getString("mainTopic_Id");
                        System.out.println("mainTopic_Id_str" + mainTopic_Id_str);
                        // Clear the list before adding new values
                        selected_subTopicList.clear();

                        // Iterate through the subTopicArray and create instances of SubTopic_class
                        for (int i = 0; i < selected_subTopicArray.length(); i++) {
                            JSONObject subTopicObj = selected_subTopicArray.getJSONObject(i);
                            String sel_subTopicId = subTopicObj.getString("subTopic_Id");
                            String sel_subTopicName = subTopicObj.getString("subTopic_Name");
                            // Create an instance of SubTopic_class and add it to the list
                            SubTopic_SM sel_subTopic = new SubTopic_SM(sel_subTopicId, sel_subTopicName);
                            selected_subTopicList.add(sel_subTopic);
                        }
                        System.out.println("selected_subTopicList in 0" + selected_subTopicList);
                        //selected_subTopicList.stream().map(SubTopic_class::getSubTopicName).collect(Collectors.toCollection(ArrayList::new));
                        List<String> subTopicNameList = selected_subTopicList.stream()
                                .map(SubTopic_SM::getSubTopicName)
                                .collect(Collectors.toList());

                        System.out.println("subTopicNameList" + subTopicNameList);

                        // Update the EditText with selected topic names
                        if (selectedsubtopics_et != null) {
                            StringBuilder selectedValuesText = new StringBuilder();
                            for (String selectedTopic : subTopicNameList) {
                                selectedValuesText.append(selectedTopic).append(", ");
                            }
                            if (selectedValuesText.length() > 0) {
                                selectedValuesText.delete(selectedValuesText.length() - 2, selectedValuesText.length());
                            }

                            selectedsubtopics_et.setText(selectedValuesText.toString());
                            //Gson gson = new Gson();
                            //String subtopic_jsonResult_str = gson.toJson(selectedsubtopics);
                            subtopic_jsonResult_str = new Gson().toJson(subTopicNameList);
                            // Log the JSON result
                            Log.d("SelectedPersonsJSON", "Selected Persons JSON in 0: " + subtopic_jsonResult_str);
                            Log.d("SelectedPersonsJSON", subtopic_jsonResult_str);
                        }
                        //selectedsubtopics_et.setText();
                        timer_str = GetSecretKey_Response.getString("ticTacTimer");
                        //timer_str = "20";
                        timer_in_mili = Integer.parseInt(timer_str) * 1000;
                        System.out.println("Exists timer_str has started");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Exists timer_str value : " + countDownTimer);
                                System.out.println("Exists timer_in_mili value : " + timer_in_mili);
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                countDownTimer = startCountdownTimer(timer_in_mili);
                                countDownTimer.start();
                            }
                        }, 1000);
                    } else {
                        previous_date_tv.setVisibility(View.GONE);
                        showToast("Attendance already completed");
                        System.out.println("boolean_classstarted" + boolean_classstarted);
                        start_attendance_ll.setVisibility(View.VISIBLE);
                        if (soapobject_recentattendance_response != null) {
                            if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                                //classstarted_bt.setEnabled(true);
                                //classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            }

                            if (soapobject_recentattendance_response.getProperty("Message").toString().
                                    equalsIgnoreCase("Success")) {
                                if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                        equalsIgnoreCase("Class Started") &&
                                        soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                                equalsIgnoreCase("1")) {
                                    dateEdt_tv.setEnabled(false);
                                    college_name_Spinner.setEnabled(false);
                                    ClassMode_spinner.setEnabled(false);
                                    cohort_spinner.setEnabled(false);
                                    subject_spinner.setEnabled(false);
                                    Topic_spinner.setEnabled(false);
                                    selectedsubtopics_et.setEnabled(false);
                                } else {
                                    dateEdt_tv.setEnabled(true);
                                    college_name_Spinner.setEnabled(true);
                                    ClassMode_spinner.setEnabled(true);
                                    cohort_spinner.setEnabled(true);
                                    subject_spinner.setEnabled(true);
                                    Topic_spinner.setEnabled(true);
                                    selectedsubtopics_et.setEnabled(true);
                                }
                            }
                        }
                       /* classscompleted_bt.setEnabled(false);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));*/
                        //fetch_Topic(selectedSubjectId_str);
                        selected_subTopicList.clear();
                        JSONArray selected_subTopicArray = GetSecretKey_Response.getJSONArray("subTopicList");
                        System.out.println("selected_subTopicArray when exists" + selected_subTopicArray);
                        System.out.println("topic list" + Topic_ArrayList);
                        mainTopic_Id_str = GetSecretKey_Response.getString("mainTopic_Id");
                        System.out.println("mainTopic_Id_str" + mainTopic_Id_str);
                        System.out.println("length : " + selected_subTopicArray.length());
                        for (int i = 0; i < selected_subTopicArray.length(); i++) {
                            JSONObject subTopicObj = selected_subTopicArray.getJSONObject(i);
                            String sel_subTopicId = subTopicObj.getString("subTopic_Id");
                            String sel_subTopicName = subTopicObj.getString("subTopic_Name");
                            // Create an instance of SubTopic_class and add it to the list
                            SubTopic_SM sel_subTopic = new SubTopic_SM(sel_subTopicId, sel_subTopicName);
                            selected_subTopicList.add(sel_subTopic);
                        }
                        System.out.println("selected_subTopicList outside 0" + selected_subTopicList);
                        //selected_subTopicList.stream().map(SubTopic_class::getSubTopicName).collect(Collectors.toCollection(ArrayList::new));
                        List<String> subTopicNameList = selected_subTopicList.stream()
                                .map(SubTopic_SM::getSubTopicName)
                                .collect(Collectors.toList());

                        System.out.println("subTopicNameList" + subTopicNameList);
                      /*  List<SubTopic_class> selectedsubtopics = selected_subTopicList.stream()
                                .filter(subtopic -> newCheckedItems_list.contains(subtopic.getSubTopicName()))
                                .collect(Collectors.toList());

                        // Extract selected topic names
                        List<String> selectedTopicNames = selectedsubtopics.stream()
                                .map(SubTopic_class::getSubTopicName)
                                .collect(Collectors.toList());*/

                        // Update the EditText with selected topic names
                        if (selectedsubtopics_et != null) {
                            StringBuilder selectedValuesText = new StringBuilder();
                            for (String selectedTopic : subTopicNameList) {
                                selectedValuesText.append(selectedTopic).append(", ");
                            }
                            if (selectedValuesText.length() > 0) {
                                selectedValuesText.delete(selectedValuesText.length() - 2, selectedValuesText.length());
                            }
                            selectedsubtopics_et.setText("");
                            if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                    equalsIgnoreCase("Class Started") &&
                                    soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                            equalsIgnoreCase("0")) {
                                selectedsubtopics_et.setText("");
                            } else {
                                selectedsubtopics_et.setText(selectedValuesText.toString());
                            }
                            //Gson gson = new Gson();
                            //String subtopic_jsonResult_str = gson.toJson(selectedsubtopics);
                            subtopic_jsonResult_str = new Gson().toJson(subTopicNameList);
                            // Log the JSON result
                            Log.d("SelectedPersonsJSON", "Selected Persons JSON in 1: " + subtopic_jsonResult_str);
                        }
                    }

                    JSONArray summary_array = GetSecretKey_Response.getJSONArray("employeeAttendanceSummary");
                    Summarylist.clear();
                    for (int i = 0; i < summary_array.length(); i++) {
                        JSONObject obj = summary_array.getJSONObject(i);//sets to custom position
                        String collegeName = obj.getString("collegeName");
                        String semesterName = obj.getString("semesterName");
                        String subjectName = obj.getString("subjectName");
                        String attendanceStartDate = obj.getString("attendanceStartDate");
                        String attendanceEndDate = obj.getString("attendanceEndDate");
                        String totalStudents = obj.getString("totalStudents");
                        String totalPresent = obj.getString("totalPresent");
                        String totalAbsent = obj.getString("totalAbsent");


                        HashMap<String, String> summary_map = new HashMap();
                        Summarylist.add(summary_map);
                        summary_map.put("collegeName", collegeName);
                        summary_map.put("semesterName", semesterName);
                        summary_map.put("subjectName", subjectName);
                        summary_map.put("attendanceStartDate", attendanceStartDate);
                        summary_map.put("attendanceEndDate", attendanceEndDate);
                        summary_map.put("totalStudents", totalStudents);
                        summary_map.put("totalPresent", totalPresent);
                        summary_map.put("totalAbsent", totalAbsent);
                    }
                    //JSONObject val1summary_array = summary_array.getJSONObject(0);
                    //System.out.println("jsonArray"+jsonArray);

                    updateStudentList(Studentlist);
               /* subtopic_adapter = new SimpleAdapter(getActivity(),Studentlist,R.layout.student_list,
                        new String[]{"name", "switch"},//use same id here, that you used in map, case sensitive
                        new int[]{R.id.textView1, R.id.checkBox1});
                //use same path here, id for id, name for name
                student_lv.setAdapter(subtopic_adapter);*/

                    super.onPostExecute(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dismissProgressDialog();
            Refresh_stuList_iv.setEnabled(true);
        }
        public void updateStudentList(List<HashMap<String, Object>> updatedList) throws JSONException {
            Studentlist.clear();
            GetSecretKey_studentlist_Response = GetSecretKey_Response.getJSONArray("studentList");
            for (int i = 0; i < GetSecretKey_studentlist_Response.length(); i++) {
                JSONObject obj = GetSecretKey_studentlist_Response.getJSONObject(i);
                String name = obj.getString("studentName");
                String app_no = obj.getString("applicationNo");
                String mobile_no = obj.getString("mobileNo");
                String reasonForAbsent_Id = obj.getString("reasonForAbsent_Id");
                attendance_status_str = obj.getString("attendanceStatus");
                HashMap<String,Object> map = new HashMap<>();
                Studentlist.add(map);
                map.put("name", name);
                map.put("app_no", app_no);
                map.put("mobile_no", mobile_no);
                map.put("reasonForAbsent_Id", reasonForAbsent_Id);
                isPresent_bool = attendance_status_str.equals("Present");
                System.out.println("isPresent_bool"+ isPresent_bool);
                map.put("switch", isPresent_bool);
                // Create and add the switch to the layout here
                Switch mySwitch = (Switch) LayoutInflater.from(getContext()).inflate(R.layout.custom_switch_layout, null);
                mySwitch.setId(i); // Set a unique ID for each switch

                // Check isPresent_bool and update switch colors accordingly
                if (isPresent_bool) {
                    mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.greenTextcolor)));
                    mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
                } else {
                    mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorred)));
                    mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.lightred)));
                }
            }

            String key = GetSecretKey_Response.getString("secretKey");
            String total = GetSecretKey_Response.getString("studentTotalCount");
            String present = GetSecretKey_Response.getString("studentPresentCount");
            String absent = GetSecretKey_Response.getString("studentAbsentCount");
            String secret_key = key;

            int present_count = Integer.parseInt(present);
            int total_count = Integer.parseInt(total);
            int absent_Count = Integer.parseInt(absent);

            double presentPercent = (present_count * 100.0) / total_count;
            double absentPercent = (absent_Count * 100.0) / total_count;
            secret_key_tv.setText(secret_key);
            total_count_tv.setText(total);
            present_count_tv.setText(present);
            absent_count_tv.setText(absent);
            present_percent_tv.setText(String.format("%.0f %%", presentPercent));
            absent_percent_tv.setText(String.format("%.0f %%", absentPercent));

            System.out.println("is_attendance_completed_str"+ is_attendance_completed_str);
            if (is_attendance_completed_str.equals("0")) {
                studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                        new String[]{"name", "app_no", "mobile_no", "switch"},
                        new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1});
                student_lv.setAdapter(studentlist_adapter);
                student_lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        showToast("Cannot Mark Attendance until class is completed");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                System.out.println("0 is executed");
            } else {
                System.out.println("1 is executed");
                boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                System.out.println("selected date"+ datevalue_str);
                System.out.println("datevalue_str : " + datevalue_str + isBefore);
                studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                        new String[]{"name", "app_no", "mobile_no", "switch"},
                        new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1})
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        String reasonForAbsent_Id = (String) updatedList.get(position).get("reasonForAbsent_Id");
                        // Find the relevant views
                        TextView student_name_tv = view.findViewById(R.id.student_name_tv);
                        TextView application_no_tv = view.findViewById(R.id.application_no_tv);
                        TextView mobile_no_tv = view.findViewById(R.id.mobile_no_tv);

                        // Reset the views to their default state
                        student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

                        // Apply conditional styling
                        if (!reasonForAbsent_Id.equals("0")) {
                            student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                        }

                        return view;
                    }
                };

                student_lv.setAdapter(studentlist_adapter);
                student_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        student_lv.setEnabled(false);
                        if (!isBefore){
                            if (countDownTimer == null){
                                HashMap<String, Object> clickedItem = updatedList.get(position);
                                boolean isPresent = (boolean) clickedItem.get("switch");

                                if (!isPresent) {
                                    // Extract necessary information from the clicked item
                                    clicked_Student_name_str = (String) clickedItem.get("name");
                                    clicked_Student_Appno_str = (String) clickedItem.get("app_no");
                                    clicked_Student_Mobno_str = (String) clickedItem.get("mobile_no");
                                    clicked_student_reasonID_str = (String) clickedItem.get("reasonForAbsent_Id");

                                    System.out.println("onclick in Async task is executed");
                                    // Show popup for items where isPresent_bool is false
                                    System.out.println("onclick listener for student_lv is called: " +
                                            clicked_Student_name_str + " " + clicked_Student_Appno_str + " " + clicked_Student_Mobno_str);
                                    //show_OTP_Popup();
                                    show_OTP_Popup();
                                } else {
                                    showToast("Cannot mark manual attendance for present student");
                                }
                            } else {
                                showToast("Complete the ongoing class");
                            }
                        } else {
                            showToast("Cannot mark attendance for Previous dates");
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                student_lv.setEnabled(true);
                            }
                        }, 1000);
                    }
                });
            }

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

    private CountDownTimer startCountdownTimer(long timerInMillis) {
        updateTimerDisplay(timerInMillis);
        return new CountDownTimer(timerInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                System.out.println("New timer_str count down timer_str");
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timer_count_tv.setText(f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                // Actions to perform when the timer_str finishes
                secret_key_cv.setVisibility(View.GONE);
                summaryHeading_tv.setText("Class Completed Summary");
                summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_500));
                AsyncCallWS_Attendance_complete();
                System.out.println("MyAsyncTasks in onfinish");
                class_typr_str = "Exists";
                if (isInternetPresent) {
                    student_lv.setEnabled(true);
                    AsyncCallWS_GetSecretKey asyncCallWSGetSecretKey = new AsyncCallWS_GetSecretKey();
                    asyncCallWSGetSecretKey.execute();
                    college_name_Spinner.setEnabled(false);
                    ClassMode_spinner.setEnabled(false);
                    cohort_spinner.setEnabled(false);
                    subject_spinner.setEnabled(false);
                    Topic_spinner.setEnabled(false);
                    selectedsubtopics_et.setEnabled(false);
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("No Internet Connection")
                            .setPositiveButton("OK", null)
                            .show();
                }
                countDownTimer.cancel();
                countDownTimer = null;
            }
        };
    }

    private void updateTimerDisplay(long millisUntilFinished) {
        NumberFormat f = new DecimalFormat("00");
        long hour = (millisUntilFinished / 3600000) % 24;
        long min = (millisUntilFinished / 60000) % 60;
        long sec = (millisUntilFinished / 1000) % 60;
        timer_count_tv.setText(f.format(min) + ":" + f.format(sec));
    }

    private class AsyncTask_DCheckSubjectWiseClassAttendance extends AsyncTask<String, Void, Void> {
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

            CheckSubjectWiseClassAttendance(); //Mark the employee IN time


            return null;
        }

        public AsyncTask_DCheckSubjectWiseClassAttendance(Context context1) {
            context = context1;
            dialog = new ProgressDialog(context1);
        }

        @Override
        protected void onPostExecute(Void result) {

            if ((this.dialog != null) && this.dialog.isShowing()) {
                dialog.dismiss();

            }


            dialog.dismiss();

        }//end of onPostExecute
    }// end Async task




    public void CheckSubjectWiseClassAttendance() {


        String str_currentlatitude = passed_lat;
        String str_currentlongitude = passed_long;
        Log.d("str_currentlatitude", "currentlatitude: " + str_currentlatitude);

        //String URL = "http://dfhrms.cloudapp.net/PMSservice.asmx?WSDL";
        String URL = getResources().getString(R.string.main_url);

        String METHOD_NAME = "CheckSubjectWiseClassAttendance";//
        String Namespace = "http://tempuri.org/", SOAPACTION = "http://tempuri.org/CheckSubjectWiseClassAttendance";
        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("employee_id", emp_id);
            request.addProperty("Employeelat", str_currentlatitude);
            request.addProperty("Employeelon", str_currentlongitude);
            request.addProperty("Collegelat", selectedLatitude);
            request.addProperty("Collegelon", selectedLongitude);
            request.addProperty("Radius", selectedRadius);

            Log.e("CheckSubjectWiseClassAttendance", emp_id);
            Log.d("SOAP_Request", "employee_id: " + emp_id);
            Log.d("SOAP_Request", "Employeelat: " + str_currentlatitude);
            Log.d("SOAP_Request", "Employeelon: " + str_currentlongitude);
            Log.d("SOAP_Request", "Collegelat: " + selectedLatitude);
            Log.d("SOAP_Request", "Collegelon: " + selectedLongitude);
            Log.d("SOAP_Request", "Radius: " + selectedRadius);

            Log.e("Respose class", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAPACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                str_signin_response = response.toString().trim();
                Log.e("Signin resp: ", response.toString());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                        System.out.println("selected date"+ datevalue_str);
                        if (isBefore){
                            previous_date_tv.setVisibility(View.VISIBLE);
                            System.out.println("Selected Previous date");
                            classstarted_bt.setEnabled(false);
                            classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            classscompleted_bt.setEnabled(false);
                            classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            start_attendance_btn.setEnabled(false);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        } else {
                            previous_date_tv.setVisibility(View.GONE);
                            System.out.println("Selected Current date");
                            if (str_signin_response.equals("Valid")) {
                                out_of_office_tv.setVisibility(View.GONE);
                                if (soapobject_recentattendance_response != null) {
                                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                                        classstarted_bt.setEnabled(true);
                                        classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                    }
                                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                                            equalsIgnoreCase("Success")) {
                                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                                equalsIgnoreCase("Class Started") &&
                                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                                        equalsIgnoreCase("0")) {
                                            classstarted_bt.setEnabled(false);
                                            classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                                        } else {
                                            classstarted_bt.setEnabled(true);
                                            classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                        }
                                    }
                                }
//showToast("Select Subject");
                            } else {
                                showToast("You are not in office");
                                out_of_office_tv.setVisibility(View.VISIBLE);
                                out_of_office_tv.setText("You are not in office");
                            }
                        }
                        /*if (str_signin_response.equals("Valid")){
                            classstarted_bt.setEnabled(true);
                            classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            //showToast("Select Subject");
                        } else {
                            Toast.makeText(getActivity(), "You are not in office", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });

            } catch (Throwable t) {
                Log.e("request fail", "> " + t.getMessage());
                str_signin_response = t.getMessage();

            }
        } catch (Throwable t) {
            Log.e("UnRegister Receiver ", "> " + t.getMessage());
            str_signin_response = t.getMessage();

        }

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

    public static String convertDateFormat(String inputDate) {
        String inputFormat = "yyyy-MM-dd";
        String outputFormat = "dd-MM-yyyy";

        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

        try {
            Date date = inputDateFormat.parse(inputDate);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle the error or return a default value as needed
        }
    }
    /*final Handler handler = new Handler(Looper.getMainLooper());
handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            startCountdownTimer(timer_in_mili);
        }
    }, 1000);
*/

    private void stopCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the countdown timer_str if it's running
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCountdownTimer(); // Stop the countdown timer_str when the fragment is closed
    }
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("attendance_main_id_str", attendance_main_id_str);
        myEdit.putString("selected_classmodeId_str", selected_classmodeId_str);
        myEdit.putString("is_attendance_completed_str", is_attendance_completed_str);
        myEdit.putString("is_classCompletedClicked", String.valueOf(is_classCompletedClicked));
        myEdit.putInt("isImageUpdated", isImageUpdated);
        myEdit.putInt("allowImageReupload", allowImageReupload);
        myEdit.putInt("imageUploadMandatory", imageUploadMandatory);
//myEdit.putString("imageUploadVisible", imageUploadVisible);
        myEdit.putInt("imageUploadVisible", imageUploadVisible);
        myEdit.apply();
        stopCountdownTimer(); // Stop the countdown timer_str when the fragment becomes invisible
    }

    // Show otp pop_up

    private int getReasonPositionById(String reasonId) {
        for (int i = 0; i < Absent_reasons_spinnermodel.size(); i++) {
            if (Absent_reasons_spinnermodel.get(i).getReasonId().equals(reasonId)) {
                return i + 1; // +1 to account for the "Select" option
            }
        }
        return 0; // Default to "Select" option if not found
    }

    public void setSpinnerToValue(String reasonId) {
        int position = getReasonPositionById(reasonId);
        absent_Reason_spinner.setSelection(position);
    }

    private void show_OTP_Popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Mark Student Attendance");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.otp_popup, null);
        builder.setView(dialogView);

        OTP_ET = dialogView.findViewById(R.id.OTP_ET);
        student_name_otp_tv = dialogView.findViewById(R.id.student_name_otp_tv);
        student_appno_otp_tv = dialogView.findViewById(R.id.student_appno_otp_tv);
        student_mobno_otp_tv = dialogView.findViewById(R.id.student_mobno_otp_tv);
        student_name_otp_tv.setText(clicked_Student_name_str);
        student_appno_otp_tv.setText(clicked_Student_Appno_str);
        student_mobno_otp_tv.setText(clicked_Student_Mobno_str);
        //student_scannedvalue_tv = dialogView.findViewById(R.id.Scanned_value_tv);
        ScanQRCode_iv = dialogView.findViewById(R.id.ScanQRCode_iv);
        absent_Reason_spinner = dialogView.findViewById(R.id.absentReason_spinner);
        SubmitReason_btn = dialogView.findViewById(R.id.submitReason_btn);

        ArrayAdapter<String> reason_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Absentreason_ArrayList);
        reason_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        absent_Reason_spinner.setAdapter(reason_adapter);

        if (!Objects.equals(clicked_student_reasonID_str, "0")){
            setSpinnerToValue(clicked_student_reasonID_str);
        }

        absent_Reason_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                absentReasonID_str = String.valueOf(position);
                //classMode_selected_str = (String) parent.getItemAtPosition(position);
                String absent_id  = (String) parent.getItemAtPosition(position);
                absentReason_selected_str = absent_id;

                if (position == 0) {
                    // Handle the "Select" case without processing subjectId
                    System.out.println("Select option is selected");
                } else {
                    // Retrieve the selected subject_SM model
                    OTP_ET.setText("");
                    SubmitReason_btn.setVisibility(View.VISIBLE);
                    Log.e("absent reason", String.valueOf(absentReason_selected_str));
                    AbsentReason_SM selectedModel = Absent_reasons_spinnermodel.get(position - 1);  // Subtract 1 to account for "Select"
                    absentReason_selected_str = selectedModel.getReasonId();
                    System.out.println("absentReason_selected_str: " + absentReason_selected_str);
                }
               /* if (position == 0) {
                    // Handle the "Select" case without processing subjectId
                    System.out.println("Select option is selected");
                } else {
                    AbsentReason_SM selectedModel = Absent_reasons_spinnermodel.get(position - 1);  // Subtract 1 to account for "Select"
                    absentReason_selected_str = selectedModel.getReasonId();
                    //Log.d("Reason Selected", absentReason_selected_str);
                    // Now you have the selected subjectId, do whatever you need with it
                    }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        send_OTP_btn = dialogView.findViewById(R.id.send_OTP_btn);

        send_OTP_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_lv.setEnabled(true);
                AsyncCallWS_Send_OTP();
            }
        });

        //AsyncCallWS_Save_StudentAbsent_Reason


        SubmitReason_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (absentReasonID_str.equals("0")){
                    showToast("Please select a reason");
                } else {
                    AsyncCallWS_Save_StudentAbsent_Reason();
                    dialog.dismiss();
                }
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manulotp_trainer_str = OTP_ET.getText().toString();
                AsyncCallWS_OTP_Attendance_Update();
                dialog.dismiss();
                student_lv.setEnabled(true);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                student_lv.setEnabled(true);
                dialog.dismiss(); // Dismiss the dialog after handling the Cancel action
            }
        });

        dialog = builder.create(); // Create the dialog
        ScanQRCode_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_lv.setEnabled(true);
                scanCode();
            }
        });

        builder.setCancelable(false);

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
        OTP_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the "Submit" button if a reason is entered
                String otp_ET = OTP_ET.getText().toString();
                boolean isSubmitEnabled = !otp_ET.isEmpty();
                if (!otp_ET.isEmpty()) {
                    absent_Reason_spinner.setSelection(0);
                    SubmitReason_btn.setVisibility(View.GONE);
                }
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isSubmitEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan Student QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barlauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barlauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null){
            scanned_appNo_str = result.getContents();
            String exists_appno = student_appno_otp_tv.getText().toString();
            Log.e("clicked app_no", exists_appno);
            Log.e("scanned app_no", scanned_appNo_str);
            if (scanned_appNo_str.equals(exists_appno)){
                AsyncCallWS_QR_Attendance_Update();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Scanned Wrong Student Id", Toast.LENGTH_SHORT).show();
            }

            //student_scannedvalue_tv.setText(result.getContents());
        }
    });

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TRAINER_GEOFENCE) {
            if (resultCode == RESULT_OK) {
                System.out.println("Onactivityresult is called");
                passed_lat = sharedPreferences.getString("passed_lat", "0.0");
                passed_long = sharedPreferences.getString("passed_long", "0.0");
                if(!passed_lat.equals("0.0")){
                    location_not_set_tv.setVisibility(View.GONE);
                    AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                    task1.execute();
                }
                System.out.println("Passed onresume Lat and long: " + passed_lat + " " + passed_long);
            } else {
                // Handle the case where Trainer_Geofence activity was not successful
                location_not_set_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    /*private byte[] convertFileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
    private void fetch_Topic(String selectedSubjectId) {
        showProgressDialog("Fetching Topics");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Topic_SpinnerInterface.Topic_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Topic_SpinnerInterface api = retrofit.create(Topic_SpinnerInterface.class);

        // Call<String> call = api.getJSONString(college_selected_str, emp_id);
        Call<String> call = api.getJSONString(emp_id, selectedSubjectId);
        Log.e("Topic URL : ", call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonresponse = response.body();
                        System.out.println("jsonresponse_topic"+ jsonresponse);

                        spinJSON_topic(jsonresponse);
                        //spinJSON(jsonresponse);
                        // Parse and handle the JSON response as needed
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add("No Topic assigned");
                        Topic_SM.clear();
                        start_attendance_ll.setVisibility(View.GONE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                        Topic_spinner.setAdapter(adapter);
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }else {
                    Log.i("onEmptyResponse", "Returned empty response1");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API Call Failure", t.getMessage());
            }
        });
    }
    private void spinJSON_topic(String response){

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                Topic_ArrayList.clear();
                Topic_SM.clear();
                System.out.println(obj.getString("status"));
                if (obj.getString("status").equals("false")) {
                    // If cohortsArray is null or empty, set the spinner value to "No cohorts assigned"
                    List<String> list = new ArrayList<>();
                    list.add("No Topic assigned");
                    Topic_SM.clear();
                    start_attendance_ll.setVisibility(View.GONE);
                   /* ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                    Topic_spinner.setAdapter(adapter);*/
                } else {
                    JSONArray Topic_Array = obj.getJSONArray("mainTopics");
                    System.out.println("Main Topic : " + Topic_Array);
                    Topic_ArrayList.add("Select");
                    Topic_SM.clear();

                    for (int i = 0; i < Topic_Array.length(); i++) {

                        com.dfhrms.SpinnerModels.Topic_SM spinnerModel = new Topic_SM();
                        JSONObject dataobj = Topic_Array.getJSONObject(i);
                        System.out.println(dataobj.getString("topicName"));
                        spinnerModel.setTopicName(dataobj.getString("topicName"));
                        spinnerModel.setTopicId(dataobj.getString("topicId"));
                        Topic_SM.add(spinnerModel);
                    }

                    for (int i = 0; i < Topic_SM.size(); i++){
                        Topic_ArrayList.add(Topic_SM.get(i).getTopicName());
                    }
                    ArrayAdapter<String> Topic_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, Topic_ArrayList);
                    Topic_adapter.setDropDownViewResource(R.layout.spinner_dropdown); // The drop down view
                    Topic_spinner.setAdapter(Topic_adapter);

                    /*if (mainTopic_Id_str != null) {
                        for (int i = 0; i < Topic_SM.size(); i++) {
                            String topicId = Topic_SM.get(i).getTopicId();
                            System.out.println("Comparing mainTopic_Id_str: " + mainTopic_Id_str + " with topicId: " + topicId);
                            if (mainTopic_Id_str.equals(topicId)) {
                                System.out.println("Setting selection to index: " + (i + 1));
                                //Topic_spinner.setSelection(i + 1);
                                break;
                            }
                        }
                    }*/



                    // cohort_spinner.setOnItemSelectedListener(this);
/*                    if(1>2) {
                        Topic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getContext(), "spinner entered", Toast.LENGTH_SHORT).show();
                                int i = 2;
                                i++;
                                topic_selected_str = (String) parent.getItemAtPosition(position);
                                try {
                                    topic_selected_str = URLEncoder.encode(topic_selected_str, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }
                                topic_id_str = String.valueOf(position);
                                // Check if the selected item is "Select"
                                if ("Select".equals(topic_selected_str)) {
                                    // Handle the "Select" case without processing topicId
                                    System.out.println("Select option is selected");
                                    selectedTopicId_str = ""; // Set to an appropriate default value
                                } else {
                                    if (subject_id_str == null || cohort_id_str == null || classmode_id_str == null ||
                                            subject_id_str.equals("0") || cohort_id_str.equals("0") || classmode_id_str.equals("0") || topic_id_str.equals("0")) {
                                        showToast("Select all fields");
                                    } else {
                                        Log.e("Topic spinner", "Topic spinner");
                                        if (i > 2) {
                                            Check_Secret_Key_Status check_secret_key_status = new Check_Secret_Key_Status();
                                            check_secret_key_status.execute();
                                        }
                                    }
                                    // Retrieve the selected topic model
                                    int adjustedPosition = position - 1; // Adjusted position without "Select"
                                    if (adjustedPosition >= 0 && adjustedPosition < Topic_SM.size()) {
                                        Topic_SpinnerModel selectedModel = Topic_SM.get(adjustedPosition);
                                        selectedTopicId_str = selectedModel.getTopicId();

                                        // Now you have the selected topicId, do whatever you need with it
                                        System.out.println("Selected TopicId: " + selectedTopicId_str);
                                    } else {
                                        // Handle the case when adjustedPosition is out of bounds
                                        System.out.println("Invalid adjustedPosition: " + adjustedPosition);
                                        selectedTopicId_str = ""; // Set to an appropriate default value
                                    }
                                    Log.d("topic_selected_str", topic_selected_str);
                                    Log.d("topic_id_str", topic_id_str);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }*/
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }









    // Pop Up for Sub-topic Multiple selection Dropdown(Listview)

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEMS_KEY = "selectedItems";

    // Declare this as a member variable in your class

    // Inside your showListPopup method
    private void showsubtopic_ListPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Inflate the custom layout
        View customLayout = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dropdown_layout, null);
        builder.setView(customLayout);
        // Reference the SearchView and ListView
        SearchView searchView = customLayout.findViewById(R.id.searchView);
        subtopics_lv = customLayout.findViewById(R.id.listView);

        // Ensure checkedItems_set is initialized
        if (checkedItems_set == null) {
            checkedItems_set = new HashSet<>();
        }

        // Initialize persons with IDs
        subTopic_List = new ArrayList<>();

        internetDectector = new ConnectionDetector(getActivity());
        isInternetPresent = internetDectector.isConnectingToInternet();
        if (isInternetPresent) {
            Get_subtopic_list();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setPositiveButton("OK", null)
                    .show();
        }


        filtered_subtopic_AL = subTopic_List.stream().map(SubTopic_SM::getSubTopicName).collect(Collectors.toCollection(ArrayList::new));


       /* subtopic_adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_multiple_choice, filtered_subtopic_AL);
        subtopics_lv.setAdapter(subtopic_adapter);
        subtopics_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);*/

        // Set up the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterItems(newText);
                return false;
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            getSelectedItems();
            returnSelectedItems();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Show keyboard automatically when the dialog appears
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }


    private void filterItems(String searchText) {
        filtered_subtopic_AL = subTopic_List.stream()
                .filter(subtopic -> subtopic.getSubTopicName().toLowerCase().contains(searchText.toLowerCase()))
                .map(SubTopic_SM::getSubTopicName)
                .collect(Collectors.toCollection(ArrayList::new));

        // Update the filtered_subtopic_AL list to include the previously selected items
        checkedItems_set.forEach(selectedItem -> {
            if (!filtered_subtopic_AL.contains(selectedItem)) {
                filtered_subtopic_AL.add(selectedItem);
            }
        });

        updateListView(filtered_subtopic_AL);
    }

    private void updateListView(ArrayList<String> itemList) {
        subtopic_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, itemList);
        subtopics_lv.setAdapter(subtopic_adapter);

        for (int i = 0; i < itemList.size(); i++) {
            if (checkedItems_set.contains(itemList.get(i))) {
                subtopics_lv.setItemChecked(i, true);
            }
        }
    }

    private void getSelectedItems() {
        newCheckedItems_list = new ArrayList<>();
        for (int i = 0; i < subtopics_lv.getCount(); i++) {
            if (subtopics_lv.isItemChecked(i)) {
                newCheckedItems_list.add(filtered_subtopic_AL.get(i));
            }
        }
    }

    private void returnSelectedItems() {
        // Add the newly selected items to the existing checked items
        checkedItems_set.addAll(newCheckedItems_list);

        List<SubTopic_SM> selectedsubtopics = subTopic_List.stream()
                .filter(subtopic -> newCheckedItems_list.contains(subtopic.getSubTopicName()))
                .collect(Collectors.toList());

        // Extract selected topic names
        List<String> selectedTopicNames = selectedsubtopics.stream()
                .map(SubTopic_SM::getSubTopicName)
                .collect(Collectors.toList());

        // Update the EditText with selected topic names
        if (selectedsubtopics_et != null) {
            StringBuilder selectedValuesText = new StringBuilder();
            for (String selectedTopic : selectedTopicNames) {
                selectedValuesText.append(selectedTopic).append(", ");
            }
            if (selectedValuesText.length() > 0) {
                selectedValuesText.delete(selectedValuesText.length() - 2, selectedValuesText.length());
            }
            selectedsubtopics_et.setText("");
            selectedsubtopics_et.setText(selectedValuesText.toString());
            Gson gson = new Gson();
            //String subtopic_jsonResult_str = gson.toJson(selectedsubtopics);
            subtopic_jsonResult_str = new Gson().toJson(selectedsubtopics);

            // Log the JSON result
            Log.d("SelectedPersonsJSON", "Selected Persons JSON in return: " + subtopic_jsonResult_str);
        }
    }

    //Getting the recent class started time
    public void  fetch_attendanceclasstaken()
    {

        long_Employeeid = Long.parseLong(emp_id); // for web service

        //http://dfhrms.dfindia.org/PMSservice.asmx?op=SendCompoffRequest
        //String URL ="http://dfhrms.dfindia.org/PMSservice.asmx?WSDL";
        String URL =getResources().getString(R.string.main_url);;
        String METHOD_NAME = "GetEmployeeClassAttendance";
        String Namespace="http://tempuri.org/", SOAPACTION="http://tempuri.org/GetEmployeeClassAttendance";

        try {

            SoapObject request = new SoapObject(Namespace, METHOD_NAME);
            request.addProperty("EmployeeId", long_Employeeid);//<EmployeeId>long</EmployeeId>
            request.addProperty("Date", str_yyymmdd.trim().toString());//<Date>string</Date>

            Log.e("EmployeeId", String.valueOf(long_Employeeid));
            Log.e("Date", str_yyymmdd.trim().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //Set output SOAP object
            envelope.setOutputSoapObject(request);
            //Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            Log.e("fetch_attendanceclasstaken", "fetch_attendanceclasstaken");

            SoapObject response=null;
            try {
                Log.e("classattendnce_resp", "classattendnce_resp");
                Log.e("Class Id=",str_classstarted_id);
                try {
                    androidHttpTransport.call(SOAPACTION, envelope);

                    response = (SoapObject) envelope.getResponse();
                    Log.e("classattendnce_resp", response.toString());
                    count = response.getPropertyCount();
                    Log.e("count", String.valueOf(count));

                    str_response = response.getProperty("Message").toString();

                    Log.e("inTime", response.getProperty("InTime").toString());
                    Log.e("outTime",response.getProperty("Outtime").toString());
                    min_timer = response.getProperty("MinClassHours").toString();
                    Log.e("min_timer", min_timer);
                }
                catch(Exception ex)
                {
                    Log.e("error",ex.toString());
                }

                SoapObject list = (SoapObject) response.getProperty("list");

                // Get the last EmpAttendance object
                SoapObject lastEmpAttendance = (SoapObject) list.getProperty(list.getPropertyCount() - 1);

                // Get the InTime of the last EmpAttendance
               /* classStartTime_str = lastEmpAttendance.getProperty("InTime").toString();
                classEndTime_str = lastEmpAttendance.getProperty("Outtime").toString();
                Log.e("Last InTime: " , classStartTime_str);*/
                //Log.e("Last OutTime: " , classEndTime_str);
            }
            catch (Exception t) {
                //Toast.makeText(context, "Request failed: " + t.toString(),
                //		Toast.LENGTH_LONG).show();
                Log.e("request fail", "> " + t.getMessage());
                // str_response="fail";

            }
        }catch (Exception t) {
            //Toast.makeText(context, "UnRegister Receiver Error " + t.toString(),
            //		Toast.LENGTH_LONG).show();
            Log.e("UnRegister Recei Error", "> " + t.getMessage());
            //Str_response="fail";

        }



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

    private void StudentAttendance_Popup(String class_mainId, String clicked_position) {
        Dialog StudentList_dialog = new Dialog(context);
        StudentList_dialog.setContentView(R.layout.studentsummary_popup);
        studentPopup_lv = StudentList_dialog.findViewById(R.id.student_attendance_lv);
        System.out.println("studentPopup_lv"+studentPopup_lv);
        ImageView closeDialogButton = StudentList_dialog.findViewById(R.id.close_attendance_iv);
        String Class_index = clicked_position;
        if (isInternetPresent) {
            AsyncCall_StudentAttendance_list(class_mainId, Class_index);
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setPositiveButton("OK", null)
                    .show();
        }
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentlist_iv.setEnabled(true);
                // Dismiss the dialog when the close button is clicked
                StudentList_dialog.dismiss();
            }
        });

        ImageView update_student_list_iv = StudentList_dialog.findViewById(R.id.update_student_list_iv);

        update_student_list_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncCall_StudentAttendance_list(class_mainId, Class_index);
            }
        });

        // Show the dialog
        StudentList_dialog.show();
    }

    // If class is not taken API Integration for Reason and Submitting the reason

    /*private void ClassNotTaken_Popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Reason for not taking class");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.classnottaken_popup, null);
        builder.setView(dialogView);
        classNotTaken_spinner = (Spinner) dialogView.findViewById(R.id.classNotTaken_spinner);
        classNotTaken_reason_ET = (EditText) dialogView.findViewById(R.id.classNotTaken_reason_ET);
        ArrayAdapter<String> reason_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, reason_ArrayList);
        reason_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        classNotTaken_spinner.setAdapter(reason_adapter);

        classNotTaken_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason_id_str = String.valueOf(position);
                reason_selected_str = (String) parent.getItemAtPosition(position);
                Log.d("cohort_selected", reason_selected_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (reasonselected_intindex != 0){
                    if (reason_id_str!=null && reason_id_str.equals("9")){
                        showToast("Enter the Remark");
                    } else {
                        reason_remark_tv = classNotTaken_reason_ET.getText().toString();
                        reasonselected_intindex = classNotTaken_spinner.getSelectedItemPosition();
                            AsyncCallWS_Save_classnottaken_Reason();
                            dialog.dismiss();
                    }
                } else {
                    showToast("Please select a reason");
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Your Cancel submitOTP_btn logic here
                dialog.dismiss(); // Dismiss the dialog after handling the Cancel action
            }
        });
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create(); // Create the dialog
        // Set the AlertDialog to be not cancelable when the user taps outside
        dialog.setCanceledOnTouchOutside(false);
        // Disable the "Submit" submitOTP_btn initially
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        });

        // Show the dialog
        dialog.show();
    }
*/
    private void ClassNotTaken_Popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Reason for not taking class");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.classnottaken_popup, null);
        builder.setView(dialogView);

        classNotTaken_spinner = dialogView.findViewById(R.id.classNotTaken_spinner);
        classNotTaken_reason_ET = dialogView.findViewById(R.id.classNotTaken_reason_ET);

        ArrayAdapter<String> reason_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, reason_ArrayList);
        reason_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classNotTaken_spinner.setAdapter(reason_adapter);

        builder.setPositiveButton("Submit", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                classNotTaken_bt.setEnabled(true);
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button submitButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reasonselected_intindex = classNotTaken_spinner.getSelectedItemPosition();
                        String reasonRemark = classNotTaken_reason_ET.getText().toString();

                        if (reasonselected_intindex != 0) {
                            if (reason_id_str != null && reason_id_str.equals("9") && reasonRemark.isEmpty()) {
                                showToast("Enter the Remark");
                            } else {
                                reason_remark_tv = reasonRemark;
                                AsyncCallWS_GetSecretKey asyncCallWSGetSecretKey = new AsyncCallWS_GetSecretKey();
                                asyncCallWSGetSecretKey.execute();
                                dialog.dismiss();
                                classNotTaken_bt.setEnabled(true);
                            }
                        } else {
                            showToast("Please select a reason");
                        }
                    }
                });
            }
        });

        dialog.show();

        // Set TextWatcher only if reason_id_str is 9
        classNotTaken_reason_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("9".equals(reason_id_str)) {
                    String reason = classNotTaken_reason_ET.getText().toString();
                    int selectedOptionIndex = classNotTaken_spinner.getSelectedItemPosition();
                    boolean isSubmitEnabled = selectedOptionIndex != 0 && !reason.isEmpty();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isSubmitEnabled);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        classNotTaken_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason_id_str = String.valueOf(position);
                reason_selected_str = (String) parent.getItemAtPosition(position);
                Log.d("cohort_selected", reason_selected_str);
                if (reason_selected_str.equals("9")){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void AsyncCallWS_getClassNotTakenReasons() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(classNotTaken_reason_SI.GetReasons_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        classNotTaken_reason_SI api = retrofit.create(classNotTaken_reason_SI.class);

        Call<String> call = api.getJSONString();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String Reason_jsonresponse = response.body();
                        spinReasonJSON(Reason_jsonresponse);
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
                Log.e("API Call Failure", t.getMessage());
            }
        });
    }
    private void spinReasonJSON(String response){

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                JSONArray reasonArray = obj.getJSONArray("reasonList");
                reason_ArrayList.add("Select");
                reasons_spinnermodel.clear();
                for (int i = 0; i < reasonArray.length(); i++) {

                    ClassNoTakenReason_SM spinnerModel = new ClassNoTakenReason_SM();
                    JSONObject dataobj = reasonArray.getJSONObject(i);

                    spinnerModel.setReason(dataobj.getString("reasonName"));
                    reasons_spinnermodel.add(spinnerModel);
                }
                for (int i = 0; i < reasons_spinnermodel.size(); i++){
                    reason_ArrayList.add(reasons_spinnermodel.get(i).getReason().toString());
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Headcount_popup(String imageUrl, String headCount, String attendanceMainId, String allowImageRetake) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_popup, null);
        ImageView headcont_camera = dialogLayout.findViewById(R.id.headcount_camera);
        ImageView imageView = dialogLayout.findViewById(R.id.popup_imageview);
        TextView noImageText = dialogLayout.findViewById(R.id.no_image_text);
        TextView heacountt = dialogLayout.findViewById(R.id.head_countt);
        heacountt.setText(headCount);
        boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
        ImageView close_iv = dialogLayout.findViewById(R.id.close_img);
        TextView upladedimg_tv = dialogLayout.findViewById(R.id.uploadeimg_tv);


        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setVisibility(View.GONE);
            upladedimg_tv.setVisibility(View.GONE);
            noImageText.setVisibility(View.VISIBLE);
        } else {
            noImageText.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            upladedimg_tv.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUrl).into(imageView);
        }

        if(allowImageRetake.equals("1")){
            if (!isBefore) {
                headcont_camera.setVisibility(View.VISIBLE);
            } else {
                headcont_camera.setVisibility(View.GONE);
            }
        }else {
            headcont_camera.setVisibility(View.GONE);
        }



        headcont_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen_str = "headpopup";
                attendanceMainId_image = attendanceMainId;
                openCamera();

              /*  if (soapobject_recentattendance_response != null) {
                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                        classstarted_bt.setEnabled(true);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                    }

                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                            equalsIgnoreCase("Success")) {
                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                equalsIgnoreCase("Class Completed")) {
                            attendanceMainId_image = attendanceMainId;
                            openCamera();
                        } else {
                            showToast("Complete the on going class first");
                        }
                    }
                }
*/

            }
        });



        builder.setView(dialogLayout);
        headcount_dialog = builder.create();

        // Set the onClickListener for the close button
        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headcount_dialog.dismiss();
            }
        });

        headcount_dialog.show();
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

// Absent Student Reason updating


    private void AsyncCallWS_getStudent_absent_Reasons() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(student_absent_reason_SI.GetReasons_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        student_absent_reason_SI api = retrofit.create(student_absent_reason_SI.class);

        Call<String> call = api.getJSONString();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String absentReason_JR = response.body();
                        spinAbsentReasonJSON(absentReason_JR);
                        Log.e("absentReason",absentReason_JR);
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
                Log.e("API Call Failure", t.getMessage());
            }
        });
    }
    private void spinAbsentReasonJSON(String response){

        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){
                JSONArray reasonArray = obj.getJSONArray("reasonList");
                Absentreason_ArrayList.add("Select");
                Absent_reasons_spinnermodel.clear();
                for (int i = 0; i < reasonArray.length(); i++) {

                    AbsentReason_SM spinnerModel = new AbsentReason_SM("0", "Select");
                    JSONObject dataobj = reasonArray.getJSONObject(i);

                    spinnerModel.setReasonName(dataobj.getString("reasonName"));
                    spinnerModel.setReasonId(dataobj.getString("reasonId"));
                    Absent_reasons_spinnermodel.add(spinnerModel);
                }
                for (int i = 0; i < Absent_reasons_spinnermodel.size(); i++){
                    Absentreason_ArrayList.add(Absent_reasons_spinnermodel.get(i).getReasonName().toString());
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //  deleteclassstarted without Deprecation
    public void AsyncCallWS_DeleteStartedClass() {

        CompletableFuture.supplyAsync(() -> {
            long long_userid = Long.parseLong(str_classstarted_id);
            String response = "";

            try {
                String DeleteClass_URL = Class_URL.API_URL + "deleteclassstarted?ClassStartedId=" + long_userid;
                URL url = new URL(DeleteClass_URL);
                Log.e("DeleteClass API:", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            // Process result on UI thread
            start_attendance_btn.setEnabled(false);
            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
            Log.e("API Response: ", result);
        });
    }

    // Student Absent Reason Without deprecated code
    public void AsyncCallWS_Save_StudentAbsent_Reason() {

        CompletableFuture.supplyAsync(() -> {
            String response = "";
            long absent_id = Long.parseLong(absentReason_selected_str);
            try {
                //http://testingrdp.dfindia.org:9000/api/skillattendance/submitabsentreason?
                // AttendanceMain_Id=12&ReasonId=1&Remark=adfasdfasdf
                //long AttendanceMain_Id, string ApplicationNo, long AbsentReasonId absentReason_selected_str

                if (attendance_mainid == null) {
                    attendance_mainid = attendance_main_id_str;
                }
                String ClassNotTaken_Reason_url = Class_URL.API_URL + "submitabsentreason?AttendanceMain_Id=" +
                        attendance_mainid + "&ApplicationNo=" + clicked_Student_Appno_str + "&AbsentReasonId=" + absent_id;
                URL url = new URL(ClassNotTaken_Reason_url);
                Log.d("classNotTaken url", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                    Log.e("Student absent Response", response);
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            // Process result on UI thread
            Log.e("API Response: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String absentReason_Status = jsonObject.optString("status");
                showToast(jsonObject.getString("message"));
                if (absentReason_Status.equals("true")) {
                    showToast(jsonObject.optString("message"));
                } else {
                    showToast("Could not update the Reason");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    // Class not taken Reason Without deprecated code
    public void AsyncCallWS_Save_classnottaken_Reason() {

        CompletableFuture.supplyAsync(() -> {
            String response = "";

            try {
                //http://testingrdp.dfindia.org:9000/api/skillattendance/classnottakenreasonremarks?
                // AttendanceMain_Id=12&ReasonId=1&Remark=adfasdfasdf
                String ClassNotTaken_Reason_url = Class_URL.API_URL + "classnottakenreasonremarks?AttendanceMain_Id=" +
                        attendance_main_id_str + "&ReasonId=" + reason_id_str + "&Remark=" + reason_remark_tv;
                URL url = new URL(ClassNotTaken_Reason_url);
                Log.d("classNotTaken url", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                    Log.e("Not taken Response", response);
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            // Process result on UI thread
            start_attendance_btn.setEnabled(false);
            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
            Log.e("API Response: ", result);
        });
    }

    // Update class Id Without deprecated code
    public void AsyncCallWS_Update_ClassId() {

        CompletableFuture.supplyAsync(() -> {
            String response = "";
            long long_userid = Long.parseLong(str_classstarted_id);
//(long ClassStartedId, int IsAttendanceStarted)
            try {
                String apiUrl = Class_URL.API_URL + "updatehrmsattendancestarted?ClassStartedId=" + long_userid + "&IsAttendanceStarted=1";
                URL url = new URL(apiUrl);
                Log.e("API Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            getActivity().runOnUiThread(() -> {
                // Process result on UI thread
                Log.e("API Response: ", result);
                AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
                task1.execute();
            });
        });
    }

    // Get Attendance Start time and end time Without deprecated code
    public void AsyncCallWS_AttendanceStartEndClass(String lastInTime_strr, String lastOutTime_strr) {

        CompletableFuture.supplyAsync(() -> {
            String lastInTime_str = lastInTime_strr;
            String lastOutTime_str = lastOutTime_strr;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL + "AttendanceStartEndClass?AttendanceMain_Id=" + attendance_main_id_str + "&ClassStartTime=" + lastInTime_str + "&ClassEndTime=" + lastOutTime_str;
                URL url = new URL(apiUrl);
                Log.e("StartEnd link", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            // Process result on UI thread
            Log.e("API Response: ", result);
        });
    }

    // Mark Attendance based on QR Code Without deprecated code
    public void AsyncCallWS_QR_Attendance_Update() {

        CompletableFuture.supplyAsync(() -> {
            String attendanceMainId = attendance_main_id_str;
            String applicationNo = scanned_appNo_str;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL + "QRCodeAttendanceUpdate?AttendanceMain_Id=" + attendanceMainId + "&Employee_Id=" + emp_id + "&Application_No=" + applicationNo;
                URL url = new URL(apiUrl);
                Log.e("QR_Attendance_Update Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            getActivity().runOnUiThread(() -> {
                // Process result on UI thread
                Log.e("API Response: ", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String otpsent_Status = jsonObject.optString("status");
                    showToast(jsonObject.getString("message"));
                    if (otpsent_Status.equals("true")) {
                        if (isInternetPresent) {
                            class_typr_str = "Exists";
                            AsyncCallWS_Update_student_list updateStudentList = new AsyncCallWS_Update_student_list();
                            updateStudentList.execute();

                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("No Internet Connection")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                    System.out.println("otpsent_Status" + otpsent_Status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    // Sub-topic list Without deprecated code
    public void Get_subtopic_list() {
        CompletableFuture.supplyAsync(() -> {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(Class_URL.API_URL + "getsubtopiclist?TopicMain_Id=" + selectedTopicId_str);
                System.out.println("Get_subtopic_list url: " + url);
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
            } catch (Exception e) {
                e.printStackTrace();
                result = "Exception: " + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }).thenAccept(result -> {
            getActivity().runOnUiThread(() -> {
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray subTopicsArray = jsonObject.getJSONArray("subTopics");
                    subTopic_List = new ArrayList<>();

                    for (int i = 0; i < subTopicsArray.length(); i++) {
                        JSONObject subTopicObj = subTopicsArray.getJSONObject(i);
                        String subTopicId = subTopicObj.getString("subTopicId");
                        String subTopicName = subTopicObj.getString("subTopicName");
                        SubTopic_SM subTopic = new SubTopic_SM(subTopicId, subTopicName);
                        subTopic_List.add(subTopic);
                    }
                    filtered_subtopic_AL = subTopic_List.stream().map(SubTopic_SM::getSubTopicName).collect(Collectors.toCollection(ArrayList::new));

                    for (SubTopic_SM subTopic : subTopic_List) {
                        System.out.println("Subtopic_list: " + subTopic.getSubTopicName() + " - " + subTopic.getSubTopicId());
                    }

                    subtopic_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, filtered_subtopic_AL);
                    subtopics_lv.setAdapter(subtopic_adapter);
                    subtopics_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    for (int i = 0; i < filtered_subtopic_AL.size(); i++) {
                        if (checkedItems_set.contains(filtered_subtopic_AL.get(i))) {
                            subtopics_lv.setItemChecked(i, true);
                        }
                    }

                    subtopics_lv.setOnItemClickListener((parent, view, position, id) -> {
                        String selectedItem = filtered_subtopic_AL.get(position);
                        if (subtopics_lv.isItemChecked(position)) {
                            checkedItems_set.add(selectedItem);
                        } else {
                            checkedItems_set.remove(selectedItem);
                        }
                    });

                    System.out.println("filtered_subtopic_AL: " + filtered_subtopic_AL);
                    System.out.println("Checked items: " + checkedItems_set);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

       getActivity().runOnUiThread(() -> showProgressDialog("processing results"));
    }

    //Attendance complete without deprecated code
    public void AsyncCallWS_Attendance_complete() {
        CompletableFuture.supplyAsync(() -> {
            String attendanceMainId = attendance_main_id_str;
            String Reason = Reason_str;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL + "completeattendance?AttendanceMain_Id=" + attendanceMainId + "&Remark=" + Reason;
                URL url = new URL(apiUrl);
                Log.e("API Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            // Process result on UI thread
            Log.e("API Response: ", result);
        });
    }

    // Get Student list Without deprecated code
    public void AsyncCall_StudentAttendance_list(String attendance_mainid, String Class_index) {
        CompletableFuture.supplyAsync(() -> {
            this.attendance_mainid = attendance_mainid;
            this.Class_index = Class_index;
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL + "getstudentslist?AttendanceMain_Id=" + attendance_mainid);
                    System.out.println("myurl in Updated_summary_list: " + url);
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
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                    //GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                    JSONArray studentAttendance_array = jsonObject.getJSONArray("studentList");
                    StudentAttendancelist.clear();
                    for (int i = 0; i < studentAttendance_array.length(); i++) {
                        JSONObject obj = studentAttendance_array.getJSONObject(i);
                        String name = obj.getString("studentName");
                        String app_no = obj.getString("applicationNo");
                        String reasonForAbsent_Id = obj.getString("reasonForAbsent_Id");
                        attendance_status_str = obj.getString("attendanceStatus");
                        HashMap<String, Object> map = new HashMap<>();
                        StudentAttendancelist.add(map);
                        map.put("name", name);
                        map.put("app_no", app_no);
                        map.put("reasonForAbsent_Id", reasonForAbsent_Id);
                        String mobile_no = obj.getString("mobileNo");
                        boolean isPresent = attendance_status_str.equals("Present");
                        System.out.println("isPresent_bool" + isPresent);
                        map.put("switch", isPresent);
                        map.put("mobile_no", mobile_no);
                        // Create and add the switch to the layout here
                        Switch mySwitch = (Switch) LayoutInflater.from(getContext()).inflate(R.layout.custom_switch_layout, null);
                        mySwitch.setId(i); // Set a unique ID for each switch
                        // Check isPresent_bool and update switch colors accordingly
                        if (isPresent) {
                            mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.greenTextcolor)));
                            mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
                        } else {
                        if (reasonForAbsent_Id.equals("0")){
                            Log.e("reasonForAbsent_Id", reasonForAbsent_Id);
                            mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorred)));
                            mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.lightred)));
                        } else {
                            Log.e("reasonForAbsent_Id", reasonForAbsent_Id);
                            mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
                            mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
                        }
                    }
                    }
                    studentlist_adapter = new SimpleAdapter(getActivity(), StudentAttendancelist, R.layout.studentsummary_list,
                            new String[]{"name", "app_no", "mobile_no", "switch"},
                            new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1}) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            // Get the data for this position
                            String reasonForAbsent_Id = (String) StudentAttendancelist.get(position).get("reasonForAbsent_Id");

                            // Find the relevant views
                            TextView student_name_tv = view.findViewById(R.id.student_name_tv);
                            TextView application_no_tv = view.findViewById(R.id.application_no_tv);
                            TextView mobile_no_tv = view.findViewById(R.id.mobile_no_tv);

                            // Reset the views to their default state
                            student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                            application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                            mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

                            // Apply conditional styling
                            if (!reasonForAbsent_Id.equals("0")) {
                                student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                                application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                                mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            }

                            return view;
                        }
                    };

// Use the adapter with the ListView
                    studentPopup_lv.setAdapter(studentlist_adapter);

                    boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                    studentPopup_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            studentPopup_lv.setEnabled(false);
                            if (!isBefore) {
                                if (countDownTimer == null) {
                                    if (Class_index.equals("0")) {
                                        HashMap<String, Object> clickedItem = StudentAttendancelist.get(position);
                                        boolean isPresent = (boolean) clickedItem.get("switch");

                                        if (!isPresent) {
                                            // Extract necessary information from the clicked item
                                            clicked_Student_name_str = (String) clickedItem.get("name");
                                            clicked_Student_Appno_str = (String) clickedItem.get("app_no");
                                            clicked_Student_Mobno_str = (String) clickedItem.get("mobile_no");
                                            clicked_student_reasonID_str = (String) clickedItem.get("reasonForAbsent_Id");

                                            Log.e("Reason ID", clicked_student_reasonID_str);
                                            System.out.println("onclick in Async task is executed");
                                            // Show popup for items where isPresent_bool is false
                                            System.out.println("onclick listener for student_lv is called: " +
                                                    clicked_Student_name_str + " " + clicked_Student_Appno_str + " " + clicked_Student_Mobno_str);
                                            //show_OTP_Popup();
                                            show_OTP_Popup();
                                        } else {
                                            showToast("No Manual attendance for Present Student");
                                        }
                                    } else {
                                        showToast("Cannot mark attendance for Previous Classes");
                                    }
                                } else {
                                    Log.e("timer", countDownTimer.toString());
                                    showToast("Complete the ongoing class");
                                }
                            } else {
                                showToast("Cannot mark attendance for Previous dates");
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    studentPopup_lv.setEnabled(true);
                                }
                            }, 1000);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        getActivity().runOnUiThread(() -> {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);
        });
    }

    // Sub-topic list Without deprecated code
    public void Check_Secret_Key_Status() {
        CompletableFuture.supplyAsync(() -> {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    //E&C EC&K
                    /*if (subject_selected_str.equals("E&C")){
                        subject_selected_str="EC&K";
                    }*/
                    //college_selected_str = college_selected_str.replace("&", "%26");

                    //college_selected_str= "HANAGAL - GFGC PG CENTRE";
                    //college_selected_str= "HANAGAL  GFGC PG CENTRE";
                    //college_selected_str= "  HANAGAL  GFGC PG @@ CENTRE";
                    //college_selected_str= "HANAGAL && GFGC PG &CENTRE";
                    //college_selected_str= "HANAGAL _ GFGC - PG & CENTRE";

                    // HANAGAL - GFGC PG & CENTRE
                    //

                    if(college_selected_str.contains("&")) {
                        try {
                            college_selected_str = URLEncoder.encode(college_selected_str, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    Log.e("collegename",college_selected_str);//
                    //HANAGAL+-+GFGC+PG+CENTRE
                    //HANAGAL++GFGC+PG+CENTRE
                    //++HANAGAL++GFGC+PG+%40%40+CENTRE
                    //HANAGAL+%26%26+GFGC+PG+%26CENTRE
                    //HANAGAL+GFGC+PG+%26+CENTRE
                    //HANAGAL_GFGC+PG+%26+CENTRE
                    //HANAGAL+_+GFGC+-+PG+%26+CENTRE
                    //http://testingrdp.dfindia.org:9000/api/skillattendance/checksecretkeystatus?Employee_Id=
                    url = new URL(Class_URL.API_URL+"checksecretkeystatus?Employee_Id="+emp_id+"&CollegeName="+ college_selected_str +
                            "&Cohort="+ cohort_selected_str +"&SubjectId="+ selectedSubjectId_str +"&SubjectName="+
                            subject_selected_str +"&Date="+ datevalue_str + "&ClassMode_Id="+ selected_classmodeId_str +
                            "&TopicId="+ selectedTopicId_str);
                    System.out.println("Check_Secret_Key_Status url: "+url);
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
                    JSONObject jsonObject = new JSONObject(result);
                    //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                    secret_key_status_str = jsonObject.getString("message");
                    System.out.println("secret_key_status_str" + secret_key_status_str);
                    boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                    System.out.println("selected date" + datevalue_str);
                    if (secret_key_status_str.equals("New"))
                    {
                        class_typr_str = "New";
                        summaryHeading_tv.setText("Summary");
                        summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        if (isBefore){
                            previous_date_tv.setVisibility(View.VISIBLE);
                            System.out.println("Selected Previous date");
                            classstarted_bt.setEnabled(false);
                            classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            classscompleted_bt.setEnabled(false);
                            classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            start_attendance_btn.setEnabled(false);
                            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                        } else {
                            previous_date_tv.setVisibility(View.GONE);
                            if (selected_classmodeId_str.equals("1") || selected_classmodeId_str.equals("3")){
                                out_of_office_tv.setVisibility(View.GONE);
                                if (soapobject_recentattendance_response != null) {
                                    if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                                        classstarted_bt.setEnabled(true);
                                        classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                    }

                                    if (soapobject_recentattendance_response.getProperty("Message").toString().
                                            equalsIgnoreCase("Success")) {
                                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                                equalsIgnoreCase("Class Started") &&
                                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                                        equalsIgnoreCase("0")) {
                                            classstarted_bt.setEnabled(false);
                                            classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                                        } else {
                                            classstarted_bt.setEnabled(true);
                                            classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                        }
                                    }
                                }
                            } else {
                                passed_lat = sharedPreferences.getString("passed_lat", "0.0");
                                passed_long = sharedPreferences.getString("passed_long", "0.0");
                                if (passed_lat.equals("0.0")) {
                                    location_not_set_tv.setVisibility(View.VISIBLE);
                                    gpstracker_obj = new GPSTracker(getActivity());
                                    if (gpstracker_obj.canGetLocation())
                                    {
                                        Intent i = new Intent(getActivity(), Trainer_Geofence.class);
                                        TrainerGeofenceResultLauncher.launch(i);
                                    } else {
                                        gpstracker_obj.showSettingsAlert();
                                        settingsDialogShown = true;
                                    }
                                } else {
                                    location_not_set_tv.setVisibility(View.GONE);
                                    AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                                    task1.execute();
                                }
                            }


                       /* if (passed_lat.equals("0.0")) {
                            location_not_set_tv.setVisibility(View.VISIBLE);
                            Intent i = new Intent(getActivity(), Trainer_Geofence.class);
                            startActivity(i);
                        } else {
                            location_not_set_tv.setVisibility(View.GONE);
                            AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                            task1.execute();
                        }
                        fetch_Topic(subject_id_str);*/
                        }
                    } else if (secret_key_status_str.equals("Exists")) {
                        class_typr_str = "Exists";
                        summaryHeading_tv.setText("Class Completed Summary");
                        summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_500));
                        location_not_set_tv.setVisibility(View.GONE);
                        //fetch_Topic(subject_id_str);
                        if (!isBefore) {
                            if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                    equalsIgnoreCase("Class Started") &&
                                    soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                            equalsIgnoreCase("0")) {
                                classstarted_bt.setEnabled(false);
                                classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                            } else {
                                classstarted_bt.setEnabled(true);
                                classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                            }
                        }
                        dateEdt_tv.setEnabled(false);
                        cohort_spinner.setEnabled(false);
                        ClassMode_spinner.setEnabled(false);
                        college_name_Spinner.setEnabled(false);
                        subject_spinner.setEnabled(false);
                        Topic_spinner.setEnabled(false);
                        selectedsubtopics_et.setEnabled(false);
                        System.out.println("MyAsyncTasks in exists");
                        if (isInternetPresent) {
                            AsyncCallWS_GetSecretKey asyncCallWSGetSecretKey = new AsyncCallWS_GetSecretKey();
                            asyncCallWSGetSecretKey.execute();
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("No Internet Connection")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }

                    //present_percent_tv.setText(presentPercent.)
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        getActivity().runOnUiThread(() -> showProgressDialog("Checking Class Status"));
    }

    // Get Trainee_summary_list Without deprecated code
    public void Trainee_summary_list() {
        CompletableFuture.supplyAsync(() -> {
            StringBuilder result = new StringBuilder();
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {

                    url = new URL(Class_URL.API_URL+"getattendancesummary?Employee_Id=" + emp_id + "&Date=" + datevalue_str);
                    System.out.println("myurl in Trainee_summary_list: " + url);

                    // Open a URL connection
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
                        result.append((char) data);
                        data = isw.read();
                    }

                    // Return the data to onPostExecute method
                    return result.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }).thenAccept(result -> {
            getActivity().runOnUiThread(() -> {
                //dismissProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray summaryArray = jsonObject.getJSONArray("employeeAttendanceSummary");

                    if (summaryArray.length() > 0) {
                        // Get the first object in the array
                        JSONObject obj = summaryArray.getJSONObject(0);
                        isImageUpdated = obj.getInt("isImageUpdated");
                        attendance_main_id_str = obj.getString("attendanceMain_Id");
                        selected_classmodeId_str = obj.getString("classMode_Id");
                        JSONArray Configurable_array = obj.getJSONArray("configurables");
                        for (int j = 0; j < Configurable_array.length(); j++) {
                            JSONObject configurable = Configurable_array.getJSONObject(j);
                            imageUploadVisible = configurable.getInt("imageUploadVisible");
                            allowImageReupload = configurable.getInt("allowImageReupload");
                            imageUploadMandatory = configurable.getInt("imageUploadMandatory");
                        }

                        Log.e("isImageUpdated: ", String.valueOf(isImageUpdated));
                        Log.e("imageUploadVisible: ", String.valueOf(imageUploadVisible));
                        Log.e("allowImageReupload: ", String.valueOf(allowImageReupload));
                        Log.e("imageUploadMandatory: ", String.valueOf(imageUploadMandatory));
                        Log.e("Read ID: ", attendance_main_id_str);


                        // Check conditions and set visibility of imageUpload_iv accordingly
                        if ((isImageUpdated == 0 && allowImageReupload == 1 && imageUploadVisible ==1) ||
                                (isImageUpdated == 1 && allowImageReupload == 1 && imageUploadVisible ==1) ||
                                (isImageUpdated == 0 && allowImageReupload == 0 && imageUploadVisible ==1)) {
                            if (screen_str!=null){
                                if (screen_str.equals("completeclass") || screen_str.equals("main")) {
                                    imageUpload_iv.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            imageUpload_iv.setVisibility(View.GONE);
                        }

                        Log.d("isImageUpdated", String.valueOf(isImageUpdated));
                    } else {
                        // Handle case when the array is empty
                        imageUpload_iv.setVisibility(View.GONE);
                        Log.d("onPostExecute", "Summary array is empty");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        getActivity().runOnUiThread(() -> {
            //showProgressDialog("Processing results...");
        });
    }

    // Get updated_summary_list Without deprecated code
    public void Updated_summary_list() {
        CompletableFuture.supplyAsync(() -> {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL+"getattendancesummary?Employee_Id="+emp_id+"&Date="+ datevalue_str);
                    System.out.println("myurl in Updated_summary_list: "+url);
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
                        JSONObject obj = summary_array.getJSONObject(i);//sets to custom position
                        String collegeName = obj.getString("collegeName");
                        String semesterName = obj.getString("semesterName");
                        String subjectName = obj.getString("subjectName");
                        String topicName = obj.getString("topicName");
                        String attendanceMain_Id = obj.getString("attendanceMain_Id");
                        String classStartTime = obj.getString("classStartTime");
                        String classEndTime = obj.getString("classEndTime");
                        String attendanceStarttime = obj.getString("attendanceStartDate");
                        String attendanceEndtime = obj.getString("attendanceEndDate");
                        String totalStudents = obj.getString("totalStudents");
                        String totalPresent = obj.getString("totalPresent");
                        String totalAbsent = obj.getString("totalAbsent");
                        String classMode = obj.getString("classMode");
                        String classMode_Id = obj.getString("classMode_Id");
                        String classNotTakenReason = obj.getString("classNotTakenReason");
                        String headCount = obj.getString("headCount");
                        String isimageupdate = obj.getString("isImageUpdated");
                        imageUrl = obj.optString("image_URL");

                        if (collegeName.equals("null")){
                            no_summaryvalues_tv.setVisibility(View.VISIBLE);
                            no_summaryvalues_tv.setText("No Data for this Date");
                            summary_lv.setVisibility(View.GONE);
                        } else {
                            no_summaryvalues_tv.setVisibility(View.GONE);
                            summary_lv.setVisibility(View.VISIBLE);
                        }

                        HashMap<String, String> summary_map = new HashMap();
                        Summarylist.add(summary_map);
                        summary_map.put("collegeName", collegeName);
                        summary_map.put("classmode", classMode);
                        summary_map.put("classMode_Id", classMode_Id);
                        summary_map.put("semesterName", semesterName);
                        summary_map.put("subjectName", subjectName);
                        summary_map.put("topicName", topicName);
                        summary_map.put("attendanceMain_Id", attendanceMain_Id);
                        summary_map.put("classNotTakenReason", classNotTakenReason);
                        summary_map.put("classStartTime", classStartTime);
                        summary_map.put("classEndTime", classEndTime);
                        summary_map.put("attendanceStarttime", attendanceStarttime);
                        summary_map.put("attendanceEndtime", attendanceEndtime);
                        summary_map.put("totalStudents", totalStudents);
                        summary_map.put("totalPresent", totalPresent);
                        summary_map.put("totalAbsent", totalAbsent);
                        summary_map.put("headCount", headCount);
                        summary_map.put("image_URL", imageUrl);
                        summary_map.put("isImageUpdated", isimageupdate);
                    }
                    System.out.println("Summarylist"+Summarylist);
                    //JSONObject val1summary_array = summary_array.getJSONObject(0);
                    //System.out.println("jsonArray"+jsonArray);
                    ListAdapter summary_adapter = new SimpleAdapter(getActivity(),Summarylist,R.layout.summary_list,
                            new String[]{"collegeName","classmode", "semesterName","subjectName", "topicName","attendanceMain_Id",
                                    "classNotTakenReason", "classStartTime", "classEndTime","attendanceStarttime",
                                    "attendanceEndtime", "totalStudents", "totalPresent", "totalAbsent", "headCount",
                                    "isImageUpdated"},
                            //use same id here, that you used in map, case sensitive
                            new int[]{R.id.college_name_tv, R.id.classMode_tv, R.id.semester_tv, R.id.subject_tv, R.id.topic_tv,
                                    R.id.mainId_tv,R.id.classNotTaken_reason_tv, R.id.classStartTime_tv, R.id.classEndTime_tv, R.id.attendanceStartTime_tv,
                                    R.id.attendanceEndTime_tv, R.id.total_students_tv, R.id.present_students_tv,
                                    R.id.absent_students_tv, R.id.head_count, R.id.imgupdated})
                    {
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
                        /*JSONObject configurable = summary_array.getJSONObject("employeeAttendanceSummary");
                        JSONArray configurablesArray = configurable.getJSONArray("configurables");
*/
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
                    };
                    update_summary_list_iv.setEnabled(true);
                    //use same path here, id for id, name for name
                    summary_lv.setAdapter(summary_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        getActivity().runOnUiThread(() -> {
            //showProgressDialog("Processing results...");
        });
    }

    // Send_OTP Without deprecated code
    public void AsyncCallWS_Send_OTP() {

        CompletableFuture.supplyAsync(() -> {
            String attendanceMainId = attendance_main_id_str;
            String response = "";

            try {
                //String apiUrl = "http://testingrdp.dfindia.org:9000/api/skillattendance/SendOTP?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+clicked_Student_Appno_str+"&StudentName="+clicked_Student_name_str+"&SubjectName="+subject_selected_str+"&MobileNo=9686616171";
                if (subject_selected_str == null){
                    subject_selected_str = info_Subject;
                }
                String apiUrl = Class_URL.API_URL+"SendOTP?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+ clicked_Student_Appno_str +"&StudentName="+ clicked_Student_name_str +"&SubjectName="+ subject_selected_str +"&MobileNo="+ clicked_Student_Mobno_str;
                URL url = new URL(apiUrl);
                Log.e("sendOTP Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            getActivity().runOnUiThread(() -> {
                // Process result on UI thread
                Log.e("API Response: ",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String otpsent_Status = jsonObject.optString("status");
                    showToast(jsonObject.getString("message"));
                    if (otpsent_Status.equals("true")){

                    }
                    System.out.println("otpsent_Status"+otpsent_Status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    // OTP_Attendance_Update Without deprecated code
    public void AsyncCallWS_OTP_Attendance_Update() {

        CompletableFuture.supplyAsync(() -> {
            String attendanceMainId = attendance_main_id_str;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL+"OTPAttendanceUpdate?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+ clicked_Student_Appno_str +"&OTP="+ manulotp_trainer_str;
                URL url = new URL(apiUrl);
                Log.e("OTP_Attendance_Update Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }).thenAcceptAsync(result -> {
            getActivity().runOnUiThread(() -> {
                // Process result on UI thread
                Log.e("API Response: ",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String otpsent_Status = jsonObject.optString("status");
                    showToast(jsonObject.getString("message"));
                    if (otpsent_Status.equals("true")){
                        if (isInternetPresent) {
                            class_typr_str = "Exists";
                            AsyncCallWS_Update_student_list updateStudentList = new AsyncCallWS_Update_student_list();
                            updateStudentList.execute();
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("No Internet Connection")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                    System.out.println("otpsent_Status"+otpsent_Status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }


    // OTP_Attendance_Update Without deprecated code
    public void AsyncCallWS_Update_student_list() {

        CompletableFuture.supplyAsync(() -> {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    currentlatitude = Double.valueOf(passed_lat);
                    currentlongitude = Double.valueOf(passed_long);
                    //http://testingrdp.dfindia.org:9000/api/skillattendance/getsecretkey?Employee_Id=1191&CollegeName=HARIHAR - SGRKS COLLEGE FOR WOMEN&Cohort=DVG-HARIHAR-SGRKSWOMEN-01&SubjectId=1&SubjectName=LEAD&Date=2023-11-10&Latitude=14.508168741&Longitude=75.80708036
                    //url = new URL(myUrl+main_id+"&Latitude="+currentlatitude+"&Longitude="+currentlongitude);
                    //String GetSecretKey_URL = "http://testingrdp.dfindia.org:9000/api/skillattendance/getsecretkey?Employee_Id=1191&CollegeName=";
                    url = new URL(Class_URL.API_URL+ "getsecretkey?Employee_Id=" +emp_id+"&CollegeName="+ college_selected_str +"&Cohort="+
                            cohort_selected_str +"&SubjectId="+ selectedSubjectId_str +"&SubjectName="+
                            subject_selected_str +"&Date="+ datevalue_str +"&Latitude="+currentlatitude+
                            "&Longitude="+currentlongitude+"&MainTopicId="+ selectedTopicId_str +"&MainTopicName="+
                            topic_selected_str +"&SubTopicJson="+ subtopic_jsonResult_str+"&ClassMode_Id="+
                            selected_classmodeId_str+"&ClassType="+class_typr_str);
                    System.out.println("myurl in Update_student_list: "+url);
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
        }).thenAcceptAsync(result -> {
            getActivity().runOnUiThread(() -> {
                // Process result on UI thread
                dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                    GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                    updateStudentList(Studentlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void updateStudentList(List<HashMap<String, Object>> updatedList) throws JSONException {
        Studentlist.clear();
        System.out.println("updateStudentList is called");
        GetSecretKey_studentlist_Response = GetSecretKey_Response.getJSONArray("studentList");
        for (int i = 0; i < GetSecretKey_studentlist_Response.length(); i++) {
            JSONObject obj = GetSecretKey_studentlist_Response.getJSONObject(i);
            String name = obj.getString("studentName");
            String app_no = obj.getString("applicationNo");
            String reasonForAbsent_Id = obj.getString("reasonForAbsent_Id");
            attendance_status_str = obj.getString("attendanceStatus");
            HashMap<String,Object> map = new HashMap<>();
            Studentlist.add(map);
            map.put("name", name);
            map.put("app_no", app_no);
            map.put("reasonForAbsent_Id", reasonForAbsent_Id);
            String mobile_no = obj.getString("mobileNo");
            boolean isPresent = attendance_status_str.equals("Present");
            System.out.println("isPresent_bool"+isPresent);
            map.put("switch", isPresent);
            map.put("mobile_no", mobile_no);
            // Create and add the switch to the layout here
            Switch mySwitch = (Switch) LayoutInflater.from(getContext()).inflate(R.layout.custom_switch_layout, null);
            mySwitch.setId(i); // Set a unique ID for each switch

            // Check isPresent_bool and update switch colors accordingly
            if (isPresent) {
                mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.greenTextcolor)));
                mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
            } else {
                mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorred)));
                mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.lightred)));
            }
        }

        String key = GetSecretKey_Response.getString("secretKey");
        String total = GetSecretKey_Response.getString("studentTotalCount");
        String present = GetSecretKey_Response.getString("studentPresentCount");
        String absent = GetSecretKey_Response.getString("studentAbsentCount");
        String secret_key = key;

        int present_count = Integer.parseInt(present);
        int total_count = Integer.parseInt(total);
        int absent_Count = Integer.parseInt(absent);

        double presentPercent = (present_count * 100.0) / total_count;
        double absentPercent = (absent_Count * 100.0) / total_count;
        secret_key_tv.setText(secret_key);
        total_count_tv.setText(total);
        present_count_tv.setText(present);
        absent_count_tv.setText(absent);
        present_percent_tv.setText(String.format("%.0f %%", presentPercent));
        absent_percent_tv.setText(String.format("%.0f %%", absentPercent));
        System.out.println("is_attendance_completed_str"+ is_attendance_completed_str);
        if (is_attendance_completed_str.equals("0")) {
            studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                    new String[]{"name", "app_no", "mobile_no", "switch"},
                    new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1});
            student_lv.setAdapter(studentlist_adapter);
            student_lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    showToast("Cannot Mark Attendance until class is completed");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            System.out.println("0 is executed");
        } else {
            System.out.println("1 is executed");
            boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
            System.out.println("selected date"+ datevalue_str);
            System.out.println("datevalue_str : " + datevalue_str + isBefore);
            studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                    new String[]{"name", "app_no", "mobile_no", "switch"},
                    new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1})
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    String reasonForAbsent_Id = (String) updatedList.get(position).get("reasonForAbsent_Id");
                    // Find the relevant views
                    TextView student_name_tv = view.findViewById(R.id.student_name_tv);
                    TextView application_no_tv = view.findViewById(R.id.application_no_tv);
                    TextView mobile_no_tv = view.findViewById(R.id.mobile_no_tv);

                    // Reset the views to their default state
                    student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                    application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                    mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

                    // Apply conditional styling
                    if (!reasonForAbsent_Id.equals("0")) {
                        student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                        application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                        mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                    }

                    return view;
                }
            };

            student_lv.setAdapter(studentlist_adapter);
            student_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    student_lv.setEnabled(false);
                    if (!isBefore){
                        if (countDownTimer == null){
                            HashMap<String, Object> clickedItem = updatedList.get(position);
                            boolean isPresent = (boolean) clickedItem.get("switch");

                            if (!isPresent) {
                                // Extract necessary information from the clicked item
                                clicked_Student_name_str = (String) clickedItem.get("name");
                                clicked_Student_Appno_str = (String) clickedItem.get("app_no");
                                clicked_Student_Mobno_str = (String) clickedItem.get("mobile_no");
                                clicked_student_reasonID_str = (String) clickedItem.get("reasonForAbsent_Id");
                                System.out.println("onclick in Update_Students_list is executed");
                                // Show popup for items where isPresent_bool is false
                                System.out.println("onclick listener for student_lv is called: " +
                                        clicked_Student_name_str + " " + clicked_Student_Appno_str + " " + clicked_Student_Mobno_str);
                                //show_OTP_Popup();
                                show_OTP_Popup();
                            } else {
                                showToast("Cannot Mark attendance for Present student");
                            }
                        } else {
                            showToast("Complete the ongoing class");
                        }
                    } else {
                        showToast("Cannot mark attendance for Previous dates");
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            student_lv.setEnabled(true);
                        }
                    }, 1000);
                }
            });
        }
        Refresh_stuList_iv.setEnabled(true);
           /* studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                    new String[]{"name", "app_no", "mobile_no", "switch"},
                    new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1});

            student_lv.setAdapter(studentlist_adapter);*/
    }

    // Deprecated Code


    // update student list when refresh is clicked
    public class AsyncCallWS_Update_student_list extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    currentlatitude = Double.valueOf(passed_lat);
                    currentlongitude = Double.valueOf(passed_long);
                    //http://testingrdp.dfindia.org:9000/api/skillattendance/getsecretkey?Employee_Id=1191&CollegeName=HARIHAR - SGRKS COLLEGE FOR WOMEN&Cohort=DVG-HARIHAR-SGRKSWOMEN-01&SubjectId=1&SubjectName=LEAD&Date=2023-11-10&Latitude=14.508168741&Longitude=75.80708036
                    //url = new URL(myUrl+main_id+"&Latitude="+currentlatitude+"&Longitude="+currentlongitude);
                    //String GetSecretKey_URL = "http://testingrdp.dfindia.org:9000/api/skillattendance/getsecretkey?Employee_Id=1191&CollegeName=";
                    url = new URL(Class_URL.API_URL+ "getsecretkey?Employee_Id=" +emp_id+"&CollegeName="+ college_selected_str +"&Cohort="+
                            cohort_selected_str +"&SubjectId="+ selectedSubjectId_str +"&SubjectName="+
                            subject_selected_str +"&Date="+ datevalue_str +"&Latitude="+currentlatitude+
                            "&Longitude="+currentlongitude+"&MainTopicId="+ selectedTopicId_str +"&MainTopicName="+
                            topic_selected_str +"&SubTopicJson="+ subtopic_jsonResult_str+"&ClassMode_Id="+
                            selected_classmodeId_str+"&ClassType="+class_typr_str);
                    System.out.println("myurl in Update_student_list: "+url);
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening
            showProgressDialog("Fetching class Details");

        }
        @Override
        protected void onPostExecute(String s) {

            // dismiss the progress dialog after receiving data from API
            dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(s);
                //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                updateStudentList(Studentlist);
                super.onPostExecute(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void updateStudentList(List<HashMap<String, Object>> updatedList) throws JSONException {
            Studentlist.clear();
            System.out.println("updateStudentList is called");
            GetSecretKey_studentlist_Response = GetSecretKey_Response.getJSONArray("studentList");
            for (int i = 0; i < GetSecretKey_studentlist_Response.length(); i++) {
                JSONObject obj = GetSecretKey_studentlist_Response.getJSONObject(i);
                String name = obj.getString("studentName");
                String app_no = obj.getString("applicationNo");
                String reasonForAbsent_Id = obj.getString("reasonForAbsent_Id");
                attendance_status_str = obj.getString("attendanceStatus");
                HashMap<String,Object> map = new HashMap<>();
                Studentlist.add(map);
                map.put("name", name);
                map.put("app_no", app_no);
                map.put("reasonForAbsent_Id", reasonForAbsent_Id);
                String mobile_no = obj.getString("mobileNo");
                boolean isPresent = attendance_status_str.equals("Present");
                System.out.println("isPresent_bool"+isPresent);
                map.put("switch", isPresent);
                map.put("mobile_no", mobile_no);
                // Create and add the switch to the layout here
                Switch mySwitch = (Switch) LayoutInflater.from(getContext()).inflate(R.layout.custom_switch_layout, null);
                mySwitch.setId(i); // Set a unique ID for each switch

                // Check isPresent_bool and update switch colors accordingly
                if (isPresent) {
                    mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.greenTextcolor)));
                    mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
                } else {
                    mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorred)));
                    mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.lightred)));
                }
            }

            String key = GetSecretKey_Response.getString("secretKey");
            String total = GetSecretKey_Response.getString("studentTotalCount");
            String present = GetSecretKey_Response.getString("studentPresentCount");
            String absent = GetSecretKey_Response.getString("studentAbsentCount");
            String secret_key = key;

            int present_count = Integer.parseInt(present);
            int total_count = Integer.parseInt(total);
            int absent_Count = Integer.parseInt(absent);

            double presentPercent = (present_count * 100.0) / total_count;
            double absentPercent = (absent_Count * 100.0) / total_count;
            secret_key_tv.setText(secret_key);
            total_count_tv.setText(total);
            present_count_tv.setText(present);
            absent_count_tv.setText(absent);
            present_percent_tv.setText(String.format("%.0f %%", presentPercent));
            absent_percent_tv.setText(String.format("%.0f %%", absentPercent));
            System.out.println("is_attendance_completed_str"+ is_attendance_completed_str);
            if (is_attendance_completed_str.equals("0")) {
                studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                        new String[]{"name", "app_no", "mobile_no", "switch"},
                        new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1});
                student_lv.setAdapter(studentlist_adapter);
                student_lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        showToast("Cannot Mark Attendance until class is completed");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                System.out.println("0 is executed");
            } else {
                System.out.println("1 is executed");
                boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                System.out.println("selected date"+ datevalue_str);
                System.out.println("datevalue_str : " + datevalue_str + isBefore);
                studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                        new String[]{"name", "app_no", "mobile_no", "switch"},
                        new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1})
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        String reasonForAbsent_Id = (String) updatedList.get(position).get("reasonForAbsent_Id");
                        // Find the relevant views
                        TextView student_name_tv = view.findViewById(R.id.student_name_tv);
                        TextView application_no_tv = view.findViewById(R.id.application_no_tv);
                        TextView mobile_no_tv = view.findViewById(R.id.mobile_no_tv);

                        // Reset the views to their default state
                        student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

                        // Apply conditional styling
                        if (!reasonForAbsent_Id.equals("0")) {
                            student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                        }

                        return view;
                    }
                };

                student_lv.setAdapter(studentlist_adapter);
                student_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        student_lv.setEnabled(false);
                        if (!isBefore){
                            if (countDownTimer == null){
                                HashMap<String, Object> clickedItem = updatedList.get(position);
                                boolean isPresent = (boolean) clickedItem.get("switch");

                                if (!isPresent) {
                                    // Extract necessary information from the clicked item
                                    clicked_Student_name_str = (String) clickedItem.get("name");
                                    clicked_Student_Appno_str = (String) clickedItem.get("app_no");
                                    clicked_Student_Mobno_str = (String) clickedItem.get("mobile_no");
                                    clicked_student_reasonID_str = (String) clickedItem.get("reasonForAbsent_Id");
                                    System.out.println("onclick in Update_Students_list is executed");
                                    // Show popup for items where isPresent_bool is false
                                    System.out.println("onclick listener for student_lv is called: " +
                                            clicked_Student_name_str + " " + clicked_Student_Appno_str + " " + clicked_Student_Mobno_str);
                                    //show_OTP_Popup();
                                    show_OTP_Popup();
                                } else {
                                    showToast("Cannot Mark attendance for Present student");
                                }
                            } else {
                                showToast("Complete the ongoing class");
                            }
                        } else {
                            showToast("Cannot mark attendance for Previous dates");
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                student_lv.setEnabled(true);
                            }
                        }, 1000);
                    }
                });
            }
            Refresh_stuList_iv.setEnabled(true);
           /* studentlist_adapter = new SimpleAdapter(getActivity(), updatedList, R.layout.student_list,
                    new String[]{"name", "app_no", "mobile_no", "switch"},
                    new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1});

            student_lv.setAdapter(studentlist_adapter);*/
        }
    }

    // Send otp to Student post API integration
 /*   private void Send_OTP() {
        new Send_OTP().execute();
    }
    private class Send_OTP extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String attendanceMainId = attendance_main_id_str;
            String response = "";

            try {
                //String apiUrl = "http://testingrdp.dfindia.org:9000/api/skillattendance/SendOTP?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+clicked_Student_Appno_str+"&StudentName="+clicked_Student_name_str+"&SubjectName="+subject_selected_str+"&MobileNo=9686616171";
                if (subject_selected_str == null){
                    subject_selected_str = info_Subject;
                }
                String apiUrl = Class_URL.API_URL+"SendOTP?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+ clicked_Student_Appno_str +"&StudentName="+ clicked_Student_name_str +"&SubjectName="+ subject_selected_str +"&MobileNo="+ clicked_Student_Mobno_str;
                URL url = new URL(apiUrl);
                Log.e("sendOTP Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String otpsent_Status = jsonObject.optString("status");
                showToast(jsonObject.getString("message"));
                if (otpsent_Status.equals("true")){

                }
                System.out.println("otpsent_Status"+otpsent_Status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Update student Attendance through sms post API Integration
    private void OTP_Attendance_Update() {
        new OTP_Attendance_Update().execute();
    }

    private class OTP_Attendance_Update extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String attendanceMainId = attendance_main_id_str;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL+"OTPAttendanceUpdate?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+ clicked_Student_Appno_str +"&OTP="+ manulotp_trainer_str;
                URL url = new URL(apiUrl);
                Log.e("OTP_Attendance_Update Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String otpsent_Status = jsonObject.optString("status");
                showToast(jsonObject.getString("message"));
                if (otpsent_Status.equals("true")){
                    if (isInternetPresent) {
                        class_typr_str = "Exists";
                        AsyncCallWS_Update_student_list updateStudentList = new AsyncCallWS_Update_student_list();
                        updateStudentList.execute();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
                System.out.println("otpsent_Status"+otpsent_Status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

*/
    // Trainer summary list for Info Button
/*

    private void Updated_summary_list() {
        new Updated_summary_list().execute();
    }
    public class Updated_summary_list extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL+"getattendancesummary?Employee_Id="+emp_id+"&Date="+ datevalue_str);
                    System.out.println("myurl in Updated_summary_list: "+url);
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening
            showProgressDialog("processing results");

        }
        @Override
        protected void onPostExecute(String s) {

            // dismiss the progress dialog after receiving data from API
            dismissProgressDialog();
            try {
                boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                JSONObject jsonObject = new JSONObject(s);
                //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                //GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                JSONArray summary_array = jsonObject.getJSONArray("employeeAttendanceSummary");
                Summarylist.clear();
                for (int i = 0; i < summary_array.length(); i++) {
                    JSONObject obj = summary_array.getJSONObject(i);//sets to custom position
                    String collegeName = obj.getString("collegeName");
                    String semesterName = obj.getString("semesterName");
                    String subjectName = obj.getString("subjectName");
                    String topicName = obj.getString("topicName");
                    String attendanceMain_Id = obj.getString("attendanceMain_Id");
                    String classStartTime = obj.getString("classStartTime");
                    String classEndTime = obj.getString("classEndTime");
                    String attendanceStarttime = obj.getString("attendanceStartDate");
                    String attendanceEndtime = obj.getString("attendanceEndDate");
                    String totalStudents = obj.getString("totalStudents");
                    String totalPresent = obj.getString("totalPresent");
                    String totalAbsent = obj.getString("totalAbsent");
                    String classMode = obj.getString("classMode");
                    String classMode_Id = obj.getString("classMode_Id");
                    String classNotTakenReason = obj.getString("classNotTakenReason");
                    String headCount = obj.getString("headCount");
                    String isimageupdate = obj.getString("isImageUpdated");
                    imageUrl = obj.optString("image_URL");

                    if (collegeName.equals("null")){
                        no_summaryvalues_tv.setVisibility(View.VISIBLE);
                        no_summaryvalues_tv.setText("No Data for this Date");
                        summary_lv.setVisibility(View.GONE);
                    } else {
                        no_summaryvalues_tv.setVisibility(View.GONE);
                        summary_lv.setVisibility(View.VISIBLE);
                    }

                    HashMap<String, String> summary_map = new HashMap();
                    Summarylist.add(summary_map);
                    summary_map.put("collegeName", collegeName);
                    summary_map.put("classmode", classMode);
                    summary_map.put("classMode_Id", classMode_Id);
                    summary_map.put("semesterName", semesterName);
                    summary_map.put("subjectName", subjectName);
                    summary_map.put("topicName", topicName);
                    summary_map.put("attendanceMain_Id", attendanceMain_Id);
                    summary_map.put("classNotTakenReason", classNotTakenReason);
                    summary_map.put("classStartTime", classStartTime);
                    summary_map.put("classEndTime", classEndTime);
                    summary_map.put("attendanceStarttime", attendanceStarttime);
                    summary_map.put("attendanceEndtime", attendanceEndtime);
                    summary_map.put("totalStudents", totalStudents);
                    summary_map.put("totalPresent", totalPresent);
                    summary_map.put("totalAbsent", totalAbsent);
                    summary_map.put("headCount", headCount);
                    summary_map.put("image_URL", imageUrl);
                    summary_map.put("isImageUpdated", isimageupdate);
                }
                System.out.println("Summarylist"+Summarylist);
                //JSONObject val1summary_array = summary_array.getJSONObject(0);
                //System.out.println("jsonArray"+jsonArray);
                ListAdapter summary_adapter = new SimpleAdapter(getActivity(),Summarylist,R.layout.summary_list,
                        new String[]{"collegeName","classmode", "semesterName","subjectName", "topicName","attendanceMain_Id",
                                "classNotTakenReason", "classStartTime", "classEndTime","attendanceStarttime",
                                "attendanceEndtime", "totalStudents", "totalPresent", "totalAbsent", "headCount",
                                "isImageUpdated"},
                        //use same id here, that you used in map, case sensitive
                        new int[]{R.id.college_name_tv, R.id.classMode_tv, R.id.semester_tv, R.id.subject_tv, R.id.topic_tv,
                                R.id.mainId_tv,R.id.classNotTaken_reason_tv, R.id.classStartTime_tv, R.id.classEndTime_tv, R.id.attendanceStartTime_tv,
                                R.id.attendanceEndTime_tv, R.id.total_students_tv, R.id.present_students_tv,
                                R.id.absent_students_tv, R.id.head_count, R.id.imgupdated})
                {
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
    JSONObject configurable = summary_array.getJSONObject("employeeAttendanceSummary");
                        JSONArray configurablesArray = configurable.getJSONArray("configurables");
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
                };
                update_summary_list_iv.setEnabled(true);
                //use same path here, id for id, name for name
                summary_lv.setAdapter(summary_adapter);
                super.onPostExecute(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/

    // Trainer summary list for capturing last class image updated value Reading

    /*
    private void Trainee_summary_list() {
        new Trainee_summary_list().execute();
    }
    public class Trainee_summary_list extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {

                    url = new URL(Class_URL.API_URL + "getattendancesummary?Employee_Id=" + emp_id + "&Date=" + datevalue_str);
                    System.out.println("myurl in Trainee_summary_list: " + url);

                    // Open a URL connection
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
                        result.append((char) data);
                        data = isw.read();
                    }

                    // Return the data to onPostExecute method
                    return result.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Display a progress dialog to show the user what is happening
            showProgressDialog("Processing results...");
        }

        @Override
        protected void onPostExecute(String s) {
            // Dismiss the progress dialog after receiving data from API
            dismissProgressDialog();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray summaryArray = jsonObject.getJSONArray("employeeAttendanceSummary");

                if (summaryArray.length() > 0) {
                    // Get the first object in the array
                    JSONObject obj = summaryArray.getJSONObject(0);
                    isImageUpdated = obj.getInt("isImageUpdated");
                    attendance_main_id_str = obj.getString("attendanceMain_Id");
                    selected_classmodeId_str = obj.getString("classMode_Id");
                    JSONArray Configurable_array = obj.getJSONArray("configurables");
                    for (int j = 0; j < Configurable_array.length(); j++) {
                        JSONObject configurable = Configurable_array.getJSONObject(j);
                        imageUploadVisible = configurable.getInt("imageUploadVisible");
                        allowImageReupload = configurable.getInt("allowImageReupload");
                        imageUploadMandatory = configurable.getInt("imageUploadMandatory");
                    }

                    Log.e("isImageUpdated: ", String.valueOf(isImageUpdated));
                    Log.e("imageUploadVisible: ", String.valueOf(imageUploadVisible));
                    Log.e("allowImageReupload: ", String.valueOf(allowImageReupload));
                    Log.e("imageUploadMandatory: ", String.valueOf(imageUploadMandatory));
                    Log.e("Read ID: ", attendance_main_id_str);


                    // Check conditions and set visibility of imageUpload_iv accordingly
                    if ((isImageUpdated == 0 && allowImageReupload == 1 && imageUploadVisible == 1) ||
                            (isImageUpdated == 1 && allowImageReupload == 1 && imageUploadVisible == 1) ||
                            (isImageUpdated == 0 && allowImageReupload == 0 && imageUploadVisible == 1)) {
                        if (screen_str != null) {
                            if (screen_str.equals("completeclass") || screen_str.equals("main")) {
                                imageUpload_iv.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        imageUpload_iv.setVisibility(View.GONE);
                    }

                    Log.d("isImageUpdated", String.valueOf(isImageUpdated));
                } else {
                    // Handle case when the array is empty
                    imageUpload_iv.setVisibility(View.GONE);
                    Log.d("onPostExecute", "Summary array is empty");
                }

                super.onPostExecute(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/


    //Check Secret key status

  /*  private void Check_Secret_Key_Status() {
        new Check_Secret_Key_Status().execute();
    }
*/
/*
    public class Check_Secret_Key_Status extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings)
        {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    //E&C EC&K
                    */
/*if (subject_selected_str.equals("E&C")){
                        subject_selected_str="EC&K";
                    }*/
    /*

                    //college_selected_str = college_selected_str.replace("&", "%26");

                    //college_selected_str= "HANAGAL - GFGC PG CENTRE";
                    //college_selected_str= "HANAGAL  GFGC PG CENTRE";
                    //college_selected_str= "  HANAGAL  GFGC PG @@ CENTRE";
                    //college_selected_str= "HANAGAL && GFGC PG &CENTRE";
                    //college_selected_str= "HANAGAL _ GFGC - PG & CENTRE";

                    // HANAGAL - GFGC PG & CENTRE
                    //

                    if(college_selected_str.contains("&")) {
                        try {
                            college_selected_str = URLEncoder.encode(college_selected_str, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    Log.e("collegename",college_selected_str);//
                    //HANAGAL+-+GFGC+PG+CENTRE
                    //HANAGAL++GFGC+PG+CENTRE
                    //++HANAGAL++GFGC+PG+%40%40+CENTRE
                    //HANAGAL+%26%26+GFGC+PG+%26CENTRE
                    //HANAGAL+GFGC+PG+%26+CENTRE
                    //HANAGAL_GFGC+PG+%26+CENTRE
                    //HANAGAL+_+GFGC+-+PG+%26+CENTRE
                    //http://testingrdp.dfindia.org:9000/api/skillattendance/checksecretkeystatus?Employee_Id=
                    url = new URL(Class_URL.API_URL+"checksecretkeystatus?Employee_Id="+emp_id+"&CollegeName="+ college_selected_str +
                            "&Cohort="+ cohort_selected_str +"&SubjectId="+ selectedSubjectId_str +"&SubjectName="+
                            subject_selected_str +"&Date="+ datevalue_str + "&ClassMode_Id="+ selected_classmodeId_str +
                            "&TopicId="+ selectedTopicId_str);
                    System.out.println("Check_Secret_Key_Status url: "+url);
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening
            showProgressDialog("Checking status");
        }
        @Override
        protected void onPostExecute(String s) {

            // dismiss the progress dialog after receiving data from API
            dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(s);
                //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                secret_key_status_str = jsonObject.getString("message");
                System.out.println("secret_key_status_str" + secret_key_status_str);
                boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                System.out.println("selected date" + datevalue_str);
                if (secret_key_status_str.equals("New"))
                {
                    class_typr_str = "New";
                    summaryHeading_tv.setText("Summary");
                    summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                    if (isBefore){
                        previous_date_tv.setVisibility(View.VISIBLE);
                        System.out.println("Selected Previous date");
                        classstarted_bt.setEnabled(false);
                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        classscompleted_bt.setEnabled(false);
                        classscompleted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        start_attendance_btn.setEnabled(false);
                        start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
                    } else {
                        previous_date_tv.setVisibility(View.GONE);
                        if (selected_classmodeId_str.equals("1") || selected_classmodeId_str.equals("3")){
                            out_of_office_tv.setVisibility(View.GONE);
                            if (soapobject_recentattendance_response != null) {
                                if (soapobject_recentattendance_response.getProperty("Message").toString().equalsIgnoreCase("There are no records")) {
                                    classstarted_bt.setEnabled(true);
                                    classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                }

                                if (soapobject_recentattendance_response.getProperty("Message").toString().
                                        equalsIgnoreCase("Success")) {
                                    if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                            equalsIgnoreCase("Class Started") &&
                                            soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                                    equalsIgnoreCase("0")) {
                                        classstarted_bt.setEnabled(false);
                                        classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                                    } else {
                                        classstarted_bt.setEnabled(true);
                                        classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                                    }
                                }
                            }
                        } else {
                            passed_lat = sharedPreferences.getString("passed_lat", "0.0");
                            passed_long = sharedPreferences.getString("passed_long", "0.0");
                            if (passed_lat.equals("0.0")) {
                                location_not_set_tv.setVisibility(View.VISIBLE);
                                gpstracker_obj = new GPSTracker(getActivity());
                                if (gpstracker_obj.canGetLocation())
                                {
                                    Intent i = new Intent(getActivity(), Trainer_Geofence.class);
                                    TrainerGeofenceResultLauncher.launch(i);
                                } else {
                                    gpstracker_obj.showSettingsAlert();
                                    settingsDialogShown = true;
                                }
                            } else {
                                location_not_set_tv.setVisibility(View.GONE);
                                AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                                task1.execute();
                            }
                        }


                       */
/* if (passed_lat.equals("0.0")) {
                            location_not_set_tv.setVisibility(View.VISIBLE);
                            Intent i = new Intent(getActivity(), Trainer_Geofence.class);
                            startActivity(i);
                        } else {
                            location_not_set_tv.setVisibility(View.GONE);
                            AsyncTask_GPSFetching task1 = new AsyncTask_GPSFetching(getActivity());
                            task1.execute();
                        }
                        fetch_Topic(subject_id_str);*/
    /*

                    }
                } else if (secret_key_status_str.equals("Exists")) {
                    class_typr_str = "Exists";
                    summaryHeading_tv.setText("Class Completed Summary");
                    summaryHeading_tv.setTextColor(ContextCompat.getColor(context, R.color.purple_500));
                    location_not_set_tv.setVisibility(View.GONE);
                    //fetch_Topic(subject_id_str);
                    if (!isBefore) {
                        if (soapobject_recentattendance_response.getProperty("Attendance").toString().
                                equalsIgnoreCase("Class Started") &&
                                soapobject_recentattendance_response.getProperty("IsAttendanceTaken").toString().
                                        equalsIgnoreCase("0")) {
                            classstarted_bt.setEnabled(false);
                            classstarted_bt.setTextColor(getResources().getColor(R.color.light_gray));
                        } else {
                            classstarted_bt.setEnabled(true);
                            classstarted_bt.setTextColor(getResources().getColor(R.color.greenTextcolor));
                        }
                    }
                    dateEdt_tv.setEnabled(false);
                    cohort_spinner.setEnabled(false);
                    ClassMode_spinner.setEnabled(false);
                    college_name_Spinner.setEnabled(false);
                    subject_spinner.setEnabled(false);
                    Topic_spinner.setEnabled(false);
                    selectedsubtopics_et.setEnabled(false);
                    System.out.println("MyAsyncTasks in exists");
                    if (isInternetPresent) {
                        AsyncCallWS_GetSecretKey asyncCallWSGetSecretKey = new AsyncCallWS_GetSecretKey();
                        asyncCallWSGetSecretKey.execute();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }

                super.onPostExecute(s);

                //present_percent_tv.setText(presentPercent.)
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

*/

    /*private void AsyncCall_StudentAttendance_list(String attendance_mainid, String Class_index) {
        new AsyncCall_StudentAttendance_list().execute(attendance_mainid, Class_index);
    }

    public class AsyncCall_StudentAttendance_list extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            attendance_mainid = params[0];
            Class_index = params[1];
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL+"getstudentslist?AttendanceMain_Id="+attendance_mainid);
                    System.out.println("myurl in Updated_summary_list: "+url);
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);

        }
        @Override
        protected void onPostExecute(String s) {

            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                //JSONArray jsonArray1 = jsonObject.getJSONArray("login");
                //GetSecretKey_Response = jsonObject.getJSONObject("attendanceResponse");
                JSONArray studentAttendance_array = jsonObject.getJSONArray("studentList");
                StudentAttendancelist.clear();
                for (int i = 0; i < studentAttendance_array.length(); i++) {
                    JSONObject obj = studentAttendance_array.getJSONObject(i);
                    String name = obj.getString("studentName");
                    String app_no = obj.getString("applicationNo");
                    String reasonForAbsent_Id = obj.getString("reasonForAbsent_Id");
                    attendance_status_str = obj.getString("attendanceStatus");
                    HashMap<String,Object> map = new HashMap<>();
                    StudentAttendancelist.add(map);
                    map.put("name", name);
                    map.put("app_no", app_no);
                    map.put("reasonForAbsent_Id", reasonForAbsent_Id);
                    String mobile_no = obj.getString("mobileNo");
                    boolean isPresent = attendance_status_str.equals("Present");
                    System.out.println("isPresent_bool"+isPresent);
                    map.put("switch", isPresent);
                    map.put("mobile_no", mobile_no);
                    // Create and add the switch to the layout here
                    Switch mySwitch = (Switch) LayoutInflater.from(getContext()).inflate(R.layout.custom_switch_layout, null);
                    mySwitch.setId(i); // Set a unique ID for each switch
                    // Check isPresent_bool and update switch colors accordingly
                    if (isPresent) {
                        mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.greenTextcolor)));
                        mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
                    } */
    /*else {
                        if (reasonForAbsent_Id.equals("0")){
                            Log.e("reasonForAbsent_Id", reasonForAbsent_Id);
                            mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorred)));
                            mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.lightred)));
                        } else {
                            Log.e("reasonForAbsent_Id", reasonForAbsent_Id);
                            mySwitch.getThumbDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
                            mySwitch.getTrackDrawable().setTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.table_code)));
                        }
                    }*/
    /*
                }
                studentlist_adapter = new SimpleAdapter(getActivity(), StudentAttendancelist, R.layout.studentsummary_list,
                        new String[]{"name", "app_no", "mobile_no", "switch"},
                        new int[]{R.id.student_name_tv, R.id.application_no_tv, R.id.mobile_no_tv, R.id.checkBox1}) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        // Get the data for this position
                        String reasonForAbsent_Id = (String) StudentAttendancelist.get(position).get("reasonForAbsent_Id");

                        // Find the relevant views
                        TextView student_name_tv = view.findViewById(R.id.student_name_tv);
                        TextView application_no_tv = view.findViewById(R.id.application_no_tv);
                        TextView mobile_no_tv = view.findViewById(R.id.mobile_no_tv);

                        // Reset the views to their default state
                        student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));
                        mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.black));

                        // Apply conditional styling
                        if (!reasonForAbsent_Id.equals("0")) {
                            student_name_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            application_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                            mobile_no_tv.setTextColor(ContextCompat.getColor(context, R.color.colorred));
                        }

                        return view;
                    }
                };

// Use the adapter with the ListView
                studentPopup_lv.setAdapter(studentlist_adapter);

                boolean isBefore = isDateBeforeCurrentDate(datevalue_str);
                studentPopup_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        studentPopup_lv.setEnabled(false);
                        if (!isBefore){
                            if (countDownTimer == null) {
                                if (Class_index.equals("0")){
                                    HashMap<String, Object> clickedItem = StudentAttendancelist.get(position);
                                    boolean isPresent = (boolean) clickedItem.get("switch");

                                    if (!isPresent) {
                                        // Extract necessary information from the clicked item
                                        clicked_Student_name_str = (String) clickedItem.get("name");
                                        clicked_Student_Appno_str = (String) clickedItem.get("app_no");
                                        clicked_Student_Mobno_str = (String) clickedItem.get("mobile_no");
                                        clicked_student_reasonID_str = (String) clickedItem.get("reasonForAbsent_Id");

                                        Log.e("Reason ID", clicked_student_reasonID_str);
                                        System.out.println("onclick in Async task is executed");
                                        // Show popup for items where isPresent_bool is false
                                        System.out.println("onclick listener for student_lv is called: " +
                                                clicked_Student_name_str + " " + clicked_Student_Appno_str + " " + clicked_Student_Mobno_str);
                                        //show_OTP_Popup();
                                        show_OTP_Popup();
                                    } else {
                                        showToast("No Manual attendance for Present Student");
                                    }
                                } else {
                                    showToast("Cannot mark attendance for Previous Classes");
                                }
                            } else {
                                Log.e("timer", countDownTimer.toString());
                                showToast("Complete the ongoing class");
                            }
                        } else {
                            showToast("Cannot mark attendance for Previous dates");
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                studentPopup_lv.setEnabled(true);
                            }
                        }, 1000);
                    }
                });
                super.onPostExecute(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/

    //Post API to close the attendance
    /*private void Perform_Attendance_complete() {
        new Attendance_complete().execute();
    }

    private class Attendance_complete extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String attendanceMainId = attendance_main_id_str;
            String Reason = Reason_str;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL+"completeattendance?AttendanceMain_Id=" + attendanceMainId + "&Remark=" + Reason;
                URL url = new URL(apiUrl);
                Log.e("API Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
        }
    }
*/
    //API integration for Sub-topic list
    /*   private void Get_subtopic_list() {
        new Get_subtopic_list().execute();
    }
    public class Get_subtopic_list extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL+"getsubtopiclist?TopicMain_Id="+ selectedTopicId_str);
                    System.out.println("Get_subtopic_list url: "+url);
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening
            showProgressDialog("processing results");
        }
        @Override
        protected void onPostExecute(String s) {
            dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(s);
                super.onPostExecute(s);
                JSONArray subTopicsArray = jsonObject.getJSONArray("subTopics");
                subTopic_List = new ArrayList<>();

                for (int i = 0; i < subTopicsArray.length(); i++) {
                    JSONObject subTopicObj = subTopicsArray.getJSONObject(i);
                    String subTopicId = subTopicObj.getString("subTopicId");
                    String subTopicName = subTopicObj.getString("subTopicName");
                    SubTopic_SM subTopic = new SubTopic_SM(subTopicId, subTopicName);
                    subTopic_List.add(subTopic);
                }
                filtered_subtopic_AL = subTopic_List.stream().map(SubTopic_SM::getSubTopicName).collect(Collectors.toCollection(ArrayList::new));
                //checkedItems_set = new HashSet<>();
                for (SubTopic_SM subTopic : subTopic_List) {
                    System.out.println("Subtopic_list" + subTopic.getSubTopicName() + " - " + subTopic.getSubTopicId());
                }
                subtopic_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, filtered_subtopic_AL);
                subtopics_lv.setAdapter(subtopic_adapter);
                subtopics_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
// Check previously selected items
                for (int i = 0; i < filtered_subtopic_AL.size(); i++) {
                    if (checkedItems_set.contains(filtered_subtopic_AL.get(i))) {
                        subtopics_lv.setItemChecked(i, true);
                    }
                }
                System.out.println("filtered_subtopic_AL : " + filtered_subtopic_AL);
                System.out.println("Checked items : " + checkedItems_set);
                subtopics_lv.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedItem = filtered_subtopic_AL.get(position);
                    if (subtopics_lv.isItemChecked(position)) {
                        checkedItems_set.add(selectedItem);
                    } else {
                        checkedItems_set.remove(selectedItem);
                    }
                });
                System.out.println("Checked items : " + checkedItems_set);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
*/
    // Mark Attendance based on QR Code
    /* private void QR_Attendance_Update() {
        new QR_Attendance_Update().execute();
    }

    private class QR_Attendance_Update extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String attendanceMainId = attendance_main_id_str;
            String applicationNo = scanned_appNo_str;
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL+"QRCodeAttendanceUpdate?AttendanceMain_Id="+attendanceMainId+"&Employee_Id="+emp_id+"&Application_No="+applicationNo;
                URL url = new URL(apiUrl);
                Log.e("QR_Attendance_Update Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String otpsent_Status = jsonObject.optString("status");
                showToast(jsonObject.getString("message"));
                if (otpsent_Status.equals("true")){
                    if (isInternetPresent) {
                        class_typr_str = "Exists";
                        AsyncCallWS_Update_student_list updateStudentList = new AsyncCallWS_Update_student_list();
                        updateStudentList.execute();

                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Internet Connection")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
                System.out.println("otpsent_Status"+otpsent_Status);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/
    // Get Attendance Start time and end time
    /*private void AsyncCallWS_AttendanceStartEndClass(String lastInTime_str, String lastOutTime_str) {
        new AsyncCallWS_AttendanceStartEndClass().execute(lastInTime_str, lastOutTime_str);
    }

    private class AsyncCallWS_AttendanceStartEndClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String lastInTime_str = params[0];
            String lastOutTime_str = params[1];
            String response = "";

            try {
                String apiUrl = Class_URL.API_URL+"AttendanceStartEndClass?AttendanceMain_Id="+attendance_main_id_str+"&ClassStartTime="+lastInTime_str+"&ClassEndTime=" + lastOutTime_str;
                URL url = new URL(apiUrl);
                Log.e("StartEnd link", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
        }
    }
*/
    // Update class Id
    /* private void AsyncCallWS_Update_ClassId() {
        new AsyncCallWS_Update_ClassId().execute();
    }
    private class AsyncCallWS_Update_ClassId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            long long_userid = Long.parseLong(str_classstarted_id);
//(long ClassStartedId, int IsAttendanceStarted)
            try {
                String apiUrl = Class_URL.API_URL+"updatehrmsattendancestarted?ClassStartedId="+long_userid+"&IsAttendanceStarted=1";
                URL url = new URL(apiUrl);
                Log.e("API Link: ", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
            AsyncCall_Validate_ClassAttendance task1 = new AsyncCall_Validate_ClassAttendance(context);
            task1.execute();
        }
    }
*/
    //Save_classnottaken_Reason
    /* private void AsyncCallWS_Save_classnottaken_Reason() {
        new AsyncCallWS_Save_classnottaken_Reason().execute();
    }

    private class AsyncCallWS_Save_classnottaken_Reason extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //http://testingrdp.dfindia.org:9000/api/skillattendance/StudentSaveDeviceReason?Reason_Id=1&Application_No=230021033KC&DeviceSlno=022299222&Remark=given for service
            //String attendanceMainId = attendance_main_id;
            String response = "";

            try {
                //http://testingrdp.dfindia.org:9000/api/skillattendance/classnottakenreasonremarks?
                // AttendanceMain_Id=12&ReasonId=1&Remark=adfasdfasdf
                String ClassNotTaken_Reason_url = Class_URL.API_URL+"classnottakenreasonremarks?AttendanceMain_Id="+
                        attendance_main_id_str +"&ReasonId="+ reason_id_str +"&Remark="+ reason_remark_tv;
                URL url = new URL(ClassNotTaken_Reason_url);
                Log.d("classNotTaken url", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                    Log.e("Not taken Response", response);
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            start_attendance_btn.setEnabled(false);
            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
            Log.e("API Response: ",result);
        }
    }
*/
    //Save_StudentAbsent_Reason
    /* private void AsyncCallWS_Save_StudentAbsent_Reason() {
        new AsyncCallWS_Save_StudentAbsent_Reason().execute();
    }

    private class AsyncCallWS_Save_StudentAbsent_Reason extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            long absent_id = Long.parseLong(absentReason_selected_str);
            try {
                //http://testingrdp.dfindia.org:9000/api/skillattendance/submitabsentreason?
                // AttendanceMain_Id=12&ReasonId=1&Remark=adfasdfasdf
                //long AttendanceMain_Id, string ApplicationNo, long AbsentReasonId absentReason_selected_str

                if (attendance_mainid == null){
                    attendance_mainid = attendance_main_id_str;
                }
                String ClassNotTaken_Reason_url = Class_URL.API_URL+"submitabsentreason?AttendanceMain_Id="+
                        attendance_mainid +"&ApplicationNo="+ clicked_Student_Appno_str +"&AbsentReasonId="+ absent_id;
                URL url = new URL(ClassNotTaken_Reason_url);
                Log.d("classNotTaken url", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                    Log.e("Student absent Response", response);
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String absentReason_Status = jsonObject.optString("status");
                showToast(jsonObject.getString("message"));
                if (absentReason_Status.equals("true")){
                    showToast(jsonObject.optString("message"));
                } else {
                    showToast("Could not update the Reason");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/
    // Delete Started class when Trainer Starts the class and wants to update not taken after starting the class
    /*   private void AsyncCallWS_DeleteStartedClass() {
        new DeleteStartedClass().execute();
    }

    private class DeleteStartedClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            long long_userid = Long.parseLong(str_classstarted_id);
            String response = "";

            try {
                String DeleteClass_URL = Class_URL.API_URL + "deleteclassstarted?ClassStartedId="+long_userid;
                URL url = new URL(DeleteClass_URL);
                Log.e("DeleteClass API:", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            start_attendance_btn.setEnabled(false);
            start_attendance_btn.setTextColor(getResources().getColor(R.color.light_gray));
            Log.e("API Response: ",result);
        }
    }

*/


    //Hotspot Attendance marking using the BSSID


/*
    private void startScan() {
        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }
    }
    private void scanSuccess() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<ScanResult> results = wifiManager.getScanResults();
        List<BSSIDData_Model> bssidDataList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (ScanResult result : results) {
            sb.append("SSID: ").append(result.SSID).append("\n")
                    .append("BSSID: ").append(result.BSSID).append("\n\n");
            String ssid = "SSID: " + result.SSID + "\nBSSID: " + result.BSSID;
            wifiList.add(ssid);

            BSSIDData_Model bssidData = new BSSIDData_Model(result.BSSID);
            bssidDataList.add(bssidData);
            */
/*sb.append("SSID: ").append(result.SSID).append("\n")
                    .append("BSSID: ").append(result.BSSID).append("\n")
                    .append("Capabilities: ").append(result.capabilities).append("\n")
                    .append("Frequency: ").append(result.frequency).append("\n")
                    .append("Level: ").append(result.level).append("\n\n");*/
    /*

        }

        // String[] stringArray = sb1.toString().split("\n\n");
        bssid_jsonResult_str = new Gson().toJson(bssidDataList);
        Log.e("Json Format", bssid_jsonResult_str);
        AsyncCallWS_markstudentattendancebssid();
        Log.e("Read BSSID", String.valueOf(sb));
        //wifiListTextView.setText(sb.toString());
    }

    private void scanFailure() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<ScanResult> results = wifiManager.getScanResults();
        showToast("Scan failed");
        // Handle failure: new scan did NOT succeed, handle according to your needs
    }


    // BSSID Attendance update

    //https://www.dfindia.org:81/api/skillattendance/markstudentattendancebssid?
    // AttendanceMain_Id=4659&BSSIDJson=[{"BSSID":"38:37:8b:b3:fa:f6"},{"BSSID":"72:ab:e0:2f:8c:8b"}]

    private void AsyncCallWS_markstudentattendancebssid() {
        new markstudentattendancebssid().execute();
    }

    private class markstudentattendancebssid extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //long long_userid = Long.parseLong(str_classstarted_id);
            String response = "";
            try {
                String markstudentattendancebssid_URL =
                        "https://www.dfindia.org:81/api/skillattendance/markstudentattendancebssid?AttendanceMain_Id="
                  +attendance_main_id_str+"&BSSIDJson="+bssid_jsonResult_str;
                URL url = new URL(markstudentattendancebssid_URL);
                Log.e("markstudentattendancebssid API:", String.valueOf(url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("API Response: ",result);
        }
    }

*/


}



