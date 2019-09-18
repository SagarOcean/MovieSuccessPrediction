package com.blogapp.aws.movieuitemplate.Auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogapp.aws.movieuitemplate.R;
import com.blogapp.aws.movieuitemplate.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText username,password;
    ProgressBar progressBar;
    Button login;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    FirebaseUser currentUser;
    static final int PERMISSION_CODE=1001;
    static final int PICK_IMAGE_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = (ImageView) findViewById(R.id.imagePicker);
        username = (EditText) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String permissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else {
                    pickImageFromGallery();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) ){
                    Toast.makeText(getApplicationContext(),"Please enter An email or password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pass.length()<6 ){
                    Toast.makeText(getApplicationContext(),"Lenght of the password should be greater than 6",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(),"Logged In!!",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    finish();



                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(),"Email or Password doesn't exist",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                    finish();

                                }

                                // ...
                            }
                        });
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else
                    Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode== PICK_IMAGE_CODE)
            imageView.setImageURI((Uri) data.getData());
    }

    public void forgetPass(View view) {
        startActivity(new Intent(Login.this,ResetPassword.class));
        return;
    }

    public void Register(View view) {
        startActivity(new Intent(Login.this,SignUp.class));
        finish();
    }
}
