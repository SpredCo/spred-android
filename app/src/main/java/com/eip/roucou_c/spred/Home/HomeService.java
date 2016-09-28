package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Errors.ApiError;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class HomeService extends MyService{
    private final IHomeService _api;

    private final IHomeView _view;

    public interface IHomeService {


    }
    public HomeService(IHomeView view, Manager manager) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(IHomeService.class, "api", manager);
    }
}
