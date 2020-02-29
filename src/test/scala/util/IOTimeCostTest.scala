package util

import java.io._
import java.nio.ByteBuffer

import org.apache.commons.io.IOUtils
import org.grapheco.commons.util.Profiler._
import org.grapheco.commons.util.{Profiler, StreamUtils}
import org.junit.Test

/**
  * Created by bluejoe on 2020/2/13.
  */
class IOTimeCostTest {
  Profiler.enableTiming = true

  @Test
  def testSerialize(): Unit = {
    val bytes = new Array[Byte](1024 * 1024);
    timing(true) {
      bytes.toArray
    }
    val bs2 = timing(true, 10) {
      StreamUtils.serializeObject(bytes)
    }

    println(bs2.length)
  }

  @Test
  def testByteBuffer(): Unit = {
    val bytes = (0 to 31).map(_.toByte).toArray

    timing(true) {
      val dis = new DataInputStream(new ByteArrayInputStream(bytes))
      println(dis.readLong(), dis.readLong())

      dis.close()
    }

    timing(true) {
      val buffer = ByteBuffer.wrap(bytes)
      println(buffer.getLong, buffer.getLong)
    }
  }
}
