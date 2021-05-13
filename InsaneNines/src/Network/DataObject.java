package Network;
import java.io.Serializable;

public class DataObject implements Serializable {
	private static final long serialVersionUID = -8218172955135337113L;
	
	public static final String HANDSHAKE = "HANDSHAKE";
	
	public static final String INFORMATION = "INFORMATION";
	
	public static final String START = "START";
	
	public static final String TURN = "TURN";
	
	public static final String PLAY = "PLAY";
	
	public static final String DRAW = "DRAW";
	
	public static final String END = "END";
	
	public static final String DISCONNECT = "DISCONNECT";
	
	public String messageType;
	
	public Object[] message;

}
