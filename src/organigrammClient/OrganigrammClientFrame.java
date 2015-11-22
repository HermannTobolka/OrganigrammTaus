package organigrammClient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;





import organigrammClient.OrganigrammServer.Abteilung;

import organigrammClient.OrganigrammServer.Mitarbeiter;//organigrammClient.organigrammServer.Mitarbeiter;
import organigrammClient.OrganigrammServer.OrganigrammServer;
import organigrammClient.OrganigrammServer.OrganigrammServerService;





public class OrganigrammClientFrame {

	private JFrame frame;
	private OrganigrammServer port;
	private JTree orga;
	private DefaultTreeModel dtm;

	public OrganigrammClientFrame() {
		port = new OrganigrammServerService().getOrganigrammServerPort();
		//Create and set up the window.
		frame = new JFrame("Unternehmensorganigramm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		DefaultMutableTreeNode root = GetAbteilung(0, null);
		dtm = new DefaultTreeModel(root);
		orga = new JTree(dtm);
		DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel();
		dtsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		orga.setSelectionModel(dtsm);
		JScrollPane sp = new JScrollPane(orga);
		contentPane.add(sp, BorderLayout.CENTER);
		JPanel bp = new JPanel();
		bp.setLayout(new FlowLayout());
		JButton add = new JButton("Hinzufügen Abteilung");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath tp = orga.getLeadSelectionPath();
				if (tp == null)
					return;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
				if (treeNode == null)
					return;	
				Object userObject = treeNode.getUserObject();
				ClientAbteilung ca = new ClientAbteilung();
				new AbteilungDialog(ca, (Abteilung)userObject, treeNode, true);	
				orga.expandPath(tp);
			} 
		});
		bp.add(add);
		JButton addm = new JButton("Hinzufügen Mitarbeiter");
		addm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath tp = orga.getLeadSelectionPath();
				if (tp == null)
					return;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
				if (treeNode == null)
					return;	
				Object userObject = treeNode.getUserObject();
				ClientMitarbeiter m = new ClientMitarbeiter();
				new MitarbeiterDialog(m, (Abteilung)userObject, treeNode, true);	
				orga.expandPath(tp);
			} 
		});
		bp.add(addm);
		JButton edit = new JButton("Bearbeiten");
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath tp = orga.getLeadSelectionPath();
				if (tp == null)
					return;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
				if (treeNode == null)
					return;
				Object userObject = treeNode.getUserObject();
				if (userObject instanceof ClientMitarbeiter) {
					ClientMitarbeiter m = (ClientMitarbeiter)userObject;					
					new MitarbeiterDialog(m, (Abteilung)((DefaultMutableTreeNode)treeNode.getParent()).getUserObject(), (DefaultMutableTreeNode)treeNode.getParent(), false);				
				}
				else if (userObject instanceof ClientAbteilung) {
					ClientAbteilung a = (ClientAbteilung)userObject;
					new AbteilungDialog(a, null, treeNode, false);			
				}
			} 
		});
		bp.add(edit);
		JButton delete = new JButton("Entfernen");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath tp = orga.getLeadSelectionPath();
				if (tp == null)
					return;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
				if (treeNode == null)
					return;
				Object userObject = treeNode.getUserObject();
				if (userObject instanceof ClientMitarbeiter) 
					port.deleteMitarbeiter(((Mitarbeiter)userObject).getId());
				else if (userObject instanceof ClientAbteilung) 
					port.deleteAbteilung(((ClientAbteilung)userObject).getId());
				DefaultMutableTreeNode parNode = (DefaultMutableTreeNode)treeNode.getParent();
				DefaultTreeModel dtm = (DefaultTreeModel)orga.getModel();
				dtm.removeNodeFromParent(treeNode);
				orga.setSelectionPath(new TreePath(dtm.getPathToRoot(parNode)));
			} 
		});
		bp.add(delete);
		contentPane.add(bp, BorderLayout.PAGE_END);
	}

	private DefaultMutableTreeNode GetAbteilung(int headabteilungId, DefaultMutableTreeNode root) {
		DefaultMutableTreeNode da = null;
		List<Abteilung> ala = port.getAbteilungsListe(headabteilungId);
		for(Abteilung a : ala) {
			ClientAbteilung ca = new ClientAbteilung(a.getId(), a.getName(), a.getBoss(), a.getHeadId());
			da = new DefaultMutableTreeNode(ca);
			if (root != null)
				root.add(da);
			GetAbteilung(a.getId(), da);
			List<Integer> midl = port.getMitarbeiterIdListe(a.getId());
			for(int mid : midl) {
				Mitarbeiter m = port.getMitarbeiterDetails(mid);
				ClientMitarbeiter cm = new ClientMitarbeiter(m.getId(), m.getName(), m.getEMail(), m.getPersNr());
				DefaultMutableTreeNode dm = new DefaultMutableTreeNode(cm);
				da.add(dm);
			}
		}
		return da;
	}

	/**
	 * show main application window on desktop
	 */
	public void showFrame() {
		//Display the window.
		frame.setSize(600, 500);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width)/2,(d.height - frame.getSize().height)/2);
		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	private class MitarbeiterDialog extends JDialog {

		public MitarbeiterDialog(final ClientMitarbeiter mitarbeiter, final Abteilung abteilung, final DefaultMutableTreeNode treeNode, final boolean newMitarbeiter) {
			super(frame, "Mitarbeiterdetails", true);
			this.setLayout(new GridLayout(0, 2));
			this.add(new JLabel("Name"));
			final JTextField dn = new JTextField(mitarbeiter.getName(), 30);
			this.add(dn);
			this.add(new JLabel("Personalnummer"));
			final JTextField dpn = new JTextField(mitarbeiter.getPersNr(), 30);
			this.add(dpn);
			this.add(new JLabel("EMail Id"));
			final JTextField dei = new JTextField(mitarbeiter.getEMail(), 30);
			this.add(dei);
			JButton cancel = new JButton("Abbrechen");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MitarbeiterDialog.this.dispose();					
				} 
			});
			this.add(cancel);
			JButton save = new JButton("Speichern");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mitarbeiter.setName(dn.getText());
					mitarbeiter.setPersNr(dpn.getText());
					mitarbeiter.setEMail(dei.getText());
					mitarbeiter.setDeptId(abteilung.getId());
					if (newMitarbeiter) {
						Mitarbeiter m = port.addMitarbeiter(mitarbeiter);
						mitarbeiter.setId(m.getId());
						DefaultTreeModel dtm = (DefaultTreeModel)orga.getModel();
						dtm.insertNodeInto(new DefaultMutableTreeNode(mitarbeiter), treeNode, treeNode.getChildCount());
					}
					else {
						port.updateMitarbeiter(mitarbeiter);
						DefaultTreeModel dtm = (DefaultTreeModel)orga.getModel();
						dtm.nodeChanged(treeNode);
					}
					MitarbeiterDialog.this.dispose();
				}
			});
			this.add(save);
			this.pack();
			// position the dialog
			this.setLocationRelativeTo(this.getOwner());
			this.setVisible(true);
		}
	}

	@SuppressWarnings("serial")
	private class AbteilungDialog extends JDialog {

		public AbteilungDialog(final ClientAbteilung abteilung, final Abteilung headabteilung, final DefaultMutableTreeNode treeNode, final boolean newAbteilung) {
			super(frame, "Abteilungdetails", true);
			this.setLayout(new GridLayout(0, 2));
			this.add(new JLabel("Name der Abteilung"));
			final JTextField dn = new JTextField(abteilung.getName(), 30);
			this.add(dn);
			this.add(new JLabel("Name des Abteilungsleiters"));
			final JTextField dhn = new JTextField(abteilung.getBoss(), 30);
			this.add(dhn);
			JButton cancel = new JButton("Abbrechen");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AbteilungDialog.this.dispose();					
				} 
			});
			this.add(cancel);
			JButton save = new JButton("Speichern");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abteilung.setName(dn.getText());
					abteilung.setBoss(dhn.getText());
					abteilung.setHeadId(headabteilung.getId());
					if (newAbteilung) {
						Abteilung a = port.addAbteilung(abteilung);
						abteilung.setId(a.getId());
						DefaultTreeModel dtm = (DefaultTreeModel)orga.getModel();
						dtm.insertNodeInto(new DefaultMutableTreeNode(abteilung), treeNode, treeNode.getChildCount());
					}
					else {
						port.updateAbteilung(abteilung);
						DefaultTreeModel dtm = (DefaultTreeModel)orga.getModel();
						dtm.nodeChanged(treeNode);
					}
					AbteilungDialog.this.dispose();
				}
			});
			this.add(save);
			this.pack();
			// position the dialog
			this.setLocationRelativeTo(this.getOwner());
			this.setVisible(true);
		}
	}
}
