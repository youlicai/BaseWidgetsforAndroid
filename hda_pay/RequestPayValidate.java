//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.haodiana.hda_pay;

import java.util.Calendar;
import java.util.Date;

public class RequestPayValidate extends RequestBase {
    public String ds_trade_no;
    public String ds_id;
    public String mch_id;
    public long timestamp;
    public String version;
    public String pay_fee;
    public String trade_type;
    public String sign;

    public RequestPayValidate() {
        this.ds_id = SklStaticParams.DS_ID;
        this.mch_id = SklStaticParams.MCH_ID;
        this.trade_type = SklStaticParams.PAY_TYPE;
        this.timestamp = this.getMinute();
        this.version = SklStaticParams.VERSION;
        this.ds_trade_no = SklStaticParams.DS_TRADE_NO;
    }

    public long getMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int min = 10 * (calendar.get(Calendar.MINUTE) / 10);
        if (calendar.get(Calendar.MINUTE) % 10 >= 5) {
            min = min + 5;
        }
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), min, 0);
        return calendar.getTimeInMillis() / 1000;
    }
}
