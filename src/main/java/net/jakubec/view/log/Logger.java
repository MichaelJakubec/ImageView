package net.jakubec.view.log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.jakubec.view.Settings.VSettings;

public class Logger {
	private FileOutputStream logStream;
	private static final String EOL = System.getProperty("line.separator");
	private static Logger log;

	public static void close() {
		try {
			if (log == null) return;
			log.logStream.close();
			log = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init() {

		log = new Logger();
		File logFile = new File(VSettings.rootDir, "LogFile.log");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
				System.out.println("LOG FILE CREATED");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			log.logStream = new FileOutputStream(logFile, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DataOutputStream out = new DataOutputStream(log.logStream);
		StringBuilder sb = new StringBuilder();
		sb.append("************ [");
		Date date = new Date();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		sb.append(format.format(date));
		sb.append("] ************");
		sb.append(EOL);
		try {
			out.writeBytes(sb.toString());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void logException(final String msg, final String exceptionString,
			final Exception e) {
		if (log == null) return;
		Date date = new Date();
		DateFormat f = new SimpleDateFormat("HH:mm");

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(f.format(date));
		sb.append("]: ");
		sb.append(msg);
		sb.append(EOL);
		sb.append("[");
		sb.append(f.format(date));
		sb.append("]: ");
		sb.append(exceptionString);
		sb.append(EOL);
		DataOutputStream out = new DataOutputStream(log.logStream);
		try {
			out.writeBytes(sb.toString());
			out.flush();
		} catch (IOException es) {
			// TODO Auto-generated catch block
			es.printStackTrace();
		}

	}

	private Logger() {

	}

	@Override
	protected void finalize() {
		try {
			logStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
