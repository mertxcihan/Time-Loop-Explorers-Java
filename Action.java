/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */
public class Action {
    
    private final Explorer explorer;
    private final String actionType;
    private final String description;
    private final String actorName; 

    // Kaşifler için Constructor
    public Action(Explorer explorer, String actionType, String description) {
        this.explorer = explorer;
        this.actionType = actionType;
        this.description = description;
        this.actorName = explorer.getName(); 
    }

    // Sistem için Constructor 
    public Action(String actorName, String actionType, String description) {
        this.explorer = null; 
        this.actionType = actionType;
        this.description = description;
        this.actorName = actorName;
    }

    @Override
    public String toString() {
        return this.actorName + ": '" + this.actionType + "' yaptı (" + this.description + ")";
    }
    
    public Explorer getExplorer() { 
        return explorer; }
    
    public String getDescription() { 
        return description; }
}