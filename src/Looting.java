public enum Looting {
	BANDOS_COIF(19458,"Bandos Coif", 346000, 1),
	BANDOS_CHAPS(19456,"Bandos Chaps",342000,1),
	BANDOS_BODY(19454, "Bandos Body", 666000, 1),
	BANDOS_FULL_HELM(19438, "Bandos Full Helm", 683000,1),
	BANDOS_KITESHIELD(19441, "Bandos Kiteshield", 209000, 1),
	BANDOS_PLATEBODY(19429, "Bandos Platebody" ,838000, 1),
	BANDOS_PLATELEGS(19432, "Bandos Platelegs", 240000, 1),
	FLAMEBURST_DEFENDER(17274, "Flameburst Defender", 3472000,1),
	STAFF_OF_LIGHT(15487, "Staff Of Light", 9561000,1),
	BERSERKER_RING_I(15221, "Berserker ring(i)", 40000000, 1),
	RUNE_BAR(2365, "Rune bar", 58280, 100),
	BANDOS_BOOTS(11729, "Bandos Boots", 6341000,1),
	RUBY_BOLTS_E(9243, "Ruby Bolts(e)",161960,25),
	BANDOS_STATUETTE(14882, "Bandos Statuette", 7500000, 1),
	SUPER_RESTORE(3026, "Super Restore" ,185000, 10),
	SARADOMIN_BREW(6687, "Saradomin Brew", 193300, 10),
	BANDOS_GODSWORD(11697, "Bandos Godsword", 9160000, 1),
	BANDOS_CHESTPLATE(11725, "Bandos Chestplate", 100000000, 1),
	BANDOS_TASSETS(11727, "Bandos Tassets", 100000000, 1);
	
	private int id;
	private String name;
	private int price;
	private int quantity;
	
	Looting(int id, String name, int price, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getQuantity() {
		return quantity;
	}
}
