package com.example.aalap.aalapsreddit.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.aalap.aalapsreddit.Activities.LoginActivity;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;

/**
 * Created by Aalap on 2017-10-12.
 */

public class DialogUtils {

    RedditStore redditStore;
    Context context;
    public DialogUtils(Context context) {
        redditStore = new RedditStore(context);
        this.context = context;
    }

    public void openCommentDialog(Activity activity, ViewGroup dialogContainer, String id, AlertDialog.Builder dialoBuilder) {
        AlertDialog alertDialog;
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.post_comment_dialog, dialogContainer, false);
        EditText comment = dialogView.findViewById(R.id.comment);
        Button cancel = dialogView.findViewById(R.id.button_cancel_comment);
        Button post = dialogView.findViewById(R.id.button_post_comment);
        alertDialog = dialoBuilder.create();
        alertDialog.setView(dialogView);
        alertDialog.show();

        cancel.setOnClickListener(v -> alertDialog.dismiss());
        post.setOnClickListener(v -> {
            String commentValue = comment.getText().toString().trim();
            redditStore.postComment(id, commentValue);
            alertDialog.dismiss();
        });
    }

    public void openLoginDialog(AlertDialog.Builder dialoBuilder, ViewGroup dialogContainer) {
        AlertDialog alertDialog;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_login, dialogContainer, false);
        alertDialog = dialoBuilder.create();
        EditText userName = dialogView.findViewById(R.id.email);
        EditText password = dialogView.findViewById(R.id.password);
        TextView noThanks = dialogView.findViewById(R.id.as_guest);
        noThanks.setText("No thanks");
        noThanks.setOnClickListener(v->alertDialog.dismiss());
        Button login = dialogView.findViewById(R.id.email_sign_in_button);
        login.setOnClickListener(v -> {
            LoginActivity.attemptLogin(context, userName, password, redditStore);
            alertDialog.dismiss();
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}
