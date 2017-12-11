package app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.slides.PhoneNumberSlideFragment;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.intro.slides.PhoneNumberVerificationSlideFragment;

/**
 * @author Kudzai Chasinda
 */

public class IntroStepperAdapter extends AbstractFragmentStepAdapter {
    public IntroStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        switch (position) {
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle(R.string.tab_title1)
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle(R.string.tab_title2)
                        .create();
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }

    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position) {
            case 0:
                return PhoneNumberSlideFragment.newInstance();
            case 1:
                return PhoneNumberVerificationSlideFragment.newInstance();
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
