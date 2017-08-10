package cn.haodian.demowidget;

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
    private final static String dirStr = "./app/src/main/res";//androidStudio开发工具

    private final static String  pxs[]={"1080x1920","720x1280","480x854","540x960","720x1184","1080x1812","1080x1800","480x800","1080x1794","720x1200","720x1196"};

    public static void generateXmlFile() {

        for (String px:pxs) {
            StringBuffer sbForWidth = new StringBuffer();
            sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            sbForWidth.append("<resources>");
            String[] xy=px.split("x");
            float cell =Integer.parseInt(xy[0])  * 1.0f / BASE;

            for (int i = 1; i < BASE; i++) {
                sbForWidth.append(Template.replace("{0}", i + "").replace("{1}",
                        changeToDouble(cell * i) + ""));
            }

            sbForWidth.append(Template.replace("{0}", BASE + "").replace("{1}",
                    xy[0] + ""));

            sbForWidth.append("</resources>");

            StringBuffer sbForHeight = new StringBuffer();
            sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            sbForHeight.append("<resources>");


            File fileDir = new File(dirStr + File.separator
                    + VALUE_TEMPLATE.replace("{0}", xy[1] + "").replace("{1}", xy[0] + ""));
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
    }

    private static float changeToDouble(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    public static void main(String[] args) {
        generateXmlFile();
    }

}