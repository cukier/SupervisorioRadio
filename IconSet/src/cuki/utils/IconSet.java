package cuki.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconSet {

	public static final String connectIcon = "Connect";
	public static final String cellPhoneIcon = "Cell Phone";
	public static final String transformersIcon = "Transformer";
	public static final String bombIcon = "Bomb";
	public static final String disconnectIcon = "Disconnect";
	public static final String poolingIcon = "Pooling";
	public static final String nullIcon = "Null";
	public static final String batmanIcon = "Batman";
	public static final String configIcon = "Configure";
	public static final String dataBaseIcon = "Data Base";
	public static final String addPivoIcon = "Add Pivô";
	public static final String delPivoIcon = "Del Pivô";
	public static final String saveIcon = "Salvar";
	public static final String cancelIcon = "Cancelar";

	private static IconSet instance = null;
	private Dimension iconSetSize = null;
	private Dimension iconSize = null;
	private BufferedImage iconSet = null;
	private BufferedImage cropedImage = null;

	private IconSet() {
		try {
			iconSet = ImageIO.read(this.getClass().getResource(
					"/brankic1979-icon-set.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		iconSetSize = new Dimension(iconSet.getWidth(), iconSet.getWidth());
		iconSize = new Dimension(42, 42);
	}

	public static ImageIcon getIcon(Point start, String description) {

		ImageIcon ret = null;

		if (instance == null) {
			instance = new IconSet();
		}

		if (start != null) {
			int x = start.x + instance.iconSize.width;
			int y = start.y + instance.iconSize.height;
			if (x > instance.iconSetSize.height
					|| y > instance.iconSetSize.width) {
				// throw new Exception("Sub-imagem fora dos limiares do set.");
				x = 0;
				y = 0;
			} else {
				instance.cropedImage = instance.iconSet.getSubimage(start.x,
						start.y, instance.iconSize.width,
						instance.iconSize.height);
				ret = new ImageIcon(instance.cropedImage, description);
			}
		}

		return ret;
	}

	public static ImageIcon getIcon(String icon) {

		Point start = null;

		switch (icon) {
		case IconSet.connectIcon:
			start = new Point(610, 542);
			break;
		case IconSet.bombIcon:
			start = new Point(707, 588);
			break;
		case IconSet.cellPhoneIcon:
			start = new Point(849, 260);
			break;
		case IconSet.transformersIcon:
			start = new Point(329, 589);
			break;
		case IconSet.poolingIcon:
			start = new Point(334, 540);
			break;
		case IconSet.disconnectIcon:
			start = new Point(895, 401);
			break;
		case IconSet.batmanIcon:
			start = new Point(379, 589);
			break;
		case IconSet.configIcon:
			start = new Point(755, 357);
			break;
		case IconSet.dataBaseIcon:
			start = new Point(380, 449);
			break;
		case IconSet.addPivoIcon:
			start = new Point(428, 354);
			break;
		case IconSet.delPivoIcon:
			start = new Point(521, 353);
			break;
		case IconSet.saveIcon:
			start = new Point(847, 401);
			break;
		case IconSet.cancelIcon:
			start = new Point(894, 401);
			break;
		default:
		case IconSet.nullIcon:
			start = new Point(150, 150);
			break;
		}

		return IconSet.getIcon(start, icon);
	}

	public static ImageIcon getIcon(int icon) {

		switch (icon) {
		case 0:
			return IconSet.getIcon(IconSet.bombIcon);
		case 1:
			return IconSet.getIcon(IconSet.cellPhoneIcon);
		case 2:
			return IconSet.getIcon(IconSet.connectIcon);
		case 3:
			return IconSet.getIcon(IconSet.poolingIcon);
		case 4:
			return IconSet.getIcon(IconSet.transformersIcon);
		case 5:
			return IconSet.getIcon(IconSet.disconnectIcon);
		case 6:
			return IconSet.getIcon(IconSet.batmanIcon);
		case 7:
			return IconSet.getIcon(IconSet.configIcon);
		case 8:
			return IconSet.getIcon(IconSet.dataBaseIcon);
		case 9:
			return IconSet.getIcon(IconSet.addPivoIcon);
		case 10:
			return IconSet.getIcon(IconSet.delPivoIcon);
		case 11:
			return IconSet.getIcon(IconSet.saveIcon);
		case 12:
			return IconSet.getIcon(IconSet.cancelIcon);
		default:
			return IconSet.getIcon(IconSet.nullIcon);
		}
	}

	public static ImageIcon getConfigIcon() {
		return IconSet.getIcon(IconSet.configIcon);
	}

	public static ImageIcon getConnectIcon() {
		return IconSet.getIcon(IconSet.connectIcon);
	}

	public static ImageIcon getDisconnectIcon() {
		return IconSet.getIcon(IconSet.disconnectIcon);
	}

	public static ImageIcon getPoolingIcon() {
		return IconSet.getIcon(IconSet.poolingIcon);
	}

	public static ImageIcon getDatabaseIcon() {
		return IconSet.getIcon(IconSet.dataBaseIcon);
	}

	public static ImageIcon getSaveIcon() {
		return IconSet.getIcon(IconSet.saveIcon);
	}

	public static ImageIcon getCancelIcon() {
		return IconSet.getIcon(IconSet.cancelIcon);
	}
}
