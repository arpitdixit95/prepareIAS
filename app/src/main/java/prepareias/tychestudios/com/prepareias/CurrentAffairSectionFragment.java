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
public class CurrentAffairSectionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    private static final String TAG = CurrentAffairSectionFragment.class.getSimpleName();
    //private String url = "http://prepareias.in/api/integration/gs/categories";
    private String url = null;
    private String topicName="0";
    private int level = 0;
    private int pageno=1;
    private ProgressDialog pDialog;
    private List<ListModel> sectionList = new ArrayList<ListModel>();
    private ListView listView;
    private CustomListAdapter adapter;
    //private ListModel topTen, monthWise, categoryWise ;

    public CurrentAffairSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(null != getArguments()) {
            url = getArguments().getString("url");
            if(getArguments().getString("topicName_level") !=  null) {
                topicName = getArguments().getString("topicName_level").split(":", 2)[0];
                level = Integer.parseInt(getArguments().getString("topicName_level").split(":", 2)[1]);
            }
            if(getArguments().getString("pageno") != null){
                topicName="categories";
                level = 2;
                pageno = Integer.parseInt(getArguments().getString("pageno"));
            }
        }
        Log.i(TAG, "url : "+ url + " :: topicName ::"+ topicName + " :: level :: " + level);
        View view;
        if(topicName.equalsIgnoreCase("categories") && level == 2){
            view = inflater.inflate(R.layout.fragment_current_affair_section, container, false);
            swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(this);
            listView = (ListView)view.findViewById(R.id.calist);
            adapter = new CustomListAdapter(getActivity(), sectionList);
            listView.setAdapter(adapter);
            //Bundle b = getArguments();
        } else {
            view = inflater.inflate(R.layout.fragment_general_studies_section, container, false);
            listView = (ListView)view.findViewById(R.id.gslist);
            adapter = new CustomListAdapter(getActivity(), sectionList);
            listView.setAdapter(adapter);
            //Bundle b = getArguments();
        }

        return view;
    }

    @Override
    public void onViewCreated( View view,
                               Bundle savedInstanceState ) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        if(level == 0){
            sectionList.clear();
            staticList();
        } else if(level == 1){
            sectionList.clear();
            dynamicList();
        } else {
            sectionList.clear();
            dynamicList();
        }

        // notifying list adapter about data changes
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Ohh Yeah !! the position = " + position + " and id = " + id, Toast.LENGTH_SHORT).show();
                if(level == 0) {
                    switch (position) {
                        case 0:topicName = "recent:";
                            mCallback.onCACategoryClicked(position,topicName+String.valueOf(level+1));
                            break;
                        case 1:topicName = "categories:";
                            mCallback.onCACategoryClicked(position,topicName+String.valueOf(level+1));
                            break;
                        case 2:topicName = "monthly:";
                            mCallback.onCACategoryClicked(position,topicName+String.valueOf(level+1));
                            break;
                    }
                }else  {

                    mCallback.onCACategoryClicked(sectionList.get(position).getSerial(), topicName+":"+String.valueOf(level+1));
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
        url ="http://prepareias.in/api/integration/current_affair/"+position + "/"+pageno;
        dynamicList();
        swipeLayout.setRefreshing(false);

    }

    // Container Activity must implement this interface
    public interface OnItemClickedListener {
        public void onCACategoryClicked(int position, String topicName_level);
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
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        if(topicName.equalsIgnoreCase("categories") && level == 2 && response.length() <= 0)pageno--;


                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                ListModel topic = new ListModel();
                                topic.setTitle(obj.getString("name"));
                                topic.setSerial(Integer.parseInt(obj.getString("id")));
                                sectionList.add(topic);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        Log.i(TAG, " :: Dynamic List :: sectionList : "+ sectionList.toString());
                        // notifying list adapter about data changes
                        adapter.notifyDataSetChanged();
                        // so that it renders the list view with updated data
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    public void staticList(){
        Log.i(TAG, "Inside static List" );
        ListModel topTen = new ListModel() ;
        ListModel monthWise = new ListModel() ;
        ListModel categoryWise = new ListModel() ;

        //First item in list
        hidePDialog();
        topTen.setSerial(1);
        topTen.setTitle("Top Ten Current Affairs");
        sectionList.add(topTen) ;

        //Second item in list
        categoryWise.setTitle("Category Wise Current Affairs");
        categoryWise.setSerial(2);
        sectionList.add(categoryWise) ;

        //Third item in list
        monthWise.setSerial(3);
        monthWise.setTitle("Month Wise Current Affairs");
        sectionList.add(monthWise) ;
    }
}
