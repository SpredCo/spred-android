package com.eip.roucou_c.spred.Inbox;

import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.MessageEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.List;

/**
 * Created by roucou_c on 14/09/2016.
 */
public interface IInboxView {
    void populateInbox(List<ConversationEntity> conversationEntities);

    UserEntity getUserEntity();

    void cancelRefresh();

    void populateSearchPseudo(List<UserEntity> userEntities);

    String getMessageConversation();

    String getSubjectCreateConversation();

    List<UserEntity> getReceiverCreateConversation();

    void setErrorReceiverCreateConversation(int resId);

    void setErrorMessageCreateConversation(int resId);

    void setErrorSubjectCreateConversation(int resId);

    void changeStep(String step);

    void conversationSelected(String conversation_id);

    void setCurrentConversation(ConversationEntity conversationEntity);

    void addMessageToConversation(MessageEntity messageEntity);

    void clearMessageConversation();
}
