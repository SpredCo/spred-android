package com.eip.roucou_c.spred.Inbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.MessageEntity;
import com.eip.roucou_c.spred.Inbox.Tokenfield.ContactsCompletionView;
import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by roucou_c on 29/09/2016.
 */
public class InboxActivity extends AppCompatActivity implements IInboxView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TokenCompleteTextView.TokenListener<UserEntity>, TextWatcher {

    private Manager _manager;
    private InboxPresenter _inboxPresenter;

    private Toolbar _toolbar;

    private RecyclerView _inbox_recycler_view;
    private InboxAdapter _inbox_adapter;
    private UserEntity _userEntity;
    private Object _curentStep;
    private SwipeRefreshLayout _inbox_swipeRefreshLayout;
    private FloatingActionButton _inbox_floatActionButton;
    private ContactsCompletionView _completionView;
    private List<UserEntity> _create_conversation_userEntities;
    private List<UserEntity> _create_conversation_tmp_userEntities;
    private ContactsCompletionView inbox_create_conversation_receiver;
    private FilteredArrayAdapter<UserEntity> _adapterPseudo;
    private MaterialEditText _inbox_create_conversation_subject;
    private MaterialEditText _inbox_create_conversation_message;
    private FancyButton _inbox_create_conversation_submit;
    private TextInputLayout _inbox_create_conversation_receiver_textInputLayout;
    private AdapterInboxMessage _adapterInboxMessage;
    private Button _inbox_conversation_send;
    private EditText _inbox_conversation_message;
    private ConversationEntity _currentConversation = null;
    private RecyclerView _inbox_conversation_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _inboxPresenter = new InboxPresenter(this, _manager, tokenEntity);

        _userEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");

        changeStep("inbox");
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void changeStep(String step){
        _curentStep = step;
        switch (step) {
            case "inbox":
                setContentView(R.layout.inbox);

                _inbox_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                _inbox_swipeRefreshLayout.setOnRefreshListener(this);
                _inbox_recycler_view = (RecyclerView) findViewById(R.id.inbox_recycler_view);

                _inbox_adapter = new InboxAdapter(this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                _inbox_recycler_view.setLayoutManager(mLayoutManager);
                _inbox_recycler_view.setItemAnimator(new DefaultItemAnimator());
                _inbox_recycler_view.setAdapter(_inbox_adapter);

                _inbox_floatActionButton = (FloatingActionButton) findViewById(R.id.inbox_floatActionButton);
                _inbox_floatActionButton.setOnClickListener(this);

                _inboxPresenter.getInbox();
                break;
            case "inbox_conversation":
                setContentView(R.layout.inbox_conversation);

                _adapterInboxMessage = new AdapterInboxMessage(_currentConversation, _userEntity);

                _inbox_conversation_send = (Button) findViewById(R.id._inbox_conversation_send);
                _inbox_conversation_send.setOnClickListener(this);

                _inbox_conversation_message = (EditText) findViewById(R.id._inbox_conversation_message);

                _inbox_conversation_recyclerView = (RecyclerView) findViewById(R.id._inbox_conversation_list_view_messages);

                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
                _inbox_conversation_recyclerView.setLayoutManager(mLayoutManager2);
                _inbox_conversation_recyclerView.setItemAnimator(new DefaultItemAnimator());
                _inbox_conversation_recyclerView.setAdapter(_adapterInboxMessage);

                _inbox_conversation_recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        _inbox_conversation_recyclerView.scrollToPosition(_inbox_conversation_recyclerView.getAdapter().getItemCount()-1);
                    }
                });
                _inbox_conversation_recyclerView.scrollToPosition(_inbox_conversation_recyclerView.getAdapter().getItemCount()-1);
                break;
            case "inbox_create_conversation":
                setContentView(R.layout.inbox_create_conversation);

                inbox_create_conversation_receiver = (ContactsCompletionView) findViewById(R.id.inbox_create_conversation_receiver);
                inbox_create_conversation_receiver.addTextChangedListener(this);
                _create_conversation_userEntities = new ArrayList<>();
                _create_conversation_tmp_userEntities = new ArrayList<>();
                _adapterPseudo = new FilteredArrayAdapter<UserEntity>(this, R.layout.tokenfield_single_row, _create_conversation_tmp_userEntities) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {

                            LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                            convertView = l.inflate(R.layout.tokenfield_single_row, parent, false);
                        }

                        UserEntity userEntity = getItem(position);
                        ((TextView) convertView.findViewById(R.id.name)).setText(userEntity.get_first_name());
                        ((TextView) convertView.findViewById(R.id.email)).setText(userEntity.get_email());

