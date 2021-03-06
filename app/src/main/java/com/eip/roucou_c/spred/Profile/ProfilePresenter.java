package com.eip.roucou_c.spred.Profile;

import android.util.Patterns;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by roucou_c on 27/09/2016.
 */
class ProfilePresenter {

    private final ProfileService _profileService;
    private final IProfileView _iProfileView;
    private final IFollowersView _iFollowersView;

    ProfilePresenter(IProfileView view, IFollowersView iFollowersView, Manager manager, TokenEntity tokenEntity) {
        this._iProfileView = view;
        this._iFollowersView = iFollowersView;
        this._profileService = new ProfileService(view, iFollowersView, manager, tokenEntity);
    }

    void getProfile(boolean populate) {
        _profileService.getProfile(populate);
    }

    void onSaveClicked() {
        String email = _iProfileView.getEmail();
        String firstName = _iProfileView.getFirstName();
        String lastName = _iProfileView.getLastName();

        UserEntity userEntity = _iProfileView.getUserEntity();

        boolean isError = false;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (email.isEmpty()) {
                _iProfileView.setErrorEmail(R.string.empty_input);
            }
            else {
                _iProfileView.setErrorEmail(R.string.invalid_email);
            }
            isError = true;
        }
        if (firstName.isEmpty()) {
            _iProfileView.setErrorFirstName(R.string.empty_input);
            isError = true;
        }
        if (lastName.isEmpty()) {
            _iProfileView.setErrorLastName(R.string.empty_input);
            isError = true;
        }

        MaterialEditText materialEditTextEmail = _iProfileView.getMaterialEditTextEmail();

        if (materialEditTextEmail.getError() == null && !isError) {

            HashMap<String, String> params = new HashMap<>();

            if (!Objects.equals(userEntity.get_email(), email)) {
                params.put("email", email);
            }
            if (!Objects.equals(userEntity.get_first_name(), firstName)) {
                params.put("first_name", firstName);
            }
            if (!Objects.equals(userEntity.get_last_name(), lastName)) {
                params.put("last_name", lastName);
            }

            _profileService.patchProfile(params);
        }
    }

    void checkEmail() {
        String email = _iProfileView.getEmail();

        UserEntity userEntity = _iProfileView.getUserEntity();
        if (email != null && !email.isEmpty() && userEntity != null && !Objects.equals(userEntity.get_email(), email)) {
            this._profileService.checkEmail(email);
        }
    }

    void follow(String user_id, boolean isFollow) {
        _profileService.follow(user_id, isFollow);
    }

    void getFollowing() {
        _profileService.getFollowing();
    }

    void getFollowers() {
        _profileService.getFollowers();
    }
}
