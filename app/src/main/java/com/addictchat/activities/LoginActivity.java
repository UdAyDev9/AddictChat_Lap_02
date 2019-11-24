package com.addictchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addictchat.R;
import com.addictchat.utils.CountryData;
import com.addictchat.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
/*
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
*/

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatSpinner spinner;
    private AppCompatEditText editText;
    private AppCompatButton btnContinue;
    private RelativeLayout relativeLayout;
    private ImageView imgLogo;
    private AppCompatTextView countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        /*spinner = (AppCompatSpinner) findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout, CountryData.countryNames));*/

        countryCode = (AppCompatTextView) findViewById(R.id.txt_country_code);

        editText = (AppCompatEditText) findViewById(R.id.editTextPhone);
        btnContinue = (AppCompatButton) findViewById(R.id.buttonContinue);

        btnContinue.setOnClickListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                    doLogin();
                }
                return false;
            }
        });

        if (!Utils.haveNetworkConnection(LoginActivity.this)) {
            Utils.showToast(LoginActivity.this, getResources().getString(R.string.netConnection));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonContinue:
                if (!Utils.haveNetworkConnection(LoginActivity.this)) {
                    Utils.showToast(LoginActivity.this,getResources().getString(R.string.netConnection));
                } else {
                    Utils.hideKeyBord(view, LoginActivity.this);
                    doLogin();
                }

                break;
            default:
                break;
        }
    }

    private void doLogin() {
        //String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

        String code = countryCode.getText().toString().trim();

        Log.e("countryCode--->",""+code);

        String number = editText.getText().toString().trim();

        if (number.isEmpty() || number.length() < 10) {
            editText.setError("Valid number is required");
            editText.requestFocus();
            return;
        }

        String phoneNumber =  code + number;

        Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("phonenumber", phoneNumber);
        startActivity(intent);
    }
}
