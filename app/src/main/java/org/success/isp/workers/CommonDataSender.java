package org.success.isp.workers;

import android.content.Context;
import android.widget.Toast;

import org.success.isp.api.ApiFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonDataSender {

    private Context context;

    private String userId;

    private String userPass;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public CommonDataSender(Context context) {
        this.context = context;
    }

    public void sendToken(String token) {
        ApiFactory.getInstance().getApiService().setToken(userId, userPass, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful())
//                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}











