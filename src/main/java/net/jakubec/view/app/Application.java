package net.jakubec.view.app;

import javax.swing.*;


/**
 * This is the entry point and main Class of this programm.
 * 
 * @author amunra
 * @since Version 0.1
 * 
 * 
 */
public class Application {



	private static ImageView view;


	/**
	 * Starts the application.
	 * 
	 * @param args
	 *            Standard input for the application
	 */
	public static void main(final String[] args) {
		try {
//			PluginClassLoader loader = new PluginClassLoader();
//			System.setSecurityManager(new ViewSecurityManager(loader));
//			PluginOrganizer.getInstance(loader);

			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (args.length == 1) {
				if (args[0].startsWith("-")) {
					if (args[0].equals("--edit") || args[0].equals("-e")) {
						view = new ImageView();
						view.showEditMode();
					} else if (args[0].equals("--help") || args[0].equals("-h")) {
						printUsage();
						return;
					}
				} else {
					view = new ImageView(args[0]);
				}
			} else if (args.length > 1) {
				view = new ImageView(args[0]);
			} else {
				view = new ImageView();
			}
			if (view != null) {
				view.setVisible(true);
			}
		} catch (Exception e) {
			VExceptionHandler.raiseException(e, "An unknow error occured");
		}

	}

	/**
	 * prints the Use Information on the Scrren
	 */
	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("\tImageView [options ...] [File ...]\n");
		System.out.println("-h, --help \t\t\tDisplay Help");
		System.out.println("-e, --edit \t\t\tStart ImageView in Edit Mode");
		System.out.println();
	}

	static ImageView getMainWindow() {
		return view;
	}
}
