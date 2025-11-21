import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.*;

public class polynomial {
    private static BigInteger decode(String base, String value) {
        return new BigInteger(value, Integer.parseInt(base));
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java polynomial <json-file>");
            return;
        }

        String jsonContent = new String(Files.readAllBytes(Paths.get(args[0])));
        JsonObject root = JsonParser.parseString(jsonContent).getAsJsonObject();

        int k = root.getAsJsonObject("keys").get("k").getAsInt();

        List<Integer> indexes = new ArrayList<>();
        for (String key : root.keySet()) {
            if (!key.equals("keys")) indexes.add(Integer.parseInt(key));
        }
        Collections.sort(indexes);

        List<BigInteger> roots = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int idx = indexes.get(i);
            JsonObject entry = root.getAsJsonObject(String.valueOf(idx));
            BigInteger r = decode(entry.get("base").getAsString(), entry.get("value").getAsString());
            roots.add(r);
        }

        BigInteger product = BigInteger.ONE;
        for (BigInteger r : roots) {
            product = product.multiply(r);
        }
        if (k % 2 == 0) {
            System.out.println(product);
        } else {
            System.out.println(product.negate());
        }
    }
}
