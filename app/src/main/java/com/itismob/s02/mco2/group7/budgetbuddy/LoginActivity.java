
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEt, passwordEt;
    private MaterialButton loginBtn, googleBtn;
    private TextView signUpTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        googleBtn = findViewById(R.id.googleBtn);
        signUpTv = findViewById(R.id.signUpTv);

        emailEt.setText(PrefsHelper.getEmail(this));

        loginBtn.setOnClickListener(v -> login());

        googleBtn.setOnClickListener(v ->
                Toast.makeText(this,
                        "Google Sign-In is unavailable",
                        Toast.LENGTH_SHORT).show()
        );

        signUpTv.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void login() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEt.setError("Enter your password");
            return;
        }

        // Save email locally for UI demonstration
        PrefsHelper.saveEmail(this, email);

        // Bypass authentication and proceed to dashboard
        startActivity(new Intent(this, MainActivity.class));
    }
}
