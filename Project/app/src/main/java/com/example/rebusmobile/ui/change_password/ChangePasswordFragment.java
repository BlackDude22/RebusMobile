package com.example.rebusmobile.ui.change_password;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.MainActivity;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.UserSettings;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText currentPasswordEditText = getActivity().findViewById(R.id.currentPasswordEditText);
        final EditText newPasswordEditText = getActivity().findViewById(R.id.newPasswordEditText);
        final EditText confirmNewPasswordEditText = getActivity().findViewById(R.id.confirmNewPasswordEditText);

        final Button changePasswordButton = getActivity().findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

                boolean hasErrors = false;

                if (currentPassword.isEmpty()){
                    currentPasswordEditText.setError(getString(R.string.change_password_missing_error));
                    hasErrors = true;
                }

                if (newPassword.isEmpty()){
                    newPasswordEditText.setError(getString(R.string.change_new_password_missing_error));
                    hasErrors = true;
                }

                if (confirmNewPassword.isEmpty()){
                    confirmNewPasswordEditText.setError(getString(R.string.change_confirm_new_password_missing_error));
                    hasErrors = true;
                }

                if (!newPassword.equals(confirmNewPassword)){
                    confirmNewPasswordEditText.setError(getString(R.string.change_password_match_error));
                    hasErrors = false;
                }

                if (hasErrors)
                    return;

                RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
                connector.sendRequest(connector.POST, connector.REQUEST_PASSWORD_CHANGE, connector.getChangePasswordRequest(UserSettings.getUsername(), currentPassword, newPassword), new IResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                            int errorCode = responseError.getInt("ErrorCode");
                            String errorMessage = responseError.getString("ErrorMessage");
                            if (errorCode == 0){
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                                UserSettings.initialize((JSONObject)response);
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_settings_logged_in);
                            } else if (errorCode == 999 || errorCode == 1) {
                                UserSettings.clear();
                                ((MainActivity)getActivity()).setLoggedOut();
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_home);
                            }
                            else Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Log.v("TEST", message);
                    }
                });
            }
        });
    }
}
