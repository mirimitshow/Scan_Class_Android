package s2017s40.kr.hs.mirim.mirimitshow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritePostActivity extends AppCompatActivity {
    private Services service;
    Utils utils = new Utils();
    SharedPreferences sharedPreference;
    String email;
    String groupToken = "";
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;

    EditText Title, Content;
    Button postingBtn;
    ImageButton GallaryBtn;
    String title_str, content_str;
    Switch Notice;
    Spinner GroupList;

    ArrayList<String> arrayListGroup;
    ArrayList<String> arrayListToken;
    ArrayAdapter<String> arrayAdapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        Title = findViewById(R.id.PostTitle);
        Content = findViewById(R.id.PostContent);
        postingBtn = findViewById(R.id.postingBtn);
        GallaryBtn = findViewById(R.id.gallaryBtn);
        Notice = findViewById(R.id.isNotice);
        GroupList = findViewById(R.id.WritePost_groupLists);
        sharedPreference = getSharedPreferences("email", Activity.MODE_PRIVATE);
        email = sharedPreference.getString("email","defValue");
        service = utils.mRetrofit.create(Services.class);
        setlist();

        GallaryBtn.setOnClickListener(new View.OnClickListener() { // 사진 가져오기 버튼 리스너
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });

        postingBtn.setOnClickListener(new View.OnClickListener() { // 작성하기 버튼을 누를 때 이벤트
            @Override
            public void onClick(View v) {
                service = utils.mRetrofit.create(Services.class);
                String isNotice =  String.valueOf(Notice.isChecked()); // 공지글 여부
                title_str = Title.getText().toString(); // 글 타이틀
                content_str = Content.getText().toString(); // 글 내용
                long Now = System.currentTimeMillis();
                Date date = new Date(Now);



                Board board = new Board("aa",isNotice,email,title_str,content_str,String.valueOf(date));
                Call<Board> call = service.setbeard(board);
                call.enqueue(new Callback<Board>() {
                    @Override
                    public void onResponse(Call<Board> call, Response<Board> response) {
                        if(response.code() == 200){
                            Toast.makeText(WritePostActivity.this, "new board successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }else if(response.code() == 400){
                            Toast.makeText(WritePostActivity.this, "invalid input, object invalid", Toast.LENGTH_SHORT).show();
                        }else if(response.code() == 409){
                            Toast.makeText(WritePostActivity.this, "duplicated board", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Board> call, Throwable t) {
                        Toast.makeText(WritePostActivity.this, "onfailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("", tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        }
    }
    private void setImage() {
       ImageView imageView = findViewById(R.id.PostImageContent);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
        imageView.setVisibility(View.VISIBLE);
    }
    public void setlist(){
        arrayListGroup = new ArrayList<String>();
        arrayListToken = new ArrayList<String>();

        Call<ArrayList<Group>> call = service.getusergroups(email);
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                if (response.code() == 200) {
                    ArrayList<Group> groupList = response.body();
                    for(Group group : groupList){
                        arrayListGroup.add(group.getName());
                        arrayListToken.add(group.getToken());
                    }
                    Toast.makeText(WritePostActivity.this, "returns user's Groups", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(WritePostActivity.this, "invalid input, object invalid", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable t) {
                Toast.makeText(WritePostActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG).show();
                Log.e("writeError", t.toString());
            }
        });
        arrayAdapterGroup = new ArrayAdapter<>(WritePostActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListGroup);
        GroupList.setAdapter(arrayAdapterGroup);
        GroupList.setSelection(0);
    }
}
