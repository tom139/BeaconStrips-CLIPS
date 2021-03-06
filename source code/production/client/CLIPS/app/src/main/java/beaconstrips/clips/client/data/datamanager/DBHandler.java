package beaconstrips.clips.client.data.datamanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;

import beaconstrips.clips.client.data.Beacon;
import beaconstrips.clips.client.data.Building;
import beaconstrips.clips.client.data.Path;
import beaconstrips.clips.client.data.PathInfo;
import beaconstrips.clips.client.data.PathResult;
import beaconstrips.clips.client.data.Proof;
import beaconstrips.clips.client.data.ProofResult;
import beaconstrips.clips.client.data.Proximity;
import beaconstrips.clips.client.data.Step;
import beaconstrips.clips.client.data.Utility;

/**
 * @file DBHandler.java
 * @date 25/07/16
 * @version 1.0.0
 * @author Enrico Bellio
 *
 * classe per la gestione del database locale
 */
public class DBHandler extends SQLiteOpenHelper {

   private static final String DB_NAME = "clipsDB";
   private static final int DB_VERSION = 1;

   public DBHandler(Context cx){
      super(cx,DB_NAME,null,DB_VERSION);
   }

   private void createBeaconTable(SQLiteDatabase db){
      String CREATE_BEACON_TABLE = "CREATE  TABLE  IF NOT EXISTS " +
              "Beacon (id INTEGER PRIMARY KEY  NOT NULL  UNIQUE , UUID VARCHAR NOT NULL, major INTEGER NOT NULL , minor INTEGER NOT NULL )";

      db.execSQL(CREATE_BEACON_TABLE);
   }

   private void createBuildingTable(SQLiteDatabase db){
      String CREATE_BUILDING_TABLE = "CREATE  TABLE  IF NOT EXISTS " +
              "Building (id INTEGER PRIMARY KEY  NOT NULL  UNIQUE , name TEXT NOT NULL , description TEXT, otherInfos TEXT," +
              " openingTime TEXT, address TEXT, latitude DOUBLE NOT NULL , longitude DOUBLE NOT NULL , telephone TEXT, email TEXT, whatsapp TEXT," +
              " telegram TEXT, twitter TEXT, facebook TEXT, websiteURL TEXT UNIQUE, image TEXT)";

      db.execSQL(CREATE_BUILDING_TABLE);
   }

   private void createPathTable(SQLiteDatabase db){
      String CREATE_PATH_TABLE = "CREATE  TABLE  IF NOT EXISTS" +
              " Path (id INTEGER PRIMARY KEY  NOT NULL  UNIQUE , startingMessage TEXT, rewardMessage TEXT)";

      db.execSQL(CREATE_PATH_TABLE);
   }

   private void createPathInfoTable(SQLiteDatabase db){
      String CREATE_PATHINFO_TABLE = "CREATE TABLE IF NOT EXISTS" +
              " PathInfo (pathID INTEGER PRIMARY KEY  NOT NULL  UNIQUE , buildingID INTEGER NOT NULL," +
              " title TEXT NOT NULL , description TEXT NOT NULL , target TEXT NOT NULL , estimatedDuration TEXT NOT NULL, position INTEGER NOT NULL," +
              " FOREIGN KEY(pathID) REFERENCES Path(id), FOREIGN KEY(buildingID) REFERENCES Building(id))";

      db.execSQL(CREATE_PATHINFO_TABLE);
   }

   private void createStepTable(SQLiteDatabase db){
      String CREATE_STEP_TABLE = "CREATE TABLE  IF NOT EXISTS" +
              " Step (id INTEGER PRIMARY KEY  NOT NULL  UNIQUE , stopBeaconID INTEGER NOT NULL ," +
              " proofID INTEGER NOT NULL , pathID INTEGER NOT NULL, helpText TEXT NOT NULL," +
              " FOREIGN KEY(stopBeaconID) REFERENCES Beacon(id), FOREIGN KEY(pathID) REFERENCES Path(id)," +
              " FOREIGN KEY(proofID) REFERENCES Proof(id))";

      db.execSQL(CREATE_STEP_TABLE);
   }

