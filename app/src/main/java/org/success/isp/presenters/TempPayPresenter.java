package org.success.isp.presenters;

import org.success.isp.api.ApiFactory;
import org.success.isp.api.ApiService;
import org.success.isp.pojos.EntityMisc;
import org.success.isp.views.Viewable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TempPayPresenter implements Presentable {

    Viewable viewable;

    public TempPayPresenter(Viewable viewable) {
        this.viewable = viewable;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getTempPayText().enqueue(new Callback<EntityMisc>() {
            @Override
            public void onResponse(Call<EntityMisc> call, Response<EntityMisc> response) {
                if (response.isSuccessful())
                    viewable.showData(response.body());
            }

            @Override
            public void onFailure(Call<EntityMisc> call, Throwable t) {
                viewable.showError(t.getMessage());
            }
        });
    }
}
