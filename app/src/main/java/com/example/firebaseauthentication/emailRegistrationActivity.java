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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class emailRegistrationActivity extends AppCompatActivity {

    EditText email,password,name;
    ImageView back;
    Button registration;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);
        back=findViewById(R.id.back);
        registration=findViewById(R.id.register);
        login=findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(emailRegistrationActivity.this,loginActivity.class));

            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(emailRegistrationActivity.this, "Plese enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else if ( !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(emailRegistrationActivity.this, "Plese enter valid Email Address", Toast.LENGTH_SHORT).show();

                }
                else  if(TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(emailRegistrationActivity.this, "Plese enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length()<6){
                    Toast.makeText(emailRegistrationActivity.this, "Plese enter Password min 6 dig", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(emailRegistrationActivity.this, "Plese enter Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    registration();
                }
            }

            private void registration() {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Map<String,Object>map =new HashMap<>();
                                map.put("Email",email.getText().toString());
                                map.put("Password",password.getText().toString());
                                map.put("Name",name.getText().toString());

                                FirebaseDatabase.getInstance().getReference()
                                        .child("Users")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(emailRegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(emailRegistrationActivity.this,DashboardActivity.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(emailRegistrationActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                ;

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(emailRegistrationActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
