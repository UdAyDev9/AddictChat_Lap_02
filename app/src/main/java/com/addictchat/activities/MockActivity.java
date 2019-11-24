package com.addictchat.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.addictchat.R;
import com.addictchat.custom.StatusBottomDialog;

public class MockActivity extends AppCompatActivity {
  Context mContext;
  Button btn,statusSaveBtn;
  BottomSheetDialog bottomSheetDialog;
  EditText statusEt;
  TextView textView;

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mock);
 /*   btn = findViewById(R.id.button);
    textView = findViewById(R.id.textView2);
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

      *//*  StatusBottomDialog dialog = new StatusBottomDialog(mContext);

        dialog.show();*//*

       // createBottomSheetDialog();
      }
    });
    mContext = this;*/


  }
/*
  private void createBottomSheetDialog() {
    if (bottomSheetDialog == null) {
      View view = LayoutInflater.from(this).inflate(R.layout.sample_layout, null);
    //  statusEt = view.findViewById(R.id.etPhone);
      statusSaveBtn = view.findViewById(R.id.btnSavePhone);
      statusSaveBtn.setOnClickListener(this);
      bottomSheetDialog = new BottomSheetDialog(this);
      bottomSheetDialog.setContentView(view);
      bottomSheetDialog.show();

    }
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()){

      case R.id.btnSavePhone:
        textView.setText(statusEt.getText().toString());
        statusEt.setText(textView.getText());
        //startActivity(new Intent(MockActivity.this,MockActivity.class));
        break;
       // bottomSheetDialog.dismiss();
    }
  }*/
}
