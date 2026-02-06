/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */
/**
 */
public class CircularDoublyLinkedList<T> {
    private DoublyNode<T> head;
    private int size;

    public CircularDoublyLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    
    public CircularDoublyLinkedList(CircularDoublyLinkedList<T> source) {
        this(); // Boş liste oluştur
        // Kopyalama işlemi GameState'in constructor'ında yapıldı
    }

    public void add(T data) {
        DoublyNode<T> newNode = new DoublyNode<>(data);
        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            DoublyNode<T> tail = head.prev;
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
        }
        size++;
    }
    
    
    public DoublyNode<T> getHead() { 
        return head; }
    
    public boolean isEmpty() { 
        return head == null; }
    
    public int size() { 
        return size; }
}