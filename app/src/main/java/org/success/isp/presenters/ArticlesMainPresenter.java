package org.success.isp.presenters;

import android.content.Context;
import android.view.View;

import org.success.isp.api.ApiFactory;
import org.success.isp.pojos.EntityArticle;
import org.success.isp.views.Viewable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesMainPresenter implements Presentable {

    private Viewable viewable;

    public ArticlesMainPresenter(Viewable viewable) {
        this.viewable = viewable;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getArticles().enqueue(new Callback<List<EntityArticle>>() {
            @Override
            public void onResponse(Call<List<EntityArticle>> call, Response<List<EntityArticle>> response) {
                if (response.isSuccessful())
                    viewable.showData(response.body());
                else
                    viewable.showError("Not successful.");
            }

            @Override
            public void onFailure(Call<List<EntityArticle>> call, Throwable t) {
                viewable.showError(t.getMessage());
            }
        });
    }
}
