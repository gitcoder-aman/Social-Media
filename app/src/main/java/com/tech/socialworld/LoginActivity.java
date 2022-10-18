package com.tech.socialworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tech.socialworld.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging...");

//        auth.signOut();
        binding.LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Objects.requireNonNull(binding.emailET.getText()).toString().trim();
                String pass = Objects.requireNonNull(binding.passwordET.getText()).toString().trim();

                if (TextUtils.isEmpty(email)) {
                    binding.emailET.setError("*");
                    return;
                } else {
                    binding.emailET.setError(null);
                    binding.emailET.clearFocus();
                }

                if (TextUtils.isEmpty(pass)) {
                    binding.passwordET.setError("*");
                    return;
                } else {
                    binding.passwordET.setError(null);
                    binding.passwordET.clearFocus();
                }
                dialog.show();
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                            Toast.makeText(LoginActivity.this, "Successful Login.", Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.goToSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,SignupActivity.class)));
    }

    @Override
    protected void onStart() {   //already login then do not enter the Login Layout
        super.onStart();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }
}