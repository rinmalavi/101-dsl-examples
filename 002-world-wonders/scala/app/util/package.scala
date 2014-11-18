package object models {

  /** We're using a cached thread pool executor,
    * which has a growing pool of threads which we reuse for IO requests */
  implicit val executionContext =
    scala.concurrent.ExecutionContext.fromExecutor(
      java.util.concurrent.Executors.newCachedThreadPool
    )
}
