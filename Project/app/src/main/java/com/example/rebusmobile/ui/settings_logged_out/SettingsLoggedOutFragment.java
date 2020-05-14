package com.example.rebusmobile.ui.settings_logged_out;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rebusmobile.MainActivity;
import com.example.rebusmobile.R;
import com.example.rebusmobile.UserSettings;

public class SettingsLoggedOutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_logged_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Switch themeSwitch = getActivity().findViewById(R.id.loggedOutThemeSwitch);
        themeSwitch.setChecked(UserSettings.isIsDarkTheme());

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserSettings.setIsDarkTheme(isChecked);
                if (isChecked)
                    ((MainActivity)getActivity()).setDarkTheme(R.id.nav_settings_logged_out);
                else ((MainActivity)getActivity()).setLightTheme(R.id.nav_settings_logged_out);
            }
        });
    }
}
