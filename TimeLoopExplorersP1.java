/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package timeloopexplorersp1;

/**
 *
 * @author MertCihan
 */
import java.util.Scanner;
import java.util.Random;

 
public class TimeLoopExplorersP1 {

    private CircularDoublyLinkedList<Explorer> explorers; // Oyuncu sırasını tutar
    private DoublyNode<Explorer> currentExplorerNode;   // Sırası gelen oyuncu
    private boolean isTurnOrderReversed;                // Sıra ters mi kontrol eder
    private Stack<Integer> echoStoneSequence;           // Kazanma koşulu yığını (1-2-3)

    //Zaman çizelgesi için
    private TimelineNode rootNode;                      
    private TimelineNode currentNode;                   // Zaman çizelgesindeki mevcut an

    private int currentRoomNumber;                      // Mevcut oda numarası
    private Scanner scanner;                            
    private Random random;                              
    private boolean gameWon = false;                   

    //Constructor
    public TimeLoopExplorersP1() {
        this.explorers = new CircularDoublyLinkedList<>();
        this.echoStoneSequence = new Stack<>(); 
        this.currentExplorerNode = null;
        this.isTurnOrderReversed = false;

        this.currentRoomNumber = 1; // Oda 1'den başlar
        this.gameWon = false;

        GameState initialState = createCurrentState(); 
        Action initialAction = new Action("Sistem", "Start", "Oyun başladı"); 
        this.rootNode = new TimelineNode(initialAction, initialState, null); 
        this.currentNode = this.rootNode; // Başlangıçta mevcut an, kök ilk düğümdür

        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }


