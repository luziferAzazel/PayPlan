package com.paymentview.payplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class check_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener au;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_);
        System.out.println("Kommer vi hit? check");
        mAuth = FirebaseAuth.getInstance();
        System.out.println("check user " + mAuth.getCurrentUser());

        if(mAuth.getCurrentUser() == null) {
            startActivity(new Intent(check_Activity.this,LoginActivity.class));
            System.out.println("Jeg er her, den er lik null");
        }
        else if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(check_Activity.this, MainActivity.class));
            System.out.println("Jeg er her");
        }
        System.out.println("Kommer vi hit?");
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
