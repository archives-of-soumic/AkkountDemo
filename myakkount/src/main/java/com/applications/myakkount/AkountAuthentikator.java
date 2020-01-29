package com.applications.myakkount;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AkountAuthentikator extends AbstractAccountAuthenticator {
    private Context mContext;
    private AccountManager am;
    public AkountAuthentikator(Context context) {
        super(context);
        this.mContext = context;
        this.am = AccountManager.get(this.mContext);
    }

    /**
     * @brief: use this to create account. If you see your account in Accounts & Settings, it worked
     * */
    public void createAccount(String email, String password, String authToken, Bundle userData) {
        Account account = new Account(email, Const.MY_ACCOUNT_TYPE);

        if(am == null){
           am = AccountManager.get(this.mContext);
        }
        am.addAccountExplicitly(account, password, userData);
        am.setAuthToken(account, Const.MY_AYTH_TOKEN_TYPE, authToken);
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(Const.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(Const.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(Const.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);
/**
 * @brief: this is optional. Use retrofit calls to get AuthToken from the server
 *
 * */
        // Lets give another try to authenticate the user
//        if (null != authToken) {
//            if (authToken.isEmpty()) {
//                final String password = am.getPassword(account);
//                if (password != null) {
//                    authToken = AccountUtils.mServerAuthenticator.signIn(account.name, password);
//
//                }
//            }
//        }

        // If we get an authToken - we return it
        if (null != authToken) {
            if (!authToken.isEmpty()) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                return result;
            }
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(Const.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(Const.ARG_AUTH_TOKEN_TYPE, authTokenType);

        // This is for the case multiple accounts are stored on the device
        // and the AccountPicker dialog chooses an account without auth token.
        // We can pass out the account name chosen to the user of write it
        // again in the Login activity intent returned.
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
