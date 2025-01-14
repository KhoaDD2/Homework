package com.mobisec.gnirts;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

class FlagChecker {
    FlagChecker() {
    }

    public static boolean checkFlag(Context ctx, String flag) {
        if (!flag.startsWith("MOBISEC{") || !flag.endsWith("}")) {
            return false;
        }
        String core = flag.substring(8, 40);
        if (core.length() != 32) {
            return false;
        }
        String[] ps = core.split(foo());
        if (ps.length != 5 || !bim(ps[0]) || !bum(ps[2]) || !bam(ps[4]) || !core.replaceAll("[A-Z]", "X").replaceAll("[a-z]", "x").replaceAll("[0-9]", " ").matches("[A-Za-z0-9]+.       .[A-Za-z0-9]+.[Xx ]+.[A-Za-z0-9 ]+")) {
            return false;
        }
        char[] syms = new char[4];
        int[] idxs = {13, 21, 27, 32};
        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < syms.length; i++) {
            syms[i] = flag.charAt(idxs[i]);
            chars.add(Character.valueOf(syms[i]));
        }
        int sum = 0;
        for (char c : syms) {
            sum += c;
        }
        if (sum == 180 && 
        chars.size() == 1 && 
        me(ctx, 
            dh(
                gs(
                    "xwe", 
                    "53P"
                    ), 
                ps[0]),
            "6e9a4d130a9b316e9201238844dd5124") && 
        me(ctx, 
            dh(
                gs(
                    "asd", 
                    ",7Q"
                    ), 
                ps[1]), 
            "7c51a5e6ea3214af970a86df89793b19") && 
        me(ctx, 
            dh(
                gs(
                    "uyt", 
                    "8=A"
                    ), 
                ps[2]), 
            "e5f20324ae520a11a86c7602e29ecbb8") && 
        me(ctx, 
            dh(
                gs(
                    "42s", 
                    "yvF"
                    ), 
                ps[3]), 
            "1885eca5a40bc32d5e1bca61fcd308a5") && 
        me(ctx, 
            dh(
                gs(
                    "p0X", 
                    "=tm"
                    ), 
                ps[4]), 
            "da5062d64347e5e020c5419cebd149a2") && 
        me(ctx, 
            dh(
                gs(
                    "70 IJTR", 
                    "dxa"
                    ), 
                flag), 
            "1c4d1410a4071880411f02ff46370e46b464ab2f87e8a487a09e13040d64e396")) {
            return true;
        }
        return false;
    }

    private static boolean bim(String s) {
        return s.matches("^[a-z]+$");
    }

    private static boolean bum(String s) {
        return s.matches("^[A-Z]+$");
    }

    private static boolean bam(String s) {
        return s.matches("^[0-9]+$");
    }

    private static String dh(String hash, String s) {
        try {
            MessageDigest md = MessageDigest.getInstance(hash);
            md.update(s.getBytes());
            return toHexString(md.digest());
        } catch (Exception e) {
            return null;
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String foo() {
        String s = "Vm0wd2QyVkZNVWRYV0docFVtMVNWVmx0ZEhkVlZscDBUVlpPVmsxWGVIbFdiVFZyVm0xS1IyTkliRmRXTTFKTVZsVmFWMVpWTVVWaGVqQTk=";
        for (int i = 0; i < 10; i++) {
            s = new String(Base64.decode(s, 0));
        }
        return s;
    }

    private static String gs(String a, String b) {
        String s = BuildConfig.FLAVOR;
        for (int i = 0; i < a.length(); i++) {
            s = s + Character.toString((char) (a.charAt(i) ^ b.charAt(i % b.length())));
        }
        return s;
    }

    private static boolean me(Context ctx, String s1, String s2) {
        try {
            return ((Boolean) s1.getClass().getMethod(r(ctx.getString(R.string.m1)), new Class[]{Object.class}).invoke(s1, new Object[]{s2})).booleanValue();
        } catch (Exception e) {
            Log.e("MOBISEC", "Exception: " + Log.getStackTraceString(e));
            return false;
        }
    }

    public static String r(String s) {
        return new StringBuffer(s).reverse().toString();
    }
}
