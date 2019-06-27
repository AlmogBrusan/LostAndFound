package com.example.lostandfoundnew;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    EditText editText;

    FirebaseAuth firebaseAuth;

    private void forgetpassword() {
        String str = editText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(str).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> param1Task) {
                if (param1Task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetPassword.this.getApplicationContext(), Login.class);
                    startActivity(intent);
                    return;
                }
                Toast.makeText(ForgetPassword.this, "Fail to send reset password email!ErrorManager", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_forget_password);
        editText = findViewById(R.id.emailreset);
        Button button = findViewById(R.id.btnreset);
        firebaseAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { forgetpassword(); }
        });
    }
}
