/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */
public class Explorer {

    private String name;
    private int hp;
    private SinglyLinkedList<Stone> inventory; 

    public Explorer(String name) {
        this.name = name;
        this.hp = 3; 
        this.inventory = new SinglyLinkedList<>(); 
    }
    
    public Explorer(Explorer source) {
        this.name = source.name;
        this.hp = source.hp;

        
        this.inventory = new SinglyLinkedList<>(source.inventory); 
    }

    public boolean isAlive() { 
        return this.hp > 0; 
    }

    public void takeDamage(int amount) {
        this.hp = this.hp - amount;
        if (this.hp < 0) { this.hp = 0; }
        System.out.println(this.name + " hasar aldı! Kalan HP: " + this.hp);
    }
    
    public void heal(int amount) {
        this.hp = this.hp + amount;
        if(this.hp > 3){ this.hp = 3; }
    }

    public void addStone(Stone stone) {
        this.inventory.add(stone);
        System.out.println(this.name + ", envanterine bir " + stone.toString() + " ekledi.");
    }

    public void printInventory() {
        System.out.println(this.name + " Envanteri:");
        if (this.inventory.isEmpty()) {
            System.out.println("  - Boş");
        } else {
            Node<Stone> current = this.inventory.getHead(); 
            while (current != null) { 
                System.out.println("  - " + current.data.toString());
                current = current.next; 
            }
        }
    }

    public String getName() { 
        return this.name; }
    
    public int getHp() { 
        return this.hp; }
    
    public SinglyLinkedList<Stone> getInventory() { 
        return this.inventory; } 
}