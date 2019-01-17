// This pkg contains global parameters, functions and implicits

import chisel3._
import chisel3.core.{FixedPoint => FP}
import complex_pkg._

package object Cfg {

  // Full bit size
  val dwin  = 14
  val dwout = 20
 
  // Fractional bit size
  val qin  = 10
  val qout = 10
  
  val inType  = SInt(dwin.W)
  val outType = SInt(dwout.W)
  //val inType  = FP ( dwin.W , qin.BP  )
  //val outType = FP ( dwout.W, qout.BP )
  //val inType  = Complex ( FP(dwin.W , qin.BP  ), FP(dwin.W , qin.BP  ))
  //val outType = Complex ( FP(dwout.W, qout.BP ), FP(dwout.W, qout.BP ))
  
  val inZero  = 0.U(inType.getWidth.W).asTypeOf(inType)
  val outZero = 0.U(outType.getWidth.W).asTypeOf(outType)
}

