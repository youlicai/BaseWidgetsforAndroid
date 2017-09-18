package pw.onlyou.easy.FastHttp;

/**
 * Created by 立才 on 2017/9/15.
 */

public interface ResponseInterface {
    boolean is_resp_ok();
    String get_message();
    int get_code();
}
