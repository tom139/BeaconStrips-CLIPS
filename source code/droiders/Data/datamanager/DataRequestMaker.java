package Data.datamanager;

import android.content.Context;

/**
 * Created by andrea on 16/07/16.
 */
public class DataRequestMaker {
   //Questo codice sarà sotto commento finché non verranno implementate le classi chiamate
   public static void getBuildings(Context cx, double latitude, double longitude, int maxBuildings, AbstractDataManagerListener<Data.Building[]> listener) {
      BuildingsDataRequest request = new BuildingsDataRequest(cx, latitude, longitude, maxBuildings, listener);
   }

   public static void getPath(Context cx, int pathID, AbstractDataManagerListener<Data.Path> listener) {
      PathDataRequest request = new PathDataRequest(cx, pathID, listener);
   }

   public static void getRanking(Context cx, int pathID, AbstractDataManagerListener<Data.PlayerRanking[]> listener) {
      GetRankingDataRequest request = new GetRankingDataRequest(cx, pathID, listener);
   }

   public static void getResults(Context cx, AbstractDataManagerListener<Data.PathResult[]> listener) {
      GetResultsDataRequest request = new GetResultsDataRequest(cx, listener);
   }

   public static void saveResult(Context cx, Data.PathResult pathResult, AbstractDataManagerListener<Boolean> listener) {
      SaveResultDataRequest request = new SaveResultDataRequest(cx, pathResult, listener);
   }

   public static void getAppInfo(Context cx, AbstractDataManagerListener<Data.AppInfo> listener) {
      AppInfoDataRequest request = new AppInfoDataRequest(cx, listener);
   }
}
