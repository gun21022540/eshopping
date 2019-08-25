package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button register_btn;
    private EditText input_name,input_phone,input_password;
    private ProgressDialog loadingBar;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView logo_bar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = (Button) findViewById(R.id.register_btn);
        input_name = (EditText) findViewById(R.id.input_name_register);
        input_phone = (EditText) findViewById(R.id.input_phone_register);
        input_password = (EditText) findViewById(R.id.input_password_register);
        loadingBar = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        logo_bar_back = (ImageView) findViewById(R.id.logo_bar_back);

        //click button register
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code go to login
                //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //startActivity(intent);
                CreateAccount();
            }
        });

        logo_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CreateAccount(){
        String inputName = input_name.getText().toString();
        String inputPhone = input_phone.getText().toString();
        String inputPassword = input_password.getText().toString();

        if (TextUtils.isEmpty(inputName)) {
            Toast.makeText(this,"Please enter your name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputPhone)) {
            Toast.makeText(this,"Please enter your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(this,"Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Validatephonenumber(inputName, inputPhone, inputPassword);
        }
    }
    //check phone number
    private void Validatephonenumber(final String inputName, final String inputPhone, final String inputPassword) {
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        //put data to firebase database
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(inputPhone).exists())){
                    HashMap<String, Object> userdatMap = new HashMap<>();
                    userdatMap.put("phone", inputPhone);
                    userdatMap.put("name", inputName);
                    userdatMap.put("password", inputPassword);

                    ref.child("Users").child(inputPhone).updateChildren(userdatMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Create Account successful.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error, Please try again after some time.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "This"+inputPhone+"ready exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
