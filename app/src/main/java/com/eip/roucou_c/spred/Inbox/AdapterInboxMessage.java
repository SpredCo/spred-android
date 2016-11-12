package com.eip.roucou_c.spred.Inbox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.MessageEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roucou_c on 04/07/2016.
 */
public class AdapterInboxMessage extends RecyclerView.Adapter<AdapterInboxMessage.ViewHolder> {

    private ConversationEntity _conversationEntity;
    private UserEntity _userEntity = null;

    public AdapterInboxMessage(ConversationEntity conversationEntity, UserEntity userEntity) {
        _conversationEntity = conversationEntity;
        _userEntity = userEntity;
    }

    @Override
    public AdapterInboxMessage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        if (viewType == 1) {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_conversation_message_right, parent, false);
        }
        else {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_conversation_message_left, parent, false);
        }
        return new ViewHolder(itemLayoutView);
    }

    public void set_messageEntities(ConversationEntity _conversationEntity) {
        this._conversationEntity = _conversationEntity;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageEntity messageEntity = _conversationEntity.get_msg().get(position);

        if (Objects.equals(messageEntity.get_from(), _userEntity.get_id())) {
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(AdapterInboxMessage.ViewHolder holder, int position) {

        MessageEntity messageEntity = _conversationEntity.get_msg().get(position);
        for (UserEntity userEntity : _conversationEntity.get_members()) {
            if (Objects.equals(userEntity.get_id(), messageEntity.get_from())) {
                holder.lblFrom.setText("@"+userEntity.get_pseudo());

                String message = messageEntity.get_created_at().replace("T", " ").replace("Z", "");
                message = message.substring(0, message.length()-7);
                holder.date.setText(message);
//                URL newurl = null;
//                try {
//                    newurl = new URL(userEntity.get_picture_url());
//                    InputStream inputStream = newurl.openConnection().getInputStream();
//                    Bitmap mIcon_val = BitmapFactory.decodeStream(inputStream);
//                    holder.profile.setImageBitmap(mIcon_val);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        }
        holder.txtMsg.setText(messageEntity.get_content());
    }

    @Override
    public int getItemCount() {
        return _conversationEntity.get_msg()  == null ? 0 : _conversationEntity.get_msg().size();
    }

    public void addMessageEntity(MessageEntity messageEntity) {
        this._conversationEntity.get_msg().add(messageEntity);
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
