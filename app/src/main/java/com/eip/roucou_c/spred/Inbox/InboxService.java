package com.eip.roucou_c.spred.Inbox;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.MessageEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.MyService;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.ArrayList;
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
import retrofit2.http.Query;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class InboxService extends MyService{
    private final IHomeService _api;

    private final IInboxView _view;


    public interface IHomeService {
        @Headers("Content-Type: application/json")
        @GET("inbox/conversation")
        Call<List<ConversationEntity>> getInbox();

        @Headers("Content-Type: application/json")
        @GET("inbox/conversation/{conversation_id}")
        Call<ConversationEntity> getConversation(@Path("conversation_id") String conversation_id);

        @Headers("Content-Type: application/json")
        @POST("inbox/conversation")
        Call<ConversationEntity> addConversation(@Body HashMap<String, Object> params);

        @Headers("Content-Type: application/json")
        @GET("users/search/pseudo/{partial_pseudo}")
        Call<List<UserEntity>> searchPartialPseudo(@Path("partial_pseudo") String partial_pseudo);

        @Headers("Content-Type: application/json")
        @POST("inbox/conversation/{conversation_id}/message")
        Call<MessageEntity> replyConversation(@Body HashMap<String, Object> params, @Path("conversation_id") String conversation_id);

        @Headers("Content-Type: application/json")
        @PATCH("/v1/inbox/conversation/{conversation_id}/read")
        Call<Void> updateReadState(@Path("conversation_id") String conversation_id, @Body HashMap<String, Object> params);
    }
    public InboxService(IInboxView view, Manager manager, TokenEntity tokenEntity) {
        super(manager);
        this._view = view;
        this._api = ServiceGeneratorApi.createService(IHomeService.class, "api", tokenEntity, manager);
    }

    public void getInbox() {
        Call<List<ConversationEntity>> call = _api.getInbox();
        call.enqueue(new Callback<List<ConversationEntity>>() {
            @Override
            public void onResponse(Call<List<ConversationEntity>> call, Response<List<ConversationEntity>> response) {
                if (response.isSuccessful()) {
                    List<ConversationEntity> conversationEntities = response.body();

                    _view.populateInbox(conversationEntities);
                    _view.cancelRefresh();
                }

            }

            @Override
            public void onFailure(Call<List<ConversationEntity>> call, Throwable t) {

            }
        });
    }

    public void createConversation(HashMap<String, Object> params) {
        Call<ConversationEntity> call = _api.addConversation(params);
        call.enqueue(new Callback<ConversationEntity>() {
            @Override
            public void onResponse(Call<ConversationEntity> call, Response<ConversationEntity> response) {
                if (response.isSuccessful()) {
//                    ConversationEntity conversationEntity = response.body();

                    _view.changeStep("inbox");
                }

            }

            @Override
            public void onFailure(Call<ConversationEntity> call, Throwable t) {
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

                    _view.populateSearchPseudo(userEntities);
//                    List<ConversationEntity> conversationEntities = new ArrayList<ConversationEntity>();
//
//                    conversationEntities.add(conversationEntity);
//
//                    _view.populateInbox(conversationEntities);
                }

            }

            @Override
            public void onFailure(Call<List<UserEntity>> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });

    }

    public void conversationSelected(String conversation_id) {
        Call<ConversationEntity> call = _api.getConversation(conversation_id);
        call.enqueue(new Callback<ConversationEntity>() {
            @Override
            public void onResponse(Call<ConversationEntity> call, Response<ConversationEntity> response) {
                if (response.isSuccessful()) {
                    ConversationEntity conversationEntity = response.body();

                    _view.setCurrentConversation(conversationEntity);
                    _view.changeStep("inbox_conversation");
                }

            }

            @Override
            public void onFailure(Call<ConversationEntity> call, Throwable t) {
                Log.d("error", t.getMessage());

            }
        });
    }

    public void replyConversation(HashMap<String, Object> params, String conversation_id) {
        Call<MessageEntity> call = _api.replyConversation(params, conversation_id);
        call.enqueue(new Callback<MessageEntity>() {
            @Override
            public void onResponse(Call<MessageEntity> call, Response<MessageEntity> response) {
                if (response.isSuccessful()) {
                    MessageEntity messageEntity = response.body();

                    _view.addMessageToConversation(messageEntity);
                    _view.clearMessageConversation();
                }

            }

            @Override
            public void onFailure(Call<MessageEntity> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });

    }

    public void updateReadState(String conversationEntity_id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("read", true);
        Call<Void> call = _api.updateReadState(conversationEntity_id, params);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }
}
