package liquid.jda.manager;

import liquid.LiquidCore;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ListenerEvent extends ListenerAdapter {
    private final CommandManager manager;

    public ListenerEvent(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LiquidCore.log.debug(String.format("Logged as %#s", event.getJDA().getSelfUser()));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (LiquidCore.COMMON.debug.get().equals(true)) {
            User author = event.getAuthor();
            Message message = event.getMessage();
            String content = message.getContentDisplay();

            if (event.isFromType(ChannelType.TEXT)) {
                Guild guild = event.getGuild();

                LiquidCore.log.debug(String.format("[JDA Module] | Guild: %s Member: <%#s>: %s", guild, author, content));
            } else if (event.isFromType(ChannelType.PRIVATE))
                LiquidCore.log.debug(String.format("[JDA Module] | [Private]<%#s>: %s", author, content));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String rw = event.getMessage().getContentRaw();

        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.startsWith(LiquidCore.COMMON.prefix.get())) {
            manager.commandFunc(event);
        }
    }
}
