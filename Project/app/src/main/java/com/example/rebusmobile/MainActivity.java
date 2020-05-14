package com.example.rebusmobile;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private static Integer theme = R.style.AppTheme;
    private static Integer startFragment = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(theme);
        setContentView(R.layout.activity_main);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);
        graph.setStartDestination(startFragment);
        navController.setGraph(graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_log_in, R.id.nav_settings_logged_in, R.id.nav_settings_logged_out, R.id.nav_log_out, R.id.nav_register)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (UserSettings.isIsLoggedIn())
            setLoggedIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setLoggedIn(){
        Menu menu = navigationView.getMenu();
        menu.removeItem(R.id.nav_log_in);
        menu.removeItem(R.id.nav_register);
        menu.removeItem(R.id.nav_settings_logged_out);
        menu.add(R.id.personal_group, R.id.nav_settings_logged_in, Menu.NONE, R.string.menu_settings);
        menu.add(R.id.personal_group, R.id.nav_log_out, Menu.NONE, R.string.menu_log_out);
        UserSettings.setIsLoggedIn(true);
    }

    public void setLoggedOut(){
        Menu menu = navigationView.getMenu();
        menu.removeItem(R.id.nav_log_out);
        menu.removeItem(R.id.nav_settings_logged_in);
        menu.add(R.id.personal_group, R.id.nav_settings_logged_out, Menu.NONE, R.string.menu_settings);
        menu.add(R.id.personal_group, R.id.nav_log_in, Menu.NONE, R.string.menu_log_in);
        menu.add(R.id.personal_group, R.id.nav_register, Menu.NONE, R.string.menu_register);
        UserSettings.setIsLoggedIn(false);
    }

    public void setDarkTheme(Integer fragment){
        theme = R.style.DarkAppTheme;
        startFragment = fragment;
        this.finish();
        this.startActivity(getIntent());
    }

    public void setLightTheme(Integer fragment){
        theme = R.style.AppTheme;
        startFragment = fragment;
        this.finish();
        this.startActivity(getIntent());
    }
}
