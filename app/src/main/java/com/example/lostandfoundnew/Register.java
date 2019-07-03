package com.example.lostandfoundnew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;

public class Register extends AppCompatActivity {
    private static final String FINE_CAMERA = "android.permission.CAMERA";

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    private static final int camera_request_code = 1234;

    Button button;

    CircleImageView circleImageView;

    DatabaseReference databaseReference;

    EditText editTextemail;

    EditText editTextpassword;

    EditText edtxtconfrmpas;

    FirebaseAuth firebaseAuth1;

    EditText firstname;

    String image = "no";

    EditText lastname;

    private boolean mCamera_permissiongrannted = false;

    EditText phonenumber;

    ProgressBar progressBar;

    ProgressDialog progressDialog;

    TextView textView;

    String uid;

    private void checkpermission() {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = FINE_CAMERA;
        arrayOfString[1] = WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_CAMERA) == 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == 0) {
                mCamera_permissiongrannted = true;
                onPickImage();

            }
            ActivityCompat.requestPermissions(this, arrayOfString, camera_request_code);

        }
        ActivityCompat.requestPermissions(this, arrayOfString, camera_request_code);
    }

    public static Bitmap decodeFile(String paramString) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(paramString, options);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferQualityOverSpeed = true;
        return BitmapFactory.decodeFile(paramString, options);
    }

    public static String encodeTobase64(Bitmap paramBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, TRIM_MEMORY_BACKGROUND, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), MODE_PRIVATE);
    }

    private void registerUser() {
        String str1 = editTextemail.getText().toString().trim();
        String str2 = editTextpassword.getText().toString().trim();
        String str3 = edtxtconfrmpas.getText().toString().trim();
        if (TextUtils.isEmpty(str1)) {
            Toast.makeText(this, "Email is empty ", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(str2)) {
            Toast.makeText(this, "Passwordis empty ", Toast.LENGTH_SHORT).show();
        }
        if (!str2.equals(str3)) {
            editTextpassword.setError("Password Not Mathched");
            edtxtconfrmpas.setError("Password Not Mathched");
        }
        if (TextUtils.isEmpty(firstname.getText())) {
            firstname.setError("first name is empty");
        }
        if (TextUtils.isEmpty(lastname.getText())) {
            lastname.setError("last name is empty");
        }
        if (TextUtils.isEmpty(phonenumber.getText())) {
            firstname.setError("phone number is empty");
        }
        progressDialog.setMessage("registring user");
        progressDialog.show();
        firebaseAuth1.createUserWithEmailAndPassword(str1, str2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> param1Task) {
                if (param1Task.isSuccessful()) {
                    uid = firebaseAuth1.getUid();
                    userprofile();
                    return;
                }
               progressDialog.dismiss();
                if (param1Task.getException() instanceof com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                    Toast.makeText(Register.this, "Email Already Register", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), param1Task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userprofile() {
        UserProfileModel _User_profileModel = new UserProfileModel(firstname.getText().toString(), lastname.getText().toString(), phonenumber.getText().toString(), editTextemail.getText().toString(), image);
        databaseReference.child(uid).setValue(_User_profileModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> param1Task) {
                if (param1Task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Suuceesfull Register", Toast.LENGTH_SHORT).show();
                    firebaseAuth1.signOut();
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception param1Exception) {
                Toast.makeText(Register.this, param1Exception.getMessage(), Toast.LENGTH_SHORT).show();
                firebaseAuth1.signOut();
                progressDialog.dismiss();
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap paramBitmap, int paramInt) {
        int i = paramBitmap.getWidth();
        int j = paramBitmap.getHeight();
        float f = i / j;
        if (f > 1.0F) {
            j = (int)(paramInt / f);
            i = paramInt;
            paramInt = j;
            return Bitmap.createScaledBitmap(paramBitmap, i, paramInt, true);
        }
        i = (int)(paramInt * f);
        return Bitmap.createScaledBitmap(paramBitmap, i, paramInt, true);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult( paramInt1, paramInt2, paramIntent);
        if (paramInt2 == RESULT_OK && paramInt1 ==(mCamera_permissiongrannted?1:0) ) {
            Bitmap bitmap = decodeFile(paramIntent.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(0));
            circleImageView.setImageBitmap(bitmap);
            image = encodeTobase64(getResizedBitmap(bitmap, 400));
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_register);
        firebaseAuth1 = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        button = findViewById(R.id.btnregister);
        textView = findViewById(R.id.txtalreadyreg);
        editTextemail = findViewById(R.id.email);
        editTextpassword = findViewById(R.id.password);
        databaseReference = FirebaseDatabase.getInstance().getReference("userprofile");
        edtxtconfrmpas = findViewById(R.id.confermpassword);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phonenumber = findViewById(R.id.phonenum);
        progressBar = findViewById(R.id.progressBarimageload);
        circleImageView = findViewById(R.id.imgupload);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { checkpermission(); }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (param1View == button)
                    registerUser();
            }
        });
        this.textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (param1View == textView) {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void onPickImage() { Pix.start(this, Options.init().setRequestCode(100).setCount(1));}
}
