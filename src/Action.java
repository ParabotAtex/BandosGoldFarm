import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.GroundItems;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.GroundItem;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.Tile;

import java.awt.Point;

public class Action implements Strategy {
	public static int idShark = 386;
	public static int[] idStrength = {-1, 162, 160, 158, 2441};
	public static int[] idAttack = {-1, 150, 148, 146,2437};
	public static int[] idDefence = {-1, 168, 166, 164, 2443};
	public static int idVial = 230;
	int solus = 2747;
	int bandos = 6260;
	boolean potted = false;
	static Banking bank = new Banking();
	
	int idMelee = 25036;
	int idPiety = 25050;
	
	boolean looting=false;
	
	Area spawn = new Area(new Tile(3090,3474), new Tile(3090, 3481), new Tile(3097,3481), new Tile(3097, 3474));
	Area lobby = new Area(new Tile(3326,9361), new Tile(3305,9361), new Tile(3305,9392), new Tile(3326, 9392));
	Mouse mouse;
	Point logIn = new Point(453,284);
	@Override
	public boolean activate() {
		if(Loader.getClient().isLoggedIn()) {
			return true;
		} else {
			Time.sleep(10000);
			Mouse.getInstance().click(logIn);
			Time.sleep(5000);
			if(Loader.getClient().isLoggedIn()) {
				Menu.sendAction(1047, 1162, 0, 0, 409, 2); //Turn on run
			}
			return false;
		}
	}
	public void execute() {
		try {
			if(Loader.getClient().isLoggedIn()) {
				if(Inventory.getItem(idVial) != null) {
					for(Item i : Inventory.getItems()) {
						if(i.getId() == idVial) {
							i.drop();
							Time.sleep(80);
						}
					}
				}
				Time.sleep(50);
				Npc Solus = Npcs.getClosest(solus);
				Npc Bandos = Npcs.getClosest(bandos);
				if(GroundItems.getGroundItems() != null) {
					for(GroundItem g : GroundItems.getGroundItems()) {
						for (Looting l : Looting.values()) {
							if((l.getId() - 1) == g.getId()) {
								looting = true;
								int previous = 0;
								if(Inventory.getItem(l.getId())!= null) {
									previous = Inventory.getCount(l.getId());
								} else {
									previous = 0;
								}
								int looted = previous;
								if(Inventory.isFull()) {
									Menu.sendAction(74, idShark - 1, Inventory.getItem(idShark).getSlot(), 3214, 2213, 4);
									Time.sleep(1000);
								}
								while(looted == previous) {
									if(Inventory.getItem(l.getId())!= null) {
										looted = Inventory.getCount(l.getId());
									} else {
										looted = 0;
									}
									g.take();
									Time.sleep(500);
									if(looted != previous) {
										Paint.profit += (l.getPrice() * l.getQuantity()) + 250000;
										System.out.println("Looted "+l.getQuantity()+" of item "+l.getName());
										Paint.trips++;
									}
								}
							}
						}
					}
					looting = false;
				}
				if(Solus != null && lobby.contains(Players.getMyPlayer().getLocation()) && Bandos == null && !looting && Inventory.getItem(idVial) == null) {
					supply();
					//in Lobby
					//Start a game
					//if(Skill.getCurrentLevel(2) < (Skill.getRealLevel(2) + 13)) {
					//	potUp(); //#getCurrentLevel > Broken hook
					//}
					if(!disposeLoot() && !needPotions() && Inventory.getCount(idShark) > 4) {
						if(Bank.isOpen()) {
							Bank.close();
						}
						if(!potted) {
							potted = true;
							potUp();
							Time.sleep(1000);
						}
						if(Inventory.getItem(idVial) == null) {
							if(Game.getOpenBackDialogId() == 2480) {
								Menu.sendAction(315, 6701056, 60, 2482, 0, 1); //Select Solo boss
								Time.sleep(2000);
							} else if(Game.getOpenBackDialogId() == 2469) {					
								Menu.sendAction(315, 6701056, 60, 2471, 0, 1); //Select Beginner
								Time.sleep(2000);
							} else if(Game.getOpenBackDialogId() == 2492) {
								Menu.sendAction(315, 6701056, 60, 2497, 0, 1); //Select Bandos
								Time.sleep(3000);
								potted = false;
								Prayers prayer = new Prayers();
								prayer.tempEnable();
							} else if(!Bank.isOpen()) {
								Solus.interact(0);
								Time.sleep(1000);
							}
						}
					}
				}
				if(Bandos != null && Solus == null) {
					//in Battle
					//Prayers melee = new Prayers(idMelee);
					//Prayers piety = new Prayers(idPiety);
					//if(!melee.isEnabled()) {
					//	melee.enable();
					//}
					//if(!piety.isEnabled()) {
					//	piety.enable();
					//}
					checkHealth();
					if(!Players.getMyPlayer().isInCombat()) {
						Bandos.interact(1);
						Time.sleep(new SleepCondition() {
							@Override
							public boolean isValid() {
								return Players.getMyPlayer().getAnimation() != -1;
							}
						}, 1000);
					}
				}
				if(spawn.contains(Players.getMyPlayer().getLocation()) || !lobby.contains(Players.getMyPlayer().getLocation())) {
					//Teleporting to lobby
					Menu.sendAction(1075, 28, 471, 155, 0, 1);
					Time.sleep(1000);
					Menu.sendAction(315, 28, 471, 1170, 0, 1);
					Time.sleep(1000);
					Menu.sendAction(315, 28, 471, 2495, 0, 1);
					Time.sleep(5000);
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Exception caught: " + e.getMessage());
		}
	}
	
	public void potUp() {
		for(int i = 0; i < idStrength.length; i++) {
			if(Inventory.getItem(idStrength[i]) != null) {
				Menu.sendAction(74, idStrength[i] - 1, Inventory.getItem(idStrength[i]).getSlot(), 3214, 2213, 5);
				Time.sleep(2000);
			}
		}
		for(int i = 0; i < idAttack.length; i++) {
			if(Inventory.getItem(idAttack[i]) != null) {
				Menu.sendAction(74, idAttack[i] - 1, Inventory.getItem(idAttack[i]).getSlot(), 3214, 2213, 5);
				Time.sleep(2000);
			}
		}
		for(int i = 0; i < idDefence.length; i++) {
			if(Inventory.getItem(idDefence[i]) != null) {
				Menu.sendAction(74, idDefence[i] - 1, Inventory.getItem(idDefence[i]).getSlot(), 3214, 2213, 5);
				Time.sleep(2000);
			}
		}
	}
	
	public void checkHealth() {
		//if((Players.getMyPlayer().getHealth()<(Players.getMyPlayer().getMaxHealth() / 2) && Inventory.getItem(idShark) == null) || Players.getMyPlayer().getHealth() < 25) {
		//	System.out.println("Emergency teleport!");
		//	Menu.sendAction(315, 28, 471, 1170, 0, 1);
		//	Time.sleep(1000);
		//	Menu.sendAction(315, 28, 471, 2495, 0, 1);
		//	Time.sleep(5000);
		/*} else*/ if (Players.getMyPlayer().getHealth() < (Players.getMyPlayer().getMaxHealth() / 2) && Inventory.getItem(idShark) != null) {
			Menu.sendAction(74, idShark - 1, Inventory.getItem(idShark).getSlot(), 3214, 2213, 4);
			Time.sleep(1000);
		}
	}
	public void supply() {
		if(needPotions()) {
			bank.openBank();
			bank.checkPotions();
			Time.sleep(new SleepCondition() {
				@Override
				public boolean isValid() {
					return !needPotions();
				}
			},1000);
		}
		if(disposeLoot()) {
			for(Item i : Inventory.getItems()) {
				for(Looting l : Looting.values()) {
					if(i.getId() == l.getId()) {
						bank.depositItem(i.getId(), 1, 300, true);
					}
				}
			}
		}
		if(Inventory.getCount(idShark) < 5) {
			bank.withdrawItem(idShark, 25 - Inventory.getCount(idShark));
			Time.sleep(1000);
		}
	}
	public boolean disposeLoot() {
		int count=0;
		for(Item i : Inventory.getItems()) {
			for(Looting l : Looting.values()) {
				if(i.getId() == l.getId()) {
					count++;
				}
			}
		}
		if(count > 10) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean needPotions() {
		int potions = 0;
		for(int i = 0;i < idStrength.length; i++) {
			if(Inventory.getItem(idStrength[i]) == null) {
				potions++;
			}
		}
		for(int i = 0;i < idAttack.length; i++) {
			if(Inventory.getItem(idAttack[i]) == null) {
				potions++;
			}
		}
		for(int i = 0;i < idDefence.length; i++) {
			if(Inventory.getItem(idDefence[i]) == null) {
				potions++;
			}
		}
		if(potions > 12) {
			return true;
		} else {
			return false;
		}
	}
}
