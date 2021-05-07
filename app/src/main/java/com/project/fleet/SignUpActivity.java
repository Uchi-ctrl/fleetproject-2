package com.project.fleet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.fleet.model.SignupUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "fleetLog";
    private final Context context = this;
    private DatabaseReference databaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    List<String> about = new ArrayList<>();

    @BindView(R.id.tv_signup_label)
    TextView tv_signup_label;
    @BindView(R.id.tv_company_label)
    TextView tv_company_label;
    @BindView(R.id.tv_about_label)
    TextView tv_about_label;
    @BindView(R.id.tv_phone_label)
    TextView tv_phone_label;
    @BindView(R.id.tv_email_label)
    TextView tv_email_label;
    @BindView(R.id.tv_password_label)
    TextView tv_password_label;
    @BindView(R.id.tv_signin)
    TextView tv_signin;
    @BindView(R.id.cb_terms)
    TextView cb_terms;
    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.et_company)
    EditText et_company;
    @BindView(R.id.et_about)
    EditText et_about;
    @BindView(R.id.spnr_about)
    Spinner spnr_about;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseInstance.getReference("users");

        setFonts();

        //adding items for about.
        about.add("Manager");
        about.add("Employee");

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

        //signin button click action
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spnr_about.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, about);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnr_about.setAdapter(dataAdapter);

        et_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnr_about.performClick();

            }
        });

        //signup button click action
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_company.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(et_about.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                            if (!TextUtils.isEmpty(et_email.getText().toString().trim())) {
                                if (!TextUtils.isEmpty(et_password.getText().toString().trim())) {


                                        if (TextUtils.isEmpty(userId)) {
                                            // Creating new user node, which returns the unique key value
                                            // new user node would be /users/$userid/
                                            userId = databaseReference.push().getKey();
                                        }

                                        // creating user object
                                        SignupUser user = new SignupUser(et_company.getText().toString().trim(),
                                                et_about.getText().toString().trim(), et_phone.getText().toString().trim(),
                                                et_email.getText().toString().trim(),
                                                et_password.getText().toString());
                                        // pushing user to 'users' node using the userId
                                        databaseReference.child(userId).setValue(user);

                                        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                SignupUser signupUser = snapshot.getValue(SignupUser.class);
                                                // Check for null
                                                if (signupUser == null) {
                                                    Log.e(TAG, "User data is null!");
                                                    return;
                                                }

                                                Toast.makeText(context, "You are registered!!", Toast.LENGTH_SHORT).show();

                                                Intent myIntent = new Intent(context, DashboardActivity.class);
                                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(myIntent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.e(TAG, "onCancelled: " + error.getMessage());
                                            }
                                        });


                                } else {
                                    et_password.requestFocus();
                                    et_password.setError("Invalid Password");
                                }
                            } else {
                                et_password.requestFocus();
                                et_password.setError("Invalid Email");
                            }
                        } else {
                            et_password.requestFocus();
                            et_password.setError("Invalid Phone Number");
                        }
                    } else {
                        et_password.requestFocus();
                        et_password.setError("Invalid About");
                    }
                } else {
                    et_password.requestFocus();
                    et_password.setError("Invalid Company Name");
                }
            }
        });
    }

    //function to set custom bold fonts for some texts.
    private void setFonts() {
        tv_signup_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_about_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_company_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_email_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_password_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_phone_label.setTypeface(Typeface.DEFAULT_BOLD);
        btn_signup.setTypeface(Typeface.DEFAULT_BOLD);
        tv_signin.setTypeface(Typeface.DEFAULT_BOLD);
        cb_terms.setTypeface(Typeface.DEFAULT_BOLD);

        String text = "<font color=#57595A>I agree to the </font><font color=#F95F69>" +
                "Terms of Services </font><font color=#57595A>and </font><font color=#F95F69>Privacy Policy</font>" +
                "<font color=#57595A>. </font>";
        cb_terms.setText(Html.fromHtml(text));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        et_about.setText(about.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}