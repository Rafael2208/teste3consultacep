package com.example.teste3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private EditText inputCep;
    private Button searchButton;
    private TextView addressTextView;

    private static final String WSDL_URL = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl";
    private static final String METHOD_NAME = "consultaCEP";
    private static final String NAMESPACE = "http://cliente.bean.master.sigep.bsb.correios.com.br/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputCep = findViewById(R.id.input_cep);
        searchButton = findViewById(R.id.search_button);
        addressTextView = findViewById(R.id.address_text_view);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = inputCep.getText().toString().trim();
                if (!cep.isEmpty()) {
                    new ConsultaCEPTask().execute(cep);
                }
            }
        });
    }

    private class ConsultaCEPTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cep = params[0];
            String result = "";

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("cep", cep);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE transport = new HttpTransportSE(WSDL_URL);
                transport.call(NAMESPACE + METHOD_NAME, envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                result = response.getProperty("end").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            addressTextView.setText(result);
        }
    }
}