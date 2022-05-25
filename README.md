# bdg-formats-duckdb
Convert bdg schema in Apache Parquet format using DuckDB

### Hacking

Install

 * JDK 1.8 or later, https://openjdk.java.net
 * Apache Maven 3.3.9 or later, https://maven.apache.org
 * Apache Parquet Tools, https://stackoverflow.com/questions/53306327/installing-parquet-tools
 * Apache Spark 3.2.1 or later, http://spark.apache.org
 * ADAM: Genomic Data System 1.0 or later, https://github.com/bigdatagenomics/adam

To build
```
$ mvn package

$ export PATH=$PATH:`pwd`/target/appassembler/bin

$ duckdb-tools --help
USAGE
  duckdb-tools [-hV] [COMMAND]

OPTIONS
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.

COMMANDS
  convert              Convert input Parquet file to DuckDB as Parquet file.
  create               Create DuckDB table and write as Parquet file.
  describe             Describe Parquet file columns as loaded by DuckDB.
  head                 Write the first n records from a Parquet file in JSON format.
  meta                 Query the metadata of a Parquet file as loaded by DuckDB.
  schema               Query the internal schema of a Parquet file as loaded by DuckDB.
  help                 Display help information about the specified command.
  generate-completion  Generate bash/zsh completion script for duckdb-tools.
```

### Summary

Parquet schema are defined via Avro in [bigdatagenomics/bdg-formats](https://github.com/bigdatagenomics/bdg-formats)
and written to disk by [bigdatagenomics/adam](https://github.com/bigdatagenomics/adam), e.g.
```
$ parquet-tools schema alignments.adam/part-r-00000.gz.parquet
message org.bdgenomics.formats.avro.Alignment {
  optional binary referenceName (STRING);
  optional int64 start;
  optional int64 originalStart;
  optional int64 end;
  optional int32 mappingQuality;
  optional binary readName (STRING);
  optional binary sequence (STRING);
  optional binary qualityScores (STRING);
  optional binary cigar (STRING);
  optional binary originalCigar (STRING);
  optional int32 basesTrimmedFromStart;
  optional int32 basesTrimmedFromEnd;
  optional boolean readPaired;
  optional boolean properPair;
  optional boolean readMapped;
  optional boolean mateMapped;
  optional boolean failedVendorQualityChecks;
  optional boolean duplicateRead;
  optional boolean readNegativeStrand;
  optional boolean mateNegativeStrand;
  optional boolean primaryAlignment;
  optional boolean secondaryAlignment;
  optional boolean supplementaryAlignment;
  optional binary mismatchingPositions (STRING);
  optional binary originalQualityScores (STRING);
  optional binary readGroupId (STRING);
  optional binary readGroupSampleId (STRING);
  optional int64 mateAlignmentStart;
  optional binary mateReferenceName (STRING);
  optional int64 insertSize;
  optional int32 readInFragment;
  optional binary attributes (STRING);
}
```

### Results

```
$ adam-shell -i generate.scala
```

#### Alignments
```
$ duckdb-tools convert -i alignments.adam/part-r-00000.gz.parquet -o convert.parquet

$ parquet-tools schema convert.parquet
message duckdb_schema {
  optional binary referenceName (STRING);
  optional int64 start (INTEGER(64,true));
  optional int64 originalStart (INTEGER(64,true));
  optional int64 end (INTEGER(64,true));
  optional int32 mappingQuality (INTEGER(32,true));
  optional binary readName (STRING);
  optional binary sequence (STRING);
  optional binary qualityScores (STRING);
  optional binary cigar (STRING);
  optional binary originalCigar (STRING);
  optional int32 basesTrimmedFromStart (INTEGER(32,true));
  optional int32 basesTrimmedFromEnd (INTEGER(32,true));
  optional boolean readPaired;
  optional boolean properPair;
  optional boolean readMapped;
  optional boolean mateMapped;
  optional boolean failedVendorQualityChecks;
  optional boolean duplicateRead;
  optional boolean readNegativeStrand;
  optional boolean mateNegativeStrand;
  optional boolean primaryAlignment;
  optional boolean secondaryAlignment;
  optional boolean supplementaryAlignment;
  optional binary mismatchingPositions (STRING);
  optional binary originalQualityScores (STRING);
  optional binary readGroupId (STRING);
  optional binary readGroupSampleId (STRING);
  optional int64 mateAlignmentStart (INTEGER(64,true));
  optional binary mateReferenceName (STRING);
  optional int64 insertSize (INTEGER(64,true));
  optional int32 readInFragment (INTEGER(32,true));
  optional binary attributes (STRING);
}
```

#### Features
```
$ duckdb-tools convert -i features.adam/part-r-00000.gz.parquet -o convert.parquet
java.sql.SQLException: IO Error: Unsupported converted type
```

#### Fragments
```
$ duckdb-tools convert -i fragments.adam/part-r-00000.gz.parquet -o convert.parquet

$ parquet-tools schema convert.parquet
message duckdb_schema {
  optional binary name (STRING);
  optional binary readGroupId (STRING);
  optional int32 insertSize (INTEGER(32,true));
  optional group alignments (LIST) {
    repeated group list {
      optional group element {
        optional binary referenceName (STRING);
        optional int64 start (INTEGER(64,true));
        optional int64 originalStart (INTEGER(64,true));
        optional int64 end (INTEGER(64,true));
        optional int32 mappingQuality (INTEGER(32,true));
        optional binary readName (STRING);
        optional binary sequence (STRING);
        optional binary qualityScores (STRING);
        optional binary cigar (STRING);
        optional binary originalCigar (STRING);
        optional int32 basesTrimmedFromStart (INTEGER(32,true));
        optional int32 basesTrimmedFromEnd (INTEGER(32,true));
        optional boolean readPaired;
        optional boolean properPair;
        optional boolean readMapped;
        optional boolean mateMapped;
        optional boolean failedVendorQualityChecks;
        optional boolean duplicateRead;
        optional boolean readNegativeStrand;
        optional boolean mateNegativeStrand;
        optional boolean primaryAlignment;
        optional boolean secondaryAlignment;
        optional boolean supplementaryAlignment;
        optional binary mismatchingPositions (STRING);
        optional binary originalQualityScores (STRING);
        optional binary readGroupId (STRING);
        optional binary readGroupSampleId (STRING);
        optional int64 mateAlignmentStart (INTEGER(64,true));
        optional binary mateReferenceName (STRING);
        optional int64 insertSize (INTEGER(64,true));
        optional int32 readInFragment (INTEGER(32,true));
        optional binary attributes (STRING);
      }
    }
  }
}
```

#### Genotypes
```
$ duckdb-tools convert -i genotypes.adam/part-r-00000.gz.parquet -o convert.parquet
java.sql.SQLException: IO Error: Unsupported converted type
```

#### Reads
```
$ duckdb-tools convert -i reads.adam/part-r-00000.gz.parquet -o convert.parquet
java.sql.SQLException: IO Error: Unsupported converted type
```

#### Sequences
```
$ duckdb-tools convert -i sequences.adam/part-r-00000.gz.parquet -o convert.parquet
java.sql.SQLException: IO Error: Unsupported converted type
```

#### Slices
```
$ duckdb-tools convert -i slices.adam/part-r-00000.gz.parquet -o convert.parquet
java.sql.SQLException: IO Error: Unsupported converted type
```

#### Variants
```
$ duckdb-tools convert -i variants.adam/part-r-00000.gz.parquet -o convert.parquet
java.sql.SQLException: IO Error: Unsupported converted type
```
