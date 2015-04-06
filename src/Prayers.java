import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Menu;

public class Prayers {
	private int prayer;
	
	//public Prayers(int prayer) {
	//	this.prayer = prayer;
	//}
	//Broken hooks so this won't work
    public void enable() {
    	if(!isEnabled()) {
    		Menu.sendAction(169, 6701056, 492, prayer, 0, 1);
    		Time.sleep(new SleepCondition() {
    			@Override
    			public boolean isValid() {
    				return isEnabled();
    			}
    		}, 1500);
    	}
    }
    
    public void tempEnable() {
    	Menu.sendAction(169, 6701056, 492, 25036, 0, 1);
    	Time.sleep(100);
    	Menu.sendAction(169, 6701056, 493, 25050, 0, 1);
    	Time.sleep(100);
    }
    
    public boolean isEnabled() {
    	return Game.getSetting(prayer) == 1;
    }
    
}