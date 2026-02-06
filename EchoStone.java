/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */
public class EchoStone extends Stone {
    private int sequenceNumber;
    
    public EchoStone(int number) {
        super();
        this.sequenceNumber = number;
    }
    
    public int getSequenceNumber() { return this.sequenceNumber; }

    @Override
    public String toString() {
        return "Echo Stone (" + this.sequenceNumber + ")";
    }
}