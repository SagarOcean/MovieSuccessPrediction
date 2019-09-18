package com.blogapp.aws.movieuitemplate.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogapp.aws.movieuitemplate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    EditText email;
    FirebaseAuth mAuth;
    Button sendCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        progressBar = (ProgressBar) findViewById(R.id.resetProgress);
        email = (EditText) findViewById(R.id.resetEmail);
        sendCode = (Button) findViewById(R.id.sendCode);
        mAuth= FirebaseAuth.getInstance();
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String mail= email.getText().toString().trim();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(getApplicationContext(),"Please Enter an Email address",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Please check your mail to reset password!!!",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(),"Error Occured: "  + message,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                                finish();
                            }
                        }
                    });

                }

            }
        });
    }
}
