package me.choijunha.youtubecommentproject.exception;

public class ChannelOperationException extends RuntimeException {
    public ChannelOperationException(String message) {
        super(message);
    }

    public ChannelOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}