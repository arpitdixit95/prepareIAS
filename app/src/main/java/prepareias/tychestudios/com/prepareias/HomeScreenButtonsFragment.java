package prepareias.tychestudios.com.prepareias;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenButtonsFragment extends Fragment {

    private Button current_affairs, generalStudies, testSeries;
    public HomeScreenButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen_buttons, container, false);
    }

    @Override
    public void onViewCreated( View view,
                               Bundle savedInstanceState ) {
        current_affairs = (Button)getActivity().findViewById(R.id.current_affairs) ;
        generalStudies = (Button)getActivity().findViewById(R.id.generalStudies) ;
        testSeries = (Button)getActivity().findViewById(R.id.testSeries) ;
        current_affairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onArticleClicked(1);
            }
        });
        generalStudies.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onArticleClicked(2);
            }
        });

        testSeries.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onArticleClicked(3);
            }
        });
    }

    OnItemClickedListener mCallback;

    // Container Activity must implement this interface
    public interface OnItemClickedListener {
        public void onArticleClicked(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnItemClickedListener) context;
            Log.i("Fragment",",mCallback:"+mCallback);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    }
