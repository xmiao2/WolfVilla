package edu.ncsu.csc440.teamk.wolfvilla.util;

/**
 * Class which enumerates the states of a Message that can be displayed by the application
 */
public class FlashMessage {
    public static final String MESSAGE = "message";
    public enum MessageType {
        SUCCESS ("success", "Success!"),
        INFO ("info", "Information!"),
        WARNING ("warning", "Warning!"),
        DANGER ("danger", "Error!");

        private String name;
        private String defaultHeader;
        MessageType(String name, String defaultHeader) {
            this.name = name;
            this.defaultHeader = defaultHeader;
        }

        public String getName() {
            return name;
        }

        public String getDefaultHeader() {
            return defaultHeader;
        }
    }

    public final String header;
    public final String content;
    public final MessageType messageType;

    public FlashMessage(MessageType messageType) {
        this(messageType, "");
    }

    public FlashMessage(MessageType messageType, String content) {
        this(messageType, content, messageType.getDefaultHeader());
    }

    public FlashMessage(final MessageType messageType, final String content, final String header) {
        this.header = header;
        this.content = content;
        this.messageType = messageType;
    }
}
