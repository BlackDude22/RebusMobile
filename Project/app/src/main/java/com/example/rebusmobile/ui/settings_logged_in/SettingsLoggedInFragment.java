package com.example.rebusmobile.ui.settings_logged_in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rebusmobile.MainActivity;
import com.example.rebusmobile.R;
import com.example.rebusmobile.UserSettings;

public class SettingsLoggedInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_logged_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView firstNameTextView = getActivity().findViewById(R.id.firstNameTextView);
        final TextView lastNameTextView = getActivity().findViewById(R.id.lastNameTextView);
        final TextView emailTextView = getActivity().findViewById(R.id.emailTextView);
        final TextView balanceTextView = getActivity().findViewById(R.id.balanceTextView);
        final TextView phoneTextView = getActivity().findViewById(R.id.phoneTextView);
        final TextView addressTextView = getActivity().findViewById(R.id.addressTextView);

        final Button changeFirstNameButton = getActivity().findViewById(R.id.goToChangeFirstNameButton);
        final Button changeLastNameButton = getActivity().findViewById(R.id.goToChangeLastNameButton);
        final Button changePhoneButton = getActivity().findViewById(R.id.goToChangePhoneButton);
        final Button changeBalanceButton = getActivity().findViewById(R.id.goToChangeBalanceButton);
        final Button changePasswordButton = getActivity().findViewById(R.id.goToChangePasswordButton);
        final Button changeAddressButton = getActivity().findViewById(R.id.goToChangeAddressButton);

        final Switch themeSwitch = getActivity().findViewById(R.id.loggedInThemeSwitch);
        themeSwitch.setChecked(UserSettings.isIsDarkTheme());

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserSettings.setIsDarkTheme(isChecked);
                if (isChecked)
                    ((MainActivity)getActivity()).setDarkTheme(R.id.nav_settings_logged_in);
                else ((MainActivity)getActivity()).setLightTheme(R.id.nav_settings_logged_in);
            }
        });

        changeFirstNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_change_first_name);
            }
        });

        changeLastNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_change_last_name);
            }
        });

        changePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_change_phone);
            }
        });

        changeBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_change_balance);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_change_password);
            }
        });

        changeAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_change_address);
            }
        });

        if (!UserSettings.getName().equals("null"))
            firstNameTextView.setText(UserSettings.getName());

        if (!UserSettings.getLastName().equals("null"))
            lastNameTextView.setText(UserSettings.getLastName());

        if (!UserSettings.getPhone().equals("null"))
            phoneTextView.setText(UserSettings.getPhone());

        if (!UserSettings.getCountry().equals("null") && !UserSettings.getCity().equals("null") && !UserSettings.getStreet().equals("null") && !UserSettings.getHouse().equals("null"))
            addressTextView.setText(UserSettings.getCountry() + ", " + UserSettings.getCity() + ", " + UserSettings.getStreet() + ", " + UserSettings.getHouse());

        if (!UserSettings.getEmail().equals("null"))
            emailTextView.setText(UserSettings.getEmail());

        if (!UserSettings.getBalance().equals("null"))
            balanceTextView.setText(UserSettings.getBalance() + " €");


    }

}
