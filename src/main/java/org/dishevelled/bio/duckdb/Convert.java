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
 * Convert input Parquet file to DuckDB as Parquet file.
 */
@Command(name = "convert")
public final class Convert implements Callable<Integer> {

    @Option(names = { "-i", "--input-parquet-file" }, required = true)
    private File inputParquetFile = null;

    @Option(names = { "-o", "--output-parquet-file" }, required = true)
    private File outputParquetFile = null;

    @Option(names = { "-u", "--url" })
    private String url = "jdbc:duckdb:";

    @Option(names = { "-c", "--codec" })
    private String parquetCodec = "ZSTD";

    // java.sql.SQLException: Binder Error: Table function requires a constant parameter
    // LINE 1: CREATE TABLE records AS SELECT * from read_parquet(?)
    //private static final String CREATE_SQL = "CREATE TABLE records AS SELECT * from read_parquet(?)";
    private static final String CREATE_SQL = "CREATE TABLE records AS SELECT * from read_parquet";
    private static final String COPY_SQL = "COPY records TO ? (FORMAT 'PARQUET', CODEC ?)";

    @Override
    public Integer call() throws Exception {

        // connect to DuckDB
        Class.forName("org.duckdb.DuckDBDriver");
        try (Connection connection = DriverManager.getConnection(url)) {

            // create in-memory DuckDB table from Parquet file
            /*
            try (PreparedStatement create = connection.prepareStatement(CREATE_SQL)) {
                create.setString(1, inputParquetFile.toString());
                create.execute();
            }
            */
            try (Statement create = connection.createStatement()) {
                String sql = CREATE_SQL + "('" + inputParquetFile.toString() + "')";
                create.execute(sql);
            }

            // copy records from DuckDB table to disk as Parquet file
            try (PreparedStatement copy = connection.prepareStatement(COPY_SQL)) {
                copy.setString(1, outputParquetFile.toString());
                copy.setString(2, parquetCodec);
                copy.execute();
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
        System.exit(new CommandLine(new Convert()).execute(args));
    }
}
