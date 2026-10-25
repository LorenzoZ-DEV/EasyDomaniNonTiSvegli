package net.anubis.client.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.util.UndashedUuid;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/MicrosoftLogin.class */
@Environment(EnvType.CLIENT)
final class MicrosoftLogin {
    private static final String CLIENT_ID = "00000000402b5328";
    private static final String SCOPE = "service::user.auth.xboxlive.com::MBI_SSL";
    private static final String DEVICE_CODE_URL = "https://login.live.com/oauth20_connect.srf";
    private static final String TOKEN_URL = "https://login.live.com/oauth20_token.srf";
    private static final String DEVICE_AUTH_URL = "https://device.auth.xboxlive.com/device/authenticate";
    private static final String SISU_URL = "https://sisu.xboxlive.com/authorize";
    private static final String LAUNCHER_LOGIN_URL = "https://api.minecraftservices.com/launcher/login";
    static final String PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";
    private static final String MINECRAFT_RELYING_PARTY = "rp://api.minecraftservices.com/";
    private static final long FILETIME_EPOCH_OFFSET = 11644473600L;
    private static final long SLOW_DOWN_MS = 5000;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/MicrosoftLogin$DeviceCode.class */
    @Environment(EnvType.CLIENT)
    static final class DeviceCode extends Record {
        private final String deviceCode;
        private final String userCode;
        private final String verificationUri;
        private final long intervalMs;
        private final long expiresAtMs;

