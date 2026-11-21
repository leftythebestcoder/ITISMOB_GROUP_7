
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameEt, emailEt, passwordEt;
    private MaterialButton signUpBtn, googleBtn;
    private TextView backTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        signUpBtn = findViewById(R.id.signUpBtn);
        googleBtn = findViewById(R.id.googleBtn);
        backTv = findViewById(R.id.backTv);

        backTv.setOnClickListener(v -> finish());

        signUpBtn.setOnClickListener(v -> register());

        googleBtn.setOnClickListener(v ->
                Toast.makeText(this,
                        "Google Sign-In is unavailable in UI-only mode",
                        Toast.LENGTH_SHORT).show()
        );
    }

    private void register() {
        String name = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameEt.setError("Enter your full name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Enter your email");
            return;
        }

        if (password.length() < 6) {
            passwordEt.setError("Password must be at least 6 characters");
            return;
        }

        // Save user details locally for UI demonstration
        PrefsHelper.saveName(this, name);
        PrefsHelper.saveEmail(this, email);

        Toast.makeText(this,
                "Registration successful!",
                Toast.LENGTH_SHORT).show();

        goToMain();
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
