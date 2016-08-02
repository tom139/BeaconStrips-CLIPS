package data;

/**
 * Created by andrea on 15/07/16.
 */
public class MultipleChoiceText extends AbstractTextQuiz {
   public final String trueResponse; //Qui è contenuto il testo della risposta
   public final String falseResponse[]; //In teoria il numero di risposte che si possono inserire sono infinite, io intanto volevo inserirne 3 (4 in tutto), alla QuizDuello per intenderci

   public MultipleChoiceText(String helpText, String instructions, String trueResponse, String falseResponse[]) {
      super(helpText, instructions);
      this.trueResponse = trueResponse;
      this.falseResponse = falseResponse;
   }

   public boolean check(String userResponse) {
      return trueResponse.equals(userResponse);
   }
}