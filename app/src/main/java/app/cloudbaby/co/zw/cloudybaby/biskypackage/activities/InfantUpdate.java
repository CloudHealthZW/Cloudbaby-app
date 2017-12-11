package app.cloudbaby.co.zw.cloudybaby.biskypackage.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.MGUtilities;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InfantUpdate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.edtFirstName)
    EditText firstname;
    @BindView(R.id.edtLastname)
    EditText lastname;
    @BindView(R.id.edtDOB)
    EditText dob;
    @BindView(R.id.edtOtherName)
    EditText otherName;
    @BindView(R.id.edtBcNumber)
    EditText bcNumber;
    @BindView(R.id.btnRegister)
    Button register;
    @BindView(R.id.spnGender)
    Spinner gender;
    @BindView(R.id.spnType)
    Spinner type;
    @BindView(R.id.btnChoose)
    Button choose;
    @BindView(R.id.imgProfilePic)
    ImageView photo;

    ProgressDialog dialog;

    RequestQueue requestQueue;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    private ValidationUtils validationUtils;
    private String genderString;
    private String typeString;
    private String infantId;

    private byte[] data;
    private String picturePath;
    private static final int RESULT_LOAD_IMAGE = 1, RESULT_LOAD_IMAGE1 = 7;

    private final int PICTURE_REQUEST_CODE = 22;
    private Uri outputFileUri;
    private final String CLOUD_BABY_DIR = "CloudBaby";

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
        setContentView(R.layout.activity_infant_update);
        infantId = getIntent().getExtras().getString("infantId");
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, InfantUpdate.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Update Infant");

        validationUtils = new ValidationUtils(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);
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
        typeCat.add("pragnancy");
        typeCat.add("baby");

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

                    String otherNameString = "N/A";
                    if (otherName.getText().toString().length() == 0) {
                        otherNameString = "N/A";
                    } else {
                        otherNameString = otherName.getText().toString();
                    }

                    if (!typeString.equalsIgnoreCase("pragnancy")) {
                        registerInfant(firstname.getText().toString(), lastname.getText().toString(), dob.getText().toString(), typeString, genderString, bcNumber.getText().toString(), otherNameString, infantId);

                    } else {
                        registerInfant(firstname.getText().toString(), lastname.getText().toString(), dob.getText().toString(), typeString, genderString, "notSpecified" + String.valueOf(System.currentTimeMillis()) + MyApplication.getParentId(), otherNameString, infantId);

                    }
                }
            }
        });

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

        loadProfile(infantId);
    }


    private void registerInfant(final String firstName, final String lastName, final String dateofbirth, final String typeTo, final String gender, final String bcertno, final String otherNames, final String infantId) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "updateInfant.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        MGUtilities.showAlertView(
                                InfantUpdate.this,
                                R.string.registered,
                                "Updated Successful");


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
                map.put("infantId", infantId);
                map.put("firstname", firstName);
                map.put("lastname", lastName);
                map.put("gender", gender);
                map.put("dateofbirth", dateofbirth);
                map.put("type", typeTo);
                map.put("othernames", otherNames);
                map.put("bcertno", bcertno);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadProfile(final String infantId) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "childInformation.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("data", response);

                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {
/*

  $user["id"] = $row['id'];
			$user["type"] = $row['type'];
			$user["bcertno"] = $row['bcertno'];
			$user["firstname"] = $row['firstname'];
			$user["lastname"] = $row['lastname'];
			$user["othernames"] = $row['othernames'];
			$user["gender"] = $row['gender'];
			$user["dateofbirth"] = $row['dateofbirth'];
			$user["status"] = $row['status'];
			$user["centreid"] = $row['centreid'];
			$user["profilephoto"] = $row['profilephoto'];
 */

                        String firstname = json.getString("firstname");
                        String lastname = json.getString("lastname");
                        String bcertno = json.getString("bcertno");
                        String othernames = json.getString("othernames");
                        String dateofbirth = json.getString("dateofbirth");
                        String profilephoto = json.getString("profilephoto");

                        InfantUpdate.this.firstname.setText(firstname);
                        InfantUpdate.this.lastname.setText(lastname);
                        InfantUpdate.this.dob.setText(dateofbirth);
                        InfantUpdate.this.otherName.setText(othernames);
                        InfantUpdate.this.bcNumber.setText(bcertno);

                        Picasso.with(getBaseContext()).load(Config.BASE_URL + profilephoto).placeholder(R.drawable.placeholder).into(InfantUpdate.this.photo);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("error", "" + error);
                Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("infantId", infantId);
                map.put("parentId", MyApplication.getParentId());

                return map;
            }
        };
        requestQueue.add(stringRequest);


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_LOAD_IMAGE) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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
//                saveScaledPhotoCamera(bitmap);
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
        uploadimage(this.data, infantId);

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

        uploadimage(this.data, infantId);

    }

    private void uploadimage(final byte[] imageData, final String infantId) {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "uploadPhotoInfant.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);

                    String success = json.getString("response");

                    if (success.equals("success")) {


                        // Toast.makeText(getBaseContext(), "Profile Picture Uploaded successful", Toast.LENGTH_SHORT).show();

                        Snackbar.make(findViewById(android.R.id.content), "Profile Picture Uploaded successful", Snackbar.LENGTH_LONG).show();
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
                //   Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("image", Base64.encodeToString(imageData, Base64.DEFAULT));

                map.put("infantId", infantId);
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
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
        final PackageManager packageManager = getPackageManager();
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
        if (resultCode == InfantUpdate.this.RESULT_OK) {
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
                    File myPic = new File(GeneralUtils.getRealPathFromURI(InfantUpdate.this, selectedImageUri));

                    saveScaledPhoto(myPic.getPath());
                }
            }
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Set the date
        dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }

}
