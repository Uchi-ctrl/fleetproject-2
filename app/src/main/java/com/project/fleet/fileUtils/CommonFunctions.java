package com.project.fleet.fileUtils;

import android.text.TextUtils;
import android.util.Patterns;

/*this class contains every common functions which are going to need almost every
pages. so instead of declaring them in individual classes it's better to just call them */
public class CommonFunctions {

    //function for validating email, entered by user
    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
