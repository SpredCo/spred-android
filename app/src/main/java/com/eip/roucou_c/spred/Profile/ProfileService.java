package com.eip.roucou_c.spred.Profile;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

/**
 * Created by roucou_c on 27/09/2016.
 */
public class ProfileService extends MyService {
    private final IProfileView _view;
    private IProfileService _api;

    public interface IProfileService {
        @Headers("Content-Type: application/json")
        @GET("users/me")
        Call<UserEntity> getProfile();

        @Headers("Content-Type: application/json")
        @PATCH("users/me")
        Call<UserEntity> patchProfile(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @GET("users/check/email/{email}")
        Call<String> checkEmail(@Path("email") String email);
    }

    public ProfileService(IProfileView view, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(IProfileService.class, "api", tokenEntity, manager);
    }

    protected void getProfile() {
        Call<UserEntity> call = _api.getProfile();
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (!response.isSuccess()) {

                }
                else {
                    UserEntity userEntity = response.body();
                    _view.populateProfile(userEntity);
                }

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
            }
        });
    }

    protected void patchProfile(HashMap<String, String> params) {
        Call<UserEntity> call = _api.patchProfile(params);
        call.enqueue(new Callback<UserEntity>() {

            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                if (response.isSuccess()) {
                   UserEntity userEntity = response.body();
                    _view.changeStep("profile");
                }
                else {
                    ApiError apiError = new ApiError(response.errorBody(), response.code(), "pathProfile");
                    _view.setError(apiError.get_target_message());
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {

            }
        });

    }


    public void checkEmail(String email) {
        IProfileService api = ServiceGeneratorApi.createService(IProfileService.class, "login", _manager);

        Call<String> call = api.checkEmail(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.isSuccess()) {
                    ApiError apiError = new ApiError(response.errorBody(), response.code(), "signUp");

                    _view.setErrorEmail(apiError.get_target_message());
                }
                else {
                    _view.setErrorEmail(0);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
