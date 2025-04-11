# MyRecipeApp


Android Component

LazyVerticalGrid
: allows you to display multiple items in a grid format vertically

```kotlin
    
@Composable
fun SimpleGrid() {
    val items = List(100) {"Item $it"}
    
    LazyVerticalGrid(
        cells = GridCells.Fixed(2), //setting the number of columns
    ){
        items(items) {item ->
            Text(item) //displaying item
        }
    }
}

```



State
androidx.compose.runtime.State 
State<T> is a read-only holder(data type) used in Jetpack Compose
It allows you to observe a value that triggers recomposition when it changes, but prevents modification from outside
when value changes, compose automatically triggers recomposition(re-draw) of any UI reading that state

```kotlin
class CounterViewModel : ViewModel() {
        //commonly used to expose state safely from a ViewModel 
        private val _count = mutableStateOf(0) // can change the value (_count.value = 2)
        val count: State<Int> = _count //read only (count.value), cant be modified from outside 

    fun increment() {
        _count.value++ // only can be changed inside
    }
}
```

```kotlin
    @Composable
    fun CounterView(viewModel: CounterViewModel = viewModel()){
        val count by viewModel.count // read-only value (using State<T>) -> can't directly access(to change) the original value(_count)
    
        //.... 
    }
```

why use State?
1. Encapsulation : prevent external modification
2. clear intention : signals this is "READ ONLY" value 
3. safety : prevent UI components from side-effects



API
** 약속과 규약 같은 느낌임
An API is a set of rules/definitions that allows software applications to communicate with each other.
it defines the "methods/data format" so that application can request&exchange information

allowing different systems to interact with each other in a seamless manner


API in various types 
-> common misunderstand - Web API is not only API!

Web API : accessible over the internet using HTTP/HTTPS protocols. allow app to interact with external services and data sources
Library / Framework APIs : set of routines, protocols, tools for building software and app
Operating System APIs : allow app to interact with the operating system, enabling reading files, interacting with hardware devices etc..

sometimes, the code you created in your app called API **effective java


Components of API
- Endpoints : specific paths or URLs (https://api.example.com/user/1)
- Methods : GET/POST/PUT/DELETE
- Requests : call 
- Response : API returns JSON or XML data as response(with status code)

tips: Some API require Key for Authentication/Authorization/Rate Limiting/Tracking & Analytics reason



Gradle Scripts in Android (android 14 version)
- Project(Top)-level build.gradle : defines configuration common across all modules(ex. repositories, classpath dependencies)
- Module(App)-level build.gradle : this file specifies module-specific configurations (app ID, SDK, dependencies)
- gradlew properties : configurations (globally) ex.memory setting
- gradle tasks : tasks are actions that gradle executes during build process(compiling, packaging the app)




Coroutines 
coroutines are a powerful feature in kotlin that makes asynchronous programming more manageable and concise
-> example of asynchronous programming : network calls, database transactions ...


suspend keyword
suspend functions : building blocks of coroutines. can be paused and resumed, allowing for non-blocking asynchronous execution(meaning remain responsive, if not user need to wait frozen screen until function returned the value/terminate)
usage : can be invoked from other suspend functions / within a coroutine scope

```kotlin
    suspend fun fetchData() {//long-running operation
        //simulate a network call / any asynchronous operation
        delay(1000)
        return "Data fetched successfully"
    }

    //invoke from coroutine scope
    GlobalScope.launch { //launch new coroutine (lightweight thread can run asynchronous code)
        val result = fetchData()//invoke suspend function inside the coroutine, without blocking the main thread(app remain responsive) 
        println(result)
    }
    
```

coroutine builders
launch : starts a new coroutine without blocking the current thread and returns a reference to the coroutine as a job
async : start a new coroutine and returns a deferred object, which is a non-blocking cancellable future that represent the result

```kotlin
    //in viewModel or any lifecycle-aware component
    viewModelScope.launch { // launch a coroutine in the viewModelScope -> coroutine will be automatically cancelled when the ViewModel is cleared(prevent memory leak)
        val result = fetchData()
        //update UI with the fetched data (it is safe to update the UI here since it is inside the coroutine launched in viewModel)
    }
```