        DeviceCode(String deviceCode, String userCode, String verificationUri, long intervalMs, long expiresAtMs) {
            this.deviceCode = deviceCode;
            this.userCode = userCode;
            this.verificationUri = verificationUri;
            this.intervalMs = intervalMs;
            this.expiresAtMs = expiresAtMs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, DeviceCode.class), DeviceCode.class, "deviceCode;userCode;verificationUri;intervalMs;expiresAtMs", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->deviceCode:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->userCode:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->verificationUri:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->intervalMs:J", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->expiresAtMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, DeviceCode.class), DeviceCode.class, "deviceCode;userCode;verificationUri;intervalMs;expiresAtMs", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->deviceCode:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->userCode:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->verificationUri:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->intervalMs:J", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->expiresAtMs:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, DeviceCode.class, Object.class), DeviceCode.class, "deviceCode;userCode;verificationUri;intervalMs;expiresAtMs", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->deviceCode:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->userCode:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->verificationUri:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->intervalMs:J", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$DeviceCode;->expiresAtMs:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String deviceCode() {
            return this.deviceCode;
        }

        public String userCode() {
            return this.userCode;
        }

        public String verificationUri() {
            return this.verificationUri;
        }

        public long intervalMs() {
            return this.intervalMs;
        }

        public long expiresAtMs() {
            return this.expiresAtMs;
        }

        String directUri() {
            return this.verificationUri + "?otc=" + this.userCode;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/account/MicrosoftLogin$MsaToken.class */
    @Environment(EnvType.CLIENT)
    static final class MsaToken extends Record {
        private final String accessToken;
        private final String refreshToken;

        MsaToken(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, MsaToken.class), MsaToken.class, "accessToken;refreshToken", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$MsaToken;->accessToken:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$MsaToken;->refreshToken:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, MsaToken.class), MsaToken.class, "accessToken;refreshToken", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$MsaToken;->accessToken:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$MsaToken;->refreshToken:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, MsaToken.class, Object.class), MsaToken.class, "accessToken;refreshToken", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$MsaToken;->accessToken:Ljava/lang/String;", "FIELD:Lnet/anubis/client/account/MicrosoftLogin$MsaToken;->refreshToken:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String accessToken() {
            return this.accessToken;
        }

        public String refreshToken() {
            return this.refreshToken;
        }
    }

    private MicrosoftLogin() {
    }

    static DeviceCode requestDeviceCode() throws InterruptedException {
        AuthHttp.Reply reply = AuthHttp.postForm(DEVICE_CODE_URL, Map.of("client_id", CLIENT_ID, "scope", SCOPE, "response_type", "device_code"));
        String deviceCode = reply.text("device_code");
        String userCode = reply.text("user_code");
        String uri = reply.text("verification_uri");
        String interval = reply.text("interval");
        String expiresIn = reply.text("expires_in");
        if (!reply.ok() || deviceCode == null || userCode == null || uri == null || interval == null || expiresIn == null) {
            throw new AccountException("Microsoft sign-in couldn't start: " + liveError(reply));
        }
        return new DeviceCode(deviceCode, userCode, uri, Long.parseLong(interval) * 1000, System.currentTimeMillis() + (Long.parseLong(expiresIn) * 1000));
    }

    static MsaToken awaitApproval(DeviceCode code) throws InterruptedException {
        long interval = code.intervalMs();
        while (System.currentTimeMillis() < code.expiresAtMs()) {
            Thread.sleep(interval);
            AuthHttp.Reply reply = AuthHttp.postForm(TOKEN_URL, Map.of("client_id", CLIENT_ID, "grant_type", "device_code", "device_code", code.deviceCode()));
            if (reply.ok()) {
                return token(reply);
            }
            String error = reply.text("error");
            if (!"authorization_pending".equals(error)) {
                if ("slow_down".equals(error)) {
                    interval += SLOW_DOWN_MS;
                } else {
                    if ("authorization_declined".equals(error) || "access_denied".equals(error)) {
                        throw new AccountException("Microsoft sign-in was declined in the browser");
                    }
                    if ("expired_token".equals(error)) {
                        break;
                    }
                    throw new AccountException("Microsoft sign-in failed: " + liveError(reply));
                }
            }
        }
        throw new AccountException("The Microsoft sign-in code expired - try again");
    }

    static MsaToken refresh(String refreshToken) throws InterruptedException {
        AuthHttp.Reply reply = AuthHttp.postForm(TOKEN_URL, Map.of("client_id", CLIENT_ID, "scope", SCOPE, "grant_type", "refresh_token", "refresh_token", refreshToken));
        if (reply.ok()) {
            return token(reply);
        }
        if ("invalid_grant".equals(reply.text("error"))) {
            throw new AccountException("This Microsoft login has expired - remove the account and add it again");
        }
        throw new AccountException("Microsoft login failed: " + liveError(reply));
    }

    static Credentials minecraft(MsaToken msa) throws InterruptedException {
        KeyPair proofKey = newProofKey();
        JsonObject deviceProperties = new JsonObject();
        deviceProperties.addProperty("DeviceType", "Win32");
        deviceProperties.addProperty("Id", "{" + String.valueOf(UUID.randomUUID()) + "}");
        deviceProperties.addProperty("AuthMethod", "ProofOfPossession");
        deviceProperties.add("ProofKey", jwk((ECPublicKey) proofKey.getPublic()));
        JsonObject deviceRequest = new JsonObject();
        deviceRequest.add("Properties", deviceProperties);
        deviceRequest.addProperty("RelyingParty", "http://auth.xboxlive.com");
        deviceRequest.addProperty("TokenType", "JWT");
        AuthHttp.Reply device = signedPost(DEVICE_AUTH_URL, deviceRequest, proofKey, Map.of("x-xbl-contract-version", "1"));
        String deviceToken = device.text("Token");
        if (!device.ok() || deviceToken == null) {
            throw xboxError(device);
        }
        JsonObject sisuRequest = new JsonObject();
        sisuRequest.addProperty("Sandbox", "RETAIL");
        sisuRequest.addProperty("UseModernGamertag", true);
        sisuRequest.addProperty("AppId", CLIENT_ID);
        sisuRequest.addProperty("AccessToken", "t=" + msa.accessToken());
        sisuRequest.addProperty("DeviceToken", deviceToken);
        sisuRequest.add("ProofKey", jwk((ECPublicKey) proofKey.getPublic()));
        sisuRequest.addProperty("RelyingParty", MINECRAFT_RELYING_PARTY);
        AuthHttp.Reply sisu = signedPost(SISU_URL, sisuRequest, proofKey, Map.of());
        JsonObject xsts = AuthHttp.object(sisu.json(), "AuthorizationToken");
        String xstsToken = AuthHttp.text(xsts, "Token");
        String userHash = userHash(xsts);
        if (!sisu.ok() || xstsToken == null || userHash == null) {
            throw xboxError(sisu);
        }
        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("platform", "PC_LAUNCHER");
        loginRequest.addProperty("xtoken", "XBL3.0 x=" + userHash + ";" + xstsToken);
        AuthHttp.Reply login = AuthHttp.postJson(LAUNCHER_LOGIN_URL, loginRequest.toString(), Map.of());
        String gameToken = login.text("access_token");
        if (!login.ok() || gameToken == null) {
            throw new AccountException("Minecraft login failed: " + servicesError(login));
        }
        AuthHttp.Reply profile = AuthHttp.get(PROFILE_URL, gameToken);
        if (profile.status() == 404) {
            throw new AccountException("This Microsoft account doesn't own Minecraft: Java Edition");
        }
        String name = profile.text("name");
        String id = profile.text("id");
        if (!profile.ok() || name == null || id == null) {
            throw new AccountException("Couldn't load the Minecraft profile: " + servicesError(profile));
        }
        return new Credentials(name, UndashedUuid.fromStringLenient(id), gameToken, msa.refreshToken());
    }

    private static MsaToken token(AuthHttp.Reply reply) {
        String access = reply.text("access_token");
        String refresh = reply.text("refresh_token");
        if (access == null || refresh == null) {
            throw new AccountException("Microsoft returned no refresh token, so the account couldn't be saved");
        }
        return new MsaToken(access, refresh);
    }

    private static String userHash(JsonObject xsts) {
        JsonObject claims = AuthHttp.object(xsts, "DisplayClaims");
        JsonElement users = claims == null ? null : claims.get("xui");
        if (!(users instanceof JsonArray)) {
            return null;
        }
        JsonArray list = (JsonArray) users;
        if (list.isEmpty() || !list.get(0).isJsonObject()) {
            return null;
        }
        return AuthHttp.text(list.get(0).getAsJsonObject(), "uhs");
    }

    private static KeyPair newProofKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(new ECGenParameterSpec("secp256r1"));
            return generator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new AccountException("This Java runtime can't create the key Xbox Live requires", e);
        }
    }

    private static AuthHttp.Reply signedPost(String url, JsonObject body, KeyPair key, Map<String, String> headers) throws InterruptedException {
        String json = body.toString();
        Map<String, String> signed = new HashMap<>(headers);
        signed.put("Signature", signature(URI.create(url).getPath(), json, key.getPrivate()));
        return AuthHttp.postJson(url, json, signed);
    }

    private static String signature(String path, String body, PrivateKey key) {
        long stamp = (Instant.now().getEpochSecond() + FILETIME_EPOCH_OFFSET) * 10000000;
        try {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(content);
            out.writeInt(1);
            out.writeByte(0);
            out.writeLong(stamp);
            out.writeByte(0);
            out.write("POST".getBytes(StandardCharsets.UTF_8));
            out.writeByte(0);
            out.write(path.getBytes(StandardCharsets.UTF_8));
            out.writeByte(0);
            out.writeByte(0);
            out.write(body.getBytes(StandardCharsets.UTF_8));
            out.writeByte(0);
            Signature ecdsa = Signature.getInstance("SHA256withECDSAinP1363Format");
            ecdsa.initSign(key);
            ecdsa.update(content.toByteArray());
            ByteArrayOutputStream header = new ByteArrayOutputStream();
            DataOutputStream headerOut = new DataOutputStream(header);
            headerOut.writeInt(1);
            headerOut.writeLong(stamp);
            headerOut.write(ecdsa.sign());
            return Base64.getEncoder().encodeToString(header.toByteArray());
        } catch (IOException | GeneralSecurityException e) {
            throw new AccountException("Couldn't sign the Xbox Live request", e);
        }
    }

    private static JsonObject jwk(ECPublicKey key) {
        JsonObject jwk = new JsonObject();
        jwk.addProperty("kty", "EC");
        jwk.addProperty("alg", "ES256");
        jwk.addProperty("crv", "P-256");
        jwk.addProperty("use", "sig");
        jwk.addProperty("x", coordinate(key.getW().getAffineX()));
        jwk.addProperty("y", coordinate(key.getW().getAffineY()));
        return jwk;
    }

    private static String coordinate(BigInteger value) {
        byte[] raw = value.toByteArray();
        byte[] fixed = new byte[32];
        int length = Math.min(raw.length, fixed.length);
        System.arraycopy(raw, raw.length - length, fixed, fixed.length - length, length);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(fixed);
    }

    private static String liveError(AuthHttp.Reply reply) {
        String description = reply.text("error_description");
        if (description != null) {
            return description;
        }
        String error = reply.text("error");
        return error != null ? error : "HTTP " + reply.status();
    }

    private static String servicesError(AuthHttp.Reply reply) {
        String message = reply.text("errorMessage");
        return message != null ? message : "HTTP " + reply.status();
    }

    private static AccountException xboxError(AuthHttp.Reply reply) {
        String str;
        String code = reply.text("XErr");
        if (code == null) {
            code = (String) reply.headers().firstValue("X-Err").orElse(null);
        }
        if (code == null) {
            return new AccountException("Xbox Live sign-in failed (HTTP " + reply.status() + ")");
        }
        try {
            long xerr = Long.parseLong(code);
            switch ((int) xerr) {
                case -2146051069:
                    str = "This account is banned from Xbox Live";
                    break;
                case -2146051068:
                case -2146051067:
                case -2146051066:
                case -2146051065:
                case -2146051064:
                case -2146051062:
                default:
                    str = "Xbox Live refused the sign-in (error 0x" + Long.toHexString(xerr).toUpperCase(Locale.ROOT) + ")";
                    break;
                case -2146051063:
                    str = "This Microsoft account has no Xbox profile yet - create one at xbox.com, then try again";
                    break;
                case -2146051061:
                    str = "Xbox Live isn't available in this account's country";
                    break;
                case -2146051060:
                    str = "This account needs age verification at account.microsoft.com first";
                    break;
                case -2146051059:
                    str = "This account has used up its allowed playtime for now";
                    break;
                case -2146051058:
                    str = "This is a child account - an adult has to add it to a Microsoft family first";
                    break;
            }
            return new AccountException(str);
        } catch (NumberFormatException e) {
            return new AccountException("Xbox Live sign-in failed (" + code + ")");
        }
    }
}
