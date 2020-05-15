package com.example.scaucontact;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

//    LinkedList<String> mGroupList = new LinkedList<>();
    GroupManager groupManager;
    RecyclerView mRecyclerView;
    GroupListAdapter mAdapter;
    LinkedList<Contact> mContactList = new LinkedList<>();
    Set<String> groupNameSet = new HashSet<>();
    private static final int WRITE_REQUEST_CODE = 43;

    DrawerLayout drawer;
    FragmentTransaction transaction;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("nmslTag", "onCreate: MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

//        checkWriteExternalPermission();


//        final NavController navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));
//        navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
//        navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//                transaction.replace(R.id.fragment_container, new SecondFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
//
////                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
////                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//
////                navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });

        init();

        findViewById(R.id.all_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 显示所有联系人
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new FirstFragment(null, 0)).commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            transaction = getSupportFragmentManager().beginTransaction();
            FirstFragment firstFragment = new FirstFragment(query, mode);
            transaction.replace(R.id.fragment_container, firstFragment).commit();
        }
    }

    void checkWriteExternalPermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "nmsl", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void createFile(String mimeType, String fileName) {
        checkWriteExternalPermission();
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);
        Uri uri = resultData.getData();
        toastPrint(uri.toString());
        writeFileFromUri(uri);
    }

    private void writeFileFromUri(Uri uri){
        try(OutputStream os = getContentResolver().openOutputStream(uri);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw)){
            for(Contact i: mContactList){
                bw.write(i.getName()+"\t"+i.getPhone()+"\t"+i.getGroupName()+"\t"
                        +i.getAddress()+"\t"+i.getRemarks()+"\t");
                bw.newLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void writeFileFromUriToVCF(Uri uri){
        try(OutputStream os = getContentResolver().openOutputStream(uri);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw)){
            for(Contact i: mContactList){
                bw.write("BEGIN:VCARD\n");
                bw.write("VERSION:2.1\n");
                bw.write("N:"+i.getName().charAt(0)+";"+i.getName().substring(1)+"\n");
                bw.write("FN:"+i.getName()+"\n");
                bw.write("TEL:"+i.getPhone()+"\n");
                bw.write("ADR:"+i.getAddress()+"\n");
                bw.write("NOTE:"+i.getRemarks()+"\n");
                bw.write("END:VCARD\n");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults){
//        switch(requestCode){
//            case 1: {
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//                }
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("nmslTag", "onCreateOptionsMenu: MainActivity");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("nmslTag", "onOptionsItemSelected: MainActivity");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action1){  // search by name
            return true;
        }
        else if(id == R.id.action2){  // search by phone
            return true;
        }
        else if(id == R.id.action3){  // export csv file
            createFile("text/plain", "ContactInfo.csv");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void initDrawerMenu(){
//        for(int i=0; i<5; i++){
//            mGroupList.addLast("Group "+i);
//        }

        mRecyclerView = findViewById(R.id.drawer_menu);
        mAdapter = new GroupListAdapter(this, groupManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setCommunicate(new Communicate() {
            @Override
            public DrawerLayout work() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new FirstFragment(mAdapter.getChosenGroupName(), 0));
                transaction.commit();
                return drawer;
            }
        });
    }

    void initContactList(){
        mContactList.clear();
        groupNameSet.clear();
        File file = new File(getExternalFilesDir(null), "ContactInfo.txt");
        try(FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr)){
            String line;
            while((line = br.readLine()) != null){
                String[] info = line.split("\\s+");
                if(info.length == 3){
                    mContactList.addLast(new Contact(
                            info[0],  // name
                            info[1],  // phone
                            info[2]   // groupName
                    ));
                }
                else if(info.length == 4){

                }
                groupNameSet.add(info[2]);
            }
        }
        catch (Exception e){
            toastPrint("init contact failed:(\n"+ e.getMessage()+"\n" + e.getLocalizedMessage());
            System.out.println("------------------------------------------------------------------------------");
            e.printStackTrace();
            System.out.println("------------------------------------------------------------------------------");
        }
        groupManager = new GroupManager(groupNameSet);
    }

    public void init(){
        initContactList();
        initDrawerMenu();
        transaction = getSupportFragmentManager().beginTransaction();
        FirstFragment firstFragment = new FirstFragment(null, 0);
        transaction.replace(R.id.fragment_container, firstFragment).commit();
    }

    public void groupToggle(View view) {
        Toast.makeText(this, "nmsl", Toast.LENGTH_LONG).show();
        findViewById(R.id.drawer_menu).setVisibility(View.VISIBLE);
        int a = findViewById(R.id.drawer_menu).getVisibility();
    }

    public void toastPrint(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void ActivateSearchName(View view) {
        mode = 1;
        onSearchRequested();
    }

    public void ActivateSearchPhone(View view) {
        mode = 2;
        onSearchRequested();
    }
}
