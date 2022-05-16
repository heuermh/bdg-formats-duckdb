# bdg-formats-duckdb
Convert bdg schema in Apache Parquet format using DuckDB

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
