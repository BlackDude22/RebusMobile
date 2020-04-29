package com.example.rebusmobile.ui.register;


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
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button registerButton = getView().findViewById(R.id.registerButton);
        final EditText usernameEditText = getView().findViewById(R.id.registerUsernameEditText);
        final EditText emailEditText = getView().findViewById(R.id.registerEmailEditText);
        final EditText passwordEditText = getView().findViewById(R.id.registerPasswordEditText);
        final EditText confirmPasswordEditText = getView().findViewById(R.id.registerConfirmPasswordEditText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                boolean hasErrors = false;

                if (username.isEmpty()){
                    usernameEditText.setError(getString(R.string.register_username_missing_error));
                    hasErrors = true;
                }

                if (email.isEmpty()) {
                    emailEditText.setError(getString(R.string.register_email_missing_error));
                    hasErrors = true;
                }

                if (password.isEmpty()){
                    passwordEditText.setError(getString(R.string.register_password_missing_error));
                    hasErrors = true;
                }

                if (confirmPassword.isEmpty()){
                    confirmPasswordEditText.setError(getString(R.string.register_confirm_password_missing_error));
                    hasErrors = true;
                }

                if (!password.equals(confirmPassword)){
                    confirmPasswordEditText.setError(getString(R.string.register_password_match_error));
                    hasErrors = true;
                }

                if (hasErrors)
                    return;

                RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
                connector.SendRequest(connector.POST, connector.REQUEST_REGISTER, connector.getRegisterRequest(username, password, email), new IResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                            int errorCode = responseError.getInt("ErrorCode");
                            String errorMessage = responseError.getString("ErrorMessage");
                            if (errorCode == 0){
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_log_in);
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
