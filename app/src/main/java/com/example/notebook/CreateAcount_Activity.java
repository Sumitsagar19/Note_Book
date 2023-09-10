package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAcount_Activity extends AppCompatActivity {

    EditText  email,password,confirm_password;
    TextView  login_btn;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_Password);
        btn = findViewById(R.id.button);
        login_btn = findViewById(R.id.text3);

        btn.setOnClickListener(v-> createAccount() );
        login_btn.setOnClickListener(v->{
            startActivity(new Intent(CreateAcount_Activity.this,LogInActivity.class));
        });
    }

 void  createAccount(){

        String Email = email.getText().toString();
        String pass = password.getText().toString();
        String conf = confirm_password.getText().toString();

        boolean isValidated = validateData(Email,pass,conf);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(Email,pass);

 }
  void createAccountInFirebase(String Email, String pass ){

      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
      firebaseAuth.createUserWithEmailAndPassword(Email, pass).addOnCompleteListener(CreateAcount_Activity.this,
              new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          // creating account is done
                          utility.showToast(CreateAcount_Activity.this, "Succesfully create email to varify");
                          firebaseAuth.getCurrentUser().sendEmailVerification();
                          firebaseAuth.signOut();
                          finish();
                      }
                      else {
                          //fail
                          utility.showToast(CreateAcount_Activity.this,task.getException().getLocalizedMessage());
                       //   Toast.makeText(CreateAcount_Activity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              });
  }
   Boolean validateData(String Email,String pass,String conf){
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
       if (!pass.equals(conf)){
           confirm_password.setError("Password not matched");
           return false;
       }
       return true;
   }

}