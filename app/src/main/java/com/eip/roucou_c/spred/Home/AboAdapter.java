package com.eip.roucou_c.spred.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.TabLayout.ViewPagerAdapter;
import com.eip.roucou_c.spred.Profile.ProfileActivity;
import com.eip.roucou_c.spred.R;

import java.util.List;

/**
 * Created by roucou_c on 07/12/2016.
 */
public class AboAdapter extends RecyclerView.Adapter<AboAdapter.MyViewHolder> {

    private final IHomeAboView _iHomeAboView;
    private final Context _context;

    private List<UserEntity> _userEntities;


    public AboAdapter(IHomeAboView iHomeAboView, Context context) {
        this._iHomeAboView = iHomeAboView;
        this._context = context;
    }

    @Override
    public AboAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abo_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AboAdapter.MyViewHolder holder, int position) {
        final UserEntity userEntity = _userEntities.get(position);
        if (userEntity != null) {
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
        return _userEntities == null ? 0 : _userEntities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout _abo_relativeLayout;
        public ImageView _photo;
        public TextView _pseudo;
        public TextView _name;

        public MyViewHolder(View itemView) {
            super(itemView);

            _photo = (ImageView) itemView.findViewById(R.id.profile_photo);
            _pseudo = (TextView) itemView.findViewById(R.id.abo_pseudo);
            _name = (TextView) itemView.findViewById(R.id.abo_name);

            _abo_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.abo);
        }
    }

    public void set_userEntities(List<UserEntity> _userEntities) {
        this._userEntities = _userEntities;
    }
}
