package com.sitimapps.ateple.ate.ateateple;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.sitimapps.ateple.ate.ateateple.LoginActivity.user_db.user;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private Button SignOut; 
    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private EditText code_id;

    String unicId = "72" +
            Build.BOARD.length()%10 + Build.BRAND.length()%10 +
            Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
            Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
            Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
            Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
            Build.TAGS.length()%10 + Build.TYPE.length()%10 +
            Build.USER.length()%10 + Build.BRAND.length()%10 +
            Build.BRAND.length()%10 + Build.MANUFACTURER.length()%10 +
            Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
            Build.TAGS.length()%10 + Build.TYPE.length()%10 +
            Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
            Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
            Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
            Build.MODEL.length()%10 + Build.BRAND.length()%10 +
            Build.PRODUCT.length()%10 + Build.PRODUCT.length()%10 +
            Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
            Build.USER.length()%10 + Build.BRAND.length()%10 +
            Build.BRAND.length()%10 + Build.MANUFACTURER.length()%10 +
            Build.BOARD.length()%10 + Build.BRAND.length()%10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_login);

        //SignOut = (Button) findViewById(R.id.bn_logout);
        SignIn = (SignInButton) findViewById(R.id.bn_login);
        SignIn.setOnClickListener(this);
        //SignOut.setOnClickListener(this);
        code_id = (EditText) findViewById(R.id.code_id);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bn_login:
                signIn();

                break;
            /*case R.id.bn_logout:
                signOut();
                break;*/
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void signOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });

    }

    private void handlerResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            user.setName(account.getDisplayName());
            user.setEmail(account.getEmail());

            String temp_check_code = code_id.getText().toString();
            String numberOnly= temp_check_code.replaceAll("[^0-9]", "");
            if (numberOnly.length()>0) {
                user.setCode_ref_id(Integer.parseInt(numberOnly));
            }
            user.setToken(unicId);
            if (account.getPhotoUrl()!=null) {
                user.setPhoto(account.getPhotoUrl().toString());
            }
            updateUI(true);
        } else {
            updateUI(false);
        }

    }

    private void updateUI (boolean isLogin) {

        if (isLogin) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {}

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode==REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlerResult(result);
        }

    }

    public interface user_db {
        User user = new User();
    }
}
