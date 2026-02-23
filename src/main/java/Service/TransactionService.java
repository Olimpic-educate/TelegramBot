package Service;

import Repository.TransactionRepository;
import model.Transaction;

import java.time.LocalDate;

public class TransactionService {
    TransactionRepository repository = new TransactionRepository();

    public void addIncome(int userId, double quality, String category) {
        String date = LocalDate.now().toString();
        Transaction transaction = new Transaction(userId, quality, "income", date, category);
        repository.save(transaction);
    }
    public void addExpense(int userId, double quality, String category){
        String date = LocalDate.now().toString();
        Transaction transaction = new Transaction(userId, quality, "expense", date, category);
        repository.save(transaction);
    }
    public double getBalance(int userId){
        double in = repository.sumByType(userId, "income");
        double ex = repository.sumByType(userId, "expense");
       return in - ex;
    }
}
