package src.innovationx.classic.util;

import java.io.*;
import java.math.BigDecimal;

public class Misc {
	
	/**
	 * @return Returns the distance between two positions.
	 */
	public static int getDistance(int coordX1, int coordY1, int coordX2,
			int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	  public static double round(double d, int decimalPlace){
		    // see the Javadoc about why we use a String in the constructor
		    // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
		    BigDecimal bd = new BigDecimal(Double.toString(d));
		    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		    return bd.doubleValue();
	  }
	  
	  	  public static int roundToWhole(double d){
		    // see the Javadoc about why we use a String in the constructor
		    // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
		    BigDecimal bd = new BigDecimal(Double.toString(d));
		    bd = bd.setScale(0,BigDecimal.ROUND_HALF_UP);
		    return (int)bd.doubleValue();
	  }
	
    public static final char playerNameXlateTable[] = {
        '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
        't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
        '3', '4', '5', '6', '7', '8', '9', '[', ']', '/', '-', ' '
    };

    public static void debugMes(String message) {
        //System.out.println("Debug: "+message);
        //debugToWrite(message);
    }

    public static String longToPlayerName(long l) {
        int i = 0;
        char ac[] = new char[99];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }

    public static void debugToWrite(String mess) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Logs/Debug.log", true));
            bw.write(mess);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ioe2) {
                    System.out.println("Error Writing To Log!");
                }
            }
        }
    }

    public static int getCurrentHP(int i, int i1, int i2) {
        double x = (double) i / (double) i1;
        return (int) Math.round(x * i2);
    }

    public static void WriteErrorToLog(String Error1, String Error2, String Error3) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Logs/ErrorLog.log", true));
            bw.write(Error1);
            bw.newLine();
            bw.write(Error2);
            bw.newLine();
            bw.write(Error3);
            bw.newLine();
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ioe2) {
                    System.out.println("Error Writing To Log!");
                }
            }
        }
    }
    public static char xlateTable[] = {
        ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r',
        'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
        'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2',
        '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?',
        '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\',
        '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
        ']', '/', ' '
    };

    public static String textUnpack(byte packedData[], int size) {
        int idx = 0, highNibble = -1;
        for (int i = 0; i < size * 2; i++) {
            int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
            if (highNibble == -1) {
                if (val < 13) {
                    decodeBuf[idx++] = xlateTable[val];
                } else {
                    highNibble = val;
                }
            } else {
                decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
                highNibble = -1;
            }
        }


        return new String(decodeBuf, 0, idx);
    }

    public static void print_debug(String str) {
        System.out.print(str);
    }

    public static void println_debug(String str) {
        System.out.println(str);
    }

    public static void print(String str) {
        System.out.print(str);
    }

    public static void println(String str) {
        System.out.println(str);
    }

    public static int HexToInt(byte data[], int offset, int len) {
        int temp = 0;
        int i = 1000;
        for (int cntr = 0; cntr < len; cntr++) {
            int num = (data[offset + cntr] & 0xFF) * i;
            temp += (int) num;
            if (i > 1) {
                i = i / 1000;
            }
        }
        return temp;
    }

    public static int random(int range) { //0 till range (range INCLUDED)
        return (int) (java.lang.Math.random() * (range + 1));
    }

    public static int random2(int range) { //1 till range
        return (int) ((java.lang.Math.random() * range) + 1);
    }

    public static int random3(int range) { //0 till range
        return (int) (java.lang.Math.random() * range);
    }

    public static int random4(int range) { //0 till range (range INCLUDED)
        return (int) (java.lang.Math.random() * (range + 1));
    }

    public static int random5(int range) { //1 till range
        return (int) ((java.lang.Math.random() * (range + 1)) + 1);
    }

    public static long playerNameToInt64(String s) {
        long l = 0L;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += (1 + c) - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += (1 + c) - 97;
            } else if (c >= '0' && c <= '9') {
                l += (27 + c) - 48;
            }
        }
        while (l % 37L == 0L && l != 0L) {
            l /= 37L;
        }
        return l;
    }
    private static char decodeBuf[] = new char[4096];

    public static int direction(int srcX, int srcY, int destX, int destY) {
        int dx = destX - srcX, dy = destY - srcY;
        if (dx < 0) {
            if (dy < 0) {
                if (dx < dy) {
                    return 11;
                } else if (dx > dy) {
                    return 9;
                } else {
                    return 10;
                }
            } else if (dy > 0) {
                if (-dx < dy) {
                    return 15;
                } else if (-dx > dy) {
                    return 13;
                } else {
                    return 14;
                }
            } else {
                return 12;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                if (dx < -dy) {
                    return 7;
                } else if (dx > -dy) {
                    return 5;
                } else {
                    return 6;
                }
            } else if (dy > 0) {
                if (dx < dy) {
                    return 1;
                } else if (dx > dy) {
                    return 3;
                } else {
                    return 2;
                }
            } else {
                return 4;
            }
        } else {
            if (dy < 0) {
                return 8;
            } else if (dy > 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }	
	public static String optimizeText(String text) {
		char buf[] = text.toCharArray();
		boolean endMarker = true;	// marks the end of a sentence to make the next char capital
		for(int i = 0; i < buf.length; i++) {
            		char c = buf[i];
           		 if(endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;	// transform lower case into upper case
				endMarker = false;
			}
			if(c == '.' || c == '!' || c == '?') endMarker = true;
		}
		return new String(buf, 0, buf.length);
	}
    public static byte directionDeltaX[] = new byte[]{0, 1, 1, 1, 0, -1, -1, -1};
    public static byte directionDeltaY[] = new byte[]{1, 1, 0, -1, -1, -1, 0, 1};
    public static byte xlateDirectionToClient[] = new byte[]{1, 2, 4, 7, 6, 5, 3, 0};
}
