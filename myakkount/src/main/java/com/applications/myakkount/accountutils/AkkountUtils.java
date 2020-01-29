package com.applications.myakkount.accountutils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.applications.myakkount.Const;

public class AkkountUtils {
    private static final String TAG = AkkountUtils.class.getSimpleName();

    public AkkountUtils(){}

    public static Account getAccount(Context context, String accountName) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(Const.MY_ACCOUNT_TYPE);
        for (Account account : accounts) {
            Log.e(TAG, ""+ account);
            if (account.name.equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }
}
