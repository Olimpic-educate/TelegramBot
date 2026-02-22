import Repository.UserRepository;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.DriverManager;

public class BotUtil extends TelegramLongPollingBot {
    UserRepository userRepository = new UserRepository();
    @Override
    public String getBotUsername() {
        return "FinManagerBot";
    }

    @Override
    public String getBotToken() {
        return "8509760634:AAFRy4qLypko5Pdr1XZpvWHUVsIivkEy92E";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (text.equals("/start")) {
                sendText(chatId, "Привет, я твой финансовый помощник");
                long tgId = update.getMessage().getFrom().getId();
                String username = update.getMessage().getFrom().getUserName();

                User user = new User(tgId, username);
                userRepository.save(user);
            }
            else if(text.equals("Что ты умеешь?")){
                sendText(chatId, "Я могу грамотно распределить твой бюджет");
            }


        }

    }

    public void sendText(long chatId, String message) {
        SendMessage mess = new SendMessage();
        mess.setChatId(chatId);
        mess.setText(message);
        try{
            execute(mess);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }

    }
}



