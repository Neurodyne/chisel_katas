
package  my_pkg  

import   chisel3._
import   chisel3.util.{MuxCase, PopCount}


class Link[A <: Data] (dType:A) extends Bundle {
  val data = Output (dType)
  override def cloneType = (new Link(dType)).asInstanceOf[this.type]
}

class Counter(size: Int) extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(size.W))
    val ena  = Input (Bool())
    val clr  = Input (Bool())
  })

  val tmp = RegInit (0.U(size.W))

  when (reset.asBool) {
    tmp := 0.U   
  }
  .otherwise {
    tmp := MuxCase(tmp, Array( io.clr -> 0.U, io.ena -> (tmp + 1.U)))
  }
  
  io.out := tmp
}

trait Ord[A] {
  def <=(a:A,b:A):A
}

object Ord {
  implicit val UIntOrd:Ord[UInt] = new Ord[UInt] {
    def <= (a:UInt, b:UInt):Bool = a < b
  }
}


sealed class AddrDecoder[A <: UInt] (baseSeq:Seq[A], sizeSeq:Seq[A], addrWidth:Int) extends Module {

  val bsize  = baseSeq.size 
  val ssize  = sizeSeq.size 
  require (bsize > 0, "Empty map passed")
  require (bsize == ssize, "Invalid initialization of AddrDecoder")
  
  val io = IO (new Bundle {
    val addr    = Input (UInt(addrWidth.W))      
    val en      = Input (Bool())  
    val sel     = Output(Vec(bsize,Bool()))  // $onehot0 selector
  })

  def inside[A <:UInt](range:(A,A))(addr:UInt):Bool = {
    
    val res = (addr >= range._1 ) && ( addr < range._1 + range._2)
    res.asBool
  }
 
  // Combine sequences to a Seq of Tupples, kind of a Map
  val ranges  = baseSeq zip sizeSeq
  
  // Check inputs and assign selector
  ranges zip io.sel foreach { case(range, port) =>
    
    when (inside(range)(io.addr)) {
      port := true.B
    }
    .otherwise {
      port := false.B
    }
    
  } 

  // $onehot0 output encoding check. This helps to validate input data vs elaboration params
  assert (PopCount(io.sel) <= 1.U, "Invalid addr decoding")
}

object UIntOrdering {
  implicit def lt(a:UInt, b:UInt):Bool = a < b
}

