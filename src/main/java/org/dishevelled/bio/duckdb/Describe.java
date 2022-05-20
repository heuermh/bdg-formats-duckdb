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
 * Describe Parquet file columns as loaded by DuckDB.
 */
@Command(name = "describe")
public final class Describe implements Callable<Integer> {

    @Option(names = { "-i", "--input-parquet-file" }, required = true)
    private File inputParquetFile = null;

    @Option(names = { "-u", "--url" })
    private String url = "jdbc:duckdb:";

    private static final String DESCRIBE_SQL = "SELECT * from read_parquet('%s') WHERE 1=0";

    @Override
    public Integer call() throws Exception {

        // connect to DuckDB
        Class.forName("org.duckdb.DuckDBDriver");
        try (Connection connection = DriverManager.getConnection(url)) {

            // describe Parquet file columns as loaded by DuckDB
            try (Statement create = connection.createStatement()) {
                String sql = String.format(DESCRIBE_SQL, inputParquetFile.toString());
                try (ResultSet resultSet = create.executeQuery(sql)) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 1; i < (metaData.getColumnCount() + 1); i++) {
                        System.out.println(i + "\t" + metaData.getColumnName(i) + "\t" + metaData.getColumnTypeName(i));
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
        System.exit(new CommandLine(new Describe()).execute(args));
    }
}
