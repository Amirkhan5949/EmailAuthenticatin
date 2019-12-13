package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
 EditText email,password;
 Button login;
 TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this,emailRegistrationActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(TextUtils.isEmpty(email.getText().toString())){
                        Toast.makeText(loginActivity.this, "Plese enter Email Address", Toast.LENGTH_SHORT).show();
                    }
                    else if ( !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                        Toast.makeText(loginActivity.this, "Plese enter valid Email Address", Toast.LENGTH_SHORT).show();

                    }
                    else  if(TextUtils.isEmpty(password.getText().toString())){
                        Toast.makeText(loginActivity.this, "Plese enter Password", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().length()<6){
                        Toast.makeText(loginActivity.this, "Plese enter Password min 6 dig", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        login();
                    }
            }

            private void login() {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(loginActivity.this, "Login Successful.... ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this,DashboardActivity.class));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginActivity.this, "  failed.... "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }
}
