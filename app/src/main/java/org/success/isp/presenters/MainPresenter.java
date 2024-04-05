package org.success.isp.presenters;

import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.AppInitData;
import org.success.isp.views.Viewable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements Presentable {
    private Viewable screenView;
    private String userName;
    private String userPass;

    private boolean pause;

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public MainPresenter(Viewable screenView) {
        this.screenView = screenView;
        pause = false;
    }

    @Override
    public void loadData() {
        if (!pause) {
            if (userName != null && userPass != null) {
                ApiFactory.getInstance().getApiService().getInitial(userName, userPass).enqueue(new Callback<AppInitData>() {
                    @Override
                    public void onResponse(Call<AppInitData> call, Response<AppInitData> response) {
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
                        } else {
                            screenView.showError("Вход невозможен.");
                        }
                    }

                    @Override
                    public void onFailure(Call<AppInitData> call, Throwable t) {
                        screenView.showError(t.getMessage());
                    }
                });
            } else {
                System.out.println("Presenter: Check userName and userPassword.");
            }
        }
    }
}
