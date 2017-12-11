package app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.slides;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.github.vacxe.phonemask.PhoneMaskManager;
import com.github.vacxe.phonemask.ValueListener;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.OnNavigationBarListener;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.events.PhoneNumberEnteredEvent;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Kudzai Chasinda
 */

public class PhoneNumberSlideFragment extends Fragment implements BlockingStep {

    private boolean isInputValidated = true;

    @BindView(R.id.textViewTitle)
    TextView title;
    @BindView(R.id.textViewSubTitle)
    TextView subTitle;
    @BindView(R.id.edtPhone)
    EditText phoneNumber;

    @BindView(R.id.img_slide_2)
    ImageView imgSlide;



    ProgressDialog dialog;
    PhoneMaskManager phoneMaskManager;

    @Nullable
    private OnNavigationBarListener onNavigationBarListener;

    public static PhoneNumberSlideFragment newInstance() {
        Bundle args = new Bundle();
        PhoneNumberSlideFragment fragment = new PhoneNumberSlideFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_phone_number_slide, container, false);
        ButterKnife.bind(this, rootView);

        title.setText(R.string.title_slide_1);

        subTitle.setText(R.string.subtitle_slide_1);

        Glide.with(getActivity())
                .load(R.drawable.intro_image_two)
                .crossFade()
                .into(imgSlide);

        updateNavigationBar();

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

        return rootView;
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        updateNavigationBar();
        if (phoneMaskManager.getPhone().length() == 13) {
            String code = "123456";
            EventBus.getDefault().post(new PhoneNumberEnteredEvent(code, phoneMaskManager.getPhone()));
            callback.goToNextStep();
        } else {
            isInputValidated = false;
            callback.getStepperLayout().updateErrorState(true);
        }

        Log.e("step","next clicked "+isInputValidated);
    }

    private boolean valid(){
        return (phoneMaskManager.getPhone().length() == 13);
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        //TODO cancel any running operations
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return valid() ? null : new VerificationError("Invalid phone number");
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
