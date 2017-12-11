package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.vacxe.phonemask.PhoneMaskManager;
import com.github.vacxe.phonemask.ValueListener;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneNumber extends AppCompatActivity {
    @BindView(R.id.edtPhone)
    EditText phoneNumber;

    @BindView(R.id.btnContinue)
    Button continueButton;
    ProgressDialog dialog;
    PhoneMaskManager phoneMaskManager;


    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        setTitle("Phone Number");
        boolean isLogin = MyApplication.getLogin();


        if (isLogin) {

            startActivity(new Intent(getBaseContext(), HomeNav.class));
            finish();
        }


        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);
        phoneMaskManager = new PhoneMaskManager();
        phoneMaskManager.withMaskSymbol("\\*");
        phoneMaskManager.withMask("(***)***-***");

        phoneMaskManager.withRegion("+263");
        phoneMaskManager.withValueListener(new ValueListener() {
            @Override
            public void onPhoneChanged(String phone) {
                Log.e("Phone", phone);
            }
        });
        phoneMaskManager.bindTo(phoneNumber);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phoneMaskManager.getPhone().length() == 13) {
                    Intent intent = new Intent(getBaseContext(), PhoneVerification.class);
                    intent.putExtra("code", "123456");
                    intent.putExtra("phoneNumber", phoneMaskManager.getPhone());
                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(PhoneNumber.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


}
