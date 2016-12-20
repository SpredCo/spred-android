package com.eip.roucou_c.spred.Inbox;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class InboxPresenter {

    private final Manager _manager;
    private final IInboxView _view;
    private final InboxService _inboxService;

    public InboxPresenter(IInboxView view, Manager manager, TokenEntity tokenEntity) {
        _view = view;
        _manager = manager;
        this._inboxService = new InboxService(view, manager, tokenEntity);
    }

    public void getInbox() {
        _inboxService.getInbox();
    }

    public void addConversation() {
        List<String> userIdArrays = new ArrayList<>();
        UserEntity userEntity = _view.getUserEntity();

        userIdArrays.add(userEntity.get_id());

        userIdArrays.add("57ed74f81c3efa05ebc65fe7");

        HashMap<String, Object> params = new HashMap<>();

        params.put("object", "toto");
        params.put("content", "premier message negro");
        params.put("members", userIdArrays);
        _inboxService.createConversation(params);
    }

    public void onRefreshInbox() {
        _inboxService.getInbox();
    }

    public void searchPartialPseudo(String s) {
        if (!s.isEmpty()) {
            _inboxService.searchPartialPseudo(s);
        }
    }

    public void createConversation() {

        List<UserEntity> receiverUserEntities = _view.getReceiverCreateConversation();

        String message = _view.getMessageConversation();
        String subject = _view.getSubjectCreateConversation();

        boolean isError = false;

        if (receiverUserEntities.size() == 1) {
            _view.setErrorReceiverCreateConversation(R.string.inbox_create_conversation_empty_receiver);
            isError = true;
        }
        if (subject.isEmpty()) {
            _view.setErrorSubjectCreateConversation(R.string.inbox_create_conversation_empty_subject);
            isError = true;
        }
        if (message.isEmpty()) {
            _view.setErrorMessageCreateConversation(R.string.inbox_create_conversation_empty_message);
            isError = true;
        }
        if (!isError) {
            List<String> userId = new ArrayList<>();

            for (UserEntity userEntity : receiverUserEntities) {
                userId.add(userEntity.get_id());
            }

            HashMap<String, Object> params = new HashMap<>();

            params.put("object", subject);
            params.put("content", message);
            params.put("members", userId);

            _inboxService.createConversation(params);
        }
    }

    public void conversationSelected(String conversation_id) {
        _inboxService.conversationSelected(conversation_id);
    }

    public void replyConversation(String conversation_id) {
        String message = _view.getMessageConversation();

        if (!message.isEmpty()) {

            HashMap<String, Object> params = new HashMap<>();
            params.put("content", message);

            _inboxService.replyConversation(params, conversation_id);
        }
    }

    public void updateReadState(String conversationEntity_id) {
        _inboxService.updateReadState(conversationEntity_id);
    }
}
