package com.eip.roucou_c.spred.Stream;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.CastTokenEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by roucou_c on 20/12/2016.
 */

public class StreamService extends MyService {

    private final IStreamService _api;
    IStreamView _iStreamView;


    public interface IStreamService {
        @Headers("Content-Type: application/json")
        @POST("spredcasts/{cast_id}/token")
        Call<CastTokenEntity> getCastToken(@Path("cast_id") String cast_id);
    }

    public StreamService(IStreamView iStreamView, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._api = ServiceGeneratorApi.createService(IStreamService.class, "api", tokenEntity, manager);

        _iStreamView = iStreamView;
    }

    public void getCastToken(String spredCast_id) {
        Call<CastTokenEntity> call = _api.getCastToken(spredCast_id);
        call.enqueue(new Callback<CastTokenEntity>() {
            @Override
            public void onResponse(Call<CastTokenEntity> call, Response<CastTokenEntity> response) {

                if (response.isSuccessful()) {
                    CastTokenEntity castTokenEntity = response.body();
                    _iStreamView.setCastToken(castTokenEntity);
                }
            }

            @Override
            public void onFailure(Call<CastTokenEntity> call, Throwable t) {
//                _view.setProcessLoadingButton(-1);
            }
        });
    }
}
