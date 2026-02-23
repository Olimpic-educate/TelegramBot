package model;

public record User(long tgId, String username, int id) {
    public User(long tgId, String username) {
        this(tgId, username, 0);
    }
}