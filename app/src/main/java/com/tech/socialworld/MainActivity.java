package com.tech.socialworld;

import static com.tech.socialworld.Fragment.NotificationFragment.notificationViewPager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.tech.socialworld.Fragment.AddFragment;
import com.tech.socialworld.Fragment.HomeFragment;

import com.tech.socialworld.Fragment.NotificationFragment;
import com.tech.socialworld.Fragment.ProfileFragment;
import com.tech.socialworld.Fragment.SearchFragment;
import com.tech.socialworld.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;

    public static BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigation = findViewById(R.id.bottom_navigation);
        auth = FirebaseAuth.getInstance();
//        Objects.requireNonNull(getSupportActionBar()).hide(); // just hide the app title

        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        binding.toolbar.setVisibility(View.GONE);
        transaction.replace(R.id.container_id,new HomeFragment());
        transaction.commit();

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()){

                    case R.id.home:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container_id,new HomeFragment());
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.notification:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container_id,new NotificationFragment());
                        Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.add:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container_id,new AddFragment());
                        Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.search:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container_id,new SearchFragment());
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.user:
                        binding.toolbar.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.container_id,new ProfileFragment());
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;

                }
                transaction.commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //new AlertDialog.Builder(MainActivity.this)
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit this App?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //end the app
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

//        builder.setPositiveButton(getResources().getDrawable(R.drawable.i))
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}