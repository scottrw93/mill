package mill.api

import mill.api.daemon.{ExecResult, Segments, Val}

/**
 * Observer for build lifecycle events, similar to Maven's EventSpy.
 *
 * Register listeners by overriding [[Module.buildListeners]] in your module.
 * Listeners are called by the execution layer at task boundaries.
 */
trait BuildListener {

  /**
   * Called once before the build begins executing any tasks.
   *
   * @param goals the top-level tasks requested for this build invocation
   */
  def onBuildStart(goals: Seq[Segments]): Unit = ()

  /**
   * Called once after the build finishes, whether it succeeded or failed.
   *
   * @param durationMillis total wall-clock duration of the build in milliseconds
   * @param success false if any task failed
   */
  def onBuildEnd(durationMillis: Long, success: Boolean): Unit = ()

  /**
   * Called before a task group begins execution.
   *
   * @param segments the fully qualified task path (e.g. "core.api.compile")
   */
  def onTaskStart(segments: Segments): Unit = ()

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
