package com.applications.myakkount;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.applications.myakkount.Const.ARG_IS_ADDING_NEW_ACCOUNT;
import static com.applications.myakkount.Const.MY_ACCOUNT_TYPE;
import static com.applications.myakkount.Const.MY_AYTH_TOKEN_TYPE;
import static com.applications.myakkount.Const.PARAM_USER_PASS;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
private static final String TAG = AuthenticatorActivity.class.getSimpleName();
    EditText tvusername,tvpassword;
    Button signin;
    AccountManager mAccountManager;
    AkountAuthentikator akountAuthentikator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAccountManager = AccountManager.get(this);
        akountAuthentikator = new AkountAuthentikator(AuthenticatorActivity.this);
        tvpassword = findViewById(R.id.tvpassword);
        tvusername = findViewById(R.id.tvusername);
        signin = findViewById(R.id.signin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    void submit(){
        String username = tvusername.getText().toString().trim();
        String password = tvpassword.getText().toString().trim();
        Log.e(TAG, "AuthentikatorActivity#submit = "+username+" "+password);
        String[] input = {username, password};
        AkkountAsyncTask akkountAsyncTask = new AkkountAsyncTask(username,password);
        akkountAsyncTask.execute(input);

    }

     private class AkkountAsyncTask extends AsyncTask<String, Void, Intent>{
        String username, userpass;
        public AkkountAsyncTask(String username, String password){
            this.username = username;
            this.userpass = password;
        }

         @Override
         protected void onPreExecute() {
             super.onPreExecute();

         }

         @Override
         protected Intent doInBackground(String... strings) {
            username = strings[0];
            userpass = strings[1];
             Log.e(TAG, "AuthentikatorActivity#doInBackground = "+username+" "+userpass);
             String authtoken = userSignIn(username, userpass);
             Log.e(TAG, "authToken = "+authtoken);
             if(authtoken == null){
                 authtoken = "my-temp-authToken";
             }
             final Intent res = new Intent();
             res.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
             res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, MY_ACCOUNT_TYPE);
             res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
             res.putExtra(PARAM_USER_PASS, userpass);
             res.putExtra(ARG_IS_ADDING_NEW_ACCOUNT, true);
             return res;
         }

         @Override
         protected void onPostExecute(Intent intent) {
             finishLogin(intent);
         }
     }

    String userSignIn(String username, String password){
        /**
         *
         * @brief: this is a place to get authToken, eg: make a Retrofit call
         *
         *  */
        Log.e(TAG, "username = "+username+" , password = "+password);
        if(username.equals("fahim.farhan@outlook.com") && password.equals("asdf")){
            return username+"-"+password+"-someTokenStuff"+System.currentTimeMillis();
        }else{
            String temp = password + "@outlook.com";
            if(temp.equals(username)){
                return username+"-"+password+"-someTokenStuff"+System.currentTimeMillis();
            }
        }
        return null;
    }

    private void finishLogin(Intent intent) {
        if(intent!=null){
            Log.e(TAG, "if block, so intent != null");
            final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            final String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
            final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String type = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
            Log.e(TAG, "finishLogin-> "+accountName+" "+accountPassword+" "+authToken+" "+account.toString());


            if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
                // Creating the account on the device and setting the auth token we got
                // (Not setting the auth token will cause another call to the server to authenticate the user)
                akountAuthentikator.createAccount(accountName, accountPassword, authToken, null);
/** /                mAccountManager.addAccountExplicitly(account, accountPassword, null);  // originally ei 2ta chilo
//                mAccountManager.setAuthToken(account, MY_AYTH_TOKEN_TYPE, authToken);  */
            } else {
                mAccountManager.setPassword(account, accountPassword);
            }

            setAccountAuthenticatorResult(intent.getExtras());
            setResult(AccountAuthenticatorActivity.RESULT_OK, intent);
            finish();
        }else{
            Log.e(TAG, "else block, so intent == null");
            setResult(AccountAuthenticatorActivity.RESULT_CANCELED, intent);
            finish();
        }

    }
}
