import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.imageio.ImageIO;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;

@ScriptManifest(author = "Atex", category = Category.COMBAT, description = "Bandos Gold Farm", name = "Bandos Gold Farm", servers = { "Ultimate scape" }, version = 0.3)
public class Paint extends Script implements Paintable {
	Image background = getImage("http://s24.postimg.org/rv2uftbdh/Paint2.png");
	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();
	public static int trips = 0;
	private long startTime = System.currentTimeMillis();
	public static int profit = 0;
	@Override
	public boolean onExecute() {
		strategies.add(new Action());
		provide(strategies);
		return true;
	}
	public void paint(Graphics iFace) {
		String abr;
		String abrRate;
		double profitShort = profit;
		int rateShort = moneyRate(profit);
		if(profitShort > 1000000) {
			profitShort /= 1000000;
			abr = "M";
		} else if(profitShort  > 1000000000) {
			profitShort /= 1000000000;
			abr = "b";
		} else {
			profitShort /= 1000;
			abr = "k";
		}
		profitShort = round(profitShort, 0);
		iFace.setFont(new Font("Impact",Font.PLAIN,20));
		iFace.setColor(Color.YELLOW);
		iFace.drawImage(background, 5, 344, null);
		if(rateShort > 1000000) {
			rateShort /= 1000000;
			abrRate = "M";
		} else if( rateShort> 1000000000) {
			rateShort /= 1000000000;
			abrRate = "b";
		} else {
			rateShort /= 1000;
			abrRate = "k";
		}
		iFace.drawString(""+rateShort+abrRate+" ("+profitShort+abr+")",202,420);
		iFace.setColor(Color.BLACK);
		iFace.drawString(""+trips,202,460);
	}
	
	private Image getImage(String url) {
        try {
                return ImageIO.read(new URL(url));
        } catch (IOException e) {
                return null;
        }
	}
	
	public int moneyRate(int money) {
        return (int)(((double)(money - 0) * 3600000D) / (double)(System.currentTimeMillis() - startTime));
	}
	public double round(double value, int decimals) {
	    if (decimals < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(decimals, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}