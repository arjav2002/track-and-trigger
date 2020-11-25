package com.oopcows.trackandtrigger.dashboard;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oopcows.trackandtrigger.databinding.ProfessionChooseFragmentBinding;
import com.oopcows.trackandtrigger.helpers.Profession;

import java.util.Objects;

public class ProfessionChooseFragment extends DialogFragment {

    private ProfessionChooseFragmentBinding binding;

    public interface ProfessionFillable {
        void fillDetails(Profession profession);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static ProfessionChooseFragment newInstance() {
        return new ProfessionChooseFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProfessionChooseFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProfessionChooseViewModel mViewModel = new ViewModelProvider(this).get(ProfessionChooseViewModel.class);

        binding.chooseProfessionSpinner.setAdapter(mViewModel.getSpinnerAdapter(this));
        binding.confirmProfessionButton.setOnClickListener((v)-> {
            ((ProfessionFillable) Objects.requireNonNull(getActivity())).fillDetails(Profession.valueOf((String) binding.chooseProfessionSpinner.getSelectedItem()));
            dismiss();
        });
        setCancelable(false);
    }


}