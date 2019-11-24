package com.addictchat.custom;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.addictchat.R;

public class StatusBottomDialog extends BottomSheetDialog {

  TextInputLayout inputPhone;
  EditText edtPhone;
  Button btnSave;

  public StatusBottomDialog(final Context context) {
    super(context);

    View view = getLayoutInflater().inflate(R.layout.sample_layout, null);
    setContentView(view);

    // additional setup below this...

    //inputPhone = (TextInputLayout) view.findViewById(R.id.inputPhone);

   // edtPhone = (EditText) view.findViewById(R.id.etPhone);
    btnSave = (Button) view.findViewById(R.id.btnSavePhone);

    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (validatePhone()) {
          Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
        }

      }
    });

  }

  private boolean validatePhone() {
    if (edtPhone.getText().toString().isEmpty()) {
      inputPhone.setError("Please enter valid phone number with country code.");
      requestFocus(edtPhone);
      return false;
    } else {
      inputPhone.setErrorEnabled(false);
    }
    return true;
  }

  private void requestFocus(View view) {
    if (view.requestFocus()) {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }

  // ...
}
