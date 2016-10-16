package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

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

    private final IHomeView _view;


    public interface IHomeService {
        @Headers("Content-Type: application/json")
        @GET("users/me")
        Call<UserEntity> getProfile();

    }
    public HomeService(IHomeView view, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(IHomeService.class, "api", tokenEntity, manager);
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

}
