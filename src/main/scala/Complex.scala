
package complex_pkg
import chisel3._

class Complex[A <: Data, B <: Data] (val re:A, val im:B) extends Bundle {
  override def cloneType: this.type = new Complex(re.cloneType, im.cloneType).asInstanceOf[this.type]

}

object Complex {

  def apply[A <: Data, B <: Data](re:A, im:B):Complex[A,B] = {
    val real  = re.cloneType
    val imag  = im.cloneType
    new Complex(real,imag)
  }
  
  def zero = Complex (0.U,0.U)
  def one  = Complex (1.U,0.U)
  
}

