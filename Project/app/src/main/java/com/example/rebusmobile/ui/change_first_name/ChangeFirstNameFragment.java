package com.example.rebusmobile.ui.change_first_name;

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

public class ChangeFirstNameFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_first_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText firstNameEditText = getActivity().findViewById(R.id.newFirstNameEditText);
        final Button button = getActivity().findViewById(R.id.changeFirstNameButton);

        if (!UserSettings.getName().equals("null"))
            firstNameEditText.setText(UserSettings.getName());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFirstName = firstNameEditText.getText().toString();

                boolean hasErrors = false;

                if (newFirstName.isEmpty()){
                    firstNameEditText.setError(getString(R.string.change_first_name_missing_error));
                    hasErrors = true;
                }

                if (hasErrors)
                    return;

                RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
                connector.sendRequest(connector.POST, connector.REQUEST_PERSONAL_INFO, connector.getChangeFirstNameRequest(UserSettings.getToken(), UserSettings.getId(), newFirstName), new IResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.v("TEST", response.toString());

                        try {
                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                            int errorCode = responseError.getInt("ErrorCode");
                            String errorMessage = responseError.getString("ErrorMessage");
                            if (errorCode == 0){
                                UserSettings.loadPersonalInfo((JSONObject) response);
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_settings_logged_in);
                            }else if (errorCode == 999 || errorCode == 1) {
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
