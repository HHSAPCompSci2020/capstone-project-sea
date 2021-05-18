package network;
import java.io.Serializable;

/**
 * A data object sent over the network between clients and servers.
 * 
 * @author Eric Chang
 * @author John Shelby
 */
public class DataObject implements Serializable {
	private static final long serialVersionUID = -8218172955135337113L;
	
	/**
	 * Sent by the client and server when a connection is established.
	 */
	public static final String HANDSHAKE = "HANDSHAKE";
	
	/**
	 * Sent by the client to request for the server's network information.
	 */
	public static final String INFORMATION = "INFORMATION";
	
	/**
	 * Sent by the client and server when a client changes their name.
	 */
	public static final String NAME_CHANGE = "NAME_CHANGE";
	
	/**
	 * Sent by the client and server when a chat message is sent.
	 */
	public static final String MESSAGE = "MESSAGE";
	
	/**
	 * Sent by the client and server when the game starts.
	 */
	public static final String START = "START";
	
	/**
	 * Sent by the server when a player's turn starts.
	 */
	public static final String TURN = "TURN";
	
	/**
	 * Sent by the client when the player plays a card.
	 */
	public static final String PLAY = "PLAY";
	
	/**
	 * Sent by the client and the server when the player draws a card.
	 */
	public static final String DRAW = "DRAW";
	
	/**
	 * Sent by the server when the game ends.
	 */
	public static final String END = "END";
	
	/**
	 * Sent by the client and server when it disconnects.
	 */
	public static final String DISCONNECT = "DISCONNECT";
	
	/**
	 * The type of message sent in this data object.
	 */
	public String messageType;
	
	/**
	 * Additional data sent in this data object.
	 */
	public Object[] message;

}
