package com.eip.roucou_c.spred.SignUp;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.ISignInSignUpView;
import com.eip.roucou_c.spred.Interceptor.AuthInterceptor;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;
import com.eip.roucou_c.spred.SignIn.SignInService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
        @GET("users/check/email/{email}")
        Call<String> checkEmail(@Path("email") String email);

        @Headers("Content-Type: application/json")
        @POST("users/facebook")
        Call<UserEntity> signUpFacebook(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("users/google")
        Call<UserEntity> signUpGoogle(@Body Map<String, String> params);
    }

    private SignInService _signInService;
    private final ISignUpView _view;
    private final ISignInSignUpView _iSignInSignUpView;

    public SignUpService(ISignUpView view, ISignInSignUpView iSignInSignUpView, Manager manager) {
        super(manager);
        this._view = view;
        _iSignInSignUpView = iSignInSignUpView;
        this._api = ServiceGeneratorApi.createService(ISignUpService.class, "login", manager);
        if (_view != null) {
            _signInService = new SignInService(null, iSignInSignUpView, _manager);
        }
    }

    public void signUp(final HashMap<String, String> userParams) {

        Call call = _api.signUp(userParams);
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (response.isSuccessful()) {
                    UserEntity userEntity = response.body();

                    if (userEntity != null) {
                        userParams.put("username", userParams.get("email"));
                        userParams.remove("email");
                        userParams.remove("pseudo");
                        userParams.remove("first_name");
                        userParams.remove("last_name");
                        _signInService.signIn(userParams);
                    }
                }
                else {
                    ApiError apiError = new ApiError(response.errorBody(), response.code(), "signUp");
                    if (Objects.equals(apiError.get_target(), "pseudo")) {
                        _view.setErrorPseudo(apiError.get_target_message());
                    }
                }

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
//                _view.setProcessLoadingButton(-1);
            }
        });
    }

    public void signUpExternalApi(final HashMap<String, String> params, final String api) {
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
            call.enqueue(new Callback<UserEntity>() {
                @Override
                public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                    if (response.isSuccessful()) {
                        UserEntity userEntity = response.body();

                       if (userEntity != null) {
                           _signInService.signInExternalApi(params, api);
                        }
                    }
                    else {
                        ApiError apiError = new ApiError(response.errorBody(), response.code(), "signUp");
                        if (Objects.equals(apiError.get_target(), "pseudo")) {
                            _view.setErrorPseudo(apiError.get_target_message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserEntity> call, Throwable t) {
                }

            });
        }
    }

    public void checkEmail(String email) {
        Call call = _api.checkEmail(email);
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
//                _view.setProcessLoadingButton(-1);
            }
        });
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