package Service;

import Repository.TransactionRepository;
import model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    TransactionRepository repository = new TransactionRepository();

    public void addIncome(int userId, double amount, String category) {
        String date = LocalDate.now().toString();
        Transaction transaction = new Transaction(userId, amount, "income", date, category);
        repository.save(transaction);
    }
    public void addExpense(int userId, double amount, String category){
        String date = LocalDate.now().toString();
        Transaction transaction = new Transaction(userId, amount, "expense", date, category);
        repository.save(transaction);
    }
    public double getBalance(int userId){
        double in = repository.sumByType(userId, "income");
        double ex = repository.sumByType(userId, "expense");
       return in - ex;
    }
    public List<Transaction> getHistory(int user_id){
        return repository.findByUserId(user_id);
    }
}
