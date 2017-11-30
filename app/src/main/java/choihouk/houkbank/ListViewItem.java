package choihouk.houkbank;

public class ListViewItem {
    private String nameStr ;
    private String dateStr ;
    private String typeStr ;
    private int amountInt;

    public void setName(String name) {
        nameStr = name ;
    }
    public void setDate(String date) {
        dateStr = date ;
    }
    public void setType(String type) {
        typeStr = type ;
    }
    public void setAmount(int amount){
        amountInt = amount;
    }

    public String getName() {
        return this.nameStr ;
    }
    public String getDate() {
        return this.dateStr ;
    }
    public String getType() {
        return this.typeStr ;
    }
    public int getAmount() {
        return this.amountInt ;
    }
}
