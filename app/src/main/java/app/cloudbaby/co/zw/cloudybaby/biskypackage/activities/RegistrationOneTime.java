package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appnirman.vaidationutils.ValidationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.helpers.FirebaseHelper;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.model.UserModel;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO use this class to create a new firebase user object for your chat application
 */

public class RegistrationOneTime extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String phoneNumber;


    @BindView(R.id.edtFirstName)
    EditText firstname;
    @BindView(R.id.edtLastname)
    EditText lastname;
    @BindView(R.id.edtNationalId)
    EditText idNumber;
    @BindView(R.id.edtEmail)
    EditText email;
    @BindView(R.id.edtAddress)
    EditText addressPhysical;
    @BindView(R.id.btnRegister)
    Button register;
    @BindView(R.id.spnGender)
    Spinner gender;

    @BindView(R.id.app_bar)
    Toolbar toolbar;


    ProgressDialog dialog;

    RequestQueue requestQueue;

    private ValidationUtils validationUtils;
    private String genderString;


    private boolean isValidData() {

        if (!validationUtils.isValidFirstName(firstname.getText().toString())) {
            firstname.setError("Enter your firstname");
            return false;
        }

        if (!validationUtils.isValidLastName(lastname.getText().toString())) {
            lastname.setError("Enter your last name");
            return false;
        }

        if (!validationUtils.isEmptyEditText(addressPhysical.getText().toString())) {
            addressPhysical.setError("Enter physical address");
            return false;
        }

        if (!validationUtils.isEmptyEditText(idNumber.getText().toString())) {
            idNumber.setError("Enter your id number");
            return false;
        }
        if (!validationUtils.isValidEmail(email.getText().toString())) {
            email.setError("Enter valid email address");
            return false;
        }

        if (genderString.equalsIgnoreCase("choose gender")) {
            Toast.makeText(this, "Choose your gender", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_one_time);

        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phoneNumber");
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Register");

        validationUtils = new ValidationUtils(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidData()) {
                    registerUser(phoneNumber, firstname.getText().toString(), lastname.getText().toString(), email.getText().toString(), idNumber.getText().toString(), addressPhysical.getText().toString(), genderString);
                }

            }
        });
        requestQueue = MyApplication.getRequestQueue();
        gender.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("choose gender");
        categories.add("male");
        categories.add("female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

    }


    private void registerUser(final String phoneNum, final String firstName, final String lastName, final String email, final String nationalId, final String physicalAddress, final String gender) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "register.php", new Response.Listener<String>() {
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


                        //Here we edit the prefs and push to firebase

                        UserModel user = new UserModel(GeneralUtils.prepPhoneNumber(phone),
                                "offline", firstname, lastname, "android", "", 0, "");
                        FirebaseHelper mHelper = FirebaseHelper.newInstance(RegistrationOneTime.this);
                        Log.e("ADDUSER",user.getUserId());
                        mHelper.addUserToDatabase(RegistrationOneTime.this, user);

                        PreferencesUtils mUtils = PreferencesUtils.getInstance(RegistrationOneTime.this);

                        //Now Save these things in the shared preferences
                        mUtils.setPrefsFirstName(firstname);
                        mUtils.setPrefsLastName(lastname);
                        mUtils.setPrefsUserPhoneNumber(GeneralUtils.prepPhoneNumber(phone));
                        mUtils.setPrefsUserid(GeneralUtils.prepPhoneNumber(phone));


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
                        //todo error register

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
                Toast.makeText(RegistrationOneTime.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("phoneNumber", phoneNum);
                map.put("firstname", firstName);
                map.put("lastname", lastName);
                map.put("gender", gender);
                map.put("nationalId", nationalId);
                map.put("email", email);
                map.put("address", physicalAddress);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genderString = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
