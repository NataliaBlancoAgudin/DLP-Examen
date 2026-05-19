package errorhandler;

import ast.types.ErrorType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {

    private static final ErrorHandler ERROR_HANDLER = new ErrorHandler();
    List<ErrorType> errorTypeList = new ArrayList<>();

    private ErrorHandler() {}

    public static ErrorHandler getInstance(){
        return ERROR_HANDLER;
    }

    public boolean anyError() {
        return !errorTypeList.isEmpty();
    }

    public void showErrors(PrintStream err) {
        for(ErrorType error: errorTypeList){
            err.println(error.toString());
        }
    }

    public void addError(ErrorType errorType){
        errorTypeList.add(errorType);
    }
}
