package com.eip.roucou_c.spred.Stream;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.ChatEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roucou_c on 04/07/2016.
 */
public class AdapterChatMessage extends RecyclerView.Adapter<AdapterChatMessage.ViewHolder> {

    private final IStreamView _iStreamView;
    private List<ChatEntity> chatEntities = new ArrayList<>();
    private UserEntity _userEntity = null;

    public AdapterChatMessage(UserEntity userEntity, IStreamView iStreamView) {
        _userEntity = userEntity;
        _iStreamView = iStreamView;
    }

    @Override
    public AdapterChatMessage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        if (viewType == 1) {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_conversation_message_right, parent, false);
        }
        else {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_conversation_message_left, parent, false);
        }
        return new ViewHolder(itemLayoutView);
    }

    public void setChatEntities(List<ChatEntity> chatEntities) {
        this.chatEntities = chatEntities;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        ChatEntity chatEntity = chatEntities.get(position);

        if (_userEntity != null && Objects.equals(chatEntity.get_sender(), _userEntity.get_pseudo())) {
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(AdapterChatMessage.ViewHolder holder, int position) {
        ChatEntity chatEntity = chatEntities.get(position);

        holder.lblFrom.setText("@"+chatEntity.get_sender());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);
        SimpleDateFormat hour_min = new SimpleDateFormat("HH:mm", Locale.FRANCE);

        try {
            Date date = format.parse(chatEntity.get_date());
            holder.date.setText(hour_min.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String url = chatEntity.get_user_picture().contains("http") ? chatEntity.get_user_picture() : "https://"+ ServiceGeneratorApi.API_BASE_URL+chatEntity.get_user_picture();

        _iStreamView.getImageProfile(url, holder.profile);
        holder.txtMsg.setText(chatEntity.get_text());
    }

    @Override
    public int getItemCount() {
        return chatEntities  == null ? 0 : chatEntities.size();
    }

    public void addChatEntity(ChatEntity chatEntity) {
        this.chatEntities.add(chatEntity);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblFrom;
        TextView txtMsg;
        TextView date;
        CircleImageView profile;

        public ViewHolder(View view) {
            super(view);

            lblFrom = (TextView) view.findViewById(R.id.lblMsgFrom);
            txtMsg = (TextView) view.findViewById(R.id.txtMsg);
            date = (TextView) view.findViewById(R.id.date);
            profile = (CircleImageView) view.findViewById(R.id.profile);
        }
    }
}
