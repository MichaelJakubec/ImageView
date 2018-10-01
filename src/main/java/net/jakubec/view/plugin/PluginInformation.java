package net.jakubec.view.plugin;

import java.io.File;

public class PluginInformation {
	private ViewPlugin plugin;
	private String pluginName;

	public PluginInformation(final File xmlFile) {

	}

	public String getName() {
		return pluginName;
	}

	public ViewPlugin getPlugin() {
		return plugin;
	}

}
