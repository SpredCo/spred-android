package com.eip.roucou_c.spred.Stream;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by roucou_c on 02/07/2016.
 */
public class WebSocket{

//        private static String IP = "52.212.178.211";
    private static final String IP = "media.spred.tv";

    /**
     * authentication_scope
     */
    public static final String AUTH_REQUEST = "auth_request";
    public static final String AUTH_ANSWER = "auth_answer";

    /**
     * chat_scope
     */
    public static final String MESSAGE = "messages";

    /**
     * question_scope
     */
    public static final String QUESTIONS = "questions";
    public static final String DOWN_QUESTION = "down_question";
    public static final String UP_QUESTION = "up_question";


    public Socket _socket;

    public WebSocket() {

        try {
            _socket = IO.socket("https://"+IP+":3030");
            _socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
