import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.wrappers.Item;


public class Banking {
	
	public void deposit(int id, int amount, int sleep) { // Deposit an x amount of one item
		if(Bank.isOpen() && Inventory.getItem(id) != null) {
			int previous = Inventory.getCount(id);
			for(Item i : Inventory.getItems()) {
				if(i.getId() == id && Inventory.getCount(id) != previous - amount) {
					Menu.sendAction(632, id - 1, Inventory.getItem(id).getSlot(), 5064, 9064, 6);
					Time.sleep(sleep);
				}
			}
		}
	}
	
	public void depositItemAll(int id, int sleep) { // Deposit a stack of items with unknown size
		if(Bank.isOpen() && Inventory.getItem(id) != null) {
			while(Inventory.getItem(id) != null) {
				Menu.sendAction(431, id - 1, Inventory.getItem(id).getSlot(), 5064, 409, 3);
				Time.sleep(sleep);
			}
		}
	}
	
	public void openBank() {
		if(!Bank.isOpen()) {
			Bank.open(Bank.getBank());
			Time.sleep(new SleepCondition() {
				@Override
				public boolean isValid()  {
					return Bank.isOpen();
				}
			},1000);
		}
	}
	
	public void withdrawItem(int id, int amount) {
		openBank();
		if(Bank.isOpen()) {
			Bank.withdraw(id, amount, 100);
			Time.sleep(500);
			Bank.close();
		}
	}
	
	public void depositItem(int id, int amount, int sleep, boolean depositAll) {
		openBank();
		if(Bank.isOpen()) {
			if(depositAll) {
				depositItemAll(id, sleep);
			} else {
				deposit(id, amount, sleep);
			}
		}
	}
	
	public void checkPotions() {
		if(Bank.isOpen()) {
			int withdraw = 0;
			for(int i = 0;i < Action.idStrength.length; i++) {
				if(Inventory.getItem(Action.idStrength[i]) == null) {
					withdraw++;
				}
				if(withdraw == 5) {
					for(int a : Action.idStrength) {
						if(Bank.getItem(a) != null) {
							Bank.withdraw(a, 1, 100);
							Time.sleep(300);
							break;
						}
					}
				}
			}
			withdraw = 0;
			for(int i = 0;i < Action.idAttack.length; i++) {
				if(Inventory.getItem(Action.idAttack[i]) == null) {
					withdraw++;
				}
				if(withdraw == 5) {
					for(int a : Action.idAttack) {
						if(Bank.getItem(a) != null) {
							Bank.withdraw(a, 1, 100);
							Time.sleep(300);
							break;
						}
					}
				}
			}
			withdraw = 0;
			
			for(int i = 0;i < Action.idDefence.length; i++) {
				if(Inventory.getItem(Action.idDefence[i]) == null) {
					withdraw++;
				}
				if(withdraw == 5) {
					for(int a : Action.idDefence) {
						if(Bank.getItem(a) != null) {
							Bank.withdraw(a, 1, 100);
							Time.sleep(300);
							break;
						}
					}
				}
			}
			withdraw = 0;
		}
	}
}

