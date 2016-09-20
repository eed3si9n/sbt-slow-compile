val sub0 = project
val sub1 = project.dependsOn(sub0)
val sub2 = project.dependsOn(sub0, sub1)
val sub3 = project.dependsOn(sub0, sub1, sub2)
val sub4 =
  project.
  dependsOn(sub0, sub1, sub2, sub3).
  settings(
    commands += Command.command("check") { s0 =>
      val extracted = (Project extract s0)
      timed("running compile") {
        val (s1, _) = extracted.runTask((compile in Compile), s0)
        s1
      }
    }
  )

lazy val root = (project in file(".")).aggregate(sub0, sub1, sub2, sub3, sub4).settings(sourcesInBase := false)

def timed[T](label: String)(t: => T): T = {
  val start = System.nanoTime
  val result = t
  val elapsed = System.nanoTime - start
  println(label + " took " + (elapsed / 1e6) + " ms")
  result
}
