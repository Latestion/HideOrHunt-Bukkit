package me.latestion.hoh.commandmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabC implements TabCompleter {
	
	List<String> arguments = new ArrayList<String>();
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (arguments.isEmpty()) {
			arguments.add("start"); arguments.add("freeze"); arguments.add("chat"); arguments.add("reload"); arguments.add("beacon"); 
			arguments.add("rules");
			arguments.add("stop");
			
			List<String> result = new ArrayList<String>();
			if (args.length == 1) {
				for (String s : arguments) {
					if (s.toLowerCase().startsWith(args[0].toLowerCase()))
						result.add(s);
				}
				return result;
			}
			
			if (args.length == 0) {
				return arguments;
			}
			
			if (args[0].equalsIgnoreCase("start")) {
				List<String> intresult = new ArrayList<String>();
				intresult.add(Integer.toString(1));intresult.add(Integer.toString(2));intresult.add(Integer.toString(3));intresult.add(Integer.toString(4));
				intresult.add(Integer.toString(5));
				return intresult;
			}
			
			if (args[0].equalsIgnoreCase("beacon")) {
				return null;
			}
		}
		return null;
	}
}
