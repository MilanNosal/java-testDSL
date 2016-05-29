package language.model;

/**
 * Interface for questions.
 * @author Milan
 */
public interface Question {
    default void addAnswer(Answer answer) {
        throw new RuntimeException("This object does not support adding answers!");
    }
    
    default void addPair(MatchingPair pair) {
        throw new RuntimeException("This object does not support adding matching pairs!");
    }
    
    String toHTML(String id);
    
    String toJS(String id);
    
    int getPoints();
}
