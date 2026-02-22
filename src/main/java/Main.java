import database.DatabaseManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception{
        DatabaseManager.init();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new BotUtil());
    }
}
