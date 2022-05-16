package org.dishevelled.bio.duckdb;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
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
