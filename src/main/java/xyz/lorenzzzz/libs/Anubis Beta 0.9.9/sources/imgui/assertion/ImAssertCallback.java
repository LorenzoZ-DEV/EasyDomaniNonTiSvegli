package imgui.assertion;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/assertion/ImAssertCallback.class */
public abstract class ImAssertCallback {
    public abstract void imAssertCallback(String str, int i, String str2);

    public void imAssert(String assertion, int line, String file) {
        try {
            imAssertCallback(assertion, line, file);
        } catch (Exception ex) {
            System.err.println("WARNING: Exception thrown in Dear ImGui Assertion Callback!");
            System.err.println("Dear ImGui Assertion Failed: " + assertion);
            System.err.println("Assertion Located At: " + file + ":" + line);
            ex.printStackTrace();
        }
        System.exit(1);
    }
}
