package com.eip.roucou_c.spred.Api;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eip.roucou_c.spred.ISignInSignUpView;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;


/**
 * Created by roucou_c on 18/06/2016.
 */
public class ApiGoogle extends AsyncTask {

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.me";

    private final Context _context;
    private final ISignInSignUpView _view;

    String mEmail;
    private String _token;

    public ApiGoogle(Context context, ISignInSignUpView view) {
        _context = context;
        _view = view;
    }

    public void launch() {
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try{
            _token = fetchToken();
        }catch(IOException e){
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(_token != null){
            Log.d("token google", _token);
            this._view.onGoogleClicked(_token);
        }
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken()throws IOException{
        try{

            AppCompatActivity activity = _view.getActivity();

            return GoogleAuthUtil.getToken(activity, mEmail, SCOPE);
        }catch(UserRecoverableAuthException userRecoverableException){
            _view.startActivityForResult(userRecoverableException.getIntent(), ApiLogin.REQUEST_AUTHORIZATION);
        // GooglePlayServices.apk is either old, disabled, or not present
        // so we need to show the user some UI in the activity to recover.
//            mActivity.handleException(userRecoverableException);
        }catch(GoogleAuthException fatalException){
        // Some other type of unrecoverable exception has occurred.
        // Report and log the error as appropriate for your app.
        }
        return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            // With the account name acquired, go get the auth token
            getToken();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // The account picker dialog closed without selecting an account.
            // Notify users that they must pick an account to proceed.
//                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
        }
    }

    public void getToken() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            this.execute();
        }
    }

    public void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        _view.startActivityForResult(intent, ApiLogin.REQUEST_CODE_GOOGLE);
    }

}
