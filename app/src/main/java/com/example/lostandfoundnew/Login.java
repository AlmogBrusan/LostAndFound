package com.example.lostandfoundnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}@[a-zA-Z0-9][a-zA-Z0-9-]{0,64}(.[a-zA-Z0-9][a-zA-Z0-9-]{0,25})+");
    EditText editTextemail;
    EditText editTextpassword;
    FirebaseAuth firebaseAuth;
    Button loginbutton;
    DatabaseReference databaseRef;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    TextView textViewreghere, txtForget;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_login);

        loginbutton = findViewById(R.id. btnlogin);
        editTextemail = findViewById(R.id. loginemail);
        editTextpassword = findViewById(R.id. loginpassword);
        textViewreghere = findViewById(R.id. txtreghere);
        relativeLayout = findViewById(R.id. relativelayoutlogin);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("token");
        txtForget = findViewById(R.id. txtforget);
        progressDialog = new ProgressDialog(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (param1View == loginbutton) {
                    validate();
                    userLogin();
                }
            }
        });
        textViewreghere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (param1View == textViewreghere) {
                    Intent intent = new Intent(getBaseContext(), Register.class);
                    startActivity(intent);
                }
            }
        });
        txtForget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        String str1 = editTextemail.getText().toString().trim();
        String str2 = editTextpassword.getText().toString().trim();
        if (TextUtils.isEmpty(str1)) {
            Toast.makeText(this, "Email is empty ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            Toast.makeText(this, "Password is empty ", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("loging in");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(str1, str2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> param1Task) {
                Intent intent;
                progressDialog.dismiss();
                if (param1Task.isSuccessful()) {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseRef.child(str).child("token_id").setValue(token);
                    finish();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
//                if (intent.getException() instanceof com.google.firebase.auth.FirebaseAuthWeakPasswordException)
//                    Toast.makeText(getApplicationContext(), intent.getException().getMessage(), 0).show();
//                if (intent.getException() instanceof com.google.firebase.auth.FirebaseAuthEmailException) {
//                    Toast.makeText(getApplicationContext(), intent.getException().getMessage(), 0).show();
//                    return;
//                }
//                Toast.makeText(getApplicationContext(), intent.getException().getMessage(), 0).show();
            }
        });
    }

    private boolean validate() {
        boolean bool = true;
        String str = this.editTextemail.getText().toString();
        this.editTextpassword.getText().toString();
        if (!this.EMAIL_ADDRESS_PATTERN.matcher(str).matches()) {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        return bool;
    }


}
