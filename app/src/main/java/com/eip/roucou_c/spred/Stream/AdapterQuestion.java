package com.eip.roucou_c.spred.Stream;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.ChatEntity;
import com.eip.roucou_c.spred.Entities.QuestionEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roucou_c on 04/07/2016.
 */
public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.ViewHolder> {

    private final IStreamView _iStreamView;
    private List<QuestionEntity> _questionEntities = new ArrayList<>();
    private UserEntity _userEntity = null;

    public AdapterQuestion(UserEntity userEntity, IStreamView iStreamView) {
        _iStreamView = iStreamView;
        _userEntity = userEntity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_question_single_row, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    public void set_questionEntities(List<QuestionEntity> _questionEntities) {
        this._questionEntities = _questionEntities;
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final QuestionEntity questionEntity = _questionEntities.get(position);

        holder._from.setText("@"+questionEntity.get_sender());

        holder._question.setText(questionEntity.get_text());
        holder._nbvote.setText(String.valueOf(questionEntity.get_nbVote()));

        holder._up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _iStreamView.upQuestion(questionEntity.get_id());
                holder._up.setEnabled(false);
            }
        });

        holder._down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _iStreamView.downQuestion(questionEntity.get_id());
                holder._down.setEnabled(false);
            }
        });

        String url = questionEntity.get_user_picture().contains("http") ? questionEntity.get_user_picture() : "https://"+ ServiceGeneratorApi.API_BASE_URL+questionEntity.get_user_picture();

        _iStreamView.getImageProfile(url, holder._from_profile);

    }

    @Override
    public int getItemCount() {
        return _questionEntities  == null ? 0 : _questionEntities.size();
    }

    public void updateQuestions(QuestionEntity questionEntity) {
        List<QuestionEntity> tmpQuestionEntities = new ArrayList<>();

        if (_questionEntities != null && _questionEntities.size() != 0) {
            for (QuestionEntity questionEntity1 : _questionEntities) {
                if (questionEntity.get_id() == questionEntity1.get_id()) {
                    tmpQuestionEntities.add(questionEntity);
                } else {
                    tmpQuestionEntities.add(questionEntity1);
                }
            }
            if (!tmpQuestionEntities.contains(questionEntity)) {
                tmpQuestionEntities.add(questionEntity);
            }

        }
        else {
            tmpQuestionEntities.add(questionEntity);
        }


        Collections.sort(tmpQuestionEntities, new Comparator<QuestionEntity>(){
            public int compare(QuestionEntity questionEntity1, QuestionEntity questionEntity2) {
                return Integer.valueOf(questionEntity2.get_nbVote()).compareTo(questionEntity1.get_nbVote());
            }
        });

        this.set_questionEntities(tmpQuestionEntities);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView _question;
        private final TextView _from;
        private final ImageView _from_profile;
        private final ImageButton _up;
        private final ImageButton _down;
        private final TextView _nbvote;

        public ViewHolder(View view) {
            super(view);

            _question = (TextView) view.findViewById(R.id.stream_question);
            _nbvote = (TextView) view.findViewById(R.id.stream_question_nbvote);
            _from = (TextView) view.findViewById(R.id.stream_question_from);
            _from_profile = (ImageView) view.findViewById(R.id.stream_question_from_profile);
            _up = (ImageButton) view.findViewById(R.id.stream_question_up);
            _down = (ImageButton) view.findViewById(R.id.stream_question_down);
        }
    }
}
