package com.example.lostandfoundnew;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

public class UtilFuncs {
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        {

            // Create a new LinkedHashSet
            Set<T> set = new LinkedHashSet<>();

            // Add the elements to set
            set.addAll(list);

            // Clear the list
            list.clear();

            // add the elements of set
            // with no duplicates to the list
            list.addAll(set);

            // return the list
            return list;
        }
    }
    public static void saveUserToFireBase(UserProfileModel userProfile){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("appUsers").child(userProfile.getUserId());
        reference.setValue(userProfile);
    }
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if (currentMinute/10==0&&currentHour/10==0){
            return date + "/" + (month+1) + "/" + year + "   " + "0"+currentHour + ":" + "0"+currentMinute;
        }
        if (currentHour/10==0){
            return date + "/" + (month+1) + "/" + year + "   " + "0"+currentHour + ":" + currentMinute;
        }
        if (currentMinute/10==0){
            return date + "/" + (month+1) + "/" + year + "   " + currentHour + ":" + "0"+currentMinute;
        }
        return date + "/" + (month+1) + "/" + year + "   " + currentHour + ":" + currentMinute;
    }
}
