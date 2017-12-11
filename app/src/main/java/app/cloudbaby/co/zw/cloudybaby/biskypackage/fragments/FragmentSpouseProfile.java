package app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSpouseProfile extends Fragment {


    String phoneNumber;


    @BindView(R.id.edtFirstName)
    EditText firstname;
    @BindView(R.id.edtLastname)
    EditText lastname;
    @BindView(R.id.edtNationalId)
    EditText idNumber;

    @BindView(R.id.edtOtherName)
    EditText othername;
    @BindView(R.id.btnRegister)
    Button register;


    ProgressDialog dialog;

    RequestQueue requestQueue;

    private ValidationUtils validationUtils;


    private boolean isValidData() {

        if (!validationUtils.isValidFirstName(firstname.getText().toString())) {
            firstname.setError("Enter Spouse's Spouse's firstname");
            return false;
        }

        if (!validationUtils.isValidLastName(lastname.getText().toString())) {
            lastname.setError("Enter Spouse's last name");
            return false;
        }


        if (!validationUtils.isEmptyEditText(idNumber.getText().toString())) {
            idNumber.setError("Enter Spouse's id number");
            return false;
        }


        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_spouse_profile, container, false);


        phoneNumber = MyApplication.getPhone();
        ButterKnife.bind(this, view);
        validationUtils = new ValidationUtils(getContext());
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("loading");
        dialog.setCancelable(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidData()) {
                    if (othername.getText().toString().length() == 0) {
                        registerUser(firstname.getText().toString(), lastname.getText().toString(), idNumber.getText().toString(), "N/A");
                    } else {
                        registerUser(firstname.getText().toString(), lastname.getText().toString(), idNumber.getText().toString(), othername.getText().toString());
                    }
                }

            }
        });
        requestQueue = MyApplication.getRequestQueue();


        loadProfile(MyApplication.getParentId());


        return view;
    }


    private void registerUser(final String firstName, final String lastName, final String nationalId, final String otherName) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "updateSpouse.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        Toast.makeText(getActivity(), "Profile updated successful", Toast.LENGTH_LONG).show();

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
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();

                map.put("firstname", firstName);
                map.put("lastname", lastName);
                map.put("otherName", otherName);
                map.put("nationalId", nationalId);

                map.put("parentId", MyApplication.getParentId());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadProfile(final String clientId) {

        // pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "clientinformation.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        String firstname = json.getString("spousefirstname");
                        String lastname = json.getString("spouselastname");
                        String spouseothernames = json.getString("spouseothernames");
                        String nationalid = json.getString("spousenationalid");


                        FragmentSpouseProfile.this.firstname.setText(firstname.equals("N/A") ? "": firstname);
                        FragmentSpouseProfile.this.lastname.setText(lastname.equals("N/A") ? "": lastname);
                        FragmentSpouseProfile.this.othername.setText(spouseothernames.equals("N/A") ? "": spouseothernames);

                        FragmentSpouseProfile.this.idNumber.setText(nationalid.equals("N/A") ? "": nationalid);


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
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("clientId", clientId);


                return map;
            }
        };
        requestQueue.add(stringRequest);


    }


}
