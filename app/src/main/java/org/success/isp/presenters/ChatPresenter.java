package org.success.isp.presenters;

import android.content.Context;

import androidx.core.content.ContextCompat;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityMessage;
import org.success.isp.views.Viewable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatPresenter implements Presentable {

    private Viewable viewable;

    private String userId;

    private String userPass;

    private Boolean needLoad;

    private int currentDataSize;

    public void pause() {
        needLoad = false;
    }

    public void start() {
        needLoad = true;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public ChatPresenter(Viewable viewable) {
        this.viewable = viewable;
        needLoad = true;
        currentDataSize = 0;
    }

    @Override
    public void loadData() {
        if (needLoad) {
            ApiFactory.getInstance().getApiService().getMessages(userId, userPass).enqueue(new Callback<ArrayList<EntityMessage>>() {
                @Override
                public void onResponse(Call<ArrayList<EntityMessage>> call, Response<ArrayList<EntityMessage>> response) {
                    if (response.isSuccessful()) {
                        switch (response.code()) {
                            case 403: {
                                viewable.showError(((Context) viewable).getString(R.string.HTTP_403));
                                break;
                            }

                            case 200: {
                                if (response.body().size() > currentDataSize) {
                                    viewable.showData(response.body());
                                    currentDataSize = response.body().size();
                                    System.out.println("DATA SIZE: " + currentDataSize);
                                }
                                break;
                            }

                            default: {
                                viewable.showError(response.message());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<EntityMessage>> call, Throwable t) {
                    viewable.showError(t.getMessage());
                }
            });
        }
    }
}
