package aml.graph;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * This class extends from OutputStream to redirect output to a JTextArea
 *
 * @author www.codejava.net
 *
 */
public class MyOutputStream extends OutputStream {

    //JTextArea to write
    private final JTextArea textArea;

    /**
     * Create new instance of MyOutputStream 
     * @param textArea to write the stream of data
     */
    public MyOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        final String text = new String(buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(text);
            }
        });
    }

    @Override
    public void write(int i) throws IOException {
        write(new byte[]{(byte) i}, 0, 1);
    }

}
