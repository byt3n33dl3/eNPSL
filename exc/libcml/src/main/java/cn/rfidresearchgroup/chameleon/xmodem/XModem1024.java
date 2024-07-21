package cn.rfidresearchgroup.chameleon.xmodem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XModem1024 extends AbstractXModem {

    //起始头!
    private byte STX = 0x02;

    public XModem1024(InputStream input, OutputStream output) {
        super(input, output);
    }

    @Override
    public boolean send(InputStream sources) throws IOException {
        return false;
    }

    @Override
    public boolean recv(OutputStream target) throws IOException {
        return false;
    }
}
