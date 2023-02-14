package me.dthbr.utils.text;

import me.dthbr.utils.DeathUtils;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class TextGUI<T> {

    private final ConversationFactory factory;
    private Conversation conversation;
    private final Player viewer;

    public TextGUI(Player viewer) {
        this.viewer = viewer;
        factory = new ConversationFactory(DeathUtils.instance());
    }

    public TextGUI<T> prompt(Prompt prompt) {
        factory.withFirstPrompt(prompt);
        return this;
    }

    public TextGUI<T> timeout(int seconds) {
        factory.withTimeout(seconds);
        return this;
    }

    public TextGUI<T> exitText(String sequence) {
        factory.withEscapeSequence(sequence);
        return this;
    }

    private Conversation build() {
        if (conversation != null) {
            return conversation;
        }
        return conversation = factory.buildConversation(viewer);
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<T> open() {
        CompletableFuture<T> future = new CompletableFuture<>();
        factory.withLocalEcho(false).addConversationAbandonedListener(event -> {
            if (event.getCanceller() != null) {
                future.complete(null);
            } else {
                T result = (T) event.getContext().getSessionData("result");
                future.complete(result);
            }
        });
        build().begin();

        return future;
    }

}
