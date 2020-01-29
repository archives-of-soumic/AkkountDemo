package com.applications.myapp1;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;

import com.applications.myakkount.accountutils.AkkountUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static com.applications.myakkount.Const.ARG_IS_ADDING_NEW_ACCOUNT;
import static com.applications.myakkount.Const.PARAM_USER_PASS;

public class MainActivity extends AppCompatActivity {
    TextView mytextview;
    private static final int SOME_REQUEST_CODE = 123;
    Intent intent;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mytextview = findViewById(R.id.mytextview);
        Account currentAkount = AkkountUtils.getAccount(MainActivity.this, "fahim.farhan@outlook.com");

        if(currentAkount!=null){
            String name = ""+currentAkount.name;
            mytextview.setText(name);
        }


        intent = new Intent(MainActivity.this, com.applications.myakkount.AuthenticatorActivity.class);
        intent.putExtra(ARG_IS_ADDING_NEW_ACCOUNT,true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(intent, SOME_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SOME_REQUEST_CODE ){
            Log.e(TAG, "onActivityResult#requestCode == SOME_REQUEST_CODE");
            if(resultCode == RESULT_OK){
                Log.e(TAG, "onActivityResult#resultCode == RESULT_OK");
                if(data != null){
                    String temp = ""+ data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)+" "+data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)+" "
                            +data.getStringExtra(AccountManager.KEY_AUTHTOKEN)+" "+data.getStringExtra(PARAM_USER_PASS);
                    mytextview.setText(temp);
                    Log.e(TAG, temp);
                }else{
                    Log.e(TAG, "onActivityResult data == null I guess");
                }
            }else{
                Log.e(TAG, "onActivityResult#resultCode != RESULT_OK");

            }

        }else{
            Log.e(TAG, "onActivityResult#requestCode != SOME_REQUEST_CODE");
            Log.e(TAG, "Sth went wrong!");
        }
    }
}
