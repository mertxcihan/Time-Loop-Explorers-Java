/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */
public class TimelineNode {
    
    private final Action action; 
    private final GameState state; 
    private final TimelineNode parent; 
    
    private final SinglyLinkedList<TimelineNode> children; 

    public TimelineNode(Action action, GameState state, TimelineNode parent) {
        this.action = action;
        this.state = state;
        this.parent = parent;
        this.children = new SinglyLinkedList<>(); 
    }

    public void addChild(TimelineNode childNode) {
        this.children.add(childNode);
    }
    
    public Action getAction() { 
        return action; }
    
    public GameState getState() { 
        return state; }
    
    public TimelineNode getParent() { 
        return parent; }
    
    public SinglyLinkedList<TimelineNode> getChildren() { 
        return children; }
}