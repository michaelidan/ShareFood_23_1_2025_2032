package com.example.sharedfood;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class PersonalAreaActivity extends AppCompatActivity {

    private TextView userDetailsTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);

        userDetailsTextView = findViewById(R.id.userDetailsTextView);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // קבלת פרטי המשתמש והצגת כמות הפוסטים
        displayUserDetails();
    }

    private void displayUserDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // הצגת אימייל
            String email = "Email: " + user.getEmail();

            // אחזור כמות הפוסטים מהקולקציה
            db.collection("foodPosts")
                    .whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            int postCount = querySnapshot != null ? querySnapshot.size() : 0;

                            // הצגת כמות הפוסטים
                            String postInfo = "Number of posts: " + postCount;
                            userDetailsTextView.setText(email + "\n" + postInfo);
                        } else {
                            userDetailsTextView.setText(email + "\nFailed to load post count.");
                        }
                    });
        } else {
            userDetailsTextView.setText("No user is logged in.");
        }
    }
}
