package liquid.jda.command;

public interface CommandBase {
    void func(CommandContext ctx);
    String setHelp();
    String setDesc();
    String setName();
}
