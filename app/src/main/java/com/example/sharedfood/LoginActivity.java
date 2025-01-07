package com.example.sharedfood;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// my import addition to check if the user is an admin, michael %%%
import java.util.List;
import java.util.Arrays;
// end of my import addition, michael %%%

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            // my addition to check if the user is an admin, michael %%%
                            checkIfUserIsAdmin(user); // בדיקה אם המשתמש הוא מנהל
                            // end of my addition, michael %%%
                            // HERE PUT LIST OF ADMINS EMAILS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            if (user != null && user.getEmail() != null) {
                                List<String> adminEmails = Arrays.asList("mici9578@gmail.com", "admin@example.com", "secondAdmin@example.com");
                                if (adminEmails.contains(user.getEmail().trim())) {
                                    Toast.makeText(LoginActivity.this, "ברוך הבא, מנהל!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // my addition - added the checkIfUserIsAdmin function, michael %%%
    private void checkIfUserIsAdmin(FirebaseUser user) {
        List<String> adminEmails = Arrays.asList("admin@example.com", "secondAdmin@example.com");
        if (user != null && user.getEmail() != null && adminEmails.contains(user.getEmail().trim())) {
            Toast.makeText(LoginActivity.this, "ברוך הבא, מנהל!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }
    // end of my addition - added the checkIfUserIsAdmin function, michael %%%
}
