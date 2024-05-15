
import java.io.*;
import java.util.ArrayList;


public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	static final int  MESSAGE = 1, LOGOUT = 2, SCORE = 3;
	private int type;
	private String message;
	
	
	ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
}

