package prepareias.tychestudios.com.prepareias;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAffairSectionFragment extends Fragment {
    private static final String TAG = GeneralStudiesSection.class.getSimpleName();
    private static final String url = "http://prepareias.in/api/integration/gs/categories";
    private ProgressDialog pDialog;
    private List<ListModel> sectionList = new ArrayList<ListModel>();
    private ListView listView;
    private CustomListAdapter adapter;
    private ListModel topTen, monthWise, categoryWise ;

    public CurrentAffairSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_studies_section, container, false);
        listView = (ListView)view.findViewById(R.id.gslist);
        return view;
    }

    @Override
    public void onViewCreated( View view,
                               Bundle savedInstanceState ) {

        adapter = new CustomListAdapter(getActivity(), sectionList);
        listView.setAdapter(adapter);


        topTen = new ListModel() ;
        monthWise = new ListModel() ;
        categoryWise = new ListModel() ;

        //First item in list
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

        // notifying list adapter about data changes
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Ohh Yeah !! the position = " + position + " and id = " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
