package com.alnajjarchattingfirebase.fragments;

import com.alnajjarchattingfirebase.notification.MyResponse;
import com.alnajjarchattingfirebase.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA8hll_zk:APA91bFIxRiFpF3TKOQCgvvUUnh-MqY_l-oeMw0bwVhwfMcsQhpke_-W0855nH2lm9aIWHY4Vntnu4VBJ3t2snowNNgGCv10RVJbxWE7Hv0iNnSmdwMsfmbQzRZIBo4eixo_g95sJmAw"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender sender);
}
