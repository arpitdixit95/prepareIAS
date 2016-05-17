package prepareias.tychestudios.com.prepareias;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralStudiesSection extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    private static final String TAG = GeneralStudiesSection.class.getSimpleName();
    private String url = "";
    private ProgressDialog pDialog;
    private int level = 0;
    private int pageno=1;
    private List<ListModel> topicList = new ArrayList<ListModel>();
    private ListView listView;
    private CustomListAdapter adapter;

    public GeneralStudiesSection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if(getArguments() != null){
            level = getArguments().getInt("level");
            url = getArguments().getString("url");
        }
        Log.i(TAG, "url : "+ url +" :: level :: " + level);
        if(level == 0){
            view = inflater.inflate(R.layout.fragment_general_studies_section, container, false);
            listView = (ListView)view.findViewById(R.id.gslist);
        } else {
            view = inflater.inflate(R.layout.fragment_current_affair_section, container, false);
            swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(this);
            listView = (ListView)view.findViewById(R.id.calist);
        }
        adapter = new CustomListAdapter(getActivity(), topicList);
        listView.setAdapter(adapter);



        return view;

    }

    @Override
    public void onViewCreated( View view,
                               Bundle savedInstanceState ){
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        topicList.clear();
        dynamicList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Ohh Yeah !! the position = " + position + " and id = " + id, Toast.LENGTH_SHORT).show();
                    if(level == 0) mCallback.onGSCategoryClicked(topicList.get(position).getSerial(), level + 1, 0);
                    else if(level == 1) {
                        String[] temp = url.split("/");
                        String pos = temp[temp.length - 2];
                        mCallback.onGSCategoryClicked(topicList.get(position).getSerial(), level+1,Integer.parseInt(pos));
                    }

            }
        });
    }

    OnItemClickedListener mCallback;

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        pageno++;
        String[] temp = url.split("/");
        String position = temp[temp.length - 2];
        url ="http://prepareias.in/api/integration/gs/"+position + "/"+pageno;
        Log.d(TAG, "onSwipe :: for url :" + url);
        dynamicList();
        swipeLayout.setRefreshing(false);

    }

    // Container Activity must implement this interface
    public interface OnItemClickedListener {
        public void onGSCategoryClicked(int position, int level, int catId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnItemClickedListener) context;
            Log.i("Fragment", ",mCallback:" + mCallback);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    public void dynamicList(){
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "for url :" + url +" :: response :"+response.toString());
                        hidePDialog();

                        // Parsing json
                        if(response.length() <= 0)pageno--;
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                ListModel topic = new ListModel();
                                topic.setTitle(obj.getString("name"));
                                topic.setSerial(Integer.parseInt(obj.getString("id")));
                                topicList.add(topic);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });
        /*{
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 24 * 60 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

        };*/
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
