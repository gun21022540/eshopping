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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import dev.ecommerce.eshopping.Model.User;
import dev.ecommerce.eshopping.Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private TextView store_login,user_login;
    private EditText phone_login,password_login;
    private Button login_btn;
    private com.rey.material.widget.CheckBox chkbLoginRemember;
    private ProgressDialog loadingBar;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView logo_bar_back;

    private String parentDBName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        store_login = (TextView) findViewById(R.id.store_login);
        user_login =(TextView) findViewById(R.id.user_login);
        phone_login =(EditText) findViewById(R.id.input_phone_login);
        password_login = (EditText) findViewById(R.id.input_password_login);
        login_btn = (Button) findViewById(R.id.login_btn);
        chkbLoginRemember = (com.rey.material.widget.CheckBox) findViewById(R.id.remember_ckb_login);
        loadingBar = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        logo_bar_back = (ImageView) findViewById(R.id.logo_bar_back);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        store_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setText("Login store");
                store_login.setVisibility(View.INVISIBLE);
                user_login.setVisibility(View.VISIBLE);
                parentDBName = "Store";
            }
        });

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setText("Login");
                store_login.setVisibility(View.VISIBLE);
                user_login.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });

        logo_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoginUser() {
        String inputPhone = phone_login.getText().toString();
        String inputPassword = password_login.getText().toString();
        if (TextUtils.isEmpty(inputPhone)) {
            Toast.makeText(this,"Please enter your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(this,"Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(inputPhone,inputPassword);
        }
    }

    private void AllowAccessToAccount(final String inputPhone, final String inputPassword) {

        if (chkbLoginRemember.isChecked()){
            Paper.book().write(Prevalent.UserPhonekey, inputPhone);
            Paper.book().write(Prevalent.UserPasswordkey, inputPassword);
        }

        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(inputPhone).exists()){
                    User user = dataSnapshot.child(parentDBName).child(inputPhone).getValue(User.class);

                    if (user.getPhone().equals(inputPhone)){
                        if (user.getPassword().equals(inputPassword)){
                            if (parentDBName.equals("Store")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDBName.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "Welcome "+user.getName() +", Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = user;
                                startActivity(intent);
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account with this" + inputPhone +"number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
