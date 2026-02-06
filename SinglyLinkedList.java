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
 * //Tek Yönlü Bağlı Liste..
 */
public class SinglyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    
    //Constructor Explorer'ın envanterini kopyalamak için
    public SinglyLinkedList(SinglyLinkedList<T> source) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        
        Node<T> current = source.head;
        while (current != null) {
            this.add(current.data); 
            current = current.next;
        }
    }
    
    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Verilen indeksteki elemanı getirir
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null; 
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Verilen indeksteki elemanı siler
    public T remove(int index) {
        if (index < 0 || index >= size) {
            return null; 
        }
        size--;

        // Baştaki elemanı silme
        if (index == 0) {
            T data = head.data;
            head = head.next;
            if (head == null) { 
                tail = null;
            }
            return data;
        }

        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        T data = current.next.data;
        if (current.next == tail) { 
            tail = current;
        }
        current.next = current.next.next;
        return data;
    }

    public Node<T> getHead() { 
        return head; }
 
    public boolean isEmpty() { 
        return head == null; }
    
    public int size() { 
        return size; }
}