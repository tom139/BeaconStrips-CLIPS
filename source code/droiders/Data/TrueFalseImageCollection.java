package Data;

import java.util.List;

/**
 * Created by andrea on 01/08/16.
 */
public class TrueFalseImageCollection extends TestCollection {
   List<TrueFalseImage> questions;

   public TrueFalseImageCollection(boolean shuffleQuestions, List<TrueFalseImage> questions) {
      super(shuffleQuestions);
      this.questions = questions;
   }
}
