package com.example.secretkeeper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    private Button verificationbutton,verifybutton;
    private EditText phonenumber,verificationnumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks Callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        loadingprogress=new ProgressDialog(this);

        verificationbutton=findViewById(R.id.send_verification_code_button);
        verifybutton=findViewById(R.id.verification_number_button);
        phonenumber=findViewById(R.id.phone_number_login);
        verificationnumber=findViewById(R.id.verification_code);

        verificationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userphonenumber=phonenumber.getText().toString();
                if (TextUtils.isEmpty(userphonenumber)){
                    Toast.makeText(Login.this, "Please give your phone number.", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingprogress.setTitle("Phone verification");
                    loadingprogress.setMessage("we are authenticating your number");
                    loadingprogress.setCanceledOnTouchOutside(false);
                    loadingprogress.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            userphonenumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            Login.this,               // Activity (for callback binding)
                            Callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });
        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationbutton.setVisibility(View.INVISIBLE);
                phonenumber.setVisibility(View.INVISIBLE);
                String verificationcode=verificationnumber.getText().toString();
                if (TextUtils.isEmpty(verificationcode)){
                    Toast.makeText(Login.this, "please enter the code", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingprogress.setTitle("Code verification");
                    loadingprogress.setMessage("we are authenticating your code");
                    loadingprogress.setCanceledOnTouchOutside(false);
                    loadingprogress.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        Callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Login.this, "please give correct phone number.", Toast.LENGTH_SHORT).show();
                verificationbutton.setVisibility(View.VISIBLE);
                phonenumber.setVisibility(View.VISIBLE);
                verifybutton.setVisibility(View.INVISIBLE);
                verificationnumber.setVisibility(View.INVISIBLE);
            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(Login.this, "Verification code has been successfully sent.", Toast.LENGTH_SHORT).show();
                verificationbutton.setVisibility(View.INVISIBLE);
                phonenumber.setVisibility(View.INVISIBLE);
                verifybutton.setVisibility(View.VISIBLE);
                verificationnumber.setVisibility(View.VISIBLE);
                loadingprogress.dismiss();

            }
        };
    }
    private void sendusertoMainactivity() {
        Intent Mainintent=new Intent(Login.this,MainActivity.class);
        Mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Mainintent);
        finish();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingprogress.dismiss();
                            Toast.makeText(Login.this, "successfully verified", Toast.LENGTH_SHORT).show();
                            sendusertoMainactivity();

                        } else {
                            String error= Objects.requireNonNull(task.getException()).toString();
                            Toast.makeText(Login.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                            loadingprogress.dismiss();

                        }
                    }
                });
    }
}
