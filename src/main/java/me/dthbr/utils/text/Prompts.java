package me.dthbr.utils.text;

import me.dthbr.utils.config.Msgs;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public record Prompts<T>(String errorMsg, Predicate<String> predicate, Function<String, T> converter) {

    public static final Prompts<String> TEXT = new Prompts<>(null, s -> true, Objects::toString);

    public ValidatingPrompt asPrompt(String displayText) {
        return new ValidatingPrompt() {

            @Override
            @NotNull
            public String getPromptText(@NotNull ConversationContext conversationContext) {
                String lineOne = Msgs.of(displayText).asLegacy();
                String lineTwo = Msgs.of("<gray>(Type 'cancel' o wait 1 minute to cancel)").asLegacy();
                return lineOne + "\n" + lineTwo;
            }

            @Override
            @Nullable
            protected String getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
                return Msgs.of("<red>" + errorMsg).var("s", invalidInput).asLegacy();
            }

            @Override
            protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String string) {
                return predicate.test(string);
            }

            @Override
            @Nullable
            protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String string) {
                conversationContext.setSessionData("result", converter.apply(string));
                return END_OF_CONVERSATION;
            }
        };
    }

    public TextGUI<T> asConv(Player who, String displayText) {
        return new TextGUI<T>(who).prompt(asPrompt(displayText));
    }

    public CompletableFuture<T> start(Player who, String displayText) {
        return asConv(who, displayText).exitText("cancel").timeout(60).open();
    }

}
