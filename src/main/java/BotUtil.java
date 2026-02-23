import Repository.UserRepository;
import Service.TransactionService;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class BotUtil extends TelegramLongPollingBot {

    CommandHandler commandHandler = new CommandHandler();


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
        if (!update.hasMessage() || !update.getMessage().hasText()) return;


        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        long tgId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();

        String response = commandHandler.handler(text, tgId, username);
        if (response != null && !response.isBlank()) sendText(chatId, response);

    }

    public void sendText(long chatId, String message) {
        SendMessage mess = new SendMessage();
        mess.setChatId(String.valueOf(chatId));
        mess.setText(message);
        try {
            execute(mess);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}



