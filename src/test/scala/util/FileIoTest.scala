package util

import java.io.{BufferedInputStream, BufferedOutputStream, File, FileInputStream, FileOutputStream}
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode

import org.apache.hadoop.hbase.io.ByteBufferInputStream
import org.grapheco.commons.util.Profiler
import org.junit.Test

import scala.io.Source

class FileIoTest {
  Profiler.enableTiming = true

  @Test
  def createFile(): Unit = {
    val fos = new BufferedOutputStream(new FileOutputStream(new File("./test.csv")))
    (1 to 10000000).foreach(i =>
      fos.write(s"f1:'abcdefgh',f2:$i\r\n".getBytes("utf-8"))
    )
    fos.close()
  }

  @Test
  def testRead(): Unit = {
    val file = new File("./test.csv")

    println("using Source.fromFile()")
    Profiler.timing() {
      val lines = Source.fromFile(file).getLines()
      println(lines.map(_.length).sum)
    }

    println
    println("using Source.fromFile() with larger buffer")
    Profiler.timing() {
      val lines = Source.fromFile(file, 10 * 1024 * 1024).getLines()
      println(lines.map(_.length).sum)
    }

    println
    println("using Source.fromInputStream()")
    Profiler.timing() {
      val fis = new BufferedInputStream(new FileInputStream(file))
      val lines = Source.fromInputStream(fis).getLines()
      println(lines.map(_.length).sum)
      fis.close()
    }

    println
    println("using Source.fromInputStream() with larger buffer")
    Profiler.timing() {
      val fis = new BufferedInputStream(new FileInputStream(file), 10 * 1024 * 1024)
      val lines = Source.fromInputStream(fis).getLines()
      println(lines.map(_.length).sum)
      fis.close()
    }

    println
    println("using mmap")
    Profiler.timing() {
      val fc = new FileInputStream(file).getChannel
      val buffer = fc.map(MapMode.READ_ONLY, 0, file.length())
      val lines = Source.fromInputStream(new ByteBufferInputStream(buffer)).getLines()
      println(lines.map(_.length).sum)
      fc.close()
    }
  }
}