   private void createProofTable(SQLiteDatabase db){
      //TODO controllo campi tabella
      String CREATE_PROOF_TABLE = "CREATE  TABLE  IF NOT EXISTS" +
              " Proof (id INTEGER PRIMARY KEY  NOT NULL  UNIQUE ," +
              " stepID INTEGER NOT NULL UNIQUE, title VARCHAR NOT NULL , instructions TEXT NOT NULL , scoringAlgorithmData TEXT NOT NULL , " +
              " testData TEXT NOT NULL)";

      db.execSQL(CREATE_PROOF_TABLE);
   }

   private void createProximityTable(SQLiteDatabase db){
      String CREATE_PROXIMITY_TABLE = "CREATE  TABLE  IF NOT EXISTS" +
              " Proximity (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL  UNIQUE , beaconID INTEGER NOT NULL ," +
              " stepID INTEGER NOT NULL , percentage FLOAT NOT NULL , textToDisplay VARCHAR NOT NULL," +
              " FOREIGN KEY(beaconID) REFERENCES Beacon(id), FOREIGN KEY(stepID) REFERENCES Step(id) )";

      db.execSQL(CREATE_PROXIMITY_TABLE);
   }

   private void createPathResultTable(SQLiteDatabase db){
      String CREATE_PATHRESULT_TABLE = "CREATE  TABLE  IF NOT EXISTS" +
              " PathResult (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, startTime TEXT NOT NULL," +
              " endTime TEXT NOT NULL, buildingName VARCHAR NOT NULL, pathName VARCHAR NOT NULL, pathID INTEGER NOT NULL)";

      db.execSQL(CREATE_PATHRESULT_TABLE);
   }

   private void createProofResultTable(SQLiteDatabase db){
      String CREATE_PROOFRESULT_TABLE = "CREATE TABLE IF NOT EXISTS" +
              " ProofResult (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, pathResultID INTEGER NOT NULL, startTime TEXT NOT NULL ," +
              " endTime TEXT NOT NULL, score INTEGER NOT NULL, proofID INTEGER NOT NULL, FOREIGN KEY(pathResultID) REFERENCES PathResult(id))";

      db.execSQL(CREATE_PROOFRESULT_TABLE);
   }


   @Override
   public void onCreate(SQLiteDatabase db) {
      createBeaconTable(db);
      createBuildingTable(db);
      createProofTable(db);
      createProximityTable(db);
      createPathTable(db);
      createPathInfoTable(db);
      createStepTable(db);
      createPathResultTable(db);
      createProofResultTable(db);
   }

   @Override
   //TODO db onUpgrade
   public void onUpgrade(SQLiteDatabase db, int i, int i1) {
   }

   public void writeBeacons(Beacon[] beacons){
      for(int i=0; i<beacons.length; ++i){
         writeBeacon(beacons[i]);
      }
   }

   private void writeBeacon(Beacon b){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("id", b.id);
      values.put("UUID", b.UUID);
      values.put("major", b.major);
      values.put("minor", b.minor);

      db.insert("Beacon", null, values);
      db.close();
   }

   public void writeBuildings(Building[] buildings){
      for(int i=0; i<buildings.length; ++i){
         writeBuilding(i, buildings[i]);
      }
   }

   private void writeBuilding(int id, Building b){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("id", id);
      values.put("name", b.name);
      values.put("description", b.description);
      values.put("otherInfos", b.otherInfos);
      values.put("openingTime", b.openingTime);
      values.put("address", b.address);
      values.put("latitude", b.latitude);
      values.put("longitude", b.longitude);
      values.put("telephone", b.telephone);
      values.put("email", b.email);
      values.put("whatsapp", b.whatsapp);
      values.put("telegram", b.telegram);
      values.put("twitter", b.twitter);
      values.put("facebook", b.facebook);
      values.put("websiteURL", b.websiteURL);
      values.put("image", b.image);

      db.insert("Building", null, values);
      db.close();

      //write PathInfos dell'edificio
      writePathInfos(id, b.pathsInfos);
   }

   public void writePaths(Path[] paths){
      for(int i=0; i<paths.length; ++i){
         writePath(paths[i]);
      }
   }

