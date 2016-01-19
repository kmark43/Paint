package main.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class SettingsDialog extends JDialog
{
	final static long serialVersionUID = 12498719274L;
	
	public final static int AUTOUPDATEDURINGFILTERS = 0;
	
	private HashMap<Integer, Boolean> rules = new HashMap<Integer, Boolean>();
	private JCheckBox chkAutoUpdateFilters = new JCheckBox("Auto update during filter changes");
	
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	
	public SettingsDialog()
	{
		loadSettings();
		initGUI();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void initGUI()
	{
		JPanel mainPane = new JPanel(new GridLayout(2, 1));
		JPanel temp = new JPanel();
		temp.add(chkAutoUpdateFilters);
		chkAutoUpdateFilters.setSelected(rules.get(AUTOUPDATEDURINGFILTERS));
		mainPane.add(temp);
	}
	
	public File getSettingsFile()
	{
		String os = (System.getProperty("os.name")).toUpperCase();
		String workingDir = "";
		if (os.contains("WIN"))
			workingDir = System.getenv("AppData") + "/Painter";
		else
			workingDir = System.getenv("user.home") + "/Library/Application Support/Painter";
		File file = new File(workingDir, "settings.txt");
		return file;
	}
	
	public void loadSettings()
	{
		HashMap<String, Integer> ruleCaster = new HashMap<String, Integer>();
		ruleCaster.put("autoupdatefilter", AUTOUPDATEDURINGFILTERS);
		try
		{
			File settingsFile = getSettingsFile();
			if (!settingsFile.exists())
				settingsFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(settingsFile));
			String line;
			
			while ((line = in.readLine()) != null)
			{
				String tokens[] = line.split("=");
				rules.put(ruleCaster.get(tokens[0]), Boolean.parseBoolean(tokens[1]));
			}
			
			in.close();
		} catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void saveSettings()
	{
		HashMap<Integer, String> ruleWriter = new HashMap<Integer, String>();
		ruleWriter.put(AUTOUPDATEDURINGFILTERS, "autoupdatefilter");
		try
		{
			File settingsFile = getSettingsFile();
			if (!settingsFile.exists())
				settingsFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(settingsFile));
			out.write(ruleWriter.get(AUTOUPDATEDURINGFILTERS) + "=" + rules.get(AUTOUPDATEDURINGFILTERS));
			out.newLine();
			out.close();
		} catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
}