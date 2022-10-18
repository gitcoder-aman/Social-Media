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
import com.google.firebase.database.FirebaseDatabase;
import com.tech.socialworld.Model.UserModel;
import com.tech.socialworld.databinding.ActivitySignupBinding;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Create an account...");

        binding.SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = Objects.requireNonNull(binding.nameET.getText()).toString().trim();
                String profession = Objects.requireNonNull(binding.professionET.getText()).toString().trim();
                String email = Objects.requireNonNull(binding.emailET.getText()).toString().trim();
                String pass = Objects.requireNonNull(binding.passwordET.getText()).toString().trim();

                if (TextUtils.isEmpty(name)) {
                    binding.nameET.setError("*");
                    return;
                } else {
                    binding.nameET.setError(null);
                    binding.nameET.clearFocus();
                }

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
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            UserModel user = new UserModel(name, profession, email, pass);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(SignupActivity.this, "Create an account.", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}