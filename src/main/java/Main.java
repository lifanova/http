import ru.lifanova.dto.Cat;
import ru.lifanova.http.HttpUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://jsonplaceholder.typicode.com/posts";
    public static final String CAT_REMOTE_SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        HttpUtils utils = new HttpUtils();
        List<Cat> cats = utils.processGet(CAT_REMOTE_SERVICE_URL);

        if (cats == null) {
            System.out.println("ERROR!");
            return;
        }

        cats = cats
                .stream()
                .filter(x -> x.getUpvotes() != null && x.getUpvotes() > 0)
                .collect(Collectors.toList());
        cats.forEach(x -> System.out.println(x.toString()));
    }
}
