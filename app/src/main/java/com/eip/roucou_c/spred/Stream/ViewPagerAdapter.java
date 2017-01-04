package com.eip.roucou_c.spred.Stream;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.eip.roucou_c.spred.Entities.CastTokenEntity;
import com.eip.roucou_c.spred.Entities.ChatEntity;
import com.eip.roucou_c.spred.Entities.QuestionEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roucou_c on 21/09/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter  {

    private List<TabFragment> fragmentList = new ArrayList<>();
    private CastTokenEntity _castToken;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabFragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addTab(TabFragment tabFragment) {
        fragmentList.add(tabFragment);
    }

    public static class TabFragment extends Fragment {

        public IStreamView _iStreamView;
        private int _type;
        private UserEntity _userEntity;
        private SpredCastEntity _spredCast;

        private AdapterChatMessage _adapterChatMessage;
        private Button _stream_chat_send;
        private EditText _stream_chat_message;
        private RecyclerView _stream_chat_recyclerView;
        private AdapterQuestion _adapterQuestion;
        private Button _stream_question_send;
        private EditText _stream_question_text;
        private RecyclerView _stream_question_recyclerView;
        private CastTokenEntity _castTokenEntity;
        private WebView mWebRTCWebView;

        public static TabFragment newInstance(int type, IStreamView iStreamView, UserEntity userEntity, SpredCastEntity spredCast) {
            TabFragment fragment = new TabFragment();
            fragment._type = type;
            fragment._iStreamView = iStreamView;
            fragment._userEntity = userEntity;
            fragment._spredCast = spredCast;
            return fragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;

            switch (_type) {
                case 1:
                    rootView = inflater.inflate(R.layout.stream_chat, container, false);
                    _adapterChatMessage = new AdapterChatMessage(_userEntity, _iStreamView);

                    _stream_chat_send = (Button) rootView.findViewById(R.id.stream_chat_send);
                    _stream_chat_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String message = _stream_chat_message.getText().toString();
                            _iStreamView.sendMessage(message);
                            _stream_chat_message.setText(null);
                        }
                    });

                    _stream_chat_message = (EditText) rootView.findViewById(R.id.stream_chat_message);

                    _stream_chat_recyclerView = (RecyclerView) rootView.findViewById(R.id.stream_chat_list_view_messages);

                    RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
                    _stream_chat_recyclerView.setLayoutManager(mLayoutManager2);
                    _stream_chat_recyclerView.setItemAnimator(new DefaultItemAnimator());
                    _stream_chat_recyclerView.setAdapter(_adapterChatMessage);

                    _stream_chat_recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            _stream_chat_recyclerView.scrollToPosition(_stream_chat_recyclerView.getAdapter().getItemCount()-1);
                        }
                    });
                    _stream_chat_recyclerView.scrollToPosition(_stream_chat_recyclerView.getAdapter().getItemCount()-1);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.stream_video, container, false);
                    mWebRTCWebView = (WebView) rootView.findViewById(R.id.webview);

                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.stream_qestions, container, false);

                    _adapterQuestion = new AdapterQuestion(_userEntity, _iStreamView);

                    _stream_question_send = (Button) rootView.findViewById(R.id.stream_question_send);
                    _stream_question_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String question = _stream_question_text.getText().toString();
                            _iStreamView.sendQuestion(question);
                            _stream_question_text.setText(null);
                        }
                    });

                    _stream_question_text = (EditText) rootView.findViewById(R.id.stream_question_text);

                    _stream_question_recyclerView = (RecyclerView) rootView.findViewById(R.id.stream_question_list_view);

                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getContext());
                    _stream_question_recyclerView.setLayoutManager(mLayoutManager3);
                    _stream_question_recyclerView.setItemAnimator(new DefaultItemAnimator());
                    _stream_question_recyclerView.setAdapter(_adapterQuestion);

//                    _stream_question_recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                        @Override
//                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                            _stream_chat_recyclerView.scrollToPosition(_stream_chat_recyclerView.getAdapter().getItemCount()-1);
//                        }
//                    });
//                    _stream_chat_recyclerView.scrollToPosition(_stream_chat_recyclerView.getAdapter().getItemCount()-1);

                    break;

            }
            return rootView;
        }

        public void setUpWebView(final CastTokenEntity castTokenEntity) {

            setUpWebViewDefaults(mWebRTCWebView);

            mWebRTCWebView.loadUrl("https://"+ ServiceGeneratorApi.API_BASE_URL+"/casts/"+castTokenEntity.get_token()+"/webview");

            mWebRTCWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    getActivity().runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void run() {
                            if (request.getOrigin().toString().equals("https://"+ ServiceGeneratorApi.API_BASE_URL+"/casts/"+castTokenEntity.get_token()+"/webview")) {
                                request.grant(request.getResources());
                            } else {
                                request.deny();
                            }
                        }
                    });
                }
            });

        }

        public void setMessage(ChatEntity chatEntity) {
            _adapterChatMessage.addChatEntity(chatEntity);
            _stream_chat_recyclerView.scrollToPosition(_stream_chat_recyclerView.getAdapter().getItemCount()-1);
        }

        private void setUpWebViewDefaults(WebView webView) {
            WebSettings settings = webView.getSettings();

            // Enable Javascript
            settings.setJavaScriptEnabled(true);

            // Use WideViewport and Zoom out if there is no viewport defined
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            // Enable pinch to zoom without the zoom buttons
            settings.setBuiltInZoomControls(true);

            // Allow use of Local Storage
            settings.setDomStorageEnabled(true);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                // Hide the zoom controls for HONEYCOMB+
                settings.setDisplayZoomControls(false);
            }

            // Enable remote debugging via chrome://inspect
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            webView.setWebViewClient(new WebViewClient());

            // AppRTC requires third party cookies to work
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        public void updateQuestions(QuestionEntity questionEntity) {
            _adapterQuestion.updateQuestions(questionEntity);
        }

        public void stopWebviewLoading() {
            mWebRTCWebView.loadUrl("about:blank");
        }
    }

}
