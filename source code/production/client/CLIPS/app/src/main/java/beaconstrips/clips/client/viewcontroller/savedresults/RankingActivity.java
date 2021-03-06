/**
 * @file RankingActivity.java
 * @date 2016-07-20
 * @version 1.50
 * @author Matteo Franco
 * Gestisce l'activity della classifica
 */

package beaconstrips.clips.client.viewcontroller.savedresults;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import beaconstrips.clips.R;
import beaconstrips.clips.client.data.Score;
import beaconstrips.clips.client.data.datamanager.AbstractDataManagerListener;
import beaconstrips.clips.client.data.datamanager.DataRequestMaker;
import beaconstrips.clips.client.urlrequest.ServerError;
import beaconstrips.clips.client.viewcontroller.utility.RankingAdapter;

public class RankingActivity extends AppCompatActivity {
    private ListView listView;
    private String TAG = "RankingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        listView = (ListView) findViewById(R.id.playerList);
        Intent i = getIntent();
        int pathId = i.getIntExtra("pathId", 0);

        final ArrayList<Score> lista = new ArrayList<Score>();

        //justifyListViewHeightBasedOnChildren(listView, lista.size());

        DataRequestMaker.getRanking(getApplicationContext(), pathId, new AbstractDataManagerListener<Score[]>() {
            public void onResponse(Score[] response) {

                for(int i=0; i<response.length; i++) {
                    lista.add(new Score(response[i].username, response[i].position, response[i].score));
                }


                listView.setAdapter(new RankingAdapter(getApplicationContext(), lista));

            }
            public void onError(ServerError error) {
                Log.d("GetRankingDataTest", "Rilevato un errore in getRanking():");
                Log.d("GetRankingDataTest", "Codice dell'errore: " + error.errorCode);
                Log.d("GetRankingDataTest", "Messaggio per l'utente: " + error.userMessage);
                Log.d("GetRankingDataTest", "Messaggio di debug: " + error.debugMessage);
            }
        });

    }

    // setta la dimensione della listView in base a quanti elementi ci sono nella lista
    public static void justifyListViewHeightBasedOnChildren (ListView listView, Integer rows) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = rows*200;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
