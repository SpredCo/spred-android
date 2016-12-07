package com.eip.roucou_c.spred.Home;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class HomeService extends MyService{
    private final IHomeService _api;
    private final IHomeService _login;

    private final IHomeView _view;

    public interface IHomeService {
        @Headers("Content-Type: application/json")
        @GET("users/me")
        Call<UserEntity> getProfile();

        @Headers("Content-Type: application/json")
        @GET("spredcasts")
        Call<List<SpredCastEntity>> getSpredCasts();

    }
    public HomeService(IHomeView view, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(IHomeService.class, "api", tokenEntity, manager);
        this._login = ServiceGeneratorApi.createService(IHomeService.class, "login", manager);

    }

    public void getProfile() {
        Call<UserEntity> call = _api.getProfile();
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (!response.isSuccess()) {

                }
                else {
                    UserEntity userEntity = response.body();
                    _view.setProfile(userEntity);
                }

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
            }
        });
    }

    public void getSpredCasts() {
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

    public void getAbo() {
        Call<UserEntity> call = _api.getProfile();
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (!response.isSuccess()) {

                }
                else {
                    UserEntity userEntity = response.body();
                    _view.setAbo(userEntity.get_followingUserEntity());
                    _view.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
            }
        });
    }
}
