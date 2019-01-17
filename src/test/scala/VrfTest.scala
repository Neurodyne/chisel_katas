
package  my_pkg

import  chisel3.iotesters
import  chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class VrfUnitTest (c: VrfShell) extends PeekPokeTester (c) {
  
}

class VrfTester extends ChiselFlatSpec {
  private val backendNames = Array("verilator")
  
  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new VrfShell) {
      c => new VrfUnitTest(c)
    } should be (true)
  }

  "using --backend-name verilator" should "be an alternative way to run using verilator" in {
    if(backendNames.contains("verilator")) {
      iotesters.Driver.execute(Array("--backend-name", "verilator"), () => new VrfShell) {
        c => new VrfUnitTest(c)
      } should be(true)
    }
  }

}

object VrfMain extends App {
  iotesters.Driver.executeFirrtlRepl(args, () => new VrfShell) 
}

