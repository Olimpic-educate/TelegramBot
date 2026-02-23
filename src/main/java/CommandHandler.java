import Repository.UserRepository;
import Service.TransactionService;
import model.User;

public class CommandHandler {
    private final TransactionService service = new TransactionService();
    UserRepository userRepository = new UserRepository();


    public String handler(String text, long tgId, String username){
        User us = userRepository.findByTelegramId(tgId);

        if (text.equals("/start")) {
            if (us == null) {
                User user = new User(tgId, username);
                userRepository.save(user);
            }
            return "Привет, я твой финансовый помощник";
        }


        if (us == null) {
            return "Сначала введите /start";
        }

        int userId = us.id();

        if(text.startsWith("/income")) {
            String[] parts = text.split(" "); //добавить quality > 0
            if (parts.length < 3) {
                return  "Формат: /income 350 подарок";
            }
            double quality = 0;
            try {
                if(Double.parseDouble(parts[1]) <= 0){
                    return "Введите число больше 0";
                }
                quality = Double.parseDouble(parts[1]);
            }catch (NumberFormatException e){
                return "Введите число правильно";
            }
            String category = String.valueOf(parts[2]);
            service.addIncome(userId, quality, category);
            return "Доход добавлен!"; //убрать (вариативно)
        }

        if (text.startsWith("/expense")) { //продумать случай день рождения
            String[] parts = text.split(" ");
            if (parts.length < 3) {
                return  "Формат: /expense 350 еда";
            }
            double quality = 0;
            try {
                if(Double.parseDouble(parts[1]) <= 0){
                    return "Введите число больше 0";
                }
                quality = Double.parseDouble(parts[1]);
            }catch (NumberFormatException e){
                return "Введите число правильно";
            }
            String category = String.valueOf(parts[2]);
            service.addExpense(userId, quality, category);
            return "Расход добавлен!"; //убрать (вариативно)
        }


        if(text.equals("/balance")){
            return String.valueOf(service.getBalance(userId));
        }
        return null;
    }

}
