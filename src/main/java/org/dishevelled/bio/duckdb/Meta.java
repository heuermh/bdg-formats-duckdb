package org.dishevelled.bio.duckdb;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.util.List;
import java.util.Map;

import java.util.concurrent.Callable;

import org.apache.commons.dbutils.QueryRunner;

import org.apache.commons.dbutils.handlers.MapListHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Query the metadata of a Parquet file as loaded by DuckDB.
 */
@Command(name = "meta")
public final class Meta implements Callable<Integer> {

    @Option(names = { "-i", "--input-parquet-file" }, required = true)
    private File inputParquetFile = null;

    @Option(names = { "-u", "--url" })
    private String url = "jdbc:duckdb:";

    private static final String META_SQL = "SELECT * from parquet_metadata('%s')";

    @Override
    public Integer call() throws Exception {

        // connect to DuckDB
        Class.forName("org.duckdb.DuckDBDriver");
        try (Connection connection = DriverManager.getConnection(url)) {

            // meta Parquet file columns as loaded by DuckDB
            try (Statement create = connection.createStatement()) {
                String sql = String.format(META_SQL, inputParquetFile.toString());
                try (ResultSet resultSet = create.executeQuery(sql)) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columns = metaData.getColumnCount() + 1;

                    // print header
                    for (int i = 1; i < columns; i++) {
                        System.out.print(metaData.getColumnLabel(i) + "\t");
                    }
                    System.out.print("\n");

                    // print rows
                    while (resultSet.next()) {
                        for (int i = 1; i < columns; i++) {
                            Object value = resultSet.getObject(i);
                            System.out.print(value == null ? "" : value.toString() + "\t");
                        }
                        System.out.print("\n");
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Main.
     *
     * @param args command line args
     */
    public static void main(final String[] args) {
        System.exit(new CommandLine(new Meta()).execute(args));
    }
}
