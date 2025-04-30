package vcmsa.projects.smartspend

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {
        private val expenseDao: ExpenseDao
        private val categoryDao: CategoryDao
        private val userDao: UserDao
        private val database: AppDatabase

        init {
            database = AppDatabase.getDatabase(application)
            expenseDao = database.expenseDao()
            categoryDao = database.categoryDao()
            userDao = database.userDao()
        }

        // LiveData for UI updates
        private val _balance = MutableLiveData<Double>()
        val balance: LiveData<Double> get() = _balance

        private val _totalIncome = MutableLiveData<Double>()
        val totalIncome: LiveData<Double> get() = _totalIncome

        private val _totalExpense = MutableLiveData<Double>()
        val totalExpense: LiveData<Double> get() = _totalExpense

        private val _totalTransactions = MutableLiveData<Int>()
        val totalTransactions: LiveData<Int> get() = _totalTransactions

        private val _categories = MutableLiveData<List<Category>>()
        val categories: LiveData<List<Category>> get() = _categories

        private val _expenses = MutableLiveData<List<Expense>>()
        val expenses: LiveData<List<Expense>> get() = _expenses

        // Coroutine to perform database operations
        private fun performDatabaseOperation(block: suspend () -> Unit) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    block()
                } catch (e: Exception) {
                    // Handle errors (e.g., show a toast)
                    Log.e("HomeViewModel", "Database error: ${e.message}")
                }
            }
        }
}
fun loadHomePageData() {
    performDatabaseOperation {
        // Example data loading (replace with your actual logic)
        val allExpenses = expenseDao.getAll()
        val allCategories = categoryDao.getAll()
        val allUsers = userDao.getAll() //Example

        var income = 0.0
        var expense = 0.0
        for (exp in allExpenses){
            if (exp.amount > 0){
                income += exp.amount
            }
            else{
                expense += Math.abs(exp.amount)
            }
        }

        _totalIncome.postValue(income)
        _totalExpense.postValue(expense)
        _balance.postValue(income - expense)
        _totalTransactions.postValue(allExpenses.size)
        _categories.postValue(allCategories)
        _expenses.postValue(allExpenses) // load all expenses
    }
}

fun addExpense(date: String, startTime: String, endTime: String, description: String, categoryId: Int, photoPath: String?, amount: Double) {
    performDatabaseOperation {
        val expense = Expense(date = date, startTime = startTime, endTime = endTime, description = description, categoryId = categoryId, photoPath = photoPath, amount = amount)
        expenseDao.insert(expense)
        loadHomePageData() // Refresh data after adding
    }
}

fun addCategory(name: String) {
    performDatabaseOperation {
        val category = Category(name = name)
        categoryDao.insert(category)
        loadHomePageData()
    }
}

fun getExpensesBetweenDates(startDate: String, endDate: String) {
    performDatabaseOperation {
        val expenses = expenseDao.getExpensesBetweenDates(startDate, endDate)
        _expenses.postValue(expenses)
    }
}

fun getTotalForCategory(categoryId: Int, startDate: String, endDate: String): LiveData<Double> {
    val total = MutableLiveData<Double>()
    performDatabaseOperation {
        val categoryTotal = expenseDao.getCategoryTotal(categoryId, startDate, endDate)
        total.postValue(categoryTotal)
    }
    return total
}

fun getUser(username: String): LiveData<User?>{
    val user = MutableLiveData<User?>()
    performDatabaseOperation {
        val foundUser = userDao.findByUsername(username)
        user.postValue(foundUser)

    }
    return user
}

fun insertUser(user: User){
    performDatabaseOperation {
        userDao.insert(user)
    }
}
}