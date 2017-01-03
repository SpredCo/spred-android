package com.eip.roucou_c.spred.SpredCast;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.Reminder;
import com.eip.roucou_c.spred.Entities.ReminderEntity;
import com.eip.roucou_c.spred.Entities.ResultEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastService extends MyService{
    private final ISpredCastService _api;
    private final ISpredCastService _login;
    private final ISpredCastView _iSpredCastView;
    private final ISpredCastNewView _iSpredCastNewView;
    private final ISpredCastByTagView _iSpredCastByTagView;
    private final ISpredCastDetailsView _iSpredCastDetailsView;

    public interface ISpredCastService {
        @Headers("Content-Type: application/json")
        @GET("users/me")
        Call<UserEntity> getProfile();

        @Headers("Content-Type: application/json")
        @GET("users/search/pseudo/{partial_pseudo}")
        Call<List<UserEntity>> searchPartialPseudo(@Path("partial_pseudo") String partial_pseudo);

        @Headers("Content-Type: application/json")
        @POST("spredcasts")
        Call<SpredCastEntity> postSpredCast(@Body HashMap<String, Object> params);

        @Headers("Content-Type: application/json")
        @GET("spredcasts")
        Call<List<SpredCastEntity>> getSpredCasts();

        @Headers("Content-Type: application/json")
        @GET("tags")
        Call<List<TagEntity>> getTags();

        @Headers("Content-Type: application/json")
        @GET("spredcasts/tag/{tag}")
        Call<List<SpredCastEntity>> getSpredCastByTags(@Path("tag") String tag);

        @Headers("Content-Type: application/json")
        @GET("tags/{name}")
        Call<TagEntity> getTag(@Path("name") String name);

        @Headers("Content-Type: application/json")
        @GET("tags/{tag_id}/subscription")
        Call<ResultEntity> getIsSubscriptionTag(@Path("tag_id") String tag_id);

        @Headers("Content-Type: application/json")
        @POST("tags/{tag_id}/subscription")
        Call<Void> postSubscription(@Path("tag_id") String tag_id);

        @Headers("Content-Type: application/json")
        @DELETE("tags/{tag_id}/subscription")
        Call<Void> deleteSubscription(@Path("tag_id") String tag_id);

        @Headers("Content-Type: application/json")
        @GET("spredcasts/{cast_id}/remind")
        Call<ResultEntity> getIsRemind(@Path("cast_id") String cast_id);


        @Headers("Content-Type: application/json")
        @POST("spredcasts/{cast_id}/remind")
        Call<Void> postReminder(@Path("cast_id") String tag_id);

        @Headers("Content-Type: application/json")
        @DELETE("spredcasts/{cast_id}/remind")
        Call<Void> deleteReminder(@Path("cast_id") String tag_id);


        @Headers("Content-Type: application/json")
        @GET("spredcasts/{cast_id}/reminders")
        Call<List<ReminderEntity>> getReminders(@Path("cast_id") String tag_id);

    }

    public SpredCastService(ISpredCastView iSpredCastView, ISpredCastNewView iSpredCastNewView, ISpredCastDetailsView iSpredCastDetailsView, ISpredCastByTagView iSpredCastByTagView, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._iSpredCastView = iSpredCastView;
        this._iSpredCastNewView = iSpredCastNewView;
        this._iSpredCastDetailsView = iSpredCastDetailsView;
        this._iSpredCastByTagView = iSpredCastByTagView;
        this._api = ServiceGeneratorApi.createService(ISpredCastService.class, "api", tokenEntity, manager);
        this._login = ServiceGeneratorApi.createService(ISpredCastService.class, "login", manager);
    }

    public void getUser() {
        Call<UserEntity> call = _api.getProfile();
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                if (response.isSuccessful()) {
                    UserEntity userEntity = response.body();
                    _iSpredCastDetailsView.setUserEntity(userEntity);
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
            }
        });
    }

    public void getSpredCast() {
        Call<List<SpredCastEntity>> call = _api.getSpredCasts();
        call.enqueue(new Callback<List<SpredCastEntity>>() {
            @Override
            public void onResponse(Call<List<SpredCastEntity>> call, Response<List<SpredCastEntity>> response) {
                if (response.isSuccessful()) {
                    List<SpredCastEntity> spredCastEntities = response.body();

                    _iSpredCastView.populateSpredCasts(spredCastEntities);
                    _iSpredCastView.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<SpredCastEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void searchPartialPseudo(String pseudo_search) {
        Call<List<UserEntity>> call = _api.searchPartialPseudo(pseudo_search);
        call.enqueue(new Callback<List<UserEntity>>() {
            @Override
            public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {
                if (response.isSuccessful()) {
                    List<UserEntity> userEntities = response.body();

                    _iSpredCastNewView.populateSearchPseudo(userEntities);
                }

            }

            @Override
            public void onFailure(Call<List<UserEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void postSpredCast(HashMap<String, Object> params) {
        Call<SpredCastEntity> call = _api.postSpredCast(params);
        call.enqueue(new Callback<SpredCastEntity>() {
            @Override
            public void onResponse(Call<SpredCastEntity> call, Response<SpredCastEntity> response) {
                if (response.isSuccessful()) {
                    _iSpredCastNewView.finishPostSpredCast();
                }

            }

            @Override
            public void onFailure(Call<SpredCastEntity> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void getTags() {
        Call<List<TagEntity>> call = _login.getTags();
        call.enqueue(new Callback<List<TagEntity>>() {
            @Override
            public void onResponse(Call<List<TagEntity>> call, Response<List<TagEntity>> response) {
                if (response.isSuccessful()) {
                    List<TagEntity> tagEntities = response.body();

                    _iSpredCastNewView.populateTags(tagEntities);
                }

            }

            @Override
            public void onFailure(Call<List<TagEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void getSpredCastsByTag(String tag) {
        Call<List<SpredCastEntity>> call = _login.getSpredCastByTags(tag);
        call.enqueue(new Callback<List<SpredCastEntity>>() {
            @Override
            public void onResponse(Call<List<SpredCastEntity>> call, Response<List<SpredCastEntity>> response) {
                if (response.isSuccessful()) {
                    List<SpredCastEntity> spredCastEntities = response.body();

                    _iSpredCastView.populateSpredCasts(spredCastEntities);
                    _iSpredCastView.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<SpredCastEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    void getTag(String tag_name) {
        Call<TagEntity> call = _login.getTag(tag_name);
        call.enqueue(new Callback<TagEntity>() {
            @Override
            public void onResponse(Call<TagEntity> call, Response<TagEntity> response) {
                if (response.isSuccessful()) {
                    TagEntity tagEntity = response.body();

                    _iSpredCastByTagView.setTag(tagEntity);
                }

            }

            @Override
            public void onFailure(Call<TagEntity> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void getIsSubscriptionTag(String id) {
        Call<ResultEntity> call = _api.getIsSubscriptionTag(id);
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                if (response.isSuccessful()) {
                    ResultEntity resultEntity = response.body();

                    _iSpredCastByTagView.setIsSub(resultEntity.get_result());
                }

            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void subscriptionTag(final boolean isSub, String tag_id) {
        Call<Void> call;
        if (isSub) {
            call = _api.postSubscription(tag_id);
        }
        else {
            call = _api.deleteSubscription(tag_id);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    _iSpredCastByTagView.setIsSub(isSub);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void getIsRemind(final String cast_id) {
        Call<ResultEntity> call = _api.getIsRemind(cast_id);
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                if (response.isSuccessful()) {
                    ResultEntity resultEntity = response.body();
                    _iSpredCastDetailsView.setIsRemind(resultEntity.get_result());

//                    for (Reminder reminder : reminders) {
//                        if (Objects.equals(reminder.get_id(), cast_id)) {
//                        }
//                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void setReminder(final boolean isRemind, String tag_id) {
        Call<Void> call;
        if (isRemind) {
            call = _api.postReminder(tag_id);
        }
        else {
            call = _api.deleteReminder(tag_id);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    _iSpredCastDetailsView.setIsRemind(isRemind);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void getReminders(String cast_id) {
        Call<List<ReminderEntity>> call = _api.getReminders(cast_id);
        call.enqueue(new Callback<List<ReminderEntity>>() {
            @Override
            public void onResponse(Call<List<ReminderEntity>> call, Response<List<ReminderEntity>> response) {
                if (response.isSuccessful()) {
                    List<ReminderEntity> reminderEntities = response.body();
                    _iSpredCastDetailsView.setReminders(reminderEntities);
                }
            }

            @Override
            public void onFailure(Call<List<ReminderEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

}
