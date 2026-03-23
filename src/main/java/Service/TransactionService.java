package Service;

import Repository.TransactionRepository;
import model.Transaction;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public Map<String, Double> getStats(int userId){
        return repository.getStats(userId);
    }
    public void createChart(int userId){
        Map<String, Double> stats = getStats(userId);
        if(stats.isEmpty()) return;
        PieChart chart =new PieChartBuilder()
                .width(600)
                .height(400)
                .title("Расходы по категориям")
                .build();
        for(String category: stats.keySet()){
            chart.addSeries(category, stats.get(category));
        }
        try{
            BitmapEncoder.saveBitmap(chart, "stats", BitmapEncoder.BitmapFormat.PNG);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Диограмма создана");
    }
    public List<Transaction> getHistory(int user_id){
        return repository.findByUserId(user_id);
    }
}
