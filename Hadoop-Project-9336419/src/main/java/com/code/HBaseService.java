package com.presidentio.freelance.hadoopproject9336419;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by presidentio on 1/17/16.
 */
public class HBaseService implements Closeable {

    private static final String COLUMN_FAMILY = "hashtags";
    private static final String TABLE = "twitter";
    private static final String FIELD_HASHTAG = "hashtag";
    private static final String FIELD_LANG = "lang";
    private static final String FIELD_FREQUENCY = "frequency";

    private Connection connection;

    public HBaseService() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        try {
            admin.getTableDescriptor(TableName.valueOf(TABLE));
        } catch (TableNotFoundException e) {
            admin.createTable(new HTableDescriptor(TableName.valueOf(TABLE)).addFamily(new HColumnDescriptor(COLUMN_FAMILY)));
        }
        admin.close();
    }

    public void load(Reader reader) throws IOException {
        Table table = connection.getTable(TableName.valueOf(TABLE));
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] fields = line.split(",");
            long timestamp = Long.valueOf(fields[0]);
            String lang = fields[1];
            Put put = new Put(Bytes.toBytes(String.format("%09d", Long.valueOf(fields[3])) + ":" + fields[2] + ":" + lang), timestamp);
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_HASHTAG), Bytes.toBytes(fields[2]));
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_FREQUENCY), Bytes.toBytes(fields[3]));
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_LANG), Bytes.toBytes(lang));
            table.put(put);
            put = new Put(Bytes.toBytes(String.format("%09d", Long.valueOf(fields[5])) + ":" + fields[4] + ":" + lang), timestamp);
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_HASHTAG), Bytes.toBytes(fields[4]));
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_FREQUENCY), Bytes.toBytes(fields[5]));
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_LANG), Bytes.toBytes(lang));
            table.put(put);
            put = new Put(Bytes.toBytes(String.format("%09d", Long.valueOf(fields[7])) + ":" + fields[6] + ":" + lang), timestamp);
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_HASHTAG), Bytes.toBytes(fields[6]));
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_FREQUENCY), Bytes.toBytes(fields[7]));
            put.add(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_LANG), Bytes.toBytes(lang));
            table.put(put);
        }
        table.close();
    }

    public List<ResultItem> query1(String lang, int n, long timeStart, long timeEnd) throws IOException {
        Table table = connection.getTable(TableName.valueOf(TABLE));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(COLUMN_FAMILY));
        scan.setTimeRange(timeStart, timeEnd);
        scan.setReversed(true);
        FilterList filterList = new FilterList();
        filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_LANG),
                CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(lang))));
        filterList.addFilter(new PageFilter(n));
        scan.setFilter(filterList);
        ResultScanner resultScanner = table.getScanner(scan);
        List<ResultItem> resultItems = new ArrayList<ResultItem>(n);
        int position = 0;
        for (Result result : resultScanner) {
            String hashTag = Bytes.toString(result.getValue(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_HASHTAG)));
            resultItems.add(new ResultItem(lang, position++, hashTag, timeStart, timeEnd));
        }
        return resultItems;
    }

    public List<ResultItem> query3(int n, long timeStart, long timeEnd) throws IOException {
        Table table = connection.getTable(TableName.valueOf(TABLE));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(COLUMN_FAMILY));
        scan.setFilter(new PageFilter(n));
        scan.setTimeRange(timeStart, timeEnd);
        scan.setReversed(true);
        ResultScanner resultScanner = table.getScanner(scan);
        List<ResultItem> resultItems = new ArrayList<ResultItem>(n);
        int position = 0;
        for (Result result : resultScanner) {
            String hashTag = Bytes.toString(result.getValue(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_HASHTAG)));
            String lang = Bytes.toString(result.getValue(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(FIELD_LANG)));
            resultItems.add(new ResultItem(lang, position++, hashTag, timeStart, timeEnd));
        }
        return resultItems;
    }


    public void close() throws IOException {
        connection.close();
    }

}
