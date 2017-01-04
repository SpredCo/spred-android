package com.eip.roucou_c.spred.Home;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.ResultEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class HomeService extends MyService{
    private final IHomeService _api;
    private final IHomeService _login;

    private final IHomeView _view;

    interface IHomeService {
        @Headers("Content-Type: application/json")
        @GET("users/me")
        Call<UserEntity> getProfile();

        @Headers("Content-Type: application/json")
        @GET("spredcasts")
        Call<List<SpredCastEntity>> getSpredCasts(@Query("state") String state);

        @Headers("Content-Type: application/json")
        @GET("users/{user_id}")
        Call<UserEntity> getUser(@Path("user_id") String user_id);

        @Headers("Content-Type: application/json")
        @GET("spredcasts/{url}")
        Call<SpredCastEntity> getSpredCast(@Path("url") String url);

        @Headers("Content-Type: application/json")
        @GET("users/follow")
        Call<List<FollowEntity>> getFollowing();


        @Headers("Content-Type: application/json")
        @GET("spredcasts/{id}/remind")
        Call<ResultEntity> isRemind(@Path("id") String id);

        @Headers("Content-Type: application/json")
        @GET("feed/trend")
        Call<List<SpredCastEntity>> getTrends();

    }
    HomeService(IHomeView view, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;

        this._api = ServiceGeneratorApi.createService(IHomeService.class, "api", tokenEntity, manager);
        this._login = ServiceGeneratorApi.createService(IHomeService.class, "login", manager);

    }

    void getProfile() {
        Call<UserEntity> call = _api.getProfile();
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (!response.isSuccessful()) {

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

    void getSpredCasts(final int state) {
        Call<List<SpredCastEntity>> call = _login.getSpredCasts(String.valueOf(state));
        call.enqueue(new Callback<List<SpredCastEntity>>() {
            @Override
            public void onResponse(Call<List<SpredCastEntity>> call, Response<List<SpredCastEntity>> response) {
                if (response.isSuccessful()) {
                    List<SpredCastEntity> spredCastEntities = response.body();

                    _view.populateSpredCasts(spredCastEntities, state);
                    _view.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<SpredCastEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    void getAbo() {
        Call<List<FollowEntity>> call = _api.getFollowing();
        call.enqueue(new Callback<List<FollowEntity>>() {
            @Override
            public void onResponse(Call<List<FollowEntity>> call, Response<List<FollowEntity>> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    List<FollowEntity> followEntities = response.body();
                    _view.setAbo(followEntities);
                    _view.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<FollowEntity>> call, Throwable t) {
            }
        });
    }

    void getSpredCastsAndShow(String url) {
        Call<SpredCastEntity> call = _login.getSpredCast(url);
        call.enqueue(new Callback<SpredCastEntity>() {
            @Override
            public void onResponse(Call<SpredCastEntity> call, Response<SpredCastEntity> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    SpredCastEntity spredCastEntity = response.body();
                    _view.startSpredCastDetailActivity(spredCastEntity);
                }

            }

            @Override
            public void onFailure(Call<SpredCastEntity> call, Throwable t) {
            }
        });
    }

    void getUserAndShow(String user_id) {
        Call<UserEntity> call = _login.getUser(user_id);
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    UserEntity userEntity = response.body();
                    _view.startProfileActivity(userEntity);
                }

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
            }
        });
    }

    public void isRemind(String id, final LinearLayout reminder) {
        Call<ResultEntity> call = _api.isRemind(id);
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    ResultEntity resultEntity = response.body();
                    if (resultEntity.get_result()) {
                        reminder.setVisibility(View.VISIBLE);
                    }
                    else {
                        reminder.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
            }
        });
    }

    public void getTrends() {
        Call<List<SpredCastEntity>> call = _login.getTrends();
        call.enqueue(new Callback<List<SpredCastEntity>>() {
            @Override
            public void onResponse(Call<List<SpredCastEntity>> call, Response<List<SpredCastEntity>> response) {
                if (response.isSuccessful()) {
                    List<SpredCastEntity> spredCastEntities = response.body();

                    _view.populateTrends(spredCastEntities);
                    _view.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<SpredCastEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

}
