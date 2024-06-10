package com.example.electronicgallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 100;
    private ImageView imageView;
    private int currentIndex = 0;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3}; // 이미지 배열
    private int delayTime = 3000; // 딜레이 시간 (3초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // 이미지 변경 시작
        changeImage();
    }

    // 이미지 변경 메서드
    private void changeImage() {
        imageView.setImageResource(images[currentIndex]);
        currentIndex = (currentIndex + 1) % images.length;

        // 일정 시간 후에 다음 이미지를 표시
        new Handler().postDelayed(this::changeImage, delayTime);
    }

    // 액션바에 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // 액션바 메뉴 선택 시 동작 설정
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_select_image){

            selectImageFromAlbum();
            return true;
        }
        else if(id == R.id.action_set_delay){
            setDelayTime();
            return true;
        }
        else if(id == R.id.action_set_orientation){

            setOrientation();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    // 앨범에서 이미지 선택
    private void selectImageFromAlbum() {
        Toast.makeText(this, "action_select_image", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    // 이미지 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            // 이미지 선택 결과를 ImageView에 설정
            imageView.setImageURI(data.getData());
        }
    }

    // 이미지 변경 딜레이 타임 설정
    private void setDelayTime() {
        // 딜레이 타임을 설정하는 다이얼로그 또는 다른 방법을 사용하여 구현
        // 여기서는 간단하게 고정된 딜레이 타임을 사용
        Toast.makeText(this, "action_set_delay", Toast.LENGTH_SHORT).show();
        showTimerDialog();
    }

    // 화면 방향 설정
    private void setOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "가로 화면으로 전환됩니다.", Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            Toast.makeText(this, "세로 화면으로 전환됩니다.", Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void showTimerDialog() {
        // 다이얼로그를 위한 레이아웃 인플레이션
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_timer, null);

        // NumberPicker 초기화
        NumberPicker secondPicker = dialogView.findViewById(R.id.secondPicker);

        // NumberPicker 설정
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        // 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("타이머 설정")
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 설정 버튼 클릭 시 동작
                        int seconds = secondPicker.getValue();
                        String time = seconds + "초";
                        Toast.makeText(MainActivity.this, "설정된 시간: " + time, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 버튼 클릭 시 동작
                        dialog.cancel();
                    }
                });
        // 다이얼로그 표시
        builder.create().show();
    }
}