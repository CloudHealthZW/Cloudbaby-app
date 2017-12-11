package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.vacxe.phonemask.PhoneMaskManager;
import com.github.vacxe.phonemask.ValueListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneVerification extends AppCompatActivity {
    String code, phoneNumber;
    @BindView(R.id.edtCode)
    EditText codeVerification;

    @BindView(R.id.btnVerify)
    Button verify;
    ProgressDialog dialog;
    PhoneMaskManager phoneMaskManager;
    RequestQueue requestQueue;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        Bundle bundle = getIntent().getExtras();


        setSupportActionBar(toolbar);
        setTitle("Verification");

        code = bundle.getString("code");
        phoneNumber = bundle.getString("phoneNumber");

        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);
        phoneMaskManager = new PhoneMaskManager();
        phoneMaskManager.withMaskSymbol("\\*");
        phoneMaskManager.withMask("***-***");

        phoneMaskManager.withValueListener(new ValueListener() {
            @Override
            public void onPhoneChanged(String phone) {
                Log.e("Code", phone);
            }
        });
        phoneMaskManager.bindTo(codeVerification);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phoneMaskManager.getPhone().length() == 7) {

                    String phone = "";
                    if (phoneMaskManager.getPhone().startsWith("+")) {
                        phone = phoneMaskManager.getPhone().substring(1);
                    }
                    //  Log.e("NewPhone", phone);
                    if (phone.equalsIgnoreCase(code)) {
                        //  Toast.makeText(PhoneVerification.this, "Code matches", Toast.LENGTH_SHORT).show();

                        Log.e("Code", "code matches");

                        loginUser(phoneNumber);
                    } else {
                        Toast.makeText(PhoneVerification.this, "Invalid code entered", Toast.LENGTH_SHORT).show();
                        //    Log.e("Code", "invalid code");
                    }
                } else {
                    Toast.makeText(PhoneVerification.this, "Enter 6 digit code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue = MyApplication.getRequestQueue();
    }


    private void loginUser(final String phoneNum) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {

                        String firstname = json.getString("firstname");
                        String lastname = json.getString("lastname");
                        String parentId = json.getString("id");

                        String names = firstname + " " + lastname;

                        String phone = json.getString("phone");


                        Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("success", true);
                        editor.apply();

                        SharedPreferences preferencesUser = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editorUser = preferencesUser.edit();


                        editorUser.putString("names", names);

                        editorUser.putString("phone", phone);
                        editorUser.putString("parentId", parentId);
                        editorUser.apply();

                        startActivity(new Intent(getBaseContext(), HomeNav.class));
                        finish();

                    } else {
                        Intent intent = new Intent(getBaseContext(), RegistrationOneTime.class);
                        intent.putExtra("phoneNumber", phoneNumber);

                        startActivity(intent);
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("data", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("error", "" + error);
                Toast.makeText(PhoneVerification.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("phoneNumber", phoneNum);


                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


}
