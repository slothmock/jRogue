package engine;

public class Inventory {

	private Item[] items;
	public Item[] getItems() { return items; }
	public Item get(int i) { return items[i]; }

	private int capacity;
	public int capacity() {return capacity;}
	
	public Inventory(int max){
		items = new Item[max];
		capacity = items.length;
	}
	
	public void add(Item item){
		for (int i = 0; i < items.length; i++){
			if (items[i] == null){
				items[i] = item;
				break;
			}
		}
	}

	public void remove(Item item){
		for (int i = 0; i < items.length; i++){
			if (items[i] == item){
				items[i] = null;
				return;
			}
		}
	}

	public boolean isFull(){
		int size = 0;
		for (int i = 0; i < items.length; i++){
			if (items[i] != null)
				size++;
		}
		return size == items.length;
	}
    
	public boolean contains(Item item) {
		for (Item i : items){
			if (i == item)
				return true;
		}
		return false;
	}

	public int currentCapacity() {
		int size = 0;
		for (int i = 0; i < items.length; i++){
			if (items[i] != null)
				size++;
		}
		return size;
	}
}
