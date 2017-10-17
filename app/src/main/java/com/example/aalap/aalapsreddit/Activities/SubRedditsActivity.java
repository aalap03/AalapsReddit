package com.example.aalap.aalapsreddit.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.aalap.aalapsreddit.Adapter.SubredditGridAdapter;
import com.example.aalap.aalapsreddit.R;
import java.util.ArrayList;

public class SubRedditsActivity extends AppCompatActivity {

    private static final String TAG = "SubRedditsActivity";

    ViewGroup rootView;
    ArrayList<String> subReddits = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_reddits);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rootView = findViewById(R.id.root_view);
        recyclerView = findViewById(R.id.gridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        Intent intent = getIntent();
        if (intent != null) {
            subReddits = intent.getStringArrayListExtra("cat");
        }
        SubredditGridAdapter adapter = new SubredditGridAdapter(this, subReddits);
        recyclerView.setAdapter(adapter);
    }
}
