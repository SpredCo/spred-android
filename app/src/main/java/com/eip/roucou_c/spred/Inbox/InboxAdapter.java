package com.eip.roucou_c.spred.Inbox;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roucou_c on 29/09/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {

    private List<ConversationEntity> _conversationEntities;
    private IInboxView _IInboxView;

    public InboxAdapter(IInboxView iInboxView) {
        this._IInboxView = iInboxView;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView _object;
        public TextView _members_pseudo;
        private String _conversation_id;
        public ImageView _read;

        public MyViewHolder(View view) {
            super(view);
            _object = (TextView) view.findViewById(R.id.inbox_object);
            _members_pseudo = (TextView) view.findViewById(R.id.inbox_members_pseudo);
            _read = (ImageView) view.findViewById(R.id.inbox_read);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.inbox_conversation);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.inbox_conversation:
                    _IInboxView.conversationSelected(_conversation_id);
                    break;
            }
        }
    }

    public List<ConversationEntity> get_conversationEntities() {
        return _conversationEntities;
    }

    public void set_conversationEntities(List<ConversationEntity> _conversationEntities) {
        this._conversationEntities = _conversationEntities;
    }

    public InboxAdapter() {
        this._conversationEntities = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_single_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ConversationEntity conversationEntity = _conversationEntities.get(position);

        holder._conversation_id = conversationEntity.get_id();
        holder._object.setText(conversationEntity.get_object());

        String members_pseudo = "";
        for (UserEntity userEntity: conversationEntity.get_members()){
            members_pseudo += "@"+userEntity.get_pseudo()+", ";
        }

        if (conversationEntity.is_read()) {
            holder._read.setImageResource(R.drawable.ic_drafts_black_24dp);
        }
        members_pseudo = members_pseudo.substring(0, members_pseudo.length()-2);

        holder._members_pseudo.setText(members_pseudo);
    }

    @Override
    public int getItemCount() {
        return _conversationEntities ==  null ? 0 : _conversationEntities.size();
    }
}
