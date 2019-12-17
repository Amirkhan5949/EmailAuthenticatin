package com.example.firebaseauthentication.phoneauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseauthentication.DashboardActivity;
import com.example.firebaseauthentication.Loader;
import com.example.firebaseauthentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneValidation extends AppCompatActivity {

    EditText otp;
    ImageView back;
    Button next;
    String number,id;
    TextView resend;
    private FirebaseAuth mAuth;
    private Loader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validation);

        otp=findViewById(R.id.otp);
        back=findViewById(R.id.back);
        next=findViewById(R.id.next);
        resend=findViewById(R.id.resend);

        mAuth=FirebaseAuth.getInstance();
        loader = new Loader(this);

        number=getIntent().getStringExtra("number");
        sendVerificationCode();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(otp.getText().toString().replace(" ",""))){
                    Toast.makeText(phoneValidation.this, "please type otp", Toast.LENGTH_SHORT).show();
                }
                else if (otp.getText().toString().replace("","").length()!=6){
                    Toast.makeText(phoneValidation.this, "enter right otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    loader.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp.getText().toString().replace(" ",""));
                   signInWithPhoneAuthCredential(credential);
                }
            }


        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });
    }

    private void sendVerificationCode() {
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long l) {
                resend.setText(""+l/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);

            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        phoneValidation.this.id=id;
                     }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        loader.dismiss();
                        Toast.makeText(phoneValidation.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loader.dismiss();
                            startActivity(new Intent(phoneValidation.this, DashboardActivity.class));
                            finish();
                            // ...
                        } else {
                            loader.dismiss();
                            Toast.makeText(phoneValidation.this, "verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
