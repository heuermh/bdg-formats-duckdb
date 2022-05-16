import org.bdgenomics.adam.ds.ADAMContext._
import org.bdgenomics.adam.ds.feature.FeatureDataset
import org.bdgenomics.adam.ds.fragment.FragmentDataset
import org.bdgenomics.adam.ds.read.{ AlignmentDataset, ReadDataset }
import org.bdgenomics.adam.ds.sequence.{ SequenceDataset, SliceDataset }
import org.bdgenomics.adam.ds.variant.{ GenotypeDataset, VariantDataset }
import org.bdgenomics.adam.models._
import org.bdgenomics.formats.avro._
import scala.collection.JavaConverters._

val alignments = AlignmentDataset.unaligned(sc.parallelize(Seq(Alignment.newBuilder().setReadName("name").setSequence("ACGT").setQualityScores("GGGG").setReadMapped(false).build())))
alignments.saveAsParquet("alignments.adam")

val features = FeatureDataset(sc.parallelize(Seq(Feature.newBuilder().setReferenceName("1").setStart(1L).setEnd(100L).build())))
features.saveAsParquet("features.adam")

val a1 = Alignment.newBuilder().setReadName("name 1").setSequence("ACGT").setQualityScores("GGGG").build()
val a2 = Alignment.newBuilder().setReadName("name 2").setSequence("ACGT").setQualityScores("GGGG").build()
val f = Fragment.newBuilder().setName("fragment").setAlignments(Seq(a1, a2).asJava).build()
val fragments = FragmentDataset(sc.parallelize(Seq(f)), SequenceDictionary.empty, ReadGroupDictionary.empty, Seq())
fragments.saveAsParquet("fragments.adam")

val v = Variant.newBuilder().setNames(Seq("name").asJava).build()
val genotypes = GenotypeDataset(sc.parallelize(Seq(Genotype.newBuilder().setVariant(v).build())), SequenceDictionary.empty, Seq())
genotypes.saveAsParquet("genotypes.adam")

val sequences = SequenceDataset(sc.parallelize(Seq(Sequence.newBuilder().setName("name").setSequence("ACGT").build())))
sequences.saveAsParquet("sequences.adam")

val slices = SliceDataset(sc.parallelize(Seq(Slice.newBuilder().setName("name").setSequence("ACGT").build())))
slices.saveAsParquet("slices.adam")

val reads = ReadDataset(sc.parallelize(Seq(Read.newBuilder().setName("name").setSequence("ACGT").setQualityScores("GGGG").build())))
reads.saveAsParquet("reads.adam")

val variants = VariantDataset(sc.parallelize(Seq(v)), SequenceDictionary.empty)
variants.saveAsParquet("variants.adam")

System.exit(0)
