package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
EditText email,password;
Button login_btn;
TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        login_btn = findViewById(R.id.button1);
        createAccount = findViewById(R.id.text31);

        login_btn.setOnClickListener(v-> loginUser());
        createAccount.setOnClickListener(v -> {
           startActivity(new Intent(LogInActivity.this,CreateAcount_Activity.class));
        });
    }

    private void loginUser() {

        String Email = email.getText().toString();
        String pass = password.getText().toString();


        boolean isValidated = validateData(Email,pass);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(Email,pass);
    }

    void loginAccountInFirebase(String Email,String pass){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //login Successful
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        // go to mainActivity
                        startActivity(new Intent(LogInActivity.this,MainActivity.class));
                        finish();
                    }else {
                        //
                        utility.showToast(LogInActivity.this,"Email is not verified,Please verify your email");
                    }

                }
                else { // login failed
                    utility.showToast(LogInActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    Boolean validateData(String Email,String pass){
        /// validate the data that are input by user .

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            email.setError("Invalid Email");
            return false;
        }
        if (pass.length() <6){
            password.setError("Password length  is invalid");
            return false;
        }

        return true;
    }

}