package prepareias.tychestudios.com.prepareias;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayTopicFragment extends Fragment {
    private WebView tv;
    private String url;
    private ProgressDialog pDialog;
    private String content;


    private static final String TAG = DisplayTopicFragment.class.getSimpleName();

    public DisplayTopicFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(null != getArguments()){
            url = getArguments().getString("url");
        }
        Log.i(TAG, " :: url : " + url);
        View view = inflater.inflate(R.layout.fragment_display_topic, container, false);
        tv = (WebView)view.findViewById(R.id.topicDisplay);
        getContent();

        return view;
    }

    @Override
    public void onViewCreated( View view,
                               Bundle savedInstanceState ) {

    }
    public void getContent(){
        JsonObjectRequest obj = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, " :: TopicResponse : " + response.toString());
                        try {
                            content = response.getString("content");
                            Log.i(TAG, " :: content : " + content);
                            //content="Hi World";
                            tv.loadData(content, "text/html","UTF-8");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                });
                AppController.getInstance().addToRequestQueue(obj);
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
