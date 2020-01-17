package todo.service.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
        super(message);
    }
}