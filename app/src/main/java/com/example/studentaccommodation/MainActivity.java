package com.example.studentaccommodation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Home fragment as the default fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new Home());
        fragmentTransaction.commit();

        // Set up the BottomNavigationView and listen for item clicks
        bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
//                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, new Home());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_add:
//                        Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.container, new Add());
                        fragmentTransaction2.commit();
                        return true;
                    case R.id.navigation_myposts:
//                        Toast.makeText(MainActivity.this, "MyPosts", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager3 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                        fragmentTransaction3.replace(R.id.container, new MyPosts());
                        fragmentTransaction3.commit();
                        return true;
                    case R.id.navigation_profile:
//                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager4 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                        fragmentTransaction4.replace(R.id.container, new Profile());
                        fragmentTransaction4.commit();
                        return true;
                }
                return false;
            }
        });
    }
}
