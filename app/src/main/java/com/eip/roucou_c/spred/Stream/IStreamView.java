package com.eip.roucou_c.spred.Stream;

import android.widget.ImageView;

import com.eip.roucou_c.spred.Entities.CastTokenEntity;
import com.eip.roucou_c.spred.Entities.ChatEntity;
import com.eip.roucou_c.spred.Entities.QuestionEntity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roucou_c on 20/12/2016.
 */

public interface IStreamView {
    void setCastToken(CastTokenEntity castTokenEntity);

    void setMessage(ChatEntity chatEntity);

    void sendMessage(String message);

    void updateQuestions(QuestionEntity questionEntity);

    void sendQuestion(String question);

    void upQuestion(int id);

    void downQuestion(int id);

    void getImageProfile(String url, ImageView profile);
}