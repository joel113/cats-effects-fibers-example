package com.joel.fibers
package Fibers

import cats.effect.{IO, IOApp}
import cats.syntax.all._
import cats.implicits.catsSyntaxApplicative

object Fibers extends IOApp.Simple {

  def repeat(letter: String): IO[Unit] =
    IO.print(letter).replicateA(100).void

  override def run: IO[Unit] = {
    for {
      fa <- (repeat("A") *> repeat("B")).as("foo!").start
      fb <- (repeat("C") *> repeat("D")).as("bar!").start
      //ra <- fa.join // waits for the completion of the fiber
      //rb <- fb.join // waits for the completion of the fiber
      // joinWithNever is a variant of join that asserts
      // the fiber has an outcome of Succeeded and returns the
      // associated value.
      ra <- fa.joinWithNever
      rb <- fb.joinWithNever
      _ <- IO.println(s"\ndone: a says: $ra, b says: $rb")
    } yield ()
  }
}
