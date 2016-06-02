package language.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple error linker.
 */
public class ErrorHandlingUtils {

    /**
     * Expression builder class for finding the right StackTraceElement object
     * in the stack trace.
     */
    private final Class expressionBuilderClass;

    /**
     * Hashtable that maps each created model object to a StackTraceElement object
     * representing a creation method call that created the given object.
     */
    private final Map<Object, StackTraceElement> debugInfo = new HashMap<Object, StackTraceElement>();

    public ErrorHandlingUtils(Class expressionBuilderClass) {
        this.expressionBuilderClass = expressionBuilderClass;
    }

    /**
     * Each created model object has to be registered.
     * The registration creates an exception to get current stack trace with
     * method calls. Using the class of the expression builder the method
     * finds a StackTraceElement object for the proper creation method that created
     * registered model object. The found StackTraceElement object is than
     * put into the hashtable.
     * @param <T>
     * @param object
     * @return 
     */
    public <T> T registerObject(T object) {
        Throwable exp = new Throwable();
        StackTraceElement[] stes = exp.getStackTrace();
        for (int i = 0; i < stes.length; i++) {
            StackTraceElement ste = stes[i];
            if (ste.getClassName().endsWith("." + expressionBuilderClass.getSimpleName())) {
                debugInfo.put(object, stes[i]);
                break;
            }
        }
        return object;
    }

    /**
     * When an error is reported the error is accompanied by the model object
     * that caused the error. Then this utilities class prints a given
     * StackTraceElement object that will point to the line where the model 
     * object was created.
     * @param <T>
     * @param cause
     * @param exception
     * @throws T 
     */
    public <T extends Exception> void reportError(Object cause, T exception) throws T {
        System.err.println(exception.getMessage());
        System.err.println("\t at " + debugInfo.get(cause).toString());
        //throw exception;
    }
}