   private void writePath(Path p){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("id", p.id);
      values.put("startingMessage", p.startingMessage);
      values.put("rewardMessage", p.rewardMessage);

      db.insert("Path", null, values);

      db.close();

      //aggiunge al db gli step del path
      writeSteps(p.id,p.steps);
   }

   public void writePathInfos(int buildingID, ArrayList<PathInfo> pathInfos){
      for(int i=0; i<pathInfos.size(); ++i){
         writePathInfo(buildingID, pathInfos.get(i));
      }
   }

   private void writePathInfo(int buildingID, PathInfo pi){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("pathID", pi.id);
      values.put("buildingID", buildingID);
      values.put("title", pi.title);
      values.put("description", pi.description);
      values.put("target", pi.target);
      values.put("estimatedDuration", pi.estimatedDuration);
      values.put("position", pi.position);

      db.insert("PathInfo", null, values);
      db.close();
   }

   private int getLastStepID(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT id FROM Step ORDER BY id DESC LIMIT 1",null); //Ritorna l'id dell'ultimo step inserito

      int ret = -1;

      while(cursor.moveToNext()){
         ret = Integer.parseInt(cursor.getString(0));
      }
      return ret;
   }

   public void writeSteps(int pathID,ArrayList<Step> steps){
      int lastStepID = getLastStepID();
      for(int i=lastStepID+1; i<(lastStepID == -1?(steps.size()):(steps.size()+lastStepID+1)); ++i){
         writeStep(pathID, i, steps.get(i%steps.size()));
      }
   }

   public void writeStep(int pathID, int stepID, Step s){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("id", stepID);
      values.put("stopBeaconID", s.stopBeacon.id);
      values.put("pathID", pathID);
      values.put("proofID", s.proof.id);
      values.put("helpText", s.helpText);

      db.insert("Step", null, values);
      db.close();

      //write stopBeacon
      writeBeacon(s.stopBeacon);

      //write Proximities
      writeProximities(stepID, s.proximities);

      //write Proof
      writeProof(stepID, s.proof);
   }

   public void writeProximities(int stepID, ArrayList<Proximity> proximities){
      for(int i=0; i<proximities.size(); ++i){
         writeProximity(stepID, proximities.get(i));
      }
   }

   private void writeProximity(int stepID, Proximity p){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("beaconID", p.beacon.id);
      values.put("textToDisplay", p.textToDisplay);
      values.put("percentage", p.percentage);
      values.put("stepID", stepID);

      db.insert("Proximity", null, values);
      db.close();
   }

   public void writeProof(int stepID, Proof p){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put("id", p.id);
      values.put("stepID", stepID);
      values.put("title", p. title);
      values.put("instructions", p.instructions);
      values.put("scoringAlgorithmData", p.algorithmJSON.toString());
      values.put("testData", p.testJSON.toString());

      db.insert("Proof", null, values);
      db.close();
   }

   public void writePathResults(PathResult[] pathResults){
      for(int i=0; i<pathResults.length; ++i){
         writePathResult(pathResults[i]);
      }
   }

   private void writePathResult(PathResult pr){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      Calendar sTime = pr.startTime;
      Calendar eTime = pr.endTime;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");

      values.put("startTime", sdf.format(sTime.getTime()));
      values.put("endTime", sdf.format(eTime.getTime()));
      values.put("buildingName", pr.buildingName);
      values.put("pathName", pr.pathName);
      values.put("pathID", pr.pathID);

      db.insert("PathResult", null, values);
      db.close();

      //aggiunta ProofResult relativi
      writeProofResults(pr.proofResults);
   }

   private int getLastPathResultID(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT id FROM PathResult ORDER BY id DESC LIMIT 1",null); //Ritorna l'id dell'ultimo PathResult inserito

      int ret = -1;

      while(cursor.moveToNext()){
         ret = Integer.parseInt(cursor.getString(0));
      }
      return ret;
   }

   public void writeProofResults(ArrayList<ProofResult> proofResults){
      int pathResultID = getLastPathResultID();
      for(int i=0; i<proofResults.size(); ++i){
         writeProofResult(pathResultID, proofResults.get(i));
      }
   }

