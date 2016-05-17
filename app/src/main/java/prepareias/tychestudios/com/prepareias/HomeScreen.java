package prepareias.tychestudios.com.prepareias;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import prepareias.tychestudios.com.prepareias.quiz.QuestionFragment;
import prepareias.tychestudios.com.prepareias.quiz.QuizActivity;

public class HomeScreen extends AppCompatActivity implements HomeScreenButtonsFragment.OnItemClickedListener, CurrentAffairSectionFragment.OnItemClickedListener, GeneralStudiesSection.OnItemClickedListener{

    //private Button current_affairs, generalStudies ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new HomeScreenButtonsFragment());
        ft.commit();
    }

    @Override
    public void onArticleClicked(int position) {
        FragmentTransaction ftemp = getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        if(position==1){
            CurrentAffairSectionFragment casf = new CurrentAffairSectionFragment();
            b.putString("url", null);
            b.putString("topicName_level","0:0" );
            casf.setArguments(b);
            ftemp.replace(R.id.fragment_placeholder, casf);
        }
        else if(position==2){
            GeneralStudiesSection gss = new GeneralStudiesSection();
            b.putString("url", "http://prepareias.in/api/integration/gs/categories");
            b.putInt("level", 0);
            gss.setArguments(b);
            ftemp.replace(R.id.fragment_placeholder, gss);
        } else if(position==3){
            Intent i = new Intent(HomeScreen.this, QuizActivity.class);
            startActivity(i);
        }
        ftemp.addToBackStack(null);
        ftemp.commit();
    }

    @Override
    public void onCACategoryClicked(int position, String topicName_level) {
        FragmentTransaction ftemp = getSupportFragmentManager().beginTransaction();
        CurrentAffairSectionFragment casf = new CurrentAffairSectionFragment();
        Bundle b = new Bundle();
        String topicName = topicName_level.split(":", 2)[0];
        int level = Integer.parseInt(topicName_level.split(":", 2)[1]);
        Log.i("on Category clicked", " position : " + position + " topicName : " + topicName + " level : " + level);
        if(level == 1){
            if(topicName.equals("recent")){
                b.putString("url", "http://prepareias.in/api/integration/current_affair/recent");
                b.putString("topicName_level", topicName_level);
                casf.setArguments(b);
                ftemp.replace(R.id.fragment_placeholder,casf);
            }
            else if(topicName.equals("categories")){
                b.putString("url", "http://prepareias.in/api/integration/current_affair/categories");
                b.putString("topicName_level", topicName_level);
                casf.setArguments(b);
                ftemp.replace(R.id.fragment_placeholder,casf);
            }
            else if(topicName.equals("monthly")){
                b.putString("url", "http://prepareias.in/api/integration/current_affair/monthly");
                casf.setArguments(b);
            }
        }
        else if (level == 2){
            if(topicName.equals("recent")){
                DisplayTopicFragment dtf = new DisplayTopicFragment();
                b.putString("url", "http://prepareias.in/api/integration/current_affair/"+position);
                dtf.setArguments(b);
                ftemp.replace(R.id.fragment_placeholder,dtf);
            }
            else if(topicName.equals("categories")){
                b.putString("url", "http://prepareias.in/api/integration/current_affair/"+position+"/1");
                b.putString("topicName_level", topicName_level);
                casf.setArguments(b);
                ftemp.replace(R.id.fragment_placeholder,casf);
            }
        } else if(level == 3){
            if(topicName.equals("categories")){
                DisplayTopicFragment dtf = new DisplayTopicFragment();
                b.putString("url", "http://prepareias.in/api/integration/current_affair/"+position);
                dtf.setArguments(b);
                ftemp.replace(R.id.fragment_placeholder,dtf);
            }
        }

        ftemp.addToBackStack(null);
        ftemp.commit();
    }

    @Override
    public void onGSCategoryClicked(int position, int level, int catId) {
        FragmentTransaction ftemp = getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        if(level == 1){
            GeneralStudiesSection gss = new GeneralStudiesSection();
            b.putString("url", "http://prepareias.in/api/integration/gs/"+position+"/1");
            b.putInt("level", level);
            gss.setArguments(b);
            ftemp.replace(R.id.fragment_placeholder, gss);

        } else{
            DisplayTopicFragment dtf = new DisplayTopicFragment();
            b.putString("url", "http://prepareias.in/api/integration/gs/"+catId+"/content/"+position);
            dtf.setArguments(b);
            ftemp.replace(R.id.fragment_placeholder,dtf);
        }
        ftemp.addToBackStack(null);
        ftemp.commit();
    }

    public void handleQuestions(){

        fetchQuestion();
    }

    public void fetchQuestion(){

    }
}
