import Repository.UserRepository;
import Service.TransactionService;
import model.Transaction;
import model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    public CommandHandler() {
        createKeyboard();
    }

    private final TransactionService service = new TransactionService();
    UserRepository userRepository = new UserRepository();
    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

    KeyboardRow row1 = new KeyboardRow();
    KeyboardRow row2 = new KeyboardRow();

    public void createKeyboard() {
        row1.add("Доход💰");
        row1.add("Расход💸");
        row2.add("Баланс📈");
        row2.add("История📜");
        row2.add("Статистика📊");

        List<KeyboardRow> key = new ArrayList<>();
        key.add(row1);
        key.add(row2);
        keyboard.setKeyboard(key);
        keyboard.setResizeKeyboard(true);
    }


    public String handler(String text, long tgId, String username) {

        User us = userRepository.findByTelegramId(tgId);

        if (text.equals("Старт") || text.equals("/start")) {
            if (us == null) {
                User user = new User(tgId, username);
                userRepository.save(user);
            }
            return "Привет, я твой финансовый помощник🖐️";
        }


        if (us == null) {
            return "Сначала введите /start";
        }

        int userId = us.id();
        if (text.equals("Доход💰")) {
            return "Введите: /income сумма категория";
        }
        if (text.equals("Расход💸")) {
            return "Введите: /expense сумма категория";
        }

        if (text.startsWith("/income")) {
            String[] parts = text.split(" "); //добавить amount > 0
            if (parts.length < 3) {
                return "Формат: /income 350 подарок";
            }
            double amount = 0;
            try {
                if (Double.parseDouble(parts[1]) <= 0) {
                    return "Введите число больше 0";
                }
                amount = Double.parseDouble(parts[1]);
            } catch (NumberFormatException e) {
                return "Неверный формат";
            }
            String category = String.valueOf(parts[2]);
            service.addIncome(userId, amount, category);
            return "💰Доход добавлен!"; //убрать (вариативно)
        }

        if (text.startsWith("/expense")) { //продумать случай день рождения
            String[] parts = text.split(" ");
            if (parts.length < 3) {
                return "Формат: /expense 350 еда";
            }
            double amount = 0;
            try {
                if (Double.parseDouble(parts[1]) <= 0) {
                    return "Введите число больше 0";
                }
                amount = Double.parseDouble(parts[1]);
            } catch (NumberFormatException e) {
                return "Неверный формат";
            }
            String category = String.valueOf(parts[2]);
            service.addExpense(userId, amount, category);
            return "💸Расход добавлен!"; //убрать (вариативно)
        }

        if (text.equals("История📜")) {
            List<Transaction> list = service.getHistory(userId);
            if (list.isEmpty()) {
                return "История пуста";
            }

            StringBuilder sb = new StringBuilder("📜История:\n\n");
            for (Transaction t : list) {
                if (t.type().equals("income")) {
                    sb.append("Доход");
                } else if (t.type().equals("expense")) {
                    sb.append("Расход");
                }
                sb.append(" (").append(t.amount()).append(")\n").append(t.category()).append("\n");
            }
            return sb.toString();

        }

        if (text.equals("/stats") || text.equals("Статистика📊")) {
            Map<String, Double> stats = service.getStats(userId);

            if (stats.isEmpty()) {
                return "История пуста";
            }

            StringBuilder sb = new StringBuilder("📊Статистика расходов:" + "\n");

            for (String category : stats.keySet()) {
                sb.append(category).
                        append(" ").
                        append(stats.get(category)).
                        append("\n");
            }
            return sb.toString();

        }


        if (text.equals("Баланс📈")) {
            return String.valueOf(service.getBalance(userId));
        }
        return null;
    }

}
