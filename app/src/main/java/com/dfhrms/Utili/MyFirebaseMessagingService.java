package com.dfhrms.Utili;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dfhrms.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.android..leadnew_22_6_2018.R;

//import static java.lang.System.lineSeparator;


/**
 * Created by User on 7/18/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";
    Bitmap url2bitmap=null;

    String str_titlewhr2go;

    NotificationCompat.Builder notificationBuilders;
    int x;



    public static final String  PREFBook_Stud= "prefbook_stud";  //sharedpreference Book
    public static final String PrefID_S_isprofileEdited = "prefid_isprofileEdited";
    SharedPreferences shardpref_S_obj;



    String str_isProfileEdited;


    Intent intent,intent_student,intent_pm,intent_events,intent_story,intent_request,intent_pm_request,intent_pm_tshirt;




    public static final String  PREFBook_LoginTrack= "prefbook_logintrack";  //sharedpreference Book
    public static final String PrefID_WhereToGo = "prefid_wheretogo"; //
    SharedPreferences shardprefLoginTrack_obj;
    String str_loginTrack;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        // Create and show notification

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilders = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

       /* notificationBuilders.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info");

        notificationManager.notify(*//*notification id*//*1, notificationBuilders.build());
*/
        x=1;
      //  notificationBuilders = new NotificationCompat.Builder(this);

        Log.e(TAG, "From: " + remoteMessage.getData().get("title"));
        // Log.e(TAG, "From: " + remoteMessage.getData().get("message").toString());

        System.out.println("TestTitle:" + remoteMessage.getData().get("tile"));

        Log.e(TAG, "Image: " + remoteMessage.getData().get("image"));


        shardpref_S_obj=this.getSharedPreferences(PREFBook_Stud, Context.MODE_PRIVATE);
        str_isProfileEdited = shardpref_S_obj.getString(PrefID_S_isprofileEdited, "").trim();
        Log.e("profileedited",str_isProfileEdited);

        shardprefLoginTrack_obj=getSharedPreferences(PREFBook_LoginTrack, Context.MODE_PRIVATE);
        str_loginTrack = shardprefLoginTrack_obj.getString(PrefID_WhereToGo, "").trim();

        Log.e("logintrack",str_loginTrack);

        // sendNotification(remoteMessage.getNotification().getBody());

       // Log.e(TAG, "From: " + remoteMessage.getData().get("message").toString());
        //sendNotification(remoteMessage.getData().get("title"));

       /* if (remoteMessage.getData().get("message").toString()!=null ||remoteMessage.getData().get("message").toString()!="" ||remoteMessage.getData().get("message").toString().length()!=0)
        {
            sendNotification(remoteMessage.getData().get("message"));
        }*/


        str_titlewhr2go =  remoteMessage.getData().get("title").trim();
        Log.e("notitle","notitle:"+str_titlewhr2go.toString());
        if(str_titlewhr2go.equals("empty")||str_titlewhr2go.equals("Empty")||str_titlewhr2go==null ||str_titlewhr2go==""||str_titlewhr2go.isEmpty())
        { str_titlewhr2go="empty";}
        else {    }


        String imageUrl = remoteMessage.getData().get("image");
        if(imageUrl.equals("empty")||imageUrl.equals("Empty")||imageUrl==null ||imageUrl==""||imageUrl.isEmpty())
        { // Log.e("Storyimage","imageURLText:"+imageUrl.toString());

        }
        else {
            //To get a Bitmap image from the URL received
            url2bitmap = getBitmapfromUrl(imageUrl);
        }

        sendNotification(remoteMessage.getData().get("message").trim());

    }

    @SuppressLint("NewApi")
    private void sendNotification(String messageBody)
    {
        Log.e("MessageBoby: ", " " + messageBody);

        if (messageBody == null || messageBody == "" || messageBody.isEmpty()) {
            Log.e("empty:", "Empty");
        } else
         {
            Log.e("else: ", " " + messageBody.toString());
            /*Bitmap bmp_image = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.aboutus);*/
            //bmp_image=null;

             int breakat =3;
             String splittedmessagex="";
             String[] messsagetobreak = messageBody.split("\\s+");
/*             for (int i = 0; i < messsagetobreak.length; i++)
             {
                 if(i==breakat)
                 {
                     breakat=breakat+4;
                     splittedmessagex= splittedmessagex+" "+messsagetobreak[i]+ System.lineSeparator();
                     Log.e("Finalx",splittedmessagex.toString());
                 }
                 else
                 { splittedmessagex=splittedmessagex+" "+messsagetobreak[i];}
             }

             messageBody=splittedmessagex;
             Log.e("SplitString",splittedmessagex.toString());
             Log.e("MessageString",messageBody.toString());



             if(str_loginTrack=="" || str_loginTrack==null ||(str_loginTrack.trim().length()==0)||str_loginTrack.trim().equals("com.leadcampusapp.LoginActivity"))
             {
                 intent = new Intent(this, MainActivity.class);
                 intent_student = intent_pm = intent_events = intent_story = intent_request = intent;
             }
             else
             {

                 if(str_titlewhr2go.equals("Manager"))
                 {
                     intent_pm = new Intent(this, PMHomeActivity.class);
                     intent_student = intent= intent_events = intent_story = intent_request =
                             intent_pm_request=intent_pm_tshirt=intent_pm;

                 }
                 if(str_titlewhr2go.equalsIgnoreCase("ApprovalLetter"))//ApprovalLetter
                 {
                     intent_pm_request = new Intent(this, PMRequestActivity.class);

                     intent_student = intent= intent_events = intent_story = intent_request =
                             intent_pm=intent_pm_tshirt=intent_pm_request;
                 }

                 if(str_titlewhr2go.equalsIgnoreCase("pmtshirt"))//pmtshirt
                 {
                     intent_pm_tshirt = new Intent(this, TShirtPaidActivity.class);

                     intent_student = intent= intent_events = intent_story = intent_request =
                             intent_pm=intent_pm_request=intent_pm_tshirt;
                 }


                 else
                {
                     if (str_isProfileEdited.equals("0"))
                     {
                         intent = new Intent(this, EditProfileActivity.class);
                         intent_student = intent_pm = intent_events = intent_story = intent_request=intent_pm_request=
                                 intent_pm_tshirt=intent_pm=intent;

                     } else {
                         intent = new Intent(this, LoginActivity.class);
                         intent_student = new Intent(this, HomeActivity.class);
                         intent_pm = new Intent(this, PMHomeActivity.class);
                         intent_events = new Intent(this, EventsActivity.class);
                         intent_story = new Intent(this, LeadStoryActivity.class);
                      //   intent_story.putExtra("story_possion" , "5");

                         intent_request = new Intent(this, EditProfileActivity.class);
                         intent_pm_request= new Intent(this, PMRequestActivity.class);
                     }

                 }//end of else

             }*///end of main else
        //-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-
        // intent.putExtra( "notification_id", 0);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra("2", messageBody);
        //-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_student.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_pm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_events.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_story.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_request.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       intent_pm_request.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*String str_passString=messageBody.toString();
        Log.e("str_passString",str_passString);*/

        intent.putExtra("2", messageBody);

        if(str_titlewhr2go.equalsIgnoreCase("Student")||
                str_titlewhr2go.equalsIgnoreCase("studenttshirtrequested")||
                str_titlewhr2go.equalsIgnoreCase("Proposed"))
        {
            x++;
            int id = (int) System.currentTimeMillis();
            // intent.putExtra("2", str_passString);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_student,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

           NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
           // notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    /*.setContentText(messageBody)*/
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

        }
        else if(str_titlewhr2go.equalsIgnoreCase("Manager"))
        {
            x++;
            int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_pm,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

           NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                /*.setSmallIcon(R.mipmap.ic_launcher_round)*/

           // notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    /*.setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(url2bitmap).setSummaryText(messageBody))*//*Notification with Image*/

                   .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }



       else if (url2bitmap!=null && str_titlewhr2go.equalsIgnoreCase("Events"))
       {
           x++;
           int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_events,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
           //notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(url2bitmap).setSummaryText(messageBody))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }

       else if( (url2bitmap!=null) && (str_titlewhr2go.equalsIgnoreCase("Stories")||str_titlewhr2go.equalsIgnoreCase("Story")))
        {
           // messageBody="LEaders in TV9 Kannada^67";
            messageBody=messageBody.replace("^^","-");
            Log.e("tag","messageBody con=="+messageBody);

            String[] parts = messageBody.split("-");
            messageBody = parts[0]; // 004
            String requestcode = parts[1]; // 034556
            intent_story.putExtra("stroy_position", requestcode);

            x++;
            int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id  /* Request code */, intent_story,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)

           // notificationBuilders
                /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(url2bitmap).setSummaryText(messageBody))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }

        else if( (url2bitmap==null) && (str_titlewhr2go.equalsIgnoreCase("Stories")||str_titlewhr2go.equalsIgnoreCase("Story")) )
        {

            messageBody=messageBody.replace("^^","-");
            Log.e("tag","messageBody con=="+messageBody);

            String[] parts = messageBody.split("-");
            messageBody = parts[0]; // 004
            String requestcode = parts[1]; // 034556
            intent_story.putExtra("stroy_position", requestcode);



            x++;
            int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent_story,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
           // notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }

        else if (str_titlewhr2go.equals("Request"))
        {
            x++;
            int id = (int) System.currentTimeMillis();
            // intent.putExtra("2", str_passString);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_request,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
            //notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    .setContentText(messageBody)
                    /*.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());



        }



        else if(str_titlewhr2go.equalsIgnoreCase("ApprovalLetter"))//ApprovalLetter
        {
            x++;
            int id = (int) System.currentTimeMillis();
            // intent.putExtra("2", str_passString);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_pm_request,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
                    //notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    .setContentText(messageBody)
                    /*.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }


        else if(str_titlewhr2go.equalsIgnoreCase("pmtshirt"))////intent_pm_tshirt
        {
            x++;
            int id = (int) System.currentTimeMillis();
            // intent.putExtra("2", str_passString);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_pm_tshirt,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
                    //notificationBuilders
                    .setSmallIcon(R.drawable.launch_icon)
                    .setContentTitle("EZ Attendance")
                    .setContentText(messageBody)
                    /*.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }




        else
        {
            x++;
            int id = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                /*.setSmallIcon(R.mipmap.ic_launcher_round)*/
            //notificationBuilders
                        .setSmallIcon(R.drawable.launch_icon)
                        .setContentTitle("EZ Attendance")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);
                        //.setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

        }

    }

    }


    public Bitmap getBitmapfromUrl(String imageUrl)
    {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


}// end of class

