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

import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.TabLayout.ViewPagerAdapter;
import com.eip.roucou_c.spred.Profile.ProfileActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;

import java.util.List;

/**
 * Created by roucou_c on 07/12/2016.
 */
public class AboAdapter extends RecyclerView.Adapter<AboAdapter.MyViewHolder> {

    private final Context _context;
    private final IHomeView _iHomeView;

    private List<FollowEntity> _followEntities;


    public AboAdapter(Context context, IHomeView iHomeView) {
        this._context = context;
        _iHomeView = iHomeView;
    }

    @Override
    public AboAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abo_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AboAdapter.MyViewHolder holder, int position) {
        final FollowEntity followEntity = _followEntities.get(position);
        if (followEntity != null) {
            final UserEntity userEntity = followEntity.get_following();
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

            String url = followEntity.get_following().get_picture_url().contains("http") ? followEntity.get_following().get_picture_url() : "https://"+ ServiceGeneratorApi.API_BASE_URL+followEntity.get_following().get_picture_url();

            _iHomeView.getImageProfile(url, holder._photo);
        }
    }

    @Override
    public int getItemCount() {
        return _followEntities == null ? 0 : _followEntities.size();
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

    public void set_followEntities(List<FollowEntity> _followEntities) {
        this._followEntities = _followEntities;
    }
}
