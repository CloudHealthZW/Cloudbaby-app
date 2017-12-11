package app.cloudbaby.co.zw.cloudybaby.biskypackage.fragments;


import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appnirman.vaidationutils.ValidationUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentClientProfile extends Fragment implements AdapterView.OnItemSelectedListener {

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
    @BindView(R.id.btnChoose)
    Button choose;
    @BindView(R.id.imgProfilePic)
    ImageView photo;

    ProgressDialog pDialog;
    private final int PICTURE_REQUEST_CODE = 1;
    private Uri outputFileUri;
    private final String CLOUD_BABY_DIR = "CloudBaby";

    RequestQueue requestQueue;

    private ValidationUtils validationUtils;
    private String genderString;

    private byte[] data;
    private String picturePath;
    private static final int RESULT_LOAD_IMAGE = 1, RESULT_LOAD_IMAGE1 = 7;

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
            Toast.makeText(getActivity(), "Choose your gender", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_client_profile, container, false);

        phoneNumber = MyApplication.getPhone();
        ButterKnife.bind(this, view);
        validationUtils = new ValidationUtils(getContext());
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("loading");
        pDialog.setCancelable(false);

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                startActivityForResult(intent, RESULT_LOAD_IMAGE1);

                openImageIntent();
            }
        });

        loadProfile(MyApplication.getParentId());


        return view;
    }

    private void registerUser(final String phoneNum, final String firstName, final String lastName, final String email, final String nationalId, final String physicalAddress, final String gender) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "updateClient.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        String names = firstName + " " + lastName;


                        SharedPreferences preferencesUser = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editorUser = preferencesUser.edit();


                        editorUser.putString("names", names);


                        editorUser.apply();

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
                pDialog.dismiss();
                Log.e("error", "" + error);
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
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
                map.put("parentId", MyApplication.getParentId());
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_LOAD_IMAGE) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                picturePath = cursor.getString(columnIndex);
//
//
//                cursor.close();
//
//
//                //   photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                saveScaledPhoto(picturePath);
//            }
//
//        } else if (requestCode == RESULT_LOAD_IMAGE1) {
//            if (resultCode == Activity.RESULT_OK) {
//                //  Log.d("CAMERA CALLED", "" + "CAMERA BUNDLES");
//                Bundle extras = data.getExtras();
//
//                Bitmap bitmap = (Bitmap) extras.get("data");
//
//
//            }
//
//        }
//    }

    private void saveScaledPhoto(String data) {

        // Resize photo from camera byte array

        Bitmap mealImage = BitmapFactory.decodeFile(data);
        Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 240, 320
                * mealImage.getHeight() / mealImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        //  matrix.postRotate(90);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
                0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        this.data = bos.toByteArray();
        Log.e("Length Gallery", "" + this.data.length + "bytes");
        photo.setImageBitmap(rotatedScaledMealImage);
        uploadimage(this.data);

    }

    private void saveScaledPhotoCamera(Bitmap data) {

        Log.d("SCALED CALLED", "" + "SCALED CALLED");

        //   Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mealImageScaled = Bitmap.createScaledBitmap(data, 320, 240
                * data.getHeight() / data.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        // matrix.postRotate(90);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
                0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        this.data = bos.toByteArray();
        Log.e("Length Camera", "" + this.data.length + "bytes");
        photo.setImageBitmap(rotatedScaledMealImage);

        uploadimage(this.data);

    }

    private void uploadimage(final byte[] imageData) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "uploadPhotoClient.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        Toast.makeText(getActivity(), "Profile Picture Uploaded successful", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("data", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("error", "" + error);
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("image", Base64.encodeToString(imageData, Base64.DEFAULT));

                map.put("parentId", MyApplication.getParentId());
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }


    private void loadProfile(final String clientId) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "clientinformation.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        String firstname = json.getString("firstname");
                        String lastname = json.getString("lastname");
                        String email = json.getString("email");
                        String homeaddress = json.getString("homeaddress");
                        String nationalid = json.getString("nationalid");
                        String image = json.getString("image");

                        FragmentClientProfile.this.firstname.setText(firstname);
                        FragmentClientProfile.this.lastname.setText(lastname);
                        FragmentClientProfile.this.email.setText(email);
                        FragmentClientProfile.this.addressPhysical.setText(homeaddress);
                        FragmentClientProfile.this.idNumber.setText(nationalid);

                        Picasso.with(getActivity()).load(Config.BASE_URL + image).placeholder(R.drawable.placeholder).into(FragmentClientProfile.this.photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("data", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
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

    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + CLOUD_BABY_DIR + File.separator);
        if (!root.exists()) {
            root.mkdirs();
        }
        final String fname = GeneralUtils.getUniqueImageFilename();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(pickIntent, "Select image from");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, PICTURE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    File myPic = new File(selectedImageUri.getPath());

                    saveScaledPhoto(myPic.getPath());
                } else {
                    selectedImageUri = data.getData();
                    File myPic = new File(GeneralUtils.getRealPathFromURI(getActivity(), selectedImageUri));

                    saveScaledPhoto(myPic.getPath());
                }
            }
        }

    }


}
