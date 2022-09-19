import ru.lifanova.http.HttpGetProcessor;
import ru.lifanova.http.HttpUtils;

public class Main {
    public static void main(String[] args) {
        HttpUtils utils = new HttpUtils();
        utils.processGet();
        utils.processPost();

//        HttpGetProcessor processor = new HttpGetProcessor();
//        processor.process();
    }
}
