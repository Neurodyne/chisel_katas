package my_pkg

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import Cfg._
import ring_pkg._

class MaccUnitTest[A <: Data:Ring, B <: Data:Ring] (inType:A, outType:B)(c: DspTop[A,B]) extends PeekPokeTester(c) {}

class MaccTester extends ChiselFlatSpec {

  private val backendNames = Array("verilator")

  "Basic for SInt" should "nice" in {
    println (s"Running UInt test")

    val inType  = SInt(dwin.W)
    val outType = SInt(dwout.W)

    iotesters.Driver.execute(Array(), () => new DspTop(inType, outType)) {
      c => new MaccUnitTest(inType, outType)(c)
    } should be (true)
  }
  
  "Basic for UInt" should "nice" in {
    println (s"Running SInt test")

    val inType  = SInt(dwin.W)
    val outType = SInt(dwout.W)


    iotesters.Driver.execute(Array(), () => new DspTop(inType, outType)) {
      c => new MaccUnitTest(inType, outType)(c)
    } should be (true)
  }

  //"using --backend-name verilator" should "be an alternative way to run using verilator" in {
  //  if(backendNames.contains("verilator")) {
  //    iotesters.Driver.execute(Array("--backend-name", "verilator"), () => new DspTop(inType, outType)) {
  //      c => new MaccUnitTest(c)
  //    } should be(true)
  //  }
  //}

  //"running with --is-verbose" should "show more about what's going on in your tester" in {
  //  iotesters.Driver.execute(Array("--is-verbose"), () => new DspTop(dwin, dwout)) {
  //    c => new MaccUnitTest(c)
  //  } should be(true)
  //}
}


object MaccMain extends App {

  // Run tests for multiple data types
  tests map { case (first, second) =>
    
    // Direct type inference 
    //iotesters.Driver.executeFirrtlRepl(args, () => new DspTop(first, second)) 

    // Using Pattern matching

    //val (inType, outType)  = t match {
    //  case ui: ( (UInt(dwin.W), UInt(dwout.W) ) ) => ui
    //  //case ui:(UInt, UInt) => ui
    //  case _ => ((UInt(dwin.W), UInt(dwout.W)))
    //}
 
    //iotesters.Driver.executeFirrtlRepl(args, () => new DspTop(inType, outType)) 
  }

  

}

