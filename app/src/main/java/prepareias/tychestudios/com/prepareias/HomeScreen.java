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

public class HomeScreen extends AppCompatActivity implements HomeScreenButtonsFragment.OnItemClickedListener, CurrentAffairSectionFragment.OnItemClickedListener{

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
        CurrentAffairSectionFragment casf = new CurrentAffairSectionFragment();
        Bundle b = new Bundle();
        b.putString("url", null);
        b.putString("topicName_level","0:0" );
        casf.setArguments(b);
        if(position==1){
            ftemp.replace(R.id.fragment_placeholder, casf);
        }
        else if(position==2){
            ftemp.replace(R.id.fragment_placeholder, new GeneralStudiesSection());
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
        Log.i("on Category clicked", " position : " + position+ " topicName : " + topicName+" level : " + level);
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
                b.putString("url", "http://prepareias.in/api/integration/current_affair/"+position);
                ftemp.replace(R.id.fragment_placeholder,new DisplayTopicFragment());
            }
            else if(topicName.equals("categories")){
                b.putString("url", "http://prepareias.in/api/integration/current_affair/"+position+"/1");
                b.putString("topicName_level", topicName_level);
                casf.setArguments(b);
                ftemp.replace(R.id.fragment_placeholder,casf);
            }
        } else if(level == 3){
            if(topicName.equals("categories")){
                b.putString("url", "http://prepareias.in/api/integration/current_affair/"+position);
                ftemp.replace(R.id.fragment_placeholder,new DisplayTopicFragment());
            }
        }

        ftemp.addToBackStack(null);
        ftemp.commit();
    }

}
