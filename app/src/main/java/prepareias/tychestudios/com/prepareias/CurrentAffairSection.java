package prepareias.tychestudios.com.prepareias;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CurrentAffairSection extends Activity {

    // Movies json url
    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<ListModel> sectionList = new ArrayList<ListModel>();
    private ListView listView;
    private CustomListAdapter adapter;
    private ListModel topTen, monthWise, categoryWise ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_affair_section);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, sectionList);
        listView.setAdapter(adapter);

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
                Toast.makeText(getApplicationContext(), "Ohh Yeah !! the position = " + position + " and id = " + id, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_affair_sectiom, menu);
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
}
