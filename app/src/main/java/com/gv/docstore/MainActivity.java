package com.gv.docstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String tempimgloc;
    File temp;
    RecyclerView recyclerView;

    private List<Dataset> datasetList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rec_view);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        myAdapter = new MyAdapter(datasetList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "FAB WORKS", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File imgsrc = null;
                try{
                    imgsrc = createImage();
                }catch (IOException e){
                    e.printStackTrace();
                }

                if(imgsrc != null){
                    Uri photouri = FileProvider.getUriForFile(getApplicationContext(), "com.gv.docstore.fileprovider", imgsrc);
                    System.out.println(photouri);
                    System.out.println(tempimgloc);

                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                    startActivityForResult(takepic, 1);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_CANCELED){
            boolean del = temp.delete();
            if(del){
                System.out.println("FILE DELETED");
            }else{
                System.out.println("FILE NOT DELETED");
            }
        }else if(requestCode == 1 && resultCode == RESULT_OK){

                getfiles();

            if(datasetList != null) {
                for (Dataset i : datasetList) {
                    System.out.println(i.getImg());
                    System.out.println(i.getImgname());
                }
            }
        }
    }

    private File createImage() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgfile = "JPEG_"+timestamp+"_";
        File storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(imgfile, ".jpg", storage);
        temp = img.getAbsoluteFile();
        tempimgloc = img.getAbsolutePath();
        return img;
    }

    private void getfiles(){
        datasetList.clear();
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(dir.listFiles() != null) {
            for (File i : dir.listFiles()) {
                if (i.isFile()) {
                    datasetList.add(new Dataset(i.getPath(), i.getPath()));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //View view = findViewById(R.id.floatingActionButton);
        //Snackbar.make(view, "resumed", Snackbar.LENGTH_SHORT).show();
        getfiles();
        myAdapter.notifyDataSetChanged();
    }

}
