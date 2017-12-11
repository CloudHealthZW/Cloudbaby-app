package app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.slides;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.vacxe.phonemask.PhoneMaskManager;
import com.github.vacxe.phonemask.ValueListener;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.HomeNav;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.activities.RegistrationOneTime;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.application.MyApplication;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.OnNavigationBarListener;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.events.PhoneNumberEnteredEvent;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.Config;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.GeneralUtils;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.utils.PreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Kudzai Chasinda
 */

public class PhoneNumberVerificationSlideFragment extends Fragment implements BlockingStep {

    private boolean isInputValidated;
    String code, phoneNumber;

    @BindView(R.id.textViewTitle)
    TextView title;

    @BindView(R.id.edtCode)
    EditText codeVerification;

    @BindView(R.id.img_slide_2)
    ImageView imgSlide;


    ProgressDialog dialog;
    PhoneMaskManager phoneMaskManager;
    RequestQueue requestQueue;
    private PreferencesUtils mUtils;


    @Nullable
    private OnNavigationBarListener onNavigationBarListener;

    public static PhoneNumberVerificationSlideFragment newInstance() {
        Bundle args = new Bundle();
        PhoneNumberVerificationSlideFragment fragment = new PhoneNumberVerificationSlideFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationBarListener) {
            onNavigationBarListener = (OnNavigationBarListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_phone_number_verification_slide, container, false);
        ButterKnife.bind(this, rootView);

        mUtils = PreferencesUtils.getInstance(getActivity());

        Glide.with(getActivity())
                .load(R.drawable.intro_image_two)
                .crossFade()
                .into(imgSlide);

        dialog = new ProgressDialog(getActivity());
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

        requestQueue = MyApplication.getRequestQueue();

        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhoneNumberEnteredEvent(PhoneNumberEnteredEvent event) {
        //We get the phone number muno and do the other stuff
        code = event.code;
        phoneNumber = event.phoneNumber;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void loginUser(final String phoneNum, final StepperLayout.OnCompleteClickedCallback callback) {

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


                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("success", true);
                        editor.apply();

                        SharedPreferences preferencesUser = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editorUser = preferencesUser.edit();


                        editorUser.putString("names", names);

                        editorUser.putString("phone", phone);
                        editorUser.putString("parentId", parentId);
                        editorUser.apply();
                        //Set Issa ID here
                        Log.e("PHONENUMBER", GeneralUtils.sanitizePhoneNumber(phone));
                        mUtils.setPrefsFirstName(firstname);
                        mUtils.setPrefsLastName(lastname);
                        mUtils.setPrefsUserPhoneNumber(GeneralUtils.prepPhoneNumber(phone));
                        mUtils.setPrefsUserid(GeneralUtils.sanitizePhoneNumber(phone));


                        startActivity(new Intent(getActivity(), HomeNav.class));
                        getActivity().finish();

                    } else {
                        Intent intent = new Intent(getActivity(), RegistrationOneTime.class);
                        intent.putExtra("phoneNumber", phoneNumber);

                        startActivity(intent);
                        getActivity().finish();
                    }
                    callback.complete();

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
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
//        if (isValid()) {
//            loginUser(phoneNumber, callback);
//        } else {
//            callback.getStepperLayout().updateErrorState(true);
//        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        //callback.complete();
        if (isValid()) {
            loginUser(phoneNumber, callback);
        } else {
            callback.getStepperLayout().updateErrorState(true);
        }
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        //TODO cancel any running operations
        callback.goToPrevStep();
    }


    @Override
    public VerificationError verifyStep() {
        return isValid() ? null : new VerificationError("Oops :/ Check your verification code");
    }

    private boolean isValid() {
        boolean valid = true;
        if (phoneMaskManager.getPhone().length() == 7) {
            String phone = "";
            if (phoneMaskManager.getPhone().startsWith("+")) {
                phone = phoneMaskManager.getPhone().substring(1);
            }

            if (phone.equalsIgnoreCase(code)) {
                Log.e("Code", "code matches");
                valid = true;
            } else {
                valid = false;
                Toast.makeText(getActivity(), "Invalid code entered", Toast.LENGTH_SHORT).show();
            }
        } else {
            valid = false;
            Toast.makeText(getActivity(), "Enter 6 digit code", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    @Override
    public void onSelected() {
        updateNavigationBar();
    }

    private void updateNavigationBar() {
        if (onNavigationBarListener != null) {
            onNavigationBarListener.onChangeEndButtonsEnabled(isInputValidated);
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        title.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
    }
}
