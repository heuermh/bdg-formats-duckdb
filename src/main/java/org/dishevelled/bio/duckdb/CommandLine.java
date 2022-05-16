package org.dishevelled.bio.duckdb;

final class CommandLine extends picocli.CommandLine {

    /**
     * Create a new command line for the specified command.
     *
     * @param command command
     */
    CommandLine(final Object command) {
        super(command);
        setUsageHelpLongOptionsMaxWidth(42);
    }
}
