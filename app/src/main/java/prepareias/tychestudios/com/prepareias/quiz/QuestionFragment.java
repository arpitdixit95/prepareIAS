package prepareias.tychestudios.com.prepareias.quiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import prepareias.tychestudios.com.prepareias.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    private static final String TAG = QuestionFragment.class.getSimpleName();
    private QuestionObject ques;
    private static final String key = "QuesKey" ;
    TextView question;




    public static QuestionFragment newInstance(QuestionObject que) {
        // Required empty public constructor
        QuestionFragment qf = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(key, (Serializable) que);
        qf.setQues(que);
        qf.setArguments(args);
        return qf;
    }
    public void setQues(QuestionObject ques){
        this.ques = ques;
    }
    public QuestionFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ques = (QuestionObject) getArguments().getSerializable(
                key);
        Log.i(TAG, " :: Dynamic List :: sectionList : " + ques.toString());
        question =(TextView)view.findViewById(R.id.questiontv);
        return view;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState){
        question.setText(ques.getId());
    }
}