   private void writeProofResult(int pathResultID, ProofResult pr){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();

      Calendar sTime = pr.startTime;
      Calendar eTime = pr.endTime;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");

      values.put("pathResultID", pathResultID);
      values.put("startTime", sdf.format(sTime.getTime()));
      values.put("endTime", sdf.format(eTime.getTime()));
      values.put("score", pr.score);
      values.put("proofID", pr.id);

      db.insert("ProofResult", null, values);
      db.close();
   }

   public ArrayList<Beacon> readBeacons(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Beacon", null, null, null, null, null, null, null); //Ritorna tutti i beacon salvati nel DB

      ArrayList<Beacon> ret = new ArrayList<Beacon>();

      while(cursor.moveToNext()){
         int id = Integer.parseInt(cursor.getString(0));
         String UUID = cursor.getString(1);
         int major = Integer.parseInt(cursor.getString(2));
         int minor = Integer.parseInt(cursor.getString(3));
         ret.add(new Beacon(id, UUID, major, minor));
      }
      return ret;
   }

   public ArrayList<Step> readAllSteps(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Step", null, null, null, null, null, null, null); //Ritorna tutti gli step salvati nel DB

      ArrayList<Step> ret = new ArrayList<Step>();

      while(cursor.moveToNext()){
         int id = Integer.parseInt(cursor.getString(0));
         Beacon stopBeacon = readBeacon(Integer.parseInt(cursor.getString(1)));
         ArrayList<Proximity> proximities = readProximities(id);
         Proof proof = readProof(Integer.parseInt(cursor.getString(2)));
         String helpText = cursor.getString(4);

         ret.add(new Step(stopBeacon, proximities, proof, helpText));
      }
      return ret;
   }

   public ArrayList<Proximity> readAllProximities(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Proximity", null, null, null, null, null, null, null); //Ritorna tutti i proximity salvati nel DB

      ArrayList<Proximity> ret = new ArrayList<Proximity>();

      while(cursor.moveToNext()){
         Beacon beacon = readBeacon(Integer.parseInt(cursor.getString(1)));
         float percentage = Float.parseFloat(cursor.getString(3));
         String textToDisplay = cursor.getString(4);

         ret.add(new Proximity(beacon,percentage,textToDisplay));
      }
      return ret;
   }

   public ArrayList<Path> readPaths(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Path", null, null, null, null, null, null, null); //Ritorna tutti i path salvati nel DB

      ArrayList<Path> ret = new ArrayList<Path>();

      while(cursor.moveToNext()){

         int id = Integer.parseInt(cursor.getString(0));
         String startingMessage = cursor.getString(1);
         String rewardMessage = cursor.getString(2);
         ArrayList<Step> steps = readSteps(id);

         ret.add(new Path(id, startingMessage, rewardMessage, steps));
      }
      return ret;
   }

   public ArrayList<Building> readBuildings(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Building", null, null, null, null, null, null, null); //Ritorna tutti gli edifici salvati nel DB

      ArrayList<Building> ret = new ArrayList<Building>();

      while(cursor.moveToNext()){
         int id = Integer.parseInt(cursor.getString(0));
         String name = cursor.getString(1);
         String description = cursor.getString(2);
         String otherInfos = cursor.getString(3);
         String openingTime = cursor.getString(4);
         String address = cursor.getString(5);
         double latitude = Double.parseDouble(cursor.getString(6));
         double longitude = Double.parseDouble(cursor.getString(7));
         String telephone = cursor.getString(8);
         String email = cursor.getString(9);
         String whatsapp = cursor.getString(10);
         String telegram = cursor.getString(11);
         String twitter = cursor.getString(12);
         String facebook = cursor.getString(13);
         ArrayList<PathInfo> pathsInfos = readPathInfos(id);
         String websiteURL = cursor.getString(14);
         String image = cursor.getString(15);

         ret.add(new Building(name,image,description,otherInfos,openingTime,address,latitude,longitude,telephone,email,whatsapp,telegram,twitter,facebook,websiteURL,pathsInfos));
      }

      return ret;
   }

