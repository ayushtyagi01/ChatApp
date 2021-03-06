package com.ayushtyagi.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText username,email,password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference refereance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        btn_register=findViewById(R.id.btn_register);

        auth =FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=username.getText().toString().trim();
                String txt_email=email.getText().toString().trim();
                String txt_password=password.getText().toString().trim();
                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<8){
                    Toast.makeText(RegisterActivity.this, "Password must contain at least 8 letters", Toast.LENGTH_SHORT).show();
                }
                else{
                    register(txt_username,txt_email,txt_password);
                }
            }
        });

    }
    private void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                  if (task.isSuccessful()) {
                      FirebaseUser firebaseuser = auth.getCurrentUser();
                      assert firebaseuser != null;
                      String userid = firebaseuser.getUid();
                      refereance = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                      HashMap<String, String> hashMap = new HashMap<>();
                      hashMap.put("id", userid);
                      hashMap.put("username", username);
                      hashMap.put("imageURL", "default");
                      refereance.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()) {
                                  Intent intent = new Intent(RegisterActivity.this, Main2Activity.class);
                                  Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(intent);
                                  finish();
                              }
                          }
                      });
                  }
                  else{
                      Toast.makeText(RegisterActivity.this, "Failed to SIGN UP", Toast.LENGTH_SHORT).show();
                  }

            }
        });
    }
}
