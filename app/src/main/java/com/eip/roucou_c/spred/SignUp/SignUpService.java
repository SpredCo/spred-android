package com.eip.roucou_c.spred.SignUp;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;
import com.eip.roucou_c.spred.SignIn.SignInService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by roucou-c on 07/12/15.
 */
public class SignUpService extends MyService {

    private final ISignUpService _api;

    public interface ISignUpService {
        @Headers("Content-Type: application/json")
        @POST("users")
        Call<UserEntity> signUp(@Body Map<String, String> params);
        @Headers("Content-Type: application/json")
        @POST("users/facebook")
        Call<TokenEntity> signUpFacebook(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("users/google")
        Call<TokenEntity> signUpGoogle(@Body Map<String, String> params);
    }

//    private final SignInService _signInService;
    private final ISignUpView _view;

    public SignUpService(ISignUpView view, Manager manager) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(ISignUpService.class, "login", manager);
    }

    public void signUp(HashMap<String, String> userParams) {

        Call call = _api.signUp(userParams);
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                UserEntity userEntity = response.body();

                if (userEntity != null) {
                    _view.signUpSuccess();
//                    _signInService.signInAfterSignUp(userParams);
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
//                _view.setProcessLoadingButton(-1);
            }
        });
    }

    public void signUpExternalApi(HashMap<String, String> params, String api) {
        Call call = null;
        switch (api) {
            case "facebook" :
                call = _api.signUpFacebook(params);
                break;
            case "google" :
                call = _api.signUpGoogle(params);
                break;
        }

        if (call != null) {
            call.enqueue(new Callback<TokenEntity>() {
                @Override
                public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {
                    TokenEntity tokenEntity = response.body();

                    if (tokenEntity != null) {
                        _manager._tokenManager.add(tokenEntity);
                        _view.signUpSuccess();
                    }
                }

                @Override
                public void onFailure(Call<TokenEntity> call, Throwable t) {
                    //                if (t instanceof UnknownHostException){
                    //                    signInWithoutConnexion();
                    //                }
                }
            });
        }
    }

//    public void createUser(final HashMap<String, String> userParams) {
//        Call call = _api.signUp(userParams);
//        call.enqueue(new Callback<ProfileEntity>() {
//            @Override
//            public void onResponse(Call<ProfileEntity> call, Response<ProfileEntity> response) {
//                ProfileEntity profileEntity = response.body();
//
//                if (profileEntity != null) {
//                    _signInService.signInAfterSignUp(userParams);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProfileEntity> call, Throwable t) {
//                _view.setProcessLoadingButton(-1);
//            }
//        });
//    }
}