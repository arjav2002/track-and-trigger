package com.oopcows.trackandtrigger.dashboard;

import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;

import com.oopcows.trackandtrigger.helpers.Profession;

import java.util.ArrayList;
import java.util.List;

public class PersonalDetailsViewModel extends ViewModel {

    public ArrayAdapter<String> getSpinnerAdapter(PersonalDetailsFragment fragment) {
        List<String> professions = new ArrayList<String>();
        for(Profession p : Profession.values()) {
            if(p == Profession.nullProfession) continue;
            professions.add(p.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getContext(),
                android.R.layout.simple_spinner_item, professions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        return adapter;
    }

}