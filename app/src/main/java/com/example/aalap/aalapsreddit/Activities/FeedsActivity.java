package com.example.aalap.aalapsreddit.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.Feed;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.RedditApp;
import com.example.aalap.aalapsreddit.Service.RedditService;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class FeedsActivity extends AppCompatActivity {

    private static final String TAG = "FeedsActivity";
    RecyclerView recyclerView;
    String feed;
    EditText feedName;

    Button load;
    public static final String BASE_URL = "https://www.reddit.com/";//"funny/.rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_screen);

        recyclerView = findViewById(R.id.recyclerview);
        feedName = findViewById(R.id.feed_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        load = findViewById(R.id.load_button);
        load.setOnClickListener(v -> loadFeed());
    }

    private void loadFeed() {
        RedditApp.getRetrofit().getFeeds(feedName.getText().toString()).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                if (response.isSuccessful()) {
                    Feed body = response.body();
                    List<Entry> entry = body.getEntry();

                    for (Entry entry1 : entry) {
                        Log.d(TAG, "content: " + entry1.getContent());
                        String[] split = entry1.getContent().split("<img src=\"");
                        if (split != null) {
                            if (split.length > 1) {
                                String[] split1 = split[1].split("\"");
                                entry1.setImageLink(split1[0]);
                                Log.d(TAG, "onResponse: set link");
                            } else
                                entry1.setImageLink("");
                        } else {
                            entry1.setImageLink("");
                        }
                    }
                    FeedAdapter adapter = new FeedAdapter(entry, FeedsActivity.this);
                    runOnUiThread(() -> recyclerView.setAdapter(adapter));

                } else
                    try {
                        Log.d(TAG, "onResponse: err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    //make pattern required
    public static String getMatch(String patternString, String text, int groupIndex) {
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return getMatch(pattern, text, groupIndex);
    }

    //use that pattern to extract the src value
    public static String getMatch(Pattern pattern, String text, int groupIndex) {
        if (text != null) {
            Matcher matcher = pattern.matcher(text);
            String match = null;
            while (matcher.find()) {
                match = matcher.group(groupIndex);
                break;
            }
            return match;
        } else {
            return null;
        }
    }

}
