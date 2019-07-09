package com.example.lostandfoundnew;



import com.example.lostandfoundnew.Notifications.MyResponse;
import com.example.lostandfoundnew.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA-3-Sz_w:APA91bG4HKYY6w3o46QOOXtDfboCwL_LfNnPZZqdcEiQ6Ql-VlP91chYPDWbmNrtTnb4WVhkgaCUKuluQGDbh0e-iOO2C5-WAcOgGxZzcOVntgU7RUMr7VQEqzW4C_Krfl0PBKjZZq_c"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
