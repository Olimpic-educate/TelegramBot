import Repository.UserRepository;
import Service.TransactionService;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class BotUtil extends TelegramLongPollingBot {
    public static final String TOKEN;

    CommandHandler commandHandler = new CommandHandler();

    static{
        Properties properties = new Properties();
        try(InputStream input = BotUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
properties.load(input);
TOKEN = properties.getProperty("bot.token");
        }catch (Exception e){
            throw new RuntimeException("Не удалось загрузить config.properties");
        }
    }

    @Override
    public String getBotUsername() {
        return "FinManagerBot";
    }


    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) return;


        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        long tgId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();

        String response = commandHandler.handler(text, tgId, username, chatId);
        if ("SEND_PHOTO".equals(response)) {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(chatId);
            photo.setPhoto(new InputFile(new File("stats.png")));
            try {
                execute(photo);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (response != null && !response.isBlank()) sendText(chatId, response);

    }

    public void sendText(long chatId, String message) {
        SendMessage mess = new SendMessage();
        mess.setChatId(String.valueOf(chatId));
        mess.setText("Выбери действие");
        mess.setReplyMarkup(commandHandler.keyboard);
        mess.setText(message);
        try {
            execute(mess);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}



