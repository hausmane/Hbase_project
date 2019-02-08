package hbaseApp.hbaseApp;
import java.io.*;
import java.util.List;


public class HBaseApplication implements Closeable {

    public static final String ID = "09";

    private HBaseService hBaseService;

    public HBaseApplication() throws IOException {
        hBaseService = new HBaseService();
    }

    public void load(String dirPath) throws IOException {
        File dir = new File(dirPath);
        for (File file : dir.listFiles()) {
            hBaseService.load(new FileReader(file));
        }
    }

    public void query1(String lang, int n, long timeStart, long timeEnd, String outputDir) throws IOException {
        List<ResultItem> resultItems = hBaseService.query1(lang, n, timeStart, timeEnd);
        save(resultItems, new File(outputDir, ID + "_query1.out"));
    }

    public void query2(String[] langs, int n, long timeStart, long timeEnd, String outputDir) throws IOException {
        for (String lang : langs) {
            List<ResultItem> resultItems = hBaseService.query1(lang, n, timeStart, timeEnd);
            save(resultItems, new File(outputDir, ID + "_query2.out"));
        }
    }

    public void query3(int n, long timeStart, long timeEnd, String outputDir) throws IOException {
        List<ResultItem> resultItems = hBaseService.query3(n, timeStart, timeEnd);
        save(resultItems, new File(outputDir, ID + "_query3.out"));
    }

    private void save(List<ResultItem> resultItems, File file) throws IOException {
        file.getParentFile().mkdirs();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        for (ResultItem resultItem : resultItems) {
            bufferedWriter.write(resultItem.getLanguage() + "," + resultItem.getPosition() + ","
                    + resultItem.getHashTag() + "," + resultItem.getTimeStart() + "," + resultItem.getTimeEnd() + "\n");
        }
        bufferedWriter.close();
    }


    public void close() throws IOException {
        hBaseService.close();
    }

    public static void main(String[] args) throws IOException {
        HBaseApplication hBaseApplication = new HBaseApplication();
        switch (args[0]) {
            case "1":
                hBaseApplication.query1(args[4], Integer.valueOf(args[3]), Long.valueOf(args[1]), Long.valueOf(args[2]),
                        args[5]);
                break;
            case "2":
                hBaseApplication.query2(args[4].split(","), Integer.valueOf(args[3]), Long.valueOf(args[1]), Long.valueOf(args[2]),
                        args[5]);
                break;
            case "3":
                hBaseApplication.query3(Integer.valueOf(args[3]), Long.valueOf(args[1]), Long.valueOf(args[2]),
                        args[4]);
                break;
            case "4":
                hBaseApplication.load(args[1]);
                break;
        }
        hBaseApplication.close();
    }

}