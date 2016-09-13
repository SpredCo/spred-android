package com.eip.roucou_c.spred.Interceptor;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.SignIn.SignInService;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by cleme_000 on 03/03/2016.
 */

public class TokenAuthenticator implements Authenticator {

    final Manager _manager;
    private final TokenEntity _tokenEntity;

    public TokenAuthenticator(TokenEntity tokenEntity, Manager manager) {
        _manager = manager;
        _tokenEntity = tokenEntity;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (_tokenEntity == null) {
            throw new IOException();
        }
        SignInService signInService = new SignInService(null, _manager);

        //TODO faire le test sur l'expiration du refresh Token

//        String newAccessToken = signInService.refreshTokenSync(_tokenEntity);

//        if (newAccessToken == null) {
//            throw new IOException();
//        }
//
//        _tokenEntity.set_access_token(newAccessToken);

        return response.request().newBuilder().build();
    }
}
