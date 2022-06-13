package liquid.jda.manager;

import liquid.LiquidCore;
import liquid.config.LiquidConfig;
import liquid.jda.command.CommandBase;
import liquid.jda.command.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {
    private final Map<String, CommandBase> commands = new HashMap<>();

    public void register(CommandBase commandBase) {
        if (!commands.containsKey(commandBase.setName()))
            commands.put(commandBase.setName(), commandBase);
    }

    public Collection<CommandBase> getCommands() {
        return commands.values();
    }

    public CommandBase getCommand(String name) {
        return commands.get(name);
    }

    void commandFunc(GuildMessageReceivedEvent event) {
        final String[] prefix = event.getMessage()
                .getContentRaw().replaceFirst(
                        "" + Pattern.quote(LiquidCore.COMMON.prefix.get()), "")
                .split("\\s+");
        final String invoke = prefix[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(prefix).subList(1, prefix.length);

            CommandContext context = new CommandContext(event, args);

            event.getChannel().sendTyping().queue();
            commands.get(invoke).func(context);
        }
    }
}
