
package  my_pkg  

import  chisel3._
import  chisel3.util.{Valid}
import  collection.mutable.{LinkedHashMap}
import  Cfg._

class Vrf[T <: Data] (dType:T, banks:Int, addrWidth:Int) extends Module {
 
  // Parameters
  val zero  = 0.U(dType.getWidth.W).asTypeOf(dType)
  
  val depth  = 1 << addrWidth
 
  // IO
  val io = IO (new Bundle {
    val din   = Flipped(Valid(new Link(dType))) // 1 input
    val dout  = Vec (banks, Valid(new Link(dType))) // multiple outputs
  })

  val in  = io.din
  val out = io.dout
  
  // WR pointer
  val cnt = Module (new Counter(addrWidth))
  val wptr = cnt.io.out
 
  cnt.io.ena := in.valid
  cnt.io.clr := 0.U

  // RAM banks
  val mem  = Seq.fill(banks) { SyncReadMem(depth, dType) }

  //val mmap  = Map ( 0x0.U -> 0x1.U, 
  //                  0x2.U -> 0x3.U)
  val baseSeq  = Seq(0x0.U, 0x1.U)
  val sizeSeq  = Seq(0x2.U, 0x3.U)
  
  // AddrDecoder 
  val adec  = Module (new AddrDecoder(baseSeq, sizeSeq, addrWidth))
  adec.io.en := true.B
  adec.io.addr := wptr

  val sel  = adec.io.sel
  

  // WR logic
  mem.zipWithIndex map { case (bank,idx) => 
    //when (sel(idx)) {
    when (true.B) {
      bank.write (wptr, in.bits.data)
    }
  }

  // RD logic
  out.zipWithIndex map { case (port,idx) => 
    //port.data := mem(idx).read(port.addr, port.valid) 
    port.bits.data := mem(idx).read(0.U, port.valid) 
  }


  //wen := adec.io.sel

  //adec.io.baseVec := 
  //adec.io.sizeVec := 

  for (i <- 0 until banks){
    out(i).valid := 0.U
    out(i).bits.data := zero
  }
    
}

class VrfShell extends Module {  
  
  val banks = 2
  val aw    = 4

  val io = IO (new Bundle {
    val din   = Flipped(Valid(new Link(inType))) // 1 input
    val dout  = Vec (banks, Valid(new Link(inType))) // multiple outputs
  })
  
  
  val vrf  = Module( new Vrf(inType, banks, aw) )

  io.din <> vrf.io.din 

  io.dout zip vrf.io.dout map { case (l,r) => l <> r}
  
   
}