                        return convertView;
                    }

                    @Override
                    protected boolean keepObject(UserEntity userEntity, String mask) {
                        mask = mask.toLowerCase();
                        return userEntity.get_first_name().toLowerCase().startsWith(mask) || userEntity.get_email().toLowerCase().startsWith(mask);
                    }
                };

                _completionView = (ContactsCompletionView) findViewById(R.id.inbox_create_conversation_receiver);
                _completionView.setAdapter(_adapterPseudo);
                _completionView.setSplitChar(';');
                _completionView.setTokenListener(this);
                _completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);


                _inbox_create_conversation_receiver_textInputLayout = (TextInputLayout) findViewById(R.id.inbox_create_conversation_receiver_textInputLayout);
                _inbox_create_conversation_subject = (MaterialEditText) findViewById(R.id.inbox_create_conversation_subject);
                _inbox_create_conversation_message = (MaterialEditText) findViewById(R.id.inbox_create_conversation_message);
                _inbox_create_conversation_submit = (FancyButton) findViewById(R.id.inbox_create_conversation_submit);
                _inbox_create_conversation_submit.setOnClickListener(this);

                break;
        }
    }

    @Override
    public void populateInbox(List<ConversationEntity> conversationEntities) {
        if (conversationEntities.isEmpty()) {
            _inboxPresenter.addConversation();
        }
        if (_inbox_adapter != null) {
            _inbox_adapter.set_conversationEntities(conversationEntities);
            _inbox_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public UserEntity getUserEntity() {
        return _userEntity;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Objects.equals(_curentStep, "inbox")) {
                    this.finish();
                }
                else if (Objects.equals(_curentStep, "inbox_conversation")) {
                    changeStep("inbox");
                }
                else if (Objects.equals(_curentStep, "inbox_create_conversation")) {
                    changeStep("inbox");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        _inboxPresenter.onRefreshInbox();
    }

    @Override
    public void cancelRefresh() {
        _inbox_swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void populateSearchPseudo(List<UserEntity> userEntities) {

        List<UserEntity> tmpUserEntities = new ArrayList<>();

        if (_create_conversation_userEntities != null) {
            for (UserEntity userEntity : userEntities) {
                boolean insert = true;
                if (Objects.equals(_userEntity.get_pseudo(), userEntity.get_pseudo())) {
                    insert = false;
                }
                else {
                    for (UserEntity _create_conversation_userEntity : _create_conversation_userEntities) {
                        if (Objects.equals(_create_conversation_userEntity.get_pseudo(), userEntity.get_pseudo())) {
                            insert = false;
                        }
                    }
                    if (insert && !tmpUserEntities.contains(userEntity)) {
                        tmpUserEntities.add(userEntity);
                    }
                }
            }
        }
        _adapterPseudo.clear();
        _adapterPseudo.addAll(tmpUserEntities);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inbox_floatActionButton:
                changeStep("inbox_create_conversation");
                break;
            case R.id.inbox_create_conversation_submit:
                _inboxPresenter.createConversation();
                break;
            case R.id._inbox_conversation_send:
                _inboxPresenter.replyConversation(_currentConversation.get_id());
                break;
        }
    }

    @Override
    public void onTokenAdded(UserEntity token) {
        _create_conversation_userEntities.add(token);
    }

    @Override
    public void onTokenRemoved(UserEntity token) {
        _create_conversation_userEntities.remove(token);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        _inboxPresenter.searchPartialPseudo(String.valueOf(s).replaceAll(";", "").trim());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public String getMessageConversation() {
        String message = null;

        if (_curentStep == "inbox_create_conversation") {
            message = _inbox_create_conversation_message.getText().toString();
        }
        else if (_curentStep == "inbox_conversation") {
            message = _inbox_conversation_message.getText().toString();
        }
        return message;
    }

    @Override
    public String getSubjectCreateConversation() {
        return _inbox_create_conversation_subject.getText().toString();
    }

    @Override
    public List<UserEntity> getReceiverCreateConversation() {
        List<UserEntity> tmp = _create_conversation_userEntities;

        tmp.add(_userEntity);
        return _create_conversation_userEntities;
    }

    @Override
    public void setErrorReceiverCreateConversation(int resId) {
        _inbox_create_conversation_receiver_textInputLayout.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorMessageCreateConversation(int resId) {
        _inbox_create_conversation_message.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorSubjectCreateConversation(int resId) {
        _inbox_create_conversation_subject.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void conversationSelected(String conversation_id) {
        _inboxPresenter.conversationSelected(conversation_id);
    }

    @Override
    public void setCurrentConversation(ConversationEntity conversationEntity) {
        _currentConversation = conversationEntity;
        _inboxPresenter.updateReadState(conversationEntity.get_id());
    }

    @Override
    public void addMessageToConversation(MessageEntity messageEntity) {
        List<MessageEntity> messageEntities = _currentConversation.get_msg();
        messageEntities.add(messageEntity);
        _currentConversation.set_msg(messageEntities);
        _adapterInboxMessage.notifyDataSetChanged();

        _inbox_conversation_recyclerView.scrollToPosition(_inbox_conversation_recyclerView.getAdapter().getItemCount()-1);
    }

    @Override
    public void clearMessageConversation() {
        _inbox_conversation_message.setText(null);
    }
}