   private ArrayList<PathInfo> readPathInfos(int buildingID){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("PathInfo", null, "buildingID =?", new String[]{String.valueOf(buildingID)}, null, null, null, null);

      ArrayList<PathInfo> ret = new ArrayList<PathInfo>();

      while(cursor.moveToNext()){
         int id = Integer.parseInt(cursor.getString(0));
         String title = cursor.getString(2);
         String description = cursor.getString(3);
         String target = cursor.getString(4);
         String estimatedDuration = cursor.getString(5);
         int position = Integer.parseInt(cursor.getString(6));

         ret.add(new PathInfo(id,title,description,target,estimatedDuration, position));
      }
      return ret;
   }

   public Path readPath(int pathID) {
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Path", null, "id=?", new String[]{String.valueOf(pathID)}, null, null, null, null);
      
      Path ret = null;

      if (cursor.getCount() > 0) {
         cursor.moveToFirst();

         int id = Integer.parseInt(cursor.getString(0));
         String startingMessage = cursor.getString(1);
         String rewardMessage = cursor.getString(2);
         ArrayList<Step> steps = readSteps(pathID);

         ret = new Path(id, startingMessage, rewardMessage, steps);
      }

      return ret;
   }

   private ArrayList<Step> readSteps(int pathID) {
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM Step WHERE pathID=? ORDER BY id ASC", new String[]{String.valueOf(pathID)});

      ArrayList<Step> ret = new ArrayList<Step>();

      while(cursor.moveToNext())
      {
         int id = Integer.parseInt(cursor.getString(0));
         Beacon stopBeacon = readBeacon(Integer.parseInt(cursor.getString(1)));
         ArrayList<Proximity> proximities = readProximities(id);
         Proof proof = readProof(Integer.parseInt(cursor.getString(2)));
         String helpText = cursor.getString(4);

         ret.add(new Step(stopBeacon, proximities, proof, helpText));
      }
      return ret;
   }

   public Beacon readBeacon(int beaconID){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Beacon", null, "id =?", new String[]{String.valueOf(beaconID)}, null, null, null, null);

      Beacon ret = null;

      if(cursor.getCount() > 0){
         cursor.moveToFirst();

         String UUID = cursor.getString(1);
         int major = Integer.parseInt(cursor.getString(2));
         int minor = Integer.parseInt(cursor.getString(3));

         ret = new Beacon(beaconID, UUID, major, minor);
      }

      return ret;
   }

   private ArrayList<Proximity> readProximities(int stepID){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Proximity", null, "stepID =?", new String[]{String.valueOf(stepID)}, null, null, null, null);

      ArrayList<Proximity> ret = new ArrayList<Proximity>();

      while(cursor.moveToNext()){

         Beacon beacon = readBeacon(Integer.parseInt(cursor.getString(1)));
         float percentage = Float.parseFloat(cursor.getString(3));
         String textToDisplay = cursor.getString(4);


         ret.add(new Proximity(beacon,percentage,textToDisplay));
      }

      return ret;
   }

   public Proof readProof(int proofID){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Proof", null, "id =?", new String[]{String.valueOf(proofID)}, null, null, null, null);

      Proof ret = null;

      if (cursor.getCount() > 0) {
         cursor.moveToFirst();

         int id = Integer.parseInt(cursor.getString(0));
         String title = cursor.getString(2);
         String instructions = cursor.getString(3);
         JSONObject algorithmData = new JSONObject();
         try {
            algorithmData = new JSONObject(cursor.getString(4));
         }
         catch(JSONException e){
            Log.d("DBHandler", "Errore conversione JSONObject - algorithmData");
         }
         JSONObject testData = new JSONObject();
         try {
            testData = new JSONObject(cursor.getString(5));
         }
         catch(JSONException e){
            Log.d("DBHandler", "Errore conversione JSONObject - testData");
         }

         ret = new Proof(id, title, instructions, algorithmData, testData);
      }

      return ret;
   }

