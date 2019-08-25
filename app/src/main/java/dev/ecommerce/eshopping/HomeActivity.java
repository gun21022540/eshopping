package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView nav_bt_View = findViewById(R.id.bt_nav_bar);
        CircleImageView imageProfile = findViewById(R.id.user_image_profile);

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id==R.id.bt_home) {

        }else if (id==R.id.bt_money) {

        }else if (id==R.id.bt_ck_stock) {

        }else if (id==R.id.bt_menu) {

        }

        return true;
    }
}