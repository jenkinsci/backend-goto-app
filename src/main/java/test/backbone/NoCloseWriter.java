package test.backbone;

import org.apache.commons.io.output.ProxyWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Kohsuke Kawaguchi
 */
public class NoCloseWriter extends ProxyWriter {
    public NoCloseWriter(Writer proxy) {
        super(proxy);
    }

    @Override
    public void close() throws IOException {
        // nope
    }
}
