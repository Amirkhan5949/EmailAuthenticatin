package com.example.firebaseauthentication.phoneauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebaseauthentication.R;
import com.hbb20.CountryCodePicker;

public class loginActivity extends AppCompatActivity {
    EditText number;
    Button send;
    ImageView back;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        number = findViewById(R.id.number);
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);
        countryCodePicker = findViewById(R.id.ccp);

        countryCodePicker.registerCarrierNumberEditText(number);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, phoneValidation.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(number.getText().toString())) {
                    Toast.makeText(loginActivity.this, "Plese enter no", Toast.LENGTH_SHORT).show();
                } else if (number.getText().toString().replace(" ","").length() != 10) {
                    Toast.makeText(loginActivity.this, "Plese enter number min 10 dig", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(loginActivity.this, phoneValidation.class);
                    intent.putExtra("number", countryCodePicker.getFullNumberWithPlus().replace("", ""));
                    startActivity(intent);
                    finish();
                }
            }


        });
    }
}
