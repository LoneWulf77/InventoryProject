public class Item {
    private int UPC;
    private String Name;
    private int amount;

    //default constructor
    public Item() {
    }

    //constructor
    public Item(int UPC, String Name) {
        this.setUPC(UPC);
        this.setName(Name);
        this.setAmount(amount);
    }

    //getter and setter methods
    public int getUPC() {return UPC;}

    public void setUPC(int UPC) {this.UPC = UPC;}

    public String getName() {return Name;}

    public void setName(String Name) {this.Name = Name;}

    public int getAmount() {return amount;}

    public void setAmount(int amount) {this.amount = amount;}

    //CRUD data management methods
    public static void add() {
    }

    //public static List<Item> retrieveAll() {
    //}

    //public static Item retrieve() {}

    public static void update() {}

    public static void destroy() {}

    public static void clearData() {}

    public static void createTestData() {}
}
