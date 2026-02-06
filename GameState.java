/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */

public class GameState {

    // Final değişkenler, durumun değişmez olmasını sağlar
    private final CircularDoublyLinkedList<Explorer> explorers; 
    private final DoublyNode<Explorer> currentExplorerNode; 
    private final boolean isTurnOrderReversed; 
    private final int currentRoomNumber;
    private final Stack<Integer> echoStoneSequence; 
    
    // Constructor
    public GameState(CircularDoublyLinkedList<Explorer> currentExplorers, DoublyNode<Explorer> currentExplorerNode,int roomNum, Stack<Integer> sequence,boolean isTurnOrderReversed) {
        
        this.currentRoomNumber = roomNum;
        this.isTurnOrderReversed = isTurnOrderReversed;
        this.echoStoneSequence = new Stack<>(sequence); 

        this.explorers = new CircularDoublyLinkedList<>(); 
        
        // Kopyalayacağımız orijinal (source) listenin başını alıyoruz
        DoublyNode<Explorer> originalListHead = currentExplorers.getHead();
        
        // Kopyalanan YENİ listede sıranın kimde olacağını işaretleyecek geçici değişk
        DoublyNode<Explorer> newCurrentNodePointer = null; 

        // Orijinal liste boş değilse kopyalama işlemine başlıyoruz
        if (originalListHead != null) {
            
            // Orijinalhead düğümündeki kaşif'ın bir kopyasını alıyoruz
            Explorer copiedHeadExplorer = new Explorer(originalListHead.data);
            // Kopyalanankaşifi listemize ekliyoruz
            this.explorers.add(copiedHeadExplorer);

            // Orijinal listede sıra currentExplorerNode dakinin sırasında mı
            if (originalListHead == currentExplorerNode) {
                newCurrentNodePointer = this.explorers.getHead();
            }

            // Orijinal listede head den sonraki düğümden (head.next) başlayarak gezmeye başlıyoruz
            DoublyNode<Explorer> originalNodeToCopy = originalListHead.next;
            
            // Tekrar başa originalListHead en dönene kadar devam et
            while (originalNodeToCopy != originalListHead) {
                
                // Gezdiğimiz orijinal düğümdeki Explorer'ın bir kopyasını çekiyoruz
                Explorer copiedExplorer = new Explorer(originalNodeToCopy.data);
                // Kopyalanan Explorer'ı yeni listemizin sonuna ekliyoruz
                this.explorers.add(copiedExplorer);

                // Orijinal listede olduğumuz sıra yani node currentExplorerNode 'ta da aynı düğümde mi
                if (originalNodeToCopy == currentExplorerNode) {
                    newCurrentNodePointer = this.explorers.getHead().prev; 
                }

                // Orijinal listede bir sonraki düğüme geçiyoruz
                originalNodeToCopy = originalNodeToCopy.next;
            } 
        }

        this.currentExplorerNode = newCurrentNodePointer;
       
    }
    
    
    public CircularDoublyLinkedList<Explorer> getExplorers() { 
        return this.explorers; }
    
    public DoublyNode<Explorer> getCurrentExplorerNode() { 
        return this.currentExplorerNode; }
    
    public boolean getIsTurnOrderReversed() { 
        return this.isTurnOrderReversed; }
    
    public int getCurrentRoomNumber() { 
        return this.currentRoomNumber; }
    
    public Stack<Integer> getEchoStoneSequence() { 
        return this.echoStoneSequence; }
    
} 