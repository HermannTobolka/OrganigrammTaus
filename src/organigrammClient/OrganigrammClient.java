package organigrammClient;


import javax.swing.SwingUtilities;

public class OrganigrammClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final OrganigrammClientFrame ocf = new OrganigrammClientFrame();
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ocf.showFrame();
            }
        });
	}

}
