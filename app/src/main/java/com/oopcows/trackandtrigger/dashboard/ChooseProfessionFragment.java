package com.oopcows.trackandtrigger.dashboard;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.databinding.ActivityEmailVerifyBinding;
import com.oopcows.trackandtrigger.databinding.ChooseProfessionFragmentBinding;
import com.oopcows.trackandtrigger.helpers.Profession;

public class ChooseProfessionFragment extends DialogFragment {

    private ChooseProfessionViewModel mViewModel;
    private ChooseProfessionFragmentBinding binding;

    public interface ChooseProfession {
        void setChosenProfession(Profession profession);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static ChooseProfessionFragment newInstance() {
        return new ChooseProfessionFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChooseProfessionFragmentBinding.inflate(inflater);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChooseProfessionViewModel.class);

        binding.chooseProfessionSpinner.setAdapter(mViewModel.getSpinnerAdapter(this));
        binding.confirmProfessionButton.setOnClickListener((v)-> {
            ((ChooseProfession)getActivity()).setChosenProfession(Profession.valueOf((String) binding.chooseProfessionSpinner.getSelectedItem()));
            dismiss();
        });
        setCancelable(false);
    }


}