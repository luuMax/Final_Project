package src;

import java.net.DatagramSocket;
import java.net.InetAddress;


class GameCode
{

    /**
     * Encodes an IPv4 address as a short base-36 alphanumeric code by packing
     * all four octets into a single long.
     *
     * @param ip
     *            a valid IPv4 address string (e.g. "192.168.1.1")
     * @return an uppercase base-36 string (e.g. "2QGDM1")
     */
    public static String encode(String ip)
    {
        String[] parts = ip.split("\\.");
        long packed = (Long.parseLong(parts[0]) << 24) | (Long.parseLong(parts[1]) << 16)
            | (Long.parseLong(parts[2]) << 8) | Long.parseLong(parts[3]);
        return Long.toString(packed, 36).toUpperCase();
    }


    /**
     * Decodes a base-36 game code back into an IPv4 address string.
     *
     * @param code
     *            an uppercase base-36 string produced by encode()
     * @return the original IPv4 address (e.g. "192.168.1.1")
     */
    public static String decode(String code)
    {
        long packed = Long.parseLong(code.toLowerCase(), 36);
        long o1 = (packed >> 24) & 0xFF;
        long o2 = (packed >> 16) & 0xFF;
        long o3 = (packed >> 8) & 0xFF;
        long o4 = packed & 0xFF;
        return o1 + "." + o2 + "." + o3 + "." + o4;
    }


    /**
     * Determines the machine's outbound IP address by briefly connecting to an
     * external DNS server. No data is actually sent.
     *
     * @return the local IP address as a string, or "127.0.0.1" on failure
     */
    public static String getIpAddress()
    {
        try (final DatagramSocket socket = new DatagramSocket())
        {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        }
        catch (Exception e)
        {
            return "127.0.0.1";
        }
    }
}
