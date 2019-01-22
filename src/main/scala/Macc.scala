package my_pkg

import chisel3._
//import Cfg._
import complex_pkg._
import ring_pkg._


class Dummy[A <: Data:Ring] (dType: A) extends Module {
 
   val io = IO (new Bundle {
     
    val vld  = Input (Bool())
    val clr  = Input (Bool())
        
    val a    = Input (dType)
    val b    = Input (dType)

    val rdy  = Output (Bool())
    val res  = Output (dType)
       
   })
  
    def plus[A <: Data:Ring] ( a:A, b:A): A = {
      val ev = implicitly[Ring[A]]
      val res  = ev.+(a,b)
      res
    }

    io.rdy := true.B
    io.res := plus (io.a, io.b)

}


// DSP top wrapper
class DspTop[A <: Data:Ring, B <: Data:Ring] (inType:A, outType:B) extends Module  {

  val io = IO(new Bundle {

    val vld = Input(Bool())
    val clr = Input(Bool())
    val a   = Input(inType)
    val b   = Input(inType)

    val rdy = Output(Bool())
    val res = Output(outType)

  })

  val inst = Module(new Dummy(inType))

  inst.io <> io

}

