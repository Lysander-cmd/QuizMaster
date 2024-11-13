package com.example.quizmaster.Database;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataPengguna {
    private FirebaseAuth auth;
    private DatabaseReference db;

    public DataPengguna() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void saveUserData(String userId, String username, String noTelp, String role) {
        db.child("users").child(userId).child("username").setValue(username);
        db.child("users").child(userId).child("noTelp").setValue(noTelp);
        db.child("users").child(userId).child("role").setValue(role);
    }

    public void readUsername(String userId, TextView textView) {
        DatabaseReference userRef = db.child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    textView.setText(username != null ? username : "NULL");
                } else {
                    textView.setText("NULL");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textView.setText("went wrong");
            }
        });
    }
    public void readPhoneNumber(String userId, TextView textView) {
        DatabaseReference userRef = db.child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String noTelp = dataSnapshot.child("noTelp").getValue(String.class);
                    textView.setText(noTelp != null ? noTelp : "NULL");
                } else {
                    textView.setText("NULL");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textView.setText("went wrong");
            }
        });
    }
}