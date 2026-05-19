import java.net.DatagramSocket;
import java.net.InetAddress;

class GameCode { //deals with the GameCode and the Networking with IP

    public static String encode(String ip) { // Takes in computer IP, creates a String gamecode out of it that is decodable
        String[] parts = ip.split("\\.");
        long packed = (Long.parseLong(parts[0]) << 24)
                    | (Long.parseLong(parts[1]) << 16)
                    | (Long.parseLong(parts[2]) << 8)
                    |  Long.parseLong(parts[3]);
        return Long.toString(packed, 36).toUpperCase();
    }

    public static String decode(String code) { // Decodes the gamecode into appropriate IP to join as client. 
        long packed = Long.parseLong(code.toLowerCase(), 36);
        long o1 = (packed >> 24) & 0xFF;
        long o2 = (packed >> 16) & 0xFF;
        long o3 = (packed >>  8) & 0xFF;
        long o4 =  packed        & 0xFF;
        return o1 + "." + o2 + "." + o3 + "." + o4;
    }

        public static String getIpAddress() { //gets computer IP
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1"; // Fallback to loopback
        }
    }

    public static void main( String[] args) {
        System.out.println(GameCode.encode(GameCode.getIpAddress()));
    }
}
