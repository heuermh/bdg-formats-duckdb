package org.dishevelled.bio.duckdb;

import java.util.List;

import picocli.AutoComplete.GenerateCompletion;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;

/**
 * DuckDB tools.
 */
@Command(
  name = "duckdb-tools",
  scope = ScopeType.INHERIT,
  subcommands = {
      Convert.class,
      Create.class,
      HelpCommand.class,
      GenerateCompletion.class
  },
  mixinStandardHelpOptions = true,
  sortOptions = false,
  usageHelpAutoWidth = true,
  resourceBundle = "org.dishevelled.bio.duckdb.Messages",
  versionProvider = org.dishevelled.bio.duckdb.About.class
)
public final class Tools {

    @Parameters(hidden = true)
    private List<String> ignored;

    /**
     * Main.
     *
     * @param args command line args
     */
    public static void main(final String[] args) {
        System.exit(new CommandLine(new Tools()).execute(args));
    }
}
