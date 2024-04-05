package org.success.isp.presenters;

import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.DB_dry_pay;
import org.success.isp.views.Viewable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsPresenter implements Presentable {

    private Viewable screenView;
    private String userId;
    private String userPass;

    public PaymentsPresenter(Viewable screenView) {
        this.screenView = screenView;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getPayments(userId, userPass).enqueue(new Callback<List<DB_dry_pay>>() {
            @Override
            public void onResponse(Call<List<DB_dry_pay>> call, Response<List<DB_dry_pay>> response) {
                if (response.isSuccessful()) {
                    switch (response.code()) {
                        case 403: {
                            screenView.showError("Неверная авторизация.");
                            break;
                        }

                        case 200: {
                            screenView.showData(response.body());
                            break;
                        }

                        default: {
                            screenView.showError(response.message());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DB_dry_pay>> call, Throwable t) {
                screenView.showError(t.getMessage());
            }
        });
    }
}
