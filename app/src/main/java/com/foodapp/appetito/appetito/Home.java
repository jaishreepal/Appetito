package com.foodapp.appetito.appetito;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

public class Home extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    private Eat eatFragment;
    private Host hostFragment;
    private Inbox inboxFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView listView= (ListView) findViewById(R.id.foodItems);
        frameLayout = (FrameLayout) findViewById(R.id.main_frame);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_nav);
        eatFragment = new Eat();
        hostFragment = new Host();
        inboxFragment = new Inbox();

        ArrayList<String> arrayFood = new ArrayList<>();
        arrayFood.addAll(Arrays.asList(getResources().getStringArray(R.array.foodItemsArray)));

        adapter=new ArrayAdapter<String>(Home.this,android.R.layout.simple_list_item_1,arrayFood);
        listView.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_eat:
//                        bottomNavigationView.setBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(eatFragment);
                        return true;

                    case R.id.nav_host:
//                        bottomNavigationView.setBackgroundResource(R.color.colorPrimary);
                        setFragment(hostFragment);
                        return true;

                    case R.id.nav_inbox:
//                        bottomNavigationView.setBackgroundResource(R.color.grey);
                        setFragment(inboxFragment);
                        return true;

                    default:
                        return false;
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item= menu.findItem(R.id.menuSearch);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setFragment(android.support.v4.app.Fragment fragment)
    {
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
