package beaconstrips.clips.client.data;

import java.io.Serializable;

/**
 * @file PathInfo.java
 * @date 19/07/16
 * @version 1.0.0
 * @author Andrea Grendene
 *
 * classe che rappresenta le informazioni di un percorso da mostrare all'utente nella lista dei percorsi
 */
public class PathInfo implements Serializable{
   public final int id;
   public final String title;
   public final String description;
   public final String target;
   public final String estimatedDuration;
   public final int position;

   public PathInfo(int id, String title, String description, String target, String estimatedDuration, int position) {
      this.id = id;
      this.title = title;
      this.description = description;
      this.target = target;
      this.estimatedDuration = estimatedDuration;
      this.position = position;
   }

}
