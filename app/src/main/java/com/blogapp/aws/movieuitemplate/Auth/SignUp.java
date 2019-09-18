package com.blogapp.aws.movieuitemplate.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blogapp.aws.movieuitemplate.R;
import com.blogapp.aws.movieuitemplate.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText email,password,confirmPass;
    Button signUp;
    RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("SignUp form");
        email = (EditText) findViewById(R.id.mEmail);
        password = (EditText) findViewById(R.id.mPassword);
        confirmPass = (EditText) findViewById(R.id.confirmPassword);
        gender = (RadioGroup) findViewById(R.id.gender);
        signUp = (Button) findViewById(R.id.signUp);
        firebaseAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confirmPassword = confirmPass.getText().toString().trim();
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Please enter An email or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Lenght of the password should be greater than 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(pass.equals(confirmPassword)) ) {
                    Toast.makeText(getApplicationContext(), "Password missmatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(), "Registration Successfull", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), "Registration Failed !!1", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), SignUp.class));
                                        finish();

                                    }

                                    // ...
                                }
                            });
                }
            }
        });


    }

    public void mlogin(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
