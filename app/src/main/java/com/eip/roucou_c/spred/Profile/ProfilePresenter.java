package com.eip.roucou_c.spred.Profile;

import android.util.Patterns;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SignIn.SignInService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by roucou_c on 27/09/2016.
 */
public class ProfilePresenter {

    private final ProfileService _profileService;
    private final IProfileView _view;

    public ProfilePresenter(IProfileView view, Manager manager, TokenEntity tokenEntity) {
        this._view = view;
        this._profileService = new ProfileService(view, manager, tokenEntity);

    }

    public void getProfile() {
        _profileService.getProfile();
    }

    public void onSaveClicked() {
        String email = _view.getEmail();
        String firstName = _view.getFirstName();
        String lastName = _view.getLastName();

        UserEntity userEntity = _view.getUserEntity();

        boolean isError = false;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (email.isEmpty()) {
                _view.setErrorEmail(R.string.empty_input);
            }
            else {
                _view.setErrorEmail(R.string.invalid_email);
            }
            isError = true;
        }
        if (firstName.isEmpty()) {
            _view.setErrorFirstName(R.string.empty_input);
            isError = true;
        }
        if (lastName.isEmpty()) {
            _view.setErrorLastName(R.string.empty_input);
            isError = true;
        }

        MaterialEditText materialEditTextEmail = _view.getMaterialEditTextEmail();

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

    public void checkEmail() {
        String email = _view.getEmail();

        UserEntity userEntity = _view.getUserEntity();
        if (email != null && !email.isEmpty() && userEntity != null && !Objects.equals(userEntity.get_email(), email)) {
            this._profileService.checkEmail(email);
        }
    }
}
