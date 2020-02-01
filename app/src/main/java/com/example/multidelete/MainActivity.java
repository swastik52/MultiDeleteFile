package com.example.multidelete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    public static  int REQUEST_PERMISSION =1;
    Toolbar toolbar;
    RecyclerView recyclerCategory;
    Boolean permission;
    File directory;
    private File_item_Adapter file_item_adapter;
    public ArrayList<File> fileArrayList = new ArrayList<File>();
    public ArrayList<File> selected_fileName = new ArrayList<File>();
    boolean is_in_Action_mode=false;
    TextView counter_textview;
    int count_selected_file=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        permission();

        String path = Environment.getExternalStorageDirectory() + "//AzAttendance//Finger_data";
        directory = new File(path);
        file_item_adapter=new File_item_Adapter(getFile(directory),this);
        recyclerCategory=findViewById(R.id.recyclerCategory);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategory.setAdapter(file_item_adapter);
        counter_textview=findViewById(R.id.counter_text);
        counter_textview.setVisibility(View.GONE);
       /* file_item_adapter.setOnItemClickListener(new onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
               preparedSelection(view,position);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Toast
    @SuppressLint("ResourceAsColor")
    public void showToast() {
        new StyleableToast
                .Builder(this)
                .text("Show Toast")
                .textColor(Color.WHITE)
                .backgroundColor(R.color.tostColor)
                .iconStart(R.drawable.thunder)
                .length(10)
                .cornerRadius(30)
                .show();
        // StyleableToast.makeText(this,"Show Toast",R.style.exampleToast).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permission = true;
            }
            else{
                Toast.makeText(this, "Please Allow the Permission ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else{
            permission = true;

        }

    }
    public ArrayList<File> getFile(File directory) {
        if (directory != null) {
            if (directory.canRead()) {
                File listFile[] = directory.listFiles();
                if (listFile != null && listFile.length > 0) {
                    for (int i = 0; i < listFile.length; i++) {
                        if (listFile[i].isDirectory()) {
                            getFile(listFile[i]);
                        } else {
                            permission = false;
                            if (listFile[i].getName().endsWith(".iso1")) {

                                for (int j = 0; j < fileArrayList.size(); j++) {
                                    if (fileArrayList.get(j).getName().equals(listFile[i].getName())) {
                                        permission = true;
                                    } else {

                                    }
                                }
                                if (permission) {
                                    permission = false;
                                } else {
                                    fileArrayList.add(listFile[i]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return fileArrayList;
    }



    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_activity_main);
        counter_textview.setVisibility(View.VISIBLE);
        is_in_Action_mode=true;
        file_item_adapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true ;
    }

    public void preparedSelection(View view,int position){
       if (((CheckBox)view).isChecked()){
           selected_fileName.add(fileArrayList.get(position));
           count_selected_file=count_selected_file+1;
           updateCounter(count_selected_file);
       }else {
           selected_fileName.remove(fileArrayList.get(position));
           count_selected_file=count_selected_file-1;
           updateCounter(count_selected_file);
       }
    }
    public void updateCounter(int counter){
        if (counter==0){
            counter_textview.setText("0 item Selected");
        }else{
            counter_textview.setText(counter+" items Selected");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.delete_menu){
            file_item_adapter.updateAdapter(selected_fileName);
            clearActionMode();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearActionMode(){
        is_in_Action_mode=false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter_textview.setVisibility(View.GONE);
        counter_textview.setText("0 item Selected");
        count_selected_file=0;
        selected_fileName.clear();
    }

    @Override
    public void onBackPressed() {
        if (is_in_Action_mode){
            clearActionMode();
            file_item_adapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }
}