    private GameState createCurrentState() {
        // GameState constructor'ı, içindeki tüm verileri kopyalar.
        return new GameState(this.explorers, this.currentExplorerNode, this.currentRoomNumber,this.echoStoneSequence,this.isTurnOrderReversed);
    }

    
    public void setupGame() {
        System.out.println("Time Loop Explorers oyununa hoş geldiniz!");

        int explorerCount = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Kaç kaşif ile oynamak istersiniz? (3-5): ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= 3 && input <= 5) {
                    explorerCount = input;
                    validInput = true;
                } else {
                    System.out.println("Geçersiz sayı. Lütfen 3, 4, veya 5 girin.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Hata! Bu bir sayı değil. Lütfen tekrar deneyin.");
            }
        }

        // Kaşifleri oluşturur ve oyuncu sırası listesine ekler
        for (int i = 1; i <= explorerCount; i++) {
            this.explorers.add(new Explorer("Kaşif " + i));
        }

        this.currentExplorerNode = this.explorers.getHead(); // İlk sıradaki kaşif
        System.out.println(explorerCount + " kaşif maceraya başlıyor!");

        // Kurulum tamamlandıktan sonraki durumu zaman çizelgesine kaydeder
        GameState setupState = createCurrentState();
        Action setupAction = new Action("Sistem", "Setup", explorerCount + " kaşif oluşturuldu");
        TimelineNode setupNode = new TimelineNode(setupAction, setupState, this.rootNode);
        this.rootNode.addChild(setupNode); // Setup düğümünü kökün çocuğu yapar
        this.currentNode = setupNode;       // Şu anın , kurulum anıdır
    }

    
    public void startGameLoop() {
        while (!isGameOver() && !this.gameWon) {
            Explorer currentExplorer = this.currentExplorerNode.data;

            if (currentExplorer.isAlive()) {
                System.out.println("---------------------------------");
                System.out.println("Oda: " + this.currentRoomNumber);
                System.out.println("Sıra " + currentExplorer.getName() + "'de (HP: " + currentExplorer.getHp() + ")");
                performAction(currentExplorer); // Hareket menüsünü gösterir
            } else {
                System.out.println(currentExplorer.getName() + " ölü ve hareket edemiyor.");
            }

            // Sırayı bir sonraki oyuncuya geçer
            if (!this.gameWon) {
                 advanceTurn();
            }
        }

        // Döngü bittiğinde oyunun bitiş mesajını gösterir
        if (!this.gameWon) {
            System.out.println("---------------------------------");
            System.out.println("Oyun Bitti! Tüm kaşifler öldü.");
        }
    }

    /**
     * Sırayı bir sonraki kaşife geçirir. Sıra tersine döndüyse
     * CircularDoublyLinkedList'in 'prev' işaretçisini kullanır.
     */
    private void advanceTurn() {
        if (this.isTurnOrderReversed == true) { 
            // Sıra ters ise, bir önceki kaşife git
            this.currentExplorerNode = this.currentExplorerNode.prev;
        } else {
            // Normal sıra ise, bir sonraki kaşife git
            this.currentExplorerNode = this.currentExplorerNode.next;
        }
    }

    
    private void performAction(Explorer explorer) {
        boolean actionTaken = false; // Geçerli bir hamle yapıldı mı?

        while (!actionTaken) {
            System.out.println("Ne yapmak istersin? (Seçenekler: 'move', 'use stone', 'inventory', 'view timeline'):");
            String input = scanner.nextLine(); 
            Action action = null; //Henüz action yapılmadı hamle

            if (input.equals("move")) {
                action = new Action(explorer, "Move", "Oda " + (this.currentRoomNumber + 1) + "'e geçti");
                triggerRandomEvent(explorer); // Odaya girince random olay tetikler
                actionTaken = true;           // Geçerli hamle yapıldı
            } else if (input.equals("use stone")) {
                action = useStone(explorer); // Taş kullanma menüsünü çağırır
                if (action != null) {        // Eğer taş başarıyla kullanıldıysa
                    actionTaken = true;      // Geçerli hamle yapıldı
                }
            } else if (input.equals("inventory")) {
                 explorer.printInventory(); // Envanteri gösterir
            } else if (input.equals("view timeline")) {
                 viewTimeline();            // Zaman çizelgesini gösterir
            } else {
                System.out.println("Geçersiz komut.");
            }
               //Hamle yapıldı ise
            if (actionTaken && action != null) {
                //O anın "durumunu" (GameState) alır
                GameState newState = createCurrentState();
                //Yeni zaman düğümünü (TimelineNode) oluşturur
                TimelineNode newNode = new TimelineNode(action, newState, this.currentNode);
                //Bu yeni düğümü, mevcut düğümün 'çocukları' listesine ekler (dallanma)
                this.currentNode.addChild(newNode);
                //Şu anki zamanı bu yeni düğüme ilerletir,ekler
                this.currentNode = newNode;
            }
        } 
    }

    
    public void reverseTurnOrder() {
        System.out.println("Zaman akışı tersine döndü! Sıra tersine çevriliyor!");
        this.isTurnOrderReversed = !this.isTurnOrderReversed; //tersle
    }

    
    public boolean isGameOver() {
        if (this.explorers.isEmpty()) {
            return false;
        }

        DoublyNode<Explorer> head = this.explorers.getHead();
        DoublyNode<Explorer> temp = head;

        if (temp.data.isAlive()) {
            return false; 
        }
        
        temp = temp.next;
        //döngü head e geldiğinde duracağı için head i de kontrol etmek için üstte aynı if i yaptık
        while (temp != head) {
            
            if (temp.data.isAlive()) {
                return false; 
            }
            
            
            temp = temp.next;
        }

        return true; 
    }

   
    private Action useStone(Explorer explorer) {
        SinglyLinkedList<Stone> inventory = explorer.getInventory(); // Kaşifin envanterini al
        // Envanter boşsa işlem yapma
        if (inventory.isEmpty()) {
            System.out.println(explorer.getName() + " taş kullanmaya çalıştı ama envanteri boş!");
            return null;
        }

        System.out.println(explorer.getName() + " envanterini açtı. Hangi taşı kullanacaksın?");
        for (int i = 0; i < inventory.size(); i++) {
            // SinglyLinkedList'in (inventory) 'get' metodu indekse göre elemanı getirir
            System.out.println("  " + (i + 1) + ": " + inventory.get(i).toString());
        }
        System.out.println("  0: Vazgeç (Geri dön)");

        int stoneChoice = -1; 
        // Kullanıcıdan geçerli bir sayı alır
        try {
            stoneChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz sayı.");
            return null;
        }

        // Vazgeçme seçeneği
        if (stoneChoice == 0) {
            System.out.println("Taş kullanmaktan vazgeçildi.");
            return null;
        }

        int stoneIndex = stoneChoice - 1; // Kullanıcı girişi 1 tabanlı, index 0 tabanlıdır
        // Seçilen taşı envanterden alma
        Stone selectedStone = inventory.get(stoneIndex);

        // Geçersiz index kontrolü
        if (selectedStone == null) {
            System.out.println("Envanterinde o numarada bir taş yok.");
            return null;
        }

        // Taş geçerliyse envanterden sil
        inventory.remove(stoneIndex);

        System.out.println(explorer.getName() + " bir " + selectedStone.toString() + " kullandı.");
        String description = selectedStone.toString() + " kullandı."; 

        //Taşın türüne gröe işlem yapılıp ona göre add lenir
        // instanceof polimorfizm kullanarak nesnenin gerçek türünü ne onu kontrol eder
        if (selectedStone instanceof EchoStone) {
            int seqNum = ((EchoStone) selectedStone).getSequenceNumber(); // Taşa özel metodu çağırır
            System.out.println("Taşın dizilim numarası: " + seqNum);
            checkWinCondition(seqNum); // Kazanma durumunu kontrol eder  1,2,3
            description = "Echo Stone (" + seqNum + ") kullandı."; // Açıklamayı günceller
        }

        else if (selectedStone instanceof ParadoxStone) {
            System.out.println("Bir Paradox Stone kullandın! Zamanın dokusu bükülüyor...");
            // Geçmiş zaman çizelgesini gösterir ve kullanıcıdan bir 'an' seçmesini ister
            TimelineNode selectedNode = selectTimelineNode();

            // Eğer kullanıcı geçerli bir 'an' seçtiyse
            if (selectedNode != null) {
                revertToState(selectedNode); // Oyunu seçilen 'an'a geri yükle
                System.out.println("...Zaman geri sarıldı! " + selectedNode.getAction().getDescription() + " anına geri döndün.");
                description = "Zamanı geri sardı."; 
            } else { // Kullanıcı vazgeçtiyse veya geçersiz seçim yaptıysa
                System.out.println("Zaman yolculuğundan vazgeçildi.");
                explorer.addStone(selectedStone); // Kullanılan taşı envantere geri koy
                return null; 
            }
        }

        // Use stone'u temsil eden Action nesnesini oluşturur ve döndürür
        return new Action(explorer, "Use Stone", description);
    }

    
    private void checkWinCondition(int stoneNumber) {
        // Beklenen sayı, yığındaki eleman sayısının 1 fazlasıdır (boşsa 1 beklenir) echostone 1 echostone2 echstone3 gibibir fazlası
        int expectedNumber = this.echoStoneSequence.size() + 1;

        // Gelen numara beklenen numara mı
        if (stoneNumber == expectedNumber) {
            this.echoStoneSequence.push(stoneNumber); // Doğruysa yığına ekler
            System.out.println("...Doğru sıra! Kullanılan taşlar: " + this.echoStoneSequence.size());
            //Eğer 3 taş doğru sırada toplandıysa oyunu kazanılır
            if (this.echoStoneSequence.size() == 3) {
                endGame(true); 
            }
        } else { 
            System.out.println("...Yanlış taştı! Sıra bozuldu.");
            this.echoStoneSequence.clear(); //Yığını sıfırlar
        }
    }

    private void endGame(boolean didWin) {
        if (didWin) {
            this.gameWon = true; //Oyun döngüsünü durduracak eylem yapıldıysa
            System.out.println("---------------------------------");
            System.out.println("TEBRİKLER! 1-2-3 sırasını tamamladın!");
            System.out.println("Zaman döngüsü kırıldı ve kaşifler harabeden kaçtı!");
            System.out.println("---------------------------------");
        }
    }


    private void viewTimeline() {
        System.out.println("--- TÜM ZAMAN ÇİZELGESİ ---");
        //Line ı yazdırmak için kökten başlatır
        printTimelineRecursive(this.rootNode, ""); 
        System.out.println("--------------------------");
    }

    private void printTimelineRecursive(TimelineNode node, String indent) {
        // Kök düğüm hariç diğer düğümlerin hareketlerini yazdırır
        if (node.getAction() != null) { //Action yapıldıysa bir hareket
            System.out.println(indent + "-> " + node.getAction().toString()); //o hareketi get le çağırır
        }

        // Bu düğümün tüm geleceklerini al
        Node<TimelineNode> currentChild = node.getChildren().getHead();
        //Her çocuk için devam eder döngüyle
        while (currentChild != null) {
            printTimelineRecursive(currentChild.data, indent + "  "); 
            currentChild = currentChild.next;
        }
    }

    private TimelineNode selectTimelineNode() {
        System.out.println("--- ZAMAN ÇİZELGESİ (GEÇMİŞE DÖN) ---");
        //Dallanmış yapıyı ,gelecekleri, gezmesi kolay düz bir listeye çevirir
        SinglyLinkedList<TimelineNode> flatTimeline = new SinglyLinkedList<>();
        flattenTimelineRecursive(this.rootNode, flatTimeline);

        // Eğer liste boşsa
        if (flatTimeline.isEmpty()) {
            System.out.println("Geri dönülecek bir geçmiş yok.");
            return null;
        }

        // Düz listeyi numaralandırarak ekrana yazdırır
        Node<TimelineNode> current = flatTimeline.getHead();
        int i = 0;
        while(current != null) {
            System.out.println("  " + (i + 1) + ": " + current.data.getAction().toString());
            // Eğer listedeki düğüm, oyunun şu anki düğümüyle aynıysa belirtir
            if (current.data == this.currentNode) {
                System.out.println("     (*** ŞU AN BURADASINIZ ***)");
            }
            current = current.next;
            i++;
        }
        System.out.println("  0: Vazgeç (Geri dön)");
        System.out.print("Hangi 'an'a geri dönmek istersin? (Sayı girin): ");

        int choice = -1; 
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz sayı.");
            return null;
        }

        // Vazgeçme seçeneği
        if (choice == 0) { return null; }

        int index = choice - 1; 
        // Seçilen düğümü listeden alır
        TimelineNode selectedNode = flatTimeline.get(index);

        if (selectedNode == null) {
            System.out.println("Geçersiz an seçimi.");
            return null;
        }
        return selectedNode;
    }

    private void flattenTimelineRecursive(TimelineNode node, SinglyLinkedList<TimelineNode> list) {
        // Kök düğümünü listeye ekleme
        if (node != this.rootNode) {
            list.add(node);
        }
        // Tüm çocuklar,gelecekler için kendini tekrar çağır
        Node<TimelineNode> currentChild = node.getChildren().getHead();
        while (currentChild != null) {
            flattenTimelineRecursive(currentChild.data, list);
            currentChild = currentChild.next;
        }
    }

    private void revertToState(TimelineNode selectedNode) {
        // Seçilen düğümün kaydettiği GameState'i  al
        GameState pastState = selectedNode.getState();

        // O  , kopyadaki verileri mevcut oyuna yükle
        this.explorers = pastState.getExplorers();
        this.currentExplorerNode = pastState.getCurrentExplorerNode();
        this.currentRoomNumber = pastState.getCurrentRoomNumber();
        this.echoStoneSequence = pastState.getEchoStoneSequence();
        this.isTurnOrderReversed = pastState.getIsTurnOrderReversed();

        //Zaman çizelgesindeki "mevcut konumu" seçilen düğüme ayarlar
        this.currentNode = selectedNode;
    }

    private void triggerRandomEvent(Explorer explorer) {
        this.currentRoomNumber++; 
        System.out.println(explorer.getName() + " Oda " + this.currentRoomNumber + "'e girdi...");

        int choice = this.random.nextInt(4);

        if (choice == 0) { // Olay 0: Taş Bulma
            System.out.println("Yerde parlayan bir nesne buldun!");
            // %50 şansla Paradox veya Echo taşı bulma
            if (this.random.nextBoolean()) {
                explorer.addStone(new ParadoxStone());
            } else {
                int stoneNum = this.random.nextInt(3) + 1; // 1, 2, veya 3 numaralı Echo taşı [0,3) + 1 ile [1,4) yani 1,2,3
                explorer.addStone(new EchoStone(stoneNum));
            }

        } else if (choice == 1) { 
            int storyChoice = this.random.nextInt(4);
            if (storyChoice == 0) {
                System.out.println("Zehirli Su Birikintisi: ... (-1 HP)");
            } else if (storyChoice == 1) {
                System.out.println("Lanetli Kalkan: ... (-1 HP)");
            } else if (storyChoice == 2) {
                System.out.println("Kara Duman Tuzağı: ... (-1 HP)");
            } else {
                System.out.println("Gölge Örümceğinin Isırığı: ... (-1 HP)");
            }
            explorer.takeDamage(1); 
            if (!explorer.isAlive()) { 
                 System.out.println(explorer.getName() + " bu saldırıdan kurtulamadı.");
            }

        } else if (choice == 2) { 
            int storyChoice = this.random.nextInt(4);
            if (storyChoice == 0) {
                 System.out.println("Şifalı Kaynak Suyu: ... (+1 HP)");
            } else if (storyChoice == 1) {
                System.out.println("Kadim Koruma Tılsımı: ... (+1 HP)");
            } else if (storyChoice == 2) {
                System.out.println("Doğa Ruhunun Dokunuşu: ... (+1 HP)");
            } else {
                System.out.println("Phoenix Tüyü: ... (+1 HP)");
            }
            explorer.heal(1); 
            System.out.println(explorer.getName() + " iyileşti! (Mevcut HP: " + explorer.getHp() + ")");

        } else { 
            reverseTurnOrder(); // Sırayı tersine çeviren metodu çağırır
        }
    } 

} 