package cn.haodian.demowidget;

import android.content.Context;
import android.util.DisplayMetrics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Dimens {

    //基数越大越准
    private final static int BASE=160;
    private final static String Template = "<dimen name=\"dimens_{0}\">{1}px</dimen>\n";
    private final static String VALUE_TEMPLATE = "values-{0}x{1}";
    private final static String FILE_NAME = "dimens.xml";
    private final static String dirStr = "./res";

    public static void generateXmlFile(Context context) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int Width = dm2.widthPixels;
        int Height = dm2.heightPixels;

        //检查dimens.xml 是否存在
        String target_file=dirStr + File.separator
                + VALUE_TEMPLATE.replace("{0}", Height + "").replace("{1}", Width + "")+ File.separator+FILE_NAME;
        File dir_file = new File(target_file);
        if (dir_file.exists()) {
            return;
        }

        StringBuffer sbForWidth = new StringBuffer();
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForWidth.append("<resources>");
        float cell = Width * 1.0f / BASE;

        for (int i = 1; i < BASE; i++) {
            sbForWidth.append(Template.replace("{0}", i + "").replace("{1}",
                    changeToDouble(cell * i) + ""));
        }

        sbForWidth.append(Template.replace("{0}", BASE + "").replace("{1}",
                Width+ ""));

        sbForWidth.append("</resources>");

        StringBuffer sbForHeight = new StringBuffer();
        sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForHeight.append("<resources>");


        File fileDir = new File(dirStr + File.separator
                + VALUE_TEMPLATE.replace("{0}", Height + "").replace("{1}", Width + ""));
        fileDir.mkdir();

        File file = new File(fileDir.getAbsolutePath(), FILE_NAME);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            pw.print(sbForWidth.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static float changeToDouble(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

}