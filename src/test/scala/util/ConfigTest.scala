package util

import org.grapheco.commons.util.ConfigurationEx
import org.junit.{Assert, Test}

/**
  * Created by bluejoe on 2020/3/11.
  */
class ConfigTest {
  @Test
  def test1(): Unit = {
    val conf = new ConfigurationEx(Map("a" -> "1", "b" -> "hello", "c" -> "red", "d" -> "false"))
    Assert.assertEquals(1, conf.get("a").asInt)
    Assert.assertEquals("hello", conf.get("b").asString)
    Assert.assertEquals(1, conf.get("c").withOptions(Map("red" -> 1, "blue" -> 2)).asInt)
    Assert.assertEquals("#ff0000", conf.get("c").withOptions(Map("red" -> "#ff0000", "blue" -> "#0000ff")).asString)
    Assert.assertEquals(false, conf.get("d").asBoolean)
    assertExceptionThrown {
      conf.get("x")
    }
    assertExceptionThrown {
      conf.get("b").asInt
    }

    Assert.assertEquals("hello", conf.get("x").withDefault("hello").asString)
    Assert.assertEquals(1, conf.get("x").withDefault(1).asInt)
  }

  private def assertExceptionThrown(body: => Unit): Unit = {
    try {
      body
      Assert.assertTrue(false)
    }
    catch {
      case e: Throwable =>
        Assert.assertTrue(true)
        println(e.getMessage)
    }
  }
}
