package liquid.jda.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public record CommandContext(GuildMessageReceivedEvent event, List<String> arguments) implements CommandEvent {
    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    public List<String> getArgs() {
        return this.arguments;
    }
}
