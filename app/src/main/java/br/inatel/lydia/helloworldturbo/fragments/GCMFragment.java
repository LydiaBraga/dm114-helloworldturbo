package br.inatel.lydia.helloworldturbo.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import br.inatel.lydia.helloworldturbo.R;
import br.inatel.lydia.helloworldturbo.gcm.GCMRegister;
import br.inatel.lydia.helloworldturbo.gcm.GCMRegisterEvents;
import br.inatel.lydia.helloworldturbo.models.OrderInfo;

public class GCMFragment extends Fragment implements GCMRegisterEvents {

    private String registrationID;
    private GCMRegister gcmRegister;
    private Button btnUnregister;
    private Button btnRegister;
    private Button btnClearMessage;
    private EditText edtSenderID;
    private TextView txtRegistrationID;
    private TextView txtOrderId;
    private TextView txtEmail;
    private TextView txtStatus;
    private TextView txtReason;

    private OrderInfo orderInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gcm, container, false);
        getActivity().setTitle("GCM");
        edtSenderID = (EditText) rootView.findViewById(R.id.edtSenderID);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        btnUnregister = (Button) rootView.findViewById(R.id.btnUnregister);
        btnClearMessage = (Button) rootView.findViewById(R.id.btnClearMessage);
        txtRegistrationID = (TextView) rootView.findViewById(R.id.txtRegistrationID);
        txtOrderId = (TextView) rootView.findViewById(R.id.txtOrderId);
        txtEmail = (TextView) rootView.findViewById(R.id.txtEmail);
        txtStatus = (TextView) rootView.findViewById(R.id.txtStatus);
        txtReason = (TextView) rootView.findViewById(R.id.txtReason);

        if (gcmRegister == null) {
            gcmRegister = new GCMRegister(getActivity(), this);
        }

        edtSenderID.setText(gcmRegister.getSenderId());

        if (!gcmRegister.isRegistrationExpired()) {
            registrationID = gcmRegister.getCurrentRegistrationId();
            setForRegistered(registrationID);
        } else {
            setForUnregistered();
        }

        Bundle bundle = this.getArguments();

        if ((bundle != null) && (bundle.containsKey("orderInfo"))) {
            orderInfo = (OrderInfo) bundle.getSerializable("orderInfo");
            txtOrderId.setText(Long.toString(orderInfo.getId()));
            txtEmail.setText(orderInfo.getEmail());
            txtStatus.setText(orderInfo.getStatus());
            txtReason.setText(orderInfo.getReason());
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationID = gcmRegister.getRegistrationId(edtSenderID.getText().toString());
                if ((registrationID == null) || (registrationID.length() == 0)) {
                    Toast.makeText(getActivity(),
                            "Dispositivo ainda não registrado na nuvem. Tentando...",
                            Toast.LENGTH_SHORT).show();
                    setForUnregistered();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Dispositivo já registrado na nuvem.",
                            Toast.LENGTH_SHORT).show();
                    setForRegistered(registrationID);
                }
            }
        });

        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gcmRegister.unRegister();
            }
        });

        btnClearMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOrderId.setText("");
                txtEmail.setText("");
                txtStatus.setText("");
                txtReason.setText("");
            }
        });

        return rootView;
    }

    @Override
    public void gcmRegisterFinished(String registrationID) {
        Toast.makeText(getActivity(),
                "Dispositivo registrado na nuvem com sucesso.",
                Toast.LENGTH_SHORT).show();
        setForRegistered(registrationID);
    }

    @Override
    public void gcmRegisterFailed(IOException ex) {
        Toast.makeText(getActivity(),
                "Falha ao registrar dispositivo na nuvem. " +
                        ex.getMessage(), Toast.LENGTH_SHORT).show();
        setForUnregistered();
    }

    @Override
    public void gcmUnregisterFinished() {
        Toast.makeText(getActivity(),
                "Dispositivo desregistrado da nuvem.",
                Toast.LENGTH_SHORT).show();
        setForUnregistered();
    }

    @Override
    public void gcmUnregisterFailed(IOException ex) {
        Toast.makeText(getActivity(),
                "Falha ao desregistrar o dispositivo na nuvem.",
                Toast.LENGTH_SHORT).show();
    }

    private void setForRegistered (String regID) {
        txtRegistrationID.setText(regID);
        btnUnregister.setEnabled(true);
        btnRegister.setEnabled(false);
    }
    private void setForUnregistered () {
        txtRegistrationID.setText("");
        btnUnregister.setEnabled(false);
        btnRegister.setEnabled(true);
    }
}
