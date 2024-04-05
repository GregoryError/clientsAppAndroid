package org.success.isp.workers;

import android.content.Context;
import android.widget.Switch;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.api.ApiService;
import org.success.isp.pojos.EntityMessage;
import org.success.isp.presenters.ChatPresenter;
import org.success.isp.views.Viewable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSender {
    private ChatPresenter chatPresenter;

    private Viewable viewable;

    private String userId;

    private String userPass;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public ChatSender(ChatPresenter chatPresenter) {
        this.chatPresenter = chatPresenter;
    }

    public void sendMessage(String message) {
        EntityMessage entityMessage = new EntityMessage();
        entityMessage.setText(message);
        entityMessage.setExtra("");
        entityMessage.setFromUser(true);
        entityMessage.setTime(System.currentTimeMillis() / 1000);
        ApiFactory.getInstance().getApiService().setMessage(userId, userPass, entityMessage).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    switch (response.code()) {
                        case 403 : {
                            viewable.showError(((Context) viewable).getString(R.string.HTTP_403));
                            break;
                        }

                        case 200: {
                            chatPresenter.loadData();
                            break;
                        }

                        default: {
                            viewable.showError(response.message());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                viewable.showError(t.getMessage());
            }
        });
    }
}
