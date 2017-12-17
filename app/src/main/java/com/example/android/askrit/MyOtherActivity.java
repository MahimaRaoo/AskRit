package com.example.android.askrit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;

import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MyOtherActivity extends AppCompatActivity implements AIListener {
    AIService aiService;
    TextView t;
    EditText e;
    AIDataService aiDataService;
    AIRequest aiRequest;
    AIResponse response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_other);

        t=(TextView)findViewById(R.id.response);

        e=(EditText)findViewById(R.id.query);

        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }


            final AIConfiguration config = new AIConfiguration("3a13046078674e8596eace8af2da82d1",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        aiDataService = new AIDataService(config);
        aiRequest = new AIRequest();




       /* Button btn = (Button)findViewById(R.id.open_activity1_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOtherActivity.this, MyOtherActivityTwo.class));
            }
        });*/
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

                switch (requestCode) {
                        case 101: {

                                       if (grantResults.length == 0 || grantResults[0] !=
                                               PackageManager.PERMISSION_GRANTED) {



                                                    } else {

                                            }
                                return;
                           }
                   }
           }



    public void buttonClicked(final View view) {

        aiService.startListening();

    }



    public void inpTextBtnClicked(final View view){


        String query=e.getText().toString();

        aiRequest.setQuery(query);
        /*try {
            aiService.textRequest(aiRequest);
        } catch (AIServiceException e1) {
            e1.printStackTrace();
        }*/
        /*try {
        final AIResponse aiResponse = aiDataService.request(aiRequest);
        } catch (AIServiceException e1) {
            e1.printStackTrace();
        }*/



        sendRequest();
    }

    public void sendRequest(){

        new AsyncTask<AIRequest,Void,AIResponse>(){

            @Override
            protected AIResponse doInBackground(AIRequest... aiRequests) {
                final AIRequest request = aiRequests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse response) {
                if (response != null) {

                    onResult(response);

                }
            }
        }.execute(aiRequest);


    }






    @Override
    public void onResult(final AIResponse result) {

        Result result1=result.getResult();
        final String speech = result1.getFulfillment().getSpeech();

        e.setText(result1.getResolvedQuery());
        t.setText("\n"+speech);
    }

    @Override
    public void onError(final AIError error) {
        t.setText(error.toString());

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {
        aiService.stopListening();
    }
}
