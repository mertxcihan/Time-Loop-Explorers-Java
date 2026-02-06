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
public class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }
    
        
    public Stack(Stack<T> source) {
        this();
        Node<T> current = source.top;
        Node<T> newTail = null;
        
        // Önce ters sırada kopyala
        while(current != null) {
            Node<T> newNode = new Node<>(current.data);
            if (newTail == null) {
                this.top = newNode;
            } else {
                newTail.next = newNode;
            }
            newTail = newNode;
            current = current.next;
        }
        
        //yığını ters çevirerek doğru sırayı elde etme
        Stack<T> tempStack = new Stack<>();
        current = this.top;
        while(current != null) {
            tempStack.push(current.data);
            current = current.next;
        }
        this.top = tempStack.top;
        this.size = tempStack.size;
    }

    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) return null;
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
    if (isEmpty()) {
        return null;
    }
    
    return top.data;
}
    
    public void clear() {
        top = null;
        size = 0;
    }

    public boolean isEmpty() { 
        return top == null; }
    
    public int size() { 
        return size; }
}