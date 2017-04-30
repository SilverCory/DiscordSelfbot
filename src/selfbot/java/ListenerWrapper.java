package selfbot.java;

import clojure.lang.IFn;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class ListenerWrapper extends ListenerAdapter {
    private ListenerWrapper() {
    }

    public static final ListenerWrapper instance = new ListenerWrapper();
    private final Map<String, IFn> commands = new HashMap<>();
    private IFn fallback;
    public static JDA jda;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor() != jda.getSelfUser() || !event.getMessage().getRawContent().startsWith("::")) {
            return;
        }
        String message = event.getMessage().getRawContent().substring(2);
        String command = message.split(" ")[0];
        IFn method;
        try {
            if ((method = commands.get(command.toLowerCase())) == null) {
                fallback.invoke(event, event.getChannel(), event.getAuthor(), event.getGuild(), event.getMessage(), message.split(" "));
                return;
            }
            method.invoke(event, event.getChannel(), event.getAuthor(), event.getGuild(), event.getMessage(), message.split(" "));
        } catch (Exception ex) {
            if (ex instanceof Return) {
                return;
            }
            boolean isLoc = ex.getLocalizedMessage() != null;
            StringBuilder ats = new StringBuilder();
            for (StackTraceElement element : ex.getStackTrace()) {
                ats.append("\t at ").append(element.toString()).append("\n");
            }
            event.getMessage().editMessage("```js\n" + ex.getClass().getName() + (isLoc ? ": " + ex.getLocalizedMessage() : "") + ats.toString() + "\n```").queue();
        }
    }

    public void registerCommand(final String command, final IFn method) {
        this.commands.put(command.toLowerCase(), method);
    }

    public void setFallback(IFn fallback) {
        this.fallback = fallback;
    }
}
