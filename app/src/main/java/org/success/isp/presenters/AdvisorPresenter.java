package org.success.isp.presenters;

import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntitiesPack;
import org.success.isp.views.Viewable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvisorPresenter implements Presentable{

    Viewable viewable;

    private ArrayList<String> tags;

    public AdvisorPresenter(Viewable viewable) {
        this.viewable = viewable;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getInfoByTags(tags).enqueue(new Callback<EntitiesPack>() {
            @Override
            public void onResponse(Call<EntitiesPack> call, Response<EntitiesPack> response) {
                if (response.isSuccessful()) {
                    viewable.showData(response.body());
                }
            }

            @Override
            public void onFailure(Call<EntitiesPack> call, Throwable t) {
                viewable.showError(t.getMessage());
            }
        });
    }
}
