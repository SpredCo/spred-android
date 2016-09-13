package com.eip.roucou_c.spred.SignIn;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.ISignInSignUpView;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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

    private ISignInService _api;

    public interface ISignInService {
        @Headers("Content-Type: application/json")
        @POST("oauth2/token")
        Call<TokenEntity> signIn(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("oauth2/facebook-connect")
        Call<TokenEntity> signInFacebook(@Body Map<String, String> params);

        @Headers("Content-Type: application/json")
        @POST("oauth2/google-connect")
        Call<TokenEntity> signInGoogle(@Body Map<String, String> params);
//
//
//        @Headers("Content-Type: application/json")
//        @POST("oauth2/token")
//        Call<TokenEntity> refreshToken(@Body Map<String, String> params);
//
//        @GET("users/me")
//        Call<ProfileEntity> getProfile();
//
    }
    private final ISignInView _view;
//
    public SignInService(ISignInView view, Manager manager) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(ISignInService.class, "login", manager);
    }
//
//
//    // TODO immplementer le loginWithoutConnexion
//    private void signInWithoutConnexion() {
//        String email = this._view.getEmail();
//        String password = this._view.getPassword();
//
//        ProfileEntity profileEntity = _manager._profileManager.selectByEmail(email);
//
//        if (profileEntity == null || !Objects.equals(profileEntity.get_password(), password)) {
//            this._view.setErrorPassword(R.string.connexionOfflline_errorUsernameOrPassword);
//            this._view.setProcessLoadingButton(0);
//            return;
//        }
//
////        if (profileEntity.get_refresh_token() == null) {
////            // TODO : faire le test sur l'expiration du token refresh
////            // impossible de se connecter en mote hors ligne
////            return;
////        }
//
//        TokenEntity tokenEntity = _manager._tokenManager.selectByProfileId(profileEntity.get_public_id());
//
//        if (tokenEntity == null) {
//            this._view.setErrorPassword(R.id.connexionOfflline_error);
//            this._view.setProcessLoadingButton(0);
//            return;
//        }
////        this._tokenEntity = tokenEntity;
//        _manager._globalManager.addGlobal("profile_public_id_connected", String.valueOf(tokenEntity.get_id()));
//        _userEntity.refresh();
//
//        this._view.startRoomActivity();
//    }
//
//    public void getProfileAfterSignIn(final String profilePassword, final TokenEntity tokenEntity) {
//        _api = ServiceGeneratorApi.createService(ISignInService.class, "api", tokenEntity, _manager);
//
//        Call call = _api.getProfile();
//        call.enqueue(new Callback<ProfileEntity>() {
//            @Override
//            public void onResponse(Call<ProfileEntity> call, Response<ProfileEntity> response) {
//                ProfileEntity profileEntity = response.body();
//
//                _userEntity.addProfile(profileEntity);
//                _userEntity._profileEntity.set_password(profilePassword);
//                _userEntity.update_profileEntity();
//
//                _manager._globalManager.addGlobal("profile_public_id_connected", profileEntity.get_public_id());
//
//                tokenEntity.set_profile_public_id(profileEntity.get_public_id());
//                _userEntity.addTokenEntity(tokenEntity);
//
//                _view.setProcessLoadingButton(100);
//
//                _view.startMainActivity();
//            }
//
//            @Override
//            public void onFailure(Call<ProfileEntity> call, Throwable t) {
//            }
//        });
//    }

    protected void signInOnResponse(Response<TokenEntity> response){
        TokenEntity tokenEntity = response.body();

        if (tokenEntity != null) {
            _manager._tokenManager.add(tokenEntity);
            _view.signinSuccess();
        }
    }

    public void signIn(HashMap<String, String> userParams) {
        userParams.put("grant_type", "password");
        userParams.put("scope", "read");

        Call call = _api.signIn(userParams);
        call.enqueue(new Callback<TokenEntity>() {
            @Override
            public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {
                signInOnResponse(response);
            }

            @Override
            public void onFailure(Call<TokenEntity> call, Throwable t) {
                if (t instanceof UnknownHostException){
//                    signInWithoutConnexion();
                }
            }
        });
    }

    public void signInExternalApi(HashMap<String, String> params, String api) {
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
                    signInOnResponse(response);
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
//    public String refreshTokenSync(TokenEntity tokenEntity) {
//        if (tokenEntity == null || tokenEntity.get_refresh_token() == null) {
//            return null;
//        }
//
//        HashMap<String, String> params = new HashMap<>();
//
//        params.put("grant_type", "refresh_token");
//        params.put("refresh_token", tokenEntity.get_refresh_token());
//
//        this._api = ServiceGeneratorApi.createService(ISignInService.class, "login",_manager);
//
//        Call<TokenEntity> call = _api.refreshToken(params);
//
//        TokenEntity resTokenEntity = null;
//        try {
//            Response<TokenEntity> execute = call.execute();
//            resTokenEntity = execute.body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (resTokenEntity == null) {
//            return null;
//        }
//
//        tokenEntity.set_access_token(resTokenEntity.get_access_token());
//        _manager._tokenManager.modify(tokenEntity);
//        return resTokenEntity.get_access_token();
//    }
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
