package com.tech.socialworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tech.socialworld.Fragment.AddFragment;
import com.tech.socialworld.Fragment.HomeFragment;
import com.tech.socialworld.Fragment.NotificationFragment;
import com.tech.socialworld.Fragment.ProfileFragment;
import com.tech.socialworld.Fragment.SearchFragment;
import com.tech.socialworld.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide(); // just hide the app title

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_id,new HomeFragment());
        transaction.commit();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()){

                    case R.id.home:
                        transaction.replace(R.id.container_id,new HomeFragment());
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.notification:
                        transaction.replace(R.id.container_id,new NotificationFragment());
                        Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.add:
                        transaction.replace(R.id.container_id,new AddFragment());
                        Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.search:
                        transaction.replace(R.id.container_id,new SearchFragment());
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.user:
                        transaction.replace(R.id.container_id,new ProfileFragment());
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;

                }
                transaction.commit();
                return true;
            }
        });
    }
}