package com.eip.roucou_c.spred.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.FollowerEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;

import java.util.List;

/**
 * Created by roucou_c on 07/12/2016.
 */
public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.MyViewHolder> {

    private final Context _context;

    private List<FollowerEntity> _followerEntities;


    public FollowersAdapter(Context context) {
        this._context = context;
    }

    @Override
    public FollowersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abo_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FollowersAdapter.MyViewHolder holder, int position) {
        final FollowerEntity followerEntity = _followerEntities.get(position);
        if (followerEntity != null) {
            final UserEntity userEntity = followerEntity.get_user();
            String pseudo = "@"+userEntity.get_pseudo();
            holder._pseudo.setText(pseudo);
            String name = userEntity.get_last_name()+" "+userEntity.get_last_name();
            holder._name.setText(name);

            holder._abo_relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(_context,ProfileActivity.class);
                    intent.putExtra("userEntityProfile", userEntity);
                    _context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return _followerEntities == null ? 0 : _followerEntities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout _abo_relativeLayout;
        ImageView _photo;
        TextView _pseudo;
        TextView _name;

        MyViewHolder(View itemView) {
            super(itemView);

            _photo = (ImageView) itemView.findViewById(R.id.profile_photo);
            _pseudo = (TextView) itemView.findViewById(R.id.abo_pseudo);
            _name = (TextView) itemView.findViewById(R.id.abo_name);

            _abo_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.abo);
        }
    }

    public void set_followerEntities(List<FollowerEntity> _followerEntities) {
        this._followerEntities = _followerEntities;
    }
}
