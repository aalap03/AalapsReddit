package com.example.aalap.aalapsreddit.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Utils.Preference;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    // UI references.
    private AutoCompleteTextView userNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    TextView asGuest;
    RedditStore store;
    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userNameView = findViewById(R.id.email);
        asGuest = findViewById(R.id.as_guest);
        store = new RedditStore(this);
        preference = new Preference(getApplicationContext());
        asGuest.setOnClickListener(v -> {
            preference.setCookie("");
            preference.setModHash("");
            startActivity(new Intent(this, FeedsActivity.class));
        });
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin(LoginActivity.this, userNameView, mPasswordView, store);
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> attemptLogin(LoginActivity.this, userNameView, mPasswordView, store));
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public static void attemptLogin(Context context, EditText userNameView, EditText mPasswordView, RedditStore store) {
        String email = userNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        if(email!=null && !email.isEmpty() && password!=null && !password.isEmpty()){
            store.login(header, email, password);
        }else
            Toasty.warning(context, "Please fill both values").show();
    }
}

