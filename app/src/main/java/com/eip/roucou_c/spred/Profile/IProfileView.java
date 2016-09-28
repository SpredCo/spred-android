package com.eip.roucou_c.spred.Profile;

import com.eip.roucou_c.spred.Entities.UserEntity;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by roucou_c on 27/09/2016.
 */
public interface IProfileView {
    void populateProfile(UserEntity userEntity);

    String getEmail();

    String getFirstName();

    String getLastName();

    void setErrorEmail(int resId);

    void setErrorFirstName(int resId);

    void setErrorLastName(int resId);

    MaterialEditText getMaterialEditTextEmail();

    UserEntity getUserEntity();

    void setError(int resId);

    void changeStep(String step);
}
