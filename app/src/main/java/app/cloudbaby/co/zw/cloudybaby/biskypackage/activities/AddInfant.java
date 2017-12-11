package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.MGUtilities;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddInfant extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    @BindView(R.id.edtFirstName)
    EditText firstname;
    @BindView(R.id.edtLastname)
    EditText lastname;
    @BindView(R.id.edtDOB)
    EditText dob;
    @BindView(R.id.edtBcNumber)
    EditText bcNumber;
    @BindView(R.id.btnRegister)
    Button register;
    @BindView(R.id.spnGender)
    Spinner gender;

    @BindView(R.id.spnType)
    Spinner type;

    ProgressDialog dialog;

    RequestQueue requestQueue;

    private ValidationUtils validationUtils;
    private String genderString;
    private String typeString;

    @BindView(R.id.app_bar)
    Toolbar toolbar;


    private boolean isValidData() {

        if (!validationUtils.isValidFirstName(firstname.getText().toString())) {
            firstname.setError("Enter your firstname");
            return false;
        }

        if (!validationUtils.isValidLastName(lastname.getText().toString())) {
            lastname.setError("Enter your last name");
            return false;
        }

        if (!validationUtils.isEmptyEditText(dob.getText().toString())) {
            dob.setError("Enter date of birth");
            return false;
        }


        if (genderString.equalsIgnoreCase("choose gender")) {


            Snackbar.make(findViewById(android.R.id.content), "Choose Gender", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (typeString.equalsIgnoreCase("choose type")) {
            Snackbar.make(findViewById(android.R.id.content), "Choose Type", Snackbar.LENGTH_LONG).show();
            return false;
        }


        if (typeString.equalsIgnoreCase("baby")) {
            if (!validationUtils.isEmptyEditText(bcNumber.getText().toString())) {
                bcNumber.setError("Enter birth certificate number");
                return false;
            } else {
                return true;
            }
            // return false;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_infant);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, AddInfant.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                datePickerDialog.show();
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dob.hasFocus()){
                    datePickerDialog.show();
                }

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Infant");

        validationUtils = new ValidationUtils(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);


        requestQueue = MyApplication.getRequestQueue();
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeString = parent.getItemAtPosition(position).toString();

                if (typeString.equalsIgnoreCase("baby")) {
                    bcNumber.setVisibility(View.VISIBLE);
                } else {
                    bcNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("choose gender");
        categories.add("male");
        categories.add("female");
        categories.add("unspecified");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);


        // Spinner Drop down elements
        List<String> typeCat = new ArrayList<String>();
        typeCat.add("choose type");
        typeCat.add("Pregnancy");
        typeCat.add("Baby");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeCat);

        // Drop down layout style - list view with radio button
        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        type.setAdapter(dataAdapterType);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData()) {


                    if (!typeString.equalsIgnoreCase("pragnancy")) {
                        registerInfant(firstname.getText().toString(), lastname.getText().toString(), dob.getText().toString(), typeString, genderString, bcNumber.getText().toString());

                    } else {
                        registerInfant(firstname.getText().toString(), lastname.getText().toString(), dob.getText().toString(), typeString, genderString, "notSpecified" + String.valueOf(System.currentTimeMillis()) + MyApplication.getParentId());

                    }
                }
            }
        });
    }


    private void registerInfant(final String firstName, final String lastName, final String dateofbirth, final String typeTo, final String gender, final String bcertno) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "registerInfant.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        AddInfant.this.firstname.setText("");
                        AddInfant.this.lastname.setText("");
                        AddInfant.this.dob.setText("");
                        AddInfant.this.bcNumber.setText("");

                        MGUtilities.showAlertView(
                                AddInfant.this,
                                R.string.registered,
                                "Registered Successful");


                    } else if (success.equals("exist")) {

                        MGUtilities.showAlertView(
                                AddInfant.this,
                                R.string.exist,
                                "Infant already exist");
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
                Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("parentid", MyApplication.getParentId());
                map.put("firstname", firstName);
                map.put("lastname", lastName);
                map.put("gender", gender);
                map.put("dateofbirth", dateofbirth);
                map.put("type", typeTo);
                map.put("bcertno", bcertno);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Set the date
        dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }
}
