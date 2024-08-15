package dio.restapi2.service.exceptions;

public class NotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super("Resource not found.");
    }

}