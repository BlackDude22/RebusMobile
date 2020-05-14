package com.example.rebusmobile.ui.log_in;


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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.MainActivity;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.UserSettings;
import com.example.rebusmobile.ui.register.RegisterFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button goToRegisterButton = getView().findViewById(R.id.goToRegisterButton);
        final Button logInButton = getView().findViewById(R.id.loginButton);
        final EditText usernameEditText = getView().findViewById(R.id.usernameEditText);
        final EditText passwordEditText = getView().findViewById(R.id.passwordEditText);

        goToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                boolean hasErrors = false;

                if (username.isEmpty()){
                    usernameEditText.setError(getString(R.string.login_username_missing_error));
                    hasErrors = true;
                }

                if (password.isEmpty()){
                    passwordEditText.setError(getString(R.string.login_password_missing_error));
                    hasErrors = true;
                }

                if (hasErrors)
                    return;

                final RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
                connector.sendRequest(connector.POST, connector.REQUEST_LOGIN, connector.getLoginRequest(username, password), new IResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                            int errorCode = responseError.getInt("ErrorCode");
                            String errorMessage = responseError.getString("ErrorMessage");
                            if (errorCode == 0){
                                ((MainActivity)getActivity()).setLoggedIn();
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                                UserSettings.initialize((JSONObject)response);

                                connector.sendRequest(connector.GET, connector.REQUEST_PERSONAL_INFO, connector.getPersonalInfoGetRequest(UserSettings.getToken(), UserSettings.getId()), new IResponseListener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        Log.v("TEST", response.toString());
                                        UserSettings.loadPersonalInfo((JSONObject) response);

                                        connector.sendRequest(connector.GET, connector.REQUEST_BALANCE, connector.getBalanceGetRequest(UserSettings.getToken(), UserSettings.getId()), new IResponseListener() {
                                            @Override
                                            public void onResponse(Object response) {
                                                Log.v("TEST BALANCE", response.toString());
                                                UserSettings.loadBalance((JSONObject) response);
                                            }

                                            @Override
                                            public void onError(String message) {
                                                Log.v("Test", message);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.v("Test", message);
                                    }
                                });


                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_home);
                            }
                            else Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("Test", response.toString());
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
