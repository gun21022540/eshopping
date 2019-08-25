package dev.ecommerce.eshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

import dev.ecommerce.eshopping.Model.User;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button main_login_btn,main_register_btn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_login_btn = findViewById(R.id.main_login_btn);
        main_register_btn = findViewById(R.id.main_register_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        //login
        main_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "go to Login Successful!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //register
        main_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "go to Register Successful!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhonekey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordkey);

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }
    private void AllowAccess(final String inputPhone, final String inputPassword) {
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(inputPhone).exists()){
                    User user = dataSnapshot.child("Users").child(inputPhone).getValue(User.class);

                    if (user.getPhone().equals(inputPhone)){
                        if (user.getPassword().equals(inputPassword)){
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account with this" + inputPhone +"number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
