package mill.api

import scala.util.DynamicVariable

/**
 * Mapped Diagnostic Context for Mill's logging system, similar to SLF4J's MDC.
 *
 * MDC provides a thread-local map of key-value pairs that are automatically
 * included in log output. Context is propagated across thread boundaries
 * when Mill spawns async tasks via `fork.async`.
 *
 * {{{
 *   MDC.put("portalId", "12345")
 *   log.info("Processing request") // => [1] {portalId=12345} Processing request
 *   MDC.remove("portalId")
 * }}}
 */
object MDC {
  private[mill] val context: DynamicVariable[Map[String, String]] =
    new DynamicVariable(Map.empty)

  def put(key: String, value: String): Unit =
    context.value = context.value.updated(key, value)

  def get(key: String): Option[String] = context.value.get(key)

  def remove(key: String): Unit =
    context.value = context.value - key

  def getContextMap: Map[String, String] = context.value

  def setContextMap(map: Map[String, String]): Unit =
    context.value = map

  def clear(): Unit =
    context.value = Map.empty

  def withContext[T](entries: (String, String)*)(body: => T): T =
    context.withValue(context.value ++ entries)(body)

  private[mill] def formatMDC: String = {
    val m = context.value
    if (m.isEmpty) ""
    else m.toSeq.sortBy(_._1).map { case (k, v) => s"$k=$v" }.mkString("{", ", ", "} ")
  }
}
