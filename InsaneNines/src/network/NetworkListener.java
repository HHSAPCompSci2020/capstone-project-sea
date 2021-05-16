package network;

/**
 * A listener in the observer design pattern.
 * 
 * @author Eric Chang
 * @author John Shelby
 */
public interface NetworkListener {
	/**
	 * Called when data is received by the client this listener is a listener of.
	 * 
	 * @param data The data object that is received.
	 */
	public void messageReceived(DataObject data);
}
