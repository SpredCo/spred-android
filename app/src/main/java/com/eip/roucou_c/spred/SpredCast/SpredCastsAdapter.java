package com.eip.roucou_c.spred.SpredCast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.ConversationEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Inbox.IInboxView;
import com.eip.roucou_c.spred.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roucou_c on 11/11/2016.
 */
public class SpredCastsAdapter extends RecyclerView.Adapter<SpredCastsAdapter.MyViewHolder> {

    private List<SpredCastEntity> _spredCastEntities;
    private ISpredCastView _iSpredCastView;

    public SpredCastsAdapter(ISpredCastView iSpredCastView) {
        this._iSpredCastView = iSpredCastView;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView _name;
        public TextView _description;
        public TextView _tags;
        private String _spredcast_id;

        public MyViewHolder(View view) {
            super(view);
            _name = (TextView) view.findViewById(R.id.spredcasts_name);
            _description = (TextView) view.findViewById(R.id.spredcasts_description);
            _tags = (TextView) view.findViewById(R.id.spredcasts_tags);

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.spredcasts);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.inbox_conversation:
//                    _IInboxView.conversationSelected(_conversation_id);
//                    break;
//            }
        }
    }

    public void set_spredCastEntities(List<SpredCastEntity> _spredCastEntities) {
        this._spredCastEntities = _spredCastEntities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spredcast_single_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SpredCastEntity spredCastEntity = _spredCastEntities.get(position);

        holder._spredcast_id = spredCastEntity.get_id();
        holder._name.setText(spredCastEntity.get_name());
        holder._description.setText(spredCastEntity.get_description());

        if (spredCastEntity.get_tags() != null && !spredCastEntity.get_tags().isEmpty()) {
            String tags = "";
            for (TagEntity tagEntity : spredCastEntity.get_tags()) {
                tags += "#" + tagEntity.get_name() + ", ";
            }
            tags = tags.substring(0, tags.length() - 2);
            holder._tags.setText(tags);
        }
        else {
            holder._tags.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return _spredCastEntities ==  null ? 0 : _spredCastEntities.size();
    }
}
