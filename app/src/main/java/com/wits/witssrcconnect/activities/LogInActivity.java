package com.wits.witssrcconnect.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.animation.AnticipateInterpolator;

import com.wits.witssrcconnect.R;
import com.wits.witssrcconnect.managers.UserManager;
import com.wits.witssrcconnect.utils.ServerUtils;
import com.wits.witssrcconnect.utils.UserUtils;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    public ConstraintLayout cc2 = null;
    public int user = UserUtils.DEFAULT_USER;
    public TextInputEditText username;
    public TextInputEditText password;
    String link = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserManager.initUserManager(this);

        //check if user has already logged in
        if (UserManager.getCurrentlyLoggedInStatus()) {
            //open next activity based on which user is already logged in
            if (UserManager.getLoggedInUserType() == UserUtils.STUDENT){
                startActivity(new Intent(this, StudentActivity.class));
            }
            else{
                startActivity(new Intent(this, SrcMemberActivity.class));
            }
            finish();
            return;
        }

        setContentView(R.layout.activity_log_in_user_selector);
        cc2 = findViewById(R.id.cc2);

        username = findViewById(R.id.username);
        password = findViewById(R.id.pass);

        findViewById(R.id.appCompatImageView2).setOnClickListener(v -> {
            user = UserUtils.STUDENT;
            resetInputs();
            animateView(R.layout.activity_log_in_student);
        });

        findViewById(R.id.member_icon_holder).setOnClickListener(v -> {
            user = UserUtils.SRC_MEMBER;
            resetInputs();
            animateView(R.layout.activity_log_in_src_member);
        });

        findViewById(R.id.log_in).setOnClickListener(v -> {
            String sUsername = Objects.requireNonNull(username.getText()).toString().trim().toLowerCase();
            String sPassword = Objects.requireNonNull(password.getText()).toString().trim();

            if (TextUtils.isEmpty(sUsername)) {
                username.setError("Enter username");
            }

            else if (TextUtils.isEmpty(sPassword)) {
                password.setError("Enter password");
            }

            else {
                ContentValues cv = new ContentValues();
                if (user == UserUtils.STUDENT) {
                    cv.put(ServerUtils.USERNAME, sUsername);
                    cv.put(ServerUtils.PASSWORD, sPassword);
                    link = ServerUtils.LOG_IN_LINK;
                } else {
                    cv.put(ServerUtils.ACTION, ServerUtils.LOG_IN);
                    cv.put(ServerUtils.SRC_USERNAME, sUsername);
                    cv.put(ServerUtils.SRC_PASSWORD, sPassword);
                    link = ServerUtils.SRC_MEMBER_LINK;
                }
                UserManager.logIn(user, cv, link, LogInActivity.this);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (user == UserUtils.DEFAULT_USER) super.onBackPressed();
        else {
            user = UserUtils.DEFAULT_USER;
            revertAnimation();
        }
    }

    private void animateView(int id) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, id);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateInterpolator(1.0f));
        transition.setDuration(1200);

        TransitionManager.beginDelayedTransition(cc2, transition);
        constraintSet.applyTo(cc2);
    }

    public void resetInputs() {
        username.setText("");
        password.setText("");
        username.clearFocus();
        password.clearFocus();
    }

    private void revertAnimation() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_log_in_user_selector);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateInterpolator(1.0f));
        transition.setDuration(1200);

        TransitionManager.beginDelayedTransition(cc2, transition);
        constraintSet.applyTo(cc2);
    }
}
