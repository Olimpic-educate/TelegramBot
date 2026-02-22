package model;

public class User {
    private int Id;
    private long tgId;
    private String username;

    public User(long tgId, String username){
        this.tgId = tgId;
        this.username = username;
    }
    public int getID(){return Id;};
    public long getTgId(){return tgId;}
    public String getUsername(){return username;}
}
