package com.eip.roucou_c.spred.Stream;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;

/**
 * Created by roucou_c on 20/12/2016.
 */

public class StreamPresenter {

    private final StreamService _streamService;

    public StreamPresenter(IStreamView iStreamView, Manager manager, TokenEntity tokenEntity) {
        _streamService = new StreamService(iStreamView, manager, tokenEntity);
    }

    public void getCastToken(String spredCast_id) {
        _streamService.getCastToken(spredCast_id);
    }
}
