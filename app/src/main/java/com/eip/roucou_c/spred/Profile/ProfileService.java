package com.eip.roucou_c.spred.Profile;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.FollowerEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by roucou_c on 27/09/2016.
 */
class ProfileService extends MyService {
    private final IProfileView _view;
    private final IFollowersView _iFollowersView;
    private IProfileService _api;

    interface IProfileService {
        @Headers("Content-Type: application/json")
        @GET("users/me")
        Call<UserEntity> getProfile();

        @Headers("Content-Type: application/json")
        @PATCH("users/me")
        Call<UserEntity> patchProfile(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @GET("users/check/email/{email}")
        Call<String> checkEmail(@Path("email") String email);

        @Headers("Content-Type: application/json")
        @GET("users/follow")
        Call<List<FollowEntity>> getFollowing();

        @Headers("Content-Type: application/json")
        @POST("users/{user_id}/follow")
        Call<Void> follow(@Path("user_id") String user_id);

        @Headers("Content-Type: application/json")
        @POST("users/{user_id}/unfollow")
        Call<Void> unfollow(@Path("user_id") String user_id);

        @Headers("Content-Type: application/json")
        @GET("users/follower")
        Call<List<FollowerEntity>> getFollowers();
    }

    ProfileService(IProfileView view, IFollowersView iFollowersView, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;
        this._iFollowersView = iFollowersView;
        this._api = ServiceGeneratorApi.createService(IProfileService.class, "api", tokenEntity, manager);
    }

    void getProfile(final boolean populate) {
        Call<UserEntity> call = _api.getProfile();
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    UserEntity userEntity = response.body();
                    _view.setUserEntity(userEntity);
                    if (populate) {
                        _view.populateProfile(userEntity);
                    }
                }

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
            }
        });
    }

    void patchProfile(HashMap<String, String> params) {
        Call<UserEntity> call = _api.patchProfile(params);
        call.enqueue(new Callback<UserEntity>() {

            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                if (response.isSuccessful()) {
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


    void checkEmail(String email) {
        IProfileService api = ServiceGeneratorApi.createService(IProfileService.class, "login", _manager);

        Call<String> call = api.checkEmail(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.isSuccessful()) {
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

    void follow(String user_id, final boolean isFollow) {
        Call<Void> call;

        if (isFollow) {
            call = _api.follow(user_id);
        }
        else {
            call = _api.unfollow(user_id);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    _view.follow(isFollow);
                }
                else {
                    _view.follow(!isFollow);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    void getFollowing() {
        Call<List<FollowEntity>> call = _api.getFollowing();
        call.enqueue(new Callback<List<FollowEntity>>() {
            @Override
            public void onResponse(Call<List<FollowEntity>> call, Response<List<FollowEntity>> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    List<FollowEntity> followEntities = response.body();
                    _view.setFollowing(followEntities);
                }
            }

            @Override
            public void onFailure(Call<List<FollowEntity>> call, Throwable t) {
            }
        });
    }

    void getFollowers() {
        Call<List<FollowerEntity>> call = _api.getFollowers();
        call.enqueue(new Callback<List<FollowerEntity>>() {
            @Override
            public void onResponse(Call<List<FollowerEntity>> call, Response<List<FollowerEntity>> response) {

                if (!response.isSuccessful()) {

                }
                else {
                    List<FollowerEntity> followerEntities = response.body();
                    _iFollowersView.setFollowers(followerEntities);
                }
            }

            @Override
            public void onFailure(Call<List<FollowerEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

}
