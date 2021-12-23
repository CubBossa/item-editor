package de.bossascrew.itemeditor.commands.flags;

import de.bossascrew.itemeditor.exception.UnexpectedFlagException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandFlagParser {


	/**
	 * Removes all args that are part of a command flag and adds command flags to the provided list
	 *
	 * @param args          the command arguments to filter
	 * @param commandFlags  the  to which the parsed command flags are added
	 * @param expectedFlags the flags to expect in the provided arguments
	 * @return a map of all found and, if required, their string value
	 */
	public String[] reduce(String[] args, List<CommandFlag> expectedFlags, Map<CommandFlag, String> commandFlags, boolean tabCompletion) {
		List<String> remainingArgs = new ArrayList<>();

		int argIndex = 0;
		while (argIndex < args.length - (tabCompletion ? 1 : 0)) {
			String arg = args[argIndex];
			if (!arg.startsWith("-")) {
				remainingArgs.add(arg);
				argIndex++;
				continue;
			}
			List<CommandFlag> flagsInArg = new ArrayList<>();
			if (arg.startsWith("--")) {
				CommandFlag flag = expectedFlags.stream().filter(commandFlag -> arg.substring(2).equalsIgnoreCase(commandFlag.getName())).findFirst().orElse(null);
				flagsInArg.add(flag);
			} else {
				for (char c : arg.substring(1).toCharArray()) {
					CommandFlag flag = expectedFlags.stream().filter(commandFlag -> commandFlag.getLetter() == c).findFirst().orElse(null);
					flagsInArg.add(flag);
				}
			}

			for (CommandFlag flag : flagsInArg) {
				if (flag == null) {
					if (tabCompletion) {
						continue;
					}
					throw new UnexpectedFlagException(args[argIndex]);
				}
				String target = null;
				if (flag instanceof TargetCommandFlag tFlag) {
					if (argIndex < args.length - 1) {
						target = args[argIndex + 1];
					}
					tFlag.setTarget(target);
				}
				commandFlags.put(flag, target);
			}
			if (flagsInArg.stream().anyMatch(flag -> flag instanceof TargetCommandFlag tcf && tcf.getTarget() != null)) {
				argIndex++;
			}
			argIndex++;
		}
		if (tabCompletion && args.length > 0) {
			remainingArgs.add(args[args.length - 1]);
		}
		return remainingArgs.toArray(String[]::new);
	}
}
