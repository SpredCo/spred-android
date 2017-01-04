package com.eip.roucou_c.spred.Stream;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.CastTokenEntity;
import com.eip.roucou_c.spred.Entities.ChatEntity;
import com.eip.roucou_c.spred.Entities.QuestionEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by roucou_c on 01/07/2016.
 */
public final class ServiceWeb {
    ServiceWeb instance = null;
    private final WebSocket _webSocket;
    private final CastTokenEntity _castTokenEntity;
    private final IStreamView _iStreamView;

    private Manager _manager = null;
    private UserEntity _userEntity = null;
    public Context _context = null;

    public final static ServiceWeb getInstance(Context context, CastTokenEntity castTokenEntity, IStreamView iStreamView) {
//        if (ServiceWeb.instance == null) {
//            synchronized(ServiceWeb.class) {
//                if (ServiceWeb.instance == null) {
//                    ServiceWeb.instance = new ServiceWeb(context, castTokenEntity, iStreamView);
//                }
//            }
//        }
        return new ServiceWeb(context, castTokenEntity, iStreamView);
    }

    private ServiceWeb(Context context, CastTokenEntity castTokenEntity, IStreamView iStreamView) {
        _context = context;
        _webSocket = new WebSocket();
        _castTokenEntity = castTokenEntity;
        _iStreamView = iStreamView;
        initEvent();
    }

    private void initEvent() {
        _webSocket._socket.on(WebSocket.AUTH_REQUEST, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                auth();
            }
        });

        _webSocket._socket.on(WebSocket.AUTH_ANSWER, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject json = new JSONObject(String.valueOf(args [0]));
                    Log.d(WebSocket.AUTH_ANSWER, String.valueOf(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _webSocket._socket.on(WebSocket.MESSAGE, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                final String json = String.valueOf(args [0]);
                final Activity activity = ((Activity)_context);
                String classname = activity.getClass().getSimpleName();

                if (Objects.equals(classname, StreamActivity.class.getSimpleName())) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            ChatEntity chatEntity = gson.fromJson(json, ChatEntity.class);
                            _iStreamView.setMessage(chatEntity);
                        }
                    });
                }

            }
        });

        _webSocket._socket.on(WebSocket.QUESTIONS, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                updateQuestion(String.valueOf(args [0]));
            }
        });

        _webSocket._socket.on(WebSocket.UP_QUESTION, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                updateQuestion(String.valueOf(args [0]));
            }
        });

        _webSocket._socket.on(WebSocket.DOWN_QUESTION, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                updateQuestion(String.valueOf(args [0]));
            }
        });
    }

    private void updateQuestion(final String json) {
        final Activity activity = ((Activity)_context);
        String classname = activity.getClass().getSimpleName();

        if (Objects.equals(classname, StreamActivity.class.getSimpleName())) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    QuestionEntity questionEntity = gson.fromJson(json, QuestionEntity.class);
                    _iStreamView.updateQuestions(questionEntity);
                }
            });
        }

    }

    private void auth() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", _castTokenEntity.get_token());

        JSONObject json = new JSONObject(params);
        this._webSocket._socket.emit(WebSocket.AUTH_ANSWER, json);
    }

    public void sendMessage(String message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("text", message);

        JSONObject json = new JSONObject(params);
        this._webSocket._socket.emit(WebSocket.MESSAGE, json);
    }

    public void sendQuestion(String question) {
        HashMap<String, String> params = new HashMap<>();
        params.put("text", question);

        JSONObject json = new JSONObject(params);
        this._webSocket._socket.emit(WebSocket.QUESTIONS, json);
    }

    public void downQuestion(int id) {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("id", id);

        JSONObject json = new JSONObject(params);
        this._webSocket._socket.emit(WebSocket.DOWN_QUESTION, json);
    }

    public void upQuestion(int id) {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("id", id);

        JSONObject json = new JSONObject(params);
        this._webSocket._socket.emit(WebSocket.UP_QUESTION, json);
    }

    public void disconnect() {
        this._webSocket._socket.disconnect();
    }
}
