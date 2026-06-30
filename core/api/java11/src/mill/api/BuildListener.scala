package mill.api

import mill.api.daemon.{ExecResult, Logger, Segments, Val}

/**
 * Observer for build lifecycle events, similar to Maven's EventSpy.
 *
 * Register listeners by overriding [[Module.buildListeners]] in your module.
 * Listeners are called by the execution layer at task boundaries.
 */
trait BuildListener {

  /**
   * Called before a task group begins execution.
   *
   * @param segments the fully qualified task path (e.g. "core.api.compile")
   * @param logger the logger assigned to this task
   */
  def onTaskStart(segments: Segments, logger: Logger): Unit = ()

  /**
   * Called after a task group finishes execution.
   *
   * @param segments the fully qualified task path
   * @param durationMillis wall-clock duration in milliseconds
   * @param cached whether the result was served from cache
   * @param result the task result — success, failure, or skipped
   */
  def onTaskEnd(
      segments: Segments,
      durationMillis: Long,
      cached: Boolean,
      result: ExecResult[Val]
  ): Unit = ()
}

object BuildListener {
  val NoOp: BuildListener = new BuildListener {}
}
