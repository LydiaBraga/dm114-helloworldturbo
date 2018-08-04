package br.inatel.lydia.helloworldturbo.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import br.inatel.lydia.helloworldturbo.R;

public class SettingsFragment extends PreferenceFragment {

    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preferences);
        getActivity().setTitle("Configurações");
    }
}