package net.jakubec.view.app;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.jakubec.view.app.Application;
import net.jakubec.view.properties.VProperties;

class ExceptionDialog extends JDialog {
	private JTextArea area;
	private Exception error;


	ExceptionDialog(final Exception e, final String message) {
		super(Application.getMainWindow(), "Exception", true);
		Container cp = getContentPane();
		super.setSize(300, 200);
		super.setLocation(400, 200);
		JScrollPane scroll = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		error = e;

		cp.setLayout(new BorderLayout());
		area = new JTextArea();
		area.setEditable(false);
		area.setText(message);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		scroll.setViewportView(area);
		cp.add(scroll, BorderLayout.CENTER);

		JPanel panel = new JPanel();

		JButton bt = new JButton(VProperties.getValue("button.ok"));
		bt.addActionListener(event -> {
			setVisible(false);
			dispose();
		});
		bt.setActionCommand("ok");
		panel.add(bt);
		bt = new JButton(VProperties.getValue("button.showError"));
		bt.addActionListener(event -> {
			StackTraceElement[] stackTrace = error.getStackTrace();
			StringBuilder sb = new StringBuilder();
			sb.append("Message: ");
			sb.append(error.getMessage());
			sb.append("\nName:");
			sb.append(error.getClass().getName());
			for (StackTraceElement aStackTrace : stackTrace) {
				sb.append("\n Class: ");
				sb.append(aStackTrace.getClassName());
				sb.append(" - Method: ");
				sb.append(aStackTrace.getMethodName());
				sb.append(" - Line: ");
				sb.append(aStackTrace.getLineNumber());
				sb.append(" ");
				sb.append(aStackTrace.getFileName());

			}
			area.setText(sb.toString());

			((JButton) event.getSource()).setEnabled(false);
		});
		panel.add(bt);
		this.add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(true);
		setVisible(true);

	}
}
