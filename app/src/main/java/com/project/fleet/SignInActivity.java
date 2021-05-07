package com.project.fleet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.fleet.fileUtils.CommonFunctions;
import com.project.fleet.model.SigninUser;
import com.project.fleet.model.SignupUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    private String TAG = "fleetLog";
    private final Context context = this;
    private CommonFunctions commonFunctions;
    private Boolean isPasswordVisible = false;
    private String userId;

    private DatabaseReference databaseReference;
    private FirebaseDatabase mFirebaseInstance;
    List<SigninUser> usersList = new ArrayList<>();

    //bindview is view binding library which helps to reduce view initialisation codes.
    @BindView(R.id.tv_signin_label)
    TextView tv_signin_label;

    @BindView(R.id.tv_email_label)
    TextView tv_email_label;

    @BindView(R.id.tv_password_label)
    TextView tv_password_label;

    @BindView(R.id.tv_signin_error)
    TextView tv_signin_error;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.iv_password_eye)
    ImageView iv_password_eye;

    @BindView(R.id.rl_twitter)
    RelativeLayout rl_twitter;

    @BindView(R.id.rl_facebook)
    RelativeLayout rl_facebook;

    @BindView(R.id.tv_twitter)
    TextView tv_twiiter;

    @BindView(R.id.tv_facebook)
    TextView tv_facebook;

    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;

    @BindView(R.id.tv_signup)
    TextView tv_signup;

    @BindView(R.id.btn_signin)
    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this); // butterknife initialisation

        //class declaration
        commonFunctions = new CommonFunctions();

        // get reference to 'users' node
        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseInstance.getReference().child("users");

        //retrieving all the email and password from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SignupUser signupUser = postSnapshot.getValue(SignupUser.class);
                    usersList.add(new SigninUser(signupUser.getEmail(), signupUser.getPassword()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //function to show white status and title bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.my_statusbar_color));
        }

        setFonts();  //function showing bold fonts for some texts

        //sign in button click action
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonFunctions.isValidEmail(et_email.getText().toString().trim())) {
                    if (!et_password.getText().toString().isEmpty()) {
                        if (isEmailPasswordValid()) {
                            Toast.makeText(context, "Welcome Back!!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(context, DashboardActivity.class);
                            startActivity(myIntent);
                            finish();
                        } else {
                            tv_signin_error.setVisibility(View.VISIBLE);
                        }
                    } else {
                        et_password.requestFocus();
                        et_password.setError("Invalid Password");
                    }
                } else {
                    et_email.requestFocus();
                    et_email.setError("Invalid Email");
                }

            }
        });

        //sign up button click action
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, SignUpActivity.class);
                startActivity(myIntent);
            }
        });

        //below codes provide hide/show password feature to user
        iv_password_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                    iv_password_eye.setImageResource(R.drawable.ic_eye_on);
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                    iv_password_eye.setImageResource(R.drawable.ic_eye_off);
                }
                et_password.setSelection(et_password.getText().toString().trim().length());
            }
        });
    }

    //this function will match the email and password with database.
    private boolean isEmailPasswordValid() {
        boolean isContain = false;
        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getEmail().equalsIgnoreCase(et_email.getText().toString().trim())) {
                if (usersList.get(i).getPassword().equals(et_password.getText().toString())) {
                    isContain = true;
                }
            }
        }

        return isContain;
    }

    //function to set custom bold fonts for some texts.
    private void setFonts() {
        tv_signin_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_email_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_password_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_twiiter.setTypeface(Typeface.DEFAULT_BOLD);
        tv_facebook.setTypeface(Typeface.DEFAULT_BOLD);
        tv_signup.setTypeface(Typeface.DEFAULT_BOLD);
        tv_forgot_password.setTypeface(Typeface.DEFAULT_BOLD);
        btn_signin.setTypeface(Typeface.DEFAULT_BOLD);
    }

}