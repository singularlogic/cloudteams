package eu.cloudteams.response;

/**
 *
 * @author Christos Paraskeva
 */
public  class ApplicationResponse <C extends Enum<? extends ResponseCode>,R >  {


    private C code;    
    private String message;     
    private R returnobject;
    
    public ApplicationResponse(C code, String message, R returnobject){
        this.message = message;
        this.code = code;
        this.returnobject = returnobject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public C getCode() {
        return code;
    }

    public void setCode(C code) {
        this.code = code;
    }

    public R getReturnobject() {
        return returnobject;
    }

    public void setReturnobject(R returnobject) {
        this.returnobject = returnobject;
    }

}
