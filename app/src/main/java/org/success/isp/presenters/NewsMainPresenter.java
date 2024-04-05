package org.success.isp.presenters;

import android.content.Context;
import android.widget.Toast;

import org.success.isp.R;
import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityNews;
import org.success.isp.views.Viewable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsMainPresenter implements Presentable {
    private Viewable viewable;

    public NewsMainPresenter(Viewable viewable) {
        this.viewable = viewable;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getNews().enqueue(new Callback<List<EntityNews>>() {
            @Override
            public void onResponse(Call<List<EntityNews>> call, Response<List<EntityNews>> response) {
                if (response.isSuccessful())
                    viewable.showData(response.body());
                else
                    viewable.showError(((Context) viewable).getString(R.string.errorLabel) + response.code());
            }

            @Override
            public void onFailure(Call<List<EntityNews>> call, Throwable t) {
                Toast.makeText((Context) viewable, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