   public ArrayList<Proof> readProofs() {
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("PathResult", null, null, null, null, null, null, null);

      ArrayList<Proof> proofs = new ArrayList<Proof>();

      while(cursor.moveToNext()){
         int id = Integer.parseInt(cursor.getString(0));
         String title = cursor.getString(1);
         String instructions = cursor.getString(2);
         JSONObject algorithmData = new JSONObject();
         JSONObject testData = new JSONObject();
         try{
            algorithmData = new JSONObject(cursor.getString(4));
            testData = new JSONObject(cursor.getString(5));
         }
         catch (JSONException e) {
            Log.d("DBHandlerLog", "Errore conversione String to JSONObject");
         }

         proofs.add(new Proof(id, title, instructions, algorithmData, testData));
      }

      return proofs;
   }

   public PathResult[] readPathResults(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("PathResult", null, null, null, null, null, null, null);

      ArrayList<PathResult> pathResults = new ArrayList<PathResult>();

      while(cursor.moveToNext()){
         int pathID = Integer.parseInt(cursor.getString(5));
         int id=  Integer.parseInt(cursor.getString(0));
         GregorianCalendar startTime = Utility.stringToGregorianCalendar(cursor.getString(1));
         GregorianCalendar endTime = Utility.stringToGregorianCalendar(cursor.getString(2));
         String buildingName = cursor.getString(3);
         String pathName = cursor.getString(4);
         ArrayList<ProofResult> proofsResults = readProofResults(id);
         int totalScore = Utility.calculateTotalScore(proofsResults);

         pathResults.add(new PathResult(pathID, pathName, buildingName, startTime, endTime, totalScore, proofsResults));
      }

      return pathResults.toArray(new PathResult[pathResults.size()]);
   }

   private ArrayList<ProofResult> readProofResults(int pathResultID){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("ProofResult", null, "pathResultID=?", new String[]{String.valueOf(pathResultID)}, null, null, null, null);

      ArrayList<ProofResult> ret = new ArrayList<ProofResult>();

      while(cursor.moveToNext()){
         int proofID = Integer.parseInt(cursor.getString(5));
         GregorianCalendar startTime = Utility.stringToGregorianCalendar(cursor.getString(2));
         GregorianCalendar endTime = Utility.stringToGregorianCalendar(cursor.getString(3));
         int score = Integer.parseInt(cursor.getString(4));

         ret.add(new ProofResult(proofID, startTime, endTime, score));
      }

      return ret;
   }

   public void deleteAllPaths(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("Path", null, null);
      db.close();
   }

   public void deleteAllSteps(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("Step", null, null);
      db.close();
   }

   public void deleteAllProximities(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("Proximity", null, null);
      db.close();
   }

   public void deleteAllBuildings(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("Building", null, null);
      db.close();

      deleteAllPathInfos();
   }

   public void deleteAllPathInfos(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("PathInfo", null, null);
      db.close();
   }

   public void deletePathResults(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("PathResult", null, null);
      db.close();

      deleteAllProofsResults();
   }

   public void deleteAllProofsResults(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("ProofResult", null, null);
      db.close();
   }

   public void deleteAllBeacons(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("Beacon", null, null);
      db.close();
   }

   public void deleteAllProofs(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("Proof", null, null);
      db.close();
   }

   public void updatePath(Path p){
      //deletePath(p.id);
      deleteAllPaths();
      deleteAllSteps();
      deleteAllProximities();
      deleteAllBeacons();
      deleteAllProofs();
      writePath(p);
   }

   public void updateBuildings(Building[] buildings){
      deleteAllBuildings();
      writeBuildings(buildings);
   }

   public void updatePathResults(PathResult[] pr){
      deletePathResults();
      writePathResults(pr);
   }

   public Building[] getNearestBuildings(float param, boolean searchByDistance, double userLatitude, double userLongitude){

      ArrayList<Building> buildings = readBuildings();
      Building[] ret = null;

      if(buildings.size() > 0){
         if(searchByDistance){
            ret = Utility.getBuildingsByDistance(buildings, param, userLatitude, userLongitude);
         }
         else{
            ret = Utility.getBuildingsByNumber(buildings, (int)param, userLatitude, userLongitude);
         }
      }

      return ret;
   }

}
