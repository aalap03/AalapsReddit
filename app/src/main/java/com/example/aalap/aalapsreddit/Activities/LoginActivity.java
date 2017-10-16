package com.example.aalap.aalapsreddit.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.aalap.aalapsreddit.Models.Categories;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Service.RedditService;
import com.example.aalap.aalapsreddit.Utils.Preference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private AutoCompleteTextView userNameView;
    private EditText mPasswordView;
    TextView asGuest;
    RedditStore store;
    Preference preference;
    ArrayList<String> feedCategories = new ArrayList<>();

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
            Intent intent = new Intent(this, SubRedditsActivity.class);
            intent.putStringArrayListExtra("cat", feedCategories);
            startActivity(intent);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.rss2json.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        RedditService redditService = retrofit.create(RedditService.class);
        redditService.getSubRedditsm()
                .flatMap(result->{
                    if(result.isSuccessful()){
                        List<Categories> categories = result.body().getCategories();
                        for (Categories category : categories) {
                            feedCategories.add(category.getCategories().get(0));
                        }
                    }else{
                        Log.d(TAG, "onCreate: err "+result.errorBody().string());
                    }
                    return Observable.empty();
                })
                .doOnError(throwable -> Log.d(TAG, "onCreate: "+throwable.getMessage()))
                .doOnComplete(() -> Log.d(TAG, "onCreate: "))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res->Toasty.success(this, "Feeds loaded").show(), throwable -> Log.d(TAG, "onCreate: suberr "+throwable.getMessage()));
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

