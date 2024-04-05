package org.success.isp.presenters;

import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityPromo;
import org.success.isp.views.Viewable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoMainPresenter implements Presentable {

    private Viewable viewable;

    public PromoMainPresenter(Viewable viewable) {
        this.viewable = viewable;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getPromos().enqueue(new Callback<List<EntityPromo>>() {
            @Override
            public void onResponse(Call<List<EntityPromo>> call, Response<List<EntityPromo>> response) {
                if (response.isSuccessful())
                    viewable.showData(response.body());
            }

            @Override
            public void onFailure(Call<List<EntityPromo>> call, Throwable t) {
                viewable.showError(t.getMessage());
            }
        });
    }
}
