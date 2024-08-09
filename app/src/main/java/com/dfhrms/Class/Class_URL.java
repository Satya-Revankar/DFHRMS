package com.dfhrms.Class;

import com.dfhrms.R;

/**
 * Created by Purnima Hanchinal on 24/06/2024.
 */

public class Class_URL
{
   public static String API_URL ="https://www.dfindia.org:81/api/skillattendance/";
   //public static String API_URL ="http://testingrdp.dfindia.org:9000/api/skillattendance/";

   public static int getImageResource() {
      if (API_URL.equals("http://testingrdp.dfindia.org:9000/api/skillattendance/")) {
         return R.drawable.test_dot; // replace with your actual drawable resource
      } else {
         return R.drawable.live_dot; // replace with your actual drawable resource
      }
   }
}
