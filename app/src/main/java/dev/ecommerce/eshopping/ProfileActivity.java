package dev.ecommerce.eshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.ecommerce.eshopping.Prevalent.Prevalent;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView backArrow = findViewById(R.id.back_profile);

        CircleImageView imageProfile = findViewById(R.id.image_profile);
        TextView nameProfile = findViewById(R.id.name_profile);
        TextView moneyProfile = findViewById(R.id.num_money_profile);
        TextView phoneProfile = findViewById(R.id.num_phone_profile);
        TextView addressProfile = findViewById(R.id.detail_address_profile);

        nameProfile.setText(Prevalent.currentOnlineUser.getName());
        phoneProfile.setText(Prevalent.currentOnlineUser.getPhone());


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
