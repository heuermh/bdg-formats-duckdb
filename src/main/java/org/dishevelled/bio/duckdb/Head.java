package org.dishevelled.bio.duckdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.util.List;
import java.util.Map;

import java.util.concurrent.Callable;

import com.google.gson.stream.JsonWriter;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Write the first n records from a Parquet file in JSON format.
 */
@Command(name = "head")
public final class Head implements Callable<Integer> {

    @Option(names = { "-i", "--input-parquet-file" }, required = true)
    private File inputParquetFile = null;

    @Option(names = { "-u", "--url" })
    private String url = "jdbc:duckdb:";

    @Option(names = { "-n", "--number" })
    private int number = 5;

    @Option(names = { "-p", "--pretty-print" })
    private boolean prettyPrint = false;

    private static final String HEAD_SQL = "SELECT * from read_parquet('%s') LIMIT %d";

    @Override
    public Integer call() throws Exception {
        // create JSON writer
        try (JsonWriter writer = new JsonWriter(new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8")))) {
            if (prettyPrint) {
                writer.setIndent("  ");
            }
            writer.setSerializeNulls(false);
            writer.beginArray();

            // connect to DuckDB
            Class.forName("org.duckdb.DuckDBDriver");
            try (Connection connection = DriverManager.getConnection(url)) {

                // query head records from Parquet file
                try (Statement create = connection.createStatement()) {
                    String sql = String.format(HEAD_SQL, inputParquetFile.toString(), number);
                    try (ResultSet resultSet = create.executeQuery(sql)) {
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columns = metaData.getColumnCount() + 1;

                        // write JSON to stdout
                        while (resultSet.next()) {
                            writer.beginObject();

                            for (int i = 1; i < columns; i++) {
                                Object value = resultSet.getObject(i);

                                if (value != null) {
                                    writer.name(metaData.getColumnName(i));
                                    if (value instanceof Boolean) {
                                        writer.value((Boolean) value);
                                    }
                                    else if (value instanceof Number) {
                                        writer.value((Number) value);
                                    }
                                    else {
                                        writer.value(value.toString());
                                    }
                                }
                            }

                            writer.endObject();
                        }
                    }
                }
            }
            writer.endArray();
        }
        return 0;
    }

    /**
     * Main.
     *
     * @param args command line args
     */
    public static void main(final String[] args) {
        System.exit(new CommandLine(new Head()).execute(args));
    }
}
