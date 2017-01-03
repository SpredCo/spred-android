package com.eip.roucou_c.spred.SignIn;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.ISignInSignUpView;
import com.eip.roucou_c.spred.Interceptor.AuthInterceptor;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;
import com.eip.roucou_c.spred.SignUp.SignUpPresenter;
import com.eip.roucou_c.spred.SignUp.SignUpService;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by cleme_000 on 23/02/2016.
 */
public class SignInService extends MyService {


    interface ISignInService {
        @Headers("Content-Type: application/json")
        @POST("oauth2/token")
        Call<TokenEntity> signIn(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("oauth2/facebook-connect")
        Call<TokenEntity> signInFacebook(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("oauth2/google-connect")
        Call<TokenEntity> signInGoogle(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("users/facebook")
        Call<TokenEntity> signUpFacebook(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("users/google")
        Call<TokenEntity> signUpGoogle(@Body Map<String, String> params);
    }

    private ISignInService _api;

    private final ISignInView _view;
    private final ISignInSignUpView _iSignInSignUpView;
//    private LoginGuestService _loginGuestService;

    public SignInService(ISignInView view, ISignInSignUpView iSignInSignUpView, Manager manager) {
        super(manager);
        this._view = view;
        _iSignInSignUpView = iSignInSignUpView;
        this._api = ServiceGeneratorApi.createService(ISignInService.class, "login", manager);

//        if (_view != null) {
//            _loginGuestService = new LoginGuestService(null, _iSignInSignUpView, _manager);
//        }
    }

    protected void signInOnResponse(Response<TokenEntity> response){
        TokenEntity tokenEntity = response.body();

        if (tokenEntity != null) {
            AuthInterceptor.updateTokenExpire(tokenEntity);
            _manager._tokenManager.add(tokenEntity);
            _iSignInSignUpView.signInSuccess();
        }
    }

    public void signIn(HashMap<String, String> userParams) {
        userParams.put("grant_type", "password");

        Call call = _api.signIn(userParams);
        call.enqueue(new Callback<TokenEntity>() {
            @Override
            public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {
                if (response.isSuccessful()) {
                    signInOnResponse(response);
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<TokenEntity> call, Throwable t) {
                if (t instanceof UnknownHostException){
//                    signInWithoutConnexion();
                }
            }
        });
    }

    public void signInExternalApi(final HashMap<String, String> params, final String api) {
        Call call = null;
        switch (api) {
            case "facebook" :
                call = _api.signInFacebook(params);
                break;
            case "google" :
                call = _api.signInGoogle(params);
                break;
        }

        if (call != null) {
            call.enqueue(new Callback<TokenEntity>() {
                @Override
                public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {
                    if (response.isSuccessful()) {
                        signInOnResponse(response);
                    }
                    else {
                        ApiError apiError = new ApiError(response.errorBody(), response.code(), "signIn");
                        if (apiError.get_httpCode() == 404) {
                            _iSignInSignUpView.changeStep("step2");
                        }
                        else if (apiError.get_httpCode() == 403) {
                            _view.setError(apiError.get_target_message());
                            if (Objects.equals(api, "facebook")) {
                                LoginManager.getInstance().logOut();
                            }
                        }
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
            call.enqueue(new Callback<TokenEntity>() {
                @Override
                public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {

                    if (response.isSuccessful()) {
                        signInExternalApi(params, api);
                    }
                    else {
                        ApiError apiError = new ApiError(response.errorBody(), response.code(), "signUp");
                        if (Objects.equals(apiError.get_target(), "pseudo")) {
                            _iSignInSignUpView.setErrorPseudo(apiError.get_target_message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<TokenEntity> call, Throwable t) {
                }

            });
        }
    }
//
//    public void refreshToken() {
//
//        if (_userEntity._tokenEntity == null || _userEntity._tokenEntity.get_refresh_token() == null) {
//            return;
//        }
//
//        HashMap<String, String> params = new HashMap<>();
//
//        params.put("grant_type", "refresh_token");
//        params.put("refresh_token", _userEntity._tokenEntity.get_refresh_token());
//
//        this._api = ServiceGeneratorApi.createService(ISignInService.class, "login", _manager);
//
//        Call call = _api.refreshToken(params);
//        call.enqueue(new Callback<TokenEntity>() {
//            @Override
//            public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {
//                TokenEntity tokenEntity = response.body();
//                if (tokenEntity != null) {
//                    _userEntity._tokenEntity.set_access_token(tokenEntity.get_access_token());
//                    _userEntity.update_tokenEntity();
//
//                    ProfileService profileModel = new ProfileService(null, _manager, _userEntity);
//                    profileModel.getProfile(null);
//
//                    _view.setProcessLoadingButton(100);
//                    _view.startMainActivity();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TokenEntity> call, Throwable t) {
//                _userEntity.logout();
//                Log.d("erreur", "erreur lors du refresh token");
//            }
//        });
//    }
//
    public String refreshTokenSync(TokenEntity tokenEntity) {
        if (tokenEntity == null || tokenEntity.get_refresh_token() == null) {
            return null;
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("grant_type", "refresh_token");
        params.put("refresh_token", tokenEntity.get_refresh_token());

        this._api = ServiceGeneratorApi.createService(ISignInService.class, "login",_manager);

        Call<TokenEntity> call = _api.signIn(params);

        TokenEntity resTokenEntity = null;
        try {
            Response<TokenEntity> execute = call.execute();
            resTokenEntity = execute.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (resTokenEntity == null) {
            return null;
        }

        tokenEntity.set_access_token(resTokenEntity.get_access_token());
        _manager._tokenManager.modify(tokenEntity);
        return resTokenEntity.get_access_token();
    }
//
//
//    public void signInWithAccessTokenValid() {
//        this._view.startMainActivity();
//    }
//
//    public void signInAfterSignUp(HashMap<String, String> userParams) {
//        String username = userParams.get("email");
//        userParams.remove("email");
//        userParams.remove("first_name");
//        userParams.remove("last_name");
//        userParams.put("username", username);
//        this.signIn(userParams);
//    }
}
