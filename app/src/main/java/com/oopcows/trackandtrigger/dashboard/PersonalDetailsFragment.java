package com.oopcows.trackandtrigger.dashboard;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oopcows.trackandtrigger.databinding.PersonalDetailsFragmentBinding;
import com.oopcows.trackandtrigger.helpers.Profession;

import java.util.Objects;

public class PersonalDetailsFragment extends DialogFragment {

    private PersonalDetailsViewModel mViewModel;
    private PersonalDetailsFragmentBinding binding;

    public interface PersonalDetailsFillable {
        void fillDetails(String username, Profession profession);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static PersonalDetailsFragment newInstance() {
        return new PersonalDetailsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PersonalDetailsFragmentBinding.inflate(inflater);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalDetailsViewModel.class);

        binding.chooseProfessionSpinner.setAdapter(mViewModel.getSpinnerAdapter(this));
        binding.confirmProfessionButton.setOnClickListener((v)-> {
            ((PersonalDetailsFillable) Objects.requireNonNull(getActivity())).fillDetails(String.valueOf(binding.usernameField.getText()), Profession.valueOf((String) binding.chooseProfessionSpinner.getSelectedItem()));
            dismiss();
        });
        setCancelable(false);
    }


}