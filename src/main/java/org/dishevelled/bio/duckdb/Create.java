package org.dishevelled.bio.duckdb;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Create DuckDB table and write as Parquet file.
 */
@Command(name = "create")
public final class Create implements Callable<Integer> {

    @Option(names = { "-s", "--create-table-sql" }, required = true)
    private String createTableSql;

    @Option(names = { "-o", "--output-parquet-file" }, required = true)
    private File outputParquetFile = null;

    @Option(names = { "-u", "--url" })
    private String url = "jdbc:duckdb:";

    @Option(names = { "-c", "--codec" })
    private String parquetCodec = "ZSTD";

    // java.sql.SQLException: Parser Error: syntax error at or near "?"
    // LINE 1: COPY records TO ? (FORMAT 'PARQUET', CODEC ?)
    //private static final String COPY_SQL = "COPY records TO ? (FORMAT 'PARQUET', CODEC ?)";

    @Override
    public Integer call() throws Exception {

        // connect to DuckDB
        Class.forName("org.duckdb.DuckDBDriver");
        try (Connection connection = DriverManager.getConnection(url)) {

            // create in-memory DuckDB table
            try (Statement create = connection.createStatement()) {
                create.execute(createTableSql);
            }

            // copy records from DuckDB table to disk as Parquet file
            /*
            try (PreparedStatement copy = connection.prepareStatement(COPY_SQL)) {
                copy.setString(1, outputParquetFile.toString());
                copy.setString(2, parquetCodec);
                copy.execute();
            }
            */
            try (Statement copy = connection.createStatement()) {
                String sql = "COPY records TO '" + outputParquetFile.toString() + "' (FORMAT 'PARQUET', CODEC '" + parquetCodec + "')";
                copy.execute(sql);
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
        System.exit(new CommandLine(new Create()).execute(args));
    }
}
