package com.eip.roucou_c.spred.SpredCast;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.MessageEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastService extends MyService{
    private final ISpredCastView _view;
    private final ISpredCastService _api;
    private final ISpredCastService _login;

    public interface ISpredCastService {
        @Headers("Content-Type: application/json")
        @GET("users/search/pseudo/{partial_pseudo}")
        Call<List<UserEntity>> searchPartialPseudo(@Path("partial_pseudo") String partial_pseudo);

        @Headers("Content-Type: application/json")
        @POST("spredcast")
        Call<SpredCastEntity> postSpredCast(@Body HashMap<String, Object> params);

        @Headers("Content-Type: application/json")
        @GET("spredcasts")
        Call<List<SpredCastEntity>> getSpredCasts();
    }

    public SpredCastService(ISpredCastView view, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(ISpredCastService.class, "api", tokenEntity, manager);
        this._login = ServiceGeneratorApi.createService(ISpredCastService.class, "login", manager);
    }

    public void getSpredCast() {
        Call<List<SpredCastEntity>> call = _login.getSpredCasts();
        call.enqueue(new Callback<List<SpredCastEntity>>() {
            @Override
            public void onResponse(Call<List<SpredCastEntity>> call, Response<List<SpredCastEntity>> response) {
                if (response.isSuccess()) {
                    List<SpredCastEntity> spredCastEntities = response.body();

                    _view.populateSpredCasts(spredCastEntities);
                    _view.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<SpredCastEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void searchPartialPseudo(String pseudo_search) {
        Call<List<UserEntity>> call = _api.searchPartialPseudo(pseudo_search);
        call.enqueue(new Callback<List<UserEntity>>() {
            @Override
            public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {
                if (response.isSuccess()) {
                    List<UserEntity> userEntities = response.body();

                    _view.populateSearchPseudo(userEntities);
                }

            }

            @Override
            public void onFailure(Call<List<UserEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void postSpredCast(HashMap<String, Object> params) {
        Call<SpredCastEntity> call = _api.postSpredCast(params);
        call.enqueue(new Callback<SpredCastEntity>() {
            @Override
            public void onResponse(Call<SpredCastEntity> call, Response<SpredCastEntity> response) {
                if (response.isSuccess()) {
                    SpredCastEntity spredCastEntity = response.body();

                    _view.changeStep("spredcast");
                }

            }

            @Override
            public void onFailure(Call<SpredCastEntity> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }
}
