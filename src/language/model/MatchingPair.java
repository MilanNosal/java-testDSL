package language.model;

/**
 * Matching pair of answers for the matching pairs question type.
 * @author Milan
 */
public class MatchingPair {
    
    private String left;
    
    private String right;

    public MatchingPair(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "MatchingPair{" + "left=" + left + ", right=" + right + '}';
    }

}
