package prepareias.tychestudios.com.prepareias.quiz;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings.System;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import prepareias.tychestudios.com.prepareias.AppController;
import prepareias.tychestudios.com.prepareias.R;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = QuizActivity.class.getSimpleName();
    private String url = "http://prepareias.in/api/integration/prelims/test/next";
    private String urlN = "";
    private String urlP = "";
    private ProgressDialog pDialog;
    private List<QuestionObject> qlist=new ArrayList<>();
    private int questionNumber = 0,pageNumber = 0;
    private boolean dataReceived=false;
    private TextView optionA, optionB,optionC,optionD;
    private WebView question, description;
    private Button checkAnswer, prev, next;
    private List<String> urlCalls = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_question);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        question =(WebView)findViewById(R.id.questiontv);
        optionA =(TextView)findViewById(R.id.optionA);
        optionB =(TextView)findViewById(R.id.optionB);
        optionC =(TextView)findViewById(R.id.optionC);
        optionD =(TextView)findViewById(R.id.optionD);
        description =(WebView)findViewById(R.id.description);
        checkAnswer =(Button)findViewById(R.id.checkAnswer);
        prev =(Button)findViewById(R.id.prev);
        next =(Button)findViewById(R.id.next);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description.setVisibility(View.VISIBLE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prev.isEnabled())prev.setEnabled(true);
                    questionNumber++;
                    if(questionNumber == 5){
                        pageNumber++;
                        questionNumber = 0;
                        url = urlCalls.get(pageNumber);
                        qlist.clear();
                        fetchQuestions();
                    }else {
                        displayQuestion(qlist.get(questionNumber));
                    }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionNumber--;
                if (questionNumber == -1) {
                    pageNumber--;
                    questionNumber = 4;
                    url = urlCalls.get(pageNumber);
                    qlist.clear();
                    fetchQuestions();
                } else {
                    displayQuestion(qlist.get(questionNumber));

                }
            }
        });
        urlCalls.add(url);
       fetchQuestions();



    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchQuestions(){

        //final List<QuestionObject> qlist = new ArrayList<>();
        JsonArrayRequest quesReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, " :: response : " + response.toString());
                        urlN = "";
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, " :: obj[" + (i + 1) + "} : " + obj.toString());
                                QuestionObject ques = new QuestionObject(Integer.parseInt(obj.getString("id")),
                                        obj.getString("question"),obj.getString("option1"),obj.getString("option2"),
                                        obj.getString("option3"),obj.getString("option4"),obj.getString("answer"),
                                        obj.getString("description"));
                                Log.d(TAG, " :: QuestionObject["+(i+1)+"} : " + ques.toString());

                                if(urlN.equalsIgnoreCase(""))urlN = obj.getString("id");
                                else urlN = urlN + "," + obj.getString("id");
                                qlist.add(ques);
                                Log.i(TAG, "qlist.size()=" +qlist.size() + " :: i=" + i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        Log.i(TAG, " :: Dynamic List :: sectionList : " + qlist.toString());
                        Log.i(TAG, "qlist.size() = " + qlist.size());
                        Collections.sort(qlist, new Comparator<QuestionObject>() {
                            @Override
                            public int compare(QuestionObject lhs, QuestionObject rhs) {
                                if (lhs.getId() < rhs.getId()) return -1;
                                if (lhs.getId() > rhs.getId()) return 1;
                                return 0;
                            }
                        });
                        urlCalls.set(pageNumber,"http://prepareias.in/api/integration/prelims/test/prev/"+urlN);
                        if(urlCalls.size() == pageNumber+1){
                            urlCalls.add("http://prepareias.in/api/integration/prelims/test/next/"+urlN);
                        }
                        displayQuestion(qlist.get(questionNumber));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //hidePDialog();

            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(quesReq);
    }

    public void displayQuestion(QuestionObject ques){
        hidePDialog();
        Log.i(TAG, "id : " +ques.getId());
        question.loadData(ques.getQuestion(), "text/html", "UTF-8");
        optionA.setText(ques.getOption1());
        optionB.setText(ques.getOption2());
        optionC.setText(ques.getOption3());
        optionD.setText(ques.getOption4());
        description.loadData(ques.getQuestion(), "text/html", "UTF-8");
        description.setVisibility(View.INVISIBLE);
        if(pageNumber < 1&&questionNumber==0){
            prev.setEnabled(false);
        }
    }
}
