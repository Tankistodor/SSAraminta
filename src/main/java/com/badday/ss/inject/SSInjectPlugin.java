package com.badday.ss.inject;

import java.awt.Desktop;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class SSInjectPlugin implements IFMLLoadingPlugin, IFMLCallHook {

	private static String transformerMain = "com.badday.ss.inject.SSInjectBytecode";
	public static boolean hasRegistered = false;
	public static final String mcVersion = "[1.7.10]";
	public static File mcDir;

	public String[] getLibraryRequestClass() {
		return null;
	}

	public static void versionCheck(String reqVersion, String mod) {
		String mcVersion = (String) cpw.mods.fml.relauncher.FMLInjectionData
				.data()[4];

		if (!VersionParser.parseRange(reqVersion).containsVersion(
				new DefaultArtifactVersion(mcVersion))) {
			String err = "This version of " + mod
					+ " does not support minecraft version " + mcVersion;
			System.err.println(err);

			JEditorPane ep = new JEditorPane(
					"text/html",
					"<html>"
							+ err
							+ "<br>Remove it from your mods folder and check <a href=\"http://none.com\">here</a> for updates"
							+ "</html>");

			ep.setEditable(false);
			ep.setOpaque(false);
			ep.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent event) {
					try {
						if (event.getEventType().equals(
								HyperlinkEvent.EventType.ACTIVATED)) {
							Desktop.getDesktop().browse(event.getURL().toURI());
						}
					} catch (Exception e) {
					}
				}
			});
			JOptionPane.showMessageDialog(null, ep, "Fatal error", 0);
			System.exit(1);
		}
	}

	public String[] getASMTransformerClass() {
		versionCheck("[1.7.10]", "SSInject");
		String[] asmStrings = { transformerMain };

		if (!hasRegistered) {
			List<String> asm = Arrays.asList(asmStrings);

			for (String s : asm) {
				try {
					Class c = Class.forName(s);

					if (c != null) {
						System.out
								.println("Successfully Registered Transformer");
					}
				} catch (Exception ex) {
					System.out.println("Error while running transformer " + s);
					return null;
				}
			}

			hasRegistered = true;
		}

		return asmStrings;
	}

	public String getModContainerClass() {
		return "com.badday.ss.inject.SSInjectContainer";
	}

	public String getSetupClass() {
		return "com.badday.ss.inject.SSInjectPlugin";
	}

	public void injectData(Map<String, Object> data) {
		if (data.containsKey("mcLocation")) {
			mcDir = (File) data.get("mcLocation");
		}

		System.out.println("[SSInject]: Patching game...");
	}

	public Void call() throws Exception {
		// MicdoodleAccessTransformer.addTransformerMap("micdoodlecore_at.cfg");
		return null;
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
