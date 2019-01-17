package my_pkg

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import Cfg._

class MaccUnitTest(c: DspTop) extends PeekPokeTester(c) {}

class MaccTester extends ChiselFlatSpec {

  private val backendNames = Array("verilator")

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new DspTop) {
      c => new MaccUnitTest(c)
    } should be (true)
  }

  "using --backend-name verilator" should "be an alternative way to run using verilator" in {
    if(backendNames.contains("verilator")) {
      iotesters.Driver.execute(Array("--backend-name", "verilator"), () => new DspTop) {
        c => new MaccUnitTest(c)
      } should be(true)
    }
  }

  //"running with --is-verbose" should "show more about what's going on in your tester" in {
  //  iotesters.Driver.execute(Array("--is-verbose"), () => new DspTop(dwin, dwout)) {
  //    c => new MaccUnitTest(c)
  //  } should be(true)
  //}
}


object MaccMain extends App {
  iotesters.Driver.executeFirrtlRepl(args, () => new DspTop) 
}

