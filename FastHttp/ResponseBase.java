package pw.onlyou.easy.FastHttp;

/**
 * Created by 立才 on 2017/8/10.
 */

public class ResponseBase  implements  ResponseInterface{

    @Override
    public boolean is_resp_ok() {
        return false;
    }

    @Override
    public String get_message() {
        return null;
    }

    @Override
    public int get_code() {
        return 0;
    }
}